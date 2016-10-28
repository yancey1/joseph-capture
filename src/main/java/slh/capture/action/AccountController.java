package slh.capture.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import slh.capture.common.ReturnJsonCode;
import slh.capture.domain.AccountDomain;
import slh.capture.domain.AppDomain;
import slh.capture.service.IAccountService;
import slh.capture.service.IAppService;

@Controller
@RequestMapping("/account/")
public class AccountController {

  private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

  @Autowired
  private IAccountService     accountService;

  @Autowired
  private IAppService         appService;
  
  @RequestMapping(value = "list", method = RequestMethod.GET)
  public String list() {
    return "/account/list";
  }

  @RequestMapping(value = "list", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> list(AccountDomain domain, DataGridModel page) {
    domain.setPageInfo(page);
    return accountService.getAccountList(domain);
  }

  @RequestMapping(value = "add", method = RequestMethod.GET)
  public String add() {
    return "/account/add";
  }

  @RequestMapping(value = "add", method = RequestMethod.POST)
  @ResponseBody
  public void add(AccountDomain domain, HttpServletResponse response) {
    try {
      List<AccountDomain> list=accountService.getAccountListByAccount(domain);
      if(list!=null&&list.size()>0){
        JSONObject resultJson = new JSONObject();
        resultJson.put(ReturnJsonCode.RETURN_CODE, ReturnJsonCode.MsgCodeEnum.INFO_EXISTS.getCode());
        resultJson.put(ReturnJsonCode.RETURN_MSG, "该账号已存在！");
        ResponseUtils.renderJson(response, resultJson.toString());
        return;
      }
      accountService.saveAccount(domain);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage());
      ResponseUtils.responseFailure(response);
    }
  }

  @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
  public String update(@PathVariable Integer id, Model model) {
    try {
      AccountDomain domain = new AccountDomain();
      domain.setAccountId(id);
      List<AccountDomain> list = accountService.getAccountListByAccount(domain);
      if (!list.isEmpty()) {
        model.addAttribute("account", list.get(0));
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return "/account/update";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  @ResponseBody
  public void update(AccountDomain domain, HttpServletResponse response) {
    try {
      List<AccountDomain> list=accountService.getAccountListByAccount(domain);
      if(list!=null&&list.size()>0){
        JSONObject resultJson = new JSONObject();
        resultJson.put(ReturnJsonCode.RETURN_CODE, ReturnJsonCode.MsgCodeEnum.INFO_EXISTS.getCode());
        resultJson.put(ReturnJsonCode.RETURN_MSG, "该账号已存在！");
        ResponseUtils.renderJson(response, resultJson.toString());
        return;
      }
      accountService.modifyAccount(domain);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage());
      ResponseUtils.responseFailure(response);
    }
  }

  @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
  @ResponseBody
  public void delete(@PathVariable Integer id, HttpServletResponse response) {
    try {
      AppDomain dd=new AppDomain();
      dd.setAccountId(id);
      List<AppDomain> appList=appService.getAppListByApp(dd);
      if(appList!=null&&appList.size()>0){
        ResponseUtils.responseBeenApplied(response);
        return;
      }
      AccountDomain domain = new AccountDomain();
      domain.setId(id);
      accountService.deleteAccount(domain);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage());
      ResponseUtils.responseFailure(response);
    }
  }

}
