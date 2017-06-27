package io.nxnet.tomrun.property;

import javax.annotation.Generated;

import io.nxnet.tomrun.scopes.CacheEntryIdentifier;

public class RunContext implements CacheEntryIdentifier<RunContext>
{
    private String hostName;

    private String pid;

    private String threadName;

    private int runNumber;

    @Generated("SparkTools")
    private RunContext(Builder builder)
    {
        this.hostName = builder.hostName;
        this.pid = builder.pid;
        this.threadName = builder.threadName;
        this.runNumber = builder.runNumber;
    }

    @Override
    public RunContext getScopeId()
    {
        return this;
    }

    public String getHostName() 
    {
        return this.hostName;
    }

    public String getPid()
    {
        return this.pid;
    }

    public String getThreadName()
    {
        return this.threadName;
    }

    public int getRunNumber()
    {
        return this.runNumber;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
        result = prime * result + ((pid == null) ? 0 : pid.hashCode());
        result = prime * result + runNumber;
        result = prime * result + ((threadName == null) ? 0 : threadName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RunContext other = (RunContext) obj;
        if (hostName == null)
        {
            if (other.hostName != null)
                return false;
        }
        else if (!hostName.equals(other.hostName))
            return false;
        if (pid == null)
        {
            if (other.pid != null)
                return false;
        }
        else if (!pid.equals(other.pid))
            return false;
        if (runNumber != other.runNumber)
            return false;
        if (threadName == null)
        {
            if (other.threadName != null)
                return false;
        }
        else if (!threadName.equals(other.threadName))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder2 = new StringBuilder();
        builder2.append("RunContext {\n   ");
        if (hostName != null)
        {
            builder2.append("hostName: ").append(hostName).append("\n   ");
        }
        if (pid != null)
        {
            builder2.append("pid: ").append(pid).append("\n   ");
        }
        if (threadName != null)
        {
            builder2.append("threadName: ").append(threadName).append("\n   ");
        }
        builder2.append("runNumber: ").append(runNumber).append("\n}");
        return builder2.toString();
    }

    /**
     * Creates builder to build {@link RunContext}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link RunContext}.
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

        public RunContext build()
        {
            return new RunContext(this);
        }
    }
}
