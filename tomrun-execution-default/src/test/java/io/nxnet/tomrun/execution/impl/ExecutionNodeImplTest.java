package io.nxnet.tomrun.execution.impl;

import java.io.PrintWriter;
import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeType;
import io.nxnet.tomrun.execution.ExecutionNodeWritter;

public class ExecutionNodeImplTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void build_empty() throws Exception
    {
        this.expectedException.expect(IllegalStateException.class);
        this.expectedException.expectMessage("Id for execution node not defined!");
        ExecutionNodeImpl.builder().build();
    }

    @Test
    public void build_withIdOnly() throws Exception
    {
        this.expectedException.expect(IllegalStateException.class);
        this.expectedException.expectMessage("Type for execution node not defined!");
        ExecutionNodeImpl.builder().withId("foo").build();
    }

    @Test
    public void build_withIdAndType() throws Exception
    {
        ExecutionNodeImpl node = ExecutionNodeImpl.builder().withId("foo").withType(ExecutionNodeType.PROJECT).build();
        assertNotNull("execution node instance expected", node);
        assertEquals("unexpected execution node id", "foo", node.getId());
        assertEquals("unexpected execution node type", ExecutionNodeType.PROJECT, node.getType());
    }

    @Test
    public void iterator_unaryTree() throws Exception
    {
        ExecutionNodeImpl node = ExecutionNodeImpl.builder().withId("1").withType(ExecutionNodeType.PROJECT)
                .addChild(ExecutionNodeImpl.builder().withId("2").withType(ExecutionNodeType.SUITE)
                        .addChild(ExecutionNodeImpl.builder().withId("3").withType(ExecutionNodeType.CASE)
                                .addChild(ExecutionNodeImpl.builder().withId("4").withType(ExecutionNodeType.ACTION)
                                        .build())
                                .build())
                        .build())
                .build();
        assertNotNull("execution node instance expected", node);

        // get root iterator
        Iterator<ExecutionNode> nodeIter = node.iterator();

        // Test project
        assertTrue("project expected", nodeIter.hasNext());
        ExecutionNode project = nodeIter.next();
        assertNotNull("project instance expected", project);
        assertEquals("unexpected project id", "1", project.getId());
        assertEquals("unexpected projecte type", ExecutionNodeType.PROJECT, project.getType());

        // Test suite
        assertTrue("suite expected", nodeIter.hasNext());
        ExecutionNode suite = nodeIter.next();
        assertNotNull("suite instance expected", suite);
        assertEquals("unexpected suite id", "2", suite.getId());
        assertEquals("unexpected suite type", ExecutionNodeType.SUITE, suite.getType());

        // Test case
        assertTrue("case expected", nodeIter.hasNext());
        ExecutionNode caze = nodeIter.next();
        assertNotNull("case instance expected", caze);
        assertEquals("unexpected case id", "3", caze.getId());
        assertEquals("unexpected case type", ExecutionNodeType.CASE, caze.getType());

        // Test action
        assertTrue("action expected", nodeIter.hasNext());
        ExecutionNode action = nodeIter.next();
        assertNotNull("action instance expected", action);
        assertEquals("unexpected action id", "4", action.getId());
        assertEquals("unexpected action type", ExecutionNodeType.ACTION, action.getType());

        assertFalse("no more nodes expected", nodeIter.hasNext());
    }

    @Test
    public void iterator_binaryTree() throws Exception
    {
        ExecutionNodeImpl node = ExecutionNodeImpl.builder().withId("1").withType(ExecutionNodeType.PROJECT)
                .addChild(ExecutionNodeImpl.builder().withId("21").withType(ExecutionNodeType.SUITE)
                        .addChild(ExecutionNodeImpl.builder().withId("31").withType(ExecutionNodeType.CASE)
                                .addChild(ExecutionNodeImpl.builder().withId("41").withType(ExecutionNodeType.ACTION)
                                        .build())
                                .addChild(ExecutionNodeImpl.builder().withId("42").withType(ExecutionNodeType.ACTION)
                                        .build())
                                .build())
                        .addChild(ExecutionNodeImpl.builder().withId("32").withType(ExecutionNodeType.CASE)
                                .addChild(ExecutionNodeImpl.builder().withId("43").withType(ExecutionNodeType.ACTION)
                                        .build())
                                .addChild(ExecutionNodeImpl.builder().withId("44").withType(ExecutionNodeType.ACTION)
                                        .build())
                                .build())
                        .build())
                .addChild(ExecutionNodeImpl.builder().withId("22").withType(ExecutionNodeType.SUITE)
                        .addChild(ExecutionNodeImpl.builder().withId("33").withType(ExecutionNodeType.CASE)
                                .addChild(ExecutionNodeImpl.builder().withId("45").withType(ExecutionNodeType.ACTION)
                                        .build())
                                .addChild(ExecutionNodeImpl.builder().withId("46").withType(ExecutionNodeType.ACTION)
                                        .build())
                                .build())
                        .addChild(ExecutionNodeImpl.builder().withId("34").withType(ExecutionNodeType.CASE)
                                .addChild(ExecutionNodeImpl.builder().withId("47").withType(ExecutionNodeType.ACTION)
                                        .build())
                                .addChild(ExecutionNodeImpl.builder().withId("48").withType(ExecutionNodeType.ACTION)
                                        .build())
                                .build())
                        .build())
                .build();
        assertNotNull("execution node instance expected", node);

        // get root iterator
        Iterator<ExecutionNode> nodeIter = node.iterator();

        // Test project
        assertTrue("project expected", nodeIter.hasNext());
        ExecutionNode project = nodeIter.next();
        assertNotNull("project instance expected", project);
        assertEquals("unexpected project id", "1", project.getId());
        assertEquals("unexpected projecte type", ExecutionNodeType.PROJECT, project.getType());

        // Test suite
        assertTrue("suite expected", nodeIter.hasNext());
        ExecutionNode suite = nodeIter.next();
        assertNotNull("suite instance expected", suite);
        assertEquals("unexpected suite id", "21", suite.getId());
        assertEquals("unexpected suite type", ExecutionNodeType.SUITE, suite.getType());

        // Test case
        assertTrue("case expected", nodeIter.hasNext());
        ExecutionNode caze = nodeIter.next();
        assertNotNull("case instance expected", caze);
        assertEquals("unexpected case id", "31", caze.getId());
        assertEquals("unexpected case type", ExecutionNodeType.CASE, caze.getType());

        // Test action
        assertTrue("action expected", nodeIter.hasNext());
        ExecutionNode action = nodeIter.next();
        assertNotNull("action instance expected", action);
        assertEquals("unexpected action id", "41", action.getId());
        assertEquals("unexpected action type", ExecutionNodeType.ACTION, action.getType());

        // Test action
        assertTrue("action expected", nodeIter.hasNext());
        action = nodeIter.next();
        assertNotNull("action instance expected", action);
        assertEquals("unexpected action id", "42", action.getId());
        assertEquals("unexpected action type", ExecutionNodeType.ACTION, action.getType());

        // Test case
        assertTrue("case expected", nodeIter.hasNext());
        caze = nodeIter.next();
        assertNotNull("case instance expected", caze);
        assertEquals("unexpected case id", "32", caze.getId());
        assertEquals("unexpected case type", ExecutionNodeType.CASE, caze.getType());

        // Test action
        assertTrue("action expected", nodeIter.hasNext());
        action = nodeIter.next();
        assertNotNull("action instance expected", action);
        assertEquals("unexpected action id", "43", action.getId());
        assertEquals("unexpected action type", ExecutionNodeType.ACTION, action.getType());

        // Test action
        assertTrue("action expected", nodeIter.hasNext());
        action = nodeIter.next();
        assertNotNull("action instance expected", action);
        assertEquals("unexpected action id", "44", action.getId());
        assertEquals("unexpected action type", ExecutionNodeType.ACTION, action.getType());

        // Test suite
        assertTrue("suite expected", nodeIter.hasNext());
        suite = nodeIter.next();
        assertNotNull("suite instance expected", suite);
        assertEquals("unexpected suite id", "22", suite.getId());
        assertEquals("unexpected suite type", ExecutionNodeType.SUITE, suite.getType());

        // Test case
        assertTrue("case expected", nodeIter.hasNext());
        caze = nodeIter.next();
        assertNotNull("case instance expected", caze);
        assertEquals("unexpected case id", "33", caze.getId());
        assertEquals("unexpected case type", ExecutionNodeType.CASE, caze.getType());

        // Test action
        assertTrue("action expected", nodeIter.hasNext());
        action = nodeIter.next();
        assertNotNull("action instance expected", action);
        assertEquals("unexpected action id", "45", action.getId());
        assertEquals("unexpected action type", ExecutionNodeType.ACTION, action.getType());

        // Test action
        assertTrue("action expected", nodeIter.hasNext());
        action = nodeIter.next();
        assertNotNull("action instance expected", action);
        assertEquals("unexpected action id", "46", action.getId());
        assertEquals("unexpected action type", ExecutionNodeType.ACTION, action.getType());

        // Test case
        assertTrue("case expected", nodeIter.hasNext());
        caze = nodeIter.next();
        assertNotNull("case instance expected", caze);
        assertEquals("unexpected case id", "34", caze.getId());
        assertEquals("unexpected case type", ExecutionNodeType.CASE, caze.getType());

        // Test action
        assertTrue("action expected", nodeIter.hasNext());
        action = nodeIter.next();
        assertNotNull("action instance expected", action);
        assertEquals("unexpected action id", "47", action.getId());
        assertEquals("unexpected action type", ExecutionNodeType.ACTION, action.getType());

        // Test action
        assertTrue("action expected", nodeIter.hasNext());
        action = nodeIter.next();
        assertNotNull("action instance expected", action);
        assertEquals("unexpected action id", "48", action.getId());
        assertEquals("unexpected action type", ExecutionNodeType.ACTION, action.getType());

        assertFalse("no more nodes expected", nodeIter.hasNext());
        try (ExecutionNodeWritter nodeWritter = new ExecutionNodeWritter(new PrintWriter(System.out)))
        {
            nodeWritter.write(project);
        }
    }

}
