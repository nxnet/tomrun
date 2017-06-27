package io.nxnet.tomrun.model;


public class ModelHelper
{

    public static int getLevel(WrapperAwareElement e)
    {
        if (TestProject.class.isAssignableFrom(e.getClass()))
        {
            return 0;
        }
        else if (TestSuite.class.isAssignableFrom(e.getClass()))
        {
            return 1;
        }
        else if (TestCase.class.isAssignableFrom(e.getClass()))
        {
            return 2;
        }
        else if (Test.class.isAssignableFrom(e.getClass()))
        {
            return 3;
        }
        else
        {
            throw new IllegalArgumentException("Unsupported element: " + e);
        }
    }

    public static int getLevel(ActionAwareElement e)
    {
        if (Test.class.isAssignableFrom(e.getClass()))
        {
            return 3;
        }
        else if (Wrapper.class.isAssignableFrom(e.getClass()))
        {
            return getLevel(((Wrapper) e).getOwner()) + 1;
        }
        else
        {
            throw new IllegalArgumentException("Unsupported element: " + e);
        }
    }

    public static int getLevel(AbstractAction e)
    {
        return getLevel(e.getOwner()) + 1;
    }

}
