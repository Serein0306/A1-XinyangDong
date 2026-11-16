import java.util.ArrayList;
import java.util.List;

/**
 * A nurse, who works on a ward and in the treatment room.
 *
 * <p>A nurse is a health professional in the same way a doctor is, so the class
 * extends the same general type and inherits everything shared. What a nurse
 * adds is the ward they are based on, and the ability to take a blood sample
 * rather than to prescribe.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class Nurse extends HealthProfessional {

    /** The ward used when the caller does not name one. */
    private static final String DEFAULT_WARD = "Outpatient Treatment Room";

    /** The ward this nurse is based on, for example "Ward 4C". */
    private final String ward;

    /**
     * Creates a nurse with an explicit ward and roster.
     *
     * @param id          the ID of the nurse, digits only
     * @param name        the full name of the nurse
     * @param ward        the ward the nurse is based on
     * @param workingDays the days of the week the nurse works
     * @throws IllegalArgumentException if the ID or name is invalid, or if the
     *         ward is empty
     */
    public Nurse(String id, String name, String ward, Weekday... workingDays) {
        super(id, name, workingDays);
        if (!hasText(ward)) {
            throw new IllegalArgumentException("A nurse must be based on a ward: " + ward);
        }
        this.ward = ward.trim();
    }

    /**
     * Creates a nurse for the outpatient treatment room, working the standard
     * Monday to Friday roster.
     *
     * <p>The second way of constructing a nurse. Here the default is not just
     * the roster but also the ward, which shows how a class can offer a
     * sensible starting point for more than one attribute.</p>
     *
     * @param id   the ID of the nurse, digits only
     * @param name the full name of the nurse
     * @throws IllegalArgumentException if the ID is not digits only or if the
     *         name is empty
     */
    public Nurse(String id, String name) {
        super(id, name);
        this.ward = DEFAULT_WARD;
    }

    /**
     * Returns the ward this nurse is based on.
     *
     * @return the ward
     */
    public String getWard() {
        return ward;
    }

    /**
     * Takes a blood sample from a patient, which is a nursing task and not
     * something the other types of professional in the clinic do.
     *
     * @param patient the patient the sample is taken from
     * @return a description of the procedure
     * @throws IllegalArgumentException if the patient is missing
     */
    public String takeBloodSample(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("A blood sample needs a patient.");
        }
        return getName() + " took a blood sample from " + patient.getName()
                + " and labelled it for pathology";
    }

    /**
     * Returns the kind of professional this object is.
     *
     * @return ProfessionalType.NURSE
     */
    @Override
    public ProfessionalType getProfessionalType() {
        return ProfessionalType.NURSE;
    }

    /**
     * Returns the details only a nurse records.
     *
     * @return the ward and the nursing duties performed
     */
    @Override
    public List<String> getSpecialistDetails() {
        List<String> details = new ArrayList<>();
        details.add("Based on: " + ward);
        details.add("Duties: observations, wound care, blood samples");
        return details;
    }

    /**
     * Cares for a patient in the treatment room.
     *
     * @param patient the patient being seen
     * @return a description of the care given
     * @throws IllegalArgumentException if the patient is missing
     */
    @Override
    public String deliverService(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("A patient is needed for a nursing service.");
        }
        return takeBloodSample(patient) + ", then recorded the patient's observations.";
    }
}
