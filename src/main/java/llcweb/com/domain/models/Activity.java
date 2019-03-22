package llcweb.com.domain.models;

import javax.persistence.*;
import java.util.*;

/**
 * @Author haien
 * @Description 活动类：活动、会议、通知和招聘信息的总类
 * @Date 2018/10/6
 **/
@Entity
@Table(name="activity")
public class Activity {
   @Id
   @GeneratedValue
   private int id;
   //活动标题
   private String title;
   //上传者
   @Column(length = 30)
   private String author;
   //参与人员
   private String peopleList;
   //活动日期
   @Column(columnDefinition = "date")
   private Date startDate;
   @Column(columnDefinition = "date")
   private Date endDate;
   //活动简介
   private int introduction;
   //组别(命名为group更好，但它是MySQL关键字)
   @Column(length = 30)
   private String model;
   //活动类型：活动、会议、通知和招聘
   @Column(length = 30)
   private String activityType;
   //专指通知和招聘信息是否要发布,0/1
   private int isPublish;

   public Activity() {
   }

   public Activity(String title, String author, String peopleList, Date startDate, Date endDate, String model, String activityType) {
      this.title = title;
      this.author = author;
      this.peopleList = peopleList;
      this.startDate = startDate;
      this.endDate = endDate;
      this.model = model;
      this.activityType = activityType;
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

   public String getAuthor() {
      return author;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public String getPeopleList() {
      return peopleList;
   }

   public void setPeopleList(String peopleList) {
      this.peopleList = peopleList;
   }

   public Date getStartDate() {
      return startDate;
   }

   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }

   public Date getEndDate() {
      return endDate;
   }

   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   public int getIntroduction() {
      return introduction;
   }

   public void setIntroduction(int introduction) {
      this.introduction = introduction;
   }

   public String getModel() {
      return model;
   }

   public void setModel(String model) {
      this.model = model;
   }

   public String getActivityType() {
      return activityType;
   }

   public void setActivityType(String activityType) {
      this.activityType = activityType;
   }

   public int getIsPublish() {
      return isPublish;
   }

   public void setIsPublish(int isPublish) {
      this.isPublish = isPublish;
   }
}