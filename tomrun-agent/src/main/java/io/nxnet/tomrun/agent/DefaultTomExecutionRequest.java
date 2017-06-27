package io.nxnet.tomrun.agent;

public class DefaultTomExecutionRequest implements TomExecutionRequest
{
    protected String tom;

    protected String suite;

    protected String caze;

    protected String test;

    protected String action;

    protected String wrapper;

    protected String requirement;

    protected String version;

    protected String detail;

    protected boolean hideWrappers;

    @Override
    public void setTom(String tom)
    {
        this.tom = tom;
    }

    @Override
    public String getTom()
    {
        return this.tom;
    }

    @Override
    public void setSuite(String suite)
    {
        this.suite = suite;
    }

    @Override
    public String getSuite()
    {
        return suite;
    }

    @Override
    public String getCaze()
    {
        return caze;
    }

    @Override
    public void setCaze(String caze)
    {
        this.caze = caze;
    }

    @Override
    public String getTest()
    {
        return test;
    }

    @Override
    public void setTest(String test)
    {
        this.test = test;
    }

    @Override
    public String getAction()
    {
        return action;
    }

    @Override
    public void setAction(String action)
    {
        this.action = action;
    }

    @Override
    public String getWrapper()
    {
        return this.wrapper;
    }

    @Override
    public void setWrapper(String wrapper)
    {
        this.wrapper = wrapper;
    }

    @Override
    public String getRequirement()
    {
        return requirement;
    }

    @Override
    public void setRequirement(String requirement)
    {
        this.requirement = requirement;
    }

    @Override
    public String getVersion()
    {
        return version;
    }

    @Override
    public void setVersion(String version)
    {
        this.version = version;
    }

    @Override
    public String getDetail()
    {
        return detail;
    }

    @Override
    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    @Override
    public boolean isHideWrappers()
    {
        return hideWrappers;
    }

    @Override
    public void setHideWrappers(boolean hideWrappers)
    {
        this.hideWrappers = hideWrappers;
    }

    public static class Builder
    {
        private String tom;
        private String suite;
        private String caze;
        private String test;
        private String action;
        private String wrapper;
        private String requirement;
        private String version;
        private String detail;
        private boolean hideWrappers;

        public Builder tom(String tom)
        {
            this.tom = tom;
            return this;
        }

        public Builder suite(String suite)
        {
            this.suite = suite;
            return this;
        }

        public Builder caze(String caze)
        {
            this.caze = caze;
            return this;
        }

        public Builder test(String test)
        {
            this.test = test;
            return this;
        }

        public Builder action(String action)
        {
            this.action = action;
            return this;
        }

        public Builder wrapper(String wrapper)
        {
            this.wrapper = wrapper;
            return this;
        }

        public Builder requirement(String requirement)
        {
            this.requirement = requirement;
            return this;
        }

        public Builder version(String version)
        {
            this.version = version;
            return this;
        }

        public Builder detail(String detail)
        {
            this.detail = detail;
            return this;
        }

        public Builder hideWrappers(boolean hideWrappers)
        {
            this.hideWrappers = hideWrappers;
            return this;
        }

        public DefaultTomExecutionRequest build()
        {
            DefaultTomExecutionRequest defaultTomExecutionRequest = new DefaultTomExecutionRequest();
            defaultTomExecutionRequest.tom = tom;
            defaultTomExecutionRequest.suite = suite;
            defaultTomExecutionRequest.caze = caze;
            defaultTomExecutionRequest.test = test;
            defaultTomExecutionRequest.action = action;
            defaultTomExecutionRequest.wrapper = wrapper;
            defaultTomExecutionRequest.requirement = requirement;
            defaultTomExecutionRequest.version = version;
            defaultTomExecutionRequest.detail = detail;
            defaultTomExecutionRequest.hideWrappers = hideWrappers;
            return defaultTomExecutionRequest;
        }
    }
}
