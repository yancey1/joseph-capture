package slh.capture.domain.oms;

import com.google.gson.annotations.SerializedName;

public class UMengQueryResult {

  @SerializedName("id")
  private String id;

  @SerializedName("name")
  private String name;

  @SerializedName("install")
  private String install;

  @SerializedName("active_user")
  private String active_user;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInstall() {
    return install;
  }

  public void setInstall(String install) {
    this.install = install;
  }

  public String getActive_user() {
    return active_user;
  }

  public void setActive_user(String active_user) {
    this.active_user = active_user;
  }

}
