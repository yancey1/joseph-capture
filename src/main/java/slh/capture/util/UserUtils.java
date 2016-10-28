package slh.capture.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import slh.capture.common.ConstantsCMP;
import slh.capture.common.UserTypeEnum;
import slh.capture.domain.Role;
import slh.capture.domain.User;

/**
 * 用户相关操作工具类
 * 
 * */
public class UserUtils {

  /**
   * 获取用户信息
   * 
   * */
  public static User getSessionUser(HttpServletRequest request) {
    return (User) (request.getSession().getAttribute(ConstantsCMP.USER_SESSION_INFO));
  }

  /**
   * 判断是否管理员
   * 
   * */
  public static int isAdmin(HttpServletRequest request) {
    List<Role> roleList = getSessionUser(request).getRoleList();
    int isAdmin = UserTypeEnum.IS_NOT_ADMIN.getType();
    if (roleList != null) {
      isAdmin = UserTypeEnum.IS_NOT_ADMIN.getType();
      for (Role role : roleList) {
        if (ConstantsCMP.ADMIN_NAME.equals(role.getRoles())) {
          isAdmin = UserTypeEnum.IS_ADMIN.getType();
        }
      }
    }

    return isAdmin;
  }

}
