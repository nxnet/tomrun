package io.nxnet.tomrun.execution.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observer;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nxnet.tomrun.context.OwnableContext;
import io.nxnet.tomrun.execution.DynamicIncludeNode;
import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeException;
import io.nxnet.tomrun.execution.ExecutionNodeFactory;
import io.nxnet.tomrun.execution.ExecutionNodeType;
import io.nxnet.tomrun.model.WrapperAwareElement;
import io.nxnet.tomrun.parser.ExecutionElementIterator;
import io.nxnet.tomrun.parser.ExecutionElementReader;
import io.nxnet.tomrun.parser.StaxActionReader;
import io.nxnet.tomrun.parser.StaxTestCaseReader;
import io.nxnet.tomrun.parser.StaxTestProjectReader;
import io.nxnet.tomrun.parser.StaxTestReader;
import io.nxnet.tomrun.parser.StaxTestSuiteReader;
import io.nxnet.tomrun.parser.StaxWrapperReader;

public class ExecutionNodeProxy extends ExecutionNodeImpl
{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected DynamicIncludeNode dynamicIncludeNode;
    
    protected ExecutionNode delegee;
    
    protected boolean loaded;
    
    protected Set<Observer> observers;

    public ExecutionNodeProxy(String id, String name, String description, ExecutionNodeType type)
    {
        super(id, name, description, type);
        observers = new HashSet<Observer>();
    }

    public DynamicIncludeNode getDynamicIncludeNode()
    {
        return dynamicIncludeNode;
    }

    public void setDynamicIncludeNode(DynamicIncludeNode dynamicIncludeNode)
    {
        this.dynamicIncludeNode = dynamicIncludeNode;
    }

    @Override
    public String toString()
    {
        return "\n" + new String(new char[getLevel()]).replace("\0", "\t") + "ExecutionNodeProxy [type=" + type + ", id="
                + id + ", name=" + name + ", description=" + description + ",\n"
                + new String(new char[getLevel()]).replace("\0", "\t") + "before=" + before + ",\n"
                + new String(new char[getLevel()]).replace("\0", "\t") + "beforeChild=" + beforeChild + ",\n"
                + new String(new char[getLevel()]).replace("\0", "\t") + "children=" + Arrays.toString(children != null ? children.toArray() : null) + ",\n"
                + new String(new char[getLevel()]).replace("\0", "\t") + "afterChild=" + afterChild + "\n"
                + new String(new char[getLevel()]).replace("\0", "\t") + "after=" + after + "\n"
                + new String(new char[getLevel()]).replace("\0", "\t") + "]";
    }

    public void doExec()
    {
        init();
        delegee.doExec();
    }

    public Iterator<ExecutionNode> iterator()
    {
        init();
        return delegee.iterator();
    }

    public void addChild(ExecutionNode n)
    {
        init();
        delegee.addChild(n);
    }

    public OwnableContext getContext()
    {
        init();
        return delegee.getContext();
    }

    public void addObserver(Observer o)
    {
        observers.add(o);
    }

    public void deleteObserver(Observer o)
    {
        observers.remove(o);
    }

    public void deleteObservers()
    {
        observers.clear();;
    }

    public int countObservers()
    {
        return observers.size();
    }

    protected void init() {
        
        boolean load = false;
        synchronized(this)
        {
            if (!loaded)
            {
                load = true;
                loaded = true;
            }
        }
        
        if (load)
        {
            InputStream tomFileInputStream = null;
            ExecutionElementReader<? extends WrapperAwareElement> reader = null;
            try
            {
                String file = dynamicIncludeNode.getFile().getValue();
                if (file != null && !file.startsWith("/"))
                {
                    file = dynamicIncludeNode.getPath() + "/" + file;
                }
                
                tomFileInputStream = getClass().getResourceAsStream(file);
                if (tomFileInputStream == null)
                {
                    throw new IllegalArgumentException("Error creating input stream for file: " + file);
                }
                
                reader = getReader(dynamicIncludeNode.getType());
                ExecutionElementIterator<? extends WrapperAwareElement> iter = reader.read(tomFileInputStream);
                WrapperAwareElement testSuite = null;
                while (iter.hasNext())
                {
                    testSuite = iter.next();
                    testSuite.setId(id);
                    testSuite.setName(name);
                    testSuite.setDescription(description);
                    delegee = ExecutionNodeFactory
                                .newInstance(testSuite.getFactory(), null)
                                       .getNode(testSuite, null);
                    delegee.setParent(parent);
                    //delegee.id = id;
                    //delegee.name = name;
                    //delegee.description = description;
                    //delegee.type = type;
                    for (Observer observer : observers)
                    {
                        delegee.addObserver(observer);
                    }
                }
            }
            catch (ExecutionNodeException e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                try
                {
                    if (reader != null)
                    {
                        reader.close();
                    }
                    
                    if (tomFileInputStream != null)
                    {
                        tomFileInputStream.close();
                    }
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    private ExecutionElementReader<? extends WrapperAwareElement> getReader(String type)
    {
        if ("P".equals(type))
        {
            return new StaxTestProjectReader();
        }
        else if ("S".equals(type))
        {
            return new StaxTestSuiteReader();
        }
        else if ("C".equals(type))
        {
            return new StaxTestCaseReader();
        }
        else if ("T".equals(type))
        {
            return new StaxTestReader();
        }
        else if ("A".equals(type))
        {
            return new StaxActionReader();
        }
        else if ("W".equals(type))
        {
            return new StaxWrapperReader();
        }
        else
        {
            return null;
        }
    }
}
