import java.io.*;
import java.util.*;

/**
 * ClassModule class - represents a class for a specific module
 */
public class ClassModule implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String classID;
    private String className;
    private String moduleID;
    private Lecturer lecturer;
    private List<Student> enrolledStudents;
    private int capacity;
    private String day;
    private String time;
    private String location;
    private String lecturerID;
    private Module module; // For backward compatibility
    
    // Original constructor with Module object
    public ClassModule(String classID, String className, Module module, 
                       Lecturer lecturer, String semester, int capacity) {
        this.classID = classID;
        this.className = className;
        this.moduleID = module != null ? module.getModuleID() : "";
        this.lecturer = lecturer;
        this.lecturerID = (lecturer != null) ? lecturer.getUserID() : null;
        this.capacity = capacity;
        this.day = semester;
        this.enrolledStudents = new ArrayList<>();
    }
    
    // New constructor matching AdminClassCreationPanel expectations
    public ClassModule(String classID, String className, String moduleID, 
                       int capacity, String day, String time, String location, 
                       Lecturer lecturer) {
        this.classID = classID;
        this.className = className;
        this.moduleID = moduleID;
        this.capacity = capacity;
        this.day = day;
        this.time = time;
        this.location = location;
        this.lecturer = lecturer;
        this.lecturerID = (lecturer != null) ? lecturer.getUserID() : null;
        this.enrolledStudents = new ArrayList<>();
    }
    
    public void enrollStudent(Student student) {
        if (enrolledStudents.size() < capacity) {
            enrolledStudents.add(student);
            
        } else {
            
        }
    }
    
    public String getClassID() { return classID; }
    public void setClassID(String classID) { this.classID = classID; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public Lecturer getLecturer() { return lecturer; }
    public void setLecturer(Lecturer lecturer) { this.lecturer = lecturer; 
        this.lecturerID = (lecturer != null) ? lecturer.getUserID() : null;
    }
    
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public String getModuleID() { return moduleID; }
    public String getSemester() { return day; }
    
    public String getLecturerID() { 
        return (lecturer != null) ? lecturer.getUserID() : lecturerID;  
        }
            

    public void setLecturerID(String lecturerID) { this.lecturerID = lecturerID;
        if (lecturerID == null || lecturerID.isEmpty()) {
            this.lecturer = null;
        }
    }

    
    public String getDay() { return day; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    
    public Module getModule() { 
        if (module != null) {
            return module;
        }
        // Return a default Module with just the ID
        return new Module(moduleID, "Unknown Module", "", "Unknown", 0, "");
    }
    public void setModule(Module module) { this.module = module; }
    
    @Override
    public String toString() {
        return "ClassModule{" +
                "classID='" + classID + '\'' +
                ", className='" + className + '\'' +
                ", moduleID='" + moduleID + '\'' +
                ", lecturer=" + (lecturer != null ? lecturer.getFullName() : "N/A") +
                ", enrolledCount=" + enrolledStudents.size() +
                ", day='" + day + '\'' +
                '}';
    }
}
