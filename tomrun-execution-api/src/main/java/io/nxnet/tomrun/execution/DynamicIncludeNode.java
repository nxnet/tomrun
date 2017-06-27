package io.nxnet.tomrun.execution;

import io.nxnet.tomrun.model.ResolvableAttribute;

public class DynamicIncludeNode
{
    private String path;
    
    private ResolvableAttribute file = new ResolvableAttribute("tom.xml");
    
    private String id;

    private String name;

    private String description;

    private String type;

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public ResolvableAttribute getFile()
    {
        return file;
    }

    public void setFile(ResolvableAttribute file)
    {
        this.file = file;
    }

    @Override
    public String toString()
    {
        return "DynamicIncludeNode [path=" + path + ", file=" + file + "]";
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
    
}
