package io.nxnet.tomrun.model;

public class Authorization
{
    private ResolvableElement method;

    private ResolvableElement preemptive;

    private ResolvableElement realm;

    private ResolvableElement username;

    private ResolvableElement password;

    public ResolvableElement getMethod()
    {
        return method;
    }

    public void setMethod(ResolvableElement method)
    {
        this.method = method;
    }

    public ResolvableElement getPreemptive()
    {
        return preemptive;
    }

    public void setPreemptive(ResolvableElement preemptive)
    {
        this.preemptive = preemptive;
    }

    public ResolvableElement getRealm()
    {
        return realm;
    }

    public void setRealm(ResolvableElement realm)
    {
        this.realm = realm;
    }

    public ResolvableElement getUsername()
    {
        return username;
    }

    public void setUsername(ResolvableElement username)
    {
        this.username = username;
    }

    public ResolvableElement getPassword()
    {
        return password;
    }

    public void setPassword(ResolvableElement password)
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "Authorization [method=" + method + ", preemptive=" + preemptive + ", realm=" + realm + ", username="
                + username + ", password=" + password + "]";
    }

}
