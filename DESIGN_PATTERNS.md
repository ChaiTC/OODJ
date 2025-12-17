# AFS Design Patterns and Class Relationships

## 1. Design Patterns Used

### 1.1 Facade Pattern
**SystemManager** acts as a facade providing a simplified interface to complex subsystems:
- Hides complexity of user management, data loading, and system operations
- Clients interact with SystemManager instead of individual classes
- Coordinates between FileManager, User classes, and domain models

### 1.2 Abstract Factory Pattern
**User** abstract class serves as abstract factory for user types:
```java
// Users are created based on role
AdminStaff admin = new AdminStaff(...);
Lecturer lecturer = new Lecturer(...);
Student student = new Student(...);
```

### 1.3 Template Method Pattern
**User** abstract class defines template for login/dashboard workflow:
- Abstract methods `displayMenu()` and `handleAction()` define the steps
- Each subclass provides specific implementation
- Common attributes and authentication logic in base class

### 1.4 Singleton-like Pattern
**SystemManager** instance created once and manages entire application state:
```java
systemManager = new SystemManager();  // Single instance for application
```

### 1.5 Strategy Pattern
**FileManager** implements different strategies for different data types:
- Serialization strategy for Users
- Serialization strategy for Modules
- Serialization strategy for Assessments
- Each with specific format and logic

## 2. Class Relationships

### 2.1 Inheritance Hierarchy
```
User (Abstract)
├── AdminStaff
│   - Creates/manages users
│   - Configures system
│   - Manages classes
│
├── AcademicLeader
│   - Manages modules
│   - Assigns lecturers
│   - Generates reports
│
├── Lecturer
│   - Designs assessments
│   - Records marks
│   - Provides feedback
│
└── Student
    - Registers classes
    - Views results
    - Receives feedback
```

### 2.2 Composition Relationships
```
ClassModule
├── HAS-A Module (required)
├── HAS-A Lecturer (required)
└── HAS-MANY Students (optional)

Assessment
├── HAS-A AssessmentType (required)
├── HAS-A Module (required)
├── HAS-A Lecturer (required)
└── HAS-MANY Marks (optional)

GradingSystem
├── HAS-MANY GradingScale
└── HAS-A Passing Percentage

Feedback
├── HAS-A Student (referenced by ID)
├── HAS-A Lecturer (referenced by ID)
└── HAS-A Assessment (referenced by ID)
```

### 2.3 Aggregation Relationships
```
AcademicLeader HAS-MANY Lecturers
AcademicLeader HAS-MANY Modules

Lecturer HAS-MANY Modules
Lecturer HAS-MANY Assessments
Lecturer HAS-MANY Feedback

Student HAS-MANY Classes
Student HAS-MANY Assessments
Student HAS-MANY Feedback

ClassModule HAS-MANY Students
```

### 2.4 Association Relationships
```
SystemManager ←→ User (current user)
SystemManager ←→ Module
SystemManager ←→ Assessment
SystemManager ←→ Feedback
SystemManager ←→ GradingSystem

Lecturer → AssessmentType (defines)
Lecturer → Module (teaches)

FileManager ←→ All Serializable Objects
```

## 3. Detailed Class Diagram

```
┌─────────────────────────────────┐
│          <<abstract>>           │
│            User                 │
├─────────────────────────────────┤
│ # userID: String                │
│ # username: String              │
│ # password: String              │
│ # email: String                 │
│ # fullName: String              │
│ # phoneNumber: String           │
│ # role: String                  │
│ # createdDate: Date             │
│ # isActive: boolean             │
├─────────────────────────────────┤
│ + displayMenu()                 │
│ + handleAction()                │
│ + getters/setters()             │
└─────────────────────────────────┘
        ▲           ▲
        │           │
    ┌───┴───┐   ┌───┴──────┐
    │       │   │          │
┌──────┐ ┌──────────┐ ┌─────────────┐
│Admin │ │Academic  │ │  Lecturer   │
│Staff │ │  Leader  │ │             │
└──────┘ └──────────┘ └─────────────┘
                         
                    ┌─────────────────┐
                    │    Student      │
                    └─────────────────┘

┌─────────────────────────────────┐
│         Module                  │
├─────────────────────────────────┤
│ - moduleID: String              │
│ - moduleName: String            │
│ - moduleCode: String            │
│ - description: String           │
│ - creditHours: int              │
│ - department: String            │
├─────────────────────────────────┤
│ + getters/setters()             │
└─────────────────────────────────┘

┌──────────────────────────────────┐
│       ClassModule               │
├──────────────────────────────────┤
│ - classID: String               │
│ - className: String             │
│ - module: Module                │◄────┐
│ - lecturer: Lecturer            │◄──┐ │
│ - enrolledStudents: List        │◄┐ │ │
│ - semester: String              │ │ │ │
│ - capacity: int                 │ │ │ │
├──────────────────────────────────┤ │ │ │
│ + enrollStudent(Student)         │ │ │ │
│ + getters/setters()              │ │ │ │
└──────────────────────────────────┘ │ │ │
                                     │ │ │
                                     │ │ │
┌──────────────────────────────┐    │ │ │
│    Assessment               │    │ │ │
├──────────────────────────────┤    │ │ │
│ - assessmentID: String      │    │ │ │
│ - assessmentName: String    │    │ │ │
│ - assessmentType: Type      │◄───┤ │ │
│ - module: Module            │◄───┼─┤ │
│ - createdBy: Lecturer       │◄───┼─┤ │
│ - studentMarks: HashMap     │    │ │ │
│ - dueDate: Date             │    │ │ │
├──────────────────────────────┤    │ │ │
│ + recordMarks()              │    │ │ │
│ + getStudentMarks()          │    │ │ │
└──────────────────────────────┘    │ │ │
         │                          │ │ │
         │                          │ │ │
┌────────▼──────────────────┐       │ │ │
│   AssessmentType          │       │ │ │
├───────────────────────────┤       │ │ │
│ - typeID: String          │       │ │ │
│ - type: Enum              │       │ │ │
│ - weightage: double       │       │ │ │
│ - totalMarks: double      │       │ │ │
├───────────────────────────┤       │ │ │
│ + getters/setters()       │       │ │ │
└───────────────────────────┘       │ │ │
                                    │ │ │
┌────────────────────────────┐      │ │ │
│      GradingSystem         │      │ │ │
├────────────────────────────┤      │ │ │
│ - grades: List             │◄──┐  │ │ │
│ - passingPercentage: double│   │  │ │ │
├────────────────────────────┤   │  │ │ │
│ + getGradeByPercentage()   │   │  │ │ │
│ + isPassed()               │   │  │ │ │
└────────────────────────────┘   │  │ │ │
                                 │  │ │ │
                    ┌────────────┴──┴─┘ │
                    │                    │
            ┌───────▼─────────────┐      │
            │  GradingScale       │      │
            ├─────────────────────┤      │
            │ - gradeID: String   │      │
            │ - letter: String    │      │
            │ - minPercent        │      │
            │ - maxPercent        │      │
            │ - description       │      │
            ├─────────────────────┤      │
            │ + isInRange()       │      │
            │ + getters/setters() │      │
            └─────────────────────┘      │
                                         │
            ┌────────────────────────┐   │
            │     Feedback           │   │
            ├────────────────────────┤   │
            │ - feedbackID: String   │   │
            │ - assessmentID: String │◄──┘
            │ - studentID: String    │
            │ - lecturerID: String   │
            │ - content: String      │
            │ - suggestedMarks       │
            │ - comments: String     │
            │ - isDelivered: boolean │
            ├────────────────────────┤
            │ + deliverFeedback()    │
            │ + addComment()         │
            │ + getters/setters()    │
            └────────────────────────┘

┌────────────────────────────┐
│    SystemManager           │
├────────────────────────────┤
│ - users: List              │
│ - modules: List            │
│ - classes: List            │
│ - assessments: List        │
│ - feedback: List           │
│ - gradingSystem            │
│ - currentUser: User        │
├────────────────────────────┤
│ + registerUser()           │
│ + authenticateUser()       │
│ + createModule()           │
│ + createAssessment()       │
│ + createFeedback()         │
│ + getUsersByRole()         │
│ + getters/setters()        │
└────────────────────────────┘

┌────────────────────────────┐
│     FileManager            │
├────────────────────────────┤
│ - DATA_DIR: String         │
│ - USERS_FILE: String       │
│ - MODULES_FILE: String     │
├────────────────────────────┤
│ + saveUser()               │
│ + loadAllUsers()           │
│ + saveModule()             │
│ + loadAllModules()         │
│ + saveFeedback()           │
│ + loadAllFeedback()        │
│ - serializeUser()          │
│ - deserializeUser()        │
│ + clearAllData()           │
└────────────────────────────┘
```

## 4. Method Interaction Flow

### 4.1 User Registration Flow
```
AFSRegistrationFrame
    ↓
SystemManager.registerUser()
    ├→ Check username uniqueness
    ├→ Add user to users list
    └→ FileManager.saveUser()
        └→ Write to users.txt
```

### 4.2 User Login Flow
```
AFSLoginFrame
    ↓
SystemManager.authenticateUser()
    ├→ Search users list
    ├→ Verify password
    ├→ Check if active
    ├→ Set currentUser
    └→ Open appropriate Dashboard
        ├→ AdminDashboard
        ├→ AcademicLeaderDashboard
        ├→ LecturerDashboard
        └→ StudentDashboard
```

### 4.3 Assessment Creation Flow
```
LecturerDashboard
    ↓
SystemManager.createAssessment()
    ├→ Create Assessment object
    ├→ Link to Module, AssessmentType, Lecturer
    ├→ Add to assessments list
    └→ FileManager.saveAssessment()
        └→ Write to assessments.txt
```

### 4.4 Mark Entry Flow
```
LecturerDashboard
    ↓
Assessment.recordMarks()
    ├→ Validate marks (0 to totalMarks)
    ├→ Store in studentMarks HashMap
    └→ SystemManager.updateAssessment()
        └→ FileManager saves updated assessment
```

### 4.5 Feedback Creation Flow
```
LecturerDashboard
    ↓
SystemManager.createFeedback()
    ├→ Create Feedback object
    ├→ Link assessment, student, lecturer
    ├→ Add to feedback list
    └→ FileManager.saveFeedback()
        └→ Write to feedback.txt
```

### 4.6 Grade Calculation Flow
```
GradingSystem.getGradeLetterByPercentage()
    ↓
Loop through GradingScale objects
    ↓
GradingScale.isInRange()
    ├→ Check percentage >= minPercentage
    ├→ Check percentage <= maxPercentage
    └→ Return matching GradingScale
        └→ Return grade letter and description
```

## 5. Data Flow Architecture

```
User Input
    ↓
GUI Frame (AFSLoginFrame, Dashboard)
    ↓
SystemManager (Facade)
    ├→ Processes request
    ├→ Updates domain objects
    └→ FileManager
        ├→ Serialization
        ├→ File I/O
        └→ Data persistence

    ↓
Domain Model Classes
    ├→ User (with role types)
    ├→ Module, ClassModule
    ├→ Assessment, AssessmentType
    ├→ GradingSystem, GradingScale
    └→ Feedback

    ↓
SystemManager loads from FileManager
    ↓
Display to User via GUI Dashboard
```

## 6. Error Handling Strategy

```
User Action
    ↓
Input Validation (Field checks)
    ├→ Empty field check
    ├→ Format validation
    └→ Business rule validation
    
    ├→ If invalid: Show error message
    └→ If valid: Proceed
    
        ↓
    System Operation
        ├→ File I/O operations wrapped in try-catch
        ├→ Display operation success/failure
        └→ Update UI accordingly
```

---
**Document Version**: 1.0
**Last Updated**: December 2025
**System**: Assessment Feedback System (AFS)
