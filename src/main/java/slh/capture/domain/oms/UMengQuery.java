package slh.capture.domain.oms;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UMengQuery {

  @SerializedName("stats")
  private List<UMengQueryResult> rows;

  public List<UMengQueryResult> getRows() {
    return rows;
  }

  public void setRows(List<UMengQueryResult> rows) {
    this.rows = rows;
  }

}
