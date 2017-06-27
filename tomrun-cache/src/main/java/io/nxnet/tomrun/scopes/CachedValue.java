package io.nxnet.tomrun.scopes;

public interface CachedValue<I, V>
{
    V getValue(CacheEntryIdentifier<I> cacheEntryIdentifier, ValueInitializationStrategy<V> initializationStrategy)
            throws CacheException;
}
