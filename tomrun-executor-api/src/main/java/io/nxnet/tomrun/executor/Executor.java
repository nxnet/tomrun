package io.nxnet.tomrun.executor;

import org.slf4j.Logger;

import io.nxnet.tomrun.service.loader.NamedService;
import io.nxnet.tomrun.service.loader.NamedServiceLoader;

public abstract class Executor implements NamedService
{
    public static final String DEFAULT_LOGGER_NAME = Executor.class.getPackage().getName();

    public static Executor getExecutor()
    {
        return NamedServiceLoader.getInstance().loadService(Executor.class.getName(), Executor.class, 
                Thread.currentThread().getContextClassLoader());
    }

    public abstract ExecutorInfo getInfo();

    public abstract Logger getLogger();

    public abstract int run() throws ExecutorException;

    /* (non-Javadoc)
     * @see io.nxnet.tomrun.service.loader.NamedService#getServiceName()
     */
    @Override
    public String getServiceName()
    {
        return Executor.class.getName();
    } 

    
}
