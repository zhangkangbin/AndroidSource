```java
public final class AsyncLayoutInflater {
    private static final String TAG = "AsyncLayoutInflater";

    LayoutInflater mInflater;
    Handler mHandler;
    InflateThread mInflateThread;

    public AsyncLayoutInflater(@NonNull Context context) {
        mInflater = new BasicInflater(context);
        //主要是处理回调
        mHandler = new Handler(mHandlerCallback);
        //处理布局线程
        mInflateThread = InflateThread.getInstance();
    }

    @UiThread
    public void inflate(@LayoutRes int resid, @Nullable ViewGroup parent,
            @NonNull OnInflateFinishedListener callback) {
        if (callback == null) {
            throw new NullPointerException("callback argument may not be null!");
        }
        InflateRequest request = mInflateThread.obtainRequest();
        request.inflater = this;
        request.resid = resid;
        request.parent = parent;
        request.callback = callback;
        //执行请求
        mInflateThread.enqueue(request);
    }

    /**
    * 回调监听。
    * */
    private Callback mHandlerCallback = new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            InflateRequest request = (InflateRequest) msg.obj;
            if (request.view == null) {
				//解析布局，xml就靠它构建成一个view的。
                request.view = mInflater.inflate(
                        request.resid, request.parent, false);
            }
            request.callback.onInflateFinished(
                    request.view, request.resid, request.parent);
            mInflateThread.releaseRequest(request);
            return true;
        }
    };

    public interface OnInflateFinishedListener {
        void onInflateFinished(@NonNull View view, @LayoutRes int resid,
                @Nullable ViewGroup parent);
    }

    private static class InflateRequest {
        AsyncLayoutInflater inflater;
        ViewGroup parent;
        //布局id
        int resid;
        //生成的view
        View view;
        //回调
        OnInflateFinishedListener callback;

        InflateRequest() {
        }
    }

    private static class BasicInflater extends LayoutInflater {
        private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app."
        };

        BasicInflater(Context context) {
            super(context);
        }

        @Override
        public LayoutInflater cloneInContext(Context newContext) {
            return new BasicInflater(newContext);
        }

        @Override
        protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
            for (String prefix : sClassPrefixList) {
                try {
                    //里面搜索.?有点疑惑？
                    // 优先去"android.widget.",android.webkit.",android.app.
                    // 因为大部分布局都在这里面 比如 button edittext textview？？
                    View view = createView(name, prefix, attrs);
                    if (view != null) {
                        return view;
                    }
                } catch (ClassNotFoundException e) {
                    // In this case we want to let the base class take a crack
                    // at it.
                }
            }

           //布局内视图不在以上三个包内，则调用父类方法构建
            return super.onCreateView(name, attrs);
        }
    }

    private static class InflateThread extends Thread {
        private static final InflateThread sInstance;
        static {
            sInstance = new InflateThread();
            sInstance.start();
        }

        public static InflateThread getInstance() {
            return sInstance;
        }

        private ArrayBlockingQueue<InflateRequest> mQueue = new ArrayBlockingQueue<>(10);
        private SynchronizedPool<InflateRequest> mRequestPool = new SynchronizedPool<>(10);

        // Extracted to its own method to ensure locals have a constrained liveness
        // scope by the GC. This is needed to avoid keeping previous request references
        // alive for an indeterminate amount of time, see b/33158143 for details
        public void runInner() {
            InflateRequest request;
            try {
                //取出一个请求，这里ArrayBlockingQueue是一个阻塞式的队列.
                request = mQueue.take();
            } catch (InterruptedException ex) {
                // Odd, just continue
                Log.w(TAG, ex);
                return;
            }

            try {
                //解析布局，返回一个view.xml就靠它构建成一个view的。
                request.view = request.inflater.mInflater.inflate(
                        request.resid, request.parent, false);
            } catch (RuntimeException ex) {
                // Probably a Looper failure, retry on the UI thread
                Log.w(TAG, "Failed to inflate resource in the background! Retrying on the UI"
                        + " thread", ex);
            }
            //布局解析完成，发送一个消息。
            Message.obtain(request.inflater.mHandler, 0, request)
                    .sendToTarget();
        }

        @Override
        public void run() {
            //循环执行 上面的，runInner()
            while (true) {
                runInner();
            }
        }

        public InflateRequest obtainRequest() {
            //从对象缓存池取出一个对象
            InflateRequest obj = mRequestPool.acquire();
            //如果为空，新建一个。 为空情况是1，是没有对象数组里面。2，缓存池子对象，被其他线程占用了。
            if (obj == null) {
                obj = new InflateRequest();
            }
            return obj;
        }

        public void releaseRequest(InflateRequest obj) {
			//做一下清洁
            obj.callback = null;
            obj.inflater = null;
            obj.parent = null;
            obj.resid = 0;
            obj.view = null;
            //把对象缓存起来。提示：如果对象存在数组里面抛出异常。防止重复缓存对象。
            mRequestPool.release(obj);
        }

        public void enqueue(InflateRequest request) {
            try {
                //把请求添加到队列里面
                mQueue.put(request);
            } catch (InterruptedException e) {
                throw new RuntimeException(
                        "Failed to enqueue async inflate request", e);
            }
        }
    }
}
```
