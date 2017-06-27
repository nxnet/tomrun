package io.nxnet.tomrun.parser;

import org.junit.Before;
import org.junit.Test;

import io.nxnet.tomrun.model.TestProject;
import io.nxnet.tomrun.parser.StaxTestProjectReader;

public class StaxTestProjectReaderTest
{
    private StaxTestProjectReader reader;

    @Before
    public void setUp() throws Exception
    {
        reader = new StaxTestProjectReader();
    }

    @Test
    public void read() throws Exception
    {
        try
        {
            TestProject testProject = reader.read(getClass().getClassLoader().getResourceAsStream("tom.xml")).next();
            System.out.println(testProject);
            // System.out.println(SizeOf.humanReadable(SizeOf.deepSizeOf(testProject)));
        }
        finally
        {
            reader.close();
        }
    }
}
