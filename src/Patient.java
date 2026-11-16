import java.util.Objects;

/**
 * The patient who books an appointment.
 *
 * <p>The patient is modelled as its own class rather than as two loose
 * {@code String} fields inside the appointment. A name and a mobile number
 * belong together because they describe one person, and keeping them in a class
 * gives the rest of the system a single place to validate them, a single place
 * to change how a number is formatted for printing, and an object that can be
 * compared and passed around.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class Patient {

    /** The number of digits in an Australian mobile number. */
    private static final int MOBILE_LENGTH = 10;

    /** The full name of the patient. */
    private final String name;

    /** The mobile number of the patient, ten digits, for example "0412345678". */
    private final String mobileNumber;

    /**
     * Creates a patient from the name and mobile number supplied.
     *
     * @param name         the full name of the patient
     * @param mobileNumber the mobile number, ten digits only
     * @throws IllegalArgumentException if the name is empty or if the mobile
     *         number is not exactly ten digits
     */
    public Patient(String name, String mobileNumber) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A patient must have a name.");
        }
        String trimmedNumber = mobileNumber == null ? "" : mobileNumber.trim();
        if (!isTenDigits(trimmedNumber)) {
            throw new IllegalArgumentException(
                    "A mobile number must be exactly ten digits: " + mobileNumber);
        }
        this.name = name.trim();
        this.mobileNumber = trimmedNumber;
    }

    /**
     * Returns the full name of the patient.
     *
     * @return the name of the patient
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mobile number of the patient, without spaces.
     *
     * @return the ten digit mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Returns the mobile number in the readable form "0412 345 678".
     *
     * <p>The digits are stored without spaces so that two numbers can be
     * compared, and the spacing is added only when the number is printed.</p>
     *
     * @return the mobile number, spaced for reading
     */
    public String getFormattedMobileNumber() {
        return mobileNumber.substring(0, 4) + " " + mobileNumber.substring(4, 7)
                + " " + mobileNumber.substring(7);
    }

    /**
     * Checks whether this patient is the same person as another object.
     *
     * <p>Two patient records are the same person when their mobile numbers
     * match, because the number is what a patient is identified by when they
     * phone the clinic.</p>
     *
     * @param other the object to compare with
     * @return true if both objects hold the same mobile number
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        Patient patient = (Patient) other;
        return mobileNumber.equals(patient.mobileNumber);
    }

    /**
     * Returns a hash code that is consistent with {@link #equals(Object)}.
     *
     * @return the hash code of the mobile number
     */
    @Override
    public int hashCode() {
        return Objects.hash(mobileNumber);
    }

    /**
     * Returns the name and the formatted mobile number of the patient.
     *
     * @return the details of the patient, for example "Alice Nguyen (0412 345 678)"
     */
    @Override
    public String toString() {
        return name + " (" + getFormattedMobileNumber() + ")";
    }

    /**
     * Checks whether the text supplied is exactly ten digits.
     *
     * @param text the text to check
     * @return true if the text is ten characters, all of them digits
     */
    private static boolean isTenDigits(String text) {
        if (text.length() != MOBILE_LENGTH) {
            return false;
        }
        for (int index = 0; index < text.length(); index++) {
            if (!Character.isDigit(text.charAt(index))) {
                return false;
            }
        }
        return true;
    }
}
