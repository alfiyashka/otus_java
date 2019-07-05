package cache;

public interface CacheEngine <K, V> {

    void put(K key, V element);

    V get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();
}
