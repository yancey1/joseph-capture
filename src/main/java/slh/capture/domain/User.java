package slh.capture.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import slh.capture.common.CustomDateTimeSerializer;
import slh.capture.common.DataGridModel;

public class User implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private Integer           userId;               // 用户uid
  private String            userName;             // 登录用户名
  private String            nickname;             // 用户昵称
  private String            password;             // 密码
  private String            status;               // 1:启用 0:禁用
  private String            description;          // 备注
  private Date              createTime;
  private Date              modifyTime;           // 手机号
  private Integer[]         roleId;
  private List<UserRole>    roles;
  private DataGridModel     pageInfo;
  private List<Role>        roleList;
  private String            roleNames;
  private Integer           rId;
  private List<Menu>        menuList;

  public List<Menu> getMenuList() {
    return menuList;
  }

  public void setMenuList(List<Menu> menuList) {
    this.menuList = menuList;
  }

  public Integer getrId() {
    return rId;
  }

  public void setrId(Integer rId) {
    this.rId = rId;
  }

  public String getRoleNames() {
    return roleNames;
  }

  public void setRoleNames(String roleNames) {
    this.roleNames = roleNames;
  }

  public List<Role> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<Role> roleList) {
    this.roleList = roleList;
  }

  public User() {
    super();
  }

  public Integer[] getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer[] roleId) {
    this.roleId = roleId;
  }

  public List<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(List<UserRole> roles) {
    this.roles = roles;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }

  public DataGridModel getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(DataGridModel pageInfo) {
    this.pageInfo = pageInfo;
  }

  @Override
  public String toString() {
    return "User [userId=" + userId + ", userName=" + userName + ", nickname=" + nickname + ", password=" + password + ", status=" + status + ", description="
        + description + ", createTime=" + createTime + ", modifyTime=" + modifyTime + "]";
  }

}
