package io.nxnet.tomrun.context.def;

public class MyCounterAttribute
{
    Integer count;

    public MyCounterAttribute(Integer count)
    {
        this.count = count;
    }

    public MyCounterAttribute getNextCount()
    {
        this.count++;
        return this;
    }

    @Override
    public String toString()
    {
       return this.count.toString();
    }
}
