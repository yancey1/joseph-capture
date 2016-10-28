package slh.capture.service.unified;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.client.CloseableHttpClient;

import slh.capture.domain.ScrabData;
import slh.capture.form.unified.CaptureQueryConditionForm;
import slh.capture.service.IScrabService;

/**
 * 通用数据抓取服务接口
 * */
public interface IUnifiedCaptureService extends IScrabService {

  /**
   * 执行抓取操作
   * 
   */
  public List<ScrabData> execute(CloseableHttpClient client, CaptureQueryConditionForm form,HttpServletRequest request, HttpServletResponse response);
  
  public void delete(ScrabData form);
}
