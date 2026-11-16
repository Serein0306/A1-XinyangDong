import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A health professional who works at the clinic.
 *
 * <p>This is the general type required by Part 1. It holds only the data and
 * behaviour that every health professional has, whatever their specialty: an ID
 * made of digits, a name, and the fixed set of days they work each week. The
 * three abstract methods at the bottom are the other half of the design: they
 * declare what every professional must be able to do, while the concrete
 * subclasses decide how. That is what allows one loop over a list of
 * professionals to print, describe and exercise each of them correctly.</p>
 *
 * <p>The class is {@code abstract}, so a "plain" health professional with no
 * specialty cannot be created. The clinic only ever employs real specialties,
 * so this removes a whole category of meaningless objects.</p>
 *
 * <p>The data is private and final with no setters, so external code can read a
 * professional but can never put one into an invalid state. All validation
 * happens once, in the constructor, and the roster is handed out as a copy so
 * that a caller cannot edit it through the collection it received.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public abstract class HealthProfessional {

    /** The roster used when the caller does not supply one: Monday to Friday. */
    private static final List<Weekday> DEFAULT_ROSTER = Arrays.asList(
            Weekday.MONDAY, Weekday.TUESDAY, Weekday.WEDNESDAY,
            Weekday.THURSDAY, Weekday.FRIDAY);

    /** The ID of the professional, digits only, for example "1042". */
    private final String id;

    /** The full name of the professional, for example "Dr Mia Chen". */
    private final String name;

    /** The days of the week this professional works. */
    private final List<Weekday> workingDays;

    /**
     * Creates a professional with an explicit roster of working days.
     *
     * <p>This is the full constructor, and the one the concrete subclasses use
     * when the caller does supply a roster.</p>
     *
     * @param id          the ID of the professional, digits only
     * @param name        the full name of the professional
     * @param workingDays the days of the week the professional works
     * @throws IllegalArgumentException if the ID is not digits only, if the
     *         name is empty, or if no working day is supplied
     */
    protected HealthProfessional(String id, String name, Weekday... workingDays) {
        if (!isDigitsOnly(id)) {
            throw new IllegalArgumentException(
                    "A health professional ID must be numbers only: " + id);
        }
        if (!hasText(name)) {
            throw new IllegalArgumentException("A health professional must have a name.");
        }
        if (workingDays == null || workingDays.length == 0) {
            throw new IllegalArgumentException(
                    "A health professional must work at least one day of the week.");
        }

        List<Weekday> days = new ArrayList<>();
        for (Weekday day : workingDays) {
            if (day == null) {
                throw new IllegalArgumentException("A working day cannot be null.");
            }
            // Working twice on the same day is the same as working that day,
            // so a repeated day is ignored rather than stored twice.
            if (!days.contains(day)) {
                days.add(day);
            }
        }

        this.id = id.trim();
        this.name = name.trim();
        this.workingDays = days;
    }

    /**
     * Creates a professional with the standard Monday to Friday roster.
     *
     * <p>This is the second way of constructing a professional. A caller who
     * does not care about the roster gets a sensible default rather than being
     * made to list the same five days every time.</p>
     *
     * @param id   the ID of the professional, digits only
     * @param name the full name of the professional
     * @throws IllegalArgumentException if the ID is not digits only or if the
     *         name is empty
     */
    protected HealthProfessional(String id, String name) {
        this(id, name, DEFAULT_ROSTER.toArray(new Weekday[0]));
    }

    /**
     * Returns the ID of the professional.
     *
     * @return the ID, digits only
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the full name of the professional.
     *
     * @return the name of the professional
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the days this professional works.
     *
     * @return a copy of the roster, in the order it was supplied
     */
    public List<Weekday> getWorkingDays() {
        return new ArrayList<>(workingDays);
    }

    /**
     * Returns the roster as one readable line.
     *
     * @return the working days separated by commas, for example "Mon, Wed, Fri"
     */
    public String getDaysSummary() {
        StringBuilder summary = new StringBuilder();
        for (int index = 0; index < workingDays.size(); index++) {
            if (index > 0) {
                summary.append(", ");
            }
            summary.append(workingDays.get(index).getShortName());
        }
        return summary.toString();
    }

    /**
     * Checks whether this professional is rostered on the day supplied.
     *
     * @param day the day to check
     * @return true if the professional works that day
     */
    public boolean worksOn(Weekday day) {
        return day != null && workingDays.contains(day);
    }

    /**
     * Returns the kind of professional this object is, in words.
     *
     * <p>Every subclass must answer this for itself, which is what lets a group
     * of different professionals be printed through this single type.</p>
     *
     * @return the kind of professional, for example ProfessionalType.NURSE
     */
    public abstract ProfessionalType getProfessionalType();

    /**
     * Returns the details that only this specialty records, one per line.
     *
     * <p>The lines are appended to the shared details by {@link #toString()},
     * so no subclass has to repeat the ID, the name or the roster when it
     * presents itself.</p>
     *
     * @return the specialty specific details, one per line
     */
    public abstract List<String> getSpecialistDetails();

    /**
     * Performs the clinical task this type of professional is able to do for a
     * patient.
     *
     * <p>The same call on the same general type produces a different answer for
     * each subclass, because the object that is actually being pointed at
     * supplies the behaviour.</p>
     *
     * @param patient the patient being seen
     * @return a description of what was done for the patient
     */
    public abstract String deliverService(Patient patient);

    /**
     * Returns a one line summary of who this professional is.
     *
     * @return the kind, the ID and the name, for example
     *         "General Practitioner #1042 - Dr Mia Chen"
     */
    public String toSummaryLine() {
        return getProfessionalType().getDisplayName() + " #" + id + " - " + name;
    }

    /**
     * Returns every detail of the professional in a readable form, so that
     * {@code System.out.println(professional)} shows the shared data and the
     * specialty data together.
     *
     * @return the details of the professional, several lines in length
     */
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder();
        details.append(toSummaryLine()).append(System.lineSeparator());
        details.append("    Working days: ").append(getDaysSummary());
        for (String line : getSpecialistDetails()) {
            details.append(System.lineSeparator()).append("    ").append(line);
        }
        return details.toString();
    }

    /**
     * Checks whether this professional and another object are the same
     * professional.
     *
     * <p>The ID is the clinic's identifier for a professional, so two objects
     * with the same ID are the same professional even if one is a general
     * practitioner and the other were somehow a nurse. The manager relies on
     * this to recognise a double booking, and the class documentation points at
     * it as the reason the design is sound.</p>
     *
     * @param other the object to compare with
     * @return true if both objects hold the same ID
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthProfessional)) {
            return false;
        }
        HealthProfessional otherProfessional = (HealthProfessional) other;
        return id.equals(otherProfessional.id);
    }

    /**
     * Returns a hash code that is consistent with {@link #equals(Object)}.
     *
     * @return the hash code of the ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Checks whether the text supplied contains at least one visible character.
     *
     * @param text the text to check
     * @return true if the text is not null and not only spaces
     */
    protected static boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }

    /**
     * Checks whether the text supplied is made up of digits only.
     *
     * @param text the text to check
     * @return true if the text is not null, not empty, and digits only
     */
    protected static boolean isDigitsOnly(String text) {
        if (text == null) {
            return false;
        }
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        for (int index = 0; index < trimmed.length(); index++) {
            if (!Character.isDigit(trimmed.charAt(index))) {
                return false;
            }
        }
        return true;
    }
}
