package io.nxnet.tomrun.context.def;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import io.nxnet.tomrun.context.Context;
import io.nxnet.tomrun.interpolation.ReplacementStrategy;

public class AttributeReplacementStrategy implements ReplacementStrategy
{
    private Context context;

    public AttributeReplacementStrategy(Context context)
    {
        this.context = context;
    }

    @Override
    public String replace(String s)
    {
        String[] beanPropertyTokens = s.split("\\.", 2);
        String attributeName = beanPropertyTokens[0];
        String attributePropertyPath = null;
        if (beanPropertyTokens.length == 2)
        {
            attributePropertyPath = beanPropertyTokens[1];
        }
        
        Object attribute = context.findAttribute(attributeName);
        if (attribute == null)
        {
            return null;
        }

        if (attributePropertyPath == null)
        {
            return attribute.toString();
        }

        Object attributeValueObject = null;
        try
        {
            attributeValueObject = PropertyUtils.getProperty(attribute, attributePropertyPath);
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException(e);
        }
        
        if (attributeValueObject == null)
        {
            return "null";
        }

        return attributeValueObject.toString();
    }
}
