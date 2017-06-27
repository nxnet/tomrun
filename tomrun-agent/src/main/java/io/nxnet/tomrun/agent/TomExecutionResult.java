package io.nxnet.tomrun.agent;

public interface TomExecutionResult
{
    void setProjectsRan(int projectsRan);
    
    int getProjectsRan();
    
    void setSuitesRan(int suitesRan);
    
    int getSuitesRan();

    void setCasesRan(int casesRan);
    
    int getCasesRan();

    void setTestsRan(int testsRan);
    
    int getTestsRan();

    void setActionsRan(int actionsRan);
    
    int getActionsRan();
}
