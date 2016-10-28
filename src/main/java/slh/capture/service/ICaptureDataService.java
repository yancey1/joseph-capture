package slh.capture.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import slh.capture.domain.CaptureDomain;

public interface ICaptureDataService {

  public Map<String, Object> getCaptureDataList(CaptureDomain domain);

  public boolean saveCaptureData(HttpServletRequest request, CaptureDomain domain) throws Exception;

  public int checkCaptureData(CaptureDomain domain);
}
