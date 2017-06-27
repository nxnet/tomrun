package io.nxnet.tomrun.property.def;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.nxnet.tomrun.alias.AliasException;
import io.nxnet.tomrun.alias.AliasRegistry;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;

public class PropertyDecoratorBuilder
{
    private AliasRegistry aliasRegistry;

    private String aliasNamespace;

    private Property target;

    private List<String> decorators;

    /**
     * @param aliasRegistry the aliasRegistry to set
     */
    public PropertyDecoratorBuilder withAliasRegistry(AliasRegistry aliasRegistry)
    {
        this.aliasRegistry = aliasRegistry;
        return this;
    }

    /**
     * @param aliasNamespace the aliasNamespace to set
     */
    public PropertyDecoratorBuilder withAliasNamespace(String aliasNamespace)
    {
        this.aliasNamespace = aliasNamespace;
        return this;
    }

    /**
     * @param target the target to set
     */
    public PropertyDecoratorBuilder withTarget(Property target)
    {
        this.target = target;
        return this;
    }

    /**
     * @param decorators the decorators to set
     */
    public PropertyDecoratorBuilder withDecorators(List<String> decorators)
    {
        this.decorators = decorators;
        return this;
    }

    /**
     * @param decorator the decorator to add
     */
    public PropertyDecoratorBuilder withDecorators(String decorator)
    {
        if (this.decorators == null)
        {
            this.decorators = new ArrayList<String>();
        }

        this.decorators.add(decorator);
        return this;
    }

    /**
     * Generates decorator instance
     */
    public Property build() throws PropertyException
    {
        return this._decorate(this.target, this.decorators);
    }

    Property _decorate(Property property, List<String> decorators) throws PropertyException
    {
        // Validate args
        if (property == null)
        {
            throw new IllegalArgumentException("property argument can't be null!");
        }
        if (decorators == null)
        {
            throw new IllegalArgumentException("decorators argument can't be null!");
        }

        // Find decorator classes, instantiate them and add wrap given property 
        Property decoratedProperty = property;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Collections.reverse(decorators);
        for (String decorator : decorators)
        {
            decoratedProperty = this._decorate(decoratedProperty, decorator.trim(), classLoader);
        }

        return decoratedProperty;
    }

    Property _decorate(Property property, String decorator, ClassLoader classLoader) throws PropertyException
    {
        // Validate args
        if (property == null)
        {
            throw new IllegalArgumentException("property argument can't be null!");
        }
        if (decorator == null)
        {
            throw new IllegalArgumentException("decorator argument can't be null!");
        }

        // Check if given decorator argument is decorator class name alias
        String decoratorClassName = decorator;
        try
        {
            String realClassName = this.aliasRegistry.getName(this.aliasNamespace, decorator);
            if (realClassName != null)
            {
                decoratorClassName = realClassName;
            }
        }
        catch (AliasException e)
        {
            throw new PropertyException("Problem resolving alias for decorator: " + decorator, e);
        }

        // Find decorator class, instantiate it and add wrap given property
        Class<Property> decoratorClass = null;
        try
        {
            decoratorClass = (Class<Property>) classLoader.loadClass(decoratorClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new PropertyException("Can't find decorator class: " + decoratorClassName, e);
        }

        // Instantiate decorator
        Property decoratorInstance = null;
        try
        {
            decoratorInstance = decoratorClass.getConstructor(Property.class).newInstance(property);
        }
        catch (InstantiationException e)
        {
            throw new PropertyException("Can't instantiate decorator: " + decorator, e);
        }
        catch (IllegalAccessException e)
        {
            throw new PropertyException("Can't instantiate decorator: " + decorator, e);
        }
        catch (IllegalArgumentException e)
        {
            throw new PropertyException("Can't instantiate decorator: " + decorator, e);
        }
        catch (InvocationTargetException e)
        {
            throw new PropertyException("Can't instantiate decorator: " + decorator, e);
        }
        catch (NoSuchMethodException e)
        {
            throw new PropertyException("Can't instantiate decorator: " + decorator, e);
        }
        catch (SecurityException e)
        {
            throw new PropertyException("Can't instantiate decorator: " + decorator, e);
        }

        return decoratorInstance;
    }

}
