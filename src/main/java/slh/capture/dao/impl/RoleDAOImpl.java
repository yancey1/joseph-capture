package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IRoleDAO;
import slh.capture.domain.Role;
import slh.capture.domain.RoleMenu;
import slh.capture.domain.UserRole;
import edu.hziee.common.dbroute.BaseDAO;

public class RoleDAOImpl extends BaseDAO implements IRoleDAO {

  @Override
  public Map<String, Object> selectRolePageList(DataGridModel page, Role form) throws Exception {
    form = (form == null ? new Role() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_role.query_slh_count", form));
    results.put("rows", super.queryForList("slh_role.select_slh_model_page_list", form));
    return results;
  }

  @Override
  public void batchRoleMenus(List<RoleMenu> forms) throws Exception {
    super.batchInsert("slh_role.insert_slh_role_menu", forms);

  }

  @Override
  public RoleMenu selectRoleMenu(RoleMenu form) throws Exception {
    return (RoleMenu) super.queryForObject("slh_role.select_slh_role_menu", form);
  }

  @Override
  public int deleteRoleMenu(RoleMenu form) throws Exception {
    return super.delete("slh_role.delete_slh_role_menu", form);
  }

  @Override
  public int insertRole(Role form) {
    int id = (Integer) super.insert("slh_role.insert_slh_model", form);
    return id;
  }

  @Override
  public int updateRole(Role form) {
    return super.update("slh_role.update_slh_model", form);
  }

  @Override
  public Role queryRole(Role form) {
    return (Role) super.queryForObject("slh_role.select_slh_model", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Role> queryRoleList(Role form) {
    return super.queryForList("slh_role.select_slh_model_list", form);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<UserRole> queryUserRoles(Role form) {

    return super.queryForList("slh_role.select_slh_user_role_list", form);
  }
  @Override
  public int deleteRole(Role form) throws Exception {
    return super.delete("slh_role.delete_slh_model", form);
  }

}
