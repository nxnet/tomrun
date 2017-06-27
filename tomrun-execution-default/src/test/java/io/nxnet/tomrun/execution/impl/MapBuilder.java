package io.nxnet.tomrun.execution.impl;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapBuilder<K, V>
{
    private Map<K, V> map;

    public MapBuilder()
    {
        this.map = new LinkedHashMap<K, V>();
    }

    public MapBuilder<K, V> put(K k, V v)
    {
        this.map.put(k, v);
        return this;
    }

    public Map<K, V> build()
    {
        return this.map;
    }
}
