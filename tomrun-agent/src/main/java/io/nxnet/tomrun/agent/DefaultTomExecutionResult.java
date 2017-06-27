package io.nxnet.tomrun.agent;

import java.util.Observable;
import java.util.Observer;

import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeEvent;

public class DefaultTomExecutionResult implements TomExecutionResult, Observer
{  
    private int projectsRan;

    private int suitesRan;

    private int casesRan;

    private int testsRan;

    private int actionsRan;

    @Override
    public void setProjectsRan(int projectsRan)
    {
        this.projectsRan = projectsRan;
    }

    @Override
    public int getProjectsRan()
    {
        return this.projectsRan;
    }

    @Override
    public void setSuitesRan(int suitesRan)
    {
        this.suitesRan = suitesRan;
    }

    @Override
    public int getSuitesRan()
    {
        return this.suitesRan;
    }

    @Override
    public void setCasesRan(int casesRan)
    {
        this.casesRan = casesRan;
    }

    @Override
    public int getCasesRan()
    {
        return this.casesRan;
    }

    @Override
    public void setTestsRan(int testsRan)
    {
        this.testsRan = testsRan;
    }

    @Override
    public int getTestsRan()
    {
        return this.testsRan;
    }

    @Override
    public void setActionsRan(int actionsRan)
    {
        this.actionsRan = actionsRan;
    }

    @Override
    public int getActionsRan()
    {
        return this.actionsRan;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        ExecutionNode executionNode = (ExecutionNode) o;
        ExecutionNodeEvent event = (ExecutionNodeEvent) arg;
     
        switch (event.getEventName())
        {
            case EXEC_START:
                switch (executionNode.getType())
                {
                    case PROJECT:
                        this.projectsRan++;
                        break;
                    case SUITE:
                        this.suitesRan++;
                        break;
                    case CASE:
                        this.casesRan++;
                        break;
                    case TEST:
                        this.testsRan++;
                        break;
                    case ACTION:
                        this.actionsRan++;
                        break;
                    default:
                        break;
                }
                break;
            case EXEC_END:
                break;
        }
    }

}
