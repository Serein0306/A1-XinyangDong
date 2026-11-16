import java.util.ArrayList;
import java.util.List;

/**
 * The fixed daily timetable of the clinic: a start time, an end time and a slot
 * length, from which the bookable slots are generated.
 *
 * <p>The timetable stores the <em>rule</em> (08:00 to 16:00 in 30 minute slots)
 * instead of a hand written list of sixteen times. The slots are built once in
 * the constructor from those three values, so the rest of the system only ever
 * asks the timetable whether a time is bookable. Changing the clinic to
 * 08:30-17:00 in 15 minute slots is then a one line change in
 * {@link AssignmentOne} and no booking code has to be touched.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class DailyTimetable {

    /** The time the clinic starts seeing patients. */
    private final ClockTime openingTime;

    /** The time the clinic stops seeing patients; the last slot starts before it. */
    private final ClockTime closingTime;

    /** The length of one appointment slot, in minutes. */
    private final int slotLengthInMinutes;

    /** Every bookable start time of the day, built from the rule above. */
    private final List<ClockTime> slots;

    /**
     * Creates the timetable from the three values that define it.
     *
     * @param openingTime        the time the clinic opens
     * @param closingTime        the time the clinic closes
     * @param slotLengthInMinutes the length of one slot, in minutes
     * @throws IllegalArgumentException if a value is missing, if the slot
     *         length is not positive, if the clinic closes before it opens, or
     *         if the opening period does not divide evenly into slots
     */
    public DailyTimetable(ClockTime openingTime, ClockTime closingTime, int slotLengthInMinutes) {
        if (openingTime == null || closingTime == null) {
            throw new IllegalArgumentException("A timetable needs an opening and a closing time.");
        }
        if (slotLengthInMinutes <= 0) {
            throw new IllegalArgumentException(
                    "A slot length must be a positive number of minutes: " + slotLengthInMinutes);
        }
        int openingMinute = openingTime.getMinutesSinceMidnight();
        int closingMinute = closingTime.getMinutesSinceMidnight();
        int period = closingMinute - openingMinute;
        if (period <= 0) {
            throw new IllegalArgumentException(
                    "The clinic must close after it opens: " + openingTime + " to " + closingTime);
        }
        if (period % slotLengthInMinutes != 0) {
            throw new IllegalArgumentException("The opening period of " + period
                    + " minutes does not divide into slots of " + slotLengthInMinutes + " minutes.");
        }

        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.slotLengthInMinutes = slotLengthInMinutes;
        this.slots = new ArrayList<>();
        for (int start = openingMinute; start < closingMinute; start += slotLengthInMinutes) {
            slots.add(new ClockTime(start / ClockTime.MINUTES_PER_HOUR,
                    start % ClockTime.MINUTES_PER_HOUR));
        }
    }

    /**
     * Creates the standard clinic timetable: 08:00 to 16:00 in 30 minute slots.
     */
    public DailyTimetable() {
        this(new ClockTime(8, 0), new ClockTime(16, 0), 30);
    }

    /**
     * Returns every bookable start time of the day.
     *
     * <p>A copy is returned, so a caller cannot add or remove slots from the
     * clinic's timetable.</p>
     *
     * @return a copy of the slots, from the first of the day to the last
     */
    public List<ClockTime> getSlots() {
        return new ArrayList<>(slots);
    }

    /**
     * Checks whether a time of day is one of the bookable slots.
     *
     * @param time the time to check
     * @return true if the clinic can start an appointment at that time
     */
    public boolean isBookableSlot(ClockTime time) {
        return time != null && slots.contains(time);
    }

    /**
     * Returns the number of slots in the day.
     *
     * @return the number of bookable slots
     */
    public int getSlotCount() {
        return slots.size();
    }

    /**
     * Returns the length of one slot.
     *
     * @return the slot length in minutes
     */
    public int getSlotLengthInMinutes() {
        return slotLengthInMinutes;
    }

    /**
     * Returns a one line description of the timetable.
     *
     * @return the description, for example
     *         "08:00 to 16:00 in 30 minute slots (16 slots)"
     */
    public String describe() {
        return openingTime + " to " + closingTime + " in " + slotLengthInMinutes
                + " minute slots (" + slots.size() + " slots)";
    }

    /**
     * Returns the description of the timetable.
     *
     * @return the description produced by {@link #describe()}
     */
    @Override
    public String toString() {
        return describe();
    }
}
