import java.util.ArrayList;
import java.util.List;

/**
 * A radiographer, who operates the clinic's imaging equipment.
 *
 * <p>One more subclass of the general type. A radiographer shares the ID, the
 * name and the roster with everyone else, and adds the imaging modality they
 * work with and the ability to perform a scan.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class Radiographer extends HealthProfessional {

    /** The modality used when the caller does not supply one. */
    private static final String DEFAULT_MODALITY = "General radiography";

    /** The imaging modality this radiographer works with, for example "CT". */
    private final String modality;

    /**
     * Creates a radiographer with an explicit modality and roster.
     *
     * @param id          the ID of the radiographer, digits only
     * @param name        the full name of the radiographer
     * @param modality    the imaging modality the radiographer works with
     * @param workingDays the days of the week the radiographer works
     * @throws IllegalArgumentException if the ID or name is invalid, or if the
     *         modality is empty
     */
    public Radiographer(String id, String name, String modality, Weekday... workingDays) {
        super(id, name, workingDays);
        if (!hasText(modality)) {
            throw new IllegalArgumentException(
                    "A radiographer must have an imaging modality: " + modality);
        }
        this.modality = modality.trim();
    }

    /**
     * Creates a general radiographer working the standard Monday to Friday
     * roster.
     *
     * @param id   the ID of the radiographer, digits only
     * @param name the full name of the radiographer
     * @throws IllegalArgumentException if the ID is not digits only or if the
     *         name is empty
     */
    public Radiographer(String id, String name) {
        super(id, name);
        this.modality = DEFAULT_MODALITY;
    }

    /**
     * Returns the imaging modality this radiographer works with.
     *
     * @return the modality
     */
    public String getModality() {
        return modality;
    }

    /**
     * Performs an imaging study on a patient.
     *
     * @param patient the patient being imaged
     * @return a description of the study
     * @throws IllegalArgumentException if the patient is missing
     */
    public String performScan(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("A scan needs a patient.");
        }
        return getName() + " performed a " + modality + " scan on " + patient.getName()
                + " and sent the images to the reporting radiologist";
    }

    /**
     * Returns the kind of professional this object is.
     *
     * @return ProfessionalType.RADIOGRAPHER
     */
    @Override
    public ProfessionalType getProfessionalType() {
        return ProfessionalType.RADIOGRAPHER;
    }

    /**
     * Returns the details only a radiographer records.
     *
     * @return the modality and the imaging duties
     */
    @Override
    public List<String> getSpecialistDetails() {
        List<String> details = new ArrayList<>();
        details.add("Imaging modality: " + modality);
        details.add("Duties: patient positioning, image acquisition, quality checks");
        return details;
    }

    /**
     * Images a patient and checks the quality of the images.
     *
     * @param patient the patient being seen
     * @return a description of the imaging service
     * @throws IllegalArgumentException if the patient is missing
     */
    @Override
    public String deliverService(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("A patient is needed for an imaging service.");
        }
        return performScan(patient) + ", then checked the images for quality.";
    }
}
