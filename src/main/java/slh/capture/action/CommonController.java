package slh.capture.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import slh.capture.common.CompanyEnum;
import slh.capture.common.ConstantsCMP;
import slh.capture.common.JsonParserUtils;
import slh.capture.common.ResponseUtils;
import slh.capture.domain.ChannelMange;
import slh.capture.domain.ReturnQuery;
import slh.capture.domain.ScrabData;
import slh.capture.service.IChannelMangeService;
import slh.capture.service.IIntroductService;
import slh.capture.service.IOperateDataService;
import slh.capture.service.IPaymentQueryService;
import slh.capture.service.IReciveQueryService;
import slh.capture.service.IScrabService;

public class CommonController {
  private final static Logger  logger = LoggerFactory.getLogger(CommonController.class);

  @Autowired
  private IIntroductService    introductService;
  @Autowired
  private IChannelMangeService channelMangeService;
  @Autowired
  private IReciveQueryService  reciveQueryService;
  @Autowired
  private IPaymentQueryService paymentQueryService;
  @Autowired
  private IOperateDataService  operateDataService;
  @Autowired
  private IScrabService        scrabService;

  public List<ScrabData> validateCaptureData(List<ScrabData> list) throws Exception {

    List<ScrabData> ls = new ArrayList<ScrabData>();
    if(list != null){
    	for (ScrabData scrabData : list) {
    		
    		// 校验营帐系统中是否存在当前渠道
    		ChannelMange mangeForm = new ChannelMange();
    		mangeForm.setProductName(scrabData.getProductName());
    		mangeForm.setChannelCode(scrabData.getChannelId());
    		
    		ChannelMange mange = channelMangeService.findChannelMange(mangeForm);
    		
    		if (mange == null) {
    			scrabData.setMark("系统中不存在当前渠道!");
    		}
    		ls.add(scrabData);
    	}
    }

    return ls;
  }

  /**
   * 保存抓取的数据
   * 
   * @param list
   * @param ls
   * @throws Exception
   */
  public void saveData(List<ScrabData> list, List<ScrabData> ls) throws Exception {
    List<ScrabData> insertList = new ArrayList<ScrabData>();
    List<ScrabData> updateList = new ArrayList<ScrabData>();
    for (ScrabData scrabData : list) {
      boolean flag = false;
      for (ScrabData scData : ls) {
        if(!scrabData.getBookMark().equals(CompanyEnum.ZYF.getCompanyName())){
          if (scrabData.getBizDate().equals(scData.getBizDate()) && scrabData.getProductName().equals(scData.getProductName())
              && scrabData.getChannelId().equals(scData.getChannelId())) {
            scrabData.setState(scData.getState());
            updateList.add(scrabData);

            flag = true;
            break;
          }
        }else{
          String amount = String.format("%.2f", Double.parseDouble(scrabData.getBizAmount()));
          if (scrabData.getBizDate().equals(scData.getBizDate()) && scrabData.getProductName().equals(scData.getProductName())
              && scrabData.getChannelId().equals(scData.getChannelId())&&amount.equals(scData.getBizAmount()) ) {
            scrabData.setState(scData.getState());
            scrabData.setBizAmount(scData.getBizAmount());
            updateList.add(scrabData);

            flag = true;
            break;
          }
        }
       
      }
      if (!flag) {
        insertList.add(scrabData);
      }

    }

    // 抓取的数据入库
    scrabService.saveData(insertList);
    scrabService.modifyData(updateList);
  }

  /**
   * 抓取的数据存入数据查询系统
   * 
   * @param scrabData
   * @param httpServletResponse
   */
  public void saveScrabData(ScrabData scrabData, HttpServletResponse httpServletResponse) {

    HttpClientBuilder clientBuilder = HttpClientBuilder.create();
    RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).setConnectionRequestTimeout(5000)
        .setStaleConnectionCheckEnabled(true).build();
    clientBuilder.setDefaultRequestConfig(defaultRequestConfig);
    CloseableHttpClient client = clientBuilder.build();

    HttpResponse response = null;
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();

    formparams.add(new BasicNameValuePair("startTime", scrabData.getStartTime()));
    formparams.add(new BasicNameValuePair("endTime", scrabData.getEndTime()));
    formparams.add(new BasicNameValuePair("bookMark", scrabData.getBookMark()));
    formparams.add(new BasicNameValuePair("state", String.valueOf(ConstantsCMP.CAPTURE_DATA_STATE_NO)));
    formparams.add(new BasicNameValuePair("channelId", scrabData.getChannelId()));
    formparams.add(new BasicNameValuePair("productName", scrabData.getProductName()));

    String url = this.getPullUrl("pull.url");

    UrlEncodedFormEntity entity;
    try {
      entity = new UrlEncodedFormEntity(formparams, "UTF-8");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(entity);
      response = client.execute(httppost);

      int status = response.getStatusLine().getStatusCode();
      if (status != HttpStatus.SC_OK && status != HttpStatus.SC_MOVED_TEMPORARILY) {
        logger.error("login failed,width keys[{}],valus[{}]");

      }
      String string = EntityUtils.toString(response.getEntity());

      ReturnQuery returnQuery = (ReturnQuery) JsonParserUtils.parseFromJsonForObject(string, ReturnQuery.class);

      Header[] s = response.getAllHeaders();

      if (response != null) {
        response.getEntity().getContent().close();
      }

      if (returnQuery.getReturn_code().equals("0")) {
        ResponseUtils.responseFailure(httpServletResponse);
      } else if (returnQuery.getReturn_code().equals("1")) {
        ResponseUtils.responseSuccess(httpServletResponse);
      } else if (returnQuery.getReturn_code().equals("8")) {
        ResponseUtils.responseNoChannel(httpServletResponse);
      } else if (returnQuery.getReturn_code().equals("9")) {
        ResponseUtils.responseHadRecive(httpServletResponse);
      }

    } catch (Exception e) {
      logger.error(e.getMessage(), e);

      ResponseUtils.responseFailure(httpServletResponse);
    }

  }

  public String getPullUrl(String anyUrl) {
    InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.properties");
    Properties p = new Properties();
    try {
      p.load(input);
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    String url = p.getProperty(anyUrl);

    return url;
  }

  public void updateScrabData(ScrabData form, HttpServletResponse response) {

    List<ScrabData> list;
    try {
      list = scrabService.getScrabDataList(form);
      for (ScrabData scrabData : list) {
        scrabData.setState(ConstantsCMP.CAPTURE_DATA_STATE_NO);
      }

      scrabService.modifyData(list);

      ResponseUtils.responseSuccess(response);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      ResponseUtils.responseFailure(response);
    }

  }

}
