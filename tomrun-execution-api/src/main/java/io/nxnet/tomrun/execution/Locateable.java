package io.nxnet.tomrun.execution;

public interface Locateable<L extends Locateable<L>>
{
    int getLevel();

    boolean isLast();

    boolean isRoot();

    boolean isLeaf();

    boolean isChild();

    boolean isParent();

    boolean isLeftChild();

    boolean isMiddleChild();

    boolean isRightChild();

    boolean isChildOf(L l);

    boolean isParentOf(L l);

    boolean isLeftChildOf(L l);

    boolean isMiddleChildOf(L l);

    boolean isRightChildOf(L l);

}
