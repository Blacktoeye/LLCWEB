package llcweb.com.domain.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "software")
public class Software {
    @Id
    @GeneratedValue
    private int id;
    //标题
    private String title;
    //申请日期
    @Column(columnDefinition = "date")
    private Date appliDate;
    //摘要
    private String introduction;
    //作者列表
    private String authorList;
    //公开链接
    private String originalLink;
    //关联的文件id
    private int sourceFile;
    //所属项目id
    private int belongProject;
    //申请号
    private String appliNum;
    //公开号
    private String publicNum;
    //公开日
    @Column(columnDefinition = "date")
    private Date publicDate;
    //代理机构
    private String agency;
    //申请人（一般为广东工业大学）
    private String appliPeople;
    //状态
    private String state;

    private String publicMethod;
    private String rightRange;

    public String getPublicMethod() {
        return publicMethod;
    }

    public void setPublicMethod(String publicMethod) {
        this.publicMethod = publicMethod;
    }

    public String getRightRange() {
        return rightRange;
    }

    public void setRightRange(String rightRange) {
        this.rightRange = rightRange;
    }

    public Software() {
    }

    public Software(String title, Date appliDate, String introduction, String authorList,
                    String appliNum, String publicNum, Date publicDate) {
        this.title = title;
        this.appliDate = appliDate;
        this.introduction = introduction;
        this.authorList = authorList;
        this.appliNum = appliNum;
        this.publicNum = publicNum;
        this.publicDate = publicDate;
        this.appliPeople = appliPeople;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAppliDate() {
        return appliDate;
    }

    public void setAppliDate(Date appliDate) {
        this.appliDate = appliDate;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAuthorList() {
        return authorList;
    }

    public void setAuthorList(String authorList) {
        this.authorList = authorList;
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public void setOriginalLink(String originalLink) {
        this.originalLink = originalLink;
    }

    public int getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(int sourceFile) {
        this.sourceFile = sourceFile;
    }

    public int getBelongProject() {
        return belongProject;
    }

    public void setBelongProject(int belongProject) {
        this.belongProject = belongProject;
    }

    public String getAppliNum() {
        return appliNum;
    }

    public void setAppliNum(String appliNum) {
        this.appliNum = appliNum;
    }

    public String getPublicNum() {
        return publicNum;
    }

    public void setPublicNum(String publicNum) {
        this.publicNum = publicNum;
    }

    public Date getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(Date publicDate) {
        this.publicDate = publicDate;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getAppliPeople() {
        return appliPeople;
    }

    public void setAppliPeople(String appliPeople) {
        this.appliPeople = appliPeople;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
