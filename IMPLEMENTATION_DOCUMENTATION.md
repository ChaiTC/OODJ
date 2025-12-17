# ASSESSMENT FEEDBACK SYSTEM (AFS) - IMPLEMENTATION DOCUMENTATION

## 1. PROJECT OVERVIEW

The Assessment Feedback System (AFS) is a comprehensive object-oriented Java application designed for Asia Pacific University (A.P.U). It supports four types of users: Administrative Staff, Academic Leaders, Lecturers, and Students. The system enables efficient management of assessments, grading, and feedback delivery.

## 2. SYSTEM ARCHITECTURE & OOP CONCEPTS

### 2.1 Class Hierarchy and Inheritance

#### User Hierarchy (Abstract Base Class Pattern)
```
User (Abstract Class)
├── AdminStaff
├── AcademicLeader
├── Lecturer
└── Student
```

The `User` class is an abstract base class that defines common attributes and methods for all user types:
- **Encapsulation**: Private fields with public getters/setters
- **Inheritance**: All user types inherit from User and implement role-specific behavior
- **Polymorphism**: Each user type implements `displayMenu()` and `handleAction()` methods differently

### 2.2 Domain Model Classes

#### Core Entities:
1. **Module** - Represents academic modules/courses
   - Stores module information (ID, name, code, credits)
   - Implements Serializable for data persistence

2. **ClassModule** - Represents a class for a specific module
   - Links modules to lecturers and students
   - Manages student enrollment with capacity constraints
   - Demonstrates composition: "A class HAS-A module" and "HAS-A lecturer"

3. **AssessmentType** - Defines types of assessments
   - Uses Enum for assessment types (ASSIGNMENT, CLASS_TEST, FINAL_EXAM, PROJECT, QUIZ, PRESENTATION)
   - Stores weightage and total marks information

4. **Assessment** - Represents an assessment instance
   - Links to AssessmentType, Module, and Lecturer
   - Maintains student marks using HashMap
   - Implements mark validation

5. **GradingScale** - Represents individual grades in the grading system
   - Stores grade criteria (letter grade, percentage range, description)
   - Provides grade classification

6. **GradingSystem** - Manages all grading scales
   - Initialized with APU standard grading system
   - Provides grade lookup and pass/fail determination
   - Demonstrates composition: "A system HAS-A collection of GradingScales"

7. **Feedback** - Represents feedback for student assessments
   - Stores lecturer feedback, suggested marks, and student comments
   - Tracks feedback delivery status

### 2.3 OOP Concepts Demonstrated

#### 1. **Inheritance**
- User class provides template for all user types
- Reduces code duplication through common interface
- Each user type extends functionality with role-specific operations

#### 2. **Polymorphism (Method Overriding)**
```java
// Abstract method in User
public abstract void displayMenu();

// Different implementations in each subclass
AdminStaff.displayMenu()      // Shows admin options
Lecturer.displayMenu()        // Shows lecturer options
Student.displayMenu()         // Shows student options
```

#### 3. **Encapsulation**
- Private fields with public accessor methods
- Data validation in setter methods
- Business logic encapsulated in methods (e.g., `recordMarks()`, `enrollStudent()`)

#### 4. **Composition**
- Assessment contains AssessmentType
- ClassModule contains Module and Lecturer
- GradingSystem contains GradingScale objects
- FileManager uses helper serialization methods

#### 5. **Abstraction**
- Abstract User class hides implementation details
- FileManager abstracts file I/O operations
- SystemManager abstracts system operations
- Users interact with simplified interfaces

#### 6. **Aggregation & Association**
- Student has many Assessments (one-to-many relationship)
- Lecturer has many Modules (one-to-many relationship)
- ClassModule has many Students (one-to-many relationship)
- Feedback has one Lecturer and one Student (many-to-one relationships)

## 3. DETAILED CLASS DESCRIPTIONS

### 3.1 User Classes

#### User.java (Abstract Base Class)
```
Attributes:
- userID: Unique identifier
- username: Login username
- password: Login password (should be encrypted in production)
- email: User email
- fullName: User full name
- phoneNumber: Contact number
- role: User role
- createdDate: Account creation date
- isActive: Account status

Methods:
- abstract displayMenu(): Shows role-specific menu
- abstract handleAction(): Handles role-specific actions
- Getters and setters for all attributes
```

#### AdminStaff.java
**Role**: System administrator managing users and configurations
**Responsibilities**:
- Create/Read/Update/Delete users
- Assign lecturers to academic leaders
- Define grading system
- Create new classes

**Methods**:
- `createUser()`: Add new user to system
- `deleteUser()`: Remove user from system
- `defineGradingSystem()`: Configure grading criteria

#### AcademicLeader.java
**Role**: Academic management and module oversight
**Responsibilities**:
- Manage modules
- Assign lecturers to modules
- Generate analyzed reports
- Oversee academic operations

**Collections**:
- `assignedLecturers`: List of lecturers under supervision
- `managedModules`: List of modules under management

#### Lecturer.java
**Role**: Assessment and feedback provider
**Responsibilities**:
- Design assessment types
- Record student marks
- Provide feedback
- Manage modules

**Collections**:
- `assignedModules`: Modules assigned to teach
- `createdAssessments`: Assessments designed
- `providedFeedback`: Feedback given to students

#### Student.java
**Role**: Learning and assessment participant
**Responsibilities**:
- Register for classes
- View results
- Receive and respond to feedback
- Provide comments on learning

**Collections**:
- `registeredClasses`: Classes enrolled in
- `takenAssessments`: Assessments completed
- `receivedFeedback`: Feedback received from lecturers

### 3.2 Academic Domain Classes

#### Module.java
Core information about academic modules with attributes:
- moduleID, moduleName, moduleCode
- creditHours, department
- description

#### ClassModule.java
Links modules to specific classes with:
- Module reference
- Lecturer assignment
- Student enrollment list
- Capacity management
- Semester information

#### AssessmentType.java
Enumeration-based design for assessment classifications:
```java
Type enum:
- ASSIGNMENT (e.g., 20% weightage)
- CLASS_TEST (e.g., 20% weightage)
- FINAL_EXAM (e.g., 40% weightage)
- PROJECT (e.g., 10% weightage)
- QUIZ (e.g., 10% weightage)
- PRESENTATION (e.g., 0% weightage)
```

#### Assessment.java
Assessment instance with:
- Assessment type and module reference
- Lecturer who created it
- HashMap of student marks
- Mark validation (0 to totalMarks)
- Due date tracking

#### GradingScale.java
Individual grade representation:
- Grade letter (A+, A, B+, B, C+, C, D, F)
- Percentage range (min to max)
- Grade description
- Range checking method

#### GradingSystem.java
Complete grading system with:
- Predefined APU grading scales
- Grade lookup by percentage
- Pass/fail determination
- Customizable passing percentage (default 60%)

**APU Standard Grading Scale**:
| Grade | Range | Description |
|-------|-------|-------------|
| A+    | 90-100| Excellent   |
| A     | 80-89 | Very Good   |
| B+    | 75-79 | Good        |
| B     | 70-74 | Satisfactory|
| C+    | 65-69 | Acceptable  |
| C     | 60-64 | Pass        |
| D     | 50-59 | Weak        |
| F     | 0-49  | Fail        |

#### Feedback.java
Feedback management with:
- Assessment reference
- Student and lecturer identification
- Feedback content and suggested marks
- Student comments field
- Delivery status tracking
- Delivery and comment recording methods

### 3.3 Management Classes

#### SystemManager.java
Central system controller implementing Facade pattern:
```
Key Methods:
- loadAllData(): Initialize system from files
- registerUser(): Add new user to system
- authenticateUser(): Verify login credentials
- createModule(): Add new module
- createClass(): Create class instance
- createAssessment(): Add assessment
- createFeedback(): Generate feedback
- getUsersByRole(): Filter users by role
- getStudentFeedback(): Retrieve student feedback
```

#### FileManager.java
Data persistence layer with:
```
Methods:
- saveUser(), loadAllUsers(): User persistence
- saveModule(), loadAllModules(): Module persistence
- saveAssessment(): Assessment saving
- saveFeedback(), loadAllFeedback(): Feedback persistence
- clearAllData(): System reset

Serialization Format (pipe-delimited text):
Users: ROLE|userID|username|password|email|fullName|phone|field1|field2
Modules: moduleID|name|code|description|credits|department
Feedback: feedbackID|assessmentID|studentID|lecturerID|content|marks
```

### 3.4 GUI Classes

#### main.java
Application entry point with Swing initialization and LaF configuration

#### AFSLoginFrame.java
Login interface featuring:
- Username and password input
- Login validation
- Registration link
- Error messaging
- Role-based dashboard navigation

#### AFSRegistrationFrame.java
User registration with:
- Dynamic form fields based on user type
- Input validation
- User creation and file persistence

#### Dashboard Classes
**AdminDashboard.java**: Admin staff interface
**AcademicLeaderDashboard.java**: Academic leader interface
**LecturerDashboard.java**: Lecturer interface
**StudentDashboard.java**: Student interface

Each dashboard provides role-specific functionality buttons and navigation.

#### AFSApplication.java
Console-based application alternative with:
- Text menu interface
- User interaction loop
- Role-specific action handling
- Module and class creation workflows

## 4. DATA PERSISTENCE

### 4.1 File Structure
```
data/
├── users.txt          # User data
├── modules.txt        # Module information
├── assessments.txt    # Assessment details
└── feedback.txt       # Feedback records
```

### 4.2 Serialization Format
All data stored in pipe-delimited (|) text format for easy parsing:
```
Example User Entry:
LECTURER|L001|jsmith|pass123|j.smith@apu.edu|John Smith|0123456789|LEC001|IT

Example Module Entry:
MOD001|Object Oriented Programming|CSIT101|Study of OOP concepts|3|IT
```

## 5. KEY FEATURES

### 5.1 User Management
- ✓ User registration with role selection
- ✓ Login authentication with password validation
- ✓ Profile editing capabilities
- ✓ Role-based access control
- ✓ User status management (active/inactive)

### 5.2 Academic Management
- ✓ Module creation and management
- ✓ Class creation with enrollment
- ✓ Lecturer assignment
- ✓ Student registration

### 5.3 Assessment Management
- ✓ Assessment type definition
- ✓ Assessment creation and scheduling
- ✓ Mark entry with validation
- ✓ Multiple assessment support per module

### 5.4 Grading System
- ✓ Predefined APU grading scale
- ✓ Automatic grade assignment
- ✓ Pass/fail determination
- ✓ Customizable passing percentage
- ✓ Grade description lookup

### 5.5 Feedback System
- ✓ Lecturer feedback creation
- ✓ Suggested marks
- ✓ Feedback delivery tracking
- ✓ Student comments on feedback
- ✓ Feedback retrieval by student

## 6. USAGE EXAMPLES

### 6.1 User Registration
```
1. Launch application
2. Click "Register" button
3. Select user type (Admin Staff, Academic Leader, Lecturer, Student)
4. Fill in all required fields
5. Click "Register" - user saved to file
```

### 6.2 User Login
```
1. Enter username
2. Enter password
3. Click "Login"
4. System authenticates and loads appropriate dashboard
```

### 6.3 Lecturer Creating Assessment
```
1. Login as Lecturer
2. Select "Design Assessment"
3. Enter assessment details:
   - Name, Type, Module, Due Date
4. System stores assessment
```

### 6.4 Lecturer Entering Marks
```
1. Select "Key-in Marks"
2. Choose assessment and student
3. Enter mark (validated against total marks)
4. System saves mark to assessment
```

### 6.5 Providing Feedback
```
1. Select "Provide Feedback"
2. Choose assessment and student
3. Enter feedback content and suggested marks
4. System saves and marks as delivered
```

## 7. COMPILATION AND EXECUTION

### 7.1 Compilation
```bash
cd c:\xampp\htdocs\OODJ
javac *.java
```

### 7.2 Execution - GUI Version
```bash
java main
```

### 7.3 Execution - Console Version
```bash
java AFSApplication
```

## 8. SYSTEM REQUIREMENTS

- Java 8 or higher
- Swing library (included in JDK)
- File system access for data persistence
- 50MB disk space for data files

## 9. OBJECT-ORIENTED PROGRAMMING CONCEPTS IMPLEMENTED

| Concept | Implementation |
|---------|-----------------|
| **Abstraction** | Abstract User class, FileManager facade, SystemManager interface |
| **Encapsulation** | Private fields with public accessors, business logic in methods |
| **Inheritance** | User hierarchy with AdminStaff, AcademicLeader, Lecturer, Student |
| **Polymorphism** | Overridden displayMenu() and handleAction() methods |
| **Composition** | Assessment contains AssessmentType, GradingSystem contains GradingScale |
| **Aggregation** | Collections of students, lecturers, modules, assessments |
| **Association** | Relationships between Users, Modules, Classes, Assessments |
| **Enumeration** | AssessmentType enum for classification |
| **Collections** | HashMap, ArrayList for data management |
| **Serialization** | All domain objects implement Serializable |

## 10. ERROR HANDLING & VALIDATION

- Username uniqueness validation during registration
- Password field masking during login
- Mark entry validation (0 to totalMarks)
- Class capacity enforcement during enrollment
- File I/O error handling with meaningful messages
- Input field validation in GUI forms

## 11. FUTURE ENHANCEMENTS

- Database integration (MySQL, PostgreSQL)
- Email notification system
- Advanced reporting with graphs
- Student grade prediction
- Attendance tracking
- Late submission handling
- Grade appeals system
- Transcript generation
- Mobile application interface
- Authentication token system

## 12. CONCLUSION

The Assessment Feedback System demonstrates comprehensive object-oriented design principles through:
- Clear class hierarchy and inheritance structure
- Proper encapsulation of data and methods
- Polymorphic behavior for role-specific functionality
- Composition and aggregation for complex relationships
- Separation of concerns with dedicated manager classes
- Persistent data storage without database dependency

This implementation meets all coursework requirements including user-friendly GUI, role-based access control, grading system, assessment management, and feedback system, all while demonstrating solid OOP principles.

---
**Date Created**: December 2025
**University**: Asia Pacific University (A.P.U)
**System**: Assessment Feedback System (AFS)
**Version**: 1.0
