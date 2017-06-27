package io.nxnet.tomrun.execution;

import java.util.List;

public interface Hierarchical<H extends Hierarchical<H>>
{
    void setParent(H parent);

    H getParent();

    void setChildren(List<H> children);

    List<H> getChildren();

    void addChild(H child);

    H getLeftSibling();

    H getRightSibling();

    H getLeftExpandedSibling();

    H getRightExpandedSibling();

    List<H> getExpandedChildren();
}
