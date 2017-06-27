package io.nxnet.tomrun.execution;

import java.util.Iterator;

public interface Iterable<I extends Iterable<I>>
{
    Iterator<I> getIter();
}
