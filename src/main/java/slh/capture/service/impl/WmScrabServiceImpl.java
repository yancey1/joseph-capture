package slh.capture.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.CompanyEnum;
import slh.capture.common.ConstantsCMP;
import slh.capture.common.JsonParserUtils;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.domain.JsonWmQuery;
import slh.capture.domain.ScrabData;
import slh.capture.service.IWmScrabService;

@Service("wmService")
public class WmScrabServiceImpl extends ScrabServiceImpl implements IWmScrabService {
  private Logger                  logger     = LoggerFactory.getLogger(getClass());
  private static final DateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");

  @Autowired
  private ICapturePlatDAO         capturePlatDAO;

  @Override
  public List<ScrabData> execute(CloseableHttpClient client, String startTime, String endTime, String randCode) {
    List<ScrabData> list = new ArrayList<ScrabData>();
    // 查询玩魔的基本信息
    CapturePlat form = new CapturePlat();
    form.setCompany(CompanyEnum.WM.getCompanyName());
    List<CapturePlat> capturePlats = capturePlatDAO.queryCapturePlatList(form);
    String[] loginKeys = genLoginKeys();

    for (CapturePlat capturePlat : capturePlats) {
      String[] loginValues = new String[4];
      loginValues[0] = capturePlat.getUserName();
      loginValues[1] = capturePlat.getPassword();
      loginValues[2] = randCode;
      boolean loginRet = getLoginHttpClient(client, capturePlat.getLoginUrl(), loginKeys, loginValues);
      if (!loginRet) {
        logger.error("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
        return null;
      }
      try {
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(simpleDate.parse(startTime));
        endDay.setTime(simpleDate.parse(endTime));
        List<String> listDay = getListDay(startDay, endDay);
        if (listDay == null) {
          return null;
        }
        for (String day : listDay) {
          String html = "";
          String apId = "";
          String bizDate = day;
          if (StringUtils.isNotBlank(day)) {
            day = day.replaceAll("-", "");
            apId = capturePlat.getChannelCode().substring(3);
            html = postByJson(client, capturePlat.getQueryUrl(),
                "{CommandName:'AP_V4.Chan_GetAllAppListPageData',Params:{\"BEGIN_ROW\":\"1\",\"END_ROW\":\"10\",\"ORDER_COLLUMN\":\"PAYABLE DESC\",\"INT_DAY\":"
                    + day + ",\"DATE_TYPE\":\"1\",\"AP_ID\":" + apId + ",\"APP_NAME\":\"\"}}");
          }
          html = html.replaceAll("\\[\\[", "[");
          html = html.replaceAll("\\]\\]", "]");
          JsonWmQuery wmObj = (JsonWmQuery) JsonParserUtils.parseFromJsonForObject(html, JsonWmQuery.class);
          List<String> rowsList = wmObj.getRows();
          ScrabData data = new ScrabData();
          if (rowsList.size() > 0) {
            data.setBizAmount(rowsList.get(2));
            data.setProductName(rowsList.get(1));
            data.setProductId(rowsList.get(0));
            data.setBizDate(bizDate);
            data.setBizType(ConstantsCMP.TYPE_CPS);
            data.setChannelId(capturePlat.getChannelCode());
            list.add(data);
          }
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
    return list;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[4];
    loginKeys[0] = "usermobile";
    loginKeys[1] = "password";
    loginKeys[2] = "seccode";
    loginKeys[3] = "hash";
    return loginKeys;
  }

  private List<String> getListDay(Calendar startDay, Calendar endDay) {
    List<String> list = new ArrayList<String>();
    // 开始日期-1，结束日期+1，这样才能取到开始日期和结束日期
    startDay.add(Calendar.DAY_OF_YEAR, -1);
    endDay.add(Calendar.DAY_OF_YEAR, 1);
    // 给出的日期开始日比结束日期大
    if (startDay.compareTo(endDay) >= 0) {
      return null;
    }
    Calendar currentPrintDay = startDay;
    while (true) {
      // 日期加一
      currentPrintDay.add(Calendar.DATE, 1);
      // 判断是否到达结束日期
      if (currentPrintDay.compareTo(endDay) == 0) {
        break;
      }
      list.add(simpleDate.format(currentPrintDay.getTime()));
    }
    return list;
  }
}
