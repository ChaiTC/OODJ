import java.io.*;

public class Module implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String moduleID;
    private String moduleName;
    private String moduleCode;
    private String description;
    private int creditHours;
    private String department;
    
    public Module(String moduleID, String moduleName, String moduleCode, 
                  String description, int creditHours, String department) {
        this.moduleID = moduleID;
        this.moduleName = moduleName;
        this.moduleCode = moduleCode;
        this.description = description;
        this.creditHours = creditHours;
        this.department = department;
    }
    
    public String getModuleID() { return moduleID; }
    public void setModuleID(String moduleID) { this.moduleID = moduleID; }
    
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    
    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public int getCredits() { return creditHours; }
    
    @Override
    public String toString() {
        return "Module{" +
                "moduleID='" + moduleID + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", moduleCode='" + moduleCode + '\'' +
                ", creditHours=" + creditHours +
                '}';
    }
}
