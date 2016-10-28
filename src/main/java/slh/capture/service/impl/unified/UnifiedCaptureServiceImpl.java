package slh.capture.service.impl.unified;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import slh.capture.common.CapturePlatEnum;
import slh.capture.common.ConstantsCMP;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.common.TableAttributeEnum;
import slh.capture.common.unified.CPSiteEnum;
import slh.capture.common.unified.DateTypeEnum;
import slh.capture.common.unified.TimeQueryTypeEnum;
import slh.capture.dao.unified.ICaptureConfigDAO;
import slh.capture.domain.ScrabData;
import slh.capture.domain.unified.CaptureConfigEntity;
import slh.capture.form.unified.CaptureQueryConditionForm;
import slh.capture.service.impl.ScrabServiceImpl;
import slh.capture.service.unified.IUnifiedCaptureService;
import slh.capture.util.ObjectConvertUtils;
import edu.hziee.common.lang.DateUtil;

@Service("unifiedCaptureService")
public class UnifiedCaptureServiceImpl extends ScrabServiceImpl implements IUnifiedCaptureService {

  private static final Logger     logger     = LoggerFactory.getLogger(UnifiedCaptureServiceImpl.class);

  @Autowired
  private ICaptureConfigDAO       captureConfigDAO;

  private static final DateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
  
  @Override
  public List<ScrabData> execute(CloseableHttpClient client, CaptureQueryConditionForm conditionform, HttpServletRequest req, HttpServletResponse res) {
    List<ScrabData> resultList = new ArrayList<ScrabData>();
    // 拉取数据配置信息
    List<CaptureConfigEntity> captureConfigList = captureConfigDAO.selectCaptureConfigList(ObjectConvertUtils.convertToCaptureConfig(conditionform));
    String[] loginKeys = null;
    String[] loginValues = null;
    for (CaptureConfigEntity config : captureConfigList) {
      if (config == null) {
        logger.info("execute() Failed, conditionform=[{}]", conditionform);
        return null;
      }
      if (StringUtils.isNotBlank(config.getLoginParam().trim())) {
        String ary[] = config.getLoginParam().trim().split(",");
        loginValues = new String[ary.length];
        loginKeys = new String[ary.length];
        for (int i = 0; i < ary.length; i++) {
          String[] val = ary[i].split(":");
          loginKeys[i] = val[0];
          if (val.length == 2) {
            loginValues[i] = val[1];
          } else {
             if (!CPSiteEnum.NEW_YM.getSite().contains(config.getLoginUrl().trim())) {// 如果不是优蜜
              loginValues[i] = conditionform.getRandomCode();
            } else {
              loginValues[i] = getYmToken(client, config.getLoginUrl());
            }
          }
        }
      }
      if(!config.getQueryPageUrl().trim().equals("true")){//如果为true就不需要登录
        this.loginPage(config.getLoginUrl(), client);
        boolean loginRet = getLoginHttpClient(client, config.getLoginUrl(), loginKeys, loginValues);
        if (!loginRet) {
          logger.info("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
          return null;
        }
      }
      String startDate = conditionform.getStartDate();
      String endDate = conditionform.getEndDate();
      if(config.getCpName().contains(CPSiteEnum.OCEAN_10086.getDesc())){//如果是OCEAN后台
        List<ScrabData> list=CaptureDataServiceImpl.getOceanCp(client, startDate, endDate, config, conditionform);
        if (list != null) {
          resultList.addAll(list);
        }
      } 
      else if (CPSiteEnum.CHINA_MOBILE_10086.getSite().equals(config.getLoginUrl().trim())) {// 10086网站
        List<ScrabData> list = CaptureDataServiceImpl.get10086(client, startDate, endDate, config, conditionform);
        if (list != null) {
          resultList.addAll(list);
        }
      } else if (CPSiteEnum.NEW_GALAXY.getSite().equals(config.getLoginUrl().trim())) {// 新银河处理方式
        List<ScrabData> list = CaptureDataServiceImpl.getXyh(1, client, startDate, endDate, config, conditionform);
        if (list != null) {
          resultList.addAll(list);
        }
      } else if (CPSiteEnum.NEW_ZYF.getSite().contains(config.getLoginUrl().trim())) {// 如果是指易付
       Boolean state=loginZyf(client,config);
       if(!state){
         return null;
       }
        List<ScrabData> list = getZyfList(startDate, endDate, client, config);
        list = getCount(list, conditionform, config.getChannelCode(), config.getAppName(), config.getBizType());// 如果日期相同就进行收益合计
        if (list != null) {
          resultList.addAll(list);
        }
      } else if (CPSiteEnum.NEW_YM.getSite().contains(config.getLoginUrl().trim())) {// 如果是优蜜
        List<ScrabData> list = getZyfList(startDate, endDate, client, config);
        for (ScrabData scrabData : list) {
          scrabData.setUserName(conditionform.getUserName());
          scrabData.setBizType(config.getBizType());
          if (StringUtils.isNotBlank(config.getChannelCode())) {
            scrabData.setChannelId(config.getChannelCode());
          }
        }
        if (list != null) {
          resultList.addAll(list);
        }
      } else if (CPSiteEnum.NEW_ZM.getSite().contains(config.getLoginUrl().trim())) {// 如果是掌盟
        List<ScrabData> list = CaptureDataServiceImpl.getZm(conditionform, startDate, endDate, config);
        if (list != null) {
          resultList.addAll(list);
        }
      } else if (CPSiteEnum.NEW_JT.getSite().contains(config.getLoginUrl().trim())) {// 如果是聚塔
        List<ScrabData> list = CaptureDataServiceImpl.getJt(client, startDate, config, conditionform, captureConfigList);
        if (list != null) {
          resultList.addAll(list);
        }
      } else if (CPSiteEnum.NEW_WB.getSite().contains(config.getLoginUrl().trim())) {// 如果是唯变
        List<ScrabData> list = CaptureDataServiceImpl.getWb(client, startDate, endDate, config, conditionform, captureConfigList);
        if (list != null) {
          resultList.addAll(list);
        }
      } else if (CPSiteEnum.NEW_YL.getSite().contains(config.getLoginUrl().trim())) {// 如果是怡乐
        List<ScrabData> list = CaptureDataServiceImpl.getYl(client, startDate, endDate, config, conditionform);
        if (list != null) {
          resultList.addAll(list);
        }
      } else if (CPSiteEnum.NEW_CY.getSite().contains(config.getLoginUrl().trim())) {// 如果是彩云
        List<ScrabData> list = CaptureDataServiceImpl.getCy(client, startDate, endDate, config, conditionform);
        if (list != null) {
          resultList.addAll(list);
        }
      } else if (CPSiteEnum.NEW_SZYL.getSite().contains(config.getLoginUrl().trim())) {// 如果是深圳云朗
        List<ScrabData> list = CaptureDataServiceImpl.getSzyl(client, startDate, endDate, config, conditionform);
        if (list != null) {
          resultList.addAll(list);
        }
      }else if (CPSiteEnum.NEW_ZZ.getSite().contains(config.getLoginUrl().trim())) {// 如果是掌众
        this.loginPage(config.getQueryPageUrl(), client);
        List<ScrabData> list = CaptureDataServiceImpl.getZz(client, startDate, endDate, config, conditionform);
        if (list != null) {
          resultList.addAll(list);
        }
      } else if (CPSiteEnum.NEW_DY.getSite().contains(config.getLoginUrl().trim())) {// 如果是动游
        resultList = CaptureDataServiceImpl.getDy(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_SY.getSite().contains(config.getLoginUrl().trim())) {// 如果是搜影
        resultList = CaptureDataServiceImpl.getSy(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_HX.getSite().contains(config.getLoginUrl().trim())) {// 如果是火星
        resultList = CaptureDataServiceImpl.getHx(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_HW.getSite().contains(config.getLoginUrl().trim())) {// 如果是幻网
        resultList = CaptureDataServiceImpl.getHw(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_JY.getSite().contains(config.getLoginUrl().trim())) {// 如果是匠游
        loginValues[loginValues.length-2]=getValueByType(client, config.getLoginUrl().trim(), "__EVENTVALIDATION", "name");
        loginValues[loginValues.length-3]=getValueByType(client, config.getLoginUrl().trim(), "__VIEWSTATE", "name");
        loginValues[loginValues.length-4]="";
        boolean state = getLoginHttpClient(client, config.getLoginUrl(), loginKeys, loginValues);
        if (!state) {
          logger.info("loginFailed,loginKeys=[{}],loginValues=[{}]", loginKeys, loginValues);
          return null;
        }
        resultList = CaptureDataServiceImpl.getJy(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_YY.getSite().contains(config.getLoginUrl().trim())) {// 如果是有缘
        resultList = CaptureDataServiceImpl.getYy(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_WSY.getSite().contains(config.getLoginUrl().trim())) {// 如果是威搜游
        resultList = CaptureDataServiceImpl.getWsy(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_WSY2.getSite().contains(config.getLoginUrl().trim())) {// 如果是威搜游
        resultList = CaptureDataServiceImpl.getWsy2(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_DW.getSite().contains(config.getLoginUrl().trim())) {// 如果是点我
        resultList = CaptureDataServiceImpl.getDw(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_QY.getSite().contains(config.getLoginUrl().trim())) {// 如果是趣源
        resultList = CaptureDataServiceImpl.getQy(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_TXFH.getSite().contains(config.getLoginUrl().trim())) {// 如果是天旭汇丰
        resultList = CaptureDataServiceImpl.getTxfh(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_FK.getSite().contains(config.getLoginUrl().trim())) {// 如果是风酷
        loginValues[2]=" ";
        getLoginHttpClient(client, config.getLoginUrl(), loginKeys, loginValues);
        resultList = CaptureDataServiceImpl.getFk(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_KY.getSite().contains(config.getLoginUrl().trim())) {// 如果是酷宇
        ClientConnectionManager cm = new MyBasicClientConnectionManager();
        DefaultHttpClient httpclient = new DefaultHttpClient(cm);
        loginValues[loginValues.length-5]=getValueByType(client, config.getLoginUrl(), "Button2", "name");
        loginValues[loginValues.length-4]="";
        loginValues[loginValues.length-3]="";
        loginValues[loginValues.length-2]=getValueByType(client, config.getLoginUrl(), "__VIEWSTATE", "name");
        loginValues[loginValues.length-1]=getValueByType(client, config.getLoginUrl(), "__EVENTVALIDATION", "name");
        boolean state=getLoginHttpClient(httpclient, config.getLoginUrl(), loginKeys, loginValues);
        String str = getDataQueryHtml(client, "http://info.coeeland.com/bss/home/menu.aspx");
        if(!state){
          return null;
        }
        resultList = CaptureDataServiceImpl.getKy(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_QP.getSite().contains(config.getLoginUrl().trim())) {// 如果是奇葩
        resultList = CaptureDataServiceImpl.getQp(client, startDate, endDate, config, conditionform,resultList);
      }/*else if (CPSiteEnum.NEW_KX.getSite().contains(config.getLoginUrl().trim())) {// 如果是开迅
        resultList = CaptureDataServiceImpl.getKx(client, startDate, endDate, config, conditionform,resultList);
      }*/else if (CPSiteEnum.NEW_KX2.getSite().contains(config.getLoginUrl().trim())) {// 如果是开迅2
        loginValues[loginValues.length-1]="142244"+getRandom();
        boolean state=getLoginHttpClient(client, config.getLoginUrl(), loginKeys, loginValues);
        if(!state){
          return null;
        }
        resultList = CaptureDataServiceImpl.getKx2(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_LC.getSite().contains(config.getLoginUrl().trim())) {// 如果是乐畅
        resultList = CaptureDataServiceImpl.getLc(client, startDate, endDate, config, conditionform,resultList);
      }else if (CPSiteEnum.NEW_XL.getSite().contains(config.getLoginUrl().trim())) {// 如果是炫亮
        resultList = CaptureDataServiceImpl.getXl(client, startDate, endDate, config, conditionform,resultList);
      }else if (config.getCpName().trim().contains(CPSiteEnum.NEW_LM.getDesc())) {// 如果是乐米
       if(config.getLoginUrl().trim().contains(CPSiteEnum.NEW_LM.getSite())){
         resultList = CaptureDataServiceImpl.getLm(client, startDate, endDate, config, conditionform,resultList);
       }else{
         resultList = CaptureDataServiceImpl.getLm2(client, startDate, endDate, config, conditionform);
       }
      }else if (config.getLoginUrl().trim().contains(CPSiteEnum.NEW_TY.getSite())) {// 如果是同娱
        resultList = CaptureDataServiceImpl.getTy(client, startDate, endDate, config, conditionform,resultList);
      }else if (config.getLoginUrl().trim().contains(CPSiteEnum.NEW_KLW.getSite())) {// 如果是快乐玩
        resultList = CaptureDataServiceImpl.getKlw(client, startDate, endDate, config, conditionform,resultList);
      }else if (config.getLoginUrl().trim().contains(CPSiteEnum.NEW_KZW.getSite())) {// 如果是空中网
        resultList = CaptureDataServiceImpl.getKzw(client, startDate, endDate, config, conditionform,resultList);
      }else if (config.getLoginUrl().trim().contains(CPSiteEnum.NEW_CSL.getSite())) {// 如果是创斯林
        resultList = CaptureDataServiceImpl.getCsl(client, startDate, endDate, config, conditionform,resultList);
        resultList = getCount1(resultList, conditionform, null, null, config.getBizType());// 如果日期相同就进行收益合计
      }
      else {
        // 页面数据的展示顺序,如日期(1), 渠道号(2), 产品(3), 客户实收(4),括号里为下标
        Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
        String dataIndexParam = config.getDataIndex();
        if (StringUtils.isBlank(dataIndexParam)) {
          logger.info("execute() Failed, conditionform=[{}]", conditionform.toString());
          return null;
        }

        String[] dataIndexParamArgs = dataIndexParam.split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
        // ====================================================================
        List<ScrabData> ls = new ArrayList<ScrabData>();
        Set<ScrabData> dataSet = new HashSet<ScrabData>();

        String params = config.getParams().trim();
        if (StringUtils.isBlank(params)) {
          logger.info("enter execute(), but the params is empty, conditionform=[{}] ", conditionform.toString());
          return null;
        }

        String[] paramsArgs = params.trim().split(",");
        int length = paramsArgs.length;
        if (length < 2) {
          logger.info("enter execute(), but the params length is small then 2, paramsArgs=[{}] ", paramsArgs);
          return null;
        }

        try {
          List<NameValuePair> formparams = this.generateFormparams(paramsArgs, config.getDateType(), config.getTimeQueryType(), conditionform);
          UrlEncodedFormEntity entity;
          entity = new UrlEncodedFormEntity(formparams, "UTF-8");
          HttpPost httppost = new HttpPost(config.getQueryUrl());
          httppost.setEntity(entity);
          HttpResponse response = client.execute(httppost);
          HttpEntity httpEntity = response.getEntity();
          String html = EntityUtils.toString(response.getEntity());
          if (entity != null) {
            EntityUtils.consume(httpEntity);
          }

          List<ScrabData> parseList = new ArrayList<ScrabData>();
          if (StringUtils.isNotBlank(config.getQueryPageUrl()) && StringUtils.isNotBlank(config.getPageKey())) {// 是否需要分页
            int index = html.indexOf(config.getPageKey().trim());
            if (index != -1) {
              String totalStr = html.substring(index + 6, index + 15);
              int dataTotal = getNumberFromString(totalStr).get(0);
              List<ScrabData> list = HtmlParserUtils.getTableDataValues(html, config.getTableAttr(), config.getTableAttrValue(), dataIndex);
              for (ScrabData scrabData : list) {
                scrabData.setUserName(conditionform.getUserName());
                if (StringUtils.isNotBlank(conditionform.getChannelCode())) {
                  scrabData.setChannelId(conditionform.getChannelCode());
                }
              }
              parseList.addAll(list);
              // 分页的情况
              int pageSize = list.size();
              if (dataTotal > pageSize) {
                int pageNum = dataTotal / pageSize;
                if (dataTotal % pageSize != 0) {
                  pageNum = dataTotal / pageSize + 1;
                }

                for (int i = 2; i <= pageNum; i++) {
                  formparams = new ArrayList<NameValuePair>();
                  // 前两个是固定的
                  formparams.add(new BasicNameValuePair(paramsArgs[0].split(":")[0], startDate));
                  formparams.add(new BasicNameValuePair(paramsArgs[1].split(":")[0], endDate));

                  for (int j = 2; j < length; j++) {
                    if (StringUtils.isBlank(paramsArgs[j])) {
                      logger.info("enter execute(), but the param is empty, conditionform=[{}] ", conditionform.toString());
                      return null;
                    }

                    if (paramsArgs[j].length() > 1) {
                      formparams.add(new BasicNameValuePair(paramsArgs[j].split(":")[0].trim(), paramsArgs[j].split(":")[1].trim()));
                    }
                  }

                  entity = new UrlEncodedFormEntity(formparams, "UTF-8");
                  httppost = new HttpPost(config.getQueryPageUrl() + "/" + String.valueOf(i));
                  httppost.setEntity(entity);
                  response = client.execute(httppost);
                  httpEntity = response.getEntity();
                  html = EntityUtils.toString(response.getEntity());
                  if (entity != null) {
                    EntityUtils.consume(httpEntity);
                  }

                  List<ScrabData> listWithPage = HtmlParserUtils.getTableDataValues(html, config.getTableAttr(), config.getTableAttrValue(), dataIndex);
                  for (ScrabData scrabData : listWithPage) {
                    scrabData.setUserName(conditionform.getUserName());
                  }
                  parseList.addAll(listWithPage);
                }
              }
            }
          }

          dataSet.addAll(parseList);
        } catch (Exception e) {
          logger.error("UnifiedCaptureServiceImpl.exstractGcData() is failed!");
          logger.error(e.getMessage(), e);
        }

        ls.addAll(dataSet);
        // ====================================================================

        for (ScrabData scrabData : ls) {
          scrabData.setBizType(ConstantsCMP.TYPE_CPA);
          String date = scrabData.getBizDate();
          scrabData.setBizDate(date.substring(0, 10));
        }
        if (ls != null) {
          resultList.addAll(ls);
        }
      }
      break;
    }
    return resultList;
  }
  /**
   * 生成表单参数
   * 
   * */
  private List<NameValuePair> generateFormparams(String[] paramsArgs, int dateType, int timeQueryType, CaptureQueryConditionForm conditionform)
      throws Exception {
    // 前三个是固定的
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    if (DateTypeEnum.YYYY_MM_DD.getType() == dateType && timeQueryType == TimeQueryTypeEnum.DAY_REGION.getType()) {
      formparams.add(new BasicNameValuePair(paramsArgs[0].split(":")[0], conditionform.getStartDate()));
      formparams.add(new BasicNameValuePair(paramsArgs[1].split(":")[0], conditionform.getEndDate()));
      if (paramsArgs[2].split(":").length > 1) {
        formparams.add(new BasicNameValuePair(paramsArgs[2].split(":")[0], paramsArgs[2].split(":")[1].trim()));
      } else {
        formparams.add(new BasicNameValuePair(paramsArgs[2].split(":")[0], conditionform.getChannelCode()));
      }

      for (int j = 3; j < paramsArgs.length; j++) {
        if (StringUtils.isBlank(paramsArgs[j])) {
          logger.info("enter execute(), but the param is empty, conditionform=[{}] ", conditionform.toString());
          return formparams;
        }

        if (paramsArgs[j].length() > 1) {
          formparams.add(new BasicNameValuePair(paramsArgs[j].split(":")[0].trim(), paramsArgs[j].split(":")[1].trim()));
        }
      }
    } else if (DateTypeEnum.YYYYMM.getType() == dateType && TimeQueryTypeEnum.CURRENT_MONTH.getType() == timeQueryType) {
      String startDate = conditionform.getStartDate();
      startDate = startDate.replaceAll("-", "");
      String month = startDate.substring(0, 6);
      formparams.add(new BasicNameValuePair(paramsArgs[0].split(":")[0], month));
      if (paramsArgs[1].split(":").length > 1) {
        formparams.add(new BasicNameValuePair(paramsArgs[1].split(":")[0], paramsArgs[1].split(":")[1].trim()));
      } else {
        formparams.add(new BasicNameValuePair(paramsArgs[1].split(":")[0], conditionform.getChannelCode()));
      }

      String p1 = "/wEPDwUJNjgxNDM1NDAwD2QWAgIBD2QWBAIBDxBkEBUGEC0t5omA5pyJ5rig6YGTLS0OeW91eW91LWRtLWp0MSAKZGFpamlfanQxMQpkYWlqaV9qdDEyCmRhaWppX2p0MTMLZGFpamlfanQxNCAVBgEwDnlvdXlvdS1kbS1qdDEgCmRhaWppX2p0MTEKZGFpamlfanQxMgpkYWlqaV9qdDEzC2RhaWppX2p0MTQgFCsDBmdnZ2dnZ2RkAgMPEGQPFhRmAgECAgIDAgQCBQIGAgcCCAIJAgoCCwIMAg0CDgIPAhACEQISAhMWFBAFBjIwMTMwMQUGMjAxMzAxZxAFBjIwMTMwMgUGMjAxMzAyZxAFBjIwMTMwMwUGMjAxMzAzZxAFBjIwMTMwNAUGMjAxMzA0ZxAFBjIwMTMwNQUGMjAxMzA1ZxAFBjIwMTMwNgUGMjAxMzA2ZxAFBjIwMTMwNwUGMjAxMzA3ZxAFBjIwMTMwOAUGMjAxMzA4ZxAFBjIwMTMwOQUGMjAxMzA5ZxAFBjIwMTMxMAUGMjAxMzEwZxAFBjIwMTMxMQUGMjAxMzExZxAFBjIwMTMxMgUGMjAxMzEyZxAFBjIwMTQwMQUGMjAxNDAxZxAFBjIwMTQwMgUGMjAxNDAyZxAFBjIwMTQwMwUGMjAxNDAzZxAFBjIwMTQwNAUGMjAxNDA0ZxAFBjIwMTQwNQUGMjAxNDA1ZxAFBjIwMTQwNgUGMjAxNDA2ZxAFBjIwMTQwNwUGMjAxNDA3ZxAFBjIwMTQwOAUGMjAxNDA4Z2RkZF0BINlzL8lRdorsruNzV+roYiLv";
      p1 = UriUtils.encodePath(p1, "utf-8");
      String p2 = "/wEWBQLpx+v+BQKklcbgAQK1lcmJCgK12/nuAwKln/OLAoAAcQHOiXuDnvh5LAyCylit9zGI";
      p2 = UriUtils.encodePath(p2, "utf-8");

      formparams.add(new BasicNameValuePair("__VIEWSTATE", p1));
      formparams.add(new BasicNameValuePair("__EVENTVALIDATION", p2));

      // for (int j = 2; j < paramsArgs.length; j++) {
      // if (StringUtils.isBlank(paramsArgs[j])) {
      // logger.info("enter execute(), but the param is empty, conditionform=[{}] ",
      // conditionform.toString());
      // return formparams;
      // }
      //
      // if (paramsArgs[j].length() > 1) {
      // formparams.add(new
      // BasicNameValuePair(paramsArgs[j].split(":")[0].trim(),
      // paramsArgs[j].split(":")[1].trim()));
      // }
      // }
    }

    return formparams;
  }

  @Override
  public String[] genLoginKeys() {
    String[] loginKeys = new String[3];
    loginKeys[0] = "username";
    loginKeys[1] = "password";
    loginKeys[2] = "authcode";
    return loginKeys;
  }

  public String[] genZyfLoginKeys() {
    String[] loginKeys = new String[3];
    loginKeys[0] = "j_username";
    loginKeys[1] = "j_password";
    loginKeys[2] = "j_captcha";
    return loginKeys;
  }

  @Override
  public String[] genLoginValues(String imgCode) {
    String[] loginValues = new String[3];
    return loginValues;
  }

  @Override
  public Map<QueryParamEnum, String> genParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 真趣数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_DATE, "startTime");
    paramMap.put(QueryParamEnum.END_DATE, "endTime");
    paramMap.put(QueryParamEnum.DATEFORMATE, "dateFormate");
    paramMap.put(QueryParamEnum.PAGENO, "page.pageNo");
    return paramMap;
  }

  @Override
  public Map<String, Object> genCustomParamMap(String startTime, String endTime, String productId, String channelId) {
    Map<String, Object> customParamMap = new HashMap<String, Object>();
    customParamMap.put("url", CapturePlatEnum.JOY.getQueryUrl());
    customParamMap.put("startTime", DateUtil.parseDate(startTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("endTime", DateUtil.parseDate(endTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("appId", productId);
    customParamMap.put("channelId", channelId);

    // 真趣后台查询结果table属性
    customParamMap.put("table_attr", "id");
    customParamMap.put("table_attr_value", "cl");

    // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    dataIndex.put(1, DataNameEnum.BIZ_DATE);
    dataIndex.put(2, DataNameEnum.PRODUCT);
    dataIndex.put(3, DataNameEnum.CHANNEL_ID);
    dataIndex.put(4, DataNameEnum.BIZ_TYPE);
    dataIndex.put(5, DataNameEnum.BIZ_AMOUNT);
    customParamMap.put("column_index", dataIndex);
    return customParamMap;
  }

  public Map<String, Object> genMMCustomParamMap(String startTime, String endTime, String productId, String channelId) {
    Map<String, Object> customParamMap = new HashMap<String, Object>();
    customParamMap.put("url", CapturePlatEnum.JOY_MM.getQueryUrl());
    customParamMap.put("query.beginTime", DateUtil.parseDate(startTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("query.endTime", DateUtil.parseDate(endTime, DateUtil.TRADITION_PATTERN));
    customParamMap.put("query.appId", productId);
    customParamMap.put("query.channelId", channelId);

    // 真趣后台查询结果table属性
    customParamMap.put("table_attr", "id");
    customParamMap.put("table_attr_value", "cl");

    // 页面数据的展示顺序,如日期(1),应用(2),渠道号(3),业务类型(4),客户实收(5),括号里为下标
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    dataIndex.put(0, DataNameEnum.BIZ_DATE);
    dataIndex.put(1, DataNameEnum.PRODUCT);
    dataIndex.put(2, DataNameEnum.CHANNEL_ID);
    dataIndex.put(3, DataNameEnum.BIZ_AMOUNT);
    customParamMap.put("column_index", dataIndex);
    return customParamMap;
  }

  public Map<QueryParamEnum, String> genMMParamMap() {

    Map<QueryParamEnum, String> paramMap = new HashMap<QueryParamEnum, String>();
    // 真趣数据查询页面属性name值
    paramMap.put(QueryParamEnum.URL, "url");
    paramMap.put(QueryParamEnum.START_TIME, "query.beginTime");
    paramMap.put(QueryParamEnum.END_TIME, "query.endTime");
    paramMap.put(QueryParamEnum.PRODUCT_ID, "query.appId");
    paramMap.put(QueryParamEnum.CHANNEL_ID, "query.channelId");
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
  public List<ScrabData> exstractGcData(CloseableHttpClient httpClient, Map<String, Object> customParamMap) {
    List<ScrabData> dataList = new ArrayList<ScrabData>();
    Set<ScrabData> dataSet = new HashSet<ScrabData>();
    String url = (String) customParamMap.get(QueryParamEnum.URL.getKey());
    String statDate = (String) customParamMap.get(QueryParamEnum.START_DATE.getKey());
    String endDate = (String) customParamMap.get(QueryParamEnum.END_DATE.getKey());
    String channelId = "0";
    String appId = "4";
    String grouptype = "day";

    String tableAttr = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR.getAttr());
    String tableAttrValue = (String) customParamMap.get(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr());
    Map<Integer, DataNameEnum> dataIndex = (Map<Integer, DataNameEnum>) customParamMap.get(TableAttributeEnum.COLUMN_INDEX.getAttr());

    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair(QueryParamEnum.START_DATE.getKey(), statDate));
    formparams.add(new BasicNameValuePair(QueryParamEnum.END_DATE.getKey(), endDate));
    formparams.add(new BasicNameValuePair(QueryParamEnum.CHANNEL_ID.getKey(), channelId));
    formparams.add(new BasicNameValuePair(QueryParamEnum.APP_ID.getKey(), appId));
    formparams.add(new BasicNameValuePair(QueryParamEnum.GROUP_TYPE.getKey(), grouptype));

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

      int index = html.indexOf("Total");
      List<ScrabData> parseList = new ArrayList<ScrabData>();
      if (index != -1) {
        String totalStr = html.substring(index + 6, index + 15);
        int dataTotal = getNumberFromString(totalStr).get(0);
        List<ScrabData> list = HtmlParserUtils.getTableDataValues(html, tableAttr, tableAttrValue, dataIndex);
        parseList.addAll(list);
        // 分页的情况
        int pageSize = list.size();
        if (dataTotal > pageSize) {
          int pageNum = dataTotal / pageSize;
          if (dataTotal % pageSize != 0) {
            pageNum = dataTotal / pageSize + 1;
          }

          for (int i = 2; i <= pageNum; i++) {
            formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair(QueryParamEnum.START_DATE.getKey(), statDate));
            formparams.add(new BasicNameValuePair(QueryParamEnum.END_DATE.getKey(), endDate));
            formparams.add(new BasicNameValuePair(QueryParamEnum.CHANNEL_ID.getKey(), channelId));
            formparams.add(new BasicNameValuePair(QueryParamEnum.APP_ID.getKey(), appId));
            formparams.add(new BasicNameValuePair(QueryParamEnum.GROUP_TYPE.getKey(), grouptype));

            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost = new HttpPost(url + "/page" + "/" + String.valueOf(i));
            httppost.setEntity(entity);
            response = httpClient.execute(httppost);
            httpEntity = response.getEntity();
            html = EntityUtils.toString(response.getEntity());
            if (entity != null) {
              EntityUtils.consume(httpEntity);
            }

            List<ScrabData> listWithPage = HtmlParserUtils.getTableDataValues(html, tableAttr, tableAttrValue, dataIndex);
            parseList.addAll(listWithPage);
          }
        }
      }

      dataSet.addAll(parseList);
    } catch (Exception e) {
      logger.error("TydScrabServiceImpl.exstractGcData() is failed!");
      logger.error(e.getMessage(), e);
    }

    dataList.addAll(dataSet);
    return dataList;
  }

  /**
   * 从字符串中获取数字
   * 
   * @param str
   * */
  public static List<Integer> getNumberFromString(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    }

    List<Integer> list = new ArrayList<Integer>();
    Pattern p = Pattern.compile("[0-9\\.]+");
    Matcher m = p.matcher(str);

    while (m.find()) {
      list.add(Integer.valueOf(m.group()));
    }

    return list;
  }
  
  //指易付登陆
  public boolean loginZyf(CloseableHttpClient httpClient,CaptureConfigEntity config){
    String str="http://dev.mopo.com/paypage";
    String loginKeys[]=new String[4];
    String loginValues[]=new String[4];
    loginKeys[0]="j_username";
    loginKeys[1]="j_password";
    loginKeys[2]="j_mark";
    loginKeys[3]="CP_LOGIN_SESSIONID";
    loginValues[0]=getValueByType(httpClient, str, "j_username", "name");
    loginValues[1]=getValueByType(httpClient, str, "j_password", "name");
    loginValues[2]=getValueByType(httpClient, str, "j_mark", "name");
    loginValues[3]=getValueByType(httpClient, str, "CP_LOGIN_SESSIONID", "name");
    boolean loginRet = getLoginHttpClient(httpClient, "http://easypay.51mrp.com/android-admin/j_spring_security_check", loginKeys, loginValues);
    return loginRet;
  }
  
  // 数据拉取(指易付,优蜜)
  public List<ScrabData> getZyfList(String startTime, String endTime, CloseableHttpClient httpClient, CaptureConfigEntity entity) {
    String ss = getDataQueryHtml(httpClient, "http://easypay.51mrp.com/android-admin/");
    List<ScrabData> list = new ArrayList<ScrabData>();
    Map<Integer, DataNameEnum> dataIndex = new HashMap<Integer, DataNameEnum>();
    Set<ScrabData> dataSet = new HashSet<ScrabData>();
    String url = entity.getQueryUrl().trim();
    String param = entity.getParams().trim();
    try {
      if (StringUtils.isNotBlank(entity.getDataIndex())) {
        String dataIndexParamArgs[] = entity.getDataIndex().split(",");
        for (String d : dataIndexParamArgs) {
          if (StringUtils.isNotBlank(d)) {
            String[] dArgs = d.split(":");
            dataIndex.put(Integer.parseInt(dArgs[0].trim()), DataNameEnum.getDataNameEnum(dArgs[1].trim()));
          }
        }
      }
      startTime = URLEncoder.encode(startTime, "utf-8");
      endTime = URLEncoder.encode(endTime, "utf-8");
      if (StringUtils.isNotBlank(param)) {
        String parAry[] = param.split(",");
        int i = 1;
        for (String string : parAry) {
          String arm = string.split(":")[0];
          if (StringUtils.isNotBlank(arm)) {
            if (i == 1) {
              url += "?" + arm + "=" + startTime;
            } else if (i == 2) {
              url += "&" + arm + "=" + endTime;
            } else {
              url += "&" + arm + "=" + "";
            }
            i++;
          }
        }
      }
      HttpGet httpget = new HttpGet(url);
      HttpResponse response = httpClient.execute(httpget);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      List<ScrabData> parseList = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
      if (parseList.size() == 100) {// 获取分页
        List<ScrabData> ll = new ArrayList<ScrabData>();
        if (CPSiteEnum.NEW_ZYF.getSite().contains(entity.getLoginUrl().trim())) {
          ll = CapturePageServiceImpl.getZyfPage(httpClient, startTime, endTime, 2, dataIndex, url, entity);
        } else if (CPSiteEnum.NEW_YM.getSite().contains(entity.getLoginUrl().trim())) {
          ll = CapturePageServiceImpl.getYmPage(httpClient, startTime, endTime, 2, dataIndex, entity.getQueryUrl().trim(), entity);
        }
        parseList.addAll(ll);
      }
      dataSet.addAll(parseList);
    } catch (Exception e) {
      e.printStackTrace();
    }
    list.addAll(dataSet);
    return list;
  }

  public String getYmToken(CloseableHttpClient httpClient, String url) {
    HttpGet httpget = new HttpGet(url);
    HttpResponse response;
    String str = "";
    try {
      response = httpClient.execute(httpget);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      str = HtmlParserUtils.getInputTagValues(html, "name", "csrfmiddlewaretoken");
    } catch (Exception e) {
      logger.error("getToken is error");
    }
    return str;
  }
  
  public String getValueByType(CloseableHttpClient httpClient, String url,String value,String type) {
    HttpGet httpget = new HttpGet(url);
    HttpResponse response;
    String str = "";
    try {
      response = httpClient.execute(httpget);
      HttpEntity httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      if (httpEntity != null) {
        EntityUtils.consume(httpEntity);
      }
      str = HtmlParserUtils.getInputTagValues(html, type, value);
    } catch (Exception e) {
      logger.error("getToken is error");
    }
    return str;
  }
  
  public List<ScrabData> getCount(List<ScrabData> list, CaptureQueryConditionForm conditionform, String channel, String appName, int type) {
    List<ScrabData> ll = new ArrayList<ScrabData>();
    Map<String, ScrabData> map = new HashMap<String, ScrabData>();
    for (ScrabData scrabData : list) {
      ScrabData scr = new ScrabData();
      if (map.containsKey(scrabData.getBizDate())) {
        scr = map.get(scrabData.getBizDate());
        float count = Float.valueOf(scr.getBizAmount()) + Float.valueOf(scrabData.getBizAmount());
        scr.setBizAmount(String.valueOf(count));
        map.put(scrabData.getBizDate(), scr);
      } else {
        map.put(scrabData.getBizDate(), scrabData);
      }
    }
    Iterator iter = map.keySet().iterator(); // 获得map的Iterator
    while (iter.hasNext()) {
      ScrabData scrabData = new ScrabData();
      String key = (String) iter.next();
      scrabData = map.get(key);
      scrabData.setUserName(conditionform.getUserName());
      scrabData.setBizType(type);
      if (StringUtils.isNotBlank(appName)) {
        scrabData.setProductName(appName);
      }
      if (StringUtils.isNotBlank(channel)) {
        scrabData.setChannelId(channel);
      }
      ll.add(scrabData);
    }
    return ll;
  }
  
  public List<ScrabData> getCount1(List<ScrabData> list, CaptureQueryConditionForm conditionform, String channel, String appName, int type) {
    List<ScrabData> ll = new ArrayList<ScrabData>();
    Map<String, ScrabData> map = new HashMap<String, ScrabData>();
    for (ScrabData scrabData : list) {
      ScrabData scr = new ScrabData();
      if (map.containsKey(scrabData.getBizDate()+"-"+scrabData.getChannelId())) {
        scr = map.get(scrabData.getBizDate()+"-"+scrabData.getChannelId());
        float count = Float.valueOf(scr.getBizAmount()) + Float.valueOf(scrabData.getBizAmount());
        scr.setBizAmount(String.valueOf(count));
        map.put(scrabData.getBizDate()+"-"+scrabData.getChannelId(), scr);
      } else {
        map.put(scrabData.getBizDate()+"-"+scrabData.getChannelId(), scrabData);
      }
    }
    Iterator iter = map.keySet().iterator(); // 获得map的Iterator
    while (iter.hasNext()) {
      ScrabData scrabData = new ScrabData();
      String key = (String) iter.next();
      scrabData = map.get(key);
      scrabData.setUserName(conditionform.getUserName());
      scrabData.setBizType(type);
      if (StringUtils.isNotBlank(appName)) {
        scrabData.setProductName(appName);
      }
      if (StringUtils.isNotBlank(channel)) {
        scrabData.setChannelId(channel);
      }
      ll.add(scrabData);
    }
    return ll;
  }
  
  public static String getRandom() {
    String math="";
    for(int i=0;i<4;i++){
      int mm=(int) (Math.random()*10);
      math+=String.valueOf(mm);
    }
    return math;
  }
  
  @Override
  public void delete(ScrabData form) {
    captureConfigDAO.delete(form);
  }
}
