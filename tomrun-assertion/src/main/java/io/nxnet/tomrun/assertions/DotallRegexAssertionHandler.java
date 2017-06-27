package io.nxnet.tomrun.assertions;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.nxnet.tomrun.context.Context;



public class DotallRegexAssertionHandler extends AbstractAssertionHandler implements AssertionHandler
{
    public DotallRegexAssertionHandler(Context testCtx)
    {
        super(testCtx);
    }

    public boolean assertEquals(String message, String expected, String actual)
    {
    	boolean success = false;
    	if (expected != null && actual != null)
    	{
    		Pattern expectedPattern = Pattern.compile(expected, Pattern.DOTALL);
    		Matcher matcher = expectedPattern.matcher(actual);
    		success = matcher.matches();
    	}
    	
        if (!success)
        {
        	throw new AssertionError(getFullMessage(message, expected, actual));
        }
        return success;
    }

}
