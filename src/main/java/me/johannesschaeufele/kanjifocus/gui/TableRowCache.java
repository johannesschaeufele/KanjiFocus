package me.johannesschaeufele.kanjifocus.gui;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TableRowCache<T> {

    private final Object cacheSyncObject = new Object();

    private final Class<?> clazz;

    private boolean initialized = false;
    private int rowCount = -1;
    private int rowCountDouble = -1;

    private int rowStart = 0;
    private int rowFirst = 0;

    private T[] cache = null;

    public TableRowCache(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void clear() {
        synchronized(this.cacheSyncObject) {
            if(this.initialized) {
                this.initialized = false;
                this.cache = null;
            }
        }
    }

    public void initialize(int rowCount) {
        synchronized(this.cacheSyncObject) {
            this.initialized = true;

            this.rowCount = rowCount;

            this.rowCountDouble = 2 * this.rowCount - 1;

            if(this.cache == null || this.cache.length != this.rowCount) {
                this.cache = (T[]) Array.newInstance(this.clazz, this.rowCount);
            }
            else {
                Arrays.fill(this.cache, null);
            }
        }
    }

    private int getCacheRow(int row) {
        int index = row - this.rowFirst;
        int cacheRow;
        if(index >= 0) {
            cacheRow = (index + this.rowStart) % this.rowCount;
            if(index < this.rowCount) {
                // Cache hit
            }
            else if(index < this.rowCountDouble) {
                int firstIndex = rowStart;
                int lastIndex = cacheRow + 1;
                if(lastIndex < firstIndex) {
                    Arrays.fill(cache, firstIndex, cache.length, null);
                    Arrays.fill(cache, 0, lastIndex, null);
                }
                else {
                    Arrays.fill(cache, firstIndex, lastIndex, null);
                }
                this.rowStart = (cacheRow + 1) % this.rowCount;
                this.rowFirst = row - this.rowCount + 1;
            }
            else {
                Arrays.fill(this.cache, null);
                this.rowStart = Math.min(row, this.rowCount / 4);
                this.rowFirst = row;
                cacheRow = rowStart;
            }
        }
        else {
            index += this.rowCount;
            if(index < 0) {
                Arrays.fill(this.cache, null);
                this.rowStart = Math.min(row, this.rowCount / 4);
                this.rowFirst = row;
                cacheRow = rowStart;
            }
            else {
                cacheRow = (index + this.rowStart) % this.rowCount;
                int firstIndex = cacheRow;
                int lastIndex = rowStart;

                if(lastIndex < 0) {
                    lastIndex += cache.length;
                }
                if(lastIndex < firstIndex) {
                    Arrays.fill(cache, firstIndex, cache.length, null);
                    Arrays.fill(cache, 0, lastIndex, null);
                }
                else {
                    Arrays.fill(cache, firstIndex, lastIndex, null);
                }
                this.rowStart = cacheRow;
                this.rowFirst = row;
            }
        }

        return cacheRow;
    }

    public boolean hasCacheRow(int row) {
        int index = row - this.rowFirst;
        if(index >= 0) {
            return index < this.rowCount;
        }

        return false;
    }

    public T get(int row) {
        if(!this.initialized) {
            return null;
        }

        synchronized(this.cacheSyncObject) {
            return this.cache[getCacheRow(row)];
        }
    }

    public void set(int row, T t) {
        if(!this.initialized) {
            return;
        }

        synchronized(this.cacheSyncObject) {
            this.cache[getCacheRow(row)] = t;
        }

    }

}
