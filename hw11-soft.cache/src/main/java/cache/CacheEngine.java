package cache;

import java.util.Optional;

public interface CacheEngine <K, V> {

    void put(K key, V element);

    Optional<V> get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();
}
