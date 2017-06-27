package io.nxnet.tomrun.execution;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ExecutionNodeWritter
{
    private Writer writer;

    private IdentFormatter identFormatter;

    public ExecutionNodeWritter(Writer writer)
    {
        this.writer = writer;
        this.identFormatter = new IdentFormatter();
    }

    public void write(ExecutionNode node) throws IOException
    {
        this.writeNodeTree(node);
    }

    public void close() throws IOException
    {
        if (this.writer != null)
        {
            this.writer.close();
        }
    }
    
    void writeNodeTree(ExecutionNode node) throws IOException
    {
        // Iterate over nodes and print each one
        ExecutionNode printingNode = null;
        Iterator<ExecutionNode> printingNodeIter = node.getIter();
        while (printingNodeIter.hasNext())
        {
            printingNode = printingNodeIter.next();
            if (printingNode == node)
            {
                this.writeNodeLine(printingNode);
            }
            else
            {
                this.writeNodeTree(printingNode);
            }
        }
    }

    void writeNodeLine(ExecutionNode e) throws IOException
    {
        // Calculate indent
        identFormatter.put(e.getLevel(), e.isLast());
        String indentation = identFormatter.formatIdentation(" ");

        // Compose line
        String line = indentation + e.getType().getSimbol() + ":" + e.getId() + " " + e.getName() + " - " + e.getDescription()
                + " (" + e.isLast() + ")\n";

        // Print line
        this.writer.write(line);
    }
    
    private class IdentFormatter {
        
        private Map<Integer, String> identTokens = new TreeMap<Integer, String>();
        
        public void put(int level, boolean isLast)
        {
            // Put level character
            if (isLast)
            {
                identTokens.put(level, "\\- ");
            }
            else
            {
                identTokens.put(level, "+- ");
            }
            
            // Recalculate other level characters  
            Iterator<Map.Entry<Integer, String>> iter = identTokens.entrySet().iterator();
            Map.Entry<Integer, String> entry = null;
            while (iter.hasNext())
            {
                entry = iter.next();
                if (entry.getKey() < level)
                {
                    if ("\\- ".equals(entry.getValue()))
                    {
                        entry.setValue("   ");
                    }
                    else if ("+- ".equals(entry.getValue()))
                    {
                        entry.setValue("|  ");
                    }
                }
                else if (entry.getKey() > level)
                {
                    iter.remove();
                }
            }
            
        }

        public String formatIdentation(String delimiter)
        {
            StringBuilder builder = new StringBuilder();
            int tokensLeft = this.identTokens.size();
            for (String identToken : this.identTokens.values())
            {
                builder.append(identToken);
                if (--tokensLeft > 0)
                {
                    builder.append(delimiter);
                }
            }

            return builder.toString();
        }
    }
}
