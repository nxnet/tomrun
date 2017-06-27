package io.nxnet.tomrun.execution;

import java.text.MessageFormat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.nxnet.tomrun.execution.ExecutionNodeFactory;

public class ExecutionNodeFactoryTest
{
    public static final String DEFAULT_EXECUTION_NODE_FACTORY_NAME = "defaultExecutionNodeFactory";

    public static final String CUSTOM_EXECUTION_NODE_FACTORY_NAME = "customExecutionNodeFactory";

    public static final String EXPECTED_EXCEPTION_MESSAGE_DEFAULT = MessageFormat.format(
            "No matching {0} service named {1} found!", ExecutionNodeFactory.class.getName(),
            DEFAULT_EXECUTION_NODE_FACTORY_NAME);

    public static final String EXPECTED_EXCEPTION_MESSAGE_CUSTOM = MessageFormat.format(
            "No matching {0} service named {1} found!", ExecutionNodeFactory.class.getName(),
            CUSTOM_EXECUTION_NODE_FACTORY_NAME);

    public static final String EXPECTED_EXCEPTION_MESSAGE_NO_CLASSLOADER = "class loader argument can't be null";

    public static final String EXPECTED_EXCEPTION_MESSAGE_NO_FACTORY_NAME = "service name argument can't be null";

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void newInstance_noArgs() throws Exception
    {
        // Prepare expectations
        this.expectedException.expect(IllegalStateException.class);
        this.expectedException.expectMessage(EXPECTED_EXCEPTION_MESSAGE_DEFAULT);

        // do test
        ExecutionNodeFactory.newInstance();
    }

    @Test
    public void newInstance_customClassLoader() throws Exception
    {
        // Prepare expectations
        this.expectedException.expect(IllegalStateException.class);
        this.expectedException.expectMessage(EXPECTED_EXCEPTION_MESSAGE_DEFAULT);

        // do test
        ExecutionNodeFactory.newInstance(this.getClass().getClassLoader());
    }

    @Test
    public void newInstance_nullClassLoader() throws Exception
    {
        // Prepare expectations
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage(EXPECTED_EXCEPTION_MESSAGE_NO_CLASSLOADER);

        // do test
        ExecutionNodeFactory.newInstance((ClassLoader)null);
    }

    @Test
    public void newInstance_customFactoryName() throws Exception
    {
        // Prepare expectations
        this.expectedException.expect(IllegalStateException.class);
        this.expectedException.expectMessage(EXPECTED_EXCEPTION_MESSAGE_CUSTOM);

        // do test
        ExecutionNodeFactory.newInstance(CUSTOM_EXECUTION_NODE_FACTORY_NAME);
    }

    @Test
    public void newInstance_nullFactoryName() throws Exception
    {
        // Prepare expectations
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage(EXPECTED_EXCEPTION_MESSAGE_NO_FACTORY_NAME);

        // do test
        ExecutionNodeFactory.newInstance((String)null);
    }

    @Test
    public void newInstance_customFactoryNameAndClassLoader() throws Exception
    {
        // Prepare expectations
        this.expectedException.expect(IllegalStateException.class);
        this.expectedException.expectMessage(EXPECTED_EXCEPTION_MESSAGE_CUSTOM);

        // do test
        ExecutionNodeFactory.newInstance(CUSTOM_EXECUTION_NODE_FACTORY_NAME, this.getClass().getClassLoader());
    }

    @Test
    public void newInstance_nullFactoryNameAndClassLoader() throws Exception
    {
        // Prepare expectations
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage(EXPECTED_EXCEPTION_MESSAGE_NO_FACTORY_NAME);

        // do test
        ExecutionNodeFactory.newInstance(null, null);
    }
}
