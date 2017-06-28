package io.nxnet.tomrun.context;

import io.nxnet.tomrun.property.Property;

public interface OwnableContext extends Context
{
    Contextable getOwner();

    void setOwner(Contextable owner);

    Property getPropertyObject(String name);

    Property findPropertyObject(String name);

    Property findPropertyObject(String name, boolean skipThisContext);
}
