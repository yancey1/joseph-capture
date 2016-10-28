package slh.capture.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.BizTypeEnum;
import slh.capture.common.CompanyEnum;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.ScrabData;
import slh.capture.service.IJiuZhouLianDongScrabService;
import edu.hziee.common.lang.DateUtil;

@Service("jiuZhouLianDongService")
public class JiuZhouLianDongScrabServiceImpl extends ScrabServiceImpl implements IJiuZhouLianDongScrabService {

  private Logger          logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ICapturePlatDAO capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode) {

    List<ScrabData> dataList = new ArrayList<ScrabData>();

    // 查询北京九州联动科技有限公司的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.JZLD.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);
    String[] loginKeys = genLoginKeys();
    for (CapturePlat capturePlat : capturePlats) {

      String[] loginValues = new String[3];
      loginValues[0] = capturePlat.getUserName();
      loginValues[1] = capturePlat.getPassword();
      loginValues[2] = imgCode;
      boolean loginRet = getLoginHttpClient(client, capturePlat.getLoginUrl(), loginKeys, loginValues);
      if (!loginRet) {
        logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
        return null;
      }

      String queryPageHtml = getDataQueryHtml(client, capturePlat.getQueryPageUrl());
      List<String> channelList = HtmlParserUtils.getSelectTagValues(queryPageHtml, "id", "channel");
      if (channelList != null && !channelList.isEmpty()) {
        for (String channel : channelList) {
          if (channel != null && !channel.equals("")) {
            Map<String, Object> customParamMap = new HashMap<String, Object>();
            customParamMap.put("url", capturePlat.getQueryUrl());

            customParamMap.put("pageTag", "1");
            customParamMap.put("dimension", "day;game;false;channel;false;false");
            customParamMap.put("date1", DateUtil.parseDate(startDate, DateUtil.TRADITION_PATTERN));
            customParamMap.put("date2", DateUtil.parseDate(endDate, DateUtil.TRADITION_PATTERN));
            customParamMap.put("channel", channel);
            customParamMap.put("province", "All");
            customParamMap.put("game", "All");

            // 17App联盟后台查询结果table属性
            customParamMap.put("table_attr", "id");
            customParamMap.put("table_attr_value", "sort_table");

            // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
            Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
            dataIndex.put(1, DataNameEnum.BIZ_DATE);
            dataIndex.put(3, DataNameEnum.PRODUCT);
            dataIndex.put(4, DataNameEnum.BIZ_AMOUNT);
            customParamMap.put("column_index", dataIndex);

            List<ScrabData> scrabDataList = exstractData(client, genParamMap(), customParamMap);
            if (scrabDataList != null) {
              for (ScrabData data : scrabDataList) {
                data.setBizType(BizTypeEnum.CPS.getCode());
                data.setChannelId(channel);
                dataList.add(data);
              }
            }
          }

        }
      }
    }
    return dataList;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 17App联盟数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "date1");
    paramMap.put(QueryParamEnum.END_TIME, "date2");
    return paramMap;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[3];
    loginKeys[0] = "id";
    loginKeys[1] = "password";
    loginKeys[2] = "verifyCode";
    return loginKeys;
  }
}
