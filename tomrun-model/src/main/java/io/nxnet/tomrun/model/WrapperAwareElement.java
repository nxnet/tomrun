package io.nxnet.tomrun.model;

import java.util.Collection;

public interface WrapperAwareElement extends TomElement
{

    public Wrapper getBeforeFirst();

    public void setBeforeFirst(Wrapper beforeFirst);

    public Wrapper getAfterLast();

    public void setAfterLast(Wrapper afterLast);

    public Wrapper getBeforeEach();

    public void setBeforeEach(Wrapper beforeEach);

    public Wrapper getAfterEach();

    public void setAfterEach(Wrapper afterEach);

    public String getId();

    public void setId(String id);

    public String getDescription();

    public void setDescription(String description);

    public String getName();

    public void setName(String name);

    public String getType();

    public Properties getProperties();

    public WrapperAwareElement getParent();

    public Collection<WrapperAwareElement> getChildren();

    public String getFactory();

    public DynamicInclude getDynamicInclude();

}
