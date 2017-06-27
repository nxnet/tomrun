package io.nxnet.tomrun.execution;

import io.nxnet.tomrun.model.WrapperAwareElement;

public interface ExecutionFilter
{
    ExecutionFilter MATCH_ALL = new MatchAllExecutionFilter();
    
    String getSuiteIdRegex();

    void setSuiteIdRegex(String suiteIdRegex);

    String getCaseIdRegex();

    void setCaseIdRegex(String caseIdRegex);

    String getTestIdRegex();

    void setTestIdRegex(String testIdRegex);

    String getActionIdRegex();

    void setActionIdRegex(String actionIdRegex);

    String getWrapperIdRegex();

    void setWrapperIdRegex(String actionIdRegex);

    String getTargetVersionRegex();

    void setTargetVersionRegex(String targetVersionRegex);

    String getRequirementRegex();

    void setRequirementRegex(String requirementRegex);

    boolean matches(WrapperAwareElement element);
}
