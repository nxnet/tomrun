package io.nxnet.tomrun.service.loader.def;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import net.jodah.concurrentunit.Waiter;

import io.nxnet.tomrun.service.loader.NamedServiceLoader;

import io.nxnet.tomrun.service.loader.def.DefaultNamedServiceLoader;

public class DefaultNamedServiceLoaderTest
{
    private DefaultNamedServiceLoader defaultNamedServiceLoader;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception
    {
        this.defaultNamedServiceLoader = (DefaultNamedServiceLoader)NamedServiceLoader.getInstance();
        assertNotNull("Service loader implementation is not found", this.defaultNamedServiceLoader);
        assertEquals("Unexpected service loader implementation", DefaultNamedServiceLoader.class, 
                this.defaultNamedServiceLoader.getClass());
        assertTrue("Named service loader should be singleton", 
                this.defaultNamedServiceLoader == NamedServiceLoader.getInstance());
    }

    @Test
    public void loadService() throws Exception
    {
        TestService testService1 = this.defaultNamedServiceLoader.loadService("testService", TestService.class, 
                Thread.currentThread().getContextClassLoader());
        assertNotNull("test service not found", testService1);
        assertEquals("Unexpected service implementation", TestServiceImpl.class, testService1.getClass());

        TestService testService2 = this.defaultNamedServiceLoader.loadService("testService", TestService.class, 
                Thread.currentThread().getContextClassLoader());
        assertNotNull("test service not found", testService2);
        assertEquals("Unexpected service implementation", TestServiceImpl.class, testService2.getClass());

        assertTrue("same service instance expected", testService1 == testService2);
    }

    @Test
    public void loadService_usingUnexistingName() throws Exception
    {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("No matching " + TestService.class.getName() 
                + " service named unexistingTestService found!");
        this.defaultNamedServiceLoader.loadService("unexistingTestService", TestService.class, 
                Thread.currentThread().getContextClassLoader());
    }

    @Test
    public void loadService_usingUnexistingNameAndClass() throws Exception
    {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("No matching " + UnimplementableTestService.class.getName() 
                + " service named unexistingTestService found!");
        this.defaultNamedServiceLoader.loadService("unexistingTestService", UnimplementableTestService.class, 
                Thread.currentThread().getContextClassLoader());
    }

    @Test
    public void loadService_concurrently() throws Exception
    {
        final TestService mainThreadtestService = NamedServiceLoader.getInstance().loadService("testService", 
                TestService.class, Thread.currentThread().getContextClassLoader());
        
        final Waiter waiter = new Waiter();
        
        final AtomicInteger threadCounter = new AtomicInteger(0);
        final Set<TestService> services = new HashSet<TestService>();
        for (int i = 0 ; i < 100 ; i++)
        {
            Thread thread = new Thread(new Runnable() {
                
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep((int)(Math.random() * 50));
                    }
                    catch (InterruptedException e)
                    {
                        throw new IllegalStateException(e);
                    }
                    
                    TestService testThreadTestService = NamedServiceLoader.getInstance().loadService(
                            "testService", TestService.class, Thread.currentThread().getContextClassLoader());
                    services.add(testThreadTestService);
                    
                    waiter.assertTrue(mainThreadtestService == testThreadTestService);
                    threadCounter.incrementAndGet();
                    waiter.resume();
                }
            });

            thread.start();
        }

        waiter.await(5000, 100);

        assertEquals("Unexpected number of threads run", 100, threadCounter.get());
        assertEquals("Unexpected number of service instances", 1, services.size());
    }
}
