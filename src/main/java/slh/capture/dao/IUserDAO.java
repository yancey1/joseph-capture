package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.Menu;
import slh.capture.domain.User;
import slh.capture.domain.UserRole;

public interface IUserDAO {

  /**
   * 保存用户
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int insertUser(User form) throws Exception;

  /**
   * 删除用户信息
   * 
   * @param form
   * @return
   * @throws Exception
   */
  void deleteUser(Integer useId) throws Exception;

  /**
   * 编辑用户
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int updateUser(User form) throws Exception;

  /**
   * 获取指定用户
   * 
   * @param form
   * @return
   * @throws Exception
   */
  User selectUser(User form) throws Exception;

  /**
   * 不带分页的用户列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<User> selectUserList(User form) throws Exception;

  /**
   * 带分页的用户列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  Map<String, Object> selectUserPageList(DataGridModel page, User form) throws Exception;

  /**
   * 保存用户的角色
   * 
   * @param form
   * @return
   * @throws Exception
   */
  void batchUserRoles(List<UserRole> forms) throws Exception;

  /**
   * 查询用户的角色
   * 
   * @param form
   * @return
   * @throws Exception
   */
  UserRole selectUserRole(UserRole form) throws Exception;

  /**
   * 删除用户的角色
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int deleteUserRole(UserRole form) throws Exception;

  /**
   * 查询用户的权限
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<Menu> selectMenus(User form) throws Exception;

}