/**
 * 数据抓取通用接口
 */
package slh.capture.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.impl.client.CloseableHttpClient;

import slh.capture.common.DataGridModel;
import slh.capture.common.QueryParamEnum;
import slh.capture.domain.ScrabData;

public interface IScrabService {

  /**
   * 获取登录页的验证码图片信息
   * 
   * @param imgUrl
   *          图片URl
   * @return
   */
  public byte[] getLoginImageData(String imgUrl, HttpServletRequest request);

  /**
   * 获取验证码https
   * @param imgUrl
   * @param request
   * @return
   */
  public byte[] getLoginImageDataForHttps(String imgUrl, HttpServletRequest request);
  
  /**
   * 用户登录信息
   * 
   * @param loginUrl
   *          登录地址
   * @param username
   *          用户名
   * @param password
   *          密码
   * @param verifyCode
   *          验证码
   * @return
   */
  public boolean getLoginHttpClient(CloseableHttpClient client, String loginUrl, String[] key, String[] value);

  /**
   * 获取抓取的数据
   * 
   * @param httpClient
   *          客户端client
   * @param url
   *          获取数据的URL地址
   * @param productId
   *          产品ID
   * @param channelId
   *          渠道ID
   * @param startTime
   *          开始时间
   * @param endTime
   *          结束时间
   * @param keys
   *          查询参数名
   * @return
   */
  public List<ScrabData> exstractData(CloseableHttpClient httpClient, Map<QueryParamEnum, String> paramMap, Map<String, Object> customParamMap);

  /**
   * 执行抓取操作
   */
  public List<ScrabData> execute(CloseableHttpClient client, String startDate, String endDate, String imgCode);

  /**
   * 保存抓取的数据
   * 
   * @param dataList
   * @return
   */
  public int saveData(List<ScrabData> dataList);

  /**
   * 修改抓取的数据
   * 
   * @param list
   * @throws Exception
   */
  public void modifyData(List<ScrabData> list) throws Exception;

  /**
   * 查询抓取的数据
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public ScrabData findScrabData(ScrabData form) throws Exception;

  /**
   * 根据合作商查询抓取的数据
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public ScrabData findScrabDataByBookMark(ScrabData form) throws Exception;

  /**
   * 抓取的数据不分页
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<ScrabData> getScrabDataList(ScrabData form) throws Exception;

  /**
   * 抓取的数据分页
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public Map<String, Object> getScrabData(DataGridModel page, ScrabData form) throws Exception;

  /**
   * 数据调用处理
   * 
   * @return
   */
  public int invokeDataHander();

  /**
   * 获取查询页面html
   * 
   * @param queryUrl
   * @return
   */
  public String getDataQueryHtml(CloseableHttpClient httpClient, String queryUrl);

  /**
   * 生成登陆页面参数名数组
   * 
   * @return
   */
  String[] genLoginKeys();

  /**
   * 生成登陆页面参数值
   * 
   * @return
   */
  String[] genLoginValues(String imgCode);

  /**
   * 获取组成参数
   * 
   * @return
   */
  Map<QueryParamEnum, String> genParamMap();

  /**
   * 获取组成参数值
   * 
   * @return
   */
  Map<String, Object> genCustomParamMap(String startTime, String endTime, String productId, String channelId);
  
  /**
   * 获取渠道号,返回结果为json
   * @param client
   * @param url
   * @param paramMap
   * @return
   */
  String getChannelIdByJsonPost(CloseableHttpClient client, String url, Map<String,String> paramMap);
}
