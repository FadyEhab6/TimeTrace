# Software Requirements Specification (SRS)

## TimeTrace --- Personal Time Tracking and Analytics Application

**Version:** 1.1 --- Draft\
**Platform:** Android\
**Programming Language:** Kotlin\
**Local Storage:** Room (SQLite) and app-private file storage

------------------------------------------------------------------------

## 1. Introduction

### 1.1 Purpose

TimeTrace is an Android application that helps users track, organize,
and analyze how they spend their time across categories. Users can
create manual entries or use a timer, review entries chronologically,
and view time summaries and reports.

### 1.2 Scope

TimeTrace provides: - Local user registration, login, and logout. - A
Profile screen where users can select and save a profile photo. -
Category creation, editing, deletion, and time summaries. - Manual
time-entry creation. - Timer-based tracking with pause, resume, and
stop. - Home Dashboard, Timeline, Reports, Categories, Profile, and
Settings screens. - Local storage of accounts, profile information,
categories, time entries, and timer state.

**All core application data is stored locally on the device.** This
version does not use a remote backend, cloud database, or cloud
synchronization. Data is not automatically transferred between devices.
Automatic device-usage monitoring, AI recommendations, and predictive
analytics are outside the scope.

### 1.3 Intended Audience

The student development team and project supervisor/evaluators.

### 1.4 Definitions

  -----------------------------------------------------------------------
  Term                                Definition
  ----------------------------------- -----------------------------------
  Category                            A user-defined group, such as
                                      Study, Work, or Health.

  Time Entry                          A recorded time period assigned to
                                      a category and given a required
                                      title.

  Manual Entry                        An entry created by specifying
                                      start and end times.

  Timer Session                       A session recorded using the
                                      application's timer.

  Timeline                            A chronological view of saved time
                                      entries.

  Tracked Time                        The duration attributed to saved
                                      time entries.

  Custom Date Range                   A selected start date and end date
                                      used to filter results.

  Local Storage                       Data saved on the device rather
                                      than a remote server.
  -----------------------------------------------------------------------

------------------------------------------------------------------------

## 2. Overall Description

### 2.1 Product Perspective

TimeTrace is a standalone Android application. User accounts, profile
information, categories, time entries, and timer-session state are
stored on the device. Home, Timeline, Reports, and Categories use the
same locally stored time-entry data for their summaries.

### 2.2 User Classes

The application supports an individual user who creates a local account
and manages their own profile, categories, and time entries. The initial
version does not support shared workspaces or multi-device
synchronization.

### 2.3 Operating Environment

-   Android smartphone.
-   Kotlin and Android Studio.
-   Native Android user interface.
-   Room database backed by SQLite.
-   App-private storage for profile photos.

The minimum supported Android version will be decided during
implementation.

### 2.4 Design and Implementation Constraints

1.  The application must be implemented for Android using Kotlin.
2.  Core data must be stored locally; no remote backend or cloud
    synchronization is required.
3.  Every saved time entry must have a required title and belong to a
    category.
4.  No separate Activity entity will be introduced; a category can
    contain multiple titled time entries.
5.  Manual and timer-based entries must be used consistently by
    Timeline, Reports, Home, and Categories.
6.  Passwords must never be stored as plain text.

### 2.5 Assumptions and Dependencies

-   The user has an Android device with sufficient local storage.
-   The user creates a local account on the device.
-   Clearing app data or uninstalling the app may remove local data
    unless a separate backup is made.
-   The user selects a profile photo through an Android-supported photo
    picker.
-   Dates and times are displayed using the device's local time zone.

------------------------------------------------------------------------

## 3. System Features and Functional Requirements

### 3.1 Local Registration and Login

**FR-01 --- User Registration:** The system shall allow a user to create
a local account using the registration fields defined by the
application.

**FR-02 --- Local Login:** The system shall allow a registered user to
log in using locally stored account credentials.

**FR-03 --- Input Validation:** The system shall validate registration
and login fields and display understandable messages when input is
invalid or authentication fails.

**FR-04 --- Session Management:** The system shall maintain the
signed-in state as appropriate and allow the user to log out.

**FR-05 --- Offline Authentication:** Registration and login shall not
require an internet connection or remote authentication service.

**FR-06 --- Credential Protection:** The system shall not store
passwords as plain text and shall use an appropriate password-hashing
approach.

**FR-07 --- Account Data Separation:** If multiple local accounts are
supported on one device, the system shall associate each account's
profile, categories, and time entries with the correct local user
identifier.

### 3.2 Home Dashboard

**FR-08 --- Daily Summary:** The system shall display the total tracked
duration for the current day based on saved time entries.

**FR-09 --- Active Timer Status:** The system shall display an active
timer's category, title, and elapsed tracked time, if one exists.

**FR-10 --- Add Time Entry:** The system shall open the Add Time Entry
form as a popup or bottom sheet from Home.

**FR-11 --- Start Tracking:** The system shall provide an action to
begin a timer-based session from Home.

**FR-12 --- Today's Entries:** The system shall display a summary of the
current day's entries and provide navigation to Timeline.

### 3.3 Categories Management

**FR-13 --- Display Categories:** The system shall display categories
belonging to the currently signed-in local account.

**FR-14 --- Add Category:** The system shall allow the user to create a
category by entering a name.

**FR-15 --- Edit Category:** The system shall allow the user to change
an existing category's name.

**FR-16 --- Delete Category:** The system shall request confirmation
before deleting a category. The treatment of associated time entries
must be decided before implementation.

**FR-17 --- Category Time Totals:** The system shall display the total
duration of entries associated with each category for the selected
period.

**FR-18 --- Category Date Filters:** The Categories screen shall provide
Today, This week, This month, and Custom date range filters.

**FR-19 --- Custom Date Range:** The system shall allow the user to
select a start date and end date.

**FR-20 --- Preserve Association on Rename:** Renaming a category shall
not break its association with existing time entries.

### 3.4 Manual Time Entry

**FR-21 --- Add Manual Entry:** The system shall allow the user to
create a manual entry through the Add Time Entry form.

**FR-22 --- Required Fields:** Every time entry shall have a title,
category, start date/time, and end date/time.

**FR-23 --- Duration Calculation:** The system shall calculate
manual-entry duration from the start and end timestamps.

**FR-24 --- Validation:** The system shall reject missing required
fields, invalid timestamps, and an end time that does not occur after
the start time.

**FR-25 --- Save Locally:** The system shall save valid entries locally
and make them available to Home, Timeline, Reports, and Categories.

**FR-26 --- Cancel:** The system shall allow the user to cancel entry
creation without saving.

### 3.5 Timer-Based Tracking

**FR-27 --- Start Timer:** The system shall allow the user to select a
category, enter a required title, and start a timer session.

**FR-28 --- Timer Navigation:** After starting tracking from Home, the
system shall display the Timer screen.

**FR-29 --- Elapsed Time:** The Timer screen shall display elapsed
tracked time.

**FR-30 --- Pause and Resume:** The system shall allow the user to pause
and resume an active timer.

**FR-31 --- Stop Timer:** The system shall allow the user to stop the
active timer.

**FR-32 --- Save Timer Session:** On stopping, the system shall save the
completed session locally as a time entry containing its category,
title, start time, end time, and duration.

**FR-33 --- Exclude Paused Time:** Timer duration shall exclude
intervals during which the timer was paused.

**FR-34 --- Active Session Visibility:** The Home Dashboard shall
provide access to the active session's status while tracking is in
progress.

**FR-35 --- Restore Timer State:** The system shall store sufficient
timer state locally to recover an active or paused session after
ordinary navigation or app recreation. Behavior after force-stop or
device restart shall be verified during implementation.

**FR-36 --- One Active Session:** The system shall prevent multiple
simultaneous active timer sessions for the same local account.

### 3.6 Timeline

**FR-37 --- Display Timeline:** The system shall display saved entries
chronologically, organized by date.

**FR-38 --- Date Navigation:** The system shall allow the user to
navigate between dates.

**FR-39 --- Entry Details:** The system shall display an entry's title,
category, start time, end time, and duration when selected.

**FR-40 --- Edit Entry:** The user shall be able to edit an entry from
Timeline using the Add Time Entry form with existing values prefilled.

**FR-41 --- Delete Entry:** The system shall allow the user to delete an
entry after an appropriate confirmation step.

**FR-42 --- Refresh Summaries:** Creating, editing, or deleting an entry
shall update relevant Home, Timeline, Reports, and Categories results.

### 3.7 Reports

**FR-43 --- Reports Screen:** The system shall provide a separate
Reports screen.

**FR-44 --- Overall Tracked Time:** The system shall display total
tracked time for the selected reporting period.

**FR-45 --- Category Breakdown:** The system shall display the
distribution of tracked time across categories.

**FR-46 --- Visual Reports:** The system shall provide charts or other
graphical representations of tracked time.

**FR-47 --- Reporting Periods:** The system shall support daily, weekly,
monthly, and custom reporting periods.

**FR-48 --- Consistent Calculations:** Report totals shall be calculated
from local time entries using the same duration and date-filtering rules
as other screens.

### 3.8 Profile

**FR-49 --- Display Profile:** The system shall provide a Profile screen
showing the signed-in local account's available profile information.

**FR-50 --- Select Profile Photo:** The system shall allow the user to
select a photo using an Android-supported photo-selection interface.

**FR-51 --- Store Photo Locally:** The system shall store the selected
photo in app-private local storage, or maintain a stable local reference
to a copied photo.

**FR-52 --- Display Photo:** The system shall display the saved profile
photo on the Profile screen and in other relevant places that show the
user's profile.

**FR-53 --- Replace Photo:** The system shall allow the user to replace
their current profile photo.

**FR-54 --- Photo Error Handling:** If selecting or saving a photo
fails, the system shall display an appropriate message and preserve the
existing photo where possible.


### 3.9 Empty States and Error Handling

**FR-58 --- Empty Timeline:** If no entries exist for the selected date,
the system shall display an empty state and an action to begin tracking.

**FR-59 --- Empty Categories:** If no categories exist, the system shall
display an empty state and an action to create a category.

**FR-60 --- Validation Messages:** The system shall display clear
messages for invalid input.

**FR-61 --- Local Storage Errors:** The system shall inform the user
when a local save, update, delete, or retrieval operation fails.

**FR-62 --- Processing Feedback:** The system shall show appropriate
feedback for operations that require noticeable processing time.

------------------------------------------------------------------------

## 4. External Interface Requirements

### 4.1 User Interface

The application shall use consistent labels, recognizable icons, and
predictable navigation. Main navigation shall provide access to Home,
Timeline, Reports, Categories, and Settings. The Timer screen shall be
reached from Home and shall not be a main-navigation destination. Add
Time Entry shall open as a popup or bottom sheet from Home; the same
form shall be reused to edit an entry from Timeline. The Profile screen
shall be accessible from Settings or the user's profile area.

### 4.2 Authentication Interface

Registration and login shall use the application's local account data
and shall not depend on a remote authentication provider.

### 4.3 Local Data Interface

The application shall use a local database, such as Room backed by
SQLite, for accounts, categories, time entries, and timer-session state.
Profile photos shall be stored in app-private device storage; the
database may store a local path or reference to the saved image.

### 4.4 Device Interface

The application shall use device date/time information for time entries
and an Android-supported photo picker to select profile photos. It shall
not require continuous location tracking or automatic monitoring of
other applications.

------------------------------------------------------------------------

## 5. Data Requirements

### 5.1 Local User Account

  -----------------------------------------------------------------------
  Attribute                           Description
  ----------------------------------- -----------------------------------
  userId                              Unique local account identifier.

  name                                User's display name.

  loginIdentifier                     Local login identifier, such as
                                      username or email, based on the
                                      final registration design.

  passwordHash                        Password verifier created using an
                                      appropriate password-hashing
                                      method; never the plain-text
                                      password.

  profilePhotoPath                    Optional local path or reference to
                                      the saved profile photo.
  -----------------------------------------------------------------------

### 5.2 Category

  Attribute    Description
  ------------ ---------------------------------------
  categoryId   Unique category identifier.
  userId       Local account that owns the category.
  name         Required category name.
  color        Optional display color.

Each category belongs to one local account and can be associated with
multiple time entries.

### 5.3 Time Entry

  -----------------------------------------------------------------------
  Attribute                           Description
  ----------------------------------- -----------------------------------
  entryId                             Unique time-entry identifier.

  userId                              Local account that owns the entry.

  categoryId                          Identifier of the associated
                                      category.

  title                               Required title describing the
                                      session.

  startTime                           Start timestamp.

  endTime                             End timestamp for a completed
                                      entry.

  duration                            Calculated tracked duration,
                                      excluding paused intervals for
                                      timer-based entries.

  source                              Whether the entry was created
                                      manually or through the timer.
  -----------------------------------------------------------------------

Timer state must also be represented locally so active or paused
sessions can be restored under supported conditions.

### 5.4 Data Integrity Rules

1.  Every account, category, and time entry must have a unique
    identifier.
2.  Each category and entry must be associated with the correct local
    account.
3.  Every saved entry must have a non-empty title and a valid category.
4.  Completed entries must have valid start and end timestamps, with the
    end after the start.
5.  Timer durations must exclude paused intervals.
6.  Totals must be updated consistently after entry changes.
7.  Profile-photo references must point to files available in
    app-private storage.
8.  Core application data must not depend on a remote service.

### 5.5 Persistence and Limitations

Structured data shall be stored in a local database, and profile images
shall be stored in app-private storage. This version does not upload
data or synchronize between devices. Data may be lost if application
data is cleared or the application is uninstalled. Backup and restore
are outside the initial scope unless added later.

------------------------------------------------------------------------

## 6. Non-Functional Requirements

### 6.1 Performance

**NFR-01:** Common interface actions should respond promptly under
normal device conditions.\
**NFR-02:** The timer display should update smoothly without a database
write for every displayed second.\
**NFR-03:** The application should retrieve or calculate only the data
needed for the selected screen and period where practical.

### 6.2 Reliability

**NFR-04:** Saved entries shall retain consistent timestamps and
durations.\
**NFR-05:** Timer calculations shall remain accurate when paused and
resumed.\
**NFR-06:** Failed local data operations shall not be presented as
successful.\
**NFR-07:** Invalid or unavailable profile-photo files shall be handled
without crashing.

### 6.3 Usability

**NFR-08:** Navigation and terminology shall be consistent across
screens.\
**NFR-09:** Required fields and validation errors shall be clear.\
**NFR-10:** Deleting entries or categories shall require appropriate
confirmation or warning.\
**NFR-11:** Empty states shall be understandable and provide a useful
next action.

### 6.4 Security and Privacy

**NFR-12:** Passwords shall never be stored as plain text.\
**NFR-13:** Account data and profile photos shall use appropriate
app-private storage mechanisms.\
**NFR-14:** The initial version shall not transmit credentials, profile
photos, categories, or time entries to a remote service.\
**NFR-15:** The application shall make clear that data is stored on the
device and is not automatically synchronized or backed up by TimeTrace.

### 6.5 Maintainability

**NFR-16:** The application shall separate UI logic, time-tracking
logic, and data-access responsibilities.\
**NFR-17:** Shared forms and calculation logic should be reused where
appropriate, especially for adding and editing entries.

### 6.6 Compatibility

**NFR-18:** The application shall run on the agreed minimum supported
Android version. The minimum version shall be established before
implementation is finalized.

------------------------------------------------------------------------

## 7. Main User Workflows

### 7.1 Registration and Login

1.  The user opens TimeTrace.
2.  The user creates a local account or enters existing credentials.
3.  The application validates the input against local account data.
4.  On success, the user is directed to Home.

### 7.2 Profile Photo Update

1.  The user opens Profile.
2.  The user selects the option to add or change the profile photo.
3.  Android displays the photo-selection interface.
4.  The user selects a photo.
5.  The application copies the photo to app-private storage and saves
    its local reference.
6.  The Profile screen displays the saved photo.

### 7.3 Manual Time Entry

1.  The user opens Home and selects Add Time Entry.
2.  A popup or bottom sheet appears.
3.  The user selects a category and enters a title.
4.  The user enters start and end times.
5.  The application validates the fields and calculates duration.
6.  The user saves the entry.
7.  The application stores it locally and refreshes relevant summaries.

### 7.4 Timer-Based Tracking

1.  The user selects Start Tracking from Home.
2.  The user selects a category, enters a title, and starts the timer.
3.  The Timer screen opens.
4.  The user may pause, resume, and stop the timer.
5.  The application calculates duration excluding pauses and saves the
    completed entry locally.
6.  The entry becomes available in Timeline and Reports.

### 7.5 Editing a Time Entry

1.  The user opens Timeline and selects an entry.
2.  The user chooses Edit.
3.  The Add Time Entry form opens with existing values prefilled.
4.  The user updates the fields and saves.
5.  The application validates and updates the local record and related
    summaries.

### 7.6 Managing Categories

1.  The user opens Categories.
2.  The application displays categories and their tracked-time totals.
3.  The user selects a period or custom date range.
4.  The user can add a category, edit its name, or request deletion.
5.  The application applies the operation and refreshes the screen.

### 7.7 Viewing Reports

1.  The user opens Reports and selects a period.
2.  The application retrieves relevant local entries.
3.  The application calculates totals and category breakdowns.
4.  The application displays summaries and charts.

------------------------------------------------------------------------

## 8. Business Rules

**BR-01:** Every time entry must have a title and category.\
**BR-02:** A category can contain multiple time entries.\
**BR-03:** A category is not a time entry; no separate Activity entity
is required.\
**BR-04:** Manual and timer-based entries shall be represented
consistently.\
**BR-05:** Paused timer intervals shall not contribute to tracked
duration.\
**BR-06:** Home, Timeline, Reports, and Categories shall use the same
locally saved time-entry data.\
**BR-07:** Category and report totals shall respect the selected date
period.\
**BR-08:** Each local account's records shall remain associated with
that account.\
**BR-09:** The initial version shall not require AI, machine learning,
automatic app-usage monitoring, remote storage, cloud synchronization,
or predictive analytics.\
**BR-10:** Profile photos shall be selected by the user and stored
locally.

------------------------------------------------------------------------

## 9. Acceptance Criteria

The initial release will be considered functionally complete when:

1.  A user can register, log in, and log out without a remote
    authentication service.
2.  Core data remains available after closing and reopening the
    application under normal conditions.
3.  A user can select, save, view, and replace a profile photo.
4.  A user can create, rename, and request deletion of categories.
5.  Categories displays the total tracked time for each category and
    supports Today, This week, This month, and Custom date filters.
6.  A user can create a manual entry with a required title, category,
    start time, and end time.
7.  Invalid or incomplete entries are rejected with understandable
    messages.
8.  A user can start, pause, resume, and stop a timer session.
9.  Timer duration calculations exclude paused intervals.
10. Completed entries appear chronologically in Timeline.
11. A user can edit and delete an entry from Timeline.
12. Changes to entries are reflected in relevant summaries and reports.
13. Reports display total tracked time and category distribution for the
    selected period.
14. Categories and Profile are separate from Settings.
15. Core account, profile, category, and time-entry data is stored
    locally and is not sent to a remote service.
16. The application handles empty states and local storage errors
    appropriately.

------------------------------------------------------------------------

## 10. Items Requiring Final Confirmation

1.  **Category deletion:** Should deleting a category also delete its
    entries, preserve them in an uncategorized group, or prevent
    deletion while entries exist?
2.  **Timer persistence:** What should happen if the application is
    force-stopped or the device restarts during a timer session?
3.  **Overlapping entries:** Should entries be allowed to cover the same
    time period, or should overlaps be prevented?
4.  **Reports:** Should Reports use the same period filters as
    Categories or have separate controls?
5.  **Profile fields:** Besides the photo, which fields should the user
    be able to edit, such as display name or login identifier?
6.  **Local registration:** Which fields are required for registration
    and login?
7.  **Local account deletion:** Should the app include an option to
    delete the local account and all its records?
8.  **Minimum Android version:** Which Android API level should be
    supported?

These decisions remain open until confirmed by the team.

------------------------------------------------------------------------

**End of SRS Draft --- Version 1.1**

