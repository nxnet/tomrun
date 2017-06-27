package io.nxnet.tomrun.model;

public class ResolvableAssertionAwareElement extends ResolvableElement
{
    protected String assertionHandler;

    public String getAssertionHandler()
    {
        return assertionHandler;
    }

    public void setAssertionHandler(String assertionHandler)
    {
        this.assertionHandler = assertionHandler;
    }

    @Override
    public String toString()
    {
        return "ResolvableAssertionAwareElement [assertionHandler=" + assertionHandler + ", value=" + value
                + ", valueResolver=" + valueResolver + "]";
    }

}
