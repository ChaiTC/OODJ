import java.io.Serializable;
import java.util.Date;

public class Announcement implements Serializable {
    private static final long serialVersionUID = 1L;
    private String announcementID;
    private String title;
    private String content;
    private String senderID;
    private String targetRole; // "STUDENT", "LECTURER", "ALL", etc.
    private Date createdDate;

    public Announcement(String announcementID, String title, String content, String senderID, String targetRole) {
        this.announcementID = announcementID;
        this.title = title;
        this.content = content;
        this.senderID = senderID;
        this.targetRole = targetRole;
        this.createdDate = new Date();
    }

    public String getAnnouncementID() { return announcementID; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getSenderID() { return senderID; }
    public String getTargetRole() { return targetRole; }
    public Date getCreatedDate() { return createdDate; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
}
