package io.nxnet.tomrun.resolver.def;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.MessageFormat;

import io.nxnet.tomrun.resolver.ValueResolver;
import io.nxnet.tomrun.resolver.ValueResolverException;

public class FileResolver implements ValueResolver
{
    private File file;
    
    private Charset charset;
    
    public FileResolver(String fileName)
    {
        this(new File(fileName));
    }

    public FileResolver(File file)
    {
        this(file, Charset.defaultCharset());
    }

    public FileResolver(String fileName, Charset charset)
    {
        this(new File(fileName), charset);
    }

    public FileResolver(File file, Charset charset)
    {
        this.file = file;
        this.charset = charset;
    }

    public FileResolver(String fileName, String charset)
    {
        this(new File(fileName), charset);
    }

    public FileResolver(File file, String charset)
    {
        this.file = file;
        
        Charset cs = null;
        try
        {
            cs = Charset.forName(charset);
        }
        catch (Exception e)
        {
            cs = Charset.defaultCharset();
        }
        this.charset = cs;
    }

    @Override
    public String resolve() throws ValueResolverException
    {
        try
        {
            return readFile(file, charset);
        }
        catch (IOException e)
        {
            throw new ValueResolverException(MessageFormat.format("Error resolving file ''{0}''", file), e);
        }
    }
    
    private String readFile(File path, Charset charset) throws IOException {
        FileInputStream stream = new FileInputStream(path);
        try {
          FileChannel fc = stream.getChannel();
          MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
          return charset.decode(bb).toString();
        }
        finally {
          stream.close();
        }
      }

}
