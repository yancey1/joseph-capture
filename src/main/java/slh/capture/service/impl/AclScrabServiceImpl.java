package slh.capture.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.CompanyEnum;
import slh.capture.common.ConstantsCMP;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.common.TableAttributeEnum;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.ScrabData;
import slh.capture.service.IAclScrabService;

/**
 * 泰酷
 * 
 * @author ck
 * 
 */
@Service("aclService")
public class AclScrabServiceImpl extends ScrabServiceImpl implements IAclScrabService {
  private final static Logger logger = LoggerFactory.getLogger(AclScrabServiceImpl.class);

  @Autowired
  private ICapturePlatDAO     capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String month, String randCode) {
    List<ScrabData> list = new ArrayList<ScrabData>();

    // 查询泰酷的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.ACL.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);

    String[] loginKeys = genLoginKeys();
    for (CapturePlat capturePlat : capturePlats) {

      String[] loginValues = new String[3];
      loginValues[0] = capturePlat.getUserName();
      loginValues[1] = capturePlat.getPassword();
      loginValues[2] = randCode;

      boolean loginRet = getLoginHttpClient(client, capturePlat.getLoginUrl(), loginKeys, loginValues);
      if (!loginRet) {
        logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
        return null;
      }

      Map<String, Object> customParamMap = new HashMap<String, Object>();
      customParamMap.put("url", capturePlat.getQueryUrl());
      customParamMap.put("statDateByMonth", month);
      customParamMap.put("dateFormate", 2);
      customParamMap.put("page.pageNo", 1);

      customParamMap.put("table_attr", "cellspacing");
      customParamMap.put("table_attr_value", "1");

      // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
      Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
      dataIndex.put(1, DataNameEnum.BIZ_DATE);
      dataIndex.put(2, DataNameEnum.CHANNEL_ID);
      dataIndex.put(4, DataNameEnum.PRODUCT);
      dataIndex.put(5, DataNameEnum.BIZ_AMOUNT);
      customParamMap.put("column_index", dataIndex);

      List<ScrabData> ls = exstractGcData(client, genParamMap(), customParamMap);

      for (ScrabData scrabData : ls) {
        scrabData.setBizType(ConstantsCMP.TYPE_CPS);
        String date = scrabData.getBizDate();

        scrabData.setBizDate(date.substring(0, 10));

      }

      if (ls != null) {
        list.addAll(ls);
      }

    }

    return list;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[3];
    loginKeys[0] = "loginname";
    loginKeys[1] = "password";
    loginKeys[2] = "validateCode";
    return loginKeys;
  }

  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 中手游数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.MONTH, "statDateByMonth");
    paramMap.put(QueryParamEnum.DATEFORMATE, "dateFormate");
    paramMap.put(QueryParamEnum.PAGENO, "page.pageNo");
    return paramMap;
  }

  /**
   * 泰酷数据解析
   * 
   * @param httpClient
   * @param paramMap
   * @param customParamMap
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ScrabData> exstractGcData(CloseableHttpClient httpClient, Map<QueryParamEnum, String> paramMap, Map<String, Object> customParamMap) {

    List<ScrabData> dataList = new ArrayList<ScrabData>();
    Set<ScrabData> dataSet = new HashSet<ScrabData>();
    String url = (String) customParamMap.get(paramMap.get(QueryParamEnum.URL));
    Integer dateFormate = (Integer) customParamMap.get(paramMap.get(QueryParamEnum.DATEFORMATE));
    Integer pageNo = (Integer) customParamMap.get(paramMap.get(QueryParamEnum.PAGENO));
    String statDateBymonth = (String) customParamMap.get(paramMap.get(QueryParamEnum.MONTH));

    String tableAttr = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR.getAttr());
    String tableAttrValue = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr());

    Map<Integer, DataNameEnum> dataIndex = (Map<Integer, DataNameEnum>) customParamMap.get(TableAttributeEnum.COLUMN_INDEX.getAttr());

    List<NameValuePair> formparams = new ArrayList<NameValuePair>();

    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.DATEFORMATE), dateFormate.toString()));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.PAGENO), pageNo.toString()));
    formparams.add(new BasicNameValuePair(paramMap.get(QueryParamEnum.MONTH), statDateBymonth));

    UrlEncodedFormEntity entity;
    try {
      entity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(entity);
      HttpResponse response = httpClient.execute(httppost);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (entity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> parseList = HtmlParserUtils.getTableDataValues(html, tableAttr, tableAttrValue, dataIndex);

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
