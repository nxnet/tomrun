package io.nxnet.tomrun.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import io.nxnet.tomrun.model.TestSuite;

public class StaxTestSuiteReader extends AbstractStaxReader implements TestSuiteReader 
{

    public TestSuiteIterator read(InputStream in) throws TomConfigurationError
    {
        if (in == null)
        {
            throw new IllegalArgumentException("Input stream can't be null");
        }
        
        reader = createReader(new BufferedInputStream(in));
        return new InternalTestSuiteIterator(); 
    }

    public void close() throws IOException
    {
    	try
        {
        	closeReader(reader);
        }
        finally
        {
        	reader = null;
        }
    }

    private class InternalTestSuiteIterator implements TestSuiteIterator {
        
        private Set<String> testCaseIds = new HashSet<String>();
        
        public TestSuite next() throws TomConfigurationError
        {
            try
            {
                TestSuite testCase = processTestSuite(reader, false);
                if (testCase != null && !testCaseIds.add(testCase.getId())) 
                {
                    throw new TomConfigurationError(MessageFormat.format("Duplicate test case ids ''{0}''", testCase.getId()));
                }
                return testCase;
            }
            catch (XMLStreamException e)
            {
                throw new TomConfigurationError(e);
            }
            catch (IOException e)
            {
                throw new TomConfigurationError(e);
            }
        }

        public boolean hasNext()
        {
            try
            {
                while(reader.hasNext()) {
                    
                    int event = reader.next();
                    if (event == XMLEvent.START_ELEMENT && "test-suite".equals(reader.getLocalName()))
                    {
                        return true;
                    }
                }
                
                return false;
            }
            catch (XMLStreamException e)
            {
                throw new RuntimeException(e);
            }
        }

    }

}
