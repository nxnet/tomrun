package io.nxnet.tomrun.model;

@Deprecated
public class HttpVersion extends ResolvableElement
{
    private String assertionHandler;

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
        return "HttpVersion [assertionHandler=" + assertionHandler + "]";
    }

}
