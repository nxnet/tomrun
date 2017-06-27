package io.nxnet.tomrun.interpolation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpolator
{
    public static final String PROPERTIES_INTERPOLATION_PATTERN = "\\$\\{(.+?[^\\$])\\}";

    public static final String ATTRIBUTES_INTERPOLATION_PATTERN = "\\#\\{(.+?[^\\$])\\}";

    private final Pattern interpolationPatternPatern;

    public Interpolator(String interpolationPattern)
    {
        this.interpolationPatternPatern = Pattern.compile(interpolationPattern);
    }

    public String interpolate(String s, ReplacementStrategy strategy)
    {
        if (s == null)
        {
            return null;
        }

        StringBuffer interpolatedStringBuffer = new StringBuffer();
        Matcher interpolationPatternMatcher = this.interpolationPatternPatern.matcher(s);
        String interpolationPatternMatch = null;
        String interpolationPatternReplacement = null;
        while (interpolationPatternMatcher.find())
        {
            interpolationPatternMatch = interpolationPatternMatcher.group(1);
            interpolationPatternReplacement = strategy.replace(interpolationPatternMatch);
            if (interpolationPatternReplacement != null) 
            {
                interpolationPatternMatcher.appendReplacement(interpolatedStringBuffer, 
                        Matcher.quoteReplacement(interpolationPatternReplacement));
            }
        }
        
        interpolationPatternMatcher.appendTail(interpolatedStringBuffer);
        return interpolatedStringBuffer.toString();
    }

    public List<String> interpolationTokens(String s)
    {
        final List<String> interpolationTokens = new ArrayList<String>();
        this.interpolate(s, new ReplacementStrategy() {
            
            @Override
            public String replace(String s)
            {
                interpolationTokens.add(s);
                return s;
            }
        });
        return interpolationTokens;
    }
}
