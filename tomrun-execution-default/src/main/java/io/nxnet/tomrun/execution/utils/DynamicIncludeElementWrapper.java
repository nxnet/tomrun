package io.nxnet.tomrun.execution.utils;

import io.nxnet.tomrun.execution.DynamicIncludeNode;
import io.nxnet.tomrun.model.DynamicInclude;
import io.nxnet.tomrun.model.ResolvableAttribute;
import io.nxnet.tomrun.model.WrapperAwareElement;

public class DynamicIncludeElementWrapper
{
    private DynamicInclude dynamicIncludeElement;
    
    public DynamicIncludeElementWrapper(DynamicInclude dynamicIncludeElement)
    {
        this.dynamicIncludeElement = dynamicIncludeElement;
    }
    
    public DynamicIncludeNode toNode()
    {
        DynamicIncludeNode dynamicIncludeNode = new DynamicIncludeNode();
        
        ResolvableAttribute fileAttribute = this.dynamicIncludeElement.getFile();
        if (fileAttribute != null)
        {
            dynamicIncludeNode.setFile(fileAttribute);
        }
        
        WrapperAwareElement ownerElement = this.dynamicIncludeElement.getOwner();
        if (ownerElement != null)
        {
            dynamicIncludeNode.setId(ownerElement.getId());
            dynamicIncludeNode.setName(ownerElement.getName());
            dynamicIncludeNode.setDescription(ownerElement.getDescription());
            dynamicIncludeNode.setType(ownerElement.getType());
            dynamicIncludeNode.setPath(new WrapperAwareElementWrapper(ownerElement).getPath());
        }
        
        return dynamicIncludeNode;
    }

}
