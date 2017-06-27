package io.nxnet.tomrun.model;

import java.util.Map;

public class HttpResponse extends ResolvableAssertionAwareElement
{
    private StatusLine statusLine;

    private Map<ResolvableElement, ResolvableAssertionAwareElement> headerFields;

    private MessageBody messageBody;

    public StatusLine getStatusLine()
    {
        return statusLine;
    }

    public void setStatusLine(StatusLine statusLine)
    {
        this.statusLine = statusLine;
    }

    public Map<ResolvableElement, ResolvableAssertionAwareElement> getHeaderFields()
    {
        return headerFields;
    }

    public void setHeaderFields(Map<ResolvableElement, ResolvableAssertionAwareElement> headerFields)
    {
        this.headerFields = headerFields;
    }

    public MessageBody getMessageBody()
    {
        return messageBody;
    }

    public void setMessageBody(MessageBody messageBody)
    {
        this.messageBody = messageBody;
    }

    @Override
    public String toString()
    {
        return "HttpResponse [statusLine=" + statusLine + ", headerFields=" + headerFields + ", messageBody="
                + messageBody + "]";
    }

}
