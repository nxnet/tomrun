package io.nxnet.tomrun.executor;

public interface ExecutorInfo
{
    String getHostName();

    String getPid();

    String getThreadName();

    int getRunNumber();
}
