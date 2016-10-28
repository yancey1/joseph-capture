package slh.capture.service.impl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.HtmlParserUtils;
import slh.capture.common.JsonParserUtils;
import slh.capture.dao.IAccountDao;
import slh.capture.dao.IAppDao;
import slh.capture.dao.ICaptureDataDao;
import slh.capture.domain.AccountDomain;
import slh.capture.domain.AppDomain;
import slh.capture.domain.CaptureDomain;
import slh.capture.domain.oms.UMengQuery;
import slh.capture.domain.oms.UMengQueryResult;
import slh.capture.service.ICaptureDataService;

@Service("captureDataService")
public class CaptureDataServiceImpl implements ICaptureDataService {

  private final static Logger logger   = LoggerFactory.getLogger(CaptureDataServiceImpl.class);
  @Autowired
  private ICaptureDataDao     captureDataDao;

  @Autowired
  private IAccountDao         accountDao;

  @Autowired
  private IAppDao             appDao;

  private static final String loginUrl = "https://www.umeng.com/sso/login";
  private static final String service  = "http://www.umeng.com/users/login_redirect";
  private static final String channelPrefix="p0000@";
  
  @Override
  public Map<String, Object> getCaptureDataList(CaptureDomain domain) {
    return captureDataDao.getCaptureDataList(domain);
  }

  @Override
  public boolean saveCaptureData(HttpServletRequest request, CaptureDomain domain) throws Exception {
    HttpSession ss = request.getSession();
    String captcha = (String) ss.getAttribute("captcha");
    CloseableHttpClient httpClient = createSSLClientDefault();
    AccountDomain acc = new AccountDomain();
    acc.setAccountId(domain.getUserId());
    List<AccountDomain> accList = accountDao.getAccountListByObj(acc);
    if (accList == null || accList.size() == 0) {
      return false;
    }

    AppDomain app = new AppDomain();
    app.setAppId(domain.getAppId());
    List<AppDomain> appList = appDao.getAppListByApp(app);
    if (appList == null || appList.size() == 0) {
      return false;
    }
    AccountDomain accountDomain = accList.get(0);
    AppDomain appDomain = appList.get(0);

    String[] loginKeys = genLoginKeys();
    String[] loginValues = genLoginValues(accountDomain.getName(), accountDomain.getPassword(), domain.getRandomCode(), captcha);
    boolean loginRet = getLoginHttpClient(httpClient, loginUrl, loginKeys, loginValues);
    if (!loginRet) {
      logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
      return false;
    }

    String dataJson = getDataQueryHtml(httpClient, appDomain.getAppUrl());
    UMengQuery ymQuery = (UMengQuery) JsonParserUtils.parseFromJsonForObject(dataJson, UMengQuery.class);
    List<UMengQueryResult> list = ymQuery.getRows();
    if (list != null && list.size() > 0) {
      List<CaptureDomain> capList = new ArrayList<CaptureDomain>();
      SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      cal.add(Calendar.DATE, -1);
      for (UMengQueryResult query : list) {
        CaptureDomain capDomain = new CaptureDomain();
        capDomain.setUserId(accountDomain.getId());
        capDomain.setUserName(accountDomain.getName());
        capDomain.setAppId(appDomain.getId());
        capDomain.setAppName(appDomain.getAppName());
        capDomain.setChannelName(channelPrefix+query.getName());
        capDomain.setNewsUserAmount(Integer.parseInt(query.getInstall()));
        capDomain.setStatisDate(sim.format(cal.getTime()));
        capList.add(capDomain);
      }
      captureDataDao.saveCaptureData(capList);
    }
    return true;
  }

  public String[] genLoginKeys() {
    String[] loginKeys = new String[6];
    loginKeys[0] = "username";
    loginKeys[1] = "password";
    loginKeys[2] = "captcha";
    loginKeys[3] = "lt";
    loginKeys[4] = "captcha_i";
    loginKeys[5] = "service";
    return loginKeys;
  }

  public String[] genLoginValues(String userName, String password, String imgCode, String captcha) {
    String[] loginValues = new String[6];
    loginValues[0] = userName;
    loginValues[1] = password;
    loginValues[2] = imgCode;
    loginValues[3] = HtmlParserUtils.getInputTagValues(loginUrl, "id", "lt");
    loginValues[4] = captcha;
    loginValues[5] = service;
    return loginValues;
  }

  public boolean getLoginHttpClient(CloseableHttpClient client, String loginUrl, String[] keys, String[] values) {
    HttpResponse response = null;
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    int index = 0;
    for (String key : keys) {
      formparams.add(new BasicNameValuePair(key, values[index++]));
    }
    UrlEncodedFormEntity entity;
    try {
      entity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(loginUrl);
      httppost.setEntity(entity);
      response = client.execute(httppost);
      int status = response.getStatusLine().getStatusCode();
      if (status != HttpStatus.SC_OK && status != HttpStatus.SC_SEE_OTHER) {
        logger.error("login failed,width keys[{}],valus[{}]", keys, values);
        return false;
      }
      if (response != null) {
        response.getEntity().getContent().close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public static String getDataQueryHtml(CloseableHttpClient httpClient, String queryUrl) {
    HttpResponse response = null;
    try {
      HttpGet httpGet = new HttpGet(queryUrl);
      logger.debug("get query page html by url=[{}]", queryUrl);
      response = httpClient.execute(httpGet);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      return html;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static CloseableHttpClient createSSLClientDefault() {
    try {
      SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
        // 信任所有
        @Override
        public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
          return false;
        }
      }).build();
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
      return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return HttpClients.createDefault();
  }

  @Override
  public int checkCaptureData(CaptureDomain domain) {
    return captureDataDao.checkCaptureData(domain);
  }
}
