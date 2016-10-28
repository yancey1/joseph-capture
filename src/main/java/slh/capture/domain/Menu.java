package slh.capture.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import slh.capture.common.CustomDateTimeSerializer;

public class Menu implements Cloneable {

  private Integer resourceId;

  private String  resourceName; // 资源模块名称

  private String  permission;  // 权限代码

  private String  resourceUrl; // 地址

  private String  parent;      // 父ID

  private String  description; // 菜单描述

  private String  createTime;  // 创建时间

  private String  modifyTime;  // 最后一次更新时间
  private String  displayType; // 展示类型
  private Integer displaySort; // 展示顺序

  private String  state;       // 菜单状态

  public String getDisplayType() {
    return displayType;
  }

  public void setDisplayType(String displayType) {
    this.displayType = displayType;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Integer getResourceId() {
    return resourceId;
  }

  public void setResourceId(Integer resourceId) {
    this.resourceId = resourceId;
  }

  public String getResourceName() {
    return resourceName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public String getResourceUrl() {
    return resourceUrl;
  }

  public void setResourceUrl(String resourceUrl) {
    this.resourceUrl = resourceUrl;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(String modifyTime) {
    this.modifyTime = modifyTime;
  }

  public Integer getDisplaySort() {
    return displaySort;
  }

  public void setDisplaySort(Integer displaySort) {
    this.displaySort = displaySort;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  @Override
  public String toString() {
    return "Menu [resourceId=" + resourceId + ", resourceName=" + resourceName + ", permission=" + permission + ", resourceUrl=" + resourceUrl + ", parent="
        + parent + ", displaySort=" + displaySort + ", displayType=" + displayType + ", description=" + description + ", state=" + state + "]";
  }

}
