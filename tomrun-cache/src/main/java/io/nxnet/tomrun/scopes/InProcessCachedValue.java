package io.nxnet.tomrun.scopes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InProcessCachedValue<I, V> implements CachedValue<I, V> {

    protected Map<I, V> cacheValues;

    public InProcessCachedValue() 
    {
        this.cacheValues = new ConcurrentHashMap<I, V>();
    }

    @Override
    public V getValue(CacheEntryIdentifier<I> cacheEntryIdentifier,
            ValueInitializationStrategy<V> initializationStrategy) throws CacheException
    {
        // Get scoped cache key
        I scopeId = cacheEntryIdentifier.getScopeId();

        // Get cached value
        V cachedValue = this.cacheValues.get(scopeId);

        // If no value in cache is found
        if (cachedValue == null)
        {
            // Resolve and cache value for new scope
            try
            {
                cachedValue = this.cacheValue(scopeId, initializationStrategy);
            }
            catch (ValueInitializationException e)
            {
                throw new CacheException(e);
            }
        }

        // Get cached value by process id
        return cachedValue;
    }

    synchronized V cacheValue(I scopeId, ValueInitializationStrategy<V> initializationStrategy) 
            throws ValueInitializationException
    {
        // Check if value is already in cache
        if (!this.cacheValues.containsKey(scopeId))
        {
            // Cache value for new scope
            V valueCandidate = initializationStrategy.initializeValue();
            this.cacheValues.put(scopeId, valueCandidate);
            return valueCandidate;
        }
        // If value is already in cache
        else
        {
            // Return value in cache
            return this.cacheValues.get(scopeId);
        }
    }

}
