package io.nxnet.tomrun.context.def;

import io.nxnet.tomrun.context.Context;
import io.nxnet.tomrun.interpolation.ReplacementStrategy;

public class PropertyReplacementStrategy implements ReplacementStrategy
{
    private Context context;

    public PropertyReplacementStrategy(Context context)
    {
        this.context = context;
    }

    @Override
    public String replace(String s)
    {
        return this.context.findProperty(s);
    }

}
