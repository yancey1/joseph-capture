package slh.capture.action.unified;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import slh.capture.action.CommonController;
import slh.capture.common.ConstantsCMP;
import slh.capture.common.DataGridModel;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.ResponseUtils;
import slh.capture.common.unified.RandomCodeTypeEnum;
import slh.capture.domain.ScrabData;
import slh.capture.domain.unified.CaptureConfigEntity;
import slh.capture.form.unified.CaptureQueryConditionForm;
import slh.capture.service.IScrabService;
import slh.capture.service.unified.ICaptureConfigService;
import slh.capture.service.unified.IUnifiedCaptureService;

/**
 * 统一数据抓取
 *  
 * @author xuwenqiang
 * */
@Controller 
@RequestMapping("/unifiedCapture")
public class UnifiedCaptureController extends CommonController {

  private static final Logger    logger = LoggerFactory.getLogger(UnifiedCaptureController.class);

  @Autowired
  private IUnifiedCaptureService unifiedCaptureService;

  @Autowired
  private ICaptureConfigService  captureConfigService;

  @Autowired
  private IScrabService          scrabService;

  /**
   * 跳转到统一抓取页面
   * 
   * @return
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String getList() {

    return "unifiedCapture/list";
  }

  /**
   * 抓取数据列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/list", params = "json")
  @ResponseBody
  public Map<String, Object> getScrabList(DataGridModel page, CaptureConfigEntity form) throws Exception {
    if (form.getState() != null && form.getState() == -1) {
      form.setState(null);
    }
    ScrabData scrabData = new ScrabData();
    if (StringUtils.isNotBlank(form.getCpName())) {
      scrabData.setBookMark(form.getCpName());
    }
    scrabData.setState(form.getState());
    scrabData.setProductName(form.getAppName());
    scrabData.setChannelId(form.getChannelCode());
    scrabData.setStartTime(form.getStartTime());
    scrabData.setEndTime(form.getEndTime());
    scrabData.setUserName(form.getUserName());
    return scrabService.getScrabData(page, scrabData);
  }

  /**
   * 通用数据拉取
   * 
   * @param request
   * @param response
   */
  @RequestMapping(value = "/pull")
  public void unifiedCaptureData(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    CloseableHttpClient httpClient = (CloseableHttpClient) session.getAttribute("client");
    if (httpClient == null) {
      HttpClientBuilder clientBuilder = HttpClientBuilder.create();
      RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(5000).setConnectionRequestTimeout(5000)
          .setStaleConnectionCheckEnabled(true).build();
      clientBuilder.setDefaultRequestConfig(defaultRequestConfig);
      httpClient = clientBuilder.build();
    }
    CaptureQueryConditionForm from = new CaptureQueryConditionForm();
    from.setStartDate(request.getParameter("startTime"));
    from.setEndDate(request.getParameter("endTime"));
    from.setRandomCode(request.getParameter("randomCode"));
    from.setCpName(request.getParameter("cpName"));
    from.setChannelCode(request.getParameter("channelCode"));
    from.setAppName(request.getParameter("appName"));
    from.setUserName(request.getParameter("userName"));
    try {
      // 登录天奕达数据查询系统并读取要抓取的数据
      List<ScrabData> list = unifiedCaptureService.execute(httpClient, from,request,response);
      // 验证抓取的数据的应用和对应渠道是否应经存在数据库
      list = validateCaptureData(list);
      for (ScrabData scrabData : list) {
        scrabData.setBookMark(request.getParameter("cpName"));
        scrabData.setState(ConstantsCMP.CAPTURE_DATA_STATE_NO);
      }
      // 验证当前时间段内是否已经有抓取过数据
      ScrabData form = new ScrabData();
      form.setStartTime(request.getParameter("startTime"));
      form.setEndTime(request.getParameter("endTime"));
      form.setBookMark(request.getParameter("cpName"));
      form.setUserName(request.getParameter("userName"));
      List<ScrabData> ls = unifiedCaptureService.getScrabDataList(form);
      saveData(list, ls);// 保存抓取的数据
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      ResponseUtils.responseFailure(response);
    }
  }

  /**
   * 获取统一验证码
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping("/unifiedCaptchaImage")
  public ModelAndView unifiedCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpSession ss=request.getSession();
    String cap=HtmlParserUtils.getInputTagValues("https://www.umeng.com/sso/login", "id", "captcha_i");
    ss.setAttribute("captcha",cap );
    response.setDateHeader("Expires", 0);
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.addHeader("Cache-Control", "post-check=0, pre-check=0");
    response.setHeader("Pragma", "no-cache");
    response.setContentType("image/jpeg");
    byte[] data = new byte[] {};
    data = unifiedCaptureService.getLoginImageData("https://pin.aliyun.com/get_img?sessionid="+cap+"&identity=umeng_sso.umeng.com&type=150_40", request);
    ServletOutputStream out = response.getOutputStream();
    out.write(data);
    try {
      if (out != null) {
        out.flush();
      }
    } finally {
      out.close();
    }
    return null;
  }
  
  /**
   * 抓取的数据存到数据库
   * 
   * @param response
   * @param form
   */
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public void saveScrabData(HttpServletResponse response, HttpServletRequest request) {
    try {
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String appName = request.getParameter("appName");
      String channelCode = request.getParameter("channelCode");
      String cpName = request.getParameter("cpName");
      ScrabData form = new ScrabData();
      form.setStartTime(startTime);
      form.setEndTime(endTime);
      form.setChannelId(channelCode);
      form.setProductName(appName);
      form.setBookMark(cpName);
      this.saveScrabData(form, response);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      ResponseUtils.responseFailure(response);
    }
  }
  
  /**
   * 修改抓取的数据
   * 
   * @param response
   * @param form
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public void updateScrabData(HttpServletResponse response, HttpServletRequest request) {
    try {
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String appName = request.getParameter("appName");
      String channelCode = request.getParameter("channelCode");
      String cpName = request.getParameter("cpName");
      String userName = request.getParameter("userName");
      ScrabData form = new ScrabData();
      form.setStartTime(startTime);
      form.setEndTime(endTime);
      form.setChannelId(channelCode);
      form.setProductName(appName);
      form.setBookMark(cpName);
      form.setUserName(userName);
      form.setState(ConstantsCMP.CAPTURE_DATA_STATE_YES);
      updateScrabData(form, response);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      ResponseUtils.responseFailure(response);
    }
  }
  
  /**
   * 
   * 抓取数据删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @ResponseBody
  public void deleteScrabData(HttpServletResponse response, HttpServletRequest request) {
    try {
      ScrabData form = new ScrabData();
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String appName = request.getParameter("appName");
      String channelCode = request.getParameter("channelCode");
      String cpName = request.getParameter("cpName");
      String userName = request.getParameter("userName");
      String state = request.getParameter("state");
      if (Integer.parseInt(state) == -1) {
        form.setState(null);
      }else{
        form.setState(Integer.parseInt(state));
      }
      form.setStartTime(startTime);
      form.setEndTime(endTime);
      form.setChannelId(channelCode);
      form.setProductName(appName);
      form.setBookMark(cpName);
      form.setUserName(userName);
      unifiedCaptureService.delete(form);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      ResponseUtils.responseFailure(response);
    }
  }
  
  @RequestMapping(value = "/getYearList", method = RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> getYearList(){
    List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
    Calendar cal = Calendar.getInstance();//得到一个Calendar的实例 
    Map<String, Object> mm=new HashMap<String, Object>();
    mm.put("year", "请选择年份");
    mm.put("id", "0");
    list.add(mm);
    for (int i = 0; i < 3; i++) {
      cal.setTime(new Date());
      Map<String, Object> map=new HashMap<String, Object>();
      cal.add(Calendar.YEAR, -i);
      map.put("year", cal.get(Calendar.YEAR));
      map.put("id", cal.get(Calendar.YEAR));
      list.add(map);
    }
   return list;
  }
}
