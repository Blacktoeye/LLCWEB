package llcweb.com.domain.models;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author haien
 * @Description 论文类
 * @Date 2018/10/7
 **/
@Entity
@Table(name = "paper")
public class Paper {
    @Id
    @GeneratedValue
    private Integer id;

    //论文标题
    @Column(name = "title", length = 64)
    private String title;

    //发表日期
    @Column(name = "date")
    private Date date;

    //文章简介
    @Column(name = "introduction", length = 255)
    private String introduction;

    //作者姓名列表，对应people表的姓名，也可以是外面合作人员
    @Column(name = "author_list", length = 64)
    private String authorList;

    //原文链接
    @Column(name = "original_link", length = 64)
    private String originalLink;

    //源码链接
    @Column(name = "source_link", length = 64)
    private String sourceLink;

    //所属项目id
    @Column(name = "belong_project", length = 11)
    private String belongProject;

    //发表期刊
    @Column(name = "periodical", length = 64)
    private String periodical;
    private String periodicalType;
    private String state;

    public String getPeriodicalType() {
        return periodicalType;
    }

    public void setPeriodicalType(String periodicalType) {
        this.periodicalType = periodicalType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Paper() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getBelongProject() {
        return belongProject;
    }

    public void setBelongProject(String belongProject) {
        this.belongProject = belongProject;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getPeriodical() {
        return periodical;
    }

    public void setPeriodical(String periodical) {
        this.periodical = periodical;
    }

    @Override
    public String toString() {
        return "Paper [id=" + id + ", title=" + title + ", date=" + date + ", introduction=" + introduction
                + ", authorList=" + authorList + ", originalLink=" + originalLink + ", belongProject=" + belongProject
                + ", sourceLink=" + sourceLink + ", periodical=" + periodical + "]";
    }


}