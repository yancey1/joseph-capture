package slh.capture.domain.zsjh;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ZsjhQuery {
  @SerializedName("revenues")
  private String                revenues;

  @SerializedName("list")
  private List<ZsjhQueryResult> list;

  public String getRevenues() {
    return revenues;
  }

  public void setRevenues(String revenues) {
    this.revenues = revenues;
  }

  public List<ZsjhQueryResult> getList() {
    return list;
  }

  public void setList(List<ZsjhQueryResult> list) {
    this.list = list;
  }

}
