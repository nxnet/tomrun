package io.nxnet.tomrun.execution;

public interface Executable<E extends Executable<E>> extends IObservable
{
    void doExec();

    void setUp();

    void tearDown();
}
