package de.maxhenkel.corelib;

import java.util.function.Supplier;

/**
 * A utility to cache values
 * @param <T> the type of the cached value
 */
public class CachedValue<T> {

    private T value;
    private Supplier<T> supplier;

    public CachedValue(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Gets the cached value or gets it from the supplier if it wasn't called before or has been invalidated
     * @return the value
     */
    public T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }

    /**
     * Invalidates the cached value
     * The value gets cached again if {@link #get()} is called
     */
    public void invalidate() {
        value = null;
    }

}
