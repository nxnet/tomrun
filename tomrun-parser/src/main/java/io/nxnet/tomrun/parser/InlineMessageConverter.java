package io.nxnet.tomrun.parser;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class InlineMessageConverter extends MessageConverter {

	@Override
	public String convert(ILoggingEvent event) {
		
		if (Level.DEBUG.equals(event.getLevel())) {
			
	       return doConvert(super.convert(event)); 
		}
				
		return super.convert(event);
	}

	protected String doConvert(String input)
	{
		StringBuffer replacement = new StringBuffer();
		String[] lines = input.split("\n", -1);
		for (int i = 0; i < lines.length;)
		{
			replacement.append("            |   ").append(lines[i++]);
			if (i < lines.length)
			{
				replacement.append("\n");
			}
		}
        
        return replacement.toString();
	}
}
