package io.nxnet.tomrun.context.def;

import io.nxnet.tomrun.context.Context;
import io.nxnet.tomrun.interpolation.ReplacementStrategy;

public class PropertyReplacementStrategy implements ReplacementStrategy
{
    private final String propertyName;

    private final Context context;

    private final Context parentContext;

    public PropertyReplacementStrategy(String propertyName, Context context)
    {
        this.propertyName = propertyName;
        this.context = context;
        this.parentContext = context.getParentContext();
    }

    @Override
    public String replace(String s)
    {
        // If we are replacing token equal to property own name, i.e.
        // property is referencing equally named property from higher context
        if (this.propertyName.equals(s))
        {
            if (this.parentContext != null)
            {
                return this.parentContext.findProperty(s);
            }

            // Property is referencing equally named property from higher context
            // but there is no higher context
            return s;
        }

        // Property is referencing NOT equally named property from its context or higher context
        return this.context.findProperty(s);
    }

}
