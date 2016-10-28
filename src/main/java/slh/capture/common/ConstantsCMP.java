package slh.capture.common;

import javax.servlet.http.HttpServletRequest;

import slh.capture.domain.User;

public class ConstantsCMP {

  public final static String MESSAGE                = "message";
  public final static String USER_SESSION_INFO      = "userSessionInfo";

  /**
   * 供应商类型：1：CPA;2：CPS
   */

  public final static int    TYPE_CPA               = 1;
  public final static int    TYPE_CPS               = 2;

  /**
   * 供应商后台导入数据收入类型：0(信息费)，1(实收)
   */
  public final static int    DATA_TYPE_FORMATTONS   = 0;
  public final static int    DATA_TYPE_REAL_RECIVE  = 1;

  /**
   * 第三方抓取数据状态：0:未入库;1：已入库
   */
  public final static int    CAPTURE_DATA_STATE_NO  = 0;
  public final static int    CAPTURE_DATA_STATE_YES = 1;

  /**
   * 发票类型
   * 
   * @param request
   * @return
   */
  public static final int    INVOICE_TYPE_PURPOSE   = 0;                // 增值税专用发票
  public static final int    INVOICE_TYPE_GENERAL   = 1;                // 增值税普通发票

  public final static String ADMIN_NAME             = "admin";

  public static User getSessionUser(HttpServletRequest request) {
    return (User) (request.getSession().getAttribute(ConstantsCMP.USER_SESSION_INFO));
  }

  public enum RoleEnum {
    ADMIN("admin", "超级系统管理员"), OPERATION("operation", "市场运营人员");

    private String roleCode;
    private String roleName;

    RoleEnum(String _roleCode, String _roleName) {
      this.roleCode = _roleCode;
      this.roleName = _roleName;
    }

    public String getRoleCode() {
      return roleCode;
    }

    public void setRoleCode(String roleCode) {
      this.roleCode = roleCode;
    }

    public String getRoleName() {
      return roleName;
    }

    public void setRoleName(String roleName) {
      this.roleName = roleName;
    }

  }
}
