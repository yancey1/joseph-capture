package slh.capture.service;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.Menu;
import slh.capture.domain.User;
import slh.capture.domain.UserRole;

/**
 * 
 * @author oc_admin
 * 
 */
public interface IUserService {

  int saveUser(User form) throws Exception;

  int removeUser(User form) throws Exception;

  int modifyUser(User form) throws Exception;
  void deleteUser(Integer userId) throws Exception;

  User findUser(User form) throws Exception;

  List<User> findUserList(User form) throws Exception;

  Map<String, Object> findUserPageList(DataGridModel page, User form) throws Exception;

  /**
   * 保存用户角色
   * 
   * @param forms
   * @throws Exception
   */
  void saveUserRoles(List<UserRole> forms) throws Exception;

  /**
   * 删除用户角色
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int removeUserRole(UserRole form) throws Exception;

  /**
   * 查询用户角色
   * 
   * @param form
   * @return
   * @throws Exception
   */
  UserRole findUserRole(UserRole form) throws Exception;

  /**
   * 查询用户的权限
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<Menu> findMenuList(User form) throws Exception;

}
