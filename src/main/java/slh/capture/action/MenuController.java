package slh.capture.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import slh.capture.common.ResponseUtils;
import slh.capture.domain.Menu;
import slh.capture.exception.IllegalParamException;
import slh.capture.service.IMenuService;

@Controller
@RequestMapping("/menu")
public class MenuController {

  private final static Logger logger = LoggerFactory.getLogger(MenuController.class);

  @Autowired
  private IMenuService        menuService;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Model model) {
    return "menu/list";
  }

  @RequestMapping(value = "/list", params = "json")
  @ResponseBody
  public List<?> queryList(Menu form, HttpServletRequest request, Model model) throws Exception {
    String resourceId = request.getParameter("id");
    model.addAttribute("menu", new Menu());
    if (resourceId == null) {
      form.setParent("0");
    } else {
      form.setParent(resourceId);
    }
    return menuService.findMenuTreeList(form);
  }

  /**
   * 所属模块
   * 
   * @throws Exception
   */
  @RequestMapping(value = "/parent", params = "json")
  @ResponseBody
  public List<?> queryParentList(Menu form, HttpServletRequest request) throws Exception {
    form.setParent("0");
    return menuService.findMenuTreeList(form);
  }

  /**
   * 跳转到新增菜单界面
   * 
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add(Model model) {
    return "menu/add";
  }
  /**
   * 保存菜单并跳转至列表页
   * 
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  public void add(Menu form, HttpServletResponse response, Model model) {
    try {
      if (verifyFormInfo(form)) {
        menuService.saveMenu(form);
        ResponseUtils.responseSuccess(response);
      } else {
        ResponseUtils.responseInfoExists(response);
      }
    } catch (Exception e1) {
      e1.printStackTrace();
      ResponseUtils.responseFailure(response);
    }
  }

  /**
   * 编辑菜单
   * 
   * @return
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public void update(Menu form, HttpServletResponse response, Model model) {

    System.out.println(form.toString());
    try {
      menuService.modifyMenu(form);
      ResponseUtils.responseSuccess(response);

    } catch (Exception e) {
      e.printStackTrace();
      ResponseUtils.responseFailure(response);
    }

  }

  /**
   * 删除菜单
   * 
   * @return
   */
  @RequestMapping(value = "/{resourceId}/delete", method = RequestMethod.POST)
  public void delete(@PathVariable Integer resourceId, HttpServletResponse response, Model model) {

    ResponseUtils.responseSuccess(response);
  }

  @RequestMapping(value = "/form", params = "json")
  @ResponseBody
  public List<Menu> queryMenuforms() {
    Menu form = new Menu();
    List<Menu> result = null;
    try {
      result = menuService.findMenus(form);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return result;

  }

  /**
   * 检验form信息，检测form 信息中NotNull属性是否有空值 检查 角色名是否已存在，
   * 
   * @param form
   * @return
   * @throws Exception
   */
  private boolean verifyFormInfo(Menu form) throws Exception {
    if (form != null && form.getResourceName() != null) {
      Menu menu = menuService.findMenuByName(form);
      if (menu != null && (form.getResourceId() != menu.getResourceId())) {
        return false;// 存在
      }
      return true;
    }
    // 信息不完全
    throw new IllegalParamException("Menu form information not complete.");
  }

}
