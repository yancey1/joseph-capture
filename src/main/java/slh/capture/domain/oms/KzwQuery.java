package slh.capture.domain.oms;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class KzwQuery {
  
  @SerializedName("rows")
  private List<KzwQueryResult> rows;

  public List<KzwQueryResult> getRows() {
    return rows;
  }

  public void setRows(List<KzwQueryResult> rows) {
    this.rows = rows;
  }

}
