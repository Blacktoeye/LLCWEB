package llcweb.com.domain.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author haien
 * @Description 网站参数类
 * @Date 2018/10/6
 **/
@Entity
@Table(name="params")
public class Params {
   @Id
   @GeneratedValue
   private int id;
   private String name;
   private String value;

   private String description;

   public Params() {
   }

   public Params(String name, String value, String description) {
      this.name = name;
      this.value = value;
      this.description = description;
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

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }
}