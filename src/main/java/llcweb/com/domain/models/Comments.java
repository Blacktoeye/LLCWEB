package llcweb.com.domain.models;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author haien
 * @Description 访客留言类
 * @Date 2018/10/6
 **/
@Entity
@Table(name="comments")
public class Comments {
   @Id
   @GeneratedValue
   private int id;
   //
   private String name;
   //
   private String contact;
   //
   private String content;

   private Date date;
   private String ipAddress;//访客ip地址
   private int isView;//标记是否已经处理该留言

   public Comments() {
   }

   public Comments(String name, String contact, String content, Date date, String ipAddress, int isView) {
      this.name = name;
      this.contact = contact;
      this.content = content;
      this.date = date;
      this.ipAddress = ipAddress;
      this.isView = isView;
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

   public String getContact() {
      return contact;
   }

   public void setContact(String contact) {
      this.contact = contact;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public String getIpAddress() {
      return ipAddress;
   }

   public void setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
   }

   public int getIsView() {
      return isView;
   }

   public void setIsView(int isView) {
      this.isView = isView;
   }
}