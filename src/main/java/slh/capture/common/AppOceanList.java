package slh.capture.common;

import java.util.ArrayList;
import java.util.List;

public enum AppOceanList {
  QMQSG("300008880289","全民切水果HD");
  private String channelCode;
  private String desc;
  
  private AppOceanList(String channelCode,String desc){
    this.channelCode=channelCode;
    this.desc=desc;
  }
  
  public static List<AppOceanList>  getAppList(){
    List<AppOceanList> list=new ArrayList<AppOceanList>();
    for (AppOceanList app : AppOceanList.values()) {
      list.add(app);
    }
    return list;
  }
  
  public String getChannelCode() {
    return channelCode;
  }
  public void setChannelCode(String channelCode) {
    this.channelCode = channelCode;
  }
  public String getDesc() {
    return desc;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  
}
