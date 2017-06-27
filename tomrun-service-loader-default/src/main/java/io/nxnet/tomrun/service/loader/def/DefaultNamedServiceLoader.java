package io.nxnet.tomrun.service.loader.def;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.nxnet.tomrun.service.loader.NamedService;
import io.nxnet.tomrun.service.loader.NamedServiceLoader;

public class DefaultNamedServiceLoader extends NamedServiceLoader
{
    private final Map<CacheKey<? extends NamedService>, NamedService> _SERVICE_CACHE = 
            new ConcurrentHashMap<DefaultNamedServiceLoader.CacheKey<? extends NamedService>, NamedService>();

    /**
     * Instantiate service using provided factory loader.
     * 
     * @return
     */
    public <S extends NamedService> S loadService(String serviceName, Class<S> serviceClass, ClassLoader classLoader)
    {
        NamedService cachedService = _SERVICE_CACHE.get(new CacheKey<S>(serviceName, serviceClass, classLoader));
        if (cachedService != null)
        {
            return (S)cachedService;
        }

        return loadServiceInternal(serviceName, serviceClass, classLoader);
    }

    private <S extends NamedService> S loadServiceInternal(String serviceName, Class<S> serviceClass, ClassLoader classLoader)
    {
        // Validate args
        if (serviceName == null)
        {
            throw new IllegalArgumentException("service name argument can't be null");
        }
        if (serviceClass == null)
        {
            throw new IllegalArgumentException("service class argument can't be null");
        }
        if (classLoader == null)
        {
            throw new IllegalArgumentException("class loader argument can't be null");
        }

        // Initialize matching service
        S serviceInstance = null;

        // Load services
        java.util.ServiceLoader<S> serviceLoader = java.util.ServiceLoader.load(serviceClass, classLoader);

        // Search for appropriate service
        Iterator<S> serviceIter = serviceLoader.iterator();
        S serviceInstanceCandidate = null;
        while (serviceIter.hasNext())
        {
            serviceInstanceCandidate = serviceIter.next();
            if (serviceName.equals(serviceInstanceCandidate.getServiceName()))
            {
                if (serviceInstance != null)
                {
                    // If we already found appropriate service instance
                    throw new IllegalStateException(MessageFormat.format(
                            "Umbiguous resolution of {0} service named {1}, multiple services found.", 
                            serviceClass.getName(), serviceName));
                }

                // Use service candidate as preferred service instance
                serviceInstance = pickService(serviceInstanceCandidate, 
                        new CacheKey<S>(serviceName, serviceClass, classLoader));
            }
        }

        // Throw exception if service is not found
        if (serviceInstance == null)
        {
            throw new IllegalStateException(MessageFormat.format("No matching {0} service named {1} found!", 
                    serviceClass.getName(), serviceName));
        }

        return serviceInstance;
    }

    private synchronized <S extends NamedService> S pickService(S serviceCandidate, CacheKey<S> cacheKey)
    {
        NamedService cachedService = _SERVICE_CACHE.get(cacheKey);
        if (cachedService == null)
        {
            _SERVICE_CACHE.put(cacheKey, serviceCandidate);
            return serviceCandidate;
        }

        return (S)cachedService;
    }

    private class CacheKey<S> {

        private String serviceName; 

        private Class<S> serviceClass; 

        private ClassLoader classLoader;

        public CacheKey(String serviceName, Class<S> serviceClass, ClassLoader classLoader)
        {
            this.serviceName = serviceName;
            this.serviceClass = serviceClass;
            this.classLoader = classLoader;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((classLoader == null) ? 0 : classLoader.hashCode());
            result = prime * result + ((serviceClass == null) ? 0 : serviceClass.hashCode());
            result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CacheKey other = (CacheKey) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (classLoader == null)
            {
                if (other.classLoader != null)
                    return false;
            }
            else if (!classLoader.equals(other.classLoader))
                return false;
            if (serviceClass == null)
            {
                if (other.serviceClass != null)
                    return false;
            }
            else if (!serviceClass.equals(other.serviceClass))
                return false;
            if (serviceName == null)
            {
                if (other.serviceName != null)
                    return false;
            }
            else if (!serviceName.equals(other.serviceName))
                return false;
            return true;
        }

        private DefaultNamedServiceLoader getOuterType()
        {
            return DefaultNamedServiceLoader.this;
        }

        
    }
}
