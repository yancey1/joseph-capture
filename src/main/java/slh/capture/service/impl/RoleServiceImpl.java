package slh.capture.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IRoleDAO;
import slh.capture.domain.Role;
import slh.capture.domain.RoleMenu;
import slh.capture.domain.UserRole;
import slh.capture.service.IRoleService;

@Service("roleService")
public class RoleServiceImpl implements IRoleService {

  @Autowired
  private IRoleDAO roleDAO;

  @Override
  public void saveRole(Role form) throws Exception {
    roleDAO.insertRole(form);
  }

  @Override
  public int modifyRole(Role form) throws Exception {
    return roleDAO.updateRole(form);
  }

  @Override
  public Role findRoleById(Role form) throws Exception {
    return roleDAO.queryRole(form);
  }

  @Override
  public List<Role> findRoleList(Role form) throws Exception {
    return roleDAO.queryRoleList(form);
  }

  @Override
  public Map<String, Object> findRolePageList(DataGridModel page, Role form) throws Exception {
    return roleDAO.selectRolePageList(page, form);
  }

  @Override
  public Role findRoleByName(Role form) throws Exception {
    return roleDAO.queryRole(form);
  }

  @Override
  public void saveRoleMenus(List<RoleMenu> forms) throws Exception {
    roleDAO.batchRoleMenus(forms);
  }

  @Override
  public int removeRoleMenu(RoleMenu form) throws Exception {
    return roleDAO.deleteRoleMenu(form);
  }

  @Override
  public RoleMenu findRoleMenu(RoleMenu form) throws Exception {
    return roleDAO.selectRoleMenu(form);
  }

  @Override
  public List<UserRole> findUserRoles(Role role) throws Exception {

    return roleDAO.queryUserRoles(role);
  }

  @Override
  public int removeRole(Role form) throws Exception {
    return roleDAO.deleteRole(form);
  }

}
