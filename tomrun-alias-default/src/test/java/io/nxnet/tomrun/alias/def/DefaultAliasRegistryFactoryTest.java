package io.nxnet.tomrun.alias.def;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.nxnet.tomrun.alias.AliasRegistry;
import io.nxnet.tomrun.alias.AliasRegistryFactory;
import io.nxnet.tomrun.service.loader.NamedServiceLoader;

import io.nxnet.tomrun.alias.def.DefaultAliasRegistry;
import io.nxnet.tomrun.alias.def.DefaultAliasRegistryFactory;

public class DefaultAliasRegistryFactoryTest
{
    private DefaultAliasRegistryFactory defaultAliasRegistryFactory;

    @Before
    public void setUp() throws Exception
    {
        this.defaultAliasRegistryFactory = (DefaultAliasRegistryFactory)AliasRegistryFactory.newInstance();
        assertNotNull("default alias registry factory not found", this.defaultAliasRegistryFactory);
        
        NamedServiceLoader namedServiceLoader = NamedServiceLoader.getInstance();
        AliasRegistryFactory f1 = namedServiceLoader.loadService(AliasRegistryFactory.DEFAULT_FACTORY_NAME, 
                AliasRegistryFactory.class, Thread.currentThread().getContextClassLoader());
        AliasRegistryFactory f2 = namedServiceLoader.loadService(AliasRegistryFactory.DEFAULT_FACTORY_NAME, 
                AliasRegistryFactory.class, Thread.currentThread().getContextClassLoader());
        assertTrue("What the fuck", f1 == f2);
        
        assertTrue("singleton factory instance expected", this.defaultAliasRegistryFactory ==
                AliasRegistryFactory.newInstance());
    }

    @Test
    public void getAliasRegistry() throws Exception
    {
        AliasRegistry aliasRegistry = this.defaultAliasRegistryFactory.getAliasRegistry();
        assertNotNull("alias registry not found", aliasRegistry);
        assertEquals("default alias registry expected", DefaultAliasRegistry.class, aliasRegistry.getClass());
        assertTrue("singleton instance expected", aliasRegistry == this.defaultAliasRegistryFactory.getAliasRegistry());
    }
}
