package io.nxnet.tomrun.execution;

import java.util.HashMap;
import java.util.Map;

public enum ExecutionNodeType
{
    PROJECT("P"),
    
    SUITE("S"),
    
    CASE("C"),
    
    TEST("T"),
    
    ACTION("A"),
    
    WRAPPER("W");

    private static Map<String, ExecutionNodeType> simbolMap;

    private String simbol;
    
    static
    {
        initSimbolMap();
    }
    
    private ExecutionNodeType(String simbol)
    {
        this.simbol = simbol;
    }
    
    public static ExecutionNodeType getEnum(String simbol)
    {
        return simbolMap.get(simbol);
    }
    
    public String getSimbol()
    {
        return this.simbol;
    }
    
    private static void initSimbolMap() 
    {
        simbolMap = new HashMap<String, ExecutionNodeType>();
        for (ExecutionNodeType executionNodeType : ExecutionNodeType.values())
        {
            simbolMap.put(executionNodeType.getSimbol(), executionNodeType);
        }
    }
}
