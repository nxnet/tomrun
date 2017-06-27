package io.nxnet.tomrun.model;

public class Insert
{
    private String text;

    private String insertHandler;

    public String getInsertHandler()
    {
        return insertHandler;
    }

    public void setInsertHandler(String insertHandler)
    {
        this.insertHandler = insertHandler;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public String toString()
    {
        return "Insert [text=" + text + ", insertHandler=" + insertHandler + "]";
    }

}
