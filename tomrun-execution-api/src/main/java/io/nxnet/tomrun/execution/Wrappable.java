package io.nxnet.tomrun.execution;

public interface Wrappable<W extends Wrappable<W>>
{
    void setBefore(W n);

    W getBefore();

    void setAfter(W n);

    W getAfter();

    void setBeforeChild(W n);

    W getBeforeChild();

    void setAfterChild(W n);

    W getAfterChild();
}
