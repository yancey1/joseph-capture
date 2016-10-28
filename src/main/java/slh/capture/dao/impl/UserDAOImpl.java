package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IUserDAO;
import slh.capture.domain.Menu;
import slh.capture.domain.User;
import slh.capture.domain.UserRole;
import edu.hziee.common.dbroute.BaseDAO;

public class UserDAOImpl extends BaseDAO implements IUserDAO {

  @Override
  public int insertUser(User form) throws Exception {
    Object obj = super.insert("slh_user.insert_slh_user", form);
    return obj == null ? 0 : (Integer) obj;
  }

  @Override
  public void deleteUser(Integer userId) throws Exception {
    super.delete("slh_user.delete_slh_user", userId);
  }

  @Override
  public int updateUser(User form) throws Exception {
    return super.update("slh_user.update_slh_user", form);
  }

  @Override
  public User selectUser(User form) throws Exception {
    return (User) super.queryForObject("slh_user.select_slh_user", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<User> selectUserList(User form) throws Exception {
    return super.queryForList("slh_user.select_slh_user_list", form);
  }

  @Override
  public Map<String, Object> selectUserPageList(DataGridModel page, User form) throws Exception {
    form = (form == null ? new User() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_user.select_slh_user_page_list_count", form));
    results.put("rows", super.queryForList("slh_user.select_slh_user_page_list", form));
    return results;
  }

  @Override
  public void batchUserRoles(List<UserRole> forms) throws Exception {
    super.batchInsert("slh_user.insert_slh_user_role", forms);
  }

  @Override
  public int deleteUserRole(UserRole form) throws Exception {
    return super.delete("slh_user.delete_slh_user_role", form);
  }

  @Override
  public UserRole selectUserRole(UserRole form) throws Exception {
    return (UserRole) super.queryForObject("slh_user.select_slh_user_role", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Menu> selectMenus(User form) throws Exception {
    return super.queryForList("slh_user.select_slh_user_menu_list", form);
  }

}