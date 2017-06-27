package io.nxnet.tomrun.model;

public class StatusLine
{
    private ResolvableAssertionAwareElement httpVersion;

    private ResolvableAssertionAwareElement statusCode;

    private ResolvableAssertionAwareElement reasonPhrase;

    public ResolvableAssertionAwareElement getHttpVersion()
    {
        return httpVersion;
    }

    public void setHttpVersion(ResolvableAssertionAwareElement httpVersion)
    {
        this.httpVersion = httpVersion;
    }

    public ResolvableAssertionAwareElement getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(ResolvableAssertionAwareElement statusCode)
    {
        this.statusCode = statusCode;
    }

    public ResolvableAssertionAwareElement getReasonPhrase()
    {
        return reasonPhrase;
    }

    public void setReasonPhrase(ResolvableAssertionAwareElement reasonPhrase)
    {
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public String toString()
    {
        return "StatusLine [httpVersion=" + httpVersion + ", statusCode=" + statusCode + ", reasonPhrase="
                + reasonPhrase + "]";
    }

}
