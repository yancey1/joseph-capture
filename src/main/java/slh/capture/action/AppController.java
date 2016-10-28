package slh.capture.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import slh.capture.common.DataGridModel;
import slh.capture.common.ResponseUtils;
import slh.capture.domain.AccountDomain;
import slh.capture.domain.AppDomain;
import slh.capture.service.IAccountService;
import slh.capture.service.IAppService;

@Controller
@RequestMapping("/appInfo/")
public class AppController {

  private final static Logger logger = LoggerFactory.getLogger(AppController.class);

  @Autowired
  private IAppService         appService;

  @Autowired
  private IAccountService     accountService;

  @RequestMapping(value = "list", method = RequestMethod.GET)
  public String list() {
    return "/app/list";
  }

  @RequestMapping(value = "list", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> list(AppDomain domain, DataGridModel page) {
    domain.setPageInfo(page);
    return appService.getAppList(domain);
  }

  @RequestMapping(value = "add", method = RequestMethod.GET)
  public String add() {
    return "/app/add";
  }

  @RequestMapping(value = "add", method = RequestMethod.POST)
  @ResponseBody
  public void add(AppDomain domain, HttpServletResponse response) {
    try {
      List<AppDomain> list = appService.getAppListByApp(domain);
      if (list != null && list.size() > 0) {
        ResponseUtils.responseInfoExists(response);
        return;
      }
      appService.saveApp(domain);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage());
      ResponseUtils.responseFailure(response);
    }
  }

  @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
  public String update(@PathVariable Integer id, Model model) {
    try {
      AppDomain domain = new AppDomain();
      domain.setAppId(id);
      List<AppDomain> list = appService.getAppListByApp(domain);
      if (!list.isEmpty()) {
        model.addAttribute("app", list.get(0));
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return "/app/update";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  @ResponseBody
  public void update(AppDomain domain, HttpServletResponse response) {
    try {
      List<AppDomain> list = appService.getAppListByApp(domain);
      if (list != null && list.size() > 0) {
        ResponseUtils.responseInfoExists(response);
        return;
      }
      appService.modifyApp(domain);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage());
      ResponseUtils.responseFailure(response);
    }
  }

  @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
  @ResponseBody
  public void delete(@PathVariable Integer id, HttpServletResponse response) {
    try {
      AppDomain domain = new AppDomain();
      domain.setId(id);
      appService.deleteApp(domain);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage());
      ResponseUtils.responseFailure(response);
    }
  }
  
  @RequestMapping(value = "getAppList/{accountId}", method = RequestMethod.POST)
  @ResponseBody
  public List<AppDomain> getAppList(@PathVariable Integer accountId) {
    AppDomain domain=new AppDomain();
    domain.setAccountId(accountId);
    return appService.getAppListByApp(domain);
  }
  
  @RequestMapping(value = "/getAccountList", method = RequestMethod.POST)
  @ResponseBody
  public List<AccountDomain> getAccountList() {
    return accountService.getAccountListByAccount(null);
  }

}
