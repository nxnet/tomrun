package io.nxnet.tomrun.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class Wrapper implements ActionAwareElement, WrapperAwareElement
{
    private WrapperAwareElement owner;

    private String id;

    private String name;

    private String description;

    private Set<AbstractAction> actions = new LinkedHashSet<AbstractAction>();

    private Properties properties = Properties.builder().build();

    private DynamicInclude dynamicInclude;

    private String perRun;

    private String perThread;

    private String factory;

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

    public Set<AbstractAction> getActions()
    {
        return actions;
    }

    public void setActions(Set<AbstractAction> tests)
    {
        this.actions = tests;
    }

    public boolean addAction(AbstractAction test)
    {
        if (this.actions == null)
        {
            this.actions = new LinkedHashSet<AbstractAction>();
        }
        return this.actions.add(test);
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

    public WrapperAwareElement getOwner()
    {
        return owner;
    }

    public void setOwner(WrapperAwareElement owner)
    {
        this.owner = owner;
    }

    public String getPerRun()
    {
        return perRun;
    }

    public void setPerRun(String perRun)
    {
        this.perRun = perRun;
    }

    public String getPerThread()
    {
        return perThread;
    }

    public void setPerThread(String perThread)
    {
        this.perThread = perThread;
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
        Wrapper other = (Wrapper) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Wrapper [id=" + id + ", name=" + name + ", description=" + description + ", actions=" + actions
                + ", properties=" + properties + ", dynamicInclude=" + dynamicInclude + "]";
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
    public String getType()
    {
        return "W";
    }

    @Override
    public Collection<WrapperAwareElement> getChildren()
    {
        Collection<WrapperAwareElement> children = new ArrayList<WrapperAwareElement>();
        for (AbstractAction child : actions)
        {
            children.add(child);
        }
        return children;
    }

    @Override
    public WrapperAwareElement getParent()
    {
        return getOwner();
    }

}
