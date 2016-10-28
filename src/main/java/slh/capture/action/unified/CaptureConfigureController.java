package slh.capture.action.unified;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import slh.capture.action.CommonController;
import slh.capture.common.DataGridModel;
import slh.capture.common.ResponseUtils;
import slh.capture.domain.unified.CaptureConfigEntity;
import slh.capture.service.unified.ICaptureConfigService;

/**
 * 统一数据配置
 * 
 * @author yw
 * */
@Controller
@RequestMapping("/captureConfig")
public class CaptureConfigureController extends CommonController {

  private static final Logger   logger = LoggerFactory.getLogger(CaptureConfigureController.class);

  @Autowired
  private ICaptureConfigService captureConfigService;

  /**
   * 跳转到统一抓取页面
   * 
   * @return
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String getList() {
    return "captureConfig/list";
  }

  @RequestMapping(value = "/list", params = "json")
  @ResponseBody
  public Map<String, Object> queryList(DataGridModel page, CaptureConfigEntity form) throws Exception {
    return captureConfigService.queryCaptureConfigList(page, form);
  }

  /**
   * 跳转到添加页面
   * 
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String toAdd() {
    return "captureConfig/add";
  }

  /**
   * 添加抓取配置信息
   * 
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  public void add(CaptureConfigEntity form, HttpServletResponse response, Model model) {
    try {
      captureConfigService.saveCaptureConfig(form);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e1) {
      ResponseUtils.responseFailure(response);
    }
  }

  /**
   * 跳转到编辑角色界面
   * 
   * @return
   */
  @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
  public String toUpdate(@PathVariable String id, Model model) {
    CaptureConfigEntity form = new CaptureConfigEntity();
    form.setId((StringUtils.isNotBlank(id) ? Integer.parseInt(id) : null));
    try {
      model.addAttribute("captureConfig", captureConfigService.findCaptureConfigById(form));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "captureConfig/update";
  }

  /**
   * 编辑角色
   * 
   * @return
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public void update(CaptureConfigEntity form, HttpServletResponse response, Model model) {
    try {
      captureConfigService.updateCaptureConfigById(form);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      e.printStackTrace();
      ResponseUtils.responseFailure(response);
    }
  }

  /**
   * 删除
   * 
   * @return
   */
  @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
  public void delete(@PathVariable String id, HttpServletResponse response, Model model) {
    CaptureConfigEntity captureConfig = new CaptureConfigEntity();
    captureConfig.setId(Integer.valueOf(id));
    try {
      captureConfigService.removeCaptureConfig(captureConfig);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      ResponseUtils.responseFailure(response);
    }
  }
}
