package slh.capture.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
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
import slh.capture.service.IXhcScrabService;

/**
 * 星华晨数据拉取
 * 
 * @author ck
 * 
 */
@Service("xhcScrabService")
public class XhcScrabServiceImpl extends ScrabServiceImpl implements IXhcScrabService {

  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  @SuppressWarnings("unused")
  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    List<String> productList = new ArrayList<String>();// 产品ID

    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.XHC.getCompanyName());

    List<CapturePlat> list = capturePlatDAO.queryCapturePlatList(form);

    List<ScrabData> dataList = new ArrayList<ScrabData>();
    for (CapturePlat capturePlat : list) {

      List<ScrabData> ls = new ArrayList<ScrabData>();

      ls = exstractData(client, genParamMap(), genCustomParamMap(null, null, capturePlat.getQueryUrl(), null));
      // 写入渠道号
      if (ls != null) {
        for (ScrabData scrabData : ls) {

          String bizAmount = scrabData.getBizAmount().substring(1);
          if (bizAmount.equals("") || bizAmount == null) {
            scrabData.setBizAmount("0");
          } else {
            scrabData.setBizAmount(bizAmount);
          }

          scrabData.setProductName(capturePlat.getProductName());
          scrabData.setBizType(ConstantsCMP.TYPE_CPS);
          scrabData.setChannelId(capturePlat.getChannelCode());
        }

      }
      dataList.addAll(ls);
    }

    return dataList;

  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 星华晨数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    return paramMap;
  }

  @Override
  public Map<String, Object> genCustomParamMap(String startTime, String endTime, String url, String channelId) {
    Map<String, Object> customParamMap = new HashMap<String, Object>();
    customParamMap.put("url", url);

    customParamMap.put("table_attr", "id");
    customParamMap.put("table_attr_value", "listmore");

    // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    dataIndex.put(6, DataNameEnum.BIZ_DATE);
    dataIndex.put(1, DataNameEnum.BIZ_AMOUNT);
    customParamMap.put("column_index", dataIndex);
    return customParamMap;
  }

  @SuppressWarnings("unchecked")
  public List<ScrabData> exstractData(CloseableHttpClient httpClient, Map<QueryParamEnum, String> paramMap, Map<String, Object> customParamMap) {

    List<ScrabData> dataList = new ArrayList<ScrabData>();
    Set<ScrabData> dataSet = new HashSet<ScrabData>();
    String url = (String) customParamMap.get(paramMap.get(QueryParamEnum.URL));

    String tableAttr = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR.getAttr());
    String tableAttrValue = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr());
    Map<Integer, DataNameEnum> dataIndex = (Map<Integer, DataNameEnum>) customParamMap.get(TableAttributeEnum.COLUMN_INDEX.getAttr());

    List<ScrabData> parseList = new ArrayList<ScrabData>();

    HttpResponse response = null;
    try {
      HttpGet httpGet = new HttpGet(url);
      response = httpClient.execute(httpGet);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());

      parseList = HtmlParserUtils.getXhcTableDataValues(html, tableAttr, tableAttrValue, dataIndex);
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }

      dataSet.addAll(parseList);

    } catch (Exception e) {
      e.printStackTrace();
    }

    dataList.addAll(dataSet);
    return dataList;
  }

}
