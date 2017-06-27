package io.nxnet.tomrun.parser;

import org.junit.Before;
import org.junit.Test;

import io.nxnet.tomrun.model.TestSuite;
import io.nxnet.tomrun.parser.StaxTestSuiteReader;
import io.nxnet.tomrun.parser.TestSuiteIterator;

public class StaxTestSuiteReaderTest
{
    private StaxTestSuiteReader reader;

    @Before
    public void setUp() throws Exception
    {
        reader = new StaxTestSuiteReader();
    }

    @Test
    public void read() throws Exception
    {
        try
        {
            TestSuiteIterator iter = reader.read(getClass().getClassLoader().getResourceAsStream("tom.xml"));
            TestSuite testSuite = null;
            while (iter.hasNext())
            {
                testSuite = iter.next();
                System.out.println(testSuite);
                // System.out.println(SizeOf.humanReadable(SizeOf.deepSizeOf(testSuite)));
            }
        }
        finally
        {
            reader.close();
        }
    }
}
