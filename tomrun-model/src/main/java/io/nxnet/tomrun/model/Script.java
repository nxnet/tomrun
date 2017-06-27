package io.nxnet.tomrun.model;

import java.util.Collection;

public class Script extends AbstractAction
{
    private String testHandler;

    public String getTestHandler()
    {
        return testHandler;
    }

    public void setTestHandler(String testHandler)
    {
        this.testHandler = testHandler;
    }

    @Override
    public String toString()
    {
        return "Script [testHandler=" + testHandler + ", ownerId=" + (owner != null ? owner.getId() : null) + ", id="
                + id + ", name=" + name + ", description=" + description + ", properties=" + properties
                + ", dynamicInclude=" + dynamicInclude + ", retryCount=" + retryCount + ", retryPeriod=" + retryPeriod
                + "]";
    }

    @Override
    public Wrapper getBeforeFirst()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBeforeFirst(Wrapper beforeFirst)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Wrapper getAfterLast()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAfterLast(Wrapper afterLast)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Wrapper getBeforeEach()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBeforeEach(Wrapper beforeEach)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Wrapper getAfterEach()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAfterEach(Wrapper afterEach)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<WrapperAwareElement> getChildren()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
