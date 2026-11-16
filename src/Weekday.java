/**
 * The days of the week, used for the days a health professional works.
 *
 * <p>An {@code enum} is the right choice here because the set of days is fixed
 * and known at compile time: the compiler rejects any value that is not one of
 * these seven, so a working roster can never hold an impossible day. Each
 * constant also carries a short label, which keeps the printed output
 * readable.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public enum Weekday {

    /** Monday. */
    MONDAY("Mon"),

    /** Tuesday. */
    TUESDAY("Tue"),

    /** Wednesday. */
    WEDNESDAY("Wed"),

    /** Thursday. */
    THURSDAY("Thu"),

    /** Friday. */
    FRIDAY("Fri"),

    /** Saturday. */
    SATURDAY("Sat"),

    /** Sunday. */
    SUNDAY("Sun");

    /** The three letter label used when a roster is printed. */
    private final String shortName;

    /**
     * Creates a day of the week with its short label.
     *
     * @param shortName the three letter label of the day
     */
    Weekday(String shortName) {
        this.shortName = shortName;
    }

    /**
     * Returns the three letter label of this day.
     *
     * @return the short label, for example "Wed"
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Returns the name of the day with a capital first letter.
     *
     * @return the name of the day, for example "Wednesday"
     */
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
