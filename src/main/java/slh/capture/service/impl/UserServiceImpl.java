package slh.capture.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IUserDAO;
import slh.capture.domain.Menu;
import slh.capture.domain.User;
import slh.capture.domain.UserRole;
import slh.capture.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

  @Autowired
  private IUserDAO userDAO;

  @Override
  public int saveUser(User form) throws Exception {
    return userDAO.insertUser(form);
  }

  @Override
  public int removeUser(User form) throws Exception {
    return 0;
  }

  @Override
  public int modifyUser(User form) throws Exception {
    return userDAO.updateUser(form);
  }

  @Override
  public User findUser(User form) throws Exception {
    return userDAO.selectUser(form);
  }

  @Override
  public List<User> findUserList(User form) throws Exception {
    return userDAO.selectUserList(form);
  }

  @Override
  public Map<String, Object> findUserPageList(DataGridModel page, User form) throws Exception {
    return userDAO.selectUserPageList(page, form);
  }

  @Override
  public void deleteUser(Integer userId) throws Exception {
    userDAO.deleteUser(userId);
  }

  @Override
  public int removeUserRole(UserRole form) throws Exception {
    return userDAO.deleteUserRole(form);
  }

  @Override
  public UserRole findUserRole(UserRole form) throws Exception {
    return userDAO.selectUserRole(form);
  }

  @Override
  public void saveUserRoles(List<UserRole> forms) throws Exception {
    userDAO.batchUserRoles(forms);
  }

  @Override
  public List<Menu> findMenuList(User form) throws Exception {

    return userDAO.selectMenus(form);
  }

}
