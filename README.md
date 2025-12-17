# Assessment Feedback System (AFS) - README

## Quick Start Guide

### Prerequisites
- Java 8 or higher installed
- Text editor or IDE (Eclipse, IntelliJ, NetBeans)
- Command line/Terminal access

### Installation & Setup

1. **Navigate to project directory**
   ```
   cd c:\xampp\htdocs\OODJ
   ```

2. **Compile all Java files**
   ```
   javac *.java
   ```

3. **Run the application**
   - **GUI Version (Recommended)**
     ```
     java main
     ```
   - **Console Version**
     ```
     java AFSApplication
     ```

### Default Test Users

For easy testing, register these users:

#### Admin Staff
- Username: `admin1`
- Password: `admin123`
- Department: `Administration`
- Staff ID: `ADM001`

#### Academic Leader
- Username: `leader1`
- Password: `leader123`
- Department: `IT`
- Leader ID: `LEAD001`

#### Lecturer
- Username: `lecturer1`
- Password: `lec123`
- Lecturer ID: `LEC001`
- Department: `IT`

#### Student
- Username: `student1`
- Password: `stu123`
- Student ID: `STU001`
- Enrollment Year: `2024`

### Directory Structure

```
OODJ/
├── *.java                          # Source files
├── *.class                         # Compiled files
├── data/
│   ├── users.txt                  # User data
│   ├── modules.txt                # Module data
│   ├── assessments.txt            # Assessment data
│   └── feedback.txt               # Feedback data
├── IMPLEMENTATION_DOCUMENTATION.md # Design documentation
└── README.md                       # This file
```

### Application Workflow

#### For Admin Staff
1. **Login** with admin credentials
2. **User Management**: Create/update/delete users
3. **Grading System**: Configure grading scales
4. **Create Classes**: Set up new classes for modules
5. **Assignments**: Assign lecturers to academic leaders

#### For Academic Leaders
1. **Login** with leader credentials
2. **Module Management**: Create and manage modules
3. **Lecturer Assignment**: Assign lecturers to modules
4. **Profile Management**: Update personal information
5. **Reports**: View analysis and statistics

#### For Lecturers
1. **Login** with lecturer credentials
2. **Design Assessments**: Create assessment types and instances
3. **Key-in Marks**: Record student marks
4. **Provide Feedback**: Add feedback for assessments
5. **Module View**: Check assigned modules

#### For Students
1. **Login** with student credentials
2. **Register Classes**: Enroll in available classes
3. **Check Results**: View assessment marks
4. **View Feedback**: See lecturer feedback and comments
5. **Profile**: Update personal information

### Key Features

✓ **User Management**
- Register users with role-based creation
- Login authentication
- Profile editing
- User status management

✓ **Academic Management**
- Create and manage modules
- Create classes with capacity limits
- Assign lecturers to modules
- Enroll students in classes

✓ **Assessment System**
- Design multiple assessment types
- Record student marks with validation
- Support for assignments, tests, exams, projects, quizzes
- Automatic mark storage

✓ **Grading System**
- Predefined APU grading scale (A+ to F)
- Automatic grade assignment based on percentage
- Pass/fail determination
- Customizable passing threshold

✓ **Feedback System**
- Create detailed feedback for assessments
- Track feedback delivery
- Allow student comments
- Retrieve student-specific feedback

✓ **Data Persistence**
- Text-file based storage (no database needed)
- Automatic saving on all operations
- System data recovery through file loading

### File Descriptions

#### Domain Model Classes
- **User.java** - Abstract base user class
- **AdminStaff.java** - Admin user type
- **AcademicLeader.java** - Academic leader user type
- **Lecturer.java** - Lecturer user type
- **Student.java** - Student user type
- **Module.java** - Academic module
- **ClassModule.java** - Module class instance
- **Assessment.java** - Assessment instance
- **AssessmentType.java** - Assessment type definition
- **Feedback.java** - Feedback record
- **GradingScale.java** - Individual grade scale
- **GradingSystem.java** - Complete grading system

#### Management Classes
- **SystemManager.java** - Central system controller
- **FileManager.java** - Data persistence handler

#### Data Files

- **`data/classes.txt`** - Stores class (section) records. Format per line:
   `classID|className|moduleID|lecturerID|semester|capacity|studentIDs(comma)`
   - Example:
      `CL001|Section A|MOD002|LEC003|2024-1|50|STU001,STU002`
   - Fields:
      - `classID`: unique class identifier (e.g., `CL001`)
      - `className`: human name for the section (e.g., `Section A`)
      - `moduleID`: module identifier as found in `modules.txt`
      - `lecturerID`: lecturer user ID assigned to the class
      - `semester`: semester code (e.g., `2024-1`)
      - `capacity`: maximum number of students
      - `studentIDs(comma)`: optional comma-separated student IDs enrolled in the class


#### GUI Classes
- **main.java** - Application entry point
- **AFSLoginFrame.java** - Login interface
- **AFSRegistrationFrame.java** - Registration interface
- **AdminDashboard.java** - Admin interface
- **AcademicLeaderDashboard.java** - Academic leader interface
- **LecturerDashboard.java** - Lecturer interface
- **StudentDashboard.java** - Student interface

#### Alternative Interface
- **AFSApplication.java** - Console-based interface

### OOP Concepts Demonstrated

- **Abstraction**: Abstract User class
- **Encapsulation**: Private fields with public accessors
- **Inheritance**: User hierarchy with four user types
- **Polymorphism**: Overridden methods in user types
- **Composition**: Objects containing other objects
- **Collections**: HashMap and ArrayList usage
- **Serialization**: Persistent data storage

### Troubleshooting

**Problem**: "javac: command not found"
- **Solution**: Install Java Development Kit (JDK), add to PATH

**Problem**: Files in wrong directory
- **Solution**: Ensure all .java files are in the same directory before compilation

**Problem**: Cannot login
- **Solution**: Ensure you registered the user first, check username/password

**Problem**: Data not saved
- **Solution**: Check that `data/` directory has write permissions

**Problem**: GUI doesn't appear
- **Solution**: Ensure Display is available, try console version instead

### Contact & Support

For issues or questions about the Assessment Feedback System, please refer to:
- IMPLEMENTATION_DOCUMENTATION.md for detailed design information
- Code comments in each Java file for implementation details

---

**System**: Assessment Feedback System (AFS)
**Version**: 1.0
**University**: Asia Pacific University (A.P.U)
**Last Updated**: December 2025
