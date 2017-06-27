package io.nxnet.tomrun.execution;

import io.nxnet.tomrun.context.Contextable;

public interface ExecutionNode extends Identifiable, Contextable, IObservable, Locateable<ExecutionNode>,
        Iterable<ExecutionNode>, Wrappable<ExecutionNode>, Executable<ExecutionNode>, Hierarchical<ExecutionNode>
{
}
