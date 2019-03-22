package llcweb.com.domain.models;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * 用户表
 */
@Entity
@Table(name="users")
public class Users implements Serializable,UserDetails {
   /**
    * user id
    */
   @Id
   @GeneratedValue
   private int id;
   /**
    * user name
    */
   private String username;
   //用户密码
   private String password;
   //更新日期
   private Date updateTime;
   //人物id
   private int peopleId;
   //所属组别
   private String team;

   @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
   @JoinTable(
           name = "users_roles",
           joinColumns = {
                   @JoinColumn(name = "ur_user_id")
           },
           inverseJoinColumns = {
                   @JoinColumn(name = "ur_role_id")
           }
   )
   //角色表
   private List<Roles> roles;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      List<GrantedAuthority> auths = new ArrayList<>();
      List<Roles> roles = getRoles();
      for(Roles role : roles)
      {
         auths.add(new SimpleGrantedAuthority(role.getrFlag()));
      }
      return auths;
   }
   public Users(){
   }
   public Users(Users users){
      this.id = users.id;
      this.username = users.username;
      this.password = users.password;
      this.updateTime = users.updateTime;
      this.peopleId = users.peopleId;
      this.team = users.team;
      this.roles = users.roles;
   }
   public Users(int id,String username,String password,Date updateTime,int peopleId,String team, 
                List<Roles> roles){
      this.id = id;
      this.username = username;
      this.password = password;
      this.updateTime = updateTime;
      this.peopleId = peopleId;
      this.team = team;
      this.roles = roles;
   }

   public String getTeam() {
	return team;
}
public void setTeam(String team) {
	this.team = team;
}
@Override
   public String getPassword() {
      return this.password;
   }

   @Override
   public String getUsername() {
      return this.username;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

   public List<Roles> getRoles() {
      return roles;
   }

   public void setRoles(List<Roles> roles) {
      this.roles = roles;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Date getUpdateTime() {
      return updateTime;
   }

   public void setUpdateTime(Date updateTime) {
      this.updateTime = updateTime;
   }

   public int getPeopleId() {
      return peopleId;
   }

   public void setPeopleId(int peopleId) {
      this.peopleId = peopleId;
   }
}