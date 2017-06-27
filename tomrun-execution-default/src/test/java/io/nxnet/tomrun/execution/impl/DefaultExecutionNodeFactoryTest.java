package io.nxnet.tomrun.execution.impl;

import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import io.nxnet.tomrun.context.OwnableContext;
import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeFactory;
import io.nxnet.tomrun.execution.ExecutionNodeType;
import io.nxnet.tomrun.execution.ExecutionNodeWritter;
import io.nxnet.tomrun.executor.Executor;
import io.nxnet.tomrun.model.Properties;
import io.nxnet.tomrun.model.Property;
import io.nxnet.tomrun.model.TestProject;
import io.nxnet.tomrun.model.TestSuite;

import io.nxnet.tomrun.execution.impl.DefaultExecutionNodeFactory;

public class DefaultExecutionNodeFactoryTest
{
    private static final String DEFAULT_EXECUTION_NODE_FACTORY_NAME = "defaultExecutionNodeFactory";

    private static final String UNEXISTING_EXECUTION_NODE_FACTORY_NAME = "unexistingExecutionNodeFactory";

    private ExecutionNodeWritter writer;

    private String hostName;

    private String pid;

    private String threadName;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private DefaultExecutionNodeFactory factory;

    @Before
    public void setUp() throws Exception
    {
        this.factory = new DefaultExecutionNodeFactory();
        this.writer = new ExecutionNodeWritter(new PrintWriter(System.out));
        this.hostName = InetAddress.getLocalHost().getLocalHost().getHostName(); 
        this.pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        this.threadName = Thread.currentThread().getName();
    }

    @After
    public void tearDown() throws Exception
    {
        this.writer.close();;
    }

    @Test
    public void newInstance_noArgs() throws Exception
    {
        ExecutionNodeFactory factory = ExecutionNodeFactory.newInstance();
        assertNotNull("factory not found", factory);
        assertTrue("default execution node factory not found",
                DefaultExecutionNodeFactory.class.isAssignableFrom(factory.getClass()));
        assertEquals("unexpected name for default execution node factory", DEFAULT_EXECUTION_NODE_FACTORY_NAME,
                factory.getServiceName());
    }

    @Test
    public void newInstance_validFactoryName() throws Exception
    {
        ExecutionNodeFactory factory = ExecutionNodeFactory.newInstance(DEFAULT_EXECUTION_NODE_FACTORY_NAME);
        assertNotNull("factory not found", factory);
        assertTrue("default execution node factory not found",
                DefaultExecutionNodeFactory.class.isAssignableFrom(factory.getClass()));
        assertEquals("unexpected name for default execution node factory", DEFAULT_EXECUTION_NODE_FACTORY_NAME,
                factory.getServiceName());
    }

    @Test
    public void newInstance_invalidFactoryName() throws Exception
    {
        // Set expectations
        this.expectedException.expect(IllegalStateException.class);
        
        // do test
        ExecutionNodeFactory.newInstance(UNEXISTING_EXECUTION_NODE_FACTORY_NAME);
    }

    @Test
    public void getNode_nullArgument() throws Exception
    {
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage("node element is null");
        factory.getNode(null);
    }

    @Test
    public void getNode_emptyArgument() throws Exception
    {
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage("node element id is null");
        factory.getNode(TestProject.builder().build());
    }

    @Test
    public void getNode_projectElementEmpty() throws Exception
    {
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage("node element id is null");
        factory.getNode(TestProject.builder().build());
    }

    @Test
    public void getNode_projectElementWithIdOnly() throws Exception
    {
        ExecutionNode executionNode = factory.getNode(TestProject.builder().withId("1").build());
        assertEquals("unexpected execution node id", "1", executionNode.getId());
        assertNull("unexpected execution node name", executionNode.getName());
        assertNull("unexpected execution node description", executionNode.getDescription());
        assertEquals("unexpected execution node type", ExecutionNodeType.PROJECT, executionNode.getType());
        assertEquals("unexpected execution node level", 0, executionNode.getLevel());
        assertNull("no parent expected", executionNode.getParent());
        assertNull("no sibling expected", executionNode.getLeftSibling());
        assertNull("no sibling expected", executionNode.getRightSibling());
        OwnableContext context = executionNode.getContext();
        assertNotNull("context expected", context);
        assertTrue("unexpected context owner", context.getOwner().equals(executionNode));
        assertTrue("unexpected context owner instance", context.getOwner() == executionNode);
        assertTrue("unexpected context properties", context.getPropertyNames().isEmpty());
        assertTrue("unexpected context attributes", context.getAttributeNames().isEmpty());
        assertEquals("unexpected context run number", 0, context.getRunNumber());
        Iterator<ExecutionNode> nodeIter = executionNode.getIter();
        assertTrue("project node expected", nodeIter.hasNext());
        assertTrue("project node instance expected", nodeIter.next() == executionNode);
        assertFalse("no further nodes expected", nodeIter.hasNext());
    }

    @Test
    public void getNode_projectElement() throws Exception
    {
        ExecutionNode executionNode = factory.getNode(
                TestProject.builder().withId("1").withName("tp1").withDescription("tpdesc1")
                .withProperties(Properties.builder()
                        .addProperty(Property.builder().withName("tp1prop1").withValue("tp1prop1val").build())
                        .addProperty(Property.builder().withName("tp1prop2").withValue("${tp1prop1}").build())
                        .addProperty(Property.builder().withName("tp1prop3").withValue("#{tp1attr1}").build())
                        .build())
                .build());
        assertNotNull("execution node expected", executionNode);
        OwnableContext context = executionNode.getContext();
        assertNotNull("context expected", context);
        context.setAttribute("tp1attr1", new String("tp1attr1val"));

        assertEquals("unexpected execution node id", "1", executionNode.getId());
        assertEquals("unexpected execution node name", "tp1", executionNode.getName());
        assertEquals("unexpected execution node description", "tpdesc1", executionNode.getDescription());
        assertEquals("unexpected execution node type", ExecutionNodeType.PROJECT, executionNode.getType());
        assertEquals("unexpected execution node level", 0, executionNode.getLevel());
        assertNull("no parent expected", executionNode.getParent());
        assertNull("no sibling expected", executionNode.getLeftSibling());
        assertNull("no sibling expected", executionNode.getRightSibling());

        assertTrue("unexpected context owner", context.getOwner().equals(executionNode));
        assertTrue("unexpected context owner instance", context.getOwner() == executionNode);
        assertEquals("unexpected context run number", 0, context.getRunNumber());

        assertEquals("unexpected number of context properties", 3, context.getPropertyNames().size());
        assertEquals("unexpected context property", "tp1prop1val", context.getProperty("tp1prop1"));
        assertEquals("unexpected context property", "tp1prop1val", context.getProperty("tp1prop2"));
        assertEquals("unexpected context property", "tp1attr1val", context.getProperty("tp1prop3"));
        
        assertEquals("unexpected number of context attributes", 1, context.getAttributeNames().size());
        assertEquals("unexpected context attribute", "tp1attr1val", context.getAttribute("tp1attr1"));
        
        Iterator<ExecutionNode> nodeIter = executionNode.getIter();
        assertTrue("project node expected", nodeIter.hasNext());
        assertTrue("project node instance expected", nodeIter.next() == executionNode);
        assertFalse("no other nodes expected", nodeIter.hasNext());
    }

    @Test
    public void getNode_projectAndEmptySuiteElement() throws Exception
    {
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage("node element id is null");
        factory.getNode(
                TestProject.builder().withId("1").withName("tp1").withDescription("tpdesc1")
                .addTestSuite(TestSuite.builder().build())
                .build());
    }

    @Test
    public void getNode_projectAndSuiteElementWithIdOnly() throws Exception
    {
        ExecutionNode executionNode = factory.getNode(
                TestProject.builder().withId("1")
                .addTestSuite(TestSuite.builder().withId("1").build())
                .build());

        ExecutionAssert.assertEquals("1", null, null, ExecutionNodeType.PROJECT, 0, null, null, null,
                executionNode);
        // Assert project context
        ExecutionAssert.assertEquals(executionNode, null, LoggerFactory.getLogger(Executor.DEFAULT_LOGGER_NAME),
                this.hostName, this.pid, this.threadName, 0, executionNode.getContext());

        Iterator<ExecutionNode> nodeIter = executionNode.getIter();
        assertTrue("project node expected", nodeIter.hasNext());
        assertTrue("project node instance expected", nodeIter.next() == executionNode);
        assertTrue("suite node expected", nodeIter.hasNext());
        ExecutionAssert.assertEquals("1", null, null, ExecutionNodeType.SUITE, 1, executionNode, null, null,
                nodeIter.next());
        assertFalse("no other nodes expected", nodeIter.hasNext());
    }

    @Test
    public void getNode_projectAndSuiteElementWithIdNameAndDescription() throws Exception
    {
        ExecutionNode executionNode = factory.getNode(
                TestProject.builder().withId("1").withName("tp1Name").withDescription("tp1Desc")
                .addTestSuite(TestSuite.builder().withId("1").withName("ts1Name").withDescription("ts1Desc").build())
                .build());

        ExecutionAssert.assertEquals("1", "tp1Name", "tp1Desc", ExecutionNodeType.PROJECT, 0, null, null, null,
                executionNode);
        // Assert project context
        ExecutionAssert.assertEquals(executionNode, null, LoggerFactory.getLogger(Executor.DEFAULT_LOGGER_NAME),
                this.hostName, this.pid, this.threadName, 0, executionNode.getContext());

        Iterator<ExecutionNode> nodeIter = executionNode.getIter();
        assertTrue("project node expected", nodeIter.hasNext());
        assertTrue("project node instance expected", nodeIter.next() == executionNode);
        assertTrue("suite node expected", nodeIter.hasNext());
        ExecutionAssert.assertEquals("1", "ts1Name", "ts1Desc", ExecutionNodeType.SUITE, 1, executionNode, null, null,
                nodeIter.next());
        assertFalse("no other nodes expected", nodeIter.hasNext());
    }

    @Test
    public void getNode_projectAndSuiteElementWithProperties() throws Exception
    {
        ExecutionNode project1 = factory.getNode(TestProject.builder()
                .withId("1").withName("tp1Name").withDescription("tp1Desc")
                .addProperty(Property.builder().withName("tp1prop1").withValue("tp1prop1val").build())
                .addProperty(Property.builder().withName("tp1prop2").withValue("${tp1prop1}").build())
                .addProperty(Property.builder().withName("tp1prop3").withValue("#{attr1}").build())
                .addProperty(Property.builder().withName("tp1prop4").withValue("#{attr2}").build())
                .addTestSuite(TestSuite.builder()
                        .withId("1").withName("ts1Name").withDescription("ts1Desc")
                        .addProperty(Property.builder().withName("tp1prop1").withValue("tp1prop1valOverride").build())
                        .addProperty(Property.builder().withName("ts1prop1").withValue("ts1prop1val").build())
                        .addProperty(Property.builder().withName("ts1prop2").withValue("${ts1prop1}").build())
                        .addProperty(Property.builder().withName("ts1prop3").withValue("${tp1prop1}").build())
                        .addProperty(Property.builder().withName("ts1prop4").withValue("${tp1prop2}").build())
                        .addProperty(Property.builder().withName("ts1prop5").withValue("#{attr1}").build())
                        .addProperty(Property.builder().withName("ts1prop6").withValue("#{attr2}").build())
                        .build())
                .build());

        // Assert project
        ExecutionAssert.assertEquals("1", "tp1Name", "tp1Desc", ExecutionNodeType.PROJECT, 0, null, null, null, 
                project1);
        // Assert project context
        ExecutionAssert.assertEquals(project1, null, LoggerFactory.getLogger(Executor.DEFAULT_LOGGER_NAME),
                this.hostName, this.pid, this.threadName, 0, project1.getContext());
        // Assert project properties
        ExecutionAssert.assertEquals(4, 0, new MapBuilder<String, String>().put("tp1prop1", "tp1prop1val")
                .put("tp1prop2", "tp1prop1val").put("tp1prop3", "#{attr1}").put("tp1prop4", "#{attr2}"),
                new MapBuilder<String, Object>(), 4, 0, new MapBuilder<String, String>().put("tp1prop1", "tp1prop1val")
                .put("tp1prop2", "tp1prop1val").put("tp1prop3", "#{attr1}").put("tp1prop4", "#{attr2}"), 
                new MapBuilder<String, Object>(), project1.getContext());
        // Assert project properties after setting attributes
        project1.getContext().setAttribute("attr1", new String("attr1val"));
        project1.getContext().setAttribute("attr2", new String("attr2val"));
        ExecutionAssert.assertEquals(4, 2, new MapBuilder<String, String>().put("tp1prop1", "tp1prop1val")
                .put("tp1prop2", "tp1prop1val").put("tp1prop3", "attr1val").put("tp1prop4", "attr2val"), 
                new MapBuilder<String, Object>().put("attr1", "attr1val").put("attr2", "attr2val"), 4, 2,
                new MapBuilder<String, String>().put("tp1prop1", "tp1prop1val").put("tp1prop2", "tp1prop1val")
                .put("tp1prop3", "attr1val").put("tp1prop4", "attr2val"), new MapBuilder<String, Object>()
                .put("attr1", "attr1val").put("attr2", "attr2val"), project1.getContext());
        // Assert project iterator
        Iterator<ExecutionNode> nodeIter = project1.getIter();
        assertTrue("project node expected", nodeIter.hasNext());
        assertTrue("project node instance expected", nodeIter.next() == project1);

        // Assert suite
        assertTrue("suite node expected", nodeIter.hasNext());
        ExecutionNode suite1 = nodeIter.next();
        ExecutionAssert.assertEquals("1", "ts1Name", "ts1Desc", ExecutionNodeType.SUITE, 1, project1, null, null, 
                suite1);
        // Assert suite context
        ExecutionAssert.assertEquals(suite1, project1.getContext(), LoggerFactory.getLogger(
                Executor.DEFAULT_LOGGER_NAME), this.hostName, this.pid, this.threadName, 0, suite1.getContext());
        // Assert suite properties
        ExecutionAssert.assertEquals(7, 0, new MapBuilder<String, String>().put("tp1prop1", "tp1prop1valOverride")
                .put("ts1prop1", "ts1prop1val").put("ts1prop2", "ts1prop1val").put("ts1prop3", "tp1prop1valOverride")
                .put("ts1prop4", "tp1prop1val").put("ts1prop5", "attr1val").put("ts1prop6", "attr2val"),
                new MapBuilder<String, Object>(), 10, 2, new MapBuilder<String, String>().put("tp1prop2", "tp1prop1val")
                .put("tp1prop3", "attr1val").put("tp1prop4", "attr2val").put("tp1prop1", "tp1prop1valOverride")
                .put("ts1prop1", "ts1prop1val").put("ts1prop2", "ts1prop1val").put("ts1prop3", "tp1prop1valOverride")
                .put("ts1prop4", "tp1prop1val").put("ts1prop5", "attr1val").put("ts1prop6", "attr2val"), 
                new MapBuilder<String, Object>().put("attr1", "attr1val").put("attr2", "attr2val"),
                suite1.getContext());
        // Assert suite properties after setting attributes
        suite1.getContext().setAttribute("attr2", new String("attr2valOverride"));
        ExecutionAssert.assertEquals(7, 1, new MapBuilder<String, String>().put("tp1prop1", "tp1prop1valOverride")
                .put("ts1prop1", "ts1prop1val").put("ts1prop2", "ts1prop1val").put("ts1prop3", "tp1prop1valOverride")
                .put("ts1prop4", "tp1prop1val").put("ts1prop5", "attr1val").put("ts1prop6", "attr2valOverride"),
                new MapBuilder<String, Object>().put("attr2", "attr2valOverride"), 10, 2, 
                new MapBuilder<String, String>().put("tp1prop2", "tp1prop1val").put("tp1prop3", "attr1val")
                .put("tp1prop4", "attr2val").put("tp1prop1", "tp1prop1valOverride").put("ts1prop1", "ts1prop1val")
                .put("ts1prop2", "ts1prop1val").put("ts1prop3", "tp1prop1valOverride").put("ts1prop4", "tp1prop1val")
                .put("ts1prop5", "attr1val").put("ts1prop6", "attr2valOverride"), new MapBuilder<String, Object>()
                .put("attr1", "attr1val").put("attr2", "attr2valOverride"), suite1.getContext());

        assertFalse("no other nodes expected", nodeIter.hasNext());
    }

    @Test
    public void getNode_projectAndSuitesElementsWithSameId() throws Exception
    {
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage("Suite with same id already added to project, can't add suite: "
                + TestSuite.builder().withId("1").build().toString());
        factory.getNode(
                TestProject.builder().withId("1")
                .addTestSuite(TestSuite.builder().withId("1").build())
                .addTestSuite(TestSuite.builder().withId("1").build())
                .build());
    }

    @Test
    public void getNode_projectAndSuitesElementsWithIdOnly() throws Exception
    {
        ExecutionNode project1 = factory.getNode(
                TestProject.builder().withId("1")
                .addTestSuite(TestSuite.builder().withId("1").build())
                .addTestSuite(TestSuite.builder().withId("2").build())
                .addTestSuite(TestSuite.builder().withId("3").build())
                .build());

        // Assert project
        ExecutionAssert.assertEquals("1", null, null, ExecutionNodeType.PROJECT, 0, null, null, null,
                project1);
        ExecutionAssert.assertEquals(true, false, true, true, false, false, false, false, project1);
        // Assert project context
        ExecutionAssert.assertEquals(project1, null, LoggerFactory.getLogger(Executor.DEFAULT_LOGGER_NAME),
                this.hostName, this.pid, this.threadName, 0, project1.getContext());

        // Assert project iterator
        Iterator<ExecutionNode> nodeIter = project1.getIter();
        assertTrue("project node expected", nodeIter.hasNext());
        assertTrue("project node instance expected", nodeIter.next() == project1);
        assertTrue("suite 1 node expected", nodeIter.hasNext());
        ExecutionNode suite1 = nodeIter.next();
        assertTrue("suite 2 node expected", nodeIter.hasNext());
        ExecutionNode suite2 = nodeIter.next();
        assertTrue("suite 3 node expected", nodeIter.hasNext());
        ExecutionNode suite3 = nodeIter.next();
        assertFalse("no other nodes expected", nodeIter.hasNext());

        // Assert suite 1
        ExecutionAssert.assertEquals("1", null, null, ExecutionNodeType.SUITE, 1, project1, null, suite2,
                suite1);
        ExecutionAssert.assertEquals(false, true, false, false, true, false, true, false, suite1);
        // Assert suite 1 context
        ExecutionAssert.assertEquals(suite1, project1.getContext(), LoggerFactory.getLogger(
                Executor.DEFAULT_LOGGER_NAME), this.hostName, this.pid, this.threadName, 0, suite1.getContext());

        // Assert suite 2
        ExecutionAssert.assertEquals("2", null, null, ExecutionNodeType.SUITE, 1, project1, suite1, suite3,
                suite2);
        ExecutionAssert.assertEquals(false, true, false, false, true, false, true, false, suite2);
        // Assert suite 2 context
        ExecutionAssert.assertEquals(suite2, project1.getContext(), LoggerFactory.getLogger(
                Executor.DEFAULT_LOGGER_NAME), this.hostName, this.pid, this.threadName, 0, suite2.getContext());

        // Assert suite 3
        ExecutionAssert.assertEquals("3", null, null, ExecutionNodeType.SUITE, 1, project1, suite2, null,
                suite3);
        ExecutionAssert.assertEquals(false, true, true, false, true, false, true, false, suite3);
        // Assert suite 2 context
        ExecutionAssert.assertEquals(suite3, project1.getContext(), LoggerFactory.getLogger(
                Executor.DEFAULT_LOGGER_NAME), this.hostName, this.pid, this.threadName, 0, suite3.getContext());

    }

}
