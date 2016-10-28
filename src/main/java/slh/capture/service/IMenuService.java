package slh.capture.service;

import java.util.List;

import slh.capture.domain.Menu;

public interface IMenuService {

  public Menu findMenuById(Menu form) throws Exception;

  public void modifyMenu(Menu form) throws Exception;

  public Menu findMenuByName(Menu form) throws Exception;

  public List<?> findMenuTreeList(Menu form) throws Exception;

  public List<Menu> findMenus(Menu form) throws Exception;

  public void saveMenu(Menu form) throws Exception;

}
