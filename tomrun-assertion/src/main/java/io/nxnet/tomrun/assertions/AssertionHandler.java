package io.nxnet.tomrun.assertions;



public interface AssertionHandler extends Handler
{
    public boolean assertEquals(String message, String expected, String actual) throws AssertionError;
}
