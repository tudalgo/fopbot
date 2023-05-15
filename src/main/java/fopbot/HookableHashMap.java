package fopbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A HashMap that can execute hooks when a value is put into it or removed from it.
 */
public class HookableHashMap<K, V> extends HashMap<K, V> {
    private final List<BiConsumer<HookableHashMap<K, V>, Entry<K, V>>> putHooks = new ArrayList<>();
    private final List<BiConsumer<HookableHashMap<K, V>, Entry<Object, V>>> removeHooks = new ArrayList<>();

    private HookableHashMap() {
        super();
    }

    /**
     * Adds a hook that is executed before a value is put into this map.
     *
     * @param hook the hook to add
     */
    protected void addPutHook(BiConsumer<HookableHashMap<K, V>, Entry<K, V>> hook) {
        putHooks.add(hook);
    }

    /**
     * Adds a hook that is executed after a value is removed from this map.
     *
     * @param hook the hook to add
     */
    protected void addRemoveHook(BiConsumer<HookableHashMap<K, V>, Entry<Object, V>> hook) {
        removeHooks.add(hook);
    }

    /**
     * Creates a new Builder for a HookableHashMap.
     *
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     */
    public static class Builder<K, V> {
        private final HookableHashMap<K, V> map = new HookableHashMap<>();

        /**
         * Adds a hook that is executed before a value is put into this map.
         *
         * @param hook the hook to add
         * @return this builder
         */
        public Builder<K, V> addPutHook(BiConsumer<HookableHashMap<K, V>, Entry<K, V>> hook) {
            map.addPutHook(hook);
            return this;
        }

        /**
         * Adds a hook that is executed after a value is removed from this map.
         *
         * @param hook the hook to add
         * @return this builder
         */
        public Builder<K, V> addRemoveHook(BiConsumer<HookableHashMap<K, V>, Entry<Object, V>> hook) {
            map.addRemoveHook(hook);
            return this;
        }

        /**
         * Builds the HookableHashMap.
         *
         * @return the built HookableHashMap
         */
        public HookableHashMap<K, V> build() {
            return map;
        }
    }

    @Override
    public V put(K key, V value) {
        putHooks.forEach(hook -> hook.accept(this, Map.entry(key, value)));
        return super.put(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        putHooks.forEach(hook -> hook.accept(this, Map.entry(key, value)));
        return super.putIfAbsent(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach((key, value) -> putHooks.forEach(hook -> hook.accept(this, Map.entry(key, value))));
        super.putAll(m);
    }

    @Override
    public V remove(Object key) {
        V result = super.remove(key);
        removeHooks.forEach(hook -> hook.accept(this, Map.entry(key, result)));
        return result;
    }
}
