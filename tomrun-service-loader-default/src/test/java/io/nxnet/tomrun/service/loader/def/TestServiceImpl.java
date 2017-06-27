package io.nxnet.tomrun.service.loader.def;

public class TestServiceImpl implements TestService
{
    @Override
    public String getServiceName()
    {
        return "testService";
    }

    @Override
    public String sayHello()
    {
        return "Hello from test service!";
    }

}
