# Software Requirements Specification (SRS)

## TimeTrace --- Personal Time Tracking and Analytics Application

**Version:** 1.0 --- Draft\
**Project Type:** Mobile Application\
**Platform:** Android\

------------------------------------------------------------------------

## 1. Introduction

### 1.1 Purpose

TimeTrace is an Android mobile application designed to help users track,
organize, and analyze how they spend their time across different
categories of activities.

The application allows users to record time manually or through a live
timer, review their recorded sessions chronologically, and analyze the
time spent on different categories over selected periods.

This document defines the functional and non-functional requirements,
system scope, user interactions, data requirements, and technical
constraints of TimeTrace.

### 1.2 Scope

TimeTrace will provide the following capabilities:

-   User registration and authentication.
-   Creation and management of categories.
-   Manual time-entry creation.
-   Timer-based time tracking, including pause, resume, and stop
    operations.
-   A dashboard showing daily summaries and active tracking sessions.
-   A chronological timeline of recorded entries.
-   Editing and deleting existing time entries.
-   Reports summarizing tracked time by period and category.
-   Category-level time summaries with date filters.
-   Account and application settings.

The application focuses on **manual, user-controlled time tracking**.
Automatic device-usage monitoring, AI-based recommendations, and complex
predictive analytics are outside the initial scope.

### 1.3 Intended Audience

This document is intended for:

-   The student development team.
-   The project supervisor and evaluators.


### 1.4 Definitions

  -----------------------------------------------------------------------
  Term                                Definition
  ----------------------------------- -----------------------------------
  Category                            A user-defined group used to
                                      organize time entries, such as
                                      Study, Work, or Health.

  Time Entry                          A single recorded time period
                                      assigned to a category and
                                      identified by a required title.

  Manual Entry                        A time entry created by specifying
                                      its start and end times.

  Timer Session                       A time-tracking session recorded
                                      using the application's timer.

  Timeline                            A chronological view of saved time
                                      entries.

  Tracked Time                        The duration attributed to saved
                                      time entries.

  Custom Date Range                   A user-selected start date and end
                                      date used to filter results.
  -----------------------------------------------------------------------

------------------------------------------------------------------------

## 2. Overall Description

### 2.1 Product Perspective

TimeTrace is a standalone Android application with a cloud-backed data
layer.

The application will retrieve saved records to generate the Home
Dashboard, Timeline, Reports, and Categories summaries.

These screens will use the same underlying time-entry data to maintain
consistency across the application.

### 2.2 User Classes

The initial version supports one primary user type: an authenticated
application user.

An authenticated user can manage their own categories, create and manage
time entries, view reports, and change supported settings.

No administrator role or shared team workspace is required for the
initial version.

### 2.3 Operating Environment

-   **Mobile platform:** Android.
-   **Programming language:** Kotlin.
-   **Development environment:** Android Studio.
-   **User interface:** Native Android interface.
-   **Device:** Android smartphone.

The minimum supported Android version will be determined during
implementation.



------------------------------------------------------------------------

## 3. System Features and Functional Requirements

### 3.1 User Authentication

**FR-01 --- User Registration**\
The system shall allow a new user to create an account using the
supported registration fields.

**FR-02 --- User Login**\
The system shall allow a registered user to log in using their
credentials.

**FR-03 --- Authentication Validation**\
The system shall validate authentication attempts and display an
appropriate error message when authentication fails.

**FR-04 --- Logout**\
The system shall allow an authenticated user to log out through the
Settings screen.

### 3.2 Home Dashboard

**FR-05 --- Daily Tracked-Time Summary**\
The system shall display the total saved time entries' duration for the
current day.

**FR-06 --- Active Timer Status**\
The system shall display the active timer session, if one exists,
including its category, title, and elapsed tracked time.

**FR-07 --- Add Time Entry**\
The system shall provide an action that opens the Add Time Entry form as
a popup or bottom sheet from the Home Dashboard.

**FR-08 --- Start Tracking**\
The system shall provide an action that allows the user to begin a
timer-based session from the Home Dashboard.

**FR-9 --- Today's Entries**\
The system shall display a summary of the user's time entries for the
current day and provide navigation to the Timeline for further details.

### 3.3 Categories Management

**FR-10 --- Display Categories**\
The system shall display all categories belonging to the authenticated
user.

**FR-11 --- Add Category**\
The system shall allow the user to create a category by providing a
category name.

**FR-12 --- Edit Category**\
The system shall allow the user to modify an existing category's name.

**FR-13 --- Delete Category**\
The system shall allow the user to request deletion of a category and
shall display a confirmation dialog before carrying out the operation.

The final deletion behavior for existing time entries must be defined
before implementation.

**FR-14 --- Category Time Totals**\
The system shall display the total duration of saved time entries
associated with each category for the selected date period.

**FR-15 --- Category Date Filters**\
The Categories screen shall provide the following filters:

-   Today.
-   This week.
-   This month.
-   Custom date range.

**FR-16 --- Custom Date Range Selection**\
The system shall allow the user to select a start date and an end date
for a custom date range.

**FR-17 --- Category Entry Association**\
The system shall maintain the association between each time entry and
its category when the category is renamed.

### 3.4 Manual Time Entry

**FR-18 --- Add Manual Entry**\
The system shall allow the user to create a manual time entry through
the Add Time Entry form.

**FR-19 --- Required Entry Fields**\
Every time entry shall have:

-   A title.
-   A category.
-   A start date and time.
-   An end date and time.

**FR-20 --- Duration Calculation**\
The system shall calculate the duration of a manual entry from its start
and end timestamps.

**FR-21 --- Input Validation**\
The system shall reject entries with missing required fields, invalid
timestamps, or an end time that does not occur after the start time.

**FR-22 --- Save Entry**\
The system shall save a valid time entry and make it available to the
Timeline, Reports, Home Dashboard, and Categories summaries.

**FR-23 --- Cancel Entry Creation**\
The system shall allow the user to cancel entry creation without saving
the new record.

### 3.5 Timer-Based Tracking

**FR-24 --- Start Timer**\
The system shall allow the user to select a category, enter a required
title, and start a new timer session.

**FR-25 --- Timer Screen Navigation**\
After the user starts tracking from Home, the system shall display the
Timer screen.

**FR-26 --- Display Elapsed Time**\
The Timer screen shall display the elapsed tracked time for the active
session.

**FR-27 --- Pause Timer**\
The system shall allow the user to pause an active timer.

**FR-28 --- Resume Timer**\
The system shall allow the user to resume a paused timer.

**FR-29 --- Stop Timer**\
The system shall allow the user to stop the active timer.

**FR-30 --- Save Timer Session**\
When the user stops tracking, the system shall save the completed
session as a time entry with its category, required title, start time,
end time, and calculated duration.

**FR-31 --- Exclude Paused Time**\
The duration of a timer-based entry shall exclude time spent in the
paused state.

**FR-32 --- Active Session Visibility**\
The system shall provide access to the active session's status from the
Home Dashboard while the session is in progress.

The precise behavior when the application is closed, terminated, or
restarted must be confirmed during implementation planning.

**FR-33 --- Prevent Multiple Active Sessions**\
The system shall prevent a user from accidentally creating multiple
simultaneous active timer sessions.

### 3.6 Timeline

**FR-34 --- Display Timeline**\
The system shall display saved time entries in chronological order,
grouped or organized by date.

**FR-35 --- Date Navigation**\
The system shall allow the user to navigate between dates to review
historical entries.

**FR-36 --- Entry Details**\
The system shall allow the user to select an entry to view its title,
category, start time, end time, and duration.

**FR-37 --- Edit Entry**\
The system shall allow the user to edit an existing entry from the
Timeline.

The editing interface shall reuse the Add Time Entry form with the
existing entry's values prefilled.

**FR-38 --- Delete Entry**\
The system shall allow the user to delete an existing time entry after
an appropriate confirmation step.

**FR-39 --- Refresh Related Summaries**\
After an entry is created, edited, or deleted, the system shall reflect
the change in the relevant Timeline, Home Dashboard, Reports, and
Categories summaries.

### 3.7 Reports

**FR-40 --- Reports Screen**\
The system shall provide a separate Reports screen for analyzing
recorded time.

**FR-41 --- Overall Tracked Time**\
The system shall display the total tracked time for the selected
reporting period.

**FR-42 --- Category Breakdown**\
The system shall display the distribution of tracked time across
categories for the selected reporting period.

**FR-43 --- Visual Reports**\
The system shall provide graphical representations of tracked time, such
as a chart showing the distribution of time by category.

**FR-44 --- Report Period Selection**\
The system shall support the reporting periods shown in the interface,
including daily, weekly, monthly, and custom periods.

**FR-45 --- Consistent Calculations**\
The system shall calculate report totals from saved time entries using
the same duration and date-filtering rules as the other screens.

### 3.8 Settings

**FR-46 --- Account Information**\
The Settings screen shall display the user's available account
information.

**FR-47 --- Logout Access**\
The Settings screen shall provide access to the logout operation.

Additional settings, such as appearance or notification preferences, may
be included if confirmed as part of the final scope.

### 3.9 Empty States and Error Handling

**FR-48 --- Empty Timeline**\
If the user has no saved entries for the selected date, the system shall
display an appropriate empty state and provide a way to begin tracking
time.

**FR-49 --- Validation Messages**\
The system shall display clear messages when user input is invalid.

**FR-50 --- Data Loading**\
The system shall provide an appropriate loading indication when waiting
for remote data operations.

------------------------------------------------------------------------

## 4. External Interface Requirements

### 4.1 User Interface

The application shall use a consistent mobile interface with clear
labels, recognizable icons, and predictable navigation.

The main navigation shall provide access to:

1.  Home.
2.  Timeline.
3.  Reports.
4.  Categories.
5.  Settings.

The Timer screen shall be accessed through the Start Tracking action
from Home and shall not be a separate main-navigation destination.

The Add Time Entry interface shall open as a popup or bottom sheet from
Home. The same form shall be reused for editing an existing entry from
Timeline.

### 4.2 Authentication Interface

The authentication interface shall provide the supported registration
and login fields, submission actions, and validation messages.

### 4.3 Database Interface
to be confirmed 

### 4.4 Device Interface

The application shall use the device's date and time information to
record and display time entries.

The application shall not require continuous location tracking or
automatic monitoring of other applications.

------------------------------------------------------------------------


## 5. Non-Functional Requirements

### 5.1 Performance

**NFR-01:** Common interface actions should respond promptly under
normal device and network conditions.

**NFR-02:** The timer display should update smoothly without relying on
repeated database writes for every displayed second.

**NFR-03:** The application should retrieve only the data required for
the selected screen and reporting period where practical.

### 5.2 Reliability

**NFR-04:** Completed time entries shall retain consistent timestamps
and durations after being saved.

**NFR-05:** Timer calculations shall remain accurate when the timer is
paused and resumed.

**NFR-06:** A failed database operation shall not be presented to the
user as a successful save or deletion.

### 5.3 Usability

**NFR-07:** The interface shall use consistent navigation and
terminology across screens.

**NFR-08:** Required fields and validation errors shall be clearly
identifiable.

**NFR-09:** Destructive operations, including deleting entries or
categories, shall provide appropriate confirmation or warning.

**NFR-10:** The application shall provide understandable empty states
when no relevant data exists.

### 5.4 Maintainability

**NFR-11:** The application shall separate user-interface logic,
time-tracking logic, and data-access responsibilities.

**NFR-12:** Shared forms and calculation logic should be reused where
appropriate, particularly for adding and editing time entries.

### 5.5 Compatibility

**NFR-13:** The application shall run on the agreed minimum supported
Android version and compatible devices.

The exact minimum Android version shall be established before
implementation is finalized.

------------------------------------------------------------------------

## 6. Main User Workflows

### 6.1 Registration and Login

1.  The user opens TimeTrace.
2.  The user registers or logs in.
3.  The system validates the authentication request.
4.  On success, the user is directed to the Home Dashboard.

### 6.2 Manual Time Entry

1.  The user opens Home.
2.  The user selects Add Time Entry.
3.  A popup or bottom sheet appears.
4.  The user selects a category and enters a title.
5.  The user specifies the start and end times.
6.  The system validates the information and calculates the duration.
7.  The user saves the entry.
8.  The system stores the record and refreshes relevant summaries.

### 6.3 Timer-Based Tracking

1.  The user opens Home.
2.  The user chooses Start Tracking.
3.  The user selects a category and enters a title.
4.  The user starts the timer.
5.  The Timer screen opens.
6.  The user may pause and resume the timer.
7.  The user stops the timer.
8.  The system calculates the tracked duration, excluding pauses, and
    saves the entry.
9.  The saved entry becomes available in the Timeline and Reports.

### 6.4 Editing a Time Entry

1.  The user opens Timeline.
2.  The user selects an existing entry.
3.  The system displays its details.
4.  The user selects Edit.
5.  The Add Time Entry form opens with existing values prefilled.
6.  The user modifies the fields and saves.
7.  The system validates and updates the record.
8.  The relevant summaries are refreshed.

### 6.5 Managing Categories

1.  The user opens Categories.
2.  The system displays the user's categories and their total tracked
    durations.
3.  The user selects a reporting period or custom date range.
4.  The user may add a category, edit a category name, or request
    deletion.
5.  The system validates the operation and updates the displayed
    information.

### 6.6 Viewing Reports

1.  The user opens Reports.
2.  The user selects a reporting period.
3.  The system retrieves the relevant time entries.
4.  The system calculates total tracked time and category breakdowns.
5.  The system displays the results using summaries and charts.

------------------------------------------------------------------------

## 7. Business Rules

**BR-01:** A time entry must always have a title and a category.

**BR-02:** Categories are reusable. Multiple time entries can belong to
the same category.

**BR-03:** A category is not a time entry; the two must remain separate
concepts.

**BR-04:** Manual and timer-based entries must be represented
consistently across the application.

**BR-05:** Paused timer intervals must not contribute to tracked
duration.

**BR-06:** The same saved time-entry data must be used to calculate the
Home, Timeline, Reports, and Categories results.

**BR-07:** Category totals and report totals must respect the selected
date period.

**BR-08:** Users may access and modify only records they are authorized
to manage.

**BR-09:** The initial version shall not require AI, machine learning,
automatic app-usage monitoring, or predictive analytics.

------------------------------------------------------------------------

## 8. Acceptance Criteria

The initial release will be considered functionally complete when the
following conditions are satisfied:

1.  A user can register, log in, and log out.
2.  A user can create, rename, and request deletion of categories.
3.  The Categories screen displays the total tracked time for each
    category.
4.  The Categories screen supports Today, This week, This month, and
    Custom date filters.
5.  A user can create a manual entry with a required title, category,
    start time, and end time.
6.  Invalid or incomplete entries are rejected with understandable
    messages.
7.  A user can start, pause, resume, and stop a timer session.
8.  Timer-based duration calculations exclude paused intervals.
9.  Completed entries appear in the Timeline in chronological order.
10. A user can edit and delete an existing entry from the Timeline.
11. Changes to entries are reflected in the relevant summaries and
    reports.
12. Reports display total tracked time and time distribution by category
    for the selected period.
13. The Settings screen is separate from Categories and provides
    account-related actions.
14. User-specific data is protected by appropriate authentication and
    database security rules.
15. The application handles empty states and database errors
    appropriately.

------------------------------------------------------------------------

## 9. Items Requiring Final Confirmation

The following decisions should be finalized before the team treats this
draft as the approved SRS:

1.  **Category deletion:** Should deleting a category also delete its
    time entries, preserve them in an uncategorized group, or prevent
    deletion while entries exist?
2.  **Timer persistence:** Should a running timer continue when the user
    leaves the Timer screen or closes and reopens the application?
3.  **Overlapping entries:** Should the application allow two entries to
    cover the same period, or prevent overlaps?
4.  **Reports:** Should the Reports screen use exactly the same period
    filters as Categories, or have its own period controls?
5.  **Settings:** Which optional settings, if any, are required for the
    initial release?
6.  **Authentication:** Is email/password authentication sufficient, or
    is social sign-in required?
7.  **Minimum Android version:** Which Android API level should the team
    target?

These items are intentionally left open rather than being treated as
already approved requirements.

------------------------------------------------------------------------

**End of SRS Draft --- Version 1.0**
