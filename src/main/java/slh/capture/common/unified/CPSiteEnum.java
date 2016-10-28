package slh.capture.common.unified;

/**
 * CP站点枚举
 * 
 * @author xuwenqiang
 * */
public enum CPSiteEnum {

  CHINA_MOBILE_10086("http://dev.10086.cn/", "10086"), 
  NEW_GALAXY("http://ngsteam.xinyinhe.com/smspay/management", "新银河"),
  NEW_ZYF("http://dev.mopo.com/login", "指易付"),
  NEW_ZM("http://115.236.43.210:2013/impartner/login.html", "掌盟"),
  NEW_JT("http://211.155.235.62:9092/cpselect/logon.aspx", "聚塔"),
  NEW_WB("http://hz.quanzhifu.net:6501/UserMngAction.do?method=doLogin&userFlag=1", "唯变"),
  NEW_YL("http://cp.gzpkwg.com/user.do?m=login", "怡乐"),
  NEW_CY("http://union.duomi.com/Account/Login", "彩云"),
  NEW_SZYL("http://www.yunlauncher.com:8090/desktop/yun_reader_login_do.htm", "神州云朗"),
  NEW_YM("http://qudao.umipay.com/accounts/login/","优蜜"),
  OCEAN_10086("http://dev.10086.cn/datau/modules/views/dataana.jsp","欧昕"),
  NEW_ZZ("http://c.chinamobiad.com:6060/login","Ocean"),
  NEW_DY("http://cx.donggame.com.cn:8080/dataQuery/method/member/common/login.action","动游"),
  NEW_SY("http://ppcool.com.cn:7002/mmdeploy/login.service","搜影"),
  NEW_HX("http://iss.funugame.com/login_index","火星"),
  NEW_QY("http://game.joy-river.com:8081/JRGC-cms/login!login.action","趣源"),
  NEW_HW("http://www.you2game.com/you2bi/main.do","幻网"),
  NEW_JY("http://112.124.60.92/login.aspx","匠游"),
  NEW_YY("http://ad.yoyuan.net/third/login.jsp","有缘"),
  NEW_WSY("http://admin.zytcgame.com/loginController.do?checkuser","威搜游"),
  NEW_WSY2("http://121.11.79.19:8085/login/index","威搜游"),
  NEW_DW("http://www.gameapts.com/site/com_access/login.php","点我"),
  NEW_TXFH("http://txhf.91app.co/Pub/api/index.php/manager/login","天旭汇丰"),
  NEW_FK("http://223.4.150.154:8080/gamePay/admindo.php","风酷"),
  NEW_KY("http://info.coeeland.com/bss/login.aspx","酷宇"),
  NEW_QP("http://ddz.qipagame.cn/ddz/logon.htm","奇葩"),
  NEW_KX("http://showdata.kascend.com/show/comm/login.php","开迅"),
  NEW_KX2("http://opendata.kascend.com/opendata/auth/login","开迅2"),
  NEW_LC("http://game.kuho.me:8000/channel/login/","乐畅"),
  NEW_XL("http://www.dshinepf.com:8060/cus/login","炫亮"),
  NEW_LM("http://114.215.129.247:8088/lemipay/login","乐米"),
  NEW_TY("http://115.28.55.233/acl/login!login.action","乐米"),
  NEW_KLW("http://112.124.10.89:8080/login/index","快乐玩"),
  NEW_KZW("http://billing.kongzhong.com/getEmpl","空中网"),
  NEW_CSL("http://www.zsyj.com.cn/login.do","创斯林");
  private String site;
  private String desc;

  private CPSiteEnum(String site, String desc) {
    this.site = site;
    this.desc = desc;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

}
