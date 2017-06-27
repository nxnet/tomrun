package io.nxnet.tomrun.execution.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observer;

import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeType;

public class ExecutionNodeList extends ExecutionNodeImpl implements List<ExecutionNode>
{
    protected List<ExecutionNode> internalList;

    public ExecutionNodeList(String id, String name, String description, ExecutionNodeType type)
    {
        super(id, name, description, type);
        this.internalList = new ArrayList<ExecutionNode>();
    }

    public int size()
    {
        return internalList.size();
    }

    public boolean isEmpty()
    {
        return internalList.isEmpty();
    }

    public boolean contains(Object o)
    {
        return internalList.contains(o);
    }

    public Iterator<ExecutionNode> iterator()
    {
        return internalList.iterator();
    }

    public Object[] toArray()
    {
        return internalList.toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return internalList.toArray(a);
    }

    public boolean add(ExecutionNode e)
    {
        return internalList.add(e);
    }

    public boolean remove(Object o)
    {
        return internalList.remove(o);
    }

    public boolean containsAll(Collection<?> c)
    {
        return internalList.containsAll(c);
    }

    public boolean addAll(Collection<? extends ExecutionNode> c)
    {
        return internalList.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends ExecutionNode> c)
    {
        return internalList.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c)
    {
        return internalList.removeAll(c);
    }

    public boolean retainAll(Collection<?> c)
    {
        return internalList.retainAll(c);
    }

    public void clear()
    {
        internalList.clear();
    }

    public boolean equals(Object o)
    {
        return internalList.equals(o);
    }

    public int hashCode()
    {
        return internalList.hashCode();
    }

    public ExecutionNode get(int index)
    {
        return internalList.get(index);
    }

    public ExecutionNode set(int index, ExecutionNode element)
    {
        return internalList.set(index, element);
    }

    public void add(int index, ExecutionNode element)
    {
        internalList.add(index, element);
    }

    public ExecutionNode remove(int index)
    {
        return internalList.remove(index);
    }

    public int indexOf(Object o)
    {
        return internalList.indexOf(o);
    }

    public int lastIndexOf(Object o)
    {
        return internalList.lastIndexOf(o);
    }

    public ListIterator<ExecutionNode> listIterator()
    {
        return internalList.listIterator();
    }

    public ListIterator<ExecutionNode> listIterator(int index)
    {
        return internalList.listIterator(index);
    }

    public List<ExecutionNode> subList(int fromIndex, int toIndex)
    {
        return internalList.subList(fromIndex, toIndex);
    }

    @Override
    public void doExec()
    {

        setChanged();
        notifyObservers("doExec");

        if (internalList != null)
        {
            for (ExecutionNode executionNode : internalList)
            {
                if (executionNode != null)
                {
                    try
                    {
                        if (before != null)
                        {
                            before.doExec();
                        }

                        executionNode.doExec();
                    }
                    finally
                    {
                        if (after != null)
                        {
                            after.doExec();
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return "\n" + new String(new char[getLevel()]).replace("\0", "\t") + "ExecutionNodeList [type=" + type
                + ", id=" + id + ", name=" + name + ", description=" + description + ",\n"
                + new String(new char[getLevel()]).replace("\0", "\t") + "before=" + before + ",\n"
                + new String(new char[getLevel()]).replace("\0", "\t") + "children="
                + Arrays.toString(internalList != null ? internalList.toArray() : null) + ",\n"
                + new String(new char[getLevel()]).replace("\0", "\t") + "after=" + after + "\n"
                + new String(new char[getLevel()]).replace("\0", "\t") + "]";
    }

    @Override
    public synchronized void addObserver(Observer o)
    {
        super.addObserver(o);
        
        if (before != null)
        {
            before.addObserver(o);
        }
        
        if (internalList != null)
        {
            for (ExecutionNode child : this.internalList)
            {
                if (child != null)
                {
                    child.addObserver(o);
                }
            }
        }
        
        if (after != null)
        {
            after.addObserver(o);
        }
    }

}
