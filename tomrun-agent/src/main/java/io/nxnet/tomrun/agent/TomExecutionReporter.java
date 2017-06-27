package io.nxnet.tomrun.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeEvent;
import io.nxnet.tomrun.execution.ExecutionNodeType;

public class TomExecutionReporter implements Observer
{
    protected Map<ExecutionNodeType, Integer> typeCounter;

    protected IdentFormatter identFormatter;
    
    public TomExecutionReporter()
    {
    	this.identFormatter = new IdentFormatter();
    	
    	this.typeCounter = new HashMap<ExecutionNodeType, Integer>();
    	for (ExecutionNodeType executionNodeType : ExecutionNodeType.values())
    	{
    		this.typeCounter.put(executionNodeType, 0);
    	}
    }

    @Override
    public void update(Observable o, Object arg)
    {
        ExecutionNode executionNode = (ExecutionNode) o;
        ExecutionNodeEvent event = (ExecutionNodeEvent) arg;
        
        switch (event.getEventName())
        {       
            case EXEC_START:

                // On Project setUp write header
//                if (executionNode.getType() == ExecutionNodeType.PROJECT)
//                {
//                    System.out.println("+--------------------------------------------------------------+");
//                    System.out.println("|                          Test Tool                           |");
//                    System.out.println("+--------------------------------------------------------------+");
//                }

                // Print line
                printLine(executionNode);
                
                // Count execution nodes
//                Integer count = typeCounter.get(executionNode.getType());
//                if (count == null)
//                {
//                    count = 0;
//                }
//                typeCounter.put(executionNode.getType(), ++count);
                
                break;

            case EXEC_END:

                // tearDown for Project to write summary
//                if (executionNode.getType() == ExecutionNodeType.PROJECT)
//                {
//                    System.out.println("+--------------------------------------------------------------+");
//                    System.out.println(MessageFormat.format(
//                            "| Suites:{0}     Cases:{1}      Tests:{2}      Actions:{3}         |", typeCounter.get(ExecutionNodeType.SUITE),
//                            typeCounter.get(ExecutionNodeType.CASE), typeCounter.get(ExecutionNodeType.TEST), typeCounter.get(ExecutionNodeType.ACTION)));
//                    System.out.println("+--------------------------------------------------------------+");
//                }
                
                break;
        }

    }

    protected void printLine(ExecutionNode n)
    {
        // Calculate indent
        identFormatter.put(n.getLevel(), n.isLast());
        String indentation = identFormatter.formatIdentation(" ");

        // Compose line
        String line = indentation + n.getType().getSimbol() + ":" + n.getId() 
        		+ " " + n.getName() + " - " + n.getDescription();

        // Print line
        System.out.println(line);
    }
}
