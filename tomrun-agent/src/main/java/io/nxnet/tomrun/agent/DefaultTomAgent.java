package io.nxnet.tomrun.agent;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observer;

import io.nxnet.tomrun.execution.ExecutionFilter;
import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeException;
import io.nxnet.tomrun.execution.ExecutionNodeFactory;
import io.nxnet.tomrun.execution.impl.DefaultExecutionFilter;
import io.nxnet.tomrun.model.TestProject;
import io.nxnet.tomrun.parser.StaxTestProjectReader;
import io.nxnet.tomrun.parser.TestProjectIterator;

public class DefaultTomAgent implements TomAgent
{
    private StaxTestProjectReader reader;
    
    private Collection<Observer> observers;

    public DefaultTomAgent()
    {
        this.reader = new StaxTestProjectReader();
    }

    @Override
    public TomExecutionResult execute(TomExecutionRequest request) throws TomExecutionException
    {
        //
        // Open tom.xml file
        //
        URL tomUrl = null;
        InputStream tomStream = null;
        try
        {
        	// Convert string to URL
            tomUrl = URLFactory.newURL(request.getTom());
            
            // Open URL's input stream
            tomStream = tomUrl.openStream();
        }
        catch (MalformedURLException e)
        {
            throw new TomExecutionException("Error parsing tom file url.", e);
        }
        catch (IOException e)
        {
            throw new TomExecutionException("Error opening tom file input stream.", e);
        }
        
        // 
        // Create test project reader
        //
        TestProjectIterator testIter = this.reader.read(tomStream);
        TestProject testProject = null;
        if (testIter.hasNext())
        {
            testProject = testIter.next();
        }
        
        // 
        // Create test filter
        //
        ExecutionFilter testFilter = new DefaultExecutionFilter.Builder()
		        .suiteIdRegex(request.getSuite())
		        .caseIdRegex(request.getCaze())
		        .testIdRegex(request.getTest())
		        .actionIdRegex(request.getAction())
		        .wrapperIdRegex(request.getWrapper())
		        .build();
        
        // 
        // Create project execution node
        //
        ExecutionNode testProjectNode = null;
        try
        {
            // Use test project factory
            String testProjectExecutionNodeFactoryName = testProject.getFactory() != null ?
                    testProject.getFactory() : ExecutionNodeFactory.DEFAULT_FACTORY_NAME;
            testProjectNode = ExecutionNodeFactory.newInstance(testProjectExecutionNodeFactoryName)
                    .getNode(testProject, testFilter);
        }
        catch (ExecutionNodeException e)
        {
            throw new TomExecutionException("Error creating test project node.", e);
        }
        
        // 
        // Register observers to project execution node
        //
        if (testProjectNode != null)
        {
	        for (Observer observer : observers)
	        {
	            testProjectNode.addObserver(observer);
	        }
        }
        
        // 
        // Create execution result instance and register it as observer
        //
        TomExecutionResult executionResult = new DefaultTomExecutionResult();
        if (testProjectNode != null)
        {
        	testProjectNode.addObserver((DefaultTomExecutionResult)executionResult);
        }
        
        // 
        // Execute test execution node
        //
        if (testProjectNode != null)
        {
        	testProjectNode.doExec();
        }
        
        // 
        // return execution result instance
        //
        return executionResult;
    }

    public StaxTestProjectReader getReader()
    {
        return reader;
    }

    public void setReader(StaxTestProjectReader reader)
    {
        this.reader = reader;
    }

    public Collection<Observer> getObservers()
    {
        return observers;
    }

    public void setObservers(Collection<Observer> observers)
    {
        this.observers = observers;
    }

    @Override
    public boolean addObserver(Observer observer)
    {
        if (observers == null)
        {
            observers = new HashSet<Observer>();
        }
        return observers.add(observer);
    }

	@Override
	public boolean removeObserver(Observer observer) 
	{
		if (observers != null)
		{
			return observers.remove(observer);
		}
		return false;
	}

}
