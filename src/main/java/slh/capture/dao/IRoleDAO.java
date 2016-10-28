package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.Role;
import slh.capture.domain.RoleMenu;
import slh.capture.domain.UserRole;

public interface IRoleDAO {
  /**
   * 查询分页内容
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  public Map<String, Object> selectRolePageList(DataGridModel page, Role form) throws Exception;

  /**
   * 保存角色的权限
   * 
   * @param form
   * @return
   * @throws Exception
   */
  void batchRoleMenus(List<RoleMenu> forms) throws Exception;

  /**
   * 查询角色的权限
   * 
   * @param form
   * @return
   * @throws Exception
   */
  RoleMenu selectRoleMenu(RoleMenu form) throws Exception;

  /**
   * 删除角色
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int deleteRole(Role form) throws Exception;

  /**
   * 删除角色的权限
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int deleteRoleMenu(RoleMenu form) throws Exception;

  int insertRole(Role form);

  int updateRole(Role form);

  Role queryRole(Role form);

  List<Role> queryRoleList(Role form);

  List<UserRole> queryUserRoles(Role role);

}
