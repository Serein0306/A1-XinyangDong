import java.util.ArrayList;
import java.util.List;

/**
 * A general practitioner, the first point of contact for most patients.
 *
 * <p>The class extends {@link HealthProfessional}, so the ID, the name, the
 * roster, the printing, the equality and the validation are inherited rather
 * than rewritten. It adds the two things only a GP records and does: a Medicare
 * provider number and the ability to prescribe.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class GeneralPractitioner extends HealthProfessional {

    /** The Medicare provider number of this GP, digits only. */
    private final String medicareProviderNumber;

    /**
     * Creates a GP with an explicit roster of working days.
     *
     * @param id                    the ID of the GP, digits only
     * @param name                  the full name of the GP
     * @param medicareProviderNumber the Medicare provider number, digits only
     * @param workingDays           the days of the week the GP works
     * @throws IllegalArgumentException if any value is missing, if the ID is
     *         not digits only, or if the provider number is not digits only
     */
    public GeneralPractitioner(String id, String name, String medicareProviderNumber,
            Weekday... workingDays) {
        super(id, name, workingDays);
        this.medicareProviderNumber = requireProviderNumber(medicareProviderNumber);
    }

    /**
     * Creates a GP who works the standard Monday to Friday roster.
     *
     * <p>The second way of constructing a GP: the roster is left to
     * {@link HealthProfessional}, so the caller supplies only what makes this
     * object a GP.</p>
     *
     * @param id                    the ID of the GP, digits only
     * @param name                  the full name of the GP
     * @param medicareProviderNumber the Medicare provider number, digits only
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public GeneralPractitioner(String id, String name, String medicareProviderNumber) {
        super(id, name);
        this.medicareProviderNumber = requireProviderNumber(medicareProviderNumber);
    }

    /**
     * Returns the Medicare provider number of this GP.
     *
     * @return the provider number, digits only
     */
    public String getMedicareProviderNumber() {
        return medicareProviderNumber;
    }

    /**
     * Writes a prescription for a patient. Only a GP can do this, which is
     * behaviour no other type of professional in the clinic has.
     *
     * @param patient    the patient the prescription is for
     * @param medication the medication prescribed
     * @return a description of the prescription
     * @throws IllegalArgumentException if the patient is missing or the
     *         medication is empty
     */
    public String prescribe(Patient patient, String medication) {
        if (patient == null) {
            throw new IllegalArgumentException("A prescription needs a patient.");
        }
        if (!hasText(medication)) {
            throw new IllegalArgumentException("A prescription needs a medication.");
        }
        return getName() + " prescribed " + medication.trim() + " for " + patient.getName();
    }

    /**
     * Returns the kind of professional this object is.
     *
     * @return ProfessionalType.GENERAL_PRACTITIONER
     */
    @Override
    public ProfessionalType getProfessionalType() {
        return ProfessionalType.GENERAL_PRACTITIONER;
    }

    /**
     * Returns the details only a GP records.
     *
     * @return the provider number and what the GP is authorised to do
     */
    @Override
    public List<String> getSpecialistDetails() {
        List<String> details = new ArrayList<>();
        details.add("Medicare provider number: " + medicareProviderNumber);
        details.add("Authorised to prescribe medication and refer to specialists");
        return details;
    }

    /**
     * Consults a patient: reviews them, prescribes and refers.
     *
     * @param patient the patient being seen
     * @return a description of the consultation
     * @throws IllegalArgumentException if the patient is missing
     */
    @Override
    public String deliverService(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("A patient is needed for a consultation.");
        }
        return prescribe(patient, "a course of antibiotics")
                + " and referred the patient for a blood test.";
    }

    /**
     * Validates and trims the provider number supplied.
     *
     * @param providerNumber the value to validate
     * @return the trimmed provider number
     * @throws IllegalArgumentException if the value is not digits only
     */
    private static String requireProviderNumber(String providerNumber) {
        if (!isDigitsOnly(providerNumber)) {
            throw new IllegalArgumentException(
                    "A Medicare provider number must be numbers only: " + providerNumber);
        }
        return providerNumber.trim();
    }
}
