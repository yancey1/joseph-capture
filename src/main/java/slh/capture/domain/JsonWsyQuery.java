package slh.capture.domain;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class JsonWsyQuery {

  @SerializedName("rows")
  private List<JsonWsyQueryResult> footer;

  public List<JsonWsyQueryResult> getFooter() {
    return footer;
  }

  public void setFooter(List<JsonWsyQueryResult> footer) {
    this.footer = footer;
  }

}
