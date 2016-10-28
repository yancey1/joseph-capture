package slh.capture.domain.fun;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.gson.annotations.SerializedName;

public class FunChannel {

  @SerializedName("channelId")
  private String channelId;

  @SerializedName("sub_channel_id")
  private String subChannelId;

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public String getSubChannelId() {
    return subChannelId;
  }

  public void setSubChannelId(String subChannelId) {
    this.subChannelId = subChannelId;
  }
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
