package io.nxnet.tomrun.model;

import java.util.Collection;
import java.util.Map;

public class HttpRequest extends AbstractAction
{
    private String method;

    private ResolvableElement requestUri;

    private Authorization authorization;

    private Map<ResolvableElement, ResolvableAssertionAwareElement> headerFields;

    private Map<ResolvableElement, ResolvableElement> parameters;

    private MessageBody messageBody;

    private HttpResponse httpResponse;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public ResolvableElement getRequestUri()
    {
        return requestUri;
    }

    public void setRequestUri(ResolvableElement requestUri)
    {
        this.requestUri = requestUri;
    }

    public Authorization getAuthorization()
    {
        return authorization;
    }

    public void setAuthorization(Authorization authorization)
    {
        this.authorization = authorization;
    }

    public Map<ResolvableElement, ResolvableAssertionAwareElement> getHeaderFields()
    {
        return headerFields;
    }

    public void setHeaderFields(Map<ResolvableElement, ResolvableAssertionAwareElement> headerFields)
    {
        this.headerFields = headerFields;
    }

    public Map<ResolvableElement, ResolvableElement> getParameters()
    {
        return parameters;
    }

    public void setParameters(Map<ResolvableElement, ResolvableElement> parameters)
    {
        this.parameters = parameters;
    }

    public MessageBody getMessageBody()
    {
        return messageBody;
    }

    public void setMessageBody(MessageBody messageBody)
    {
        this.messageBody = messageBody;
    }

    public HttpResponse getHttpResponse()
    {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse)
    {
        this.httpResponse = httpResponse;
    }

    @Override
    public String toString()
    {
        return "HttpRequest [testId=" + (owner != null ? owner.getId() : null) + ", id=" + id + ", name=" + name
                + ", description=" + description + ", method=" + method + ", properties=" + properties
                + ", requestUri=" + requestUri + ", authorization=" + authorization + ", headerFields=" + headerFields
                + ", parameters=" + parameters + ", messageBody=" + messageBody + ", httpResponse=" + httpResponse
                + "]";
    }

    @Override
    public Wrapper getBeforeFirst()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBeforeFirst(Wrapper beforeFirst)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Wrapper getAfterLast()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAfterLast(Wrapper afterLast)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Wrapper getBeforeEach()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBeforeEach(Wrapper beforeEach)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Wrapper getAfterEach()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAfterEach(Wrapper afterEach)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<WrapperAwareElement> getChildren()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
