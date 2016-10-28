package slh.capture.domain.oms;

import com.google.gson.annotations.SerializedName;

public class KzwSdkQueryResult {
  
  @SerializedName("grouptype")
  private String groupType;
  
  @SerializedName("totalamt")
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
