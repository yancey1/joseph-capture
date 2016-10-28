package slh.capture.domain.oms;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class KzwSdkQuery {
  
  @SerializedName("rows")
  private List<KzwSdkQueryResult> rows;

  public List<KzwSdkQueryResult> getRows() {
    return rows;
  }

  public void setRows(List<KzwSdkQueryResult> rows) {
    this.rows = rows;
  }

}
