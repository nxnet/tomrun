package io.nxnet.tomrun.context;

public interface Contextable
{
    OwnableContext getContext();

    void setContext(OwnableContext context);

    Contextable getParent();
}
