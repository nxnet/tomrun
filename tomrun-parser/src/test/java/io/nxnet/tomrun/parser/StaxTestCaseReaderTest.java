package io.nxnet.tomrun.parser;

import org.junit.Before;
import org.junit.Test;

import io.nxnet.tomrun.model.TestCase;
import io.nxnet.tomrun.parser.StaxTestCaseReader;
import io.nxnet.tomrun.parser.TestCaseIterator;

public class StaxTestCaseReaderTest
{
    private StaxTestCaseReader reader;

    @Before
    public void setUp() throws Exception
    {
        reader = new StaxTestCaseReader();
    }

    @Test
    public void read() throws Exception
    {
        try
        {
            TestCaseIterator iter = reader.read(getClass().getClassLoader().getResourceAsStream("tom.xml"));
            TestCase testCase = null;
            while (iter.hasNext())
            {
                System.out.println(iter.next());
            }
        }
        finally
        {
            reader.close();
        }
    }
}
