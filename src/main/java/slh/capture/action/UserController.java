package slh.capture.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import slh.capture.common.ConstantsCMP;
import slh.capture.common.DataGridModel;
import slh.capture.common.EncryptUtils;
import slh.capture.common.ResponseUtils;
import slh.capture.common.ReturnJsonCode;
import slh.capture.domain.Role;
import slh.capture.domain.User;
import slh.capture.domain.UserRole;
import slh.capture.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private IUserService        userService;

  /**
   * 用户中心-后台首页
   * 
   * @return
   */
  @RequestMapping(value = "/index")
  public String index(HttpServletRequest request, Model model) {

    try {
      model.addAttribute("user", ConstantsCMP.getSessionUser(request));
    } catch (Exception e) {
      logger.debug(e.getMessage(), e);
    }
    return "user/index";
  }

  /**
   * 用户管理
   * 
   * @param model
   * @return
   */
  @RequestMapping(value = "list", method = RequestMethod.GET)
  public String list(Model model) {

    return "user/list";
  }

  @RequestMapping(value = "/list", params = "json")
  @ResponseBody
  public Map<String, Object> queryList(DataGridModel page, User form) throws Exception {
    return userService.findUserPageList(page, form);
  }

  /**
   * 跳转到新增用户界面
   * 
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add() {
    return "user/add";
  }

  /**
   * 保存用户并跳转至列表页
   * 
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  public void add(User form, HttpServletResponse response, Model model) {

    try {

      // 验证用户是否已经存在
      if (StringUtils.isNotBlank(form.getUserName())) {
        User query = new User();
        query.setUserName(form.getUserName());

        List<User> list = userService.findUserList(query);
        if (list != null && list.size() > 0) {
          ResponseUtils.responseInfoExists(response);
        } else {
          // 加密用户密码
          if (StringUtils.isNotBlank(form.getPassword())) {
            form.setPassword(EncryptUtils.encryptMD5(form.getPassword()));
          }

          userService.saveUser(form);

          // 存用户的角色
          Integer[] roleIds = form.getRoleId();
          List<UserRole> ls = new ArrayList<UserRole>();
          for (int i = 0; i < roleIds.length; i++) {
            UserRole role = new UserRole();
            role.setUserId(form.getUserId());
            role.setRoleId(roleIds[i]);
            ls.add(role);
          }
          userService.saveUserRoles(ls);
          ResponseUtils.responseSuccess(response);
        }
      }

    } catch (Exception e1) {
      logger.error(e1.getMessage(), e1);
      ResponseUtils.responseFailure(response);
    }
  }

  /**
   * 跳转到编辑用户界面
   * 
   * @return
   */
  @RequestMapping(value = "/{userId}/update", method = RequestMethod.GET)
  public String update(@PathVariable String userId, Model model) {
    User form = new User();
    form.setUserId(StringUtils.isNotBlank(userId) ? Integer.parseInt(userId) : null);

    try {
      User user = userService.findUser(form);
      String roleNames = "";
      List<Role> list = user.getRoleList();
      if (list != null && list.size() > 0) {
        for (Role role : list) {
          roleNames += role.getRoleName();
          roleNames += ";";
        }
      }

      if (roleNames != "") {
        user.setRoleNames(roleNames.substring(0, roleNames.length() - 1));
      }

      model.addAttribute("user", user);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return "user/update";
  }

  /**
   * 编辑用户
   * 
   * @return
   */
  @RequestMapping(value = "/{userId}/update", method = RequestMethod.POST)
  public void update(@PathVariable String userId, User form, HttpServletResponse response, Model model) {
    // TODO 待完成：用户所属角色逻辑
    User User = new User();
    User.setUserName(form.getUserName());
    form.setUserId(StringUtils.isNotBlank(userId) ? Integer.parseInt(userId) : null);

    try {
      User currentUser = userService.findUser(User);
      // 校验是否同一个用户
      if (currentUser == null || currentUser.getUserId().equals(form.getUserId().intValue())) {
        // 验证用户是否重置了密码
        if (StringUtils.isNotBlank(form.getPassword())) {
          form.setPassword(EncryptUtils.encryptMD5(form.getPassword()));
        }
        userService.modifyUser(form);

        // 更新用户角色
        UserRole role = new UserRole();
        role.setUserId(form.getUserId());

        userService.removeUserRole(role);

        Integer[] roleIds = form.getRoleId();
        List<UserRole> list = new ArrayList<UserRole>();
        for (int i = 0; i < roleIds.length; i++) {
          UserRole role1 = new UserRole();
          role1.setUserId(form.getUserId());
          role1.setRoleId(roleIds[i]);
          list.add(role1);
        }
        userService.saveUserRoles(list);
        ResponseUtils.responseSuccess(response);
      } else { // 登录名重复，输入其他
        ResponseUtils.responseInfoExists(response);
        return;
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      ResponseUtils.responseFailure(response);
    }
  }

  /**
   * 删除用户
   * 
   * @return
   */
  @RequestMapping(value = "/{userId}/delete", method = RequestMethod.POST)
  public void delete(@PathVariable String userId, HttpServletResponse response, Model model) {

    Integer uId = Integer.parseInt(userId);
    try {
      userService.deleteUser(uId);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      ResponseUtils.responseFailure(response);
    }
  }

  /**
   * 跳转到修改密码界面
   * 
   * @return
   */
  @RequestMapping(value = "/resetPwd", method = RequestMethod.GET)
  public String resetPwd() {
    return "user/password";
  }
  /**
   * 修改密码
   * 
   * @return
   */
  @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
  @ResponseBody
  public void resetPwd(HttpServletRequest request, HttpServletResponse response, Model model) {
    String pwd = request.getParameter("pwd").replaceAll(" ", "");
    String npwd = request.getParameter("npwd").replaceAll(" ", "");
    String rpwd = request.getParameter("rpwd").replaceAll(" ", "");
    JSONObject resultJson = new JSONObject();
    try {
      User user = ConstantsCMP.getSessionUser(request);
      // 1、验证当前密码是否正确
      if (!user.getPassword().equals(EncryptUtils.encryptMD5(pwd))) {
        resultJson.put(ReturnJsonCode.RETURN_CODE, "100");
        resultJson.put(ReturnJsonCode.RETURN_MSG, "原密码输入不正确！");
        ResponseUtils.renderJson(response, resultJson.toString());
        return;
      }
      // 2、验证新密码是否相等
      if (!npwd.equals(rpwd)) {
        resultJson.put(ReturnJsonCode.RETURN_CODE, "101");
        resultJson.put(ReturnJsonCode.RETURN_MSG, "新密码两次输入不一致！");
        ResponseUtils.renderJson(response, resultJson.toString());
        return;
      }
      User form = new User();
      form.setUserId(user.getUserId());
      form.setPassword(EncryptUtils.encryptMD5(rpwd));
      userService.modifyUser(form);
      resultJson.put(ReturnJsonCode.RETURN_CODE, ReturnJsonCode.MsgCodeEnum.SUCCESS.getCode());
      resultJson.put(ReturnJsonCode.RETURN_MSG, "密码修改成功，请重新登录系统！");
      ResponseUtils.renderJson(response, resultJson.toString());

      Subject currentUser = SecurityUtils.getSubject();
      try {
        currentUser.logout();
      } catch (AuthenticationException e) {
        logger.error(e.getMessage(), e);
      }
      return;
    } catch (Exception e1) {
      logger.error(e1.getMessage(), e1);
      ResponseUtils.responseFailure(response);
      return;
    }
  }

  /**
   * Just For Test
   * 
   * @param model
   * @param ra
   * @return
   */
  @Deprecated
  @RequestMapping(value = "/redirect")
  public String redirect(Model model, RedirectAttributes ra) {
    ra.addFlashAttribute("msg", "用户已登录！");
    return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/user/list";
  }

}
