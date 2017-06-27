package io.nxnet.tomrun.agent;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/** A {@link URLStreamHandler} that handles resources on the classpath. */
public class ClasspathUrlStreamHandler extends URLStreamHandler {
    /** The classloader to find resources from. */
    private final ClassLoader classLoader;

    public ClasspathUrlStreamHandler() {
        this.classLoader = getClass().getClassLoader();
    }

    public ClasspathUrlStreamHandler(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        final URL resourceUrl = getClass().getResource(u.getPath());
        return resourceUrl.openConnection();
    }
}