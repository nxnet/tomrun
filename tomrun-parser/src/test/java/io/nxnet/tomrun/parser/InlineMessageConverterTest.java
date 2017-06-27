package io.nxnet.tomrun.parser;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

import io.nxnet.tomrun.parser.InlineMessageConverter;

public class InlineMessageConverterTest
{

    private static Logger log = LoggerFactory.getLogger(InlineMessageConverterTest.class);

    @Test
    public void doConvert() throws Exception
    {
        String conversionString = new InlineMessageConverter().doConvert("first line\nsecond line\n\n");
        log.debug("----------------");
        log.debug(conversionString);
        log.debug("-------------------");
        assertEquals("Enexpected conversion",
                "            |   first line\n            |   second line\n            |   \n            |   ",
                conversionString);
    }
}
