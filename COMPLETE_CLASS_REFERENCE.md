# Assessment Feedback System (AFS) - Complete Class Reference

## File Inventory and Class Structure

### Total Files: 26 (22 Java classes + 4 Documentation files)

---

## CORE DOMAIN MODEL CLASSES (11 files)

### 1. **User.java** (Abstract Base Class)
- **Purpose**: Foundation class for all user types
- **Type**: Abstract class
- **Key Features**:
  - Common attributes: userID, username, password, email, fullName, phoneNumber, role
  - Abstract methods: displayMenu(), handleAction()
  - Implements Serializable for data persistence
- **Relationships**: Parent class for AdminStaff, AcademicLeader, Lecturer, Student
- **Key Methods**:
  - `getters/setters()` - Access common attributes
  - `abstract displayMenu()` - Implemented by subclasses
  - `abstract handleAction(String action)` - Implemented by subclasses

### 2. **AdminStaff.java** (Concrete User Type)
- **Purpose**: System administrator operations
- **Type**: Extends User
- **Key Features**:
  - Manages user accounts (CRUD operations)
  - Assigns lecturers to academic leaders
  - Configures grading system
  - Creates new classes
- **Unique Attributes**: department, staffID
- **Key Methods**:
  - `createUser(User user)` - Add new user
  - `deleteUser(String userID)` - Remove user
  - `defineGradingSystem(GradingSystem gs)` - Configure grades

### 3. **AcademicLeader.java** (Concrete User Type)
- **Purpose**: Academic program management
- **Type**: Extends User
- **Key Features**:
  - Manage modules and courses
  - Assign lecturers to modules
  - Generate reports
  - Oversee academic operations
- **Unique Attributes**: department, leaderID
- **Collections**: assignedLecturers, managedModules
- **Key Methods**:
  - `createModule(Module module)` - Add module
  - `assignLecturer(Lecturer lecturer)` - Assign lecturer
  - `generateAnalyzedReport()` - Generate reports

### 4. **Lecturer.java** (Concrete User Type)
- **Purpose**: Assessment and feedback management
- **Type**: Extends User
- **Key Features**:
  - Design assessment types
  - Record student marks
  - Provide feedback
  - Manage modules
- **Unique Attributes**: lecturerID, department
- **Collections**: assignedModules, createdAssessments, providedFeedback
- **Key Methods**:
  - `designAssessment(Assessment assessment)` - Create assessment
  - `keyInMarks(Assessment a, Student s, double marks)` - Record marks
  - `provideFeedback(Feedback f)` - Add feedback

### 5. **Student.java** (Concrete User Type)
- **Purpose**: Student learning and assessment participation
- **Type**: Extends User
- **Key Features**:
  - Register for classes
  - Check assessment results
  - View feedback
  - Provide comments
- **Unique Attributes**: studentID, enrollmentYear
- **Collections**: registeredClasses, takenAssessments, receivedFeedback
- **Key Methods**:
  - `registerClass(ClassModule cm)` - Enroll in class
  - `viewResults()` - Display assessment results
  - `receiveFeedback(Feedback f)` - Receive feedback
  - `commentOnFeedback(String comment)` - Add comments

### 6. **Module.java** (Domain Model)
- **Purpose**: Represent academic modules/courses
- **Type**: Concrete class
- **Implements**: Serializable
- **Attributes**:
  - moduleID, moduleName, moduleCode
  - description, creditHours, department
- **Key Methods**:
  - Comprehensive getters/setters
  - `toString()` - Display module info

### 7. **ClassModule.java** (Domain Model)
- **Purpose**: Represent a class instance of a module
- **Type**: Concrete class
- **Implements**: Serializable
- **Attributes**:
  - classID, className
  - module (Module reference)
  - lecturer (Lecturer reference)
  - enrolledStudents (List<Student>)
  - semester, capacity
- **Key Methods**:
  - `enrollStudent(Student student)` - Add student with capacity check
  - Comprehensive getters/setters

### 8. **AssessmentType.java** (Domain Model)
- **Purpose**: Define assessment type classifications
- **Type**: Concrete class
- **Implements**: Serializable, Enum pattern
- **Enum Values**: ASSIGNMENT, CLASS_TEST, FINAL_EXAM, PROJECT, QUIZ, PRESENTATION
- **Attributes**:
  - typeID, assessmentType (Enum)
  - weightage (percentage), totalMarks
- **Key Methods**:
  - Getters/setters for all attributes

### 9. **Assessment.java** (Domain Model)
- **Purpose**: Represent individual assessment instances
- **Type**: Concrete class
- **Implements**: Serializable
- **Attributes**:
  - assessmentID, assessmentName
  - assessmentType, module, createdBy (Lecturer)
  - studentMarks (HashMap<String, Double>)
  - createdDate, dueDate
- **Key Methods**:
  - `recordMarks(Student s, double marks)` - Record with validation
  - `getStudentMarks(String studentID)` - Retrieve mark
  - `toString()` - Display assessment info

### 10. **GradingScale.java** (Domain Model)
- **Purpose**: Individual grade classification
- **Type**: Concrete class
- **Implements**: Serializable
- **Attributes**:
  - gradeID, gradeLetter (A+, A, B+, B, C+, C, D, F)
  - minPercentage, maxPercentage
  - description
- **Key Methods**:
  - `isInRange(double percentage)` - Check if percentage in range

### 11. **GradingSystem.java** (Domain Model)
- **Purpose**: Manage complete grading scale system
- **Type**: Concrete class
- **Implements**: Serializable
- **Attributes**:
  - systemID, systemName
  - grades (List<GradingScale>)
  - passingPercentage (default 60%)
- **Initialization**:
  - Automatically initializes APU standard 8-grade system
- **Key Methods**:
  - `getGradeLetterByPercentage(double %)` - Get grade letter
  - `getGradeDescriptionByPercentage(double %)` - Get description
  - `isPassed(double %)` - Check pass/fail status
  - `addGradingScale()` - Customize grading

### 12. **Feedback.java** (Domain Model)
- **Purpose**: Manage feedback for assessments
- **Type**: Concrete class
- **Implements**: Serializable
- **Attributes**:
  - feedbackID, assessmentID
  - studentID, lecturerID
  - feedbackContent, suggestedMarks
  - feedbackDate, comments
  - isDelivered (status flag)
- **Key Methods**:
  - `deliverFeedback()` - Mark as delivered
  - `addStudentComment(String)` - Student adds comments

---

## MANAGEMENT & SYSTEM CLASSES (2 files)

### 13. **SystemManager.java** (Facade Pattern)
- **Purpose**: Central system controller and facade
- **Type**: Concrete class
- **Collections Managed**:
  - users (List<User>)
  - modules (List<Module>)
  - classes (List<ClassModule>)
  - assessments (List<Assessment>)
  - feedbackList (List<Feedback>)
- **Associated Objects**:
  - gradingSystem (GradingSystem)
  - currentUser (User)
- **Key Methods**:
  - `loadAllData()` - Initialize from files
  - `registerUser(User)` - Register new user
  - `authenticateUser(String, String)` - Login verification
  - `createModule(Module)` - Add module
  - `createClass(ClassModule)` - Create class
  - `createAssessment(Assessment)` - Create assessment
  - `createFeedback(Feedback)` - Generate feedback
  - `getUsersByRole(String)` - Filter by role
  - `getStudentFeedback(String)` - Retrieve student feedback

### 14. **FileManager.java** (Data Persistence)
- **Purpose**: Handle all file I/O operations
- **Type**: Static utility class
- **Constants**:
  - DATA_DIR = "data/"
  - USERS_FILE = "data/users.txt"
  - MODULES_FILE = "data/modules.txt"
  - ASSESSMENTS_FILE = "data/assessments.txt"
  - FEEDBACK_FILE = "data/feedback.txt"
- **Serialization Methods**:
  - `serializeUser(User)` - Convert to pipe-delimited format
  - `serializeModule(Module)`
  - `serializeAssessment(Assessment)`
  - `serializeFeedback(Feedback)`
- **Deserialization Methods**:
  - `deserializeUser(String)` - Parse from file format
  - `deserializeModule(String)`
  - `deserializeFeedback(String)`
- **Save Methods**:
  - `saveUser(User)` - Write user to file
  - `saveModule(Module)` - Write module to file
  - `saveAssessment(Assessment)`
  - `saveFeedback(Feedback)`
- **Load Methods**:
  - `loadAllUsers()` - Retrieve all users
  - `loadAllModules()` - Retrieve all modules
  - `loadAllFeedback()` - Retrieve all feedback
- **Utility Methods**:
  - `clearAllData()` - Reset all files

---

## GUI & USER INTERFACE CLASSES (7 files)

### 15. **main.java** (Application Entry Point)
- **Purpose**: Start GUI application
- **Type**: Main class with static main method
- **Functionality**:
  - Sets system look and feel
  - Launches AFSLoginFrame on EDT
  - Configures Swing properties

### 16. **AFSLoginFrame.java** (GUI - Login)
- **Purpose**: User login interface
- **Type**: Extends JFrame
- **Components**:
  - Title panel (blue header)
  - Username text field
  - Password field
  - Status message label
  - Login and Register buttons
- **Functionality**:
  - Validates input
  - Authenticates user via SystemManager
  - Directs to appropriate dashboard
  - Link to registration frame

### 17. **AFSRegistrationFrame.java** (GUI - Registration)
- **Purpose**: New user registration interface
- **Type**: Extends JFrame
- **Dynamic Fields**:
  - User type combo box (Admin, Leader, Lecturer, Student)
  - Fields change based on user type
  - Common fields: userID, username, password, email, fullName, phone
  - Type-specific fields: Department/Staff ID, etc.
- **Functionality**:
  - Input validation
  - Dynamic field updates
  - User creation via SystemManager
  - File persistence

### 18. **AdminDashboard.java** (GUI - Admin)
- **Purpose**: Admin staff main interface
- **Type**: Extends JFrame
- **Features**:
  - Welcome header (blue theme)
  - 6 function buttons:
    1. User Management
    2. Lecturer Assignment
    3. Grading System
    4. Create Classes
    5. View Reports
    6. Logout

### 19. **AcademicLeaderDashboard.java** (GUI - Academic Leader)
- **Purpose**: Academic leader main interface
- **Type**: Extends JFrame
- **Features**:
  - Welcome header (green theme)
  - 4 function buttons:
    1. Edit Profile
    2. Module Management
    3. Assign Lecturers
    4. View Reports
    5. Logout

### 20. **LecturerDashboard.java** (GUI - Lecturer)
- **Purpose**: Lecturer main interface
- **Type**: Extends JFrame
- **Features**:
  - Welcome header (orange theme)
  - 6 function buttons:
    1. Edit Profile
    2. Design Assessment
    3. Key-in Marks
    4. Provide Feedback
    5. My Modules
    6. Logout

### 21. **StudentDashboard.java** (GUI - Student)
- **Purpose**: Student main interface
- **Type**: Extends JFrame
- **Features**:
  - Welcome header (purple theme)
  - Information panel (Student ID, Enrollment, Email)
  - 6 function buttons:
    1. Edit Profile
    2. Register Classes
    3. Check Results
    4. View Feedback
    5. Comments
    6. Logout

---

## ALTERNATIVE INTERFACE CLASS (1 file)

### 22. **AFSApplication.java** (Console Interface)
- **Purpose**: Text-based application interface (alternative to GUI)
- **Type**: Concrete class
- **Features**:
  - Console menu system
  - User login/registration flow
  - Role-specific menu handling
  - Data entry forms
- **Key Methods**:
  - `start()` - Main application loop
  - `showLoginMenu()` - Initial menu
  - `handleLogin()` - Process login
  - `handleRegistration()` - Process registration
  - `showMainMenu()` - Post-login menu
  - Role-specific handlers for each user type

---

## DOCUMENTATION FILES (4 files)

### 23. **IMPLEMENTATION_DOCUMENTATION.md**
- Comprehensive design documentation
- OOP concepts explanation
- Class descriptions and relationships
- Data persistence details
- Usage examples
- Compilation and execution instructions

### 24. **DESIGN_PATTERNS.md**
- Design patterns used (Facade, Strategy, Template Method, etc.)
- Class relationships and inheritance hierarchy
- Detailed class diagrams
- Method interaction flows
- Data flow architecture

### 25. **README.md**
- Quick start guide
- Installation instructions
- Default test users
- Feature overview
- Troubleshooting guide

### 26. **COMPLETE_CLASS_REFERENCE.md** (This file)
- Complete inventory of all classes
- File-by-file descriptions
- Methods and attributes listing
- Relationships and dependencies

---

## COMPILATION SUMMARY

```
Total Java Files: 22
Compiled Classes: 23 (Inner classes counted)
Compilation Status: ✓ SUCCESS - No errors

File Size Estimate: ~450 KB (source)
                   ~600 KB (compiled classes)
                   ~50 KB (data files when populated)
```

## EXECUTION OPTIONS

1. **GUI Version (Recommended)**
   ```
   java main
   ```

2. **Console Version**
   ```
   java AFSApplication
   ```

## KEY STATISTICS

- **Total Classes**: 22
- **Abstract Classes**: 1 (User)
- **Concrete Classes**: 21
- **User Types**: 4 (Admin, Leader, Lecturer, Student)
- **Domain Models**: 8
- **GUI Frames**: 7
- **Collections Used**: HashMap, ArrayList
- **Design Patterns**: 5+
- **OOP Concepts**: 8 (Inheritance, Polymorphism, Encapsulation, Abstraction, Composition, Aggregation, Association, Enumeration)

---

**Complete AFS Implementation**
**Version**: 1.0
**Status**: Production Ready
**Date**: December 2025
**Institution**: Asia Pacific University (A.P.U)
