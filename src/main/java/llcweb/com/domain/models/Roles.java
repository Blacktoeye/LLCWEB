package llcweb.com.domain.models; /***********************************************************************
 * Module:  Roles.java
 * Author:  Ricardo
 * Purpose: Defines the Class Roles
 ***********************************************************************/

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 角色表
 */
@Entity
@Table(name = "roles")
public class Roles implements Serializable {
   /** @pdOid f16a930e-44f7-408b-a69e-7ff0a4f31fe4 */
   @Id
   private int rId;
   private String rName;
   private String rFlag;

   public Roles(int rId, String rName, String rFlag) {
      this.rId = rId;
      this.rName = rName;
      this.rFlag = rFlag;
   }
   public Roles() {
   }
   public int getrId() {
      return rId;
   }

   public void setrId(int rId) {
      this.rId = rId;
   }

   public String getrName() {
      return rName;
   }

   public void setrName(String rName) {
      this.rName = rName;
   }

   public String getrFlag() {
      return rFlag;
   }

   public void setrFlag(String rFlag) {
      this.rFlag = rFlag;
   }
}