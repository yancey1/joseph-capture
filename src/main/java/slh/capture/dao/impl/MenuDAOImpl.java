package slh.capture.dao.impl;

import java.util.List;

import slh.capture.dao.IMenuDAO;
import slh.capture.domain.Menu;
import edu.hziee.common.dbroute.BaseDAO;

public class MenuDAOImpl extends BaseDAO implements IMenuDAO {

  @Override
  public List<?> selectMenuTreeList(Menu form) throws Exception {
    return super.queryForList("slh_menu.select_slh_model_page_list", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Menu> selectMenus(Menu form) throws Exception {
    return (List<Menu>) super.queryForList("slh_menu.select_slh_model_list", form);
  }

  @Override
  public Menu queryMenu(Menu form) {
    return (Menu) super.queryForObject("slh_menu.select_slh_model", form);
  }

  @Override
  public int updateMenu(Menu form) {
    return super.update("slh_menu.update_slh_model", form);
  }

  @Override
  public int insertMenu(Menu form) {
    int id = (Integer) super.insert("slh_menu.insert_slh_model", form);
    return id;
  }

}
