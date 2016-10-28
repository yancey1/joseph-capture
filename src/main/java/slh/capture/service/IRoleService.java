package slh.capture.service;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.Role;
import slh.capture.domain.RoleMenu;
import slh.capture.domain.UserRole;

public interface IRoleService {

  public void saveRole(Role form) throws Exception;

  public int modifyRole(Role form) throws Exception;

  public Role findRoleById(Role form) throws Exception;

  public Role findRoleByName(Role form) throws Exception;

  public List<Role> findRoleList(Role form) throws Exception;

  public Map<String, Object> findRolePageList(DataGridModel page, Role form) throws Exception;

  public int removeRole(Role form) throws Exception;

  public void saveRoleMenus(List<RoleMenu> forms) throws Exception;

  public int removeRoleMenu(RoleMenu form) throws Exception;

  public RoleMenu findRoleMenu(RoleMenu form) throws Exception;

  public List<UserRole> findUserRoles(Role role) throws Exception;

}
