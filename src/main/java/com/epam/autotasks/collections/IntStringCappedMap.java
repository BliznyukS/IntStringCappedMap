package com.epam.autotasks.collections;

import java.util.*;

class IntStringCappedMap extends AbstractMap<Integer, String> {

    private LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
    private final long capacity;
    public long currentCapacity;

    public IntStringCappedMap(final long capacity) {
        this.capacity = capacity;
        currentCapacity = capacity;
    }

    public long getCapacity() {
        return capacity;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<Entry<Integer, String>> iterator() {
                return new Iterator<>() {
                    Iterator<Entry<Integer, String>> value = map.entrySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return value.hasNext();
                    }

                    @Override
                    public Entry<Integer, String> next() {
                        if (value.hasNext()) {
                            return value.next();
                        } else {
                            throw new NoSuchElementException();
                        }
                    }
                };
            }

            @Override
            public int size() {
                return IntStringCappedMap.this.size();
            }
        };
    }

    @Override
    public String get(final Object key) {
        return map.get(key);
    }

    @Override
    public String put(final Integer key, final String value) {

        String oldValue = map.remove(key);

        if (oldValue != null) {
            currentCapacity = currentCapacity + oldValue.length();
        }
        if (currentCapacity == capacity) {
            System.out.println(key + " : " + value /*+ " // " + currentCapacity*/);
        }

        if (value.length() > capacity) {
            throw new IllegalArgumentException();
        } else if (value.length() > currentCapacity) {
            Iterator<Map.Entry<Integer, String>> iterator = entrySet().iterator();
            Map.Entry<Integer, String> entry;
            while (currentCapacity < value.length() && iterator.hasNext()) {
                entry = iterator.next();
                remove(entry.getKey());
            }
        }

        currentCapacity = currentCapacity - value.length();
        String newValue = map.put(key, value);
        return oldValue == null ? newValue : oldValue;//map.put(key, value);
    }

    @Override
    public String remove(final Object key) {
        if (map.containsKey(key)) {
            currentCapacity = currentCapacity + get(key).length();
        }
        return map.remove(key);
    }

    @Override
    public int size() {
        return map.size();
    }

}
