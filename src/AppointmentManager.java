import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Holds and manages the day's appointments for the clinic.
 *
 * <p>An {@code ArrayList} is used for the collection. The clinic adds and
 * cancels bookings throughout the day and needs to walk through the list to
 * display, filter and sort it, which is exactly what a resizable list is good
 * at; there is no need to look appointments up by a key, so a map would only
 * add complexity. Because the class keeps the list private, every rule about
 * the bookings (no double booking, only bookable slots, only rostered
 * professionals) is enforced in one place.</p>
 *
 * <p>Every operation that can fail returns an {@link OperationResult} instead of
 * printing a warning. The caller can then print it, or ignore it, and the
 * manager itself never writes to the console.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class AppointmentManager {

    /** The clinic timetable, which decides which times can be booked. */
    private final DailyTimetable timetable;

    /** The current day. All bookings are for this day only. */
    private final Weekday today;

    /** The appointments held by the clinic, in the order they were booked. */
    private final List<Appointment> appointments;

    /**
     * Creates a manager for the timetable and the day supplied.
     *
     * @param timetable the clinic timetable
     * @param today     the current day, the only day that can be booked
     * @throws IllegalArgumentException if the timetable or the day is missing
     */
    public AppointmentManager(DailyTimetable timetable, Weekday today) {
        if (timetable == null) {
            throw new IllegalArgumentException("An appointment manager needs a timetable.");
        }
        if (today == null) {
            throw new IllegalArgumentException("An appointment manager needs the current day.");
        }
        this.timetable = timetable;
        this.today = today;
        this.appointments = new ArrayList<>();
    }

    /**
     * Creates a manager for the standard clinic timetable and the day supplied.
     *
     * @param today the current day, the only day that can be booked
     * @throws IllegalArgumentException if the day is missing
     */
    public AppointmentManager(Weekday today) {
        this(new DailyTimetable(), today);
    }

    /**
     * Books an appointment for today.
     *
     * <p>The booking is checked in the order a receptionist would check it: is
     * everything supplied, is the time one the clinic uses, is the professional
     * working today, and is that professional free at that time.</p>
     *
     * @param patient      the patient booking the appointment
     * @param professional the professional the patient wants to see
     * @param time         the time of the appointment
     * @return a successful result describing the booking, or a failed result
     *         explaining why it was refused
     */
    public OperationResult book(Patient patient, HealthProfessional professional, ClockTime time) {
        if (patient == null) {
            return OperationResult.failure("Booking refused: no patient was supplied.");
        }
        if (professional == null) {
            return OperationResult.failure("Booking refused: no health professional was supplied.");
        }
        if (time == null) {
            return OperationResult.failure("Booking refused: no time was supplied.");
        }
        if (!timetable.isBookableSlot(time)) {
            return OperationResult.failure("Booking refused: " + time
                    + " is not a bookable slot. The clinic runs " + timetable.describe() + ".");
        }
        if (!professional.worksOn(today)) {
            return OperationResult.failure("Booking refused: " + professional.getName() + " (#"
                    + professional.getId() + ") does not work on " + today
                    + ". Their roster is " + professional.getDaysSummary() + ".");
        }
        if (isAlreadyBooked(professional, time)) {
            return OperationResult.failure("Booking refused: " + professional.getName() + " (#"
                    + professional.getId() + ") already has an appointment at " + time + ".");
        }

        Appointment appointment = new Appointment(patient, professional, today, time);
        appointments.add(appointment);
        return OperationResult.success("Booked " + time + " on " + today + " for " + patient
                + " with " + professional.getName() + " (#" + professional.getId() + ").");
    }

    /**
     * Checks whether a professional already has an appointment at a time.
     *
     * @param professional the professional to check
     * @param time         the time to check
     * @return true if that professional is already booked at that time
     */
    public boolean isAlreadyBooked(HealthProfessional professional, ClockTime time) {
        for (Appointment appointment : appointments) {
            if (appointment.isWith(professional) && appointment.isAt(time)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns every appointment held, sorted from earliest to latest.
     *
     * @return a sorted copy of the appointments
     */
    public List<Appointment> getAllAppointments() {
        return sortedCopy(appointments);
    }

    /**
     * Returns the appointments of the professional with the ID supplied.
     *
     * @param professionalId the ID to look for, digits only
     * @return the matching appointments, in the order they were booked
     */
    public List<Appointment> findByProfessionalId(String professionalId) {
        List<Appointment> matches = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.isForProfessionalWithId(professionalId)) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    /**
     * Returns the appointments of the professional supplied.
     *
     * @param professional the professional to look for
     * @return the matching appointments, in the order they were booked
     */
    public List<Appointment> findByProfessional(HealthProfessional professional) {
        List<Appointment> matches = new ArrayList<>();
        if (professional == null) {
            return matches;
        }
        for (Appointment appointment : appointments) {
            if (appointment.isWith(professional)) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    /**
     * Returns the appointments of the patient with the mobile number supplied.
     *
     * @param mobileNumber the mobile number to look for
     * @return the matching appointments, in the order they were booked
     */
    public List<Appointment> findByPatientMobile(String mobileNumber) {
        List<Appointment> matches = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.isForPatientWithMobile(mobileNumber)) {
                matches.add(appointment);
            }
        }
        return matches;
    }

    /**
     * Sorts the appointments supplied in place, earliest first.
     *
     * <p>It is given a list from somewhere else on purpose: the manager sorts
     * the whole day through {@link #getAllAppointments()}, and a filtered
     * subset through this method, without either of them having their own
     * sorting code.</p>
     *
     * @param toSort the appointments to sort
     * @throws IllegalArgumentException if the list is null
     */
    public void sortInTimeOrder(List<Appointment> toSort) {
        if (toSort == null) {
            throw new IllegalArgumentException("There is nothing to sort.");
        }
        Collections.sort(toSort);
    }

    /**
     * Cancels the appointment of the professional with the ID supplied at the
     * time supplied.
     *
     * @param professionalId the ID of the professional, digits only
     * @param time           the time of the appointment
     * @return a successful result describing the cancellation, or a failed
     *         result explaining why nothing was cancelled
     */
    public OperationResult cancel(String professionalId, ClockTime time) {
        if (time == null) {
            return OperationResult.failure("Cancellation refused: no time was supplied.");
        }
        Iterator<Appointment> iterator = appointments.iterator();
        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();
            if (appointment.isForProfessionalWithId(professionalId)
                    && appointment.isAt(time)) {
                iterator.remove();
                return OperationResult.success("Cancelled " + time + " appointment for "
                        + appointment.getProfessional().getName() + " with "
                        + appointment.getPatient() + ".");
            }
        }
        return OperationResult.failure("Cancellation refused: no appointment found for professional "
                + professionalId + " at " + time + ".");
    }

    /**
     * Cancels the appointment of the professional supplied at the time supplied.
     *
     * @param professional the professional whose appointment is cancelled
     * @param time         the time of the appointment
     * @return a successful result describing the cancellation, or a failed
     *         result explaining why nothing was cancelled
     */
    public OperationResult cancel(HealthProfessional professional, ClockTime time) {
        if (professional == null) {
            return OperationResult.failure("Cancellation refused: no health professional was supplied.");
        }
        if (time == null) {
            return OperationResult.failure("Cancellation refused: no time was supplied.");
        }
        Iterator<Appointment> iterator = appointments.iterator();
        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();
            if (appointment.isWith(professional) && appointment.isAt(time)) {
                iterator.remove();
                return OperationResult.success("Cancelled " + time + " appointment for "
                        + professional.getName() + " with " + appointment.getPatient() + ".");
            }
        }
        return OperationResult.failure("Cancellation refused: " + professional.getName()
                + " has no appointment at " + time + ".");
    }

    /**
     * Cancels every appointment held under the mobile number supplied.
     *
     * @param mobileNumber the mobile number of the patient
     * @return a successful result describing how many appointments were
     *         cancelled, or a failed result if the number had none
     */
    public OperationResult cancelByPatientMobile(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.trim().isEmpty()) {
            return OperationResult.failure("Cancellation refused: no mobile number was supplied.");
        }
        int cancelled = 0;
        Iterator<Appointment> iterator = appointments.iterator();
        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();
            if (appointment.isForPatientWithMobile(mobileNumber)) {
                iterator.remove();
                cancelled++;
            }
        }
        if (cancelled == 0) {
            return OperationResult.failure("Cancellation refused: no appointment found for mobile number "
                    + mobileNumber.trim() + ".");
        }
        return OperationResult.success("Cancelled " + cancelled + " appointment(s) for mobile number "
                + mobileNumber.trim() + ".");
    }

    /**
     * Returns the number of appointments currently held.
     *
     * @return the number of appointments
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Returns the clinic timetable this manager books against.
     *
     * @return the timetable
     */
    public DailyTimetable getTimetable() {
        return timetable;
    }

    /**
     * Returns the day all bookings are made for.
     *
     * @return the current day
     */
    public Weekday getToday() {
        return today;
    }

    /**
     * Returns a sorted copy of the list supplied.
     *
     * @param toCopy the appointments to copy and sort
     * @return the sorted copy
     */
    private static List<Appointment> sortedCopy(List<Appointment> toCopy) {
        List<Appointment> copy = new ArrayList<>(toCopy);
        Collections.sort(copy);
        return copy;
    }
}
