package io.nxnet.tomrun.assertions;

import io.nxnet.tomrun.context.Context;

public class RegexAssertionHandler extends AbstractAssertionHandler implements AssertionHandler
{
    
    public RegexAssertionHandler(Context testCtx)
    {
        super(testCtx);
    }

    public boolean assertEquals(String message, String expected, String actual)
    {
        boolean success = actual != null && actual.matches(expected);
        if (!success)
        {
        	throw new AssertionError(getFullMessage(message, expected, actual));
        }
        return success;
    }

}
