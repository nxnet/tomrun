package io.nxnet.tomrun.parser;

import java.io.InputStream;

import io.nxnet.tomrun.model.TestProject;

public interface TomReader {

	public TestProject read(InputStream tomInputStream) throws TomConfigurationError;
	
}
