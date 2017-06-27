package io.nxnet.tomrun.execution.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.nxnet.tomrun.execution.ExecutionFilter;
import io.nxnet.tomrun.model.WrapperAwareElement;

public class DefaultExecutionFilter implements ExecutionFilter
{
    private Pattern suiteIdRegex;

    private Pattern caseIdRegex;

    private Pattern testIdRegex;

    private Pattern actionIdRegex;

    private Pattern wrapperIdRegex;

    @Override
    public String getSuiteIdRegex()
    {
        if (this.suiteIdRegex != null)
        {
            return this.suiteIdRegex.pattern();
        }
        return null;
    }

    @Override
    public String getCaseIdRegex()
    {
        if (this.caseIdRegex != null)
        {
            return this.caseIdRegex.pattern();
        }
        return null;
    }

    @Override
    public String getTestIdRegex()
    {
        if (this.testIdRegex != null)
        {
            return this.testIdRegex.pattern();
        }
        return null;
    }

    @Override
    public String getActionIdRegex()
    {
        if (this.actionIdRegex != null)
        {
            return this.actionIdRegex.pattern();
        }
        return null;
    }

    @Override
    public String getWrapperIdRegex()
    {
        if (this.wrapperIdRegex != null)
        {
            return this.wrapperIdRegex.pattern();
        }
        return null;
    }

    @Override
    public void setSuiteIdRegex(String suiteIdRegex)
    {
        if (suiteIdRegex != null)
        {
            this.suiteIdRegex = Pattern.compile(suiteIdRegex);
        }
    }

    @Override
    public void setCaseIdRegex(String caseIdRegex)
    {
        if (caseIdRegex != null)
        {
            this.caseIdRegex = Pattern.compile(caseIdRegex);
        }
    }

    @Override
    public void setTestIdRegex(String testIdRegex)
    {
        if (testIdRegex != null)
        {
            this.testIdRegex = Pattern.compile(testIdRegex);
        }
    }

    @Override
    public void setActionIdRegex(String actionIdRegex)
    {
        if (actionIdRegex != null)
        {
            this.actionIdRegex = Pattern.compile(actionIdRegex);
        }
    }

    @Override
    public void setWrapperIdRegex(String wrapperIdRegex)
    {
        if (wrapperIdRegex != null)
        {
            this.wrapperIdRegex = Pattern.compile(wrapperIdRegex);
        }
    }

    @Override
    public String getTargetVersionRegex()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRequirementRegex()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTargetVersionRegex(String targetVersionRegex)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setRequirementRegex(String requirementRegex)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean matches(WrapperAwareElement element)
    {
        Matcher matcher = null;
        if ("S".equals(element.getType()) && suiteIdRegex != null)
        {
            matcher = suiteIdRegex.matcher(element.getId());
            return matcher.matches();
        }
        else if ("C".equals(element.getType()) && caseIdRegex != null)
        {
            matcher = caseIdRegex.matcher(element.getId());
            return matcher.matches();
        }
        else if ("T".equals(element.getType()) && testIdRegex != null)
        {
            matcher = testIdRegex.matcher(element.getId());
            return matcher.matches();
        }
        else if ("A".equals(element.getType()) && actionIdRegex != null)
        {
            matcher = actionIdRegex.matcher(element.getId());
            return matcher.matches();
        }
        else if ("W".equals(element.getType()) && wrapperIdRegex != null)
        {
            matcher = wrapperIdRegex.matcher(element.getId());
            return matcher.matches();
        }
        return true;
    }

    public static class Builder
    {
        private Pattern suiteIdRegex;
        private Pattern caseIdRegex;
        private Pattern testIdRegex;
        private Pattern actionIdRegex;
        private Pattern wrapperIdRegex;

        public Builder suiteIdRegex(String suiteIdRegex)
        {
            if (suiteIdRegex != null)
            {
                this.suiteIdRegex = Pattern.compile(suiteIdRegex);
            }
            return this;
        }

        public Builder caseIdRegex(String caseIdRegex)
        {
            if (caseIdRegex != null)
            {
                this.caseIdRegex = Pattern.compile(caseIdRegex);
            }
            return this;
        }

        public Builder testIdRegex(String testIdRegex)
        {
            if (testIdRegex != null)
            {
                this.testIdRegex = Pattern.compile(testIdRegex);
            }
            return this;
        }

        public Builder actionIdRegex(String actionIdRegex)
        {
            if (actionIdRegex != null)
            {
                this.actionIdRegex = Pattern.compile(actionIdRegex);
            }
            return this;
        }

        public Builder wrapperIdRegex(String wrapperIdRegex)
        {
            if (wrapperIdRegex != null)
            {
                this.wrapperIdRegex = Pattern.compile(wrapperIdRegex);
            }
            return this;
        }

        public DefaultExecutionFilter build()
        {
            return new DefaultExecutionFilter(this);
        }
    }

    private DefaultExecutionFilter(Builder builder)
    {
        this.suiteIdRegex = builder.suiteIdRegex;
        this.caseIdRegex = builder.caseIdRegex;
        this.testIdRegex = builder.testIdRegex;
        this.actionIdRegex = builder.actionIdRegex;
        this.wrapperIdRegex = builder.wrapperIdRegex;
    }
}
