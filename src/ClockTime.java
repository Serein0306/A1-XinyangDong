/**
 * A time of day, stored as an hour and a minute.
 *
 * <p>The time is kept as a <em>number</em> rather than as text on purpose. Text
 * such as "9:05" or "09:05" sorts badly once the hours reach double digits
 * ("11:00" would come before "9:00"), so a text field could not be trusted for
 * ordering. Two integers, and a computed number of minutes since midnight,
 * compare correctly and cannot get out of step with the printed form.</p>
 *
 * <p>The object is immutable and validates itself in the constructor, so an
 * invalid time such as 24:15 can never exist.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class ClockTime implements Comparable<ClockTime> {

    /** The number of minutes in one hour. */
    public static final int MINUTES_PER_HOUR = 60;

    /** The number of minutes in one day. */
    public static final int MINUTES_PER_DAY = 24 * MINUTES_PER_HOUR;

    /** The hour of the day, from 0 to 23. */
    private final int hour;

    /** The minute of the hour, from 0 to 59. */
    private final int minute;

    /**
     * Creates the time of day supplied.
     *
     * @param hour   the hour of the day, from 0 to 23
     * @param minute the minute of the hour, from 0 to 59
     * @throws IllegalArgumentException if the hour or the minute is out of range
     */
    public ClockTime(int hour, int minute) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("An hour must be between 0 and 23: " + hour);
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("A minute must be between 0 and 59: " + minute);
        }
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Returns the hour of this time.
     *
     * @return the hour, from 0 to 23
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the minute of this time.
     *
     * @return the minute, from 0 to 59
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Returns this time as a single number of minutes since midnight.
     *
     * <p>This is the value used for ordering and for adding a slot length, which
     * is why it is made available to the rest of the system.</p>
     *
     * @return the number of minutes since midnight
     */
    public int getMinutesSinceMidnight() {
        return hour * MINUTES_PER_HOUR + minute;
    }

    /**
     * Returns a new time, the given number of minutes after this one.
     *
     * @param minutes the number of minutes to add
     * @return the resulting time of day
     */
    public ClockTime plusMinutes(int minutes) {
        int total = getMinutesSinceMidnight() + minutes;
        return new ClockTime(total / MINUTES_PER_HOUR, total % MINUTES_PER_HOUR);
    }

    /**
     * Compares this time with another time, earliest first.
     *
     * @param other the time to compare with
     * @return a negative number if this time is earlier, zero if both are the
     *         same time, and a positive number if this time is later
     * @throws IllegalArgumentException if {@code other} is null
     */
    @Override
    public int compareTo(ClockTime other) {
        if (other == null) {
            throw new IllegalArgumentException("A time cannot be compared with null.");
        }
        return Integer.compare(getMinutesSinceMidnight(), other.getMinutesSinceMidnight());
    }

    /**
     * Checks whether this time is the same time of day as another object.
     *
     * @param other the object to compare with
     * @return true if both objects describe the same hour and minute
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ClockTime)) {
            return false;
        }
        ClockTime time = (ClockTime) other;
        return hour == time.hour && minute == time.minute;
    }

    /**
     * Returns a hash code that is consistent with {@link #equals(Object)}.
     *
     * @return the hash code of the number of minutes since midnight
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(getMinutesSinceMidnight());
    }

    /**
     * Returns the time in the form "08:30".
     *
     * @return the time of day, always two digits for the hour
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}
