package slh.capture.service.impl.unified;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.JsonParserUtils;
import slh.capture.domain.JsonDyQuery;
import slh.capture.domain.JsonDyQueryResult;
import slh.capture.domain.ScrabData;
import slh.capture.domain.oms.XyhQuery;
import slh.capture.domain.oms.XyhQueryResult;
import slh.capture.domain.unified.CaptureConfigEntity;


public class CapturePageServiceImpl {
  private static final Logger     logger     = LoggerFactory.getLogger(CapturePageServiceImpl.class);
  
  //指易付
    public  static List<ScrabData> getZyfPage(CloseableHttpClient client,String startTime,String endTime,int page, Map<Integer, DataNameEnum> dataIndex,String url, CaptureConfigEntity entity){
      url+="&currentPage="+page;
      List<ScrabData> list=new ArrayList<ScrabData>();
      HttpGet httpget = new HttpGet(url);
      HttpResponse response;
      try {
        response = client.execute(httpget);
        HttpEntity httpEntity = response.getEntity();
        String html = EntityUtils.toString(response.getEntity());
        if (httpEntity != null) {
          EntityUtils.consume(httpEntity);
        }
        list = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
        if(list.size()==100){
          page++;
          List<ScrabData> ll=getZyfPage(client, startTime, endTime, page,dataIndex,url,entity);
          list.addAll(ll);
        }
      }  catch (Exception e) {
        logger.error("ZYF page is Error");
      }
      return list;
    }
    
    //优蜜
    public  static List<ScrabData> getYmPage(CloseableHttpClient client,String startTime,String endTime,int page, Map<Integer, DataNameEnum> dataIndex,String url, CaptureConfigEntity entity){
      String uu=url;
      url+="?page="+page;
      String param = entity.getParams().trim();
      if (StringUtils.isNotBlank(param)) {
        String parAry[] = param.split(",");
        int i = 1;
        for (String string : parAry) {
          String arm = string.split(":")[0];
          if (StringUtils.isNotBlank(arm)) {
            if (i == 1) {
              url += "&" + arm + "=" + startTime;
            } else if (i == 2) {
              url += "&" + arm + "=" + endTime;
              break;
            } 
            i++;
          }
        }
      }
      List<ScrabData> list=new ArrayList<ScrabData>();
      HttpGet httpget = new HttpGet(url);
      HttpResponse response;
      try {
        response = client.execute(httpget);
        HttpEntity httpEntity = response.getEntity();
        String html = EntityUtils.toString(response.getEntity());
        if (httpEntity != null) {
          EntityUtils.consume(httpEntity);
        }
        list = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
        if(list.size()==100){
          page++;
          List<ScrabData> ll=getYmPage(client,startTime,endTime, page,dataIndex,uu,entity);
          list.addAll(ll);
        }
      }  catch (Exception e) {
        logger.error("YM page is Error");
      }
      System.out.println(list.size());
      return list;
    }
    
    //怡乐
    public  static List<ScrabData> getYlPage(CloseableHttpClient client,String startTime,String endTime,int page, Map<Integer, DataNameEnum> dataIndex,CaptureConfigEntity entity){
      List<ScrabData> list = new ArrayList<ScrabData>();
      List<NameValuePair> formparams = new ArrayList<NameValuePair>();
      String url = entity.getQueryUrl().trim();
      UrlEncodedFormEntity tity;
      try {
        formparams.add(new BasicNameValuePair("starttime", startTime));
        formparams.add(new BasicNameValuePair("endtime", endTime));
        formparams.add(new BasicNameValuePair("m", "click"));
        formparams.add(new BasicNameValuePair("pageid", String.valueOf(page)));
        tity = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(tity);
        HttpResponse response = client.execute(httppost);
        HttpEntity httpEntity = response.getEntity();
        String html = EntityUtils.toString(response.getEntity());
        if (entity != null) {
          EntityUtils.consume(httpEntity);
        }
        list = HtmlParserUtils.getTableZzValues(html, entity.getTableAttr(), entity.getTableAttrValue(),dataIndex);
        if(list.size()==30){
          page++;
          List<ScrabData> ll=getYlPage(client, startTime, endTime, page, dataIndex,  entity);
          list.addAll(ll);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return list;
    }
    
    //新银河
    public  static List<XyhQueryResult> getXyhPage(CloseableHttpClient client,String startDate,String endDate,int page, CaptureConfigEntity config,int size,String cpId){
      List<XyhQueryResult> list = new ArrayList<XyhQueryResult>();
      String html="";
      StringEntity entity;
      String jsonParam="{\"reqType\":\"F9902014\",\"cpid\":"+cpId+",\"startDate\":"+startDate+",\"endDate\":"+endDate+",\"cppid\":\"-1\",\"pagesize\":20,\"currentpage\":"+page+",\"totalRows\":"+size+"}";
      try {
        entity = new StringEntity(jsonParam);
        HttpPost httppost = new HttpPost(config.getQueryUrl());
        httppost.setEntity(entity);
        HttpResponse response = client.execute(httppost);
        HttpEntity httpEntity = response.getEntity();
        html = EntityUtils.toString(httpEntity);
        if (httpEntity != null) {
          EntityUtils.consume(httpEntity);
        }
        html = html.replaceAll("\\[\\[", "[");
        html = html.replaceAll("\\]\\]", "]");
        XyhQuery xyhQuery = (XyhQuery) JsonParserUtils
            .parseFromJsonForObject(html, XyhQuery.class);
        list = xyhQuery.getRows();
        if(list.size()==20){
          page++;
          List<XyhQueryResult> dataList=CapturePageServiceImpl.getXyhPage(client, startDate, endDate, page, config,size,cpId);
          list.addAll(dataList);
        }
      } catch (Exception e) {
        logger.error("Xyh page is error");
      }
      return list;
    }
    
  //彩云
    public  static List<ScrabData> getCyPage(CloseableHttpClient client,String url,int page, CaptureConfigEntity entity,Map<Integer, DataNameEnum> dataIndex){
      List<ScrabData> list = new ArrayList<ScrabData>();
      String uu=url+"&page="+page;
      HttpGet httppost = new HttpGet(uu);
      HttpResponse response;
      try {
        response = client.execute(httppost);
        HttpEntity httpEntity = response.getEntity();
        String html = EntityUtils.toString(response.getEntity());
        if (httpEntity != null) {
          EntityUtils.consume(httpEntity);
        }
        list = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(),dataIndex);
        if(list.size()==20){
          page++;
          List<ScrabData>ll=  CapturePageServiceImpl.getCyPage(client, url, page, entity,dataIndex);
          list.addAll(ll);
        }
      }  catch (Exception e) {
       logger.error("cy page is error");
      }
      return list;
    }
    
    //掌众
    public  static List<ScrabData> getZzPage(CloseableHttpClient client,String startTime,String endTime,int page, Map<Integer, DataNameEnum> dataIndex,CaptureConfigEntity entity){
      List<ScrabData> list = new ArrayList<ScrabData>();
      List<NameValuePair> formparams = new ArrayList<NameValuePair>();
      String url = entity.getQueryUrl().trim();
      UrlEncodedFormEntity tity;
      try {
        formparams.add(new BasicNameValuePair("beginDate", startTime));
        formparams.add(new BasicNameValuePair("endDate", endTime));
        formparams.add(new BasicNameValuePair("pageNum", String.valueOf(page)));
        formparams.add(new BasicNameValuePair("numPerPage", "20"));
        tity = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(tity);
        HttpResponse response = client.execute(httppost);
        HttpEntity httpEntity = response.getEntity();
        String html = EntityUtils.toString(response.getEntity());
        if (entity != null) {
          EntityUtils.consume(httpEntity);
        }
        list = HtmlParserUtils.getTableZzValues(html, entity.getTableAttr(), entity.getTableAttrValue(),dataIndex);
        if(list.size()==20){
          page++;
          List<ScrabData> ll=getZzPage(client, startTime, endTime, page, dataIndex,  entity);
          list.addAll(ll);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return list;
    }
    
    //动游
    public  static List<JsonDyQueryResult> getYdPage(CloseableHttpClient client,String startTime,String endTime,int page, CaptureConfigEntity entity){
      List<JsonDyQueryResult> list = new ArrayList<JsonDyQueryResult>();
      List<NameValuePair> formparams = new ArrayList<NameValuePair>();
      String url = entity.getQueryUrl().trim();
      UrlEncodedFormEntity tity;
      try {
        formparams.add(new BasicNameValuePair("customer_id", "30"));
        formparams.add(new BasicNameValuePair("pid", "0"));
        formparams.add(new BasicNameValuePair("cid", "0"));
        formparams.add(new BasicNameValuePair("startdate", startTime));
        formparams.add(new BasicNameValuePair("enddate", endTime));
        formparams.add(new BasicNameValuePair("page", String.valueOf(page)));
        formparams.add(new BasicNameValuePair("rows", "30"));
        formparams.add(new BasicNameValuePair("insert", "CPS"));
        tity = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(tity);
        HttpResponse response = client.execute(httppost);
        String html = EntityUtils.toString(response.getEntity());
        JsonDyQuery dyObj = (JsonDyQuery) JsonParserUtils.parseFromJsonForObject(html, JsonDyQuery.class);
        list.addAll(dyObj.getRows());
        if(dyObj.getTotal() == 31){
          page++;
          getYdPage(client, startTime, endTime, page,  entity);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return list;
    }
    
  //趣源
    public  static List<ScrabData> getQyPage(CloseableHttpClient client,Map<Integer, DataNameEnum> dataIndex,String startTime,String endTime,int page, CaptureConfigEntity entity,String aryName){
      List<ScrabData> list = new ArrayList<ScrabData>();
      List<NameValuePair> formparams = new ArrayList<NameValuePair>();
      String url = entity.getQueryUrl().trim();
      UrlEncodedFormEntity tity;
      try {
        formparams.add(new BasicNameValuePair("searchStartDate", startTime));
        formparams.add(new BasicNameValuePair("searchEndDate", endTime));
        formparams.add(new BasicNameValuePair("pageInfo.pageNo",String.valueOf(page)));
        formparams.add(new BasicNameValuePair("pageInfo.orderBy", "date"));
        formparams.add(new BasicNameValuePair("pageInfo.order", "desc"));
        formparams.add(new BasicNameValuePair("filter#EQ#app_name", aryName));
        tity = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(tity);
        HttpResponse response = client.execute(httppost);
        String html = EntityUtils.toString(response.getEntity());
        List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
        list.addAll(ll);
        if(ll!=null&&ll.size()==20){
          page++;
          CapturePageServiceImpl.getQyPage(client, dataIndex,startTime, endTime, page, entity,aryName);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return list;
    }
    
    //威搜游
    public  static List<ScrabData> getWsyPage(CloseableHttpClient client,Map<Integer, DataNameEnum> dataIndex,String startTime,String endTime,int page, CaptureConfigEntity entity){
      List<ScrabData> list = new ArrayList<ScrabData>();
      List<NameValuePair> formparams = new ArrayList<NameValuePair>();
      String url = entity.getQueryUrl().trim();
      url="http://121.11.79.19:8085/gamelist"+"/"+page;
      UrlEncodedFormEntity tity;
      try {
        if(StringUtils.isNotBlank(entity.getParams())){
          String ary[]=entity.getParams().trim().split(",");
          for (String string : ary) {
           String aa[]=string.split(":");
           formparams.add(new BasicNameValuePair(aa[0], aa[1]));
          }
        }
        formparams.add(new BasicNameValuePair("startDate", startTime));
        formparams.add(new BasicNameValuePair("endDate", endTime));
        tity = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(tity);
        HttpResponse response = client.execute(httppost);
        String html = EntityUtils.toString(response.getEntity());
        List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
        list.addAll(ll);
        if(ll!=null&&ll.size()==12){
          page++;
          CapturePageServiceImpl.getWsyPage(client, dataIndex,startTime, endTime, page, entity);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return list;
    }
    
  //匠游
    public  static List<ScrabData> getJyPage(CloseableHttpClient client,Map<Integer, DataNameEnum> dataIndex,String startTime,String endTime,int page, CaptureConfigEntity entity,String game,String appName,String state,String tion){
      List<ScrabData> list = new ArrayList<ScrabData>();
      List<NameValuePair> formparams = new ArrayList<NameValuePair>();
      String url = entity.getQueryUrl().trim();
      UrlEncodedFormEntity tity;
      try {
        formparams.add(new BasicNameValuePair("__VIEWSTATE", getValueByType(client, "http://112.124.60.92/admin/Sys_CPS_List.aspx", "__VIEWSTATE", "name")));
        formparams.add(new BasicNameValuePair("__EVENTVALIDATION", getValueByType(client, "http://112.124.60.92/admin/Sys_CPS_List.aspx", "__EVENTVALIDATION","name")));
        formparams.add(new BasicNameValuePair("Uc_Client$DDL", "sy50"));
        formparams.add(new BasicNameValuePair("txtFromDate", startTime));
        formparams.add(new BasicNameValuePair("txtToDate", endTime));
        formparams.add(new BasicNameValuePair("__EVENTTARGET", "AspNetPager1"));
        formparams.add(new BasicNameValuePair("__EVENTARGUMENT", page+""));
        formparams.add(new BasicNameValuePair("btnSearch", "查询"));
        formparams.add(new BasicNameValuePair("Uc_Kind$DDL", ""));
        formparams.add(new BasicNameValuePair("Uc_Gate$DDL", ""));
        formparams.add(new BasicNameValuePair("Uc_Game$DDL", game));
        formparams.add(new BasicNameValuePair("ddlOperator", ""));
        formparams.add(new BasicNameValuePair("FileUpload1", ""));
        tity = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(tity);
        HttpResponse response = client.execute(httppost);
        String html = EntityUtils.toString(response.getEntity());
        List<ScrabData> ll = HtmlParserUtils.getTableDataValues(html, entity.getTableAttr(), entity.getTableAttrValue(), dataIndex);
        list.addAll(ll);
        /*if(ll!=null&&ll.size()==20){
          page++;
          CapturePageServiceImpl.getJyPage(client, dataIndex,startTime, endTime, page, entity,game,appName,state,tion);
        }*/
      } catch (Exception e) {
        e.printStackTrace();
      }
      return list;
    }
    
    public static String getValueByType(CloseableHttpClient httpClient, String url, String value, String type) {
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
}
