package io.nxnet.tomrun.execution.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import io.nxnet.tomrun.context.OwnableContext;
import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeEvent;
import io.nxnet.tomrun.execution.ExecutionNodeEventName;
import io.nxnet.tomrun.execution.ExecutionNodeType;

public class ExecutionNodeImpl extends Observable implements ExecutionNode
{
    protected String id;

    protected String name;

    protected String description;

    protected ExecutionNodeType type;

    protected OwnableContext context;

    protected ExecutionNode parent;

    protected List<ExecutionNode> children;

    protected ExecutionNode before;

    protected ExecutionNode after;

    protected ExecutionNode beforeChild;

    protected ExecutionNode afterChild;

    protected List<ExecutionNode> expandedChildList;

    protected boolean expandedChildListObsolete;

    public ExecutionNodeImpl(String id, String name, String description, ExecutionNodeType type)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Execution node element id is null");
        }
        
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.children = new ArrayList<ExecutionNode>();
        this.expandedChildListObsolete = true;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    public ExecutionNodeType getType()
    {
        return type;
    }

    @Override
    public int getLevel()
    {
        int level = 0;
        ExecutionNode parent = this.parent;
        while (parent != null)
        {
            level++;
            parent = parent.getParent();
        }

        return level;
    }

    @Override
    public boolean isLast()
    {
        if (parent == null)
        {
            return true;
        }

        List<ExecutionNode> expandedSiblings = this.parent.getExpandedChildren();
        return this.equals(expandedSiblings.get(expandedSiblings.size() - 1));
//        
//        Iterator<ExecutionNode> parentIter = parent.getIter();
//        ExecutionNode iteratedNode = null;
//        while (parentIter.hasNext())
//        {
//            iteratedNode = parentIter.next();
//            if (this.equals(iteratedNode))
//            {
//                break;
//            }
//        }
//        
//        return !parentIter.hasNext();
    }

    @Override
    public boolean isRoot()
    {
        return parent == null;
    }

    @Override
    public boolean isLeaf()
    {
        return children == null || children.isEmpty();
    }

    @Override
    public boolean isChild()
    {
        return !isRoot();
    }

    @Override
    public boolean isParent()
    {
        return !isLeaf();
    }

    @Override
    public boolean isLeftChild()
    {
        if (isChildOf(this.parent))
        {
            return this.equals(this.parent.getBefore()) || this.equals(this.parent.getBeforeChild());
        }
        return false;
    }

    @Override
    public boolean isMiddleChild()
    {
        if (isChildOf(this.parent))
        {
            return this.parent.getChildren().contains(this);
        }
        return false;
    }

    @Override
    public boolean isRightChild()
    {
        if (isChildOf(this.parent))
        {
            return this.equals(this.parent.getAfter()) || this.equals(this.parent.getAfterChild());
        }
        return false;
    }

    @Override
    public boolean isChildOf(ExecutionNode l)
    {
        if (this.parent != null)
        {
            return this.parent.equals(l);
        }

        return false;
    }

    @Override
    public boolean isParentOf(ExecutionNode l)
    {
        boolean isParent = false;
        if (l != null && this.children != null)
        {
            isParent = this.children.contains(l);
            if (isParent == false)
            {
                isParent = l.equals(this.getBefore()) || l.equals(this.getBeforeChild()) || l.equals(this.getAfter())
                        || l.equals(this.getAfterChild());
            }
        }

        return isParent;
    }

    @Override
    public boolean isLeftChildOf(ExecutionNode l)
    {
        if (isChildOf(l))
        {
            return this.equals(l.getBefore()) || this.equals(l.getBeforeChild());
        }
        return false;
    }

    @Override
    public boolean isMiddleChildOf(ExecutionNode l)
    {
        if (isChildOf(l))
        {
            return l.getChildren().contains(this);
        }
        return false;
    }

    @Override
    public boolean isRightChildOf(ExecutionNode l)
    {
        if (isChildOf(l))
        {
            return this.equals(l.getAfter()) || this.equals(l.getAfterChild());
        }
        return false;
    }

    @Override
    public ExecutionNode getLeftSibling()
    {
        if (this.parent != null)
        {
            List<ExecutionNode> siblingCandidates = this.parent.getChildren();
            if (siblingCandidates != null)
            {
                int leftSiblingPosition = siblingCandidates.indexOf(this) - 1;
                if (leftSiblingPosition >= 0)
                {
                    return siblingCandidates.get(leftSiblingPosition);
                }
            }
        }

        return null;
    }

    @Override
    public ExecutionNode getRightSibling()
    {
        if (this.parent != null)
        {
            List<ExecutionNode> siblingCandidates = this.parent.getChildren();
            if (siblingCandidates != null)
            {
                int rightSiblingPosition = siblingCandidates.indexOf(this) + 1;
                if (rightSiblingPosition < siblingCandidates.size())
                {
                    return siblingCandidates.get(rightSiblingPosition);
                }
            }
        }

        return null;
    }

    @Override
    public ExecutionNode getLeftExpandedSibling()
    {
        if (this.parent != null)
        {
            List<ExecutionNode> siblingCandidates = this.parent.getExpandedChildren();
            if (siblingCandidates != null)
            {
                int leftSiblingPosition = siblingCandidates.indexOf(this) - 1;
                if (leftSiblingPosition >= 0)
                {
                    return siblingCandidates.get(leftSiblingPosition);
                }
            }
        }

        return null;
    }

    @Override
    public ExecutionNode getRightExpandedSibling()
    {
        if (this.parent != null)
        {
            List<ExecutionNode> siblingCandidates = this.parent.getExpandedChildren();
            if (siblingCandidates != null)
            {
                int rightSiblingPosition = siblingCandidates.indexOf(this) + 1;
                if (rightSiblingPosition < siblingCandidates.size())
                {
                    return siblingCandidates.get(rightSiblingPosition);
                }
            }
        }

        return null;
    }

    @Override
    public OwnableContext getContext()
    {
        return context;
    }

    @Override
    public void setContext(OwnableContext context)
    {
        this.context = context;
        context.setOwner(this);
    }

    @Override
    public void setParent(ExecutionNode p)
    {
        this.parent = p;
    }

    @Override
    public ExecutionNode getParent()
    {
        return this.parent;
    }

    @Override
    public void setBefore(ExecutionNode n)
    {
        this.expandedChildListObsolete = true;
        this.before = n;
    }

    @Override
    public ExecutionNode getBefore()
    {
        return this.before;
    }

    @Override
    public void setAfter(ExecutionNode n)
    {
        this.expandedChildListObsolete = true;
        this.after = n;
    }

    @Override
    public ExecutionNode getAfter()
    {
        return this.after;
    }

    @Override
    public void setBeforeChild(ExecutionNode n)
    {
        this.expandedChildListObsolete = true;
        this.beforeChild = n;
    }

    @Override
    public ExecutionNode getBeforeChild()
    {
        return this.beforeChild;
    }

    @Override
    public void setAfterChild(ExecutionNode n)
    {
        this.expandedChildListObsolete = true;
        this.afterChild = n;
    }

    @Override
    public ExecutionNode getAfterChild()
    {
        return this.afterChild;
    }

    @Override
    public void setChildren(List<ExecutionNode> children)
    {
        this.expandedChildListObsolete = true;
        this.children = children;
    }

    @Override
    public void addChild(ExecutionNode n)
    {
        this.expandedChildListObsolete = true;
        if (this.children == null)
        {
            this.children = new ArrayList<ExecutionNode>();
        }
        this.children.add(n);
    }

    @Override
    public List<ExecutionNode> getChildren()
    {
        return this.children;
    }

    @Override
    public Iterator<ExecutionNode> getIter()
    {
        // Make temporary flat collection
        List<ExecutionNode> tmpList = new ArrayList<ExecutionNode>();

        // Add this node
        tmpList.add(this);

        // Add all children
        tmpList.addAll(this.getExpandedChildren());

        // Get iterator for such flat collection
        return tmpList.iterator();
    }

    @Override
    public synchronized void addObserver(Observer o)
    {
        super.addObserver(o);
        
        if (before != null)
        {
            before.addObserver(o);
        }

        if (beforeChild != null)
        {
            beforeChild.addObserver(o);
        }
        
        if (children != null)
        {
            for (ExecutionNode child : this.children)
            {
                if (child != null)
                {
                    child.addObserver(o);
                }
            }
        }
        
        if (afterChild != null)
        {
            afterChild.addObserver(o);
        }
        
        if (after != null)
        {
            after.addObserver(o);
        }
    }

    @Override
    public void setUp()
    {
        // do nothing
    }

    @Override
    public void tearDown()
    {
        // do nothing
    }

    @Override
    public void doExec()
    {
        try
        {
            // Notify observers
            setChanged();
            notifyObservers(new ExecutionNodeEvent.Builder()
                    .eventName(ExecutionNodeEventName.EXEC_START)
                    .build());

            if (before != null)
            {
                before.doExec();
            }

            if (children != null)
            {
                for (ExecutionNode child : children)
                {
                    if (child != null)
                    {
                        try
                        {
                            if (beforeChild != null)
                            {
                                beforeChild.doExec();
                            }

                            child.doExec();
                        }
                        finally
                        {
                            if (afterChild != null)
                            {
                                afterChild.doExec();
                            }
                        }
                    }
                }
            }
        }
        finally
        {
            if (after != null)
            {
                after.doExec();
            }

            // Notify observers
            setChanged();
            notifyObservers(new ExecutionNodeEvent.Builder()
                    .eventName(ExecutionNodeEventName.EXEC_END)
                    .build());
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n   class: ").append(getClass().getName()).append("\n   ");
        if (id != null)
        {
            builder.append("id: ").append(id).append("\n   ");
        }
        if (name != null)
        {
            builder.append("name: ").append(name).append("\n   ");
        }
        if (description != null)
        {
            builder.append("description: ").append(description).append("\n   ");
        }
        if (type != null)
        {
            builder.append("type: ").append(type).append("\n   ");
        }
        if (context != null)
        {
            builder.append("context: ").append(context.toString().replaceAll("(.*)", "   $1").trim()).append("\n   ");
        }
        if (parent != null)
        {
            builder.append("parent: ").append("...").append("\n   ");
        }
        if (before != null)
        {
            builder.append("before: ").append(before.toString().replaceAll("(.*)", "   $1").trim()).append("\n   ");
        }
        if (beforeChild != null)
        {
            builder.append("beforeChild: ").append(beforeChild.toString().replaceAll("(.*)", "   $1").trim()).append("\n   ");
        }
        if (children != null)
        {
            builder.append("children: ").append(children.toString().replaceAll("(.*)", "   $1").trim()).append("\n   ");
        }
        if (afterChild != null)
        {
            builder.append("afterChild: ").append(afterChild.toString().replaceAll("(.*)", "   $1").trim()).append("\n   ");
        }
        if (after != null)
        {
            builder.append("after: ").append(after.toString().replaceAll("(.*)", "   $1").trim());
        }
        builder.append("\n}");
        return builder.toString();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!ExecutionNodeImpl.class.isAssignableFrom(obj.getClass()))
            return false;
        ExecutionNodeImpl other = (ExecutionNodeImpl) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (type == null)
        {
            if (other.type != null)
                return false;
        }
        else if (!type.equals(other.type))
            return false;
        return true;
    }

    synchronized void initExpandedChildList()
    {
        if (this.expandedChildListObsolete)
        {
            // Make temporary flat collection
            List<ExecutionNode> tmpList = new ArrayList<ExecutionNode>();
    
            // Add before node
            if (before != null)
            {
                tmpList.add(before);
            }
    
            // Add child nodes
            if (children != null && !children.isEmpty())
            {
                for (ExecutionNode child : children)
                {
                    // Add before each child node
                    if (beforeChild != null)
                    {
                        tmpList.add(beforeChild);
                    }
    
                    // Add child node itself
                    tmpList.add(child);
    
                    // Add after each child node
                    if (afterChild != null)
                    {
                        tmpList.add(afterChild);
                    }
                }
            }
            else // No children but nevertheless...
            {
                // Add before each child node
                if (beforeChild != null)
                {
                    tmpList.add(beforeChild);
                }
    
                // Add after each child node
                if (afterChild != null)
                {
                    tmpList.add(afterChild);
                }
            }
    
            // Add after node
            if (after != null)
            {
                tmpList.add(after);
            }
    
            // Get iterator for such flat collection
            this.expandedChildList = tmpList;
            this.expandedChildListObsolete = false;
        }
    }

    @Override
    public List<ExecutionNode> getExpandedChildren()
    {
        if (this.expandedChildListObsolete)
        {
            this.initExpandedChildList();
        }
        return this.expandedChildList;
    }

}
