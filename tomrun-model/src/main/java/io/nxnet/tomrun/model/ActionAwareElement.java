package io.nxnet.tomrun.model;

import java.util.Set;

public interface ActionAwareElement
{

    String getId();

    void setId(String id);

    Set<AbstractAction> getActions();

    void setActions(Set<AbstractAction> tests);

    boolean addAction(AbstractAction test);

}
