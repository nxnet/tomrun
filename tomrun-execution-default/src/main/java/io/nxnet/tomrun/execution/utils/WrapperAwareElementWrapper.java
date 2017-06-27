package io.nxnet.tomrun.execution.utils;

import io.nxnet.tomrun.model.WrapperAwareElement;

public class WrapperAwareElementWrapper
{
    private WrapperAwareElement wrapperAwareElement;
    
    public WrapperAwareElementWrapper(WrapperAwareElement wrapperAwareElement)
    {
        this.wrapperAwareElement = wrapperAwareElement;
    }
    
    public String getPath()
    {
        return this._getPathRecursively(this.wrapperAwareElement);
    }

    private String _getPathRecursively(WrapperAwareElement nodeElement)
    {
        if (nodeElement == null || nodeElement.getType().equals("P"))
        {
            return "";
        }
        return _getPathRecursively(nodeElement.getParent()) + "/" + nodeElement.getType().toLowerCase()
                + "_" + nodeElement.getId().toLowerCase();
    }

}
