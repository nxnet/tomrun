package io.nxnet.tomrun.resolver.def;

import io.nxnet.tomrun.resolver.ValueResolver;
import io.nxnet.tomrun.resolver.ValueResolverException;

public class LiteralResolver implements ValueResolver
{
    private String literal;
    
    public LiteralResolver(String literal)
    {
        this.literal = literal;
    }
    
    @Override
    public String resolve() throws ValueResolverException
    {
        return this.literal != null ? this.literal : "";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n   class: ").append(getClass().getName()).append("\n   ");
        if (literal != null)
        {
            builder.append("literal: ").append(literal);
        }
        builder.append("\n}");
        return builder.toString();
    }

}
