package slh.capture.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import slh.capture.common.DataGridModel;
import slh.capture.common.ResponseUtils;
import slh.capture.common.ReturnJsonCode;
import slh.capture.domain.CaptureDomain;
import slh.capture.service.ICaptureDataService;

@Controller
@RequestMapping("/captureData/")
public class CaptureDataController {

  private final static Logger logger = LoggerFactory.getLogger(CaptureDataController.class);

  @Autowired
  private ICaptureDataService captureDataService;

  @RequestMapping(value = "list", method = RequestMethod.GET)
  public String list() {
    return "/capture/list";
  }

  @RequestMapping(value = "list", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> list(CaptureDomain domain, DataGridModel page) {
    domain.setPageInfo(page);
    return captureDataService.getCaptureDataList(domain);
  }

  @RequestMapping(value = "getData", method = RequestMethod.POST)
  @ResponseBody
  public void getData(HttpServletRequest request, CaptureDomain domain, HttpServletResponse response) {
    try {
      SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      cal.add(Calendar.DATE, -1);
      domain.setStatisDate(sim.format(cal.getTime()));
      if (captureDataService.checkCaptureData(domain) > 0) {
        JSONObject resultJson = new JSONObject();
        resultJson.put(ReturnJsonCode.RETURN_CODE, ReturnJsonCode.MsgCodeEnum.FAILURE.getCode());
        resultJson.put(ReturnJsonCode.RETURN_MSG, "昨日数据已拉取！");
        ResponseUtils.renderJson(response, resultJson.toString());
        return;
      }
      
      if(!captureDataService.saveCaptureData(request, domain)){
        ResponseUtils.responseFailure(response);
        return;
      }
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage());
      ResponseUtils.responseFailure(response);
    }
  }

}
