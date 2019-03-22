package llcweb.com.domain.models; /***********************************************************************
 * Module:  UsersRoles.java
 * Author:  Ricardo
 * Purpose: Defines the Class UsersRoles : 用户角色关系表
 ***********************************************************************/

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;

/** @pdOid b2ebeea8-a755-48d8-aece-a5f464024cd6 */
//@Entity
//@Table(name="users_roles")
public class UsersRoles {
   /** @pdOid 9f70ee88-6f28-4cb1-92df-abffe2ee22a5 */
//   @Id
//   @GeneratedValue
   public int urId;
   /** @pdOid b0c311d5-4df4-40a5-bb9c-d8e952567bcb */
   public int urUserId;
   /** @pdOid 2a446607-daf6-4332-96c9-4983a7a801f9 */
   public int urRoleId;
   /** @pdOid 0824dab2-5c2e-4c55-8dee-177660742620 */
   public String limitPages;


   public UsersRoles() {
   }
   public UsersRoles(int urUserId, int urRoleId, String limitPages) {
      this.urUserId = urUserId;
      this.urRoleId = urRoleId;
      this.limitPages = limitPages;
   }
   public UsersRoles(int urId, int urUserId, int urRoleId, String limitPages) {
      this.urId = urId;
      this.urUserId = urUserId;
      this.urRoleId = urRoleId;
      this.limitPages = limitPages;
   }

   public int getUrId() {
      return urId;
   }

   public void setUrId(int urId) {
      this.urId = urId;
   }

   public int getUrUserId() {
      return urUserId;
   }

   public void setUrUserId(int urUserId) {
      this.urUserId = urUserId;
   }

   public int getUrRoleId() {
      return urRoleId;
   }

   public void setUrRoleId(int urRoleId) {
      this.urRoleId = urRoleId;
   }

   public String getLimitPages() {
      return limitPages;
   }

   public void setLimitPages(String limitPages) {
      this.limitPages = limitPages;
   }
}