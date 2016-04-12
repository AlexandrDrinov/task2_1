package task2_1.cache;

public class CacheEntry {

    private final String key;

    private int value;

    private int frequency;

    public CacheEntry(String key, int value, int frequency) {
        this.key = key;
        this.value = value;
        this.frequency = frequency;
    }

    public String getKey() {
        return key;
    }
    
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int incFrequency() {
        return this.frequency++;
    }

    @Override
    public String toString() {
        return "CacheEntry{" + "key=" + key + ", value=" + value + ", frequency=" + frequency + '}';
    }
}
