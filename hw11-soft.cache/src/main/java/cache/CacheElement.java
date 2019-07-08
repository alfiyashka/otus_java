package cache;

public class CacheElement<T> {

    private final T value;
    private final long creationTime;
    private long lastAccessTime;

    public CacheElement(T value) {
        this.value = value;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public T value() {
        return value;
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public long usingTime() {
        return lastAccessTime - creationTime;
    }

}

