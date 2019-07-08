package cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;


public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {
    private static final int TIME_THRESHOLD_MS = 5;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, SoftReference<CacheElement<V>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    private CacheElement<V> remove(K key) {
        SoftReference softRef = elements.remove(key);
        if (softRef == null) {
            return null;
        }
        CacheElement oldValue = (CacheElement)softRef.get();
        softRef.clear();

        return oldValue;
    }

    private void removeOldUsedOrFirst() {
        Optional<Map.Entry<K, SoftReference<CacheElement<V>>>> oldUsedCacheElement = elements.entrySet()
                .stream()
                .min((element1, element2) ->
                        ((Long)element1.getValue().get().usingTime())
                                .compareTo(element2.getValue().get().usingTime())
                );
        if (oldUsedCacheElement.isPresent()) {
            K key = oldUsedCacheElement.get().getKey();
            System.out.println("Removed old used element with key " + key);
            remove(key);
        } else {
            K firstKey = elements.keySet().iterator().next();
            System.out.println("Removed first used element with key " + firstKey);
            remove(firstKey);
        }
    }

    public void put(K key, V element) {
        if (elements.size() == maxElements) {
            removeOldUsedOrFirst();
        }
        CacheElement cacheElement = new CacheElement<>(element);
        elements.put(key, new SoftReference(cacheElement));

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement ->
                        lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement ->
                        idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    public Optional<V> get(K key) {
        SoftReference softRef = elements.get(key);

        if (softRef == null) {
            miss++;
            return Optional.ofNullable(null);
        }
        CacheElement<V> element = (CacheElement)softRef.get();

        if (element != null) {
            hit++;
            element.setAccessed();
            System.out.println("Get element from cache " + element.value());
        } else {
            miss++;
        }
        return Optional.ofNullable(element.value());
    }

    public int getHitCount() {
        return hit;
    }

    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(final K key, Function<CacheElement<V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                CacheElement <V> element = elements.get(key).get();
                if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                    elements.remove(key);
                    this.cancel();
                }
            }
        };
    }


    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
