package task2_1.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import task2_1.Constants;

//LFU implementation
public class Cache {

    private final Map<String, CacheEntry> cache;
    private final LinkedHashSet[] frequencyList;
    private int lowFrequency;
    private int maxFrequency;
    private final int cacheSize;
    private final float eviction;

    public Cache(int cacheSize, float eviction) {
        if (eviction <= 0 || eviction >= 1) {
            throw new IllegalArgumentException(Constants.ERROR_EVICTION);
        }
        this.cache = new HashMap<String, CacheEntry>(cacheSize);
        this.frequencyList = new LinkedHashSet[cacheSize];
        this.lowFrequency = 0;
        this.maxFrequency = cacheSize - 1;
        this.cacheSize = cacheSize;
        this.eviction = eviction;
        initFrequencyList();
    }

    private void initFrequencyList() {
        for (int i = 0; i <= maxFrequency; i++) {
            frequencyList[i] = new LinkedHashSet<CacheEntry>();
        }
    }

    public int put(String key, int value) {
        int oldValue = Constants.NEGATIVE_NUMBER;
        CacheEntry currentEntry = cache.get(key);
        if (currentEntry == null) {
            if (cache.size() == cacheSize) {
                eviction();
            }
            currentEntry = new CacheEntry(key, value, Constants.DEFAULT_FREQUENCY);
            LinkedHashSet<CacheEntry> entries = frequencyList[Constants.INDEX_ZERO];
            entries.add(currentEntry);
            cache.put(key, currentEntry);
            lowFrequency = Constants.DEFAULT_LOW_FREQUENCY;
        } else {
            oldValue = currentEntry.getValue();
            currentEntry.setValue(value);
        }
        return oldValue;
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            sb.append(entry.getKey());
            sb.append(Constants.DELIMITER);
            sb.append(entry.getValue());
            sb.append(Constants.NEW_LINE);
        }
        System.out.println(sb);
        System.out.println();
    }

    public int get(String key) {
        CacheEntry currentEntry = cache.get(key);
        if (currentEntry != null) {
            int currentFrequency = currentEntry.getFrequency();
            if (currentFrequency < maxFrequency) {
                int nextFrequency = currentFrequency + 1;
                LinkedHashSet<CacheEntry> currentEntries = frequencyList[currentFrequency];
                LinkedHashSet<CacheEntry> newEntries = frequencyList[nextFrequency];
                moveToNextFrequency(currentEntry, nextFrequency, currentEntries, newEntries);
                cache.put(key, currentEntry);
                if (lowFrequency == currentFrequency && currentEntries.isEmpty()) {
                    lowFrequency = nextFrequency;
                }
            }
            return currentEntry.getValue();
        } else {
            return Constants.NEGATIVE_NUMBER;
        }
    }

    private void moveToNextFrequency(CacheEntry currentEntry, int nextFrequency, LinkedHashSet<CacheEntry> currentEntries, LinkedHashSet<CacheEntry> newEntries) {
        currentEntries.remove(currentEntry);
        newEntries.add(currentEntry);
        currentEntry.setFrequency(nextFrequency);
    }

    public int remove(String key) {
        CacheEntry currentEntry = cache.remove(key);
        if (currentEntry != null) {
            LinkedHashSet<CacheEntry> entries = frequencyList[currentEntry.getFrequency()];
            entries.remove(currentEntry);
            if (lowFrequency == currentEntry.getFrequency()) {
                findNextLowFrequency();
            }
            return currentEntry.getValue();
        } else {
            return Constants.NEGATIVE_NUMBER;
        }
    }

    public void clear() {
        for (int i = 0; i <= maxFrequency; i++) {
            frequencyList[i].clear();
        }
        cache.clear();
        lowFrequency = Constants.DEFAULT_LOW_FREQUENCY;
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return this.cache.isEmpty();
    }

    public boolean containsKey(String key) {
        return this.cache.containsKey(key);
    }

    private void eviction() {
        int currentDeleted = Constants.DEFAULT_CURRENT_DELETED;
        float target = cacheSize * eviction;
        while (currentDeleted < target) {
            LinkedHashSet<CacheEntry> entries = frequencyList[lowFrequency];
            if (entries.isEmpty()) {
                throw new IllegalStateException(Constants.ERROR_LOW_FREQUENCY);
            } else {
                Iterator<CacheEntry> it = entries.iterator();
                while (it.hasNext() && currentDeleted++ < target) {
                    CacheEntry entry = it.next();
                    it.remove();
                    cache.remove(entry.getKey());
                }
                if (!it.hasNext()) {
                    findNextLowFrequency();
                }
            }
        }
    }

    private void findNextLowFrequency() {
        while (lowFrequency <= maxFrequency && frequencyList[lowFrequency].isEmpty()) {
            lowFrequency++;
        }
        if (lowFrequency > maxFrequency) {
            lowFrequency = Constants.DEFAULT_LOW_FREQUENCY;
        }
    }

}
