package io.nxnet.tomrun.model;

public abstract class AbstractAction implements WrapperAwareElement
{
    protected ActionAwareElement owner;

    protected String id;

    protected String name;

    protected String description;

    protected Properties properties = Properties.builder().build();

    protected DynamicInclude dynamicInclude;

    protected ResolvableAttribute retryCount;

    protected ResolvableAttribute retryPeriod;

    protected String factory;

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

    public ActionAwareElement getOwner()
    {
        return owner;
    }

    public void setOwner(ActionAwareElement test)
    {
        this.owner = test;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }

    public boolean addProperty(Property property)
    {
        return this.properties.addProperty(property);
    }

    public DynamicInclude getDynamicInclude()
    {
        return dynamicInclude;
    }

    public void setDynamicInclude(DynamicInclude dynamicInclude)
    {
        this.dynamicInclude = dynamicInclude;
    }

    public ResolvableAttribute getRetryCount()
    {
        return retryCount;
    }

    public void setRetryCount(ResolvableAttribute retryCount)
    {
        this.retryCount = retryCount;
    }

    public ResolvableAttribute getRetryPeriod()
    {
        return retryPeriod;
    }

    public void setRetryPeriod(ResolvableAttribute retryPeriod)
    {
        this.retryPeriod = retryPeriod;
    }

    public String getFactory()
    {
        return factory;
    }

    public void setFactory(String factory)
    {
        this.factory = factory;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractAction other = (AbstractAction) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (owner == null)
        {
            if (other.owner != null)
                return false;
        }
        else if (!owner.equals(other.owner))
            return false;
        return true;
    }

    @Override
    public String getType()
    {
        return "A";
    }

    @Override
    public WrapperAwareElement getParent()
    {
        return (WrapperAwareElement)getOwner();
    }

}
