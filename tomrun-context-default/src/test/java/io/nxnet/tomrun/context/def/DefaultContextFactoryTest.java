package io.nxnet.tomrun.context.def;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import io.nxnet.tomrun.alias.AliasRegistry;
import io.nxnet.tomrun.alias.AliasRegistryFactory;
import io.nxnet.tomrun.context.Context;
import io.nxnet.tomrun.context.ContextFactory;
import io.nxnet.tomrun.interpolation.InterpolationStrategy;
import io.nxnet.tomrun.model.Properties;
import io.nxnet.tomrun.model.Property;
import io.nxnet.tomrun.resolver.def.DefaultValueResolverFactory;
import io.nxnet.tomrun.resolver.def.LiteralResolver;

import io.nxnet.tomrun.context.def.DefaultContextFactory;

public class DefaultContextFactoryTest
{
    private DefaultContextFactory defaultContextFactory;

    private AliasRegistry aliasRegistry;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception
    {
        // Set context factory
        this.defaultContextFactory = (DefaultContextFactory)ContextFactory.newInstance();
        assertNotNull("context factory not found", this.defaultContextFactory);
        assertEquals("unexpected context factory", DefaultContextFactory.class, this.defaultContextFactory.getClass());
        assertTrue("same factory instance expected", this.defaultContextFactory == ContextFactory.newInstance());

        // Register aliases
        this.aliasRegistry = AliasRegistryFactory.newInstance().getAliasRegistry();
        this.aliasRegistry.registerAlias(DefaultValueResolverFactory.DEFAULT_ALIAS_NAMESPACE,
                LiteralResolver.class.getName(), "res");
    }

    @Test
    public void propertyFactory() throws Exception
    {
        assertNotNull("context factory not initialized correctly, missing property factory", 
                this.defaultContextFactory.getPropertyFactory());
    }

    @Test
    public void getServiceName() throws Exception
    {
        assertEquals("unexpected factory name", ContextFactory.DEFAULT_FACTORY_NAME,
                this.defaultContextFactory.getServiceName());
    }

    @Test
    public void getContext_nullArgument() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(null);
        assertNotNull("No context created", context);
        assertTrue("No properties expected", context.getPropertyNames().isEmpty());
        assertTrue("No attributes expected", context.getAttributeNames().isEmpty());
        assertNull("No parent context expected", context.getParentContext());
        assertNotNull("no logger found", context.getLogger());
        assertNotNull("no hostname found", context.getHostName());
        assertNotNull("no pid found", context.getPid());
        assertNotNull("no thread name found", context.getThreadName());
        assertNotNull("no run number found", context.getRunNumber());
    }

    @Test
    public void getContext_emptyArgument() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(Properties.builder().build());
        assertNotNull("No context created", context);
        assertTrue("No properties expected", context.getPropertyNames().isEmpty());
        assertTrue("No attributes expected", context.getAttributeNames().isEmpty());
        assertNull("No parent context expected", context.getParentContext());
        assertNotNull("no logger found", context.getLogger());
        assertNotNull("no hostname found", context.getHostName());
        assertNotNull("no pid found", context.getPid());
        assertNotNull("no thread name found", context.getThreadName());
        assertNotNull("no run number found", context.getRunNumber());
    }

    @Test
    public void getContext_separateInstances() throws Exception
    {
        assertTrue("different instances expected", this.defaultContextFactory.getContext(Properties.builder().build())
                != this.defaultContextFactory.getContext(Properties.builder().build()));
    }

    @Test
    public void getContext_noParentContext() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(Properties.builder().build());
        assertNotNull("No context created", context);
        assertNull("No parent context expected", context.getParentContext());
    }

    @Test
    public void getContext_noProperties() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(Properties.builder().build());
        assertNotNull("No context created", context);
        assertTrue("No properties expected", context.getPropertyNames().isEmpty());
    }

    @Test
    public void getContext_oneProperty() throws Exception
    {
        String propertyName = "p1";
        String propertyValue = "p1value";
        Context context = this.defaultContextFactory.getContext(Properties.builder()
                .addProperty(Property.builder().withName(propertyName).withValue(propertyValue)
                        .withValueResolver("res").build())
                .build());
        assertNotNull("No context created", context);
        assertTrue("No attributes expected", context.getAttributeNames().isEmpty());
        assertEquals("One property expected", 1, context.getPropertyNames().size());
        assertTrue("Unexpected property name", context.getPropertyNames().contains(propertyName));
        assertEquals("Unexpected property value", propertyValue, context.getProperty(propertyName));
        assertEquals("Unexpected property value", propertyValue, context.findProperty(propertyName));
    }

    @Test
    public void getContext_twoProperties() throws Exception
    {
        String propertyName1 = "p1";
        String propertyValue1 = "p1value";
        String propertyName2 = "p2";
        String propertyValue2 = "p2value";
        Context context = this.defaultContextFactory.getContext(Properties.builder()
                .addProperty(Property.builder().withName(propertyName1).withValue(propertyValue1)
                        .withValueResolver("res").build())
                .addProperty(Property.builder().withName(propertyName2).withValue(propertyValue2)
                        .withValueResolver("res").build())
                .build());
        assertNotNull("No context created", context);

        assertEquals("Two properties expected", 2, context.getPropertyNames().size());

        assertTrue("Unexpected property name", context.getPropertyNames().contains(propertyName1));
        assertEquals("Unexpected property value", propertyValue1, context.getProperty(propertyName1));
        assertEquals("Unexpected property value", propertyValue1, context.findProperty(propertyName1));

        assertTrue("Unexpected property name", context.getPropertyNames().contains(propertyName2));
        assertEquals("Unexpected property value", propertyValue2, context.getProperty(propertyName2));
        assertEquals("Unexpected property value", propertyValue2, context.findProperty(propertyName2));
    }

    @Test
    public void getContext_noAttributes() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(Properties.builder().build());
        assertNotNull("No context created", context);
        assertTrue("No attributes expected", context.getAttributeNames().isEmpty());
    }

    @Test
    public void getContext_oneAttribute() throws Exception
    {
        String attributeName = "a1";
        String attributeValue = "a1value";
        Context context = this.defaultContextFactory.getContext(Properties.builder().build());
        assertNotNull("No context created", context);
        context.setAttribute(attributeName, attributeValue);
        assertEquals("One attribute expected", 1, context.getAttributeNames().size());
        assertTrue("Unexpected attribute name", context.getAttributeNames().contains(attributeName));
        assertEquals("Unexpected attribute value", attributeValue, context.getAttribute(attributeName));
        assertEquals("Unexpected attribute value", attributeValue, context.findAttribute(attributeName));
    }

    @Test
    public void getContext_twoAttributes() throws Exception
    {
        String attributeName1 = "a1";
        String attributeValue1 = "a1value";
        String attributeName2 = "a2";
        String attributeValue2 = "a2value";
        Context context = this.defaultContextFactory.getContext(Properties.builder().build());
        assertNotNull("No context created", context);
        context.setAttribute(attributeName1, attributeValue1);
        context.setAttribute(attributeName2, attributeValue2);
        assertNotNull("No context created", context);

        assertEquals("Two attributes expected", 2, context.getAttributeNames().size());

        assertTrue("Unexpected attribute name", context.getAttributeNames().contains(attributeName1));
        assertEquals("Unexpected attribute value", attributeValue1, context.getAttribute(attributeName1));
        assertEquals("Unexpected attribute value", attributeValue1, context.findAttribute(attributeName1));

        assertTrue("Unexpected attribute name", context.getAttributeNames().contains(attributeName2));
        assertEquals("Unexpected attribute value", attributeValue2, context.getAttribute(attributeName2));
        assertEquals("Unexpected attribute value", attributeValue2, context.findAttribute(attributeName2));
    }

    @Test
    public void getContext_noPropertiesInterpolation() throws Exception
    {
        String propertyName1 = "p1";
        String propertyValue1 = "p1value";
        String propertyName2 = "p2";
        String propertyValue2 = "${p1}";
        Context context = this.defaultContextFactory.getContext(Properties.builder()
                .addProperty(Property.builder().withName(propertyName1).withValue(propertyValue1)
                        .withValueResolver("res").build())
                .addProperty(Property.builder().withName(propertyName2).withValue(propertyValue2)
                        .withValueResolver("res").withInterpolationStrategy(InterpolationStrategy.NEVER.toString())
                        .build())
                .build());
        assertNotNull("No context created", context);
        assertEquals("Two properties expected", 2, context.getPropertyNames().size());
        assertEquals("Unexpected property value", propertyValue1, context.getProperty(propertyName1));
        assertEquals("Unexpected property value", propertyValue2, context.getProperty(propertyName2));
    }

    @Test
    public void getContext_explicitPropertiesInterpolation() throws Exception
    {
        String propertyName1 = "p1";
        String propertyValue1 = "p1value";
        String propertyName2 = "p2";
        String propertyValue2 = "${p1}";
        Context context = this.defaultContextFactory.getContext(Properties.builder()
                .addProperty(Property.builder().withName(propertyName1).withValue(propertyValue1)
                        .withValueResolver("res").build())
                .addProperty(Property.builder().withName(propertyName2).withValue(propertyValue2)
                        .withValueResolver("res").withInterpolationStrategy(InterpolationStrategy.ALWAYS.toString())
                        .build())
                .build());
        assertNotNull("No context created", context);
        assertEquals("Two properties expected", 2, context.getPropertyNames().size());
        assertEquals("Unexpected property value", propertyValue1, context.getProperty(propertyName1));
        assertEquals("Unexpected property value", propertyValue1, context.getProperty(propertyName2));
        assertFalse("different instances expected because of interpolation strategy", 
                context.getProperty(propertyName2) == context.getProperty(propertyName2));
    }

    @Test
    public void getContext_implicitPropertiesInterpolation() throws Exception
    {
        String propertyName1 = "p1";
        String propertyValue1 = "p1value";
        String propertyName2 = "p2";
        String propertyValue2 = "${p1}";
        Context context = this.defaultContextFactory.getContext(Properties.builder()
                .addProperty(Property.builder().withName(propertyName1).withValue(propertyValue1)
                        .withValueResolver("res").build())
                .addProperty(Property.builder().withName(propertyName2).withValue(propertyValue2)
                        .withValueResolver("res").build())
                .build());
        assertNotNull("No context created", context);
        assertEquals("Two properties expected", 2, context.getPropertyNames().size());
        assertEquals("Unexpected property value", propertyValue1, context.getProperty(propertyName1));
        assertEquals("Unexpected property value", propertyValue1, context.getProperty(propertyName2));
        assertTrue("same instance expected because of interpolation strategy smart detection", 
                context.getProperty(propertyName2) == context.getProperty(propertyName2));
    }

    @Test
    public void getContext_propertiesInterpolationInverseOrderWorksAlso() throws Exception
    {
        String propertyName1 = "p1";
        String propertyValue1 = "${p2}";
        String propertyName2 = "p2";
        String propertyValue2 = "p2value";
        Context context = this.defaultContextFactory.getContext(Properties.builder()
                .addProperty(Property.builder().withName(propertyName1).withValue(propertyValue1)
                        .withValueResolver("res").build())
                .addProperty(Property.builder().withName(propertyName2).withValue(propertyValue2)
                        .withValueResolver("res").build())
                .build());
        assertNotNull("No context created", context);
        assertEquals("Two properties expected", 2, context.getPropertyNames().size());
        assertEquals("Unexpected property value", propertyValue2, context.getProperty(propertyName1));
        assertEquals("Unexpected property value", propertyValue2, context.getProperty(propertyName2));
        assertTrue("same instance expected because of interpolation strategy smart detection", 
                context.getProperty(propertyName1) == context.getProperty(propertyName1));
    }

    @Test
    public void getContext_implicitAttributesInterpolation() throws Exception
    {
        String propertyName1 = "a1";
        String propertyValue1 = "#{myAttribute1}";
        String propertyName2 = "a2";
        String propertyValue2 = "#{myAttribute2.nextCount}";
        Context context = this.defaultContextFactory.getContext(Properties.builder()
                .addProperty(Property.builder().withName(propertyName1).withValue(propertyValue1)
                        .withValueResolver("res").build())
                .addProperty(Property.builder().withName(propertyName2).withValue(propertyValue2)
                        .withValueResolver("res").build())
                .build());
        assertNotNull("No context created", context);
        context.setAttribute("myAttribute1", new MyCounterAttribute(100));
        context.setAttribute("myAttribute2", new MyCounterAttribute(200));
        assertEquals("Two properties expected", 2, context.getPropertyNames().size());
        assertEquals("Unexpected property value", "100", context.getProperty(propertyName1));
        assertFalse("Not the same instance expected as attributes are allways interpolated", 
                context.getProperty(propertyName1) == context.getProperty(propertyName1));
        assertEquals("Unexpected property value", "201", context.getProperty(propertyName2));
        assertFalse("Not the same instance expected as attributes are allways interpolated", 
                context.getProperty(propertyName2) == context.getProperty(propertyName2));
    }

    @Test
    public void getContext_explicitAttributesInterpolation() throws Exception
    {
        String propertyName1 = "a1";
        String propertyValue1 = "#{myAttribute1}";
        String propertyName2 = "a2";
        String propertyValue2 = "#{myAttribute2.nextCount}";
        Context context = this.defaultContextFactory.getContext(Properties.builder()
                .addProperty(Property.builder().withName(propertyName1).withValue(propertyValue1)
                        .withValueResolver("res").withInterpolationStrategy(InterpolationStrategy.ONCE.name()).build())
                .addProperty(Property.builder().withName(propertyName2).withValue(propertyValue2)
                        .withValueResolver("res").build())
                .build());
        assertNotNull("No context created", context);
        context.setAttribute("myAttribute1", new MyCounterAttribute(100));
        context.setAttribute("myAttribute2", new MyCounterAttribute(200));
        assertEquals("Two properties expected", 2, context.getPropertyNames().size());
        assertEquals("Unexpected property value", "100", context.getProperty(propertyName1));
        assertTrue("Same instance expected because of interpolation strategy", 
                context.getProperty(propertyName1) == context.getProperty(propertyName1));
        assertEquals("Unexpected property value", "201", context.getProperty(propertyName2));
        assertFalse("Not the same instance expected as attributes are allways interpolated", 
                context.getProperty(propertyName2) == context.getProperty(propertyName2));
    }

    @Test
    public void getContext_noAttributesInterpolation() throws Exception
    {
        String propertyName1 = "a1";
        String propertyValue1 = "#{myAttribute1}";
        Context context = this.defaultContextFactory.getContext(Properties.builder()
                .addProperty(Property.builder().withName(propertyName1).withValue(propertyValue1)
                        .withValueResolver("res").withInterpolationStrategy(InterpolationStrategy.NEVER.name()).build())
                .build());
        assertNotNull("No context created", context);
        context.setAttribute("myAttribute1", new MyCounterAttribute(100));
        assertEquals("Unexpected number of properties", 1, context.getPropertyNames().size());
        assertEquals("Unexpected property value", "#{myAttribute1}", context.getProperty(propertyName1));
        assertTrue("Same instance expected because of interpolation strategy", 
                context.getProperty(propertyName1) == context.getProperty(propertyName1));
    }

    @Test
    public void getContext_properties() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(null);
        assertNotNull("No context created", context);
        assertTrue("No properties expected", context.getPropertyNames().isEmpty());
    }

    @Test
    public void getContext_attributes() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(null);
        assertNotNull("No context created", context);
        assertTrue("No attributes expected", context.getAttributeNames().isEmpty());
        context.setAttribute("att1", new String("foo"));
        context.setAttribute("att1", new String("bar"));
        context.setAttribute("att2", new Integer(100));
        assertEquals("unexpected number of attributes", 2, context.getAttributeNames().size());
        assertEquals("unexpected attribute", "bar", context.getAttribute("att1"));
        assertEquals("unexpected attribute", Integer.valueOf(100), context.getAttribute("att2"));
    }

    @Test
    public void getContext_parentContext() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(null);
        assertNotNull("No context created", context);
        assertNull("No parent context expected", context.getParentContext());
    }

    @Test
    public void getContext_logger() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(null);
        assertNotNull("No context created", context);
        assertNotNull("no logger found", context.getLogger());
        assertEquals("unexpected logger", "io.nxnet.tomrun.executor", context.getLogger().getName());
    }

    @Test
    public void getContext_hostName() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(null);
        assertNotNull("No context created", context);
        assertNotNull("no hostname found", context.getHostName());
        assertEquals("unexpected hostname", InetAddress.getLocalHost().getLocalHost().getHostName(), 
                context.getHostName());
    }

    @Test
    public void getContext_pid() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(null);
        assertNotNull("No context created", context);
        assertNotNull("no pid found", context.getPid());
        assertEquals("unexpected pid", ManagementFactory.getRuntimeMXBean().getName().split("@")[0], context.getPid());
    }

    @Test
    public void getContext_threadName() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(null);
        assertNotNull("No context created", context);
        assertNotNull("no thread name found", context.getThreadName());
        assertEquals("unexpected thread name", Thread.currentThread().getName(), context.getThreadName());
    }

    @Test
    public void getContext_runNumber() throws Exception
    {
        Context context = this.defaultContextFactory.getContext(null);
        assertNotNull("No context created", context);
        assertNotNull("no run number found", context.getRunNumber());
        assertEquals("unexpected run number", 0, context.getRunNumber());
    }

}
