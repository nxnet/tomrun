package io.nxnet.tomrun.assertions;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import io.nxnet.tomrun.context.Context;

public class FileInsertHandler extends AbstractHandler implements InsertHandler
{
    private String fileName;
    
    private Charset charset;
    
    public FileInsertHandler(Context testCtx, String fileName)
    {
        this(testCtx, fileName, Charset.defaultCharset());
    }

    public FileInsertHandler(Context testCtx, String fileName, Charset charset)
    {
        super(testCtx);
        this.fileName = fileName;
        this.charset = charset;
    }

    public FileInsertHandler(Context testCtx, String fileName, String charset)
    {
        super(testCtx);
        
        this.fileName = fileName;
        
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
    
    public String doInsert() throws Exception
    {
        try
        {
            return readFile(fileName, charset);
        }
        catch (IOException e)
        {
            throw new IOException(getFullMessage("Error inserting content into test case configuration"), e);
        }
    }
    
    private String readFile(String path, Charset charset) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));
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
