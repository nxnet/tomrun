package io.nxnet.tomrun.execution;

public interface Identifiable
{
    String getId();

    String getName();

    String getDescription();

    ExecutionNodeType getType();
}
