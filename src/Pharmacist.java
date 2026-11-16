import java.util.ArrayList;
import java.util.List;

/**
 * A pharmacist, who dispenses and reviews medication for patients.
 *
 * <p>The fifth subclass of the general type. A pharmacist is a health
 * professional with the same shared data as everyone else, and adds a licence
 * number and the ability to dispense a prescription.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class Pharmacist extends HealthProfessional {

    /** The licence number of this pharmacist, digits only. */
    private final String licenceNumber;

    /**
     * Creates a pharmacist with an explicit roster.
     *
     * @param id            the ID of the pharmacist, digits only
     * @param name          the full name of the pharmacist
     * @param licenceNumber the licence number, digits only
     * @param workingDays   the days of the week the pharmacist works
     * @throws IllegalArgumentException if a value is missing, if the ID is not
     *         digits only, or if the licence number is not digits only
     */
    public Pharmacist(String id, String name, String licenceNumber, Weekday... workingDays) {
        super(id, name, workingDays);
        this.licenceNumber = requireLicenceNumber(licenceNumber);
    }

    /**
     * Creates a pharmacist working the standard Monday to Friday roster.
     *
     * @param id            the ID of the pharmacist, digits only
     * @param name          the full name of the pharmacist
     * @param licenceNumber the licence number, digits only
     * @throws IllegalArgumentException if the ID, name or licence is invalid
     */
    public Pharmacist(String id, String name, String licenceNumber) {
        super(id, name);
        this.licenceNumber = requireLicenceNumber(licenceNumber);
    }

    /**
     * Returns the licence number of this pharmacist.
     *
     * @return the licence number, digits only
     */
    public String getLicenceNumber() {
        return licenceNumber;
    }

    /**
     * Dispenses a medication to a patient and checks it against their record.
     *
     * @param patient    the patient collecting the medication
     * @param medication the medication dispensed
     * @return a description of the dispensing
     * @throws IllegalArgumentException if the patient is missing or the
     *         medication is empty
     */
    public String dispense(Patient patient, String medication) {
        if (patient == null) {
            throw new IllegalArgumentException("Dispensing needs a patient.");
        }
        if (!hasText(medication)) {
            throw new IllegalArgumentException("Dispensing needs a medication.");
        }
        return getName() + " dispensed " + medication.trim() + " to " + patient.getName()
                + " and checked it for interactions";
    }

    /**
     * Returns the kind of professional this object is.
     *
     * @return ProfessionalType.PHARMACIST
     */
    @Override
    public ProfessionalType getProfessionalType() {
        return ProfessionalType.PHARMACIST;
    }

    /**
     * Returns the details only a pharmacist records.
     *
     * @return the licence number and the pharmacy duties
     */
    @Override
    public List<String> getSpecialistDetails() {
        List<String> details = new ArrayList<>();
        details.add("Licence number: " + licenceNumber);
        details.add("Duties: dispensing, medication reviews, interaction checks");
        return details;
    }

    /**
     * Dispenses a prescription for a patient who has been seen by a GP.
     *
     * @param patient the patient collecting the medication
     * @return a description of the service
     * @throws IllegalArgumentException if the patient is missing
     */
    @Override
    public String deliverService(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("A patient is needed for a pharmacy service.");
        }
        return dispense(patient, "amoxicillin 500 mg") + ", and explained the dosage schedule.";
    }

    /**
     * Validates and trims the licence number supplied.
     *
     * @param licenceNumber the value to validate
     * @return the trimmed licence number
     * @throws IllegalArgumentException if the value is not digits only
     */
    private static String requireLicenceNumber(String licenceNumber) {
        if (!isDigitsOnly(licenceNumber)) {
            throw new IllegalArgumentException(
                    "A pharmacist licence number must be numbers only: " + licenceNumber);
        }
        return licenceNumber.trim();
    }
}
