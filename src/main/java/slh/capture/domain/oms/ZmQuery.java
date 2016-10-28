package slh.capture.domain.oms;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ZmQuery {
  
  @SerializedName("Rows")
  private List<ZmQueryResult> rows;

  public List<ZmQueryResult> getRows() {
    return rows;
  }

  public void setRows(List<ZmQueryResult> rows) {
    this.rows = rows;
  }
}
