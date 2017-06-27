package io.nxnet.tomrun.resolver.def;

import java.util.Date;
import java.util.Random;

import io.nxnet.tomrun.resolver.ValueResolver;

/**
 * Created by IntelliJ IDEA.
 * User: ebrapav
 * Date: 13.07.11.
 * Time: 12:37
 * To change this template use File | Settings | File Templates.
 */
public class CuidResolver implements ValueResolver {

    public static String DEFAULT_UCAID = "UCAID";

    public static String DEFAULT_TOKEN_SEPARATOR = "/";

    private String ucaid;

    private String tokenSeparator;

    public CuidResolver() {
        this(DEFAULT_UCAID);
    }

    public CuidResolver(Class c) {
        this(c != null ? c.getCanonicalName() : null);
    }

    public CuidResolver(String ucaid) {
        init(ucaid, tokenSeparator);
    }

     public CuidResolver(String ucaid, String tokenSeparator) {
        init(ucaid, tokenSeparator);
    }

    public String resolve() {
        Date d = new Date();
        String time = Long.toHexString(d.getTime()).toUpperCase();
        if(time.length()<12){
            int n= 12-time.length();
            for (int i = 0; i<n; i++){
                time = "0"+time;
            }
        }
        String random = createRandomString(8);

        if (ucaid != null && ucaid.trim().length() > 0) { // Prefix is defined
            // Create id with ucaid
            return new StringBuilder(ucaid.toUpperCase()).append(tokenSeparator)
                    .append(time).append(random).toString();
        } else {

            // Create id without ucaid
            return new StringBuilder("UCAID").append(tokenSeparator)
                    .append(time).append(random).toString();
        }
    }

    public String getUcaid() {
        return ucaid;
    }

    /**
     * <p>
     * UCAID can be max 55 char</br>
     * Default value is {@link this#DEFAULT_UCAID}.
     * </p>
     *
     * @param ucaid
     */
    public void setUcaid(String ucaid) {
        if (ucaid!=null && ucaid.length()>55){
            ucaid = ucaid.substring(0,56);
        }
        this.ucaid = ucaid;
    }

     public String getTokenSeparator() {
        return tokenSeparator;
    }

    /**
     * <p>
     * Set string which will separate tokens of generated id.</br>
     * Default value is {@link this#DEFAULT_TOKEN_SEPARATOR}.
     * </p>
     *
     * @param tokenSeparator
     */
    public void setTokenSeparator(String tokenSeparator) {
        this.tokenSeparator = tokenSeparator;
    }

    /////////////////////////////////////////// Private Methods /////////////////////////////////////////////

    /**
     * <p>
     * Default object initialization logic.
     * </p>
     *
     * @param ucaid to be used by <code>this</code> object internal logic
     *        or {@link this#DEFAULT_UCAID} if <code>null</code> is passed.

     */
    private void init(String ucaid, String tokenSeparator) {
        if(ucaid == null) {
            setUcaid(DEFAULT_UCAID);
        } else {
            setUcaid(ucaid);
        }
        if(tokenSeparator == null) {
            setTokenSeparator(DEFAULT_TOKEN_SEPARATOR);
        } else {
            setTokenSeparator(tokenSeparator);
        }
    }

    private static String createRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < (length+1)) {
            sb.append(Integer.toHexString(random.nextInt()).toUpperCase());
        }
        return sb.toString().substring(0,length);
    }

}

