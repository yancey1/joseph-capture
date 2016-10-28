package slh.capture.domain.oms;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OmsQuery {
  @SerializedName("total")
  private String               total;

  @SerializedName("rows")
  private List<OmsQueryResult> rows;

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public List<OmsQueryResult> getRows() {
    return rows;
  }

  public void setRows(List<OmsQueryResult> rows) {
    this.rows = rows;
  }

}
