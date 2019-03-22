package llcweb.com.domain.models;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author haien
 * @Description 活动类：活动、会议、通知和招聘信息的总类
 * @Date 2018/10/6
 **/
@Entity
@Table(name="platform")
public class Platform {
   @Id
   @GeneratedValue
   private int id;
   private String name;
   private String address;
   private String videoAddress;
   private String coverImage;//介绍封面图片地址
   private int pdfFile;
   private Date date;
   //平台简介
   private int introduction;
   private int isPublish;

   public Platform() {
   }

   public Platform(String name, String address, String videoAddress, String coverImage, int pdfFile, Date date, int introduction, int isPublish) {
      this.name = name;
      this.address = address;
      this.videoAddress = videoAddress;
      this.coverImage = coverImage;
      this.pdfFile = pdfFile;
      this.date = date;
      this.introduction = introduction;
      this.isPublish = isPublish;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getVideoAddress() {
      return videoAddress;
   }

   public void setVideoAddress(String videoAddress) {
      this.videoAddress = videoAddress;
   }

   public String getCoverImage() {
      return coverImage;
   }

   public void setCoverImage(String coverImage) {
      this.coverImage = coverImage;
   }

   public int getPdfFile() {
      return pdfFile;
   }

   public void setPdfFile(int pdfFile) {
      this.pdfFile = pdfFile;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public int getIntroduction() {
      return introduction;
   }

   public void setIntroduction(int introduction) {
      this.introduction = introduction;
   }

   public int getIsPublish() {
      return isPublish;
   }

   public void setIsPublish(int isPublish) {
      this.isPublish = isPublish;
   }
}