package io.nxnet.tomrun.resolver.def;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.nxnet.tomrun.resolver.ValueResolver;

/**
 * Created by IntelliJ IDEA.
 * User: enikruz
 * Date: Mar 28, 2011
 * Time: 10:11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class IdResolver implements ValueResolver {

    public static String DEFAULT_PATTERN = "%tyyyyMMddHHmmssSSS%-[a-zA-Z0-9]{8}";

    public static String DEFAULT_PARAMETER_PATTERN = "(?<!\\\\)\\%([0-9])\\%";

    public static String DEFAULT_UUID_PATTERN = "(?<!\\\\)\\%u\\%";

    public static String DEFAULT_TIMESTAMP_PATTERN = "(?<!\\\\)\\%t(.*)(?<!\\\\)\\%";

    public static String DEFAULT_ALPHANUMERIC_CLASS_PATTERN = "(\\[((a-z){1}(A-Z)?(0-9)?|(a-z)?(A-Z){1}(0-9)?|(a-z)?(A-Z)?(0-9){1})\\])(\\{([0-9]+|[0-9]+\\,[0-9]+)\\})";

    public static String DEFAULT_TOKEN_PATTERN = "(" + DEFAULT_PARAMETER_PATTERN + ")" + "|"
            + "(" + DEFAULT_TIMESTAMP_PATTERN + ")" + "|"
            + "(" + DEFAULT_ALPHANUMERIC_CLASS_PATTERN + ")" + "|"
            + "(" + DEFAULT_UUID_PATTERN + ")";

    public static int GROUP_INDEX_PARAM = 2;

    public static int GROUP_INDEX_DATE_FORMAT = 4;

    public static int[] GROUP_INDEXES_ALPHANUMERIC_FORMATS = {8,9,10,11,12,13,14,15,16};

    public static int GROUP_INDEX_ALPHANUMERIC_REPETITION = 18;

    public static String ALPHANUMERIC_FORMAT_AZ_LOWERCASE = "a-z";

    public static String ALPHANUMERIC_FORMAT_AZ_UPPERCASE = "A-Z";

    public static String ALPHANUMERIC_FORMAT_NUMERIC = "0-9";


    private String pattern;

    private String[] params;

    public IdResolver() {
        this(DEFAULT_PATTERN, new String[] {});
    }

    public IdResolver(Class c) {
        this("%0%-" + DEFAULT_PATTERN, new String[] {c.getCanonicalName()});
    }

    public IdResolver(String pattern) {
        this(pattern, new String[] {});
    }

    public IdResolver(String[] params) {
        this(DEFAULT_PATTERN, params);
    }

    public IdResolver(String pattern, String[] params) {
        this.pattern = (pattern == null ? DEFAULT_PATTERN : pattern);
        this.params = (params == null ? new String[] {} : params);
    }

    public String resolve() {
        return resolveInternal();
    }

    public String getPattern() {
        return pattern;
    }

    /////////////////////////////////////////// Helper Methods /////////////////////////////////////////////

    protected String createRandomToken(int length, TokenType tokenType, LetterCase letterCase) {

        if (length < 1) {
            throw new IllegalArgumentException("length of random token must be greather then zero");
        }
        if (tokenType == null) {
            throw new IllegalArgumentException("token type can't be null");
        }
        if (letterCase == null) {
            throw new IllegalArgumentException("letter case can't be null");
        }

        StringBuilder sb = new StringBuilder();
        String chunk = null;
        while (sb.length() < (length+1)) {

            chunk = Long.toHexString(Double.doubleToLongBits(Math.random()));

            switch (tokenType) {

                case NUMERIC_ONLY:
                    chunk = chunk.replaceAll("[a-zA-Z]", "");
                    break;
                case LETTERS_ONLY:
                    chunk = chunk.replaceAll("[0-9]", "");
                    break;
                case ALPHANUMERIC:
                default:
                    // do nothing
            }

            switch (letterCase) {

                case UPPERCASE:
                    chunk = chunk.toUpperCase();
                    break;
                case LOWERCASE:
                    chunk = chunk.toLowerCase();
                    break;
                case MIX:
                default:
                    // do nothing
            }

            sb.append(chunk);
        }

        return sb.toString().substring(0,length);
    }

    protected String resolveInternal() {

        Pattern parameterPattern = Pattern.compile(DEFAULT_PARAMETER_PATTERN);
        Pattern timestampPattern = Pattern.compile(DEFAULT_TIMESTAMP_PATTERN);
        Pattern uuidPattern = Pattern.compile(DEFAULT_UUID_PATTERN);
        Pattern alphanumericPattern = Pattern.compile(DEFAULT_ALPHANUMERIC_CLASS_PATTERN);
        Matcher patternMatcher = null;

        Pattern tokenPattern = Pattern.compile(DEFAULT_TOKEN_PATTERN);
        Matcher tokenMatcher = tokenPattern.matcher(this.pattern);
        String replacement = this.pattern;
        String token = null;
        while (tokenMatcher.find()) {

            token = tokenMatcher.group();

            patternMatcher = parameterPattern.matcher(token);
            if (patternMatcher.matches()) {

                replacement = tokenMatcher.replaceFirst(params[Integer.parseInt(tokenMatcher.group(GROUP_INDEX_PARAM))]);
                tokenMatcher = tokenPattern.matcher(replacement);
                continue;
            }

            patternMatcher = timestampPattern.matcher(token);
            if (patternMatcher.matches()) {

                replacement = tokenMatcher.replaceFirst(new SimpleDateFormat(
                        tokenMatcher.group(GROUP_INDEX_DATE_FORMAT)).format(new Date()));
                tokenMatcher = tokenPattern.matcher(replacement);
                continue;
            }

            patternMatcher = uuidPattern.matcher(token);
            if (patternMatcher.matches()) {

                replacement = tokenMatcher.replaceFirst(UUID.randomUUID().toString());
                tokenMatcher = tokenPattern.matcher(replacement);
                continue;
            }

            patternMatcher = alphanumericPattern.matcher(token);
            if (patternMatcher.matches()) {

                Set<String> alphanumericFormats = new HashSet<String>();
                for (int groupIndexAlphanumericFormat : GROUP_INDEXES_ALPHANUMERIC_FORMATS) {
                	
                	String alphanumericFormat = tokenMatcher.group(groupIndexAlphanumericFormat);
                	if (alphanumericFormat != null) {
                		alphanumericFormats.add(alphanumericFormat);
                	}
                }

                String randomToken = null;
                String repetitionString = tokenMatcher.group(GROUP_INDEX_ALPHANUMERIC_REPETITION);
                Integer repetition = null;
                if (repetitionString.contains(",")) {
                	
                	String[] repetitionTokens = repetitionString.split(",");
                	Integer min = Integer.parseInt(repetitionTokens[0]);
                	Integer max = Integer.parseInt(repetitionTokens[1]);
                	
                	repetition = min + (int)(Math.random() * ((max - min) + 1));
                	
                } else {
                	
                	repetition = Integer.parseInt(repetitionString);
                }
                
                if (alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_LOWERCASE)
                        && alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_UPPERCASE)
                        && alphanumericFormats.contains(ALPHANUMERIC_FORMAT_NUMERIC)) {

                    randomToken = createRandomToken(repetition, TokenType.ALPHANUMERIC, LetterCase.MIX);

                } else if (alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_LOWERCASE)
                        && alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_UPPERCASE)
                        && !alphanumericFormats.contains(ALPHANUMERIC_FORMAT_NUMERIC)) {

                    randomToken = createRandomToken(repetition, TokenType.LETTERS_ONLY, LetterCase.MIX);

                } else if (alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_LOWERCASE)
                        && !alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_UPPERCASE)
                        && alphanumericFormats.contains(ALPHANUMERIC_FORMAT_NUMERIC)) {

                    randomToken = createRandomToken(repetition, TokenType.ALPHANUMERIC, LetterCase.LOWERCASE);

                } else if (alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_LOWERCASE)
                        && !alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_UPPERCASE)
                        && !alphanumericFormats.contains(ALPHANUMERIC_FORMAT_NUMERIC)) {

                    randomToken = createRandomToken(repetition, TokenType.LETTERS_ONLY, LetterCase.LOWERCASE);

                } else if (!alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_LOWERCASE)
                        && alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_UPPERCASE)
                        && alphanumericFormats.contains(ALPHANUMERIC_FORMAT_NUMERIC)) {

                    randomToken = createRandomToken(repetition, TokenType.ALPHANUMERIC, LetterCase.UPPERCASE);

                } else if (!alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_LOWERCASE)
                        && alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_UPPERCASE)
                        && !alphanumericFormats.contains(ALPHANUMERIC_FORMAT_NUMERIC)) {

                    randomToken = createRandomToken(repetition, TokenType.LETTERS_ONLY, LetterCase.UPPERCASE);

                } else if (!alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_LOWERCASE)
                        && !alphanumericFormats.contains(ALPHANUMERIC_FORMAT_AZ_UPPERCASE)
                        && alphanumericFormats.contains(ALPHANUMERIC_FORMAT_NUMERIC)) {

                    randomToken = createRandomToken(repetition, TokenType.NUMERIC_ONLY, LetterCase.MIX);

                } else {

                    throw new IllegalStateException("Unknown combination of alphanumeric formats" + alphanumericFormats);
                }

                replacement = tokenMatcher.replaceFirst(randomToken);
                tokenMatcher = tokenPattern.matcher(replacement);
                continue;
            }

            throw new IllegalStateException("Unknown token " + token);
        }

        return replacement;
    }

    protected enum TokenType {
        NUMERIC_ONLY,
        LETTERS_ONLY,
        ALPHANUMERIC
    }

    protected enum LetterCase {
        UPPERCASE,
        LOWERCASE,
        MIX
    }

}
