package io.nxnet.tomrun.model;

public class ResolvableAttribute extends ResolvableElement
{
    public ResolvableAttribute()
    {
        super(null, null);
    }

    public ResolvableAttribute(String value)
    {
        super(value, null);
    }

    @Override
    public String toString()
    {
        return "ResolvableAttribute [value=" + value + ", valueResolver=" + valueResolver + "]";
    }

}
