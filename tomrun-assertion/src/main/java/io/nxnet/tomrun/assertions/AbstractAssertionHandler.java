package io.nxnet.tomrun.assertions;


import java.text.MessageFormat;

import io.nxnet.tomrun.context.Context;

public abstract class AbstractAssertionHandler extends AbstractHandler implements AssertionHandler
{
    public AbstractAssertionHandler(Context ctx)
    {
        super(ctx);
    }

    protected String getFullMessage(String message, String expected, String actual)
    {
        String partialMessage = MessageFormat.format("{0} expected:<{1}> but was:<{2}>", message, expected, actual);
        return getFullMessage(partialMessage);
    }

}
