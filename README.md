# IT Components Problem Reporting System

## Project Overview and Problem Statement

This project is a Java Swing implementation of the **IT Components Problem Reporting System** from the Software Design & Analysis assignment.

The core problem is that university hardware issues are often reported manually (verbal messages, informal notes, delayed escalation). That leads to:

- missing complaints
- delayed resolution
- no transparent status tracking for students/faculty
- weak prioritization for IT staff

This application provides a centralized, role-based workflow so complaint reporting, handling, and administrative oversight happen in one system.

## Design-to-Implementation Mapping

The implementation is directly mapped from the earlier analysis and UML work:

- **Use cases -> screens and role workflows**
  - Student/Faculty: submit complaint, view own complaints, track status
  - IT Staff: view/filter complaints, assign priority, update status, add notes
  - Admin: generate reports, manage IT staff accounts

- **Class diagram -> Java model layer**
  - `User` hierarchy: `Student`, `Faculty`, `ITStaff`, `Admin`
  - `Complaint`, `ResolutionNote`, `Report`
  - `Status`, `Priority`, `UserRole` enums

- **Sequence/activity/state behavior -> service and UI interactions**
  - login validation and role routing
  - complaint lifecycle updates and resolution notes
  - status-change notification simulation

## Architecture Summary

The project follows a layered structure:

```text
src/main/java/edu/university/itreporting/
  model/        # domain classes and enums
  repository/   # repository interfaces + in-memory implementations
  service/      # use-case services (auth, complaint, admin, notification)
  ui/           # launcher, shell controller, main frame
  ui/panels/    # role dashboards and login panel
  util/         # seed data, identifiers, validators
```

Runtime flow:

1. `ApplicationLauncher` initializes theme and seeded data.
2. `AppShellController` coordinates authentication and role operations.
3. Role panels call controller methods for all use-case actions.
4. Services apply business rules and use repositories for persistence.
5. Repositories store data in memory for this assignment version.

## Role-Based Functional Coverage

### Student / Faculty

- submit complaint memo
- view submitted complaints
- track complaint status with note details
- see in-app notifications

### IT Staff

- view all complaints
- filter by location/equipment/status/priority
- assign priority
- update status
- add resolution notes

### Admin

- generate reports
- review report summaries and status counts
- create/update/delete IT staff accounts
- list IT staff accounts

## UI Screenshot Convention and Mapping

The expected screenshot naming convention is:

- `01-login.png`
- `02-student-submit.png`
- `03-student-complaints.png`
- `04-student-track.png`
- `05-it-dashboard-all.png`
- `06-it-dashboard-actions.png`
- `07-admin-reports.png`
- `08-admin-staff-management.png`

Current screenshots in the root folder `Image Assets` use different names. Mapping:

| Expected Name | Actual File in `Image Assets` |
|---|---|
| `01-login.png` | `image (2).png` |
| `02-student-submit.png` | `image.png` |
| `03-student-complaints.png` | `image (1).png` (closest available user-view screenshot) |
| `04-student-track.png` | `image (1).png` |
| `05-it-dashboard-all.png` | `image (7).png` |
| `06-it-dashboard-actions.png` | `image (6).png` |
| `07-admin-reports.png` | `image (4).png` |
| `08-admin-staff-management.png` | `image (3).png` |

Additional captured view:

- `image (8).png` (IT Actions tab baseline before selecting complaint row)
- `image (5).png` (admin login)

## UI Walkthrough (with Screenshots)

### 1) Login Screen

Proves credential entry and role-based access entry point.

<img src="Image%20Assets/image%20%282%29.png" alt="Login Screen" width="1000" />

### 2) Student/Faculty: Submit Complaint

Proves complaint form fields, validation feedback, and successful memo creation.

<img src="Image%20Assets/image.png" alt="Student Submit Complaint" width="1000" />

### 3) Student/Faculty: Track Complaint Status

Proves status tracking by memo ID and visibility into complaint details/notes.

<img src="Image%20Assets/image%20%281%29.png" alt="Student Track Status" width="1000" />

### 4) IT Staff: Complaint Dashboard (All Complaints + Filters)

Proves centralized complaint visibility and filtering controls.

<img src="Image%20Assets/image%20%287%29.png" alt="IT Staff All Complaints" width="1000" />

### 5) IT Staff: Actions Panel

Proves priority assignment, status updates, and resolution note workflow.

<img src="Image%20Assets/image%20%286%29.png" alt="IT Staff Actions" width="1000" />

Additional IT actions baseline view:

<img src="Image%20Assets/image%20%288%29.png" alt="IT Staff Actions Baseline" width="1000" />

### 6) Admin: Reports Module

Proves report generation and summarized complaint-status analytics.

<img src="Image%20Assets/image%20%284%29.png" alt="Admin Reports" width="1000" />

### 7) Admin: IT Staff Management

Proves IT staff create/update/delete/list workflow.

<img src="Image%20Assets/image%20%283%29.png" alt="Admin IT Staff Management" width="1000" />

Optional admin login capture:

<img src="Image%20Assets/image%20%285%29.png" alt="Admin Login" width="1000" />

## Validation and Error-Handling Behavior

Examples of guarded behavior implemented:

- login requires non-empty, valid email format
- login rejects invalid credentials with clear message
- complaint submission enforces required fields and minimum description quality
- IT action forms validate memo ID format and note content
- admin staff management validates staff ID numeric format and email format
- operation failures are shown in panel feedback messages

## Run Instructions

### Run in IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Open this project root folder.
3. Ensure project SDK is set (JDK 17+ recommended):
   - `File > Project Structure > Project SDK`
4. Open:
   - `src/main/java/edu/university/itreporting/ui/ApplicationLauncher.java`
5. Run `ApplicationLauncher.main()`.

## Seed Credentials and Data Behavior

Seeded login accounts:

- Admin: `admin@university.edu` / `admin123`
- IT Staff: `ali.it@university.edu` / `it123`
- IT Staff: `fatima.it@university.edu` / `it123`
- Student: `ahmed.student@university.edu` / `user123`
- Faculty: `sana.faculty@university.edu` / `user123`

Data storage note:

- This version uses in-memory repositories.
- Restarting the application resets data to seeded state.
