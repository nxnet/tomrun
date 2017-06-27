package io.nxnet.tomrun.execution.impl;

import java.util.Map;

import org.junit.Assert;
import org.slf4j.Logger;

import io.nxnet.tomrun.context.Context;
import io.nxnet.tomrun.context.Contextable;
import io.nxnet.tomrun.context.OwnableContext;
import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeType;

public class ExecutionAssert
{
    public static void assertEquals(String expectedId, String expectedName, String expectedDescription,
            ExecutionNodeType expectedType, int expectedLevel, ExecutionNode expectedParent, 
            ExecutionNode expectedLeftSibling, ExecutionNode expectedRightSibling, ExecutionNode actualExecutionNode)
    {
        Assert.assertNotNull("execution node instance expected", actualExecutionNode);
        Assert.assertEquals("unexpected execution node id", expectedId, actualExecutionNode.getId());
        Assert.assertEquals("unexpected execution node name", expectedName, actualExecutionNode.getName());
        Assert.assertEquals("unexpected execution node description", expectedDescription,
                actualExecutionNode.getDescription());
        Assert.assertEquals("unexpected execution node type", expectedType, actualExecutionNode.getType());
        Assert.assertEquals("unexpected execution node level", expectedLevel, actualExecutionNode.getLevel());
        Assert.assertEquals("unexpected execution node parent", expectedParent, actualExecutionNode.getParent());
        Assert.assertEquals("unexpected execution node left sibling", expectedLeftSibling,
                actualExecutionNode.getLeftSibling());
        Assert.assertEquals("unexpected execution node right sibling", expectedRightSibling,
                actualExecutionNode.getRightSibling());
    }

    public static void assertEquals(boolean isRoot, boolean isLeaf, boolean isLast, boolean isParent, boolean isChild,
            boolean isLeftChild, boolean isMiddleChild, boolean isRightChild, ExecutionNode actualExecutionNode)
    {
        Assert.assertEquals("execution node root", isRoot, actualExecutionNode.isRoot());
        Assert.assertEquals("execution node leaf", isLeaf, actualExecutionNode.isLeaf());
        Assert.assertEquals("execution node last", isLast, actualExecutionNode.isLast());
        Assert.assertEquals("execution node parent", isParent, actualExecutionNode.isParent());
        Assert.assertEquals("execution node child", isChild, actualExecutionNode.isChild());
        Assert.assertEquals("execution node left child", isLeftChild, actualExecutionNode.isLeftChild());
        Assert.assertEquals("execution node middle child", isMiddleChild, actualExecutionNode.isMiddleChild());
        Assert.assertEquals("execution node right child", isRightChild, actualExecutionNode.isRightChild());
    }

    public static void assertEquals(Contextable expectedOwner, Context expectedParent, Logger expectedLogger,
            String expectedHostName, String expectedPid, String expectedThreadName, int expectedRunNumber,
            OwnableContext actualContext)
    {
        Assert.assertNotNull("context instance expected", actualContext);
        Assert.assertEquals("unexpected context owner", expectedOwner, actualContext.getOwner());
        Assert.assertTrue("unexpected context owner instance", expectedOwner == actualContext.getOwner());
        Assert.assertEquals("unexpected context owner", expectedParent, actualContext.getParentContext());
        Assert.assertEquals("unexpected context logger", expectedLogger, actualContext.getLogger());
        Assert.assertEquals("unexpected context host name", expectedHostName, actualContext.getHostName());
        Assert.assertEquals("unexpected context pid", expectedPid, actualContext.getPid());
        Assert.assertEquals("unexpected context thread name", expectedThreadName, actualContext.getThreadName());
        Assert.assertEquals("unexpected context run number", expectedRunNumber, actualContext.getRunNumber());
    }

    public static void assertEquals(int expectedPropertiesNumberGot, int expectedAttributesNumberGot,
            MapBuilder<String, String> expectedPropertiesGot, MapBuilder<String, Object> expectedAttributesGot,
            int expectedPropertiesNumberFound, int expectedAttributesNumberFound,
            MapBuilder<String, String> expectedPropertiesFound, MapBuilder<String, Object> expectedAttributesFound,
            OwnableContext actualContext)
    {
        // Assert get properties
        Assert.assertEquals("unexpected number of context properties using get", expectedPropertiesNumberGot,
                actualContext.getPropertyNames().size());
        for (Map.Entry<String, String> expectedPropertyEntry : expectedPropertiesGot.build().entrySet())
        {
            Assert.assertEquals("unexpected context property using get", expectedPropertyEntry.getValue(),
                    actualContext.getProperty(expectedPropertyEntry.getKey()));
        }

        // Assert get attributes
        Assert.assertEquals("unexpected number of context attributes using get", expectedAttributesNumberGot,
                actualContext.getAttributeNames().size());
        for (Map.Entry<String, Object> expectedAttributeEntry : expectedAttributesGot.build().entrySet())
        {
            Assert.assertEquals("unexpected context attribute using get", expectedAttributeEntry.getValue(),
                    actualContext.getAttribute(expectedAttributeEntry.getKey()));
        }

        // Assert find properties
        Assert.assertEquals("unexpected number of context properties using find", expectedPropertiesNumberFound,
                actualContext.findPropertyNames().size());
        for (Map.Entry<String, String> expectedPropertyEntry : expectedPropertiesFound.build().entrySet())
        {
            Assert.assertEquals("unexpected context property using find", expectedPropertyEntry.getValue(),
                    actualContext.findProperty(expectedPropertyEntry.getKey()));
        }

        // Assert find attributes
        Assert.assertEquals("unexpected number of context attributes using find", expectedAttributesNumberFound,
                actualContext.findAttributeNames().size());
        for (Map.Entry<String, Object> expectedAttributeEntry : expectedAttributesFound.build().entrySet())
        {
            Assert.assertEquals("unexpected context attribute using find", expectedAttributeEntry.getValue(),
                    actualContext.findAttribute(expectedAttributeEntry.getKey()));
        }
    }
}
