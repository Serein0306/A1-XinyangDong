import java.util.Objects;

/**
 * One appointment: a patient, a health professional, the day and the time.
 *
 * <p>The appointment refers to a {@link Patient} object and a
 * {@link HealthProfessional} object rather than to loose text. Because the
 * professional is held through the general type, a booking can be made with any
 * of the five specialties without this class knowing which one it is.</p>
 *
 * <p>An appointment only makes sense when all four details are present, so the
 * constructor refuses to build an incomplete one. It also refuses a booking
 * with a professional who is not rostered on that day, which stops the object
 * from ever existing in a state the clinic could not honour. Every field is
 * private and final, so once the object exists it cannot be changed.</p>
 *
 * <p>The class implements {@link Comparable}, which gives each appointment the
 * ability to say how it orders against another one. Two appointments are
 * ordered by time alone, so a collection of them sorts from earliest to latest.
 * When two are at the same time the comparison falls back to the professional's
 * ID purely so that the order is predictable; the clinic is happy with either
 * order at the same time.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class Appointment implements Comparable<Appointment> {

    /** The patient who booked the appointment. */
    private final Patient patient;

    /** The health professional the patient is booked to see. */
    private final HealthProfessional professional;

    /** The day of the appointment, which is always the current day. */
    private final Weekday day;

    /** The time of the appointment. */
    private final ClockTime time;

    /**
     * Creates an appointment from the four details supplied.
     *
     * @param patient      the patient who booked the appointment
     * @param professional the professional the patient will see
     * @param day          the day of the appointment
     * @param time         the time of the appointment
     * @throws IllegalArgumentException if any detail is missing, or if the
     *         professional does not work on the day of the appointment
     */
    public Appointment(Patient patient, HealthProfessional professional, Weekday day,
            ClockTime time) {
        if (patient == null) {
            throw new IllegalArgumentException("An appointment must have a patient.");
        }
        if (professional == null) {
            throw new IllegalArgumentException("An appointment must have a health professional.");
        }
        if (day == null) {
            throw new IllegalArgumentException("An appointment must have a day.");
        }
        if (time == null) {
            throw new IllegalArgumentException("An appointment must have a time.");
        }
        if (!professional.worksOn(day)) {
            throw new IllegalArgumentException(professional.getName() + " (#" + professional.getId()
                    + ") does not work on " + day + ", so this appointment cannot be made.");
        }

        this.patient = patient;
        this.professional = professional;
        this.day = day;
        this.time = time;
    }

    /**
     * Returns the patient who booked the appointment.
     *
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the professional the patient is booked to see.
     *
     * @return the health professional
     */
    public HealthProfessional getProfessional() {
        return professional;
    }

    /**
     * Returns the day of the appointment.
     *
     * @return the day of the appointment
     */
    public Weekday getDay() {
        return day;
    }

    /**
     * Returns the time of the appointment.
     *
     * @return the time of the appointment
     */
    public ClockTime getTime() {
        return time;
    }

    /**
     * Checks whether this appointment is booked with the professional whose ID
     * is supplied.
     *
     * @param professionalId the ID to look for, digits only
     * @return true if this appointment belongs to that professional
     */
    public boolean isForProfessionalWithId(String professionalId) {
        return professionalId != null
                && professional.getId().equals(professionalId.trim());
    }

    /**
     * Checks whether this appointment is booked with the professional supplied.
     *
     * <p>The comparison uses {@link HealthProfessional#equals(Object)}, which
     * treats two objects with the same ID as the same professional.</p>
     *
     * @param other the professional to look for
     * @return true if this appointment belongs to that professional
     */
    public boolean isWith(HealthProfessional other) {
        return other != null && professional.equals(other);
    }

    /**
     * Checks whether this appointment is at the time supplied.
     *
     * @param otherTime the time to look for
     * @return true if this appointment starts at that time
     */
    public boolean isAt(ClockTime otherTime) {
        return otherTime != null && time.equals(otherTime);
    }

    /**
     * Checks whether this appointment belongs to the patient with the mobile
     * number supplied.
     *
     * @param mobileNumber the mobile number to look for
     * @return true if this appointment belongs to that patient
     */
    public boolean isForPatientWithMobile(String mobileNumber) {
        return mobileNumber != null
                && patient.getMobileNumber().equals(mobileNumber.trim());
    }

    /**
     * Compares this appointment with another one, earliest first.
     *
     * @param other the appointment to compare with
     * @return a negative number if this appointment is earlier, zero if both
     *         are at the same time, and a positive number if this one is later
     * @throws IllegalArgumentException if {@code other} is null
     */
    @Override
    public int compareTo(Appointment other) {
        if (other == null) {
            throw new IllegalArgumentException("An appointment cannot be compared with null.");
        }
        int byTime = time.compareTo(other.time);
        if (byTime != 0) {
            return byTime;
        }
        // Same time: order by ID so that a printed timetable is repeatable.
        return professional.getId().compareTo(other.professional.getId());
    }

    /**
     * Checks whether this appointment and another object describe the same
     * booking.
     *
     * @param other the object to compare with
     * @return true if the patient, professional, day and time all match
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Appointment)) {
            return false;
        }
        Appointment appointment = (Appointment) other;
        return patient.equals(appointment.patient)
                && professional.equals(appointment.professional)
                && day == appointment.day
                && time.equals(appointment.time);
    }

    /**
     * Returns a hash code that is consistent with {@link #equals(Object)}.
     *
     * @return the hash code of the booking details
     */
    @Override
    public int hashCode() {
        return Objects.hash(patient, professional, day, time);
    }

    /**
     * Returns the details of the appointment in a readable form, including the
     * professional that was booked.
     *
     * @return the details of the appointment, several lines in length
     */
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder();
        details.append("Appointment at ").append(time).append(" on ").append(day);
        details.append(System.lineSeparator());
        details.append("    Patient      : ").append(patient);
        details.append(System.lineSeparator());
        details.append("    Professional : ").append(professional.toSummaryLine());
        details.append(System.lineSeparator());
        details.append("    Their roster : ").append(professional.getDaysSummary());
        return details.toString();
    }
}
