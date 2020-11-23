```java
public static class SimplePool<T> implements Pool<T> {
        private final Object[] mPool;

        private int mPoolSize;

        /**
         * Creates a new instance.
         * 最大对象池size
         * @param maxPoolSize The max pool size.
         *
         * @throws IllegalArgumentException If the max pool size is less than zero.
         */
        public SimplePool(int maxPoolSize) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            mPool = new Object[maxPoolSize];
        }

        @Override
        @SuppressWarnings("unchecked")
        public T acquire() {
	//判断 mPoolSize ，判断对象池是否已经用完了。用完了返回null
            if (mPoolSize > 0) {
	         //取一个下标
                final int lastPooledIndex = mPoolSize - 1;
		 //取出一个对象
                T instance = (T) mPool[lastPooledIndex];
		//把这个对象从数组中移出（后面release会把它再加回来）觉得这里设计好巧妙。
                mPool[lastPooledIndex] = null;
		//下标减1，意思是已经拿走一个去使用了。
                mPoolSize--;
		//返回对象
                return instance;
            }
	    //对象池用完了，返回null，让上层自己去新建一个吧
            return null;
        }

        @Override
        public boolean release(@NonNull T instance) {
	    //主要防止一个对象重复保存到数组中，判断这个对象是否存在数组中，存在就抛出异常。因为acquire()时候已经把它移出了，还在就违规了，不能赖着不走。哈哈
            if (isInPool(instance)) {
                throw new IllegalStateException("Already in the pool!");
            }
	    //如果没有存满，缓存一个对象。
            if (mPoolSize < mPool.length) {
	        //把上面从数组中移出对象，再次保存回来。（移出保存是同一个哦）毕竟不是一次性的。
                mPool[mPoolSize] = instance;
		//池子大小加一
                mPoolSize++;
                return true;
            }
	    //存满了，返回false ,这个上层可以针对这个做对应逻辑。
            return false;
        }

        private boolean isInPool(@NonNull T instance) {
	    //查找这个对象是否存在数组中
            for (int i = 0; i < mPoolSize; i++) {
                if (mPool[i] == instance) {
                    return true;
                }
            }
            return false;
        }
    }

```