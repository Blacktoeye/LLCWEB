package llcweb.com.domain.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/***********************************************************************
 * Module:  People.java
 * Author:  Ricardo
 * Purpose: Defines the Class People
 ***********************************************************************/
import javax.persistence.*;

@Entity
@Table(name = "people")
public class People {
    /**
     * 人物id
     **/
    @Id
    @GeneratedValue
    private int id;

    /**

     * 人物姓名
     */
    private String name;

    /**
     * 人物头像路径(关联image表)
     */
    private int portrait;

    /**
     * 职称，教授、副教授、讲师、博士后、博士、硕士，本科生
     */
    private String position;

    /**
     * 个人简介（关联document表）
     */
    private int introduction;

    /**
     * 对于本科生、硕士生、博士生是届数；讲师，教授，等是加入年份
     */
    private int grade;

    /**
     * 手机
     */
    @Column(length = 11)
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 30)
    private String email;

    /**
     * 性别
     */
    private String sex;

    /**
     * 行政职务
     */
    private String adminPosition;

    /**
     * 最高学历
     */
    private String highestDegree;

    /**
     * 研究领域
     */
    private String researchField;

    /**
     * 学术头衔
     */
    private String academicTitle;

    public People() {

    }

    public People(String name, String position, int grade) {
        this.name = name;
        this.position = position;
        this.grade = grade;
    }

    public People(String name, int portrait, String position, int introduction, int grade) {
        this.name = name;
        this.portrait = portrait;
        this.position = position;
        this.introduction = introduction;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPortrait() {
        return portrait;
    }

    public String getPosition() {
        return position;
    }

    public int getIntroduction() {
        return introduction;
    }

    public int getGrade() {
        return grade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setIntroduction(int introduction) {
        this.introduction = introduction;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAdminPosition() {
        return adminPosition;
    }

    public void setAdminPosition(String adminPosition) {
        this.adminPosition = adminPosition;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public String getResearchField() {
        return researchField;
    }

    public void setResearchField(String researchField) {
        this.researchField = researchField;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }
}


