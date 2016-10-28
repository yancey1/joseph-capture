package slh.capture.util;

import slh.capture.domain.unified.CaptureConfigEntity;
import slh.capture.form.unified.CaptureQueryConditionForm;

/**
 * 类型转换工具类
 * 
 * @author xuwenqiang
 * */
public class ObjectConvertUtils {

  public static CaptureConfigEntity convertToCaptureConfig(CaptureQueryConditionForm from) {
    CaptureConfigEntity captureConfigEntity = new CaptureConfigEntity();

    if (from != null) {
      captureConfigEntity.setAppName(from.getAppName());
      captureConfigEntity.setChannelCode(from.getChannelCode());
      captureConfigEntity.setCpName(from.getCpName());
      captureConfigEntity.setUserName(from.getUserName());
    }

    return captureConfigEntity;
  }

}
