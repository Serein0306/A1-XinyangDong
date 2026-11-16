import java.util.ArrayList;
import java.util.List;

/**
 * A physiotherapist, who works on movement, strength and rehabilitation.
 *
 * <p>The class extends the same general type as every other professional, so
 * the shared data and the printing come from the base class. It adds the area
 * this physiotherapist specialises in and the ability to design an exercise
 * programme, which is behaviour of its own.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class Physiotherapist extends HealthProfessional {

    /** The focus area used when the caller does not supply one. */
    private static final String DEFAULT_FOCUS_AREA = "General physiotherapy";

    /** The area this physiotherapist focuses on, for example "Sports injuries". */
    private final String focusArea;

    /**
     * Creates a physiotherapist with an explicit focus area and roster.
     *
     * @param id          the ID of the physiotherapist, digits only
     * @param name        the full name of the physiotherapist
     * @param focusArea   the area the physiotherapist focuses on
     * @param workingDays the days of the week the physiotherapist works
     * @throws IllegalArgumentException if the ID or name is invalid, or if the
     *         focus area is empty
     */
    public Physiotherapist(String id, String name, String focusArea, Weekday... workingDays) {
        super(id, name, workingDays);
        if (!hasText(focusArea)) {
            throw new IllegalArgumentException(
                    "A physiotherapist must have a focus area: " + focusArea);
        }
        this.focusArea = focusArea.trim();
    }

    /**
     * Creates a general physiotherapist working the standard Monday to Friday
     * roster.
     *
     * @param id   the ID of the physiotherapist, digits only
     * @param name the full name of the physiotherapist
     * @throws IllegalArgumentException if the ID is not digits only or if the
     *         name is empty
     */
    public Physiotherapist(String id, String name) {
        super(id, name);
        this.focusArea = DEFAULT_FOCUS_AREA;
    }

    /**
     * Returns the area this physiotherapist focuses on.
     *
     * @return the focus area
     */
    public String getFocusArea() {
        return focusArea;
    }

    /**
     * Designs an exercise programme for a patient.
     *
     * @param patient the patient the programme is for
     * @return a description of the programme
     * @throws IllegalArgumentException if the patient is missing
     */
    public String designExerciseProgramme(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("An exercise programme needs a patient.");
        }
        return getName() + " designed a six week " + focusArea.toLowerCase()
                + " programme for " + patient.getName();
    }

    /**
     * Returns the kind of professional this object is.
     *
     * @return ProfessionalType.PHYSIOTHERAPIST
     */
    @Override
    public ProfessionalType getProfessionalType() {
        return ProfessionalType.PHYSIOTHERAPIST;
    }

    /**
     * Returns the details only a physiotherapist records.
     *
     * @return the focus area and the treatment duties
     */
    @Override
    public List<String> getSpecialistDetails() {
        List<String> details = new ArrayList<>();
        details.add("Focus area: " + focusArea);
        details.add("Duties: assessment, manual therapy, exercise programmes");
        return details;
    }

    /**
     * Assesses a patient and starts their rehabilitation.
     *
     * @param patient the patient being seen
     * @return a description of the treatment
     * @throws IllegalArgumentException if the patient is missing
     */
    @Override
    public String deliverService(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("A patient is needed for a physiotherapy service.");
        }
        return designExerciseProgramme(patient) + " and reviewed their walking technique.";
    }
}
