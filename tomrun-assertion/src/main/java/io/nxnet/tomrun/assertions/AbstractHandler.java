package io.nxnet.tomrun.assertions;


import java.text.MessageFormat;

import io.nxnet.tomrun.context.Context;


public abstract class AbstractHandler implements Handler
{
    protected Context actionContext;
    
    public AbstractHandler(Context actionContext)
    {
        this.actionContext = actionContext;
    }

    protected String getFullMessage(String message)
    {
    	Integer actionId = null/*actionContext.getActionId()*/;
    	Integer testId = null;
    	Integer caseId = null;
    	Integer suiteId = null;
    	
    	Context parentContext = actionContext.getParentContext();
    	while (parentContext != null)
    	{
//    		if (parentContext instanceof TestContext)
//    		{
//    			testId = ((TestContext)parentContext).getTestId();
//    		}
//    		else if (parentContext instanceof TestCaseContext)
//    		{
//    			caseId = ((TestCaseContext)parentContext).getTestCaseId();
//    		}
//    		else if (parentContext instanceof TestSuiteContext)
//    		{
//    			suiteId = ((TestSuiteContext)parentContext).getTestSuiteId();
//    		}
    		
    		parentContext = parentContext.getParentContext();
    	}
    	
        return MessageFormat.format(MESSAGE_PATTERN, message);
    }

}
