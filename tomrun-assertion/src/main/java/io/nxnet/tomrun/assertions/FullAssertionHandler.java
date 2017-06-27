package io.nxnet.tomrun.assertions;

import io.nxnet.tomrun.context.Context;

public class FullAssertionHandler extends AbstractAssertionHandler implements AssertionHandler
{
    public FullAssertionHandler(Context testCtx)
    {
        super(testCtx);
    }

    public boolean assertEquals(String message, String expected, String actual)
    {
        boolean success = expected.equals(actual);
        if (!success)
        {
            throw new AssertionError(getFullMessage(message, expected, actual));
        }
        return success;
    }

}
