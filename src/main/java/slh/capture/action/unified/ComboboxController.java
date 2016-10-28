package slh.capture.action.unified;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import slh.capture.common.ResponseUtils;
import slh.capture.domain.unified.CaptureConfigEntity;
import slh.capture.service.unified.ICaptureConfigService;

/**
 * 下拉列表数据
 * 
 * @author xuwenqiang
 * */
@Controller
@RequestMapping("/combobox")
public class ComboboxController {

  @Autowired
  private ICaptureConfigService captureConfigService;

  /**
   * 根据company 动态获取供应商
   * 
   * @param request
   * @param response
   * @param model
   */
  @RequestMapping(value = "/cp/list", method = RequestMethod.GET)
  public void findCpList(HttpServletRequest request, HttpServletResponse response) {
    String cpName = request.getParameter("q");
    CaptureConfigEntity form = new CaptureConfigEntity();
    if (StringUtils.isNotBlank(cpName)) {
      form.setCpName(cpName);
    }

    List<CaptureConfigEntity> list = captureConfigService.findCpList(form);
    JSONArray resultJson = JSONArray.fromObject(list);
    ResponseUtils.renderJson(response, resultJson.toString());

  }

  /**
   * 根据company 动态获取渠道信息
   * 
   * @param request
   * @param response
   * @param model
   */
  @RequestMapping(value = "/channel/list/{userName}/{cpName}", method = RequestMethod.GET)
  public void findChannelList(@PathVariable String userName,@PathVariable String cpName, HttpServletRequest request, HttpServletResponse response, Model model) {
    CaptureConfigEntity form = new CaptureConfigEntity();
    if (StringUtils.isNotBlank(userName)) {
      form.setUserName(userName);
    }
    if (StringUtils.isNotBlank(cpName)) {
      form.setCpName(cpName);
    }
    List<CaptureConfigEntity> list = captureConfigService.findChannelList(form);
    JSONArray resultJson = JSONArray.fromObject(list);
    ResponseUtils.renderJson(response, resultJson.toString());
  }

  /**
   * 根据引入应用名称 动态获取应用
   * 
   * @param request
   * @param response
   * @param model
   */
  @RequestMapping(value = "/app/list/{cpName}/{userName}/{channelCode}", method = RequestMethod.GET)
  public void findAppList(@PathVariable String cpName,@PathVariable String userName, @PathVariable String channelCode,HttpServletRequest request, HttpServletResponse response, Model model) {
    CaptureConfigEntity form = new CaptureConfigEntity();
    if (StringUtils.isNotBlank(channelCode)) {
      form.setChannelCode(channelCode);
    }
    if (StringUtils.isNotBlank(userName)) {
      form.setUserName(userName);
    }
    if (StringUtils.isNotBlank(cpName)) {
      form.setCpName(cpName);
    }
    JSONArray resultJson = JSONArray.fromObject(captureConfigService.findAppList(form));
    ResponseUtils.renderJson(response, resultJson.toString());
  }

  /**
   * 根据company 动态获取渠道信息
   * 
   * @param request
   * @param response
   * @param model
   */
  @RequestMapping(value = "/user/list/{cpName}", method = RequestMethod.GET)
  public void findUserlList(@PathVariable String cpName,  HttpServletRequest request,
      HttpServletResponse response, Model model) {
    CaptureConfigEntity form = new CaptureConfigEntity();
    if (StringUtils.isNotBlank(cpName)) {
      form.setCpName(cpName);
    }
    List<CaptureConfigEntity> list = captureConfigService.findUserList(form);
    JSONArray resultJson = JSONArray.fromObject(list);
    ResponseUtils.renderJson(response, resultJson.toString());
  }
  
  @RequestMapping(value = "/checkCode", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, String> checkCode(HttpServletRequest request,@RequestParam String name,@RequestParam String user, HttpServletResponse response) {
    Map<String, String> map=new HashMap<String, String>();
    CaptureConfigEntity form = new CaptureConfigEntity();
    if (StringUtils.isNotBlank(name)) {
      form.setCpName(name);
    }
    if (StringUtils.isNotBlank(user)) {
      form.setUserName(user);
    }
    List<CaptureConfigEntity> list = captureConfigService.checkCode(form);
    if(list.size()>0){
      CaptureConfigEntity entity=list.get(0);
      map.put("code",entity.getIdentifyType()+":"+entity.getTimeQueryType());
    }
    return map;
  }
}
