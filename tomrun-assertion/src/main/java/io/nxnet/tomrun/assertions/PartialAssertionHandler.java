package io.nxnet.tomrun.assertions;

import io.nxnet.tomrun.context.Context;

public class PartialAssertionHandler extends AbstractAssertionHandler implements AssertionHandler
{
    public PartialAssertionHandler(Context ctx)
    {
        super(ctx);
    }

    public boolean assertEquals(String message, String expected, String actual)
    {
        boolean success = actual != null && actual.contains(expected);
        if (!success)
        {
        	throw new AssertionError(getFullMessage(message, expected, actual));
        }
        return success;
    }

}
