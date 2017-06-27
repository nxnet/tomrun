package io.nxnet.tomrun.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nxnet.tomrun.model.AbstractAction;
import io.nxnet.tomrun.model.Authorization;
import io.nxnet.tomrun.model.DynamicInclude;
import io.nxnet.tomrun.model.HttpRequest;
import io.nxnet.tomrun.model.HttpResponse;
import io.nxnet.tomrun.model.Include;
import io.nxnet.tomrun.model.Insert;
import io.nxnet.tomrun.model.MessageBody;
import io.nxnet.tomrun.model.Properties;
import io.nxnet.tomrun.model.Property;
import io.nxnet.tomrun.model.ResolvableAssertionAwareElement;
import io.nxnet.tomrun.model.ResolvableAttribute;
import io.nxnet.tomrun.model.ResolvableElement;
import io.nxnet.tomrun.model.Script;
import io.nxnet.tomrun.model.StatusLine;
import io.nxnet.tomrun.model.Test;
import io.nxnet.tomrun.model.TestCase;
import io.nxnet.tomrun.model.TestProject;
import io.nxnet.tomrun.model.TestSuite;
import io.nxnet.tomrun.model.Wrapper;

public abstract class AbstractStaxReader
{
	private static final Logger log = LoggerFactory.getLogger(AbstractStaxReader.class);
	
    protected XMLStreamReader reader; 
    
    protected int depth;
    
    protected XMLStreamReader createReader(InputStream in) throws TomConfigurationError
    {
        XMLStreamReader reader = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try
        {
            reader = factory.createXMLStreamReader(new BufferedInputStream(in));
        }
        catch (XMLStreamException e)
        {
            throw new TomConfigurationError(e);
        }
        return reader;
    }

    protected void closeReader(XMLStreamReader reader) throws IOException
    {
        if (reader != null) 
        {
            try
            {
                reader.close();
            }
            catch (XMLStreamException e)
            {
                throw new IOException(e);
            }
        }
    }

    protected TestProject processTestProject(XMLStreamReader reader) throws XMLStreamException, IOException 
    {
    	if (log.isTraceEnabled())
    	{
    		log.trace(">>> process test project...");
    	}
    	
        TestProject testProject = null;
        if (reader.getEventType() == reader.START_ELEMENT 
                || reader.getEventType() == reader.END_ELEMENT 
                || reader.getEventType() == reader.ENTITY_REFERENCE)
        {
            if (reader.getLocalName() == "test-project")
            {
                testProject = TestProject.builder()
                        .withId(reader.getAttributeValue(null, "id"))
                        .withName(reader.getAttributeValue(null, "name"))
                        .withDescription(reader.getAttributeValue(null, "description"))
                        .withFactory(reader.getAttributeValue(null, "factory"))
                        .build();
                
                boolean processingDone = false;
                while(reader.hasNext()) {
                    
                    int event = reader.next();
                    switch(event)
                    {
                        case XMLEvent.START_ELEMENT:
                            
                            if (reader.getLocalName().equals("properties"))
                            {
                                Properties properties = processProperties(reader);
                                testProject.setProperties(properties);
                            } 
                            else if (reader.getLocalName().equals("test-project-config"))
                            {
                                Map<String, String> config = processConfig(reader);
                                testProject.setConfiguration(config);
                            } 
                            else if (reader.getLocalName().equals("before-first"))
                            {
                                Wrapper beforeFirst = processWrapper(reader, false);
                                beforeFirst.setOwner(testProject);
                                testProject.setBeforeFirst(beforeFirst);
                                if (beforeFirst.getId() == null)
                                {
                                    beforeFirst.setId("BFS");
                                }
                            } 
                            else if (reader.getLocalName().equals("after-last"))
                            {
                                Wrapper afterLast = processWrapper(reader, false);
                                afterLast.setOwner(testProject);
                                testProject.setAfterLast(afterLast);
                                if (afterLast.getId() == null)
                                {
                                    afterLast.setId("ALS");
                                }
                            } 
                            else if (reader.getLocalName().equals("before-each"))
                            {
                                Wrapper beforeEach = processWrapper(reader, false);
                                beforeEach.setOwner(testProject);
                                testProject.setBeforeEach(beforeEach);
                                if (beforeEach.getId() == null)
                                {
                                    beforeEach.setId("BES");
                                }
                            } 
                            else if (reader.getLocalName().equals("after-each"))
                            {
                                Wrapper afterEach = processWrapper(reader, false);
                                afterEach.setOwner(testProject);
                                testProject.setAfterEach(afterEach);
                                if (afterEach.getId() == null)
                                {
                                    afterEach.setId("AES");
                                }
                            } 
                            else if (reader.getLocalName().equals("test-suite"))
                            {
                                TestSuite testSuite = processTestSuite(reader, false);
                                testSuite.setTestProject(testProject);
                                if (!testProject.addTestSuite(testSuite))
                                {
                                    throw new TomConfigurationError(MessageFormat.format(
                                            "Multiple test suites with id ''{0}'' found. Test suite ids should be unique.", testSuite.getId()));
                                }
                            }
                            
                            break;
                            
                        case XMLEvent.END_ELEMENT:
                            
                            if (reader.getLocalName().equals("test-project")) 
                            {
                                processingDone = true;
                            }
                            
                            break;
                    }
                    
                    if (processingDone) {
                        break;
                    }
                }
            }
        }
        
        return testProject;
    }
    
    protected TestSuite processTestSuite(XMLStreamReader reader, boolean inclusion) throws XMLStreamException, IOException
    {
    	if (log.isTraceEnabled())
    	{
    		if (!inclusion) 
    		{
				depth++;
				String id = null;
				if (reader.getLocalName() == "test-suite")
				{
					id = reader.getAttributeValue(null, "id");
				}
				log.trace(MessageFormat.format(">>>{0} + process test suite... {1}", StringUtils.repeat(" |", depth), id));
			}
    	}
        TestSuite testSuite = null;
        if (reader.getLocalName() == "test-suite")
        {
            testSuite = TestSuite.builder()
                    .withId(reader.getAttributeValue(null, "id"))
                    .withName(reader.getAttributeValue(null, "name"))
                    .withDescription(reader.getAttributeValue(null, "description"))
                    .withTargetVersions(reader.getAttributeValue(null, "target-versions"))
                    .withTargetRequirements(reader.getAttributeValue(null, "target-requirements"))
                    .build();
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("properties"))
                        {
                            Properties properties = processProperties(reader);
                            testSuite.setProperties(properties);
                        } 
                        else if (reader.getLocalName().equals("test-case"))
                        {
                            TestCase testCase = processTestCase(reader, false);
                            testCase.setTestSuite(testSuite);
                            if (!testSuite.addTestCase(testCase))
                            {
                                throw new TomConfigurationError(MessageFormat.format(
                                        "Multiple test cases with id ''{0}'' found in test suite ''{1}''. Test cases under one test suite should be unique.", testCase.getId(), testSuite.getId()));
                            }
                        }
                        else if (reader.getLocalName().equals("before-first"))
                        {
                            Wrapper beforeFirst = processWrapper(reader, false);
                            beforeFirst.setOwner(testSuite);
                            testSuite.setBeforeFirst(beforeFirst);
                            if (beforeFirst.getId() == null)
                            {
                                beforeFirst.setId("BFC");
                            }
                        } 
                        else if (reader.getLocalName().equals("after-last"))
                        {
                            Wrapper afterLast = processWrapper(reader, false);
                            afterLast.setOwner(testSuite);
                            testSuite.setAfterLast(afterLast);
                            if (afterLast.getId() == null)
                            {
                                afterLast.setId("ALC");
                            }
                        } 
                        else if (reader.getLocalName().equals("before-each"))
                        {
                            Wrapper beforeEach = processWrapper(reader, false);
                            beforeEach.setOwner(testSuite);
                            testSuite.setBeforeEach(beforeEach);
                            if (beforeEach.getId() == null)
                            {
                                beforeEach.setId("BEC");
                            }
                        } 
                        else if (reader.getLocalName().equals("after-each"))
                        {
                            Wrapper afterEach = processWrapper(reader, false);
                            afterEach.setOwner(testSuite);
                            testSuite.setAfterEach(afterEach);
                            if (afterEach.getId() == null)
                            {
                                afterEach.setId("AEC");
                            }
                        } 
                        else if (reader.getLocalName().equals("include"))
                        {
                            TestSuite includedTestSuite = (TestSuite)processInclude(reader, "test-suite");
                            if (testSuite.getId() != null)
                            {
                            	includedTestSuite.setId(testSuite.getId());
                            }
                            if (testSuite.getName() != null)
                            {
                            	includedTestSuite.setName(testSuite.getName());
                            }
                            if (testSuite.getDescription() != null)
                            {
                            	includedTestSuite.setDescription(testSuite.getDescription());
                            }
                            if (testSuite.getTargetVersions() != null)
                            {
                            	includedTestSuite.setTargetVersions(testSuite.getTargetVersions());
                            }
                            if (testSuite.getTargetRequirements() != null)
                            {
                            	includedTestSuite.setTargetRequirements(testSuite.getTargetRequirements());
                            }
                            if (testSuite.getTestProject() != null)
                            {
                            	includedTestSuite.setTestProject(testSuite.getTestProject());
                            }
                            testSuite = includedTestSuite;
                            processingDone = true;
                        }
                        else if (reader.getLocalName().equals("dynamic-include"))
                        {
                            DynamicInclude dynamicInclude = processDynamicInclude(reader);
                            testSuite.setDynamicInclude(dynamicInclude);
                            dynamicInclude.setOwner(testSuite);
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("test-suite")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        if (log.isTraceEnabled())
    	{
        	if (!inclusion) 
    		{
        		depth--;
    		}
    	}
        return testSuite;
    }
    
    protected TestCase processTestCase(XMLStreamReader reader, boolean inclusion) throws XMLStreamException, IOException 
    {
    	if (log.isTraceEnabled())
    	{
    		if (!inclusion) 
    		{
	    		depth++;
	    		String id = null;
				if (reader.getLocalName() == "test-case")
				{
					id = reader.getAttributeValue(null, "id");
				}
	    		log.trace(MessageFormat.format(">>>{0} + process test case... {1}", StringUtils.repeat(" |", depth), id));
    		}
		}
        TestCase testCase = null;
        if (reader.getLocalName() == "test-case")
        {
            testCase = TestCase.builder()
                    .withId(reader.getAttributeValue(null, "id"))
                    .withName(reader.getAttributeValue(null, "name"))
                    .withDescription(reader.getAttributeValue(null, "description"))
                    .withTargetVersions(reader.getAttributeValue(null, "target-versions"))
                    .withTargetRequirements(reader.getAttributeValue(null, "target-requirements"))
                    .build();
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("properties"))
                        {
                        	Properties properties = processProperties(reader);
                            testCase.setProperties(properties);
                        } 
                        else if (reader.getLocalName().equals("test"))
                        {
                            Test test = processTest(reader, false);
                            test.setTestCase(testCase);
                            if (!testCase.addTest(test))
                            {
                                throw new TomConfigurationError(MessageFormat.format(
                                        "Multiple tests with id ''{0}'' found in test case ''{1}''. Tests under one test case should be unique.", test.getId(), testCase.getId()));
                            }
                        } 
                        else if (reader.getLocalName().equals("before-first"))
                        {
                            Wrapper beforeFirst = processWrapper(reader, false);
                            beforeFirst.setOwner(testCase);
                            testCase.setBeforeFirst(beforeFirst);
                            if (beforeFirst.getId() == null)
                            {
                                beforeFirst.setId("BFT");
                            }
                        } 
                        else if (reader.getLocalName().equals("after-last"))
                        {
                            Wrapper afterLast = processWrapper(reader, false);
                            afterLast.setOwner(testCase);
                            testCase.setAfterLast(afterLast);
                            if (afterLast.getId() == null)
                            {
                                afterLast.setId("ALT");
                            }
                        } 
                        else if (reader.getLocalName().equals("before-each"))
                        {
                            Wrapper beforeEach = processWrapper(reader, false);
                            beforeEach.setOwner(testCase);
                            testCase.setBeforeEach(beforeEach);
                            if (beforeEach.getId() == null)
                            {
                                beforeEach.setId("BET");
                            }
                        } 
                        else if (reader.getLocalName().equals("after-each"))
                        {
                            Wrapper afterEach = processWrapper(reader, false);
                            afterEach.setOwner(testCase);
                            testCase.setAfterEach(afterEach);
                            if (afterEach.getId() == null)
                            {
                                afterEach.setId("AET");
                            }
                        } 
                        else if (reader.getLocalName().equals("include"))
                        {
                            TestCase includedTestCase = (TestCase)processInclude(reader, "test-case");
                            if (testCase.getId() != null)
                            {
                            	includedTestCase.setId(testCase.getId());
                            }
                            if (testCase.getName() != null)
                            {
                            	includedTestCase.setName(testCase.getName());
                            }
                            if (testCase.getDescription() != null)
                            {
                            	includedTestCase.setDescription(testCase.getDescription());
                            }
                            if (testCase.getTargetVersions() != null)
                            {
                            	includedTestCase.setTargetVersions(testCase.getTargetVersions());
                            }
                            if (testCase.getTargetRequirements() != null)
                            {
                            	includedTestCase.setTargetRequirements(testCase.getTargetRequirements());
                            }
                            if (testCase.getTestSuite() != null)
                            {
                            	includedTestCase.setTestSuite(testCase.getTestSuite());
                            }
                            testCase = includedTestCase;
                            processingDone = true;
                        }
                        else if (reader.getLocalName().equals("dynamic-include"))
                        {
                            DynamicInclude dynamicInclude = processDynamicInclude(reader);
                            testCase.setDynamicInclude(dynamicInclude);
                            dynamicInclude.setOwner(testCase);
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("test-case")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        if (log.isTraceEnabled())
    	{
        	if (!inclusion) 
    		{
        		depth--;
    		}
    	}
        return testCase;
    }

    protected Test processTest(XMLStreamReader reader, boolean inclusion) throws XMLStreamException, IOException 
    {
    	if (log.isTraceEnabled())
    	{
    		if (!inclusion) 
    		{
	    		depth++;
	    		String id = null;
				if (reader.getLocalName() == "test")
				{
					id = reader.getAttributeValue(null, "id");
				}
	    		log.trace(MessageFormat.format(">>>{0} + process test... {1}", StringUtils.repeat(" |", depth), id));
    		}
		}
        Test test = null;
        if (reader.getLocalName() == "test")
        {
            test = Test.builder()
                    .withId(reader.getAttributeValue(null, "id"))
                    .withName(reader.getAttributeValue(null, "name"))
                    .withDescription(reader.getAttributeValue(null, "description"))
                    .withTargetVersions(reader.getAttributeValue(null, "target-versions"))
                    .withTargetRequirements(reader.getAttributeValue(null, "target-requirements"))
                    .build();
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("properties"))
                        {
                        	Properties properties = processProperties(reader);
                            test.setProperties(properties);
                        } 
                        else if (reader.getLocalName().equals("script") 
                        		|| reader.getLocalName().equals("http-request"))
                        {
                            AbstractAction action = processAction(reader, false);
                            action.setOwner(test);
                            if (!test.addAction(action))
                            {
                                throw new TomConfigurationError(MessageFormat.format(
                                        "Multiple actions with id ''{0}'' found in test ''{1}''. Actions under one test should be unique.", action.getId(), test.getId()));
                            }
                        }
                        else if (reader.getLocalName().equals("before-first"))
                        {
                            Wrapper beforeFirst = processWrapper(reader, false);
                            beforeFirst.setOwner(test);
                            test.setBeforeFirst(beforeFirst);
                            if (beforeFirst.getId() == null)
                            {
                                beforeFirst.setId("BFA");
                            }
                        } 
                        else if (reader.getLocalName().equals("after-last"))
                        {
                            Wrapper afterLast = processWrapper(reader, false);
                            afterLast.setOwner(test);
                            test.setAfterLast(afterLast);
                            if (afterLast.getId() == null)
                            {
                                afterLast.setId("ALA");
                            }
                        } 
                        else if (reader.getLocalName().equals("include"))
                        {
                            Test includedTest = (Test)processInclude(reader, "test");
                            if (test.getId() != null)
                            {
                            	includedTest.setId(test.getId());
                            }
                            if (test.getName() != null)
                            {
                            	includedTest.setName(test.getName());
                            }
                            if (test.getDescription() != null)
                            {
                            	includedTest.setDescription(test.getDescription());
                            }
                            if (test.getTargetVersions() != null)
                            {
                            	includedTest.setTargetVersions(test.getTargetVersions());
                            }
                            if (test.getTargetRequirements() != null)
                            {
                            	includedTest.setTargetRequirements(test.getTargetRequirements());
                            }
                            if (test.getTestCase() != null)
                            {
                            	includedTest.setTestCase(test.getTestCase());
                            }
                            test = includedTest;
                            processingDone = true;
                        }
                        else if (reader.getLocalName().equals("dynamic-include"))
                        {
                            DynamicInclude dynamicInclude = processDynamicInclude(reader);
                            test.setDynamicInclude(dynamicInclude);
                            dynamicInclude.setOwner(test);
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("test")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        if (log.isTraceEnabled())
    	{
        	if (!inclusion) 
    		{
        		depth--;
    		}
    	}
        return test;
    }
    
    protected Object processInclude(XMLStreamReader reader, String allowedIncludeElement) throws XMLStreamException, IOException
    {
        Include include = null;
        Object includedElement = null;
        String foundedElement = null;
        if (reader.getLocalName() == "include")
        {
            include = new Include();
            include.setFile(reader.getAttributeValue(null, "file"));
            
            InputStream includingElementStream = null;
            XMLStreamReader includeReader = null;
            try
            {

                URL includedFile = ClassLoader.getSystemResource(include.getFile());
                if (includedFile == null)
                {
                    throw new TomConfigurationError(MessageFormat.format(
                            "Error including file: ''{0}'' file not found.", include.getFile()));
                }
                
                includingElementStream = includedFile.openStream();
                if (includingElementStream == null)
                {
                    throw new TomConfigurationError(MessageFormat.format(
                            "Error including file: ''{0}'' file not found.", include.getFile()));
                }
                
	            includeReader = createReader(includingElementStream);
	            while(includeReader.hasNext()) {
	                
	                int _event = includeReader.next();
	                if (_event == XMLEvent.START_ELEMENT)
	                {
	                    break;
	                }
	            }
	            
	            foundedElement = includeReader.getLocalName();
	            if ("test-suite".equals(foundedElement) && foundedElement.equals(allowedIncludeElement))
	            {
	                includedElement = processTestSuite(includeReader, true);
	            }
	            else if ("test-case".equals(foundedElement) && foundedElement.equals(allowedIncludeElement))
	            {
	                includedElement = processTestCase(includeReader, true);
	            }
	            else if ("test".equals(foundedElement) && foundedElement.equals(allowedIncludeElement))
	            {
	                includedElement = processTest(includeReader, true);
	            }
	            else if (("script".equals(foundedElement) 
	            		|| "http-request".equals(foundedElement)) 
	            		&& foundedElement.equals(allowedIncludeElement))
	            {
	            	includedElement = processAction(includeReader, true);
	            }
	            else if (("before-first".equals(foundedElement) 
	            		|| "before-each".equals(foundedElement)
	            		|| "after-each".equals(foundedElement)
	            		|| "after-last".equals(foundedElement)) 
	            		&& foundedElement.equals(allowedIncludeElement))
	            {
	            	includedElement = processWrapper(includeReader, false);
	            }
            }
            finally
            {
            	if (includingElementStream != null)
            	{
            		includingElementStream.close();
            	}
            	
            	if (includeReader != null)
            	{
            		closeReader(includeReader);
            	}
            }
        }
    
        if (includedElement == null)
        {
            throw new TomConfigurationError(MessageFormat.format(
                    "No allowed element found in ''{0}'' to include. Allowed element is ''{1}'' but ''{2}'' was found.",
                    (include != null ? include.getFile() : null), allowedIncludeElement, foundedElement));
        }
        
        return includedElement;
    }
    
    protected Script processScript(XMLStreamReader reader, boolean inclusion) throws XMLStreamException, IOException 
    {
    	if (log.isTraceEnabled())
    	{
    		if (!inclusion) 
    		{
	    		depth++;
	    		String id = null;
				if (reader.getLocalName() == "script")
				{
					id = reader.getAttributeValue(null, "id");
				}
	    		log.trace(MessageFormat.format(">>>{0} + process script... {1}", StringUtils.repeat(" |", depth), id));
	    	}
    	}
        Script script = null;
        if (reader.getLocalName() == "script")
        {
            script = new Script();
            script.setId(reader.getAttributeValue(null, "id"));
            script.setName(reader.getAttributeValue(null, "name"));
            script.setDescription(reader.getAttributeValue(null, "description"));

            String retryCount = reader.getAttributeValue(null, "retry-count");
            if (retryCount != null)
            {
            	script.setRetryCount(new ResolvableAttribute(retryCount));
            }
            
            
            String retryPeriod = reader.getAttributeValue(null, "retry-period");
            if (retryPeriod != null)
            {
            	script.setRetryPeriod(new ResolvableAttribute(retryPeriod));
            }
            
            script.setTestHandler(reader.getAttributeValue(null, "test-handler"));
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("properties"))
                        {
                        	Properties properties = processProperties(reader);
                            script.setProperties(properties);
                        } 
                        else if (reader.getLocalName().equals("dynamic-include"))
                        {
                            DynamicInclude dynamicInclude = processDynamicInclude(reader);
                            script.setDynamicInclude(dynamicInclude);
                            dynamicInclude.setOwner(script);
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("script")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        if (log.isTraceEnabled())
    	{
        	if (!inclusion) 
    		{
        		depth--;
    		}
    	}
        return script;
    }
    
    protected HttpRequest processHttpRequest(XMLStreamReader reader, boolean inclusion) throws XMLStreamException, IOException
    {
    	if (log.isTraceEnabled())
    	{
    		if (!inclusion) 
    		{
	    		depth++;
	    		String id = null;
				if (reader.getLocalName() == "http-request")
				{
					id = reader.getAttributeValue(null, "id");
				}
	    		log.trace(MessageFormat.format(">>>{0} + process http request... {1}", StringUtils.repeat(" |", depth), id));
    		}
    	}
        HttpRequest httpRequest = null;
        if (reader.getLocalName() == "http-request")
        {
            httpRequest = new HttpRequest();
            httpRequest.setId(reader.getAttributeValue(null, "id"));
            httpRequest.setName(reader.getAttributeValue(null, "name"));
            httpRequest.setDescription(reader.getAttributeValue(null, "description"));
            
            String retryCount = reader.getAttributeValue(null, "retry-count");
            if (retryCount != null)
            {
            	httpRequest.setRetryCount(new ResolvableAttribute(retryCount));
            }
            
            
            String retryPeriod = reader.getAttributeValue(null, "retry-period");
            if (retryPeriod != null)
            {
            	httpRequest.setRetryPeriod(new ResolvableAttribute(retryPeriod));
            }
            
            httpRequest.setMethod(reader.getAttributeValue(null, "method"));
           
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("properties"))
                        {
                        	Properties properties = processProperties(reader);
                            httpRequest.setProperties(properties);
                        } 
                        else if (reader.getLocalName().equals("request-uri"))
                        {
                            ResolvableElement uri = processRequestUri(reader);
                            httpRequest.setRequestUri(uri);
                        } 
                        else if (reader.getLocalName().equals("authorization"))
                        {
                            Authorization authorization = processAuthorization(reader);
                            httpRequest.setAuthorization(authorization);
                        }
                        else if (reader.getLocalName().equals("header-fields"))
                        {
                            Map<ResolvableElement, ResolvableAssertionAwareElement> headerFields = processHeaderFields(reader);
                            httpRequest.setHeaderFields(headerFields);
                        }
                        else if (reader.getLocalName().equals("parameters"))
                        {
                            Map<ResolvableElement, ResolvableElement> parameters = processParameters(reader);
                            httpRequest.setParameters(parameters);
                        }
                        else if (reader.getLocalName().equals("message-body"))
                        {
                            MessageBody messageBody = processMessageBody(reader);
                            httpRequest.setMessageBody(messageBody);
                        }
                        else if (reader.getLocalName().equals("http-response"))
                        {
                            HttpResponse httpResponse = processHttpResponse(reader);
                            httpRequest.setHttpResponse(httpResponse);
                        }
                        else if (reader.getLocalName().equals("include"))
                        {
                            HttpRequest includedHttpRequest = (HttpRequest)processInclude(reader, "http-request");
                            if (httpRequest.getId() != null)
                            {
                            	includedHttpRequest.setId(httpRequest.getId());
                            }
                            if (httpRequest.getName() != null)
                            {
                            	includedHttpRequest.setName(httpRequest.getName());
                            }
                            if (httpRequest.getDescription() != null)
                            {
                            	includedHttpRequest.setDescription(httpRequest.getDescription());
                            }
                            if (httpRequest.getMethod() != null)
                            {
                            	includedHttpRequest.setMethod(httpRequest.getMethod());
                            }
                            if (httpRequest.getRetryCount() != null)
                            {
                            	includedHttpRequest.setRetryCount(httpRequest.getRetryCount());
                            }
                            if (httpRequest.getRetryPeriod() != null)
                            {
                            	includedHttpRequest.setRetryPeriod(httpRequest.getRetryPeriod());
                            }
                            if (httpRequest.getOwner() != null)
                            {
                            	includedHttpRequest.setOwner(httpRequest.getOwner());
                            }
                            httpRequest = includedHttpRequest;
                            processingDone = true;
                        }
                        else if (reader.getLocalName().equals("dynamic-include"))
                        {
                            DynamicInclude dynamicInclude = processDynamicInclude(reader);
                            httpRequest.setDynamicInclude(dynamicInclude);
                            dynamicInclude.setOwner(httpRequest);
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("http-request")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        if (log.isTraceEnabled())
    	{
        	if (!inclusion) 
    		{
        		depth--;
    		}
    	}
        return httpRequest;
    }

    protected ResolvableElement processRequestUri(XMLStreamReader reader) throws XMLStreamException
    {
        ResolvableElement resolvableElement = null;
        if (reader.getLocalName() == "request-uri")
        {
            resolvableElement = new ResolvableElement();
            resolvableElement.setValueResolver(reader.getAttributeValue(null, "value-resolver"));

            StringBuilder text = null;
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.CHARACTERS:

                        if (text == null)
                        {
                            text = new StringBuilder();
                        }
                        text.append(reader.getText());
                        
                        break;
                      
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("request-uri")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }

            if (text != null)
            {
                resolvableElement.setValue(text.toString());
            }
            
        }
        
        return resolvableElement;
    }
    
    protected Authorization processAuthorization(XMLStreamReader reader) throws XMLStreamException
    {
        Authorization authorization = null;
        if (reader.getLocalName() == "authorization")
        {
            authorization = new Authorization();
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("auth-method"))
                        {
                            authorization.setMethod(processResolvableElement(reader));
                        }
                        else if (reader.getLocalName().equals("auth-preemptive"))
                        {
                            authorization.setPreemptive(processResolvableElement(reader));
                        }
                        else if (reader.getLocalName().equals("auth-realm"))
                        {
                            authorization.setRealm(processResolvableElement(reader));
                        }
                        else if (reader.getLocalName().equals("auth-username"))
                        {
                            authorization.setUsername(processResolvableElement(reader));
                        }
                        else if (reader.getLocalName().equals("auth-password"))
                        {
                            authorization.setPassword(processResolvableElement(reader));
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("authorization")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        return authorization;
    }

    protected ResolvableElement processResolvableElement(XMLStreamReader reader) throws XMLStreamException
    {
        ResolvableElement resolvableElement = new ResolvableElement();
        resolvableElement.setValueResolver(reader.getAttributeValue(null, "value-resolver"));
        resolvableElement.setValue(reader.getAttributeValue(null, "value"));

        StringBuilder text = null;
        boolean processingDone = false;
        while(reader.hasNext()) {
            
            int event = reader.next();
            switch(event)
            {
                case XMLEvent.CHARACTERS:

                    if (text == null)
                    {
                        text = new StringBuilder();
                    }
                    text.append(reader.getText());
                    
                    break;
                    
                case XMLEvent.END_ELEMENT:
                    
                    processingDone = true;
                    break;
            }
            
            if (processingDone) {
                break;
            }
        }

        if (text != null)
        {
            resolvableElement.setValue(text.toString());
        }
        
        return resolvableElement;
    }

    protected ResolvableAssertionAwareElement processResolvableAssertionAwareElement(XMLStreamReader reader) throws XMLStreamException
    {
        ResolvableAssertionAwareElement resolvableElement = new ResolvableAssertionAwareElement();
        resolvableElement.setAssertionHandler(reader.getAttributeValue(null, "assertion-handler"));
        resolvableElement.setValueResolver(reader.getAttributeValue(null, "value-resolver"));
        resolvableElement.setValue(reader.getAttributeValue(null, "value"));

        StringBuilder text = null;
        boolean processingDone = false;
        while(reader.hasNext()) {
            
            int event = reader.next();
            switch(event)
            {
                case XMLEvent.CHARACTERS:
                    
                    if (text == null)
                    {
                        text = new StringBuilder();
                    }
                    text.append(reader.getText());
                    
                    break;
                    
                case XMLEvent.END_ELEMENT:
                    
                    processingDone = true;
                    break;
            }
            
            if (processingDone) {
                break;
            }
        }
        
        if (text != null)
        {
            resolvableElement.setValue(text.toString());
        }
        
        return resolvableElement;
    }

    protected Map<ResolvableElement, ResolvableAssertionAwareElement> processHeaderFields(XMLStreamReader reader) throws XMLStreamException
    {
        Map<ResolvableElement, ResolvableAssertionAwareElement> headerFields = null;
        if (reader.getLocalName() == "header-fields")
        {
            headerFields = new HashMap<ResolvableElement, ResolvableAssertionAwareElement>();
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("header-field"))
                        {
                            Map.Entry<ResolvableElement, ResolvableAssertionAwareElement> headerField = processHeaderField(reader);
                            headerFields.put(headerField.getKey(), headerField.getValue());
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("header-fields")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        return headerFields;
    }

    protected MessageBody processMessageBody(XMLStreamReader reader) throws XMLStreamException
    {
        MessageBody messageBody = null;
        if (reader.getLocalName() == "message-body")
        {
            messageBody = new MessageBody();
            messageBody.setIgnoreWhiteSpace(Boolean.parseBoolean(reader.getAttributeValue(null, "ignore-white-space")));
            messageBody.setAssertionHandler(reader.getAttributeValue(null, "assertion-handler"));
            messageBody.setContentType(reader.getAttributeValue(null, "content-type"));
            messageBody.setCharset(reader.getAttributeValue(null, "charset"));
            messageBody.setValueResolver(reader.getAttributeValue(null, "value-resolver"));
            messageBody.setValue(reader.getAttributeValue(null, "value"));
            
            StringBuilder text = null;
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.CHARACTERS:

                        if (text == null)
                        {
                            text = new StringBuilder();
                        }
                        text.append(reader.getText());
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("message-body")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }

            if (text != null)
            {
                messageBody.setValue(text.toString());
            }
            
        }
        
        return messageBody;
    }

    protected Map.Entry<ResolvableElement, ResolvableAssertionAwareElement> processHeaderField(XMLStreamReader reader) throws XMLStreamException
    {
        ResolvableElement headerFieldName = null;
        ResolvableAssertionAwareElement headerFieldValue = null;
        if (reader.getLocalName() == "header-field")
        {
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("header-field-name"))
                        {
                            headerFieldName = processResolvableElement(reader);
                        }
                        else if (reader.getLocalName().equals("header-field-value"))
                        {
                            headerFieldValue = processHeaderFieldValue(reader);
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("header-field")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        return new AbstractMap.SimpleEntry<ResolvableElement, ResolvableAssertionAwareElement>(headerFieldName, headerFieldValue);
    }

    protected Map<ResolvableElement, ResolvableElement> processParameters(XMLStreamReader reader) throws XMLStreamException
    {
        Map<ResolvableElement, ResolvableElement> parameters = null;
        if (reader.getLocalName() == "parameters")
        {
            parameters = new HashMap<ResolvableElement, ResolvableElement>();
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("parameter"))
                        {
                            Map.Entry<ResolvableElement, ResolvableElement> parameter = processParameter(reader);
                            parameters.put(parameter.getKey(), parameter.getValue());
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("parameters")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        return parameters;
    }

    protected Map.Entry<ResolvableElement, ResolvableElement> processParameter(XMLStreamReader reader) throws XMLStreamException
    {
        ResolvableElement parameterName = null;
        ResolvableElement parameterValue = null;
        if (reader.getLocalName() == "parameter")
        {
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("parameter-name"))
                        {
                            parameterName = processParameterName(reader);
                        }
                        else if (reader.getLocalName().equals("parameter-value"))
                        {
                            parameterValue = processParameterValue(reader);
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("parameter")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        return new AbstractMap.SimpleEntry<ResolvableElement, ResolvableElement>(parameterName, parameterValue);
    }

    protected ResolvableElement processHeaderFieldName(XMLStreamReader reader) throws XMLStreamException
    {
        ResolvableElement headerFieldValue = null;
        if (reader.getLocalName() == "header-field-name")
        {
            headerFieldValue = new ResolvableElement();
            headerFieldValue.setValueResolver(reader.getAttributeValue(null, "value-resolver"));
            headerFieldValue.setValue(reader.getElementText());
        }
        
        return headerFieldValue;
    }

    protected ResolvableAssertionAwareElement processHeaderFieldValue(XMLStreamReader reader) throws XMLStreamException
    {
        ResolvableAssertionAwareElement headerFieldValue = null;
        if (reader.getLocalName() == "header-field-value")
        {
            headerFieldValue = new ResolvableAssertionAwareElement();
            headerFieldValue.setAssertionHandler(reader.getAttributeValue(null, "assertion-handler"));
            headerFieldValue.setValueResolver(reader.getAttributeValue(null, "value-resolver"));
            headerFieldValue.setValue(reader.getElementText());
        }
        
        return headerFieldValue;
    }

    protected ResolvableElement processParameterName(XMLStreamReader reader) throws XMLStreamException
    {
        ResolvableElement headerFieldValue = null;
        if (reader.getLocalName() == "parameter-name")
        {
            headerFieldValue = new ResolvableElement();
            headerFieldValue.setValueResolver(reader.getAttributeValue(null, "value-resolver"));
            headerFieldValue.setValue(reader.getElementText());
        }
        
        return headerFieldValue;
    }

    protected ResolvableElement processParameterValue(XMLStreamReader reader) throws XMLStreamException
    {
        ResolvableElement headerFieldValue = null;
        if (reader.getLocalName() == "parameter-value")
        {
            headerFieldValue = new ResolvableElement();
            headerFieldValue.setValueResolver(reader.getAttributeValue(null, "value-resolver"));
            headerFieldValue.setValue(reader.getElementText());
        }
        
        return headerFieldValue;
    }

    protected Insert processInsert(XMLStreamReader reader) throws XMLStreamException
    {
        Insert insert = null;
        if (reader.getLocalName() == "insert")
        {
            insert = new Insert();
            insert.setInsertHandler(reader.getAttributeValue(null, "insert-handler"));
            insert.setText(reader.getElementText());
        }
        
        return insert;
    }

    protected HttpResponse processHttpResponse(XMLStreamReader reader) throws XMLStreamException
    {
        HttpResponse httpResponse = null;
        if (reader.getLocalName() == "http-response")
        {
            httpResponse = new HttpResponse();
            httpResponse.setAssertionHandler(reader.getAttributeValue(null, "assertion-handler"));
            httpResponse.setValueResolver(reader.getAttributeValue(null, "value-resolver"));
            httpResponse.setValue(reader.getAttributeValue(null, "value"));
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("status-line"))
                        {
                            StatusLine statusLine = processStatusLine(reader);
                            httpResponse.setStatusLine(statusLine);
                        }
                        else if (reader.getLocalName().equals("header-fields"))
                        {
                            Map<ResolvableElement, ResolvableAssertionAwareElement> headerFields = processHeaderFields(reader);
                            httpResponse.setHeaderFields(headerFields);
                        }
                        else if (reader.getLocalName().equals("message-body"))
                        {
                            MessageBody messageBody = processMessageBody(reader);
                            httpResponse.setMessageBody(messageBody);
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("http-response")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        return httpResponse;
    }

    protected StatusLine processStatusLine(XMLStreamReader reader) throws XMLStreamException
    {
        StatusLine httpResponse = null;
        if (reader.getLocalName() == "status-line")
        {
            httpResponse = new StatusLine();
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("http-version"))
                        {
                            httpResponse.setHttpVersion(processResolvableAssertionAwareElement(reader));
                        }
                        else if (reader.getLocalName().equals("status-code"))
                        {
                            httpResponse.setStatusCode(processResolvableAssertionAwareElement(reader));
                        }
                        else if (reader.getLocalName().equals("reason-phrase"))
                        {
                            httpResponse.setReasonPhrase(processResolvableAssertionAwareElement(reader));
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("status-line")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        return httpResponse;
    }

    protected Properties processProperties(XMLStreamReader reader) throws XMLStreamException
    {
        Properties properties = null;
        if (reader.getLocalName() == "properties")
        {
            properties = Properties.builder().build();
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("property"))
                        {
                            Property property = processProperty(reader);
                            if (!properties.addProperty(property))
                            {
                                throw new TomConfigurationError(MessageFormat.format(
                                        "Duplicate property name: ''{0}'' at line {1}. Properties in same scope should have unique names.", 
                                        property.getName(), reader.getLocation().getLineNumber()));
                            }
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("properties")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        return properties;
    }

    protected Property processProperty(XMLStreamReader reader) throws XMLStreamException
    {
        Property property = null;
        if (reader.getLocalName() == "property")
        {
            property = Property.builder()
                    .withName(reader.getAttributeValue(null, "name"))
                    .withValue(reader.getAttributeValue(null, "value"))
                    .withFilters(reader.getAttributeValue(null, "filters"))
                    .withValueResolver(reader.getAttributeValue(null, "value-resolver"))
                    .withResolveStrategy(reader.getAttributeValue(null, "resolve-strategy"))
                    .withInterpolationStrategy(reader.getAttributeValue(null, "interpolation-strategy"))
                    .build();

            StringBuilder text = null;
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.CHARACTERS:

                        if (text == null)
                        {
                            text = new StringBuilder();
                        }
                        text.append(reader.getText());
                        
                        break;
                       
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("property")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }

            if (text != null)
            {
                property.setValue(text.toString());
            }
            
        }
        
        return property;
    }

    protected Map<String, String> processConfig(XMLStreamReader reader) throws XMLStreamException
    {
        Map<String, String> config = null;
        if (reader.getLocalName() == "test-project-config")
        {
            config = new LinkedHashMap<String, String>();
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                    	Map.Entry<String, String> configEntry = processConfigEntry(reader);
                    	if (configEntry != null)
                    	{
                    		config.put(configEntry.getKey(), configEntry.getValue());
                    	}
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("test-project-config")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        return config;
    }

    protected Map.Entry<String, String> processConfigEntry(XMLStreamReader reader) throws XMLStreamException
    {
        Map.Entry<String, String> configEntry = null;
        String configName = reader.getLocalName();
   
        StringBuilder text = null;
        boolean processingDone = false;
        while(reader.hasNext()) {
            
            int event = reader.next();
            switch(event)
            {
                case XMLEvent.CHARACTERS:

                    if (text == null)
                    {
                        text = new StringBuilder();
                    }
                    text.append(reader.getText());
                    
                    break;
                   
                case XMLEvent.END_ELEMENT:
                	
                    processingDone = true;
                    break;
            }
            
            if (processingDone) {
                break;
            }
        }

        if (text != null)
        {
            configEntry = new AbstractMap.SimpleEntry<String, String>(configName, text.toString());;
        }
        
        return configEntry;
    }

    protected DynamicInclude processDynamicInclude(XMLStreamReader reader) throws XMLStreamException, IOException 
    {
    	DynamicInclude test = null;
        if (reader.getLocalName() == "dynamic-include")
        {
            test = new DynamicInclude();
            
            String file = reader.getAttributeValue(null, "file");
            if (file != null)
            {
                test.setFile(new ResolvableAttribute(file));
            }
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("properties"))
                        {
                        	Properties properties = processProperties(reader);
                            test.setProperties(properties);
                        } 
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals("dynamic-include")) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }
        
        return test;
    }

    protected AbstractAction processAction(XMLStreamReader reader, boolean inclusion) throws XMLStreamException, IOException 
    {
        AbstractAction action = null;
        if (reader.getLocalName() == "script")
        {
        	action = processScript(reader, inclusion);
        }
        if (reader.getLocalName() == "http-request")
        {
        	action = processHttpRequest(reader, inclusion);
        }
        
        return action;
    }

    protected Wrapper processWrapper(XMLStreamReader reader, boolean inclusion) throws XMLStreamException, IOException 
    {
    	Wrapper wrapper = null;
        String localName = reader.getLocalName();
        
    	if (log.isTraceEnabled())
    	{
    		if (!inclusion) 
    		{
	    		depth++;
	    		String id = null;
				if (localName == "before-first" || localName == "after-last"
		        		|| localName == "before-each" || localName == "after-each")
				{
					id = reader.getAttributeValue(null, "id");
				}
	    		log.trace(MessageFormat.format(">>>{0} + process wrapper... {1}", StringUtils.repeat(" |", depth), id));
    		}
    	}
    	
        if (localName == "before-first" || localName == "after-last"
        		|| localName == "before-each" || localName == "after-each")
        {
            wrapper = new Wrapper();
            wrapper.setId(reader.getAttributeValue(null, "id"));
            wrapper.setName(reader.getAttributeValue(null, "name"));
            wrapper.setDescription(reader.getAttributeValue(null, "description"));
            wrapper.setPerRun(reader.getAttributeValue(null, "per-run"));
            wrapper.setPerThread(reader.getAttributeValue(null, "per-thread"));
            
            boolean processingDone = false;
            while(reader.hasNext()) {
                
                int event = reader.next();
                switch(event)
                {
                    case XMLEvent.START_ELEMENT:
                        
                        if (reader.getLocalName().equals("properties"))
                        {
                        	Properties properties = processProperties(reader);
                            wrapper.setProperties(properties);
                        } 
                        else if (reader.getLocalName().equals("script") 
                        		|| reader.getLocalName().equals("http-request"))
                        {
                            AbstractAction action = processAction(reader, false);
                            action.setOwner(wrapper);
                            if (!wrapper.addAction(action))
                            {
                                throw new TomConfigurationError(MessageFormat.format(
                                        "Multiple actions with id ''{0}'' found in wrapper ''{1}''. Actions under one wrapper should be unique.", action.getId(), localName));
                            }
                        }
                        else if (reader.getLocalName().equals("include"))
                        {
                            Wrapper includedWrapper = (Wrapper)processInclude(reader, localName);
                            includedWrapper.setName(wrapper.getName());
                            includedWrapper.setDescription(wrapper.getDescription());
                            
                            if (wrapper.getId() != null)
                            {
                            	includedWrapper.setId(wrapper.getId());
                            }
                            if (wrapper.getName() != null)
                            {
                            	includedWrapper.setName(wrapper.getName());
                            }
                            if (wrapper.getDescription() != null)
                            {
                            	includedWrapper.setDescription(wrapper.getDescription());
                            }
                            if (wrapper.getOwner() != null)
                            {
                            	includedWrapper.setOwner(wrapper.getOwner());
                            }
                            if (wrapper.getPerRun() != null)
                            {
                            	includedWrapper.setPerRun(wrapper.getPerRun());
                            }
                            if (wrapper.getPerThread() != null)
                            {
                            	includedWrapper.setPerThread(wrapper.getPerThread());
                            }
                            
                            wrapper = includedWrapper;
                            processingDone = true;
                        }
                        else if (reader.getLocalName().equals("dynamic-include"))
                        {
                            DynamicInclude dynamicInclude = processDynamicInclude(reader);
                            wrapper.setDynamicInclude(dynamicInclude);
                            dynamicInclude.setOwner(wrapper);
                        }
                        
                        break;
                        
                    case XMLEvent.END_ELEMENT:
                        
                        if (reader.getLocalName().equals(localName)) 
                        {
                            processingDone = true;
                        }
                        
                        break;
                }
                
                if (processingDone) {
                    break;
                }
            }
        }

        if (log.isTraceEnabled())
    	{
        	if (!inclusion) 
    		{
        		depth--;
    		}
    	}
        
        return wrapper;
    }
    
}
