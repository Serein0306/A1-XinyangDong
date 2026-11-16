import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Runs the scripted simulation that stands in for the staff and patients of the
 * clinic, and prints the results to the console.
 *
 * <p>There is no menu and no user interface, and nothing is saved: this class
 * simply creates the objects, calls their behaviour and prints what happens,
 * which is how the design of the other classes is shown to work. Part 3 builds
 * and prints the clinic's health professionals, Part 4 models a single
 * appointment and shows that appointments can be ordered, and Part 6 runs the
 * appointment manager end to end, including the operations that must be
 * refused.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class AssignmentOne {

    /** The day the simulated clinic is running on. All bookings are for this day. */
    private static final Weekday TODAY = Weekday.WEDNESDAY;

    /**
     * Runs the demonstration.
     *
     * @param args command line arguments, which are not used
     */
    public static void main(String[] args) {
        // ------------------------------------------------------------------
        // Part 3 - the health professionals of the clinic
        // ------------------------------------------------------------------
        // Eight professionals of five different types. Some are given an
        // explicit roster, others rely on the default Monday to Friday week,
        // which shows the second constructor of each class in use.
        GeneralPractitioner drChen = new GeneralPractitioner("1042", "Dr Mia Chen", "281334",
                Weekday.WEDNESDAY, Weekday.FRIDAY);
        GeneralPractitioner drBaker = new GeneralPractitioner("1078", "Dr Owen Baker", "290117");
        GeneralPractitioner drRaman = new GeneralPractitioner("1091", "Dr Priya Raman", "301882",
                Weekday.MONDAY, Weekday.TUESDAY, Weekday.WEDNESDAY);

        Nurse nurseGrace = new Nurse("2015", "Nurse Grace Whitfield", "Ward 4C",
                Weekday.THURSDAY, Weekday.FRIDAY);
        Nurse nurseFoster = new Nurse("2020", "Nurse Liam Foster");

        Physiotherapist physioSantos = new Physiotherapist("3011", "Physiotherapist Ruby Santos",
                "musculoskeletal rehabilitation", Weekday.WEDNESDAY, Weekday.FRIDAY);

        Radiographer radiographerNowak = new Radiographer("4012", "Radiographer Tomas Nowak",
                "CT", Weekday.WEDNESDAY);

        Pharmacist pharmacistBello = new Pharmacist("5007", "Pharmacist Aisha Bello", "771234",
                Weekday.WEDNESDAY, Weekday.THURSDAY, Weekday.FRIDAY);

        // All five types are stored in one list of the general type, which is
        // what allows them to be processed together: nothing below needs to
        // know which concrete type it is looking at.
        List<HealthProfessional> team = new ArrayList<>();
        team.add(drChen);
        team.add(drBaker);
        team.add(drRaman);
        team.add(nurseGrace);
        team.add(nurseFoster);
        team.add(physioSantos);
        team.add(radiographerNowak);
        team.add(pharmacistBello);

        printTitle("PART 3 - THE HEALTH PROFESSIONALS OF THE CLINIC");
        printTeam(team);

        // ------------------------------------------------------------------
        // Part 4 - modelling a single appointment
        // ------------------------------------------------------------------
        Patient alice = new Patient("Alice Nguyen", "0412345678");
        Patient ben = new Patient("Ben Carter", "0422334455");
        Patient cathy = new Patient("Cathy Wu", "0433556677");
        Patient daniel = new Patient("Daniel Osei", "0445667788");
        Patient erin = new Patient("Erin Blake", "0456778899");
        Patient frank = new Patient("Frank Doyle", "0467889900");

        printTitle("PART 4 - MODELLING ONE APPOINTMENT");
        Appointment single = new Appointment(alice, drChen, TODAY, new ClockTime(9, 0));
        System.out.println("An appointment joins a patient, a professional, a day and a time:");
        System.out.println(single);
        System.out.println();

        // An appointment with a professional who is not rostered today cannot
        // be built at all, which is the constructor protecting the object from
        // an invalid state.
        System.out.println("Trying to build an invalid appointment on purpose:");
        try {
            new Appointment(cathy, nurseGrace, TODAY, new ClockTime(10, 0));
            System.out.println("  (no exception, which should not happen)");
        } catch (IllegalArgumentException exception) {
            System.out.println("  Refused by the Appointment constructor: " + exception.getMessage());
        }
        System.out.println();

        // Three appointments created out of order, then sorted through the
        // ordering each appointment knows about itself.
        printSubTitle("Three appointments in the order they happen to be created");
        List<Appointment> trial = new ArrayList<>();
        trial.add(new Appointment(cathy, physioSantos, TODAY, new ClockTime(13, 0)));
        trial.add(new Appointment(alice, drChen, TODAY, new ClockTime(9, 0)));
        trial.add(new Appointment(ben, nurseFoster, TODAY, new ClockTime(10, 30)));
        printAppointments(trial);

        printSubTitle("The same three appointments after Collections.sort(...)");
        Collections.sort(trial);
        printAppointments(trial);

        // ------------------------------------------------------------------
        // Part 6 - the appointment system end to end
        // ------------------------------------------------------------------
        printTitle("PART 6 - THE APPOINTMENT SYSTEM END TO END");
        AppointmentManager manager = new AppointmentManager(new DailyTimetable(), TODAY);
        System.out.println("Clinic timetable : " + manager.getTimetable().describe());
        System.out.println("Bookings are for : " + manager.getToday() + " only");
        List<ClockTime> slots = manager.getTimetable().getSlots();
        System.out.println("Slots today      : " + slots.size() + " slots, from "
                + slots.get(0) + " to " + slots.get(slots.size() - 1));

        printSubTitle("6.1 Booking appointments");
        // The two bookings for Dr Chen are made out of time order on purpose:
        // section 6.3 uses them to show a filtered list being sorted.
        System.out.println(manager.book(alice, drChen, new ClockTime(15, 30)));
        System.out.println(manager.book(ben, drRaman, new ClockTime(9, 0)));
        System.out.println(manager.book(cathy, nurseFoster, new ClockTime(10, 30)));
        System.out.println(manager.book(daniel, physioSantos, new ClockTime(11, 0)));
        System.out.println(manager.book(alice, drChen, new ClockTime(9, 0)));
        System.out.println(manager.book(erin, radiographerNowak, new ClockTime(14, 0)));
        System.out.println(manager.book(ben, pharmacistBello, new ClockTime(13, 30)));
        System.out.println(manager.book(frank, drBaker, new ClockTime(8, 0)));
        System.out.println("Appointments held: " + manager.getAppointmentCount());

        printSubTitle("6.2 All appointments, from earliest to latest");
        printAppointments(manager.getAllAppointments());

        printSubTitle("6.3 Filtering by health professional ID, then sorting the subset");
        List<Appointment> chenList = manager.findByProfessionalId("1042");
        System.out.println("Appointments found for ID 1042: " + chenList.size()
                + " (in the order they were booked)");
        printAppointments(chenList);
        manager.sortInTimeOrder(chenList);
        System.out.println("The same subset after sortInTimeOrder(...):");
        printAppointments(chenList);

        printSubTitle("6.4 Filtering by health professional object");
        List<Appointment> fosterList = manager.findByProfessional(nurseFoster);
        System.out.println("Appointments found for " + nurseFoster.toSummaryLine() + ": "
                + fosterList.size());
        printAppointments(fosterList);

        printSubTitle("6.5 Filtering by patient mobile number");
        List<Appointment> aliceList = manager.findByPatientMobile("0412345678");
        System.out.println("Appointments found for 0412 345 678: " + aliceList.size());
        printAppointments(aliceList);

        printSubTitle("6.6 Operations that cannot be completed");
        System.out.println(manager.book(alice, drChen, new ClockTime(9, 0)));
        System.out.println(manager.book(frank, nurseFoster, new ClockTime(7, 30)));
        System.out.println(manager.book(daniel, drRaman, new ClockTime(12, 15)));
        System.out.println(manager.book(cathy, nurseGrace, new ClockTime(10, 0)));
        System.out.println(manager.book(null, drBaker, new ClockTime(11, 0)));
        System.out.println(manager.cancel("9999", new ClockTime(9, 0)));
        System.out.println(manager.cancelByPatientMobile("0499999999"));
        System.out.println("Appointments held: " + manager.getAppointmentCount()
                + " (unchanged - nothing invalid was added or removed)");

        printSubTitle("6.7 Cancelling appointments by the three supported routes");
        System.out.println(manager.cancel("1042", new ClockTime(9, 0)));
        System.out.println(manager.cancel(drChen, new ClockTime(15, 30)));
        System.out.println(manager.cancelByPatientMobile("0433556677"));
        System.out.println("Appointments held: " + manager.getAppointmentCount());

        printSubTitle("6.8 All appointments after the cancellations");
        printAppointments(manager.getAllAppointments());

        printSubTitle("6.9 Appointments for health professional 1042 after the cancellations");
        printAppointments(manager.findByProfessionalId("1042"));

        System.out.println();
        System.out.println("End of the demonstration.");
    }

    /**
     * Prints every professional in the team, with all of their details and the
     * service they deliver to a sample patient.
     *
     * <p>One loop covers all five types. The details and the service both come
     * from the object itself, so each professional answers in its own way
     * through the same two calls.</p>
     *
     * @param team the professionals to print
     */
    private static void printTeam(List<HealthProfessional> team) {
        Patient samplePatient = new Patient("Sam Taylor", "0498765432");
        for (int index = 0; index < team.size(); index++) {
            HealthProfessional professional = team.get(index);
            System.out.println((index + 1) + ". " + professional);
            System.out.println("    Service given: " + professional.deliverService(samplePatient));
            System.out.println();
        }
        System.out.println("Health professionals in the team: " + team.size());
    }

    /**
     * Prints the appointments supplied, in the order they are supplied.
     *
     * @param appointments the appointments to print
     */
    private static void printAppointments(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("(no appointments)");
            return;
        }
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
            System.out.println();
        }
        System.out.println("Appointments shown: " + appointments.size());
    }

    /**
     * Prints a section heading.
     *
     * @param title the heading to print
     */
    private static void printTitle(String title) {
        System.out.println();
        System.out.println("================================================================");
        System.out.println(title);
        System.out.println("================================================================");
    }

    /**
     * Prints a sub heading inside a section.
     *
     * @param title the sub heading to print
     */
    private static void printSubTitle(String title) {
        System.out.println();
        System.out.println("-- " + title + " --");
    }
}
