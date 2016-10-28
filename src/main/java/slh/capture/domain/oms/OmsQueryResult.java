package slh.capture.domain.oms;

import com.google.gson.annotations.SerializedName;

public class OmsQueryResult {

  @SerializedName("rptdate")
  private String rpdate;

  @SerializedName("channel_no")
  private String channel_no;

  @SerializedName("game_1")
  private String game_1;

  public String getRpdate() {
    return rpdate;
  }

  public void setRpdate(String rpdate) {
    this.rpdate = rpdate;
  }

  public String getChannel_no() {
    return channel_no;
  }

  public void setChannel_no(String channel_no) {
    this.channel_no = channel_no;
  }

  public String getGame_1() {
    return game_1;
  }

  public void setGame_1(String game_1) {
    this.game_1 = game_1;
  }

}
