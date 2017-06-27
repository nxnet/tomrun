package io.nxnet.tomrun.interpolation;

public enum InterpolationStrategy
{
    ALWAYS,
    PER_RUN,
    PER_THREAD,
    PER_PROCES,
    PER_HOST,
    ONCE,
    NEVER;

    public static InterpolationStrategy parseFrom(String strategyName)
    {
        return strategyName != null ? InterpolationStrategy.valueOf(InterpolationStrategy.toUpper(
                InterpolationStrategy.camel2snake(strategyName))) : null;
    }

    static String camel2snake(String camel)
    {
        if (camel == null)
        {
            return null;
        }
        return camel.replaceAll("([a-z])([A-Z]+)", "$1_$2");
    }

    static String toUpper(String lower)
    {
        if (lower == null)
        {
            return null;
        }
        return lower.toUpperCase();
    }

}
