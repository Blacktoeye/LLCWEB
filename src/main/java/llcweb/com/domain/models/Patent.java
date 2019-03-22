package llcweb.com.domain.models;

import javax.persistence.*;
import java.util.Date;


/**
 * @author tong
 * 专利的实体类
 */
@Entity
@Table(name = "patent")
public class Patent {
    @Id
    @GeneratedValue
    private Integer id;

    //专利标题
    private String title;

    //申请日期
    private Date appliDate;

    //文章简介
    private String introduction;

    //发明人，作者姓名列表，对应people表的姓名，也可以是外面合作人员
    private String authorList;

    //原文链接
    public String originalLink;

    //所属项目
    private String belongProject;

    //专利申请号
    private String appliNum;

    //专利公开号
    private String publicNum;

    //公开日期
    private Date publicDate;

    //代理机构
    private String agency;

    //申请人（一般为广东工业大学）
    private String appliPeople;

    //授权日期
    private Date authorizationDate;

    private int sourceFile;
    //状态
    private String state;

    public Patent() {

    }

    public int getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(int sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Date getAuthorizationDate() {
        return authorizationDate;
    }

    public void setAuthorizationDate(Date authorizationDate) {
        this.authorizationDate = authorizationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getBelongProject() {
        return belongProject;
    }

    public void setBelongProject(String belongProject) {
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

    @Override
    public String toString() {
        return "Patent [id=" + id + ", title=" + title + ", appliDate=" + appliDate + ", introduction=" + introduction
                + ", authorList=" + authorList + ", originalLink=" + originalLink + ", belongProject=" + belongProject
                + ", appliNum=" + appliNum + ", publicNum=" + publicNum + ", publicDate=" + publicDate + ", agency="
                + agency + ", appliPeople=" + appliPeople + "]";
    }
}