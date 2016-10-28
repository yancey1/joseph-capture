package slh.capture.dao;

import java.util.List;

import slh.capture.domain.Menu;

public interface IMenuDAO {
  /**
   * 查询分页内容
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  public List<?> selectMenuTreeList(Menu form) throws Exception;

  public List<Menu> selectMenus(Menu form) throws Exception;

  Menu queryMenu(Menu form);

  int updateMenu(Menu form);

  int insertMenu(Menu form);
}
