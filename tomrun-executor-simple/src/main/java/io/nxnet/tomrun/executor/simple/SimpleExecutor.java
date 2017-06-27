package io.nxnet.tomrun.executor.simple;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nxnet.tomrun.executor.Executor;
import io.nxnet.tomrun.executor.ExecutorException;
import io.nxnet.tomrun.executor.ExecutorInfo;

public class SimpleExecutor extends Executor
{
    private final Logger logger;

    private final ThreadLocal<Integer> runNumberThreadLocal;
    
    private final String hostName;

    private final String pid;

    private final String threadName;

    public SimpleExecutor()
    {
        this(LoggerFactory.getLogger(DEFAULT_LOGGER_NAME));
    }

    public SimpleExecutor(Logger logger)
    {
        try
        {
            this.hostName = InetAddress.getLocalHost().getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            throw new ExceptionInInitializerError(e);
        }
        this.pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        this.threadName = Thread.currentThread().getName();
        this.logger = logger;
        this.runNumberThreadLocal = new ThreadLocal<Integer>();
        this.runNumberThreadLocal.set(new Integer(0));
    }
    
    @Override
    public ExecutorInfo getInfo()
    {
        return SimpleExecutorInfo.builder()
                .withHostName(this.hostName)
                .withPid(this.pid)
                .withThreadName(this.threadName)
                .withRunNumber(this.runNumberThreadLocal.get())
                .build();
    }

    @Override
    public Logger getLogger()
    {
        return this.logger;
    }

    @Override
    public int run() throws ExecutorException
    {
        this.incrementRunNumber();
        // Do something smart here
        return this.runNumberThreadLocal.get();
    }

    private void incrementRunNumber()
    {
        Integer runNumber = this.runNumberThreadLocal.get();
        if (runNumber == null)
        {
            runNumber = new Integer(0);
        }
        this.runNumberThreadLocal.set(runNumber + 1);
    }
}
