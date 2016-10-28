package slh.capture.domain.oms;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ChannelQuery {
  @SerializedName("cpList")
  private List<AppQueryResult> cpList;

  public List<AppQueryResult> getCpList() {
    return cpList;
  }

  public void setCpList(List<AppQueryResult> cpList) {
    this.cpList = cpList;
  }
}
