package slh.capture.domain.oms;

import com.google.gson.annotations.SerializedName;

public class KzwQueryResult {
  
  @SerializedName("groupType")
  private String groupType;
  
  @SerializedName("reguser")
  private String credituser;

  public String getGroupType() {
    return groupType;
  }

  public void setGroupType(String groupType) {
    this.groupType = groupType;
  }

  public String getCredituser() {
    return credituser;
  }

  public void setCredituser(String credituser) {
    this.credituser = credituser;
  }
  

  
}
