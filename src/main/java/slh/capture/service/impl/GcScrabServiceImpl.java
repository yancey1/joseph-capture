package slh.capture.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.CompanyEnum;
import slh.capture.common.ConstantsCMP;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.HttpFactory;
import slh.capture.common.QueryParamEnum;
import slh.capture.common.TableAttributeEnum;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.ScrabData;
import slh.capture.service.IGcScrabService;

@Service("gcService")
public class GcScrabServiceImpl extends ScrabServiceImpl implements IGcScrabService {
  private final static Logger logger = LoggerFactory.getLogger(GcScrabServiceImpl.class);

  @Autowired
  private ICapturePlatDAO     capturePlatDAO;

  @Override
  public List<ScrabData> execute(String month) {
    List<ScrabData> list = new ArrayList<ScrabData>();

    // 查询古川的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.GC.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);

    String[] loginKeys = genLoginKeys();
    String year="";
    if(StringUtils.isNotBlank(month)){
      String ary[]=month.split("-");
      year=ary[0];
      month=ary[1];
    }
    for (CapturePlat capturePlat : capturePlats) {

      CloseableHttpClient httpClient = HttpFactory.createHttpClient();
      String[] loginValues = new String[2];
      loginValues[0] = capturePlat.getUserName();
      loginValues[1] = capturePlat.getPassword();

      boolean loginRet = getLoginHttpClient(httpClient, capturePlat.getLoginUrl(), loginKeys, loginValues);
      if (!loginRet) {
        logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
        return null;
      }

      Map<String, Object> customParamMap = new HashMap<String, Object>();
      customParamMap.put("url", capturePlat.getQueryUrl());
      customParamMap.put("month", month);

      customParamMap.put("table_attr", "id");
      customParamMap.put("table_attr_value", "data_detail");

      List<ScrabData> ls = exstractGcData(httpClient, genParamMap(), customParamMap,year);

      for (ScrabData scrabData : ls) {
        scrabData.setChannelId(capturePlat.getChannelCode());
        scrabData.setBizType(ConstantsCMP.TYPE_CPS);
      }

      if (ls != null) {
        list.addAll(ls);
      }

    }

    return list;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[2];
    loginKeys[0] = "j_username";
    loginKeys[1] = "j_password";
    return loginKeys;
  }

  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 中手游数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.MONTH, "month");
    return paramMap;
  }

  /**
   * 古川数据解析
   * 
   * @param httpClient
   * @param paramMap
   * @param customParamMap
   * @return
   */
  public List<ScrabData> exstractGcData(CloseableHttpClient httpClient, Map<QueryParamEnum, String> paramMap, Map<String, Object> customParamMap,String year) {

    List<ScrabData> dataList = new ArrayList<ScrabData>();
    Set<ScrabData> dataSet = new HashSet<ScrabData>();
    String url = (String) customParamMap.get(paramMap.get(QueryParamEnum.URL));
    String month = (String) customParamMap.get(paramMap.get(QueryParamEnum.MONTH));
    String tableAttr = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR.getAttr());
    String tableAttrValue = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr());

    try {
      month = URLEncoder.encode(month, "utf-8");
      url += "&month=" + month;
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    }

    try {
      HttpGet httpget = new HttpGet(url);

      HttpResponse response = httpClient.execute(httpget);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      logger.info(html);
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> parseList = HtmlParserUtils.getGcTableDataValues(html, tableAttr, tableAttrValue,year);
      dataSet.addAll(parseList);
    } catch (Exception e) {
      e.printStackTrace();
    }

    dataList.addAll(dataSet);
    return dataList;
  }

  public static void main(String[] args) {

  }

}
