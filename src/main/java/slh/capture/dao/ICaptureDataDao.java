package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.domain.CaptureDomain;

public interface ICaptureDataDao {

  public Map<String, Object> getCaptureDataList(CaptureDomain domain);

  public void saveCaptureData(List<CaptureDomain> list) throws Exception;

  public int checkCaptureData(CaptureDomain domain);
}
