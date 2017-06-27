package io.nxnet.tomrun.property;

import io.nxnet.tomrun.interpolation.InterpolationStrategy;
import io.nxnet.tomrun.resolver.ResolveStrategy;

public interface Property
{
    String getName();

    String getValue(RunContext runContext) throws PropertyException;

    InterpolationStrategy getInterpolationStrategy();

    ResolveStrategy getResolveStrategy();
}
