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
                .min((element1, element2) -> {
                    var cacheElement1 = Optional.ofNullable(element1.getValue().get());
                    var cacheElement2 = Optional.ofNullable(element2.getValue().get());
                    return ((Long) (cacheElement1.isPresent() ? cacheElement1.get().usingTime() : 0))
                            .compareTo((cacheElement2.isPresent() ? cacheElement2.get().usingTime() : 0));
                });
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
        CacheElement<V> cacheElement = new CacheElement<>(element);
        elements.put(key, new SoftReference<>(cacheElement));

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
        Optional<SoftReference<CacheElement<V>>> softRef = Optional.ofNullable(elements.get(key));

        Optional<CacheElement<V>> cacheElement = softRef.map(it ->  {
            var data = Optional.ofNullable(it);
            return data.isPresent() ? data.get().get() : null;
        });
        cacheElement.ifPresentOrElse(
                (it) -> {
                    hit++;
                    it.setAccessed();
                    System.out.println("Get element from cache " + it.value());
                },
                ()-> miss++);

        return Optional.ofNullable(cacheElement.isPresent() ? cacheElement.get().value() : null);
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
