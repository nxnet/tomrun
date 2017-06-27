package io.nxnet.tomrun.resolver.def;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import io.nxnet.tomrun.resolver.ValueResolver;
import io.nxnet.tomrun.resolver.ValueResolverFactory;

import io.nxnet.tomrun.resolver.def.DateResolver;
import io.nxnet.tomrun.resolver.def.DefaultValueResolverFactory;
import io.nxnet.tomrun.resolver.def.LiteralResolver;
import io.nxnet.tomrun.resolver.def.NullResolver;

public class DefaultValueResolverFactoryTest
{
    private DefaultValueResolverFactory defaultValueResolverFactory;

    @Before
    public void setUp() throws Exception
    {
        this.defaultValueResolverFactory = (DefaultValueResolverFactory)ValueResolverFactory.newInstance();
        assertNotNull("default value resolver factory not found", this.defaultValueResolverFactory);
        assertEquals("Unexpected factory class", DefaultValueResolverFactory.class, 
                this.defaultValueResolverFactory.getClass());
        assertTrue("singleton expected", this.defaultValueResolverFactory == ValueResolverFactory.newInstance());
    }

    @Test
    public void getValueResolver_LiteralResolver() throws Exception
    {
        ValueResolver valueResolver = this.defaultValueResolverFactory
                .getValueResolver(LiteralResolver.class.getName(), "foo");
        assertNotNull("resolver not found", valueResolver);
        assertEquals("Unexpected resolver class", LiteralResolver.class, valueResolver.getClass());
        assertEquals("unexpected value resolved", "foo", valueResolver.resolve());
        assertFalse("New instance expected", valueResolver == this.defaultValueResolverFactory
                .getValueResolver(LiteralResolver.class.getName(), "foo"));
    }

    @Test
    public void getValueResolver_NullResolver() throws Exception
    {
        ValueResolver valueResolver = this.defaultValueResolverFactory
                .getValueResolver(NullResolver.class.getName());
        assertNotNull("resolver not found", valueResolver);
        assertEquals("Unexpected resolver class", NullResolver.class, valueResolver.getClass());
        assertNull("unexpected value resolved", valueResolver.resolve());
        assertFalse("New instance expected", valueResolver == this.defaultValueResolverFactory
                .getValueResolver(NullResolver.class.getName()));
    }

    @Test
    public void getValueResolver_DateResolver() throws Exception
    {
        // prepare expected value
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1);
        String expectedValueResolved = new SimpleDateFormat("yyyyMMdd").format(tomorrow.getTime());

        // do test
        ValueResolver valueResolver = this.defaultValueResolverFactory
                .getValueResolver(DateResolver.class.getName(), "yyyyMMdd", 1, Locale.ENGLISH);
        assertNotNull("resolver not found", valueResolver);
        assertEquals("Unexpected resolver class", DateResolver.class, valueResolver.getClass());
        assertEquals("unexpected value resolved", expectedValueResolved, valueResolver.resolve());
        assertFalse("New instance expected", valueResolver == this.defaultValueResolverFactory
                .getValueResolver(DateResolver.class.getName(), "yyyyMMdd", 1, Locale.ENGLISH));
    }
}
