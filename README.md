# A1-XinyangDong – Health Service Appointment System

**Unit:** PROG2004 Object Oriented Programming
**Assessment:** Assessment 1 (Portfolio/Programming, 40%)
**Student:** Xinyang Dong (25258567)
**Repository:** <https://github.com/Serein0306/A1-XinyangDong>

A small appointment system for a health service, built with Java. There is no
user interface and nothing is saved: the project is the set of collaborating
classes a real system would be built on, and `AssignmentOne` runs a scripted
simulation that creates the objects, exercises their behaviour and prints the
results to the console.

---

## How to compile and run

From the project root (the folder containing `src`):

```bash
javac -d out src/*.java
java -cp out AssignmentOne
```

The project was checked with:

```bash
javac -Xlint:all --release 11 -d out src/*.java
```

which compiles with **no warnings** and runs on Java 11 or later.

A copy of the console output produced by the run is in `console-output.txt`.

---

## Classes and responsibilities

| File | Responsibility |
|---|---|
| `AssignmentOne.java` | `main` method: runs the Part 3, Part 4 and Part 6 demonstrations |
| `HealthProfessional.java` | Abstract general health professional: ID, name, weekly roster, printing, equality, validation, and the abstract behaviour every specialty must provide |
| `ProfessionalType.java` | Enum of the kinds of professional, so each type's printed label exists in exactly one place |
| `GeneralPractitioner.java` | GP: Medicare provider number, `prescribe(...)` |
| `Nurse.java` | Nurse: ward, `takeBloodSample(...)` |
| `Physiotherapist.java` | Physiotherapist: focus area, `designExerciseProgramme(...)` |
| `Radiographer.java` | Radiographer: imaging modality, `performScan(...)` |
| `Pharmacist.java` | Pharmacist: licence number, `dispense(...)` |
| `Patient.java` | A patient: name and mobile number, held together as one object |
| `Appointment.java` | One booking: patient + professional + day + time; `Comparable` so it sorts by time |
| `AppointmentManager.java` | Holds the day's bookings: add, filter, sort, cancel, all with warnings |
| `OperationResult.java` | The outcome of an operation: success or refusal, with a message |
| `DailyTimetable.java` | The fixed daily schedule (08:00–16:00 in 30 minute slots), built from a rule |
| `ClockTime.java` | A time of day stored as numbers, so ordering is correct |
| `Weekday.java` | Enum of the days of the week, with short labels |

---

## Design decisions by part

### Part 1 – the general health professional

* **Shared data only.** The base class holds an ID (digits only), a name and the
  fixed set of days the professional works. Nothing specialty specific lives
  here.
* **Controlled access.** Every field is `private final` with no setters, all
  validation happens once in the constructor, and `getWorkingDays()` returns a
  copy so a caller cannot edit the roster through the collection it received.
  An object therefore cannot be put into an invalid state from outside.
* **Readable printing.** `toString()` prints the type, ID, name, roster and the
  specialty details supplied by the subclass, so one `System.out.println(...)`
  shows the whole professional.
* **Two constructors.** A full constructor taking the roster, and a shorter one
  that defaults to Monday–Friday when the caller does not care.

### Part 2 – the specialised types

* **Inheritance, not duplication.** Five concrete subclasses extend the base
  class, so the ID, name, roster, printing, equality and validation are written
  once.
* **Three abstract methods** (`getProfessionalType()`, `getSpecialistDetails()`,
  `deliverService(...)`) force every subclass to say what it is, what extra data
  it holds and what it does for a patient. The base class is `abstract`, so a
  "plain" professional with no specialty cannot be created at all.
* **Each subclass also offers two constructors**, so a caller can supply the
  specialty data with or without an explicit roster (the nurse, physiotherapist
  and radiographer also default their specialty attribute).
* **Unique behaviour per type:** GPs prescribe, nurses take blood samples,
  physiotherapists design exercise programmes, radiographers perform scans,
  pharmacists dispense.

### Part 3 – working with the objects together

* All five types go into one `ArrayList<HealthProfessional>` and are printed,
  described and exercised by **one loop and two calls**, which is what makes the
  design worth the inheritance.
* Adding a sixth type would mean one new subclass and one `add(...)` line — no
  existing printing or processing code would change.

### Part 4 – modelling an appointment

* **The patient is its own class** because a name and a number describe one
  person, need one place to be validated, and one place to decide how a number
  prints (digits stored, spaces added only for display).
* An appointment holds a `Patient`, a `HealthProfessional`, a `Weekday` and a
  `ClockTime`, so it works with **any** specialty without knowing which one it
  has.
* **No incomplete appointments:** the constructor rejects a missing detail, and
  it also rejects a booking with a professional who is not rostered that day,
  so an unbookable appointment can never exist.
* **Ordering:** `compareTo` compares the time of day (then the professional's ID
  only to make same-time output repeatable), so `Collections.sort(...)` works on
  a whole collection.
* **Time is stored as numbers** (`ClockTime` keeps hour/minute and a computed
  minutes-since-midnight) because text such as "9:00" versus "11:00" does not
  sort correctly.

### Part 5 – managing the collection

* **`ArrayList`** is the right structure: the clinic keeps adding, cancelling and
  walking through bookings, and never looks one up by a key, so a map would only
  add complexity.
* The timetable is stored as a **rule** (start, end, slot length) and the slots
  are generated from it, so changing the clinic to 08:30–17:00 in 15 minute
  slots is a one line change in `AssignmentOne`.
* **Every refusal goes through `OperationResult`** — a double booking, a time
  outside the timetable, a professional not working that day, an appointment
  that does not exist. The manager never prints; the caller decides what to do
  with the message.
* **Double booking is prevented** by comparing the professional and the time;
  two professionals with the same ID are the same professional because
  `HealthProfessional.equals/hashCode` are based on the ID.
* Sorting is one `Collections.sort(...)` over the filtered list as well as the
  full list.

### Part 6 – the demonstration

`AssignmentOne` shows, in order: booking eight appointments across all five
types; the whole day in time order; filtering by professional ID **and** by
professional object; filtering by patient mobile number; sorting a filtered
subset that was booked out of time order; five refusals (double booking, a time
before opening, a time between slots, a professional not rostered today, a
missing patient, plus cancelling an appointment that does not exist); cancelling
through all three routes; and the collection afterwards.

---

## Where the four pillars of OOP appear

| Pillar | Where | Why |
|---|---|---|
| **Encapsulation** | `HealthProfessional` (private final fields, validation in the constructor, `getWorkingDays()` returns a copy), `Appointment`, `Patient` | An object can be read but never pushed into an invalid state |
| **Inheritance** | `GeneralPractitioner`, `Nurse`, `Physiotherapist`, `Radiographer`, `Pharmacist` extending `HealthProfessional` | Shared data and code written once, not five times |
| **Polymorphism** | `deliverService(...)`, `getSpecialistDetails()`, `getProfessionalType()` called on `HealthProfessional` references in `AssignmentOne.printTeam(...)` and in `Appointment` | One call, five different behaviours; the loop does not care which type it holds |
| **Abstraction** | `abstract class HealthProfessional` plus the three abstract methods; `Comparable` in `Appointment` | Hides the shared implementation, exposes only what each type must do; callers just say "sort" |

---

## Concepts used (Modules 1–3)

Classes and objects, constructors and constructor overloading, `private`/`final`,
getters, validation, `this`, `static` constants and helpers, inheritance,
`abstract` classes and methods, method overriding, polymorphism, `toString()`,
`equals`/`hashCode`, `Comparable`/`compareTo`, `enum`, `ArrayList`, `List`,
`Iterator`, `Collections.sort`, exception handling (`IllegalArgumentException`,
`try`/`catch`), `StringBuilder`, Javadoc and inline comments.

---

## GenAI declaration

This assessment is GenAI Level 2: AI tools may not write the solution code or
its comments, and may not create the video. Complete the declaration in the
submission comments honestly, and be ready to explain every line of the code and
every design decision in the video and, if asked, in an interview.

---

## Submission checklist

See `SUBMISSION-CHECKLIST.md` in the same folder for the full list of what still
has to be done outside the code (GitHub history, WakaTime report, video, the
GenAI statement, and the MySCU username in the project name).
