package slh.capture.domain.oms;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class WbQuery {
  @SerializedName("totalCount")
  private String totalCount;
  
  @SerializedName("root")
  private List<WbQueryResult> root;

  public String getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(String totalCount) {
    this.totalCount = totalCount;
  }

  public List<WbQueryResult> getRoot() {
    return root;
  }

  public void setRoot(List<WbQueryResult> root) {
    this.root = root;
  }

  
}
