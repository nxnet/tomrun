package io.nxnet.tomrun.model;

public class DynamicInclude
{
    private ResolvableAttribute file;

    private Properties properties = Properties.builder().build();
    
    private String factory;
    
    private WrapperAwareElement owner;

    public ResolvableAttribute getFile()
    {
        return file;
    }

    public void setFile(ResolvableAttribute file)
    {
        this.file = file;
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

    @Override
    public String toString()
    {
        return "DynamicInclude [file=" + file + ", properties=" + properties + "]";
    }

    public String getFactory()
    {
        return factory;
    }

    public void setFactory(String factory)
    {
        this.factory = factory;
    }

    public WrapperAwareElement getOwner()
    {
        return owner;
    }

    public void setOwner(WrapperAwareElement owner)
    {
        this.owner = owner;
    }

}
