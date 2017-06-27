package io.nxnet.tomrun.execution;

import io.nxnet.tomrun.model.WrapperAwareElement;

public class MatchAllExecutionFilter implements ExecutionFilter
{

    @Override
    public String getSuiteIdRegex()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSuiteIdRegex(String suiteIdRegex)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCaseIdRegex()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCaseIdRegex(String caseIdRegex)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTestIdRegex()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTestIdRegex(String testIdRegex)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getActionIdRegex()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setActionIdRegex(String actionIdRegex)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getWrapperIdRegex()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWrapperIdRegex(String actionIdRegex)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTargetVersionRegex()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTargetVersionRegex(String targetVersionRegex)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequirementRegex()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRequirementRegex(String requirementRegex)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean matches(WrapperAwareElement element)
    {
        return true;
    }

}
