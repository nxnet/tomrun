package io.nxnet.tomrun.agent;

public interface TomExecutionRequest
{
    void setTom(String tom);
    
    String getTom();
    
    void setSuite(String suite);
    
    String getSuite();

    String getCaze();

    void setCaze(String caze);

    String getTest();

    void setTest(String test);

    String getAction();

    void setAction(String action);

    String getWrapper();

    void setWrapper(String wrapper);

    String getRequirement();

    void setRequirement(String requirement);

    String getVersion();

    void setVersion(String version);

    String getDetail();

    void setDetail(String detail);

    boolean isHideWrappers();

    void setHideWrappers(boolean hideWrappers);

}
