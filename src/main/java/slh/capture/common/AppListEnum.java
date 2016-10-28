package slh.capture.common;

import java.util.ArrayList;
import java.util.List;

public enum AppListEnum {
  XIAOXINXIN("300008730823","天天消星星2015-MM版"),PAOPAOLONG("300008758204","泡泡龙2015-MM版");
  
  private String channelCode;
  private String desc;
  
  private AppListEnum(String channelCode,String desc){
    this.channelCode=channelCode;
    this.desc=desc;
  }
  
  public static List<AppListEnum>  getAppList(){
    List<AppListEnum> list=new ArrayList<AppListEnum>();
    for (AppListEnum app : AppListEnum.values()) {
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
