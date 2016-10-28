package slh.capture.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import slh.capture.common.DataGridModel;
import slh.capture.common.ResponseUtils;
import slh.capture.domain.Role;
import slh.capture.domain.RoleMenu;
import slh.capture.domain.UserRole;
import slh.capture.exception.IllegalParamException;
import slh.capture.service.IRoleService;

@Controller
@RequestMapping("/role")
public class RoleController {
  private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

  @Autowired
  private IRoleService        roleService;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Model model) {
    return "role/list";
  }

  @RequestMapping(value = "/list", params = "json")
  @ResponseBody
  public Map<String, Object> queryList(DataGridModel page, Role form) throws Exception {
    return roleService.findRolePageList(page, form);
  }

  /**
   * 跳转到新增角色界面
   * 
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add() {
    return "role/add";
  }

  /**
   * 保存角色并跳转至列表页
   * 
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  public void add(Role form, HttpServletResponse response, Model model) {
    try {
      // 验证角色是否已经存在
      if (verifyFormInfo(form)) {
        roleService.saveRole(form);
        ResponseUtils.responseSuccess(response);
      } else {
        ResponseUtils.responseInfoExists(response);
      }
    } catch (Exception e1) {
      ResponseUtils.responseFailure(response);
    }
  }

  /**
   * 跳转到编辑角色界面
   * 
   * @return
   */
  @RequestMapping(value = "/{roleId}/update", method = RequestMethod.GET)
  public String update(@PathVariable String roleId, Model model) {
    Role form = new Role();
    form.setRoleId(StringUtils.isNotBlank(roleId) ? Integer.parseInt(roleId) : null);
    try {
      model.addAttribute("role", roleService.findRoleById(form));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "role/update";
  }

  /**
   * 编辑角色
   * 
   * @return
   */
  @RequestMapping(value = "/{roleId}/update", method = RequestMethod.POST)
  public void update(@PathVariable Integer roleId, Role form, HttpServletResponse response, Model model) {

    try {

      // 首先判断角色名是否已经发生改变
      Role role = new Role();
      role.setRoleId(roleId);
      Role role2 = roleService.findRoleById(role);
      if (!form.getRoleName().equals(role2.getRoleName())) {
        // 判断角色名是否已经存在
        Role rr = new Role();
        rr.setRoleName(form.getRoleName());
        List<Role> list = roleService.findRoleList(form);
        if (list != null && list.size() > 0) {
          ResponseUtils.responseInfoExists(response);
        } else {
          roleService.modifyRole(form);
          ResponseUtils.responseSuccess(response);
        }

      } else {
        roleService.modifyRole(form);
        ResponseUtils.responseSuccess(response);
      }

    } catch (Exception e) {
      e.printStackTrace();
      ResponseUtils.responseFailure(response);
    }

  }

  /**
   * 删除角色
   * 
   * @return
   */
  @RequestMapping(value = "/{roleId}/delete", method = RequestMethod.POST)
  public void delete(@PathVariable String roleId, HttpServletResponse response, Model model) {
    // TODO 删除角色业务逻辑

    Role role = new Role();
    role.setRoleId(Integer.valueOf(roleId));

    try {
      List<UserRole> list = roleService.findUserRoles(role);

      if (list.size() > 0) {
        ResponseUtils.responseBeenApplied(response);
      } else {
        roleService.removeRole(role);
        ResponseUtils.responseSuccess(response);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      ResponseUtils.responseFailure(response);
    }
  }
  /**
   * 跳转到权限管理界面
   * 
   * @return
   */

  @RequestMapping(value = "/{roleId}/authority", method = RequestMethod.GET)
  public String authority(@PathVariable String roleId, Model model) {
    Role form = new Role();
    form.setRoleId(StringUtils.isNotBlank(roleId) ? Integer.parseInt(roleId) : null);
    try {
      model.addAttribute("role", roleService.findRoleById(form));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return "role/authority";
  }
  @RequestMapping(value = "/{roleId}/authority", method = RequestMethod.POST)
  public void saveAuthority(@PathVariable Integer roleId, HttpServletRequest request, HttpServletResponse response, Model model) {
    String resObj = request.getParameter("resObj");
    String[] res = resObj.substring(1).split(";");
    List<RoleMenu> list = new ArrayList<RoleMenu>();
    for (int i = 0; i < res.length; i++) {
      String string = res[i];
      RoleMenu menu = new RoleMenu();
      menu.setRoleId(roleId);
      menu.setResourceId(Integer.parseInt(string));
      list.add(menu);
    }
    try {
      RoleMenu roleMenu = new RoleMenu();
      roleMenu.setRoleId(roleId);
      roleService.removeRoleMenu(roleMenu);
      roleService.saveRoleMenus(list);
      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      ResponseUtils.responseFailure(response);
    }
  }

  /**
   * 检验form信息，检测form 信息中NotNull属性是否有空值 检查 角色名是否已存在，
   * 
   * @param form
   * @return
   * @throws Exception
   */
  private boolean verifyFormInfo(Role form) throws Exception {
    if (form != null && form.getRoleName() != null && form.getRoles() != null) {
      // 角色名是否存在
      Role role = roleService.findRoleByName(form);
      if (role != null) {
        return false;// 存在
      }
      return true;
    }
    // 信息不完全
    throw new IllegalParamException("role form information not complete.");
  }

  /**
   * 根据role_name 动态获取角色
   * 
   * @param request
   * @param response
   * @param model
   */
  @RequestMapping(value = "/content", method = RequestMethod.GET)
  public void findRoleFormList(HttpServletRequest request, HttpServletResponse response, Model model) {
    String roleName = request.getParameter("q");
    String defaultValues = request.getParameter("defaultValues");

    StringBuffer jsonData = new StringBuffer("[");
    try {
      Role form = new Role();
      form.setRoleName(roleName);
      List<Role> list = roleService.findRoleList(form);
      if (list != null && list.size() > 0) {
        for (Role role : list) {
          jsonData.append("{");
          jsonData.append("\"roleId\":\"" + role.getRoleId() + "\",");
          if (defaultValues != null) {
            String[] roleNames = defaultValues.split(";");
            if (roleNames.length > 0) {
              for (int i = 0; i < roleNames.length; i++) {
                String string = roleNames[i];
                if (role.getRoleName().equals(string)) {
                  jsonData.append("\"selected\":\"" + true + "\",");
                }

              }
            }
          }
          jsonData.append("\"roleName\":\"" + role.getRoleName() + "\"},");
        }
        jsonData.delete(jsonData.toString().length() - 1, jsonData.toString().length());
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    jsonData.append("]");
    ResponseUtils.renderJson(response, jsonData.toString());
  }
}
