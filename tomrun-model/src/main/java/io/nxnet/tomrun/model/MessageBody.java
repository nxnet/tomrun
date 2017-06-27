package io.nxnet.tomrun.model;

public class MessageBody extends ResolvableAssertionAwareElement
{
    private boolean ignoreWhiteSpace;

    private String contentType;

    private String charset;

    public boolean isIgnoreWhiteSpace()
    {
        return ignoreWhiteSpace;
    }

    public void setIgnoreWhiteSpace(boolean ignoreWhiteSpace)
    {
        this.ignoreWhiteSpace = ignoreWhiteSpace;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getCharset()
    {
        return charset;
    }

    public void setCharset(String charset)
    {
        this.charset = charset;
    }

    @Override
    public String toString()
    {
        return "MessageBody [ignoreWhiteSpace=" + ignoreWhiteSpace + ", contentType=" + contentType + ", charset="
                + charset + ", assertionHandler=" + assertionHandler + ", value=" + value + ", valueResolver="
                + valueResolver + "]";
    }

}
