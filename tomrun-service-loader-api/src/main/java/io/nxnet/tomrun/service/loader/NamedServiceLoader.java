package io.nxnet.tomrun.service.loader;

import java.util.Iterator;

public abstract class NamedServiceLoader
{
    private static NamedServiceLoader _SINGLETON;

    /**
     * Instantiate service using some service loader mechanism.
     * 
     * @return
     */
    public abstract <S extends NamedService> S loadService(String serviceName, Class<S> serviceClass, 
            ClassLoader classLoader);

    public static NamedServiceLoader getInstance()
    {
        return getInstance(Thread.currentThread().getContextClassLoader());
    }

    public static NamedServiceLoader getInstance(ClassLoader classLoader)
    {
        if (_SINGLETON != null)
        {
            return _SINGLETON;
        }

        return getInstanceInternal(classLoader);
    }

    private static NamedServiceLoader getInstanceInternal(ClassLoader classLoader)
    {
        // Validate args
        if (classLoader == null)
        {
            throw new IllegalArgumentException("class loader argument can't be null");
        }

        // Initialize service loader
        java.util.ServiceLoader<NamedServiceLoader> namedServiceLoaderLoader = java.util.ServiceLoader
                .load(NamedServiceLoader.class, classLoader);

        // Initialize factory
        NamedServiceLoader namedServiceLoaderInstance = null;

        // Search for factories in classpath
        Iterator<NamedServiceLoader> namedServiceLoaderLoaderIter = namedServiceLoaderLoader.iterator();
        NamedServiceLoader namedServiceLoaderInstanceCandidate = null;
        while (namedServiceLoaderLoaderIter.hasNext())
        {
            namedServiceLoaderInstanceCandidate = namedServiceLoaderLoaderIter.next();
            if (namedServiceLoaderInstance != null)
            {
                // If we already found appropriate factory instance
                throw new IllegalStateException("Umbiguous service loader resolution, multiple service loaders found.");
            }

            // Use factory candidate as preferred factory instance
            namedServiceLoaderInstance = pickInstance(namedServiceLoaderInstanceCandidate);
        }

        // Throw exception if factory is not found
        if (namedServiceLoaderInstance == null)
        {
            throw new IllegalStateException("No service loader found!");
        }

        return namedServiceLoaderInstance;
    }

    private static synchronized NamedServiceLoader pickInstance(NamedServiceLoader instanceCandidate)
    {
        if (_SINGLETON == null)
        {
            _SINGLETON = instanceCandidate;
        }

        return _SINGLETON;
    }
}
