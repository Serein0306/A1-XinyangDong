/**
 * The kinds of health professional the clinic employs.
 *
 * <p>Each concrete professional class answers {@code getType()} with one of
 * these constants instead of returning a hand typed {@code String}. The label
 * is then written once, here, so a typo cannot make two professionals of the
 * same kind print differently, and adding a new kind of professional is a
 * matter of adding a constant and a subclass.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public enum ProfessionalType {

    /** A general practitioner, the first point of contact for a patient. */
    GENERAL_PRACTITIONER("General Practitioner"),

    /** A nurse, who works on the wards and in the treatment room. */
    NURSE("Nurse"),

    /** A physiotherapist, who works on movement and rehabilitation. */
    PHYSIOTHERAPIST("Physiotherapist"),

    /** A radiographer, who operates the imaging equipment. */
    RADIOGRAPHER("Radiographer"),

    /** A pharmacist, who dispenses and reviews medication. */
    PHARMACIST("Pharmacist");

    /** The label used whenever this kind of professional is printed. */
    private final String displayName;

    /**
     * Creates a kind of professional with its printed label.
     *
     * @param displayName the label of the kind
     */
    ProfessionalType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the printed label of this kind of professional.
     *
     * @return the label, for example "General Practitioner"
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the printed label of this kind of professional.
     *
     * @return the label of the kind
     */
    @Override
    public String toString() {
        return displayName;
    }
}
