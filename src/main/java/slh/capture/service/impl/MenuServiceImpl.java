package slh.capture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.dao.IMenuDAO;
import slh.capture.domain.Menu;
import slh.capture.service.IMenuService;

@Service("menuService")
public class MenuServiceImpl implements IMenuService {

  @Autowired
  private IMenuDAO menuDAO;

  @Override
  public Menu findMenuById(Menu form) throws Exception {
    return menuDAO.queryMenu(form);
  }

  @Override
  public void modifyMenu(Menu form) throws Exception {
    menuDAO.updateMenu(form);
  }

  @Override
  public Menu findMenuByName(Menu form) throws Exception {
    return menuDAO.queryMenu(form);
  }

  @Override
  public List<?> findMenuTreeList(Menu form) throws Exception {
    // Map<String, Object> result = menuDAO.selectMenuPageList(page, form);
    List<?> result = menuDAO.selectMenuTreeList(form);
    for (Object o : result) {
      if (o instanceof Menu) {
        Menu tmp = (Menu) o;
        if ("0".equals(tmp.getState())) {
          tmp.setState("open");
        } else {
          tmp.setState("closed");
        }
      }
    }
    return result;
  }

  @Override
  public void saveMenu(Menu form) throws Exception {
    // 展示类型(1:模块 2:菜单 3:功能)
    if (form != null && "1".equals(form.getDisplayType())) {
      form.setParent("0");
    }
    menuDAO.insertMenu(form);
  }

  @Override
  public List<Menu> findMenus(Menu form) throws Exception {
    return menuDAO.selectMenus(form);
  }

}
