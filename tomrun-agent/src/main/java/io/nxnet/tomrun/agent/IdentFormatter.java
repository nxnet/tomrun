package io.nxnet.tomrun.agent;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public class IdentFormatter {
	
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
		return StringUtils.join(identTokens.values(), delimiter);
	}
}
