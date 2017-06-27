package io.nxnet.tomrun.resolver.def;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.nxnet.tomrun.resolver.ValueResolver;
import io.nxnet.tomrun.resolver.ValueResolverException;

/**
 * Created by IntelliJ IDEA.
 * User: enikruz
 * Date: Apr 4, 2011
 * Time: 1:59:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateResolver implements ValueResolver {

    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd'T'HHmmss.SSSZ";

    public static final int DEFAULT_DAY_OFFSET = 0;

    private String dateFormat;

    private DateFormat formatter;

    private int dayOffset;

    private TimeZone timeZone;

    private Locale locale;

    private Date resolvedDate;

    ///////////////////////////////// Public Constructors /////////////////////////////////////////////////

    /**
     * <p>
     * Default CEN message date resolver with zero offset in days, i.e. resolved date
     * will be system date and default date format {@link #DEFAULT_DATE_FORMAT}.
     * </p>
     */
    public DateResolver() {
        this(DEFAULT_DATE_FORMAT, DEFAULT_DAY_OFFSET);
    }

    public DateResolver(String dateFormat) {
        this(dateFormat, DEFAULT_DAY_OFFSET);
    }

    public DateResolver(int dayOffset) {
        this(DEFAULT_DATE_FORMAT, dayOffset);
    }

    public DateResolver(int dayOffset, TimeZone timeZone) {
        this(DEFAULT_DATE_FORMAT, dayOffset, timeZone);
    }

    public DateResolver(int dayOffset, Locale locale) {
        this(DEFAULT_DATE_FORMAT, dayOffset, locale);
    }

    public DateResolver(String dateFormat, Integer dayOffset) {
        this(dateFormat, dayOffset, TimeZone.getDefault(), Locale.getDefault());
    }

    public DateResolver(String dateFormat, Integer dayOffset, TimeZone timeZone) {
        this(dateFormat, dayOffset, timeZone, Locale.getDefault());
    }

    public DateResolver(String dateFormat, Integer dayOffset, Locale locale) {
        this(dateFormat, dayOffset, TimeZone.getDefault(), locale);
    }

    /**
     * <p>
     * CEN message date resolver with default date format {@link #DEFAULT_DATE_FORMAT} and
     * custom offset in days, custom time zone and locale.
     * </p>
     *
     * @param dayOffset
     * @param timeZone
     * @param locale
     */
    public DateResolver(Integer dayOffset, TimeZone timeZone, Locale locale) {
        this(DEFAULT_DATE_FORMAT, dayOffset, timeZone, locale);
    }

    /**
     * <p>
     * CEN message date resolver with custom date format, offset in days, time zone and locale.
     * </p>
     *
     * @param dateFormat
     * @param dayOffset
     * @param timeZone
     * @param locale
     */
    public DateResolver(String dateFormat, Integer dayOffset, TimeZone timeZone, Locale locale) {

        this.dateFormat = dateFormat;

        // Initialize callendar values
        this.dayOffset = dayOffset;
        this.timeZone = timeZone;
        this.locale = locale;
    }

    ///////////////////////////////// End of Public Constructors /////////////////////////////////////////////////

    /**
     * <p>
     * Getter for date format. </br>
     * See {@link #setDateFormat(String)} for details on date format.
     * </p>
     *
     * @return
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * <p>
     * Setter for date format which will be used by this resolver to convert date to string.
     * </p>
     *
     * @param dateFormat
     */
    public void setDateFormat(String dateFormat) {

        // Validate arguments
        if (dateFormat == null) {
            throw new IllegalArgumentException("date format can't be null");
        }

        // Set date format string.
        this.dateFormat = dateFormat;

        // Post prosess clean up.
        // This is needed because formatter is cached for better performance.
        destroyFormatter();
    }

    public int getDayOffset() {
        return dayOffset;
    }

    public void setDayOffset(int dayOffset) {
        this.dayOffset = dayOffset;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        if (timeZone == null) {
            throw new IllegalArgumentException("time zone can't be null");
        }
        this.timeZone = timeZone;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("locale can't be null");
        }
        this.locale = locale;
    }

    public Date getResolvedDate() {
        return resolvedDate;
    }

    /**
     * <p>
     * Converts system date with desired day offset to string.
     * </p>
     *
     * @return
     * @throws hr.nixanet.tomrun.lang.ResolveException
     */
    public String resolve() throws ValueResolverException {

        // Initialize formatter only once to gain some performance boost.
        if(formatter == null) {
            initFormatter();
        }

        // Do date format.
        Calendar calendar = Calendar.getInstance(timeZone, locale);
        calendar.add(Calendar.DAY_OF_YEAR, dayOffset);
        resolvedDate = calendar.getTime();
        return formatter.format(resolvedDate);
    }

    ///////////////////////////////// Private Methods /////////////////////////////////////////////

    /**
     * <p>
     * Initialize date formatter with default pattern or custom pattern defined previouslly with
     * {@link #setDateFormat(String)} method.
     * </p>
     *
     * @return
     */
    private DateFormat initFormatter() {
        DateFormat old = formatter;
        formatter = new SimpleDateFormat(dateFormat);
        return old;
    }

    /**
     * <p>
     * Sets {@link #formatter} property to <code>null</code>.</br>
     * The one will need to initialize formatter aggain in order to use it,
     * preferablly with {@link #initFormatter()} method.
     * </p>
     *
     * @return
     */
    private DateFormat destroyFormatter() {
        DateFormat old = formatter;
        formatter = null;
        return old;
    }

    ///////////////////////////////// End of Private Methods /////////////////////////////////////////////

}
