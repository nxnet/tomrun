package io.nxnet.tomrun.agent;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.nxnet.tomrun.agent.TomExecutionReporter;
import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.impl.DefaultExecutionNodeFactory;
import io.nxnet.tomrun.model.TestProject;
import io.nxnet.tomrun.parser.StaxTestProjectReader;
import io.nxnet.tomrun.parser.TestProjectIterator;

public class TomReporterTest
{
    private DefaultExecutionNodeFactory factory;

    private StaxTestProjectReader reader;

    @Before
    public void setUp() throws Exception
    {
        factory = new DefaultExecutionNodeFactory();
        reader = new StaxTestProjectReader();
    }

    @After
    public void tearDown() throws Exception
    {
        reader.close();
    }

    @Test @Ignore
    public void update() throws Exception
    {
        //System.out.println(StringUtils.toString(getClass().getClassLoader().getResourceAsStream("tom.xml")));
        TestProject testProject = null;
        TestProjectIterator testProjectIter = reader.read(getClass().getClassLoader().getResourceAsStream("tom.xml"));
        if (testProjectIter.hasNext())
        {
            testProject = testProjectIter.next();
        }
        System.out.println(testProject);
        
        ExecutionNode executionNode = factory.getNode(testProject, null);
        System.out.println(executionNode);
        
        executionNode.addObserver(new TomExecutionReporter());
        executionNode.doExec();
    }


}
