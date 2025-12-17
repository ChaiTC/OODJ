import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassModule class - represents a class for a specific module
 */
public class ClassModule implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String classID;
    private String className;
    private Module module;
    private Lecturer lecturer;
    private List<Student> enrolledStudents;
    private String semester;
    private int capacity;
    
    public ClassModule(String classID, String className, Module module, 
                       Lecturer lecturer, String semester, int capacity) {
        this.classID = classID;
        this.className = className;
        this.module = module;
        this.lecturer = lecturer;
        this.semester = semester;
        this.capacity = capacity;
        this.enrolledStudents = new ArrayList<>();
    }
    
    public void enrollStudent(Student student) {
        if (enrolledStudents.size() < capacity) {
            enrolledStudents.add(student);
            System.out.println("Student " + student.getFullName() + " enrolled in " + this.className);
        } else {
            System.out.println("Class is full!");
        }
    }
    
    public String getClassID() { return classID; }
    public void setClassID(String classID) { this.classID = classID; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public Module getModule() { return module; }
    public void setModule(Module module) { this.module = module; }
    
    public Lecturer getLecturer() { return lecturer; }
    public void setLecturer(Lecturer lecturer) { this.lecturer = lecturer; }
    
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    @Override
    public String toString() {
        return "ClassModule{" +
                "classID='" + classID + '\'' +
                ", className='" + className + '\'' +
                ", module=" + module.getModuleName() +
                ", lecturer=" + lecturer.getFullName() +
                ", enrolledCount=" + enrolledStudents.size() +
                ", semester='" + semester + '\'' +
                '}';
    }
}
