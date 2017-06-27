package io.nxnet.tomrun.property.def;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.jodah.concurrentunit.Waiter;

import io.nxnet.tomrun.alias.AliasRegistry;
import io.nxnet.tomrun.alias.AliasRegistryFactory;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;
import io.nxnet.tomrun.property.PropertyFactory;
import io.nxnet.tomrun.property.RunContext;
import io.nxnet.tomrun.resolver.ResolveStrategy;
import io.nxnet.tomrun.resolver.def.LiteralResolver;

import io.nxnet.tomrun.property.def.DefaultPropertyFactory;
import io.nxnet.tomrun.property.def.GloballyScopedProperty;
import io.nxnet.tomrun.property.def.HostScopedProperty;
import io.nxnet.tomrun.property.def.ProcessScopedProperty;
import io.nxnet.tomrun.property.def.RunScopedProperty;
import io.nxnet.tomrun.property.def.ThreadScopedProperty;
import io.nxnet.tomrun.property.def.util.FixedNumberDecorator;
import io.nxnet.tomrun.property.def.util.FixedNumberDecorator2;
import io.nxnet.tomrun.property.def.util.FixedNumberResolver;

public class DefaultPropertyFactoryTest
{
    public static final String EXPECTED_EXCEPTION_NO_PROPERTY_ELEMENT = "property element is null";

    public static final String EXPECTED_EXCEPTION_NO_PROPERTY_ELEMENT_NAME = "property element name is null";

    public static final String EXPECTED_EXCEPTION_NO_PROPERTY_ELEMENT_VALUE_RESOLVER = 
            "property element value resolver is null";

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private DefaultPropertyFactory defaultPropertyFactory;

    @Before
    public void setUp() throws Exception
    {
        // Reset value counter
        FixedNumberResolver.VALUE_COUNTER.set(0);

        this.defaultPropertyFactory = (DefaultPropertyFactory)PropertyFactory.newInstance();
        assertNotNull("default property factory not found", this.defaultPropertyFactory);
        assertEquals("unexpected property factory", DefaultPropertyFactory.class, 
                this.defaultPropertyFactory.getClass());
        assertTrue("Singleton expected", this.defaultPropertyFactory == PropertyFactory.newInstance());
    }

    @Test
    public void getProperty_nullArgument() throws Exception
    {
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage(EXPECTED_EXCEPTION_NO_PROPERTY_ELEMENT);
        this.defaultPropertyFactory.getProperty(null);
    }

    @Test
    public void getProperty_noName() throws Exception
    {
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage(EXPECTED_EXCEPTION_NO_PROPERTY_ELEMENT_NAME);
        this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withValue("foo")
                .withValueResolver("bar")
                .build());
    }

    @Test
    public void getProperty_noValueResolver() throws Exception
    {
        final String expectedPropertyName = "myprop";
        final String expectedPropertyValue = "foo";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .build());
        assertNotNull("property instance expected", property);
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());

        String actualPropertyValue = property.getValue(RunContext.builder().build());
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property resolve strategy", ResolveStrategy.PER_RUN, property.getResolveStrategy());
        assertNull("Unexpected property interpolation strategy", property.getInterpolationStrategy());

        assertTrue("Property should return given value reference", expectedPropertyValue == actualPropertyValue);
        assertTrue("Property should return same value reference", 
                actualPropertyValue == property.getValue(RunContext.builder().build()));
        assertTrue("Property should always return same value reference", 
                actualPropertyValue == property.getValue(RunContext.builder().build()));
    }

    @Test
    public void getProperty_globalScopedProperty() throws Exception
    {
        final String expectedPropertyName = "myprop";
        final String expectedPropertyValue = "88";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("once")
                .build());
        String actualPropertyValue = property.getValue(null);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", GloballyScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue);
        assertTrue("Property should return same value reference", actualPropertyValue == property.getValue(null));
        assertTrue("Property should always return same value reference", 
                actualPropertyValue == property.getValue(null));
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_globalScopedPropertyMultithreaded() throws Exception
    {
        final Waiter waiter = new Waiter();

        final String expectedPropertyName = "myprop";
        final String expectedPropertyValue = "99";
        final Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("once")
                .build());

        final AtomicInteger threadCounter = new AtomicInteger(0);
        for (int i = 0 ; i < 100 ; i++)
        {
            Thread thread = new Thread(new Runnable() {
                
                @Override
                public void run()
                {
                    try
                    {
                        // try to sleep to achieve even more randomness
                        Thread.sleep((int)(Math.random() * 50));

                        String threadPropertyValue = null;
                        for (int j = 0; j < 100 ; j++)
                        {
                            // Run context for i-th thread's j-th run
                            RunContext runContext = RunContext.builder().withHostName("localhost").withPid("1000")
                                    .withThreadName(Thread.currentThread().getName()).withRunNumber(j).build();

                            // Resolve and assert value
                            String actualPropertyValue = property.getValue(runContext);
                            if (j == 0) threadPropertyValue = actualPropertyValue;
                            waiter.assertEquals(expectedPropertyValue, actualPropertyValue);
                            waiter.assertEquals(expectedPropertyName, property.getName());
                            waiter.assertEquals(GloballyScopedProperty.class, property.getClass());
                            waiter.assertFalse(expectedPropertyValue == actualPropertyValue);
                            waiter.assertTrue(actualPropertyValue == property.getValue(runContext));
                            waiter.assertTrue(actualPropertyValue == threadPropertyValue);
                        }

                        threadCounter.incrementAndGet();
                        waiter.resume();;
                    }
                    catch (PropertyException e)
                    {
                        throw new IllegalStateException(e);
                    }
                    catch (InterruptedException e)
                    {
                        throw new IllegalStateException(e);
                    }
                }
            });

            thread.start();
        }

        waiter.await(5000, 100);

        assertEquals("Unexpected number of threads run", 100, threadCounter.get());
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_invalidResolveStrategyString() throws Exception
    {
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage(
                "No enum constant io.nxnet.tomrun.resolver.ResolveStrategy.ONCEEEEEEEE");
        this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName("myprop")
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("onceeeeeeee")
                .build());
    }

    @Test
    public void getProperty_runScopedPropertyImplicit() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "1000";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .build());
        assertNotNull("property instance expected", property);
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());

        String actualPropertyValue = property.getValue(RunContext.builder().build());
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property resolve strategy", ResolveStrategy.PER_RUN, property.getResolveStrategy());
        assertNull("Unexpected property interpolation strategy", property.getInterpolationStrategy());

        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue);
        assertTrue("Property should return same value reference", 
                actualPropertyValue == property.getValue(RunContext.builder().build()));
        assertTrue("Property should always return same value reference", 
                actualPropertyValue == property.getValue(RunContext.builder().build()));
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_runScopedPropertyImplicitWithNoValue() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "100";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValueResolver(FixedNumberResolver.class.getName())
                .build());
        assertNotNull("property instance expected", property);
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());

        String actualPropertyValue = property.getValue(RunContext.builder().build());
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property resolve strategy", ResolveStrategy.PER_RUN, property.getResolveStrategy());
        assertNull("Unexpected property interpolation strategy", property.getInterpolationStrategy());

        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue);
        assertTrue("Property should return same value reference", 
                actualPropertyValue == property.getValue(RunContext.builder().build()));
        assertTrue("Property should always return same value reference", 
                actualPropertyValue == property.getValue(RunContext.builder().build()));
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_hostScopedProperty() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "55";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perHost")
                .build());
        RunContext runContext = RunContext.builder().withHostName("localhost").build();
        String actualPropertyValue = property.getValue(runContext);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", HostScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue);
        assertTrue("Property should return same value reference", actualPropertyValue == property.getValue(runContext));
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_hostScopedPropertyMultithreaded() throws Exception
    {
        final Waiter waiter = new Waiter();

        final String expectedPropertyName = "myprop";
        final String expectedPropertyValue = "66";
        final Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perHost")
                .build());

        final AtomicInteger threadCounter = new AtomicInteger(0);
        for (int i = 0 ; i < 100 ; i++)
        {
            Thread thread = new Thread(new Runnable() {
                
                @Override
                public void run()
                {
                    try
                    {
                        // try to sleep to achieve even more randomness
                        Thread.sleep((int)(Math.random() * 50));

                        String threadPropertyValue = null;
                        for (int j = 0; j < 100 ; j++)
                        {
                            // Run context for i-th thread's j-th run
                            RunContext runContext = RunContext.builder().withHostName("localhost").withPid("1000")
                                    .withThreadName(Thread.currentThread().getName()).withRunNumber(j).build();

                            // Resolve and assert value
                            String actualPropertyValue = property.getValue(runContext);
                            if (j == 0) threadPropertyValue = actualPropertyValue;
                            waiter.assertEquals(expectedPropertyValue, actualPropertyValue);
                            waiter.assertEquals(expectedPropertyName, property.getName());
                            waiter.assertEquals(HostScopedProperty.class, property.getClass());
                            waiter.assertFalse(expectedPropertyValue == actualPropertyValue);
                            waiter.assertTrue(actualPropertyValue == property.getValue(runContext));
                            waiter.assertTrue(actualPropertyValue == threadPropertyValue);
                        }

                        threadCounter.incrementAndGet();
                        waiter.resume();;
                    }
                    catch (PropertyException e)
                    {
                        throw new IllegalStateException(e);
                    }
                    catch (InterruptedException e)
                    {
                        throw new IllegalStateException(e);
                    }
                }
            });

            thread.start();
        }

        waiter.await(5000, 100);

        assertEquals("Unexpected number of threads run", 100, threadCounter.get());
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_hostScopedPropertyWithNoRunContext() throws Exception
    {
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName("myprop")
                .withValue("foo")
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perHost")
                .build());
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage("run context argument is null");
        property.getValue(null);
    }

    @Test
    public void getProperty_hostScopedPropertyWithRunContextWithNoHostname() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "19";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perHost")
                .build());
        RunContext runContext = RunContext.builder().build();
        String actualPropertyValue = property.getValue(runContext);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", HostScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue);
        assertTrue("Property should return same value reference", actualPropertyValue == property.getValue(runContext));
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_processScopedProperty() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "22";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perProcess")
                .build());
        RunContext runContext = RunContext.builder().withPid("1000").build();
        String actualPropertyValue = property.getValue(runContext);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", ProcessScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue);
        assertTrue("Property should return same value reference", actualPropertyValue == property.getValue(runContext));
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_processScopedPropertyMultithreaded() throws Exception
    {
        final Waiter waiter = new Waiter();

        final String expectedPropertyName = "myprop";
        final String expectedPropertyValue = "22";
        final Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perProcess")
                .build());

        final AtomicInteger threadCounter = new AtomicInteger(0);
        for (int i = 0 ; i < 100 ; i++)
        {
            Thread thread = new Thread(new Runnable() {
                
                @Override
                public void run()
                {
                    try
                    {
                        // try to sleep to achieve even more randomness
                        Thread.sleep((int)(Math.random() * 50));

                        String threadPropertyValue = null;
                        for (int j = 0; j < 100 ; j++)
                        {
                            // Run context for i-th thread's j-th run
                            RunContext runContext = RunContext.builder().withHostName("localhost").withPid("1000")
                                    .withThreadName(Thread.currentThread().getName()).withRunNumber(j).build();

                            // Resolve and assert value
                            String actualPropertyValue = property.getValue(runContext);
                            if (j == 0) threadPropertyValue = actualPropertyValue;
                            waiter.assertEquals(expectedPropertyValue, actualPropertyValue);
                            waiter.assertEquals(expectedPropertyName, property.getName());
                            waiter.assertEquals(ProcessScopedProperty.class, property.getClass());
                            waiter.assertFalse(expectedPropertyValue == actualPropertyValue);
                            waiter.assertTrue(actualPropertyValue == property.getValue(runContext));
                            waiter.assertTrue(actualPropertyValue == threadPropertyValue);
                        }

                        threadCounter.incrementAndGet();
                        waiter.resume();;
                    }
                    catch (PropertyException e)
                    {
                        throw new IllegalStateException(e);
                    }
                    catch (InterruptedException e)
                    {
                        throw new IllegalStateException(e);
                    }
                }
            });

            thread.start();
        }

        waiter.await(5000, 100);

        assertEquals("Unexpected number of threads run", 100, threadCounter.get());
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_processScopedPropertyWithNoRunContext() throws Exception
    {
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName("myprop")
                .withValue("foo")
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perProcess")
                .build());
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage("run context argument is null");
        property.getValue(null);
    }

    @Test
    public void getProperty_processScopedPropertyWithRunContextWithNoPid() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "18";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perProcess")
                .build());
        RunContext runContext = RunContext.builder().build();
        String actualPropertyValue = property.getValue(runContext);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", ProcessScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue);
        assertTrue("Property should return same value reference", actualPropertyValue == property.getValue(runContext));
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_threadScopedProperty() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "55";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perThread")
                .build());

        // Run context for 1st thread
        RunContext runContext1 = RunContext.builder().withPid("1000").withThreadName("thread1").build();
        String actualPropertyValue1 = property.getValue(runContext1);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue1);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", ThreadScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue1);
        assertTrue("Property should return same value reference", 
                actualPropertyValue1 == property.getValue(runContext1));

        // Run context for 2nd thread
        RunContext runContext2 = RunContext.builder().withPid("1000").withThreadName("thread2").build();
        String actualPropertyValue2 = property.getValue(runContext2);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue2);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", ThreadScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue2);
        assertTrue("Property should return same value reference", 
                actualPropertyValue2 == property.getValue(runContext2));

        assertEquals("Equal values expected", actualPropertyValue1, actualPropertyValue2);
        assertFalse("Per thread instances of property values expected", actualPropertyValue1 == actualPropertyValue2);
        assertEquals("Unexpected number of values created", 2, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_threadScopedPropertyMultithreaded() throws Exception
    {
        final Waiter waiter = new Waiter();
        
        final String expectedPropertyName = "myprop";
        final String expectedPropertyValue = "55";
        final Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perThread")
                .build());

        final AtomicInteger threadCounter = new AtomicInteger(0);
        for (int i = 0 ; i < 100 ; i++)
        {
            Thread thread = new Thread(new Runnable() {
                
                @Override
                public void run()
                {
                    try
                    {
                        // try to sleep to achieve even more randomness
                        Thread.sleep((int)(Math.random() * 50));

                        String threadPropertyValue = null;
                        for (int j = 0; j < 100 ; j++)
                        {
                            // Run context for i-th thread's j-th run
                            RunContext runContext = RunContext.builder().withHostName("localhost").withPid("1000")
                                    .withThreadName(Thread.currentThread().getName()).withRunNumber(j).build();

                            // Resolve and assert value
                            String actualPropertyValue = property.getValue(runContext);
                            if (j == 0) threadPropertyValue = actualPropertyValue;
                            waiter.assertEquals(expectedPropertyValue, actualPropertyValue);
                            waiter.assertEquals(expectedPropertyName, property.getName());
                            waiter.assertEquals(ThreadScopedProperty.class, property.getClass());
                            waiter.assertFalse(expectedPropertyValue == actualPropertyValue);
                            waiter.assertTrue(actualPropertyValue == property.getValue(runContext));
                            waiter.assertTrue(actualPropertyValue == threadPropertyValue);
                        }

                        threadCounter.incrementAndGet();
                        waiter.resume();
                    }
                    catch (PropertyException e)
                    {
                        throw new IllegalStateException(e);
                    }
                    catch (InterruptedException e)
                    {
                        throw new IllegalStateException(e);
                    }
                }
            });

            thread.start();
        }

        waiter.await(5000, 100);

        assertEquals("Unexpected number of threads run", 100, threadCounter.get());
        assertEquals("Unexpected number of values created", 100, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_threadScopedPropertyWithNoRunContext() throws Exception
    {
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName("myprop")
                .withValue("foo")
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perThread")
                .build());
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage("run context argument is null");
        property.getValue(null);
    }

    @Test
    public void getProperty_threadScopedPropertyWithRunContextWithPidOnly() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "44";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perThread")
                .build());

        RunContext runContext1 = RunContext.builder().withPid("1000").build();
        String actualPropertyValue1 = property.getValue(runContext1);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue1);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", ThreadScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue1);
        assertTrue("Property should return same value reference", 
                actualPropertyValue1 == property.getValue(runContext1));

        RunContext runContext2 = RunContext.builder().withPid("1000").build();
        String actualPropertyValue2 = property.getValue(runContext2);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue2);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", ThreadScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue2);
        assertTrue("Property should return same value reference", 
                actualPropertyValue2 == property.getValue(runContext2));

        assertEquals("Equal values expected", actualPropertyValue1, actualPropertyValue2);
        assertTrue("Same instances of property values expected because we are not declaring thread in run context", 
                actualPropertyValue1 == actualPropertyValue2);
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_runScopedProperty() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "77";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perRun")
                .build());

        // Run context for 1st thread 1st run
        RunContext runContext_1000_1_1 = RunContext.builder().withPid("1000").withThreadName("thread1").withRunNumber(1)
                .build();
        String actualPropertyValue_1000_1_1 = property.getValue(runContext_1000_1_1);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_1_1);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_1_1);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_1_1 == property.getValue(runContext_1000_1_1));

        // Run context for 1st thread 2nd run
        RunContext runContext_1000_1_2 = RunContext.builder().withPid("1000").withThreadName("thread1").withRunNumber(2)
                .build();
        String actualPropertyValue_1000_1_2 = property.getValue(runContext_1000_1_2);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_1_2);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_1_2);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_1_2 == property.getValue(runContext_1000_1_2));

        // Run context for 2nd thread 1st run
        RunContext runContext_1000_2_1 = RunContext.builder().withPid("1000").withThreadName("thread2").withRunNumber(1)
                .build();
        String actualPropertyValue_1000_2_1 = property.getValue(runContext_1000_2_1);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_2_1);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_2_1);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_2_1 == property.getValue(runContext_1000_2_1));

        // Run context for 2nd thread 2nd run
        RunContext runContext_1000_2_2 = RunContext.builder().withPid("1000").withThreadName("thread2").withRunNumber(2)
                .build();
        String actualPropertyValue_1000_2_2 = property.getValue(runContext_1000_2_2);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_2_2);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_2_2);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_2_2 == property.getValue(runContext_1000_2_2));

        assertEquals("Equal values expected", actualPropertyValue_1000_1_1, actualPropertyValue_1000_1_2);
        assertEquals("Equal values expected", actualPropertyValue_1000_2_1, actualPropertyValue_1000_2_2);
        assertEquals("Equal values expected", actualPropertyValue_1000_1_1, actualPropertyValue_1000_2_1);
        assertFalse("Per run instances of property values expected", 
                actualPropertyValue_1000_1_1 == actualPropertyValue_1000_1_2);
        assertFalse("Per run instances of property values expected", 
                actualPropertyValue_1000_2_1 == actualPropertyValue_1000_2_2);
        assertFalse("Per run instances of property values expected", 
                actualPropertyValue_1000_1_1 == actualPropertyValue_1000_2_1);
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_1_1 == property.getValue(runContext_1000_1_1));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_1_2 == property.getValue(runContext_1000_1_2));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_2_1 == property.getValue(runContext_1000_2_1));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_2_2 == property.getValue(runContext_1000_2_2));
        assertEquals("Unexpected number of values created", 4, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_runScopedPropertyMultithreaded() throws Exception
    {
        final Waiter waiter = new Waiter();

        final String expectedPropertyName = "myprop";
        final String expectedPropertyValue = "77";
        final Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perRun")
                .build());

        final AtomicInteger threadCounter = new AtomicInteger(0);
        for (int i = 0 ; i < 100 ; i++)
        {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run()
                {
                    try
                    {
                        // try to sleep to achieve even more randomness
                        Thread.sleep((int)(Math.random() * 50));

                        String previousRunPropertyValue = null;
                        for (int j = 0 ; j < 100 ; j++)
                        {
                            // Run context for i-th thread's j-th run
                            RunContext runContext = RunContext.builder().withHostName("localhost").withPid("1000")
                                    .withThreadName(Thread.currentThread().getName()).withRunNumber(j).build();

                            // Get and assert property for j-th run
                            String actualPropertyValue = property.getValue(runContext);
                            waiter.assertEquals(expectedPropertyValue, actualPropertyValue);
                            waiter.assertEquals(expectedPropertyName, property.getName());
                            waiter.assertEquals(RunScopedProperty.class, property.getClass());
                            waiter.assertFalse(expectedPropertyValue == actualPropertyValue);
                            waiter.assertTrue(actualPropertyValue == property.getValue(runContext));
                            waiter.assertFalse(actualPropertyValue == previousRunPropertyValue);

                            previousRunPropertyValue = actualPropertyValue;
                        }

                        threadCounter.incrementAndGet();
                        waiter.resume();
                    }
                    catch (PropertyException e)
                    {
                        throw new IllegalStateException(e);
                    }
                    catch (InterruptedException e)
                    {
                        throw new IllegalStateException(e);
                    }
                }
            });

            thread.start();
        }

        waiter.await(5000, 100);

        assertEquals("Unexpected number of threads run", 100, threadCounter.get());
        assertEquals("Unexpected number of values created", 10000, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_runScopedPropertyWithNoRunContext() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "66";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perRun")
                .build());

        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage("run context argument is null");
        property.getValue(null);
    }

    @Test
    public void getProperty_runScopedPropertyWithHostname() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "33";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perRun")
                .build());

        // Run context for 1st thread 1st run
        RunContext runContext_1000_1_1 = RunContext.builder().withHostName("localhost").build();
        String actualPropertyValue_1000_1_1 = property.getValue(runContext_1000_1_1);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_1_1);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_1_1);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_1_1 == property.getValue(runContext_1000_1_1));

        // Run context for 1st thread 2nd run
        RunContext runContext_1000_1_2 = RunContext.builder().withHostName("localhost").build();
        String actualPropertyValue_1000_1_2 = property.getValue(runContext_1000_1_2);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_1_2);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_1_2);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_1_2 == property.getValue(runContext_1000_1_2));

        // Run context for 2nd thread 1st run
        RunContext runContext_1000_2_1 = RunContext.builder().withHostName("localhost").build();
        String actualPropertyValue_1000_2_1 = property.getValue(runContext_1000_2_1);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_2_1);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_2_1);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_2_1 == property.getValue(runContext_1000_2_1));

        // Run context for 2nd thread 2nd run
        RunContext runContext_1000_2_2 = RunContext.builder().withHostName("localhost").build();
        String actualPropertyValue_1000_2_2 = property.getValue(runContext_1000_2_2);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_2_2);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_2_2);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_2_2 == property.getValue(runContext_1000_2_2));

        assertEquals("Equal values expected", actualPropertyValue_1000_1_1, actualPropertyValue_1000_1_2);
        assertEquals("Equal values expected", actualPropertyValue_1000_2_1, actualPropertyValue_1000_2_2);
        assertEquals("Equal values expected", actualPropertyValue_1000_1_1, actualPropertyValue_1000_2_1);
        assertTrue("Same instances of property values expected as we are not declaring run number", 
                actualPropertyValue_1000_1_1 == actualPropertyValue_1000_1_2);
        assertTrue("Same instances of property values expected as we are not declaring run number", 
                actualPropertyValue_1000_2_1 == actualPropertyValue_1000_2_2);
        assertTrue("Same instances of property values expected as we are not declaring run number", 
                actualPropertyValue_1000_1_1 == actualPropertyValue_1000_2_1);
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_1_1 == property.getValue(runContext_1000_1_1));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_1_2 == property.getValue(runContext_1000_1_2));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_2_1 == property.getValue(runContext_1000_2_1));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_2_2 == property.getValue(runContext_1000_2_2));
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_runScopedPropertyWithHostnameAndPid() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "33";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perRun")
                .build());

        // Run context for 1st thread 1st run
        RunContext runContext_1000_1_1 = RunContext.builder().withHostName("localhost").withPid("1000").build();
        String actualPropertyValue_1000_1_1 = property.getValue(runContext_1000_1_1);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_1_1);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_1_1);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_1_1 == property.getValue(runContext_1000_1_1));

        // Run context for 1st thread 2nd run
        RunContext runContext_1000_1_2 = RunContext.builder().withHostName("localhost").withPid("1000").build();
        String actualPropertyValue_1000_1_2 = property.getValue(runContext_1000_1_2);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_1_2);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_1_2);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_1_2 == property.getValue(runContext_1000_1_2));

        // Run context for 2nd thread 1st run
        RunContext runContext_1000_2_1 = RunContext.builder().withHostName("localhost").withPid("1000").build();
        String actualPropertyValue_1000_2_1 = property.getValue(runContext_1000_2_1);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_2_1);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_2_1);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_2_1 == property.getValue(runContext_1000_2_1));

        // Run context for 2nd thread 2nd run
        RunContext runContext_1000_2_2 = RunContext.builder().withHostName("localhost").withPid("1000").build();
        String actualPropertyValue_1000_2_2 = property.getValue(runContext_1000_2_2);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_2_2);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_2_2);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_2_2 == property.getValue(runContext_1000_2_2));

        assertEquals("Equal values expected", actualPropertyValue_1000_1_1, actualPropertyValue_1000_1_2);
        assertEquals("Equal values expected", actualPropertyValue_1000_2_1, actualPropertyValue_1000_2_2);
        assertEquals("Equal values expected", actualPropertyValue_1000_1_1, actualPropertyValue_1000_2_1);
        assertTrue("Same instances of property values expected as we are not declaring run number", 
                actualPropertyValue_1000_1_1 == actualPropertyValue_1000_1_2);
        assertTrue("Same instances of property values expected as we are not declaring run number", 
                actualPropertyValue_1000_2_1 == actualPropertyValue_1000_2_2);
        assertTrue("Same instances of property values expected as we are not declaring run number", 
                actualPropertyValue_1000_1_1 == actualPropertyValue_1000_2_1);
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_1_1 == property.getValue(runContext_1000_1_1));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_1_2 == property.getValue(runContext_1000_1_2));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_2_1 == property.getValue(runContext_1000_2_1));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_2_2 == property.getValue(runContext_1000_2_2));
        assertEquals("Unexpected number of values created", 1, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_runScopedPropertyWithHostnameAndPidAndThreadName() throws Exception
    {
        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "11";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy("perRun")
                .build());

        // Run context for 1st thread 1st run
        RunContext runContext_1000_1_1 = RunContext.builder().withHostName("localhost").withPid("1000")
                .withThreadName("thread1").build();
        String actualPropertyValue_1000_1_1 = property.getValue(runContext_1000_1_1);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_1_1);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_1_1);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_1_1 == property.getValue(runContext_1000_1_1));

        // Run context for 1st thread 2nd run
        RunContext runContext_1000_1_2 = RunContext.builder().withHostName("localhost").withPid("1000")
                .withThreadName("thread1").build();
        String actualPropertyValue_1000_1_2 = property.getValue(runContext_1000_1_2);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_1_2);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_1_2);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_1_2 == property.getValue(runContext_1000_1_2));

        // Run context for 2nd thread 1st run
        RunContext runContext_1000_2_1 = RunContext.builder().withHostName("localhost").withPid("1000")
                .withThreadName("thread2").build();
        String actualPropertyValue_1000_2_1 = property.getValue(runContext_1000_2_1);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_2_1);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_2_1);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_2_1 == property.getValue(runContext_1000_2_1));

        // Run context for 2nd thread 2nd run
        RunContext runContext_1000_2_2 = RunContext.builder().withHostName("localhost").withPid("1000")
                .withThreadName("thread2").build();
        String actualPropertyValue_1000_2_2 = property.getValue(runContext_1000_2_2);
        assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue_1000_2_2);
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property instance", RunScopedProperty.class, property.getClass());
        assertFalse("Property should return its own value", expectedPropertyValue == actualPropertyValue_1000_2_2);
        assertTrue("Property should return same value reference", 
                actualPropertyValue_1000_2_2 == property.getValue(runContext_1000_2_2));

        assertEquals("Equal values expected", actualPropertyValue_1000_1_1, actualPropertyValue_1000_1_2);
        assertEquals("Equal values expected", actualPropertyValue_1000_2_1, actualPropertyValue_1000_2_2);
        assertEquals("Equal values expected", actualPropertyValue_1000_1_1, actualPropertyValue_1000_2_1);
        assertTrue("Same instances of property values expected as we are not declaring run number", 
                actualPropertyValue_1000_1_1 == actualPropertyValue_1000_1_2);
        assertTrue("Same instances of property values expected as we are not declaring run number", 
                actualPropertyValue_1000_2_1 == actualPropertyValue_1000_2_2);
        assertFalse("Per thread instances of property values expected as we are not declaring run number", 
                actualPropertyValue_1000_1_1 == actualPropertyValue_1000_2_1);
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_1_1 == property.getValue(runContext_1000_1_1));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_1_2 == property.getValue(runContext_1000_1_2));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_2_1 == property.getValue(runContext_1000_2_1));
        assertTrue("Same instance expected for same run", 
                actualPropertyValue_1000_2_2 == property.getValue(runContext_1000_2_2));
        assertEquals("Unexpected number of values created", 2, FixedNumberResolver.VALUE_COUNTER.get());
    }

    @Test
    public void getProperty_globalScopedPropertyWithDecorator() throws Exception
    {
        String expectedPropertyName = "myprop";
        String undecoratedPropertyValue = "100";
        String expectedPropertyValue = undecoratedPropertyValue + "000";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(undecoratedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy(ResolveStrategy.ONCE.toString())
                .withFilters(FixedNumberDecorator.class.getName())
                .build());
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property value", expectedPropertyValue, property.getValue(null));
    }

    @Test
    public void getProperty_globalScopedPropertyWithDecorators() 
            throws Exception
    {
        String expectedPropertyName = "myprop";
        String undecoratedPropertyValue = "100";
        String expectedPropertyValue = undecoratedPropertyValue + "111" + "000";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(undecoratedPropertyValue)
                .withValueResolver(FixedNumberResolver.class.getName())
                .withResolveStrategy(ResolveStrategy.ONCE.toString())
                .withFilters(FixedNumberDecorator.class.getName() + " , " + FixedNumberDecorator2.class.getName())
                .build());
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property value", expectedPropertyValue, property.getValue(null));
    }

    @Test
    public void getProperty_literalResolverAlias() throws Exception
    {
        AliasRegistry aliasRegistry = AliasRegistryFactory.newInstance().getAliasRegistry();
        aliasRegistry.registerAlias("valueResolver", LiteralResolver.class.getName(), "foo");

        String expectedPropertyName = "myprop";
        String expectedPropertyValue = "1000";
        Property property = this.defaultPropertyFactory.getProperty(io.nxnet.tomrun.model.Property.builder()
                .withName(expectedPropertyName)
                .withValue(expectedPropertyValue)
                .withValueResolver("foo")
                .build());
        assertEquals("Unexpected property name", expectedPropertyName, property.getName());
        assertEquals("Unexpected property value", expectedPropertyValue,
                property.getValue(RunContext.builder().build()));
    }
}
