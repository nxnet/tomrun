package io.nxnet.tomrun.executor.simple;

import javax.annotation.Generated;

import io.nxnet.tomrun.executor.ExecutorInfo;

public class SimpleExecutorInfo implements ExecutorInfo
{
    private String hostName;

    private String pid;

    private String threadName;

    private int runNumber;

    @Generated("SparkTools")
    private SimpleExecutorInfo(Builder builder)
    {
        this.hostName = builder.hostName;
        this.pid = builder.pid;
        this.threadName = builder.threadName;
        this.runNumber = builder.runNumber;
    }

    @Override
    public String getHostName()
    {
        return this.hostName;
    }

    @Override
    public String getPid()
    {
        return this.pid;
    }

    @Override
    public String getThreadName()
    {
        return this.threadName;
    }

    @Override
    public int getRunNumber()
    {
        return this.runNumber;
    }

    /**
     * Creates builder to build {@link SimpleExecutorInfo}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link SimpleExecutorInfo}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private String hostName;
        private String pid;
        private String threadName;
        private int runNumber;

        private Builder()
        {
        }

        public Builder withHostName(String hostName)
        {
            this.hostName = hostName;
            return this;
        }

        public Builder withPid(String pid)
        {
            this.pid = pid;
            return this;
        }

        public Builder withThreadName(String threadName)
        {
            this.threadName = threadName;
            return this;
        }

        public Builder withRunNumber(int runNumber)
        {
            this.runNumber = runNumber;
            return this;
        }

        public SimpleExecutorInfo build()
        {
            return new SimpleExecutorInfo(this);
        }
    }

}
