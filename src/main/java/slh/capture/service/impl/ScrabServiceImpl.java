/**
 * 页面数据抓取通用父类service
 */
package slh.capture.service.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import slh.capture.common.CapturePlatEnum;
import slh.capture.common.DataGridModel;
import slh.capture.common.DataNameEnum;
import slh.capture.common.HtmlParserUtils;
import slh.capture.common.JsonParserUtils;
import slh.capture.common.QueryParamEnum;
import slh.capture.common.TableAttributeEnum;
import slh.capture.dao.IScrabDAO;
import slh.capture.domain.ScrabData;
import slh.capture.domain.fun.FunQueryResult;
import slh.capture.domain.oms.OmsQuery;
import slh.capture.domain.oms.OmsQueryResult;
import slh.capture.domain.zsjh.ZsjhQuery;
import slh.capture.domain.zsjh.ZsjhQueryResult;
import slh.capture.service.IScrabService;

import com.google.gson.reflect.TypeToken;

import edu.hziee.common.lang.DateUtil;

@Service("scrabService")
public class ScrabServiceImpl implements IScrabService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private IScrabDAO scrabDAO;
	private int MAX_QUERY_DAY = 10;

	@Override
	public byte[] getLoginImageData(String imgUrl, HttpServletRequest request) {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setSocketTimeout(10000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000)
				.setStaleConnectionCheckEnabled(true).build();
		clientBuilder.setDefaultRequestConfig(defaultRequestConfig);
		CloseableHttpClient httpclient = clientBuilder.build();
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(imgUrl);
			logger.debug("download image by url=[{}]", imgUrl);
			response = httpclient.execute(httpget);
			HttpSession session = request.getSession();
			session.setAttribute("client", httpclient);
			HttpEntity httpEntity = response.getEntity();
			byte[] data = EntityUtils.toByteArray(httpEntity);
			if (httpEntity != null) {
				EntityUtils.consume(httpEntity);
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public byte[] getLoginImageDataForHttps(String imgUrl,
			HttpServletRequest request) {

		CloseableHttpClient httpclient = createSSLInsecureClient();
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(imgUrl);
			logger.debug("download image by url=[{}]", imgUrl);
			response = httpclient.execute(httpget);

			HttpSession session = request.getSession();

			session.setAttribute("client", httpclient);
			HttpEntity httpEntity = response.getEntity();
			byte[] data = EntityUtils.toByteArray(httpEntity);
			if (httpEntity != null) {
				EntityUtils.consume(httpEntity);
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static CloseableHttpClient createSSLInsecureClient() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						// 信任所有
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext, new AllowAllHostnameVerifier());
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScrabData> exstractData(CloseableHttpClient httpClient,
			Map<QueryParamEnum, String> paramMap,
			Map<String, Object> customParamMap) {

		if (!checkParamValid(paramMap, customParamMap)) {
			return null;
		}
		List<ScrabData> dataList = new ArrayList<ScrabData>();
		Set<ScrabData> dataSet = new HashSet<ScrabData>();
		String url = (String) customParamMap.get(paramMap
				.get(QueryParamEnum.URL));
		Date startTime = (Date) customParamMap.get(paramMap
				.get(QueryParamEnum.START_TIME));
		Date endTime = (Date) customParamMap.get(paramMap
				.get(QueryParamEnum.END_TIME));
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		String tableAttr = (String) customParamMap
				.get(TableAttributeEnum.TABLE_ATTR.getAttr());
		String tableAttrValue = (String) customParamMap
				.get(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr());
		Map<Integer, DataNameEnum> dataIndex = (Map<Integer, DataNameEnum>) customParamMap
				.get(TableAttributeEnum.COLUMN_INDEX.getAttr());

		Date searchStartTime = startTime;
		Date searchEndTime = null;
		while (cal.getTime().compareTo(endTime) < 0) {

			if (cal.getTime().after(startTime)) {
				searchStartTime = cal.getTime();
			}
			cal.add(Calendar.DAY_OF_MONTH, MAX_QUERY_DAY);
			if (cal.getTime().after(endTime)) {
				searchEndTime = (Date) endTime.clone();
			} else {
				searchEndTime = cal.getTime();
			}
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair(paramMap
					.get(QueryParamEnum.START_TIME), DateUtil.formatDate(
					searchStartTime, DateUtil.TRADITION_PATTERN)));
			formparams.add(new BasicNameValuePair(paramMap
					.get(QueryParamEnum.END_TIME), DateUtil.formatDate(
					searchEndTime, DateUtil.TRADITION_PATTERN)));
			for (Map.Entry<String, Object> param : customParamMap.entrySet()) {
				Object obj = param.getValue();
				if (obj instanceof Date) {
				} else if (obj instanceof String) {
					String value = param.getValue().toString();
					String[] val = value.split(",");
					for (String v : val) {
						formparams
								.add(new BasicNameValuePair(param.getKey(), v));
					}
				}
			}

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
				List<ScrabData> parseList = new ArrayList<ScrabData>();

				// 解析饭游页面数据
				if (customParamMap.get("url").equals(
						CapturePlatEnum.FUNU.getQueryUrl())) {
					// json
					Type type = new TypeToken<List<FunQueryResult>>() {
					}.getType();
					List<FunQueryResult> ret = (List<FunQueryResult>) JsonParserUtils
							.parseFromJsonForList(html, type);
					parseList = convertToScrabData(ret);
				} else if (customParamMap.get("url").equals(
						CapturePlatEnum.OMS.getQueryUrl())) {// 解析破晓

					OmsQuery omsQuery = (OmsQuery) JsonParserUtils
							.parseFromJsonForObject(html, OmsQuery.class);

					List<OmsQueryResult> list = omsQuery.getRows();

					parseList = convertOmsToScrabData(list);

				} else if (customParamMap.get("url").equals(
						CapturePlatEnum.ZSJH.getQueryUrl())) {// 解析掌上极浩
					ZsjhQuery zsjhQuery = (ZsjhQuery) JsonParserUtils
							.parseFromJsonForObject(html, ZsjhQuery.class);

					List<ZsjhQueryResult> list = zsjhQuery.getList();

					parseList = convertZsjhToScrabData(list);

				} else {
					// table parse
					parseList = HtmlParserUtils.getTableDataValues(html,
							tableAttr, tableAttrValue, dataIndex);
				}
				dataSet.addAll(parseList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dataList.addAll(dataSet);
		return dataList;
	}

	/**
	 * 饭游数据解析
	 * 
	 * @param result
	 * @return
	 */
	private List<ScrabData> convertToScrabData(List<FunQueryResult> result) {
		List<ScrabData> dataList = new ArrayList<ScrabData>();
		if (result != null) {
			for (FunQueryResult r : result) {
				ScrabData data = new ScrabData();
				data.setBizDate(r.getLogDate().replace("年", "-")
						.replace("月", "-").replace("日", "-"));
				data.setProductName(r.getAppName());
				data.setBizAmount(r.getRechargeAmount());
				data.setChannelId(r.getSubChannelId());
				dataList.add(data);
			}
		}
		return dataList;
	}

	/**
	 * 破晓数据解析
	 * 
	 * @param result
	 * @return
	 */
	private List<ScrabData> convertOmsToScrabData(List<OmsQueryResult> result) {
		List<ScrabData> dataList = new ArrayList<ScrabData>();
		if (result != null) {
			for (OmsQueryResult r : result) {
				ScrabData data = new ScrabData();
				data.setBizDate(r.getRpdate());
				data.setBizAmount(r.getGame_1());
				data.setChannelId(r.getChannel_no());
				dataList.add(data);
			}
		}
		return dataList;
	}

	/**
	 * 掌上极浩数据解析
	 * 
	 * @param result
	 * @return
	 */
	private List<ScrabData> convertZsjhToScrabData(List<ZsjhQueryResult> result) {
		List<ScrabData> dataList = new ArrayList<ScrabData>();

		Map<String, String> map = new HashMap<String, String>();

		for (ZsjhQueryResult r : result) {

			if (map.containsKey(r.getTime() + r.getAppName2()) == true) {

				String bizAmount = r.getPrice().multiply(r.getPv())
						.divide(new BigDecimal("100")).toString();
				String amount = map.get(r.getTime() + r.getAppName2());
				map.put(r.getTime() + r.getAppName2(), new BigDecimal(amount)
						.add(new BigDecimal(bizAmount)).toString());

			} else {
				map.put(r.getTime() + r.getAppName2(),
						r.getPrice().multiply(r.getPv())
								.divide(new BigDecimal("100")).toString());
			}

		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			ScrabData data = new ScrabData();

			String key = entry.getKey();

			data.setBizDate(key.substring(0, 10));
			data.setProductName(key.substring(10, key.length()));
			String bizAmount = entry.getValue();
			data.setBizAmount(bizAmount);
			dataList.add(data);

		}

		return dataList;
	}

	private boolean checkParamValid(Map<QueryParamEnum, String> paramMap,
			Map<String, Object> customParamMap) {

		if (paramMap == null || paramMap.isEmpty()) {
			logger.error("paramMap is empty,please check invoke paramter first.");
			return false;
		}
		if (paramMap.get(QueryParamEnum.URL) == null) {
			logger.error("searchUrl is empty,please check invoke paramter first.");
			return false;
		}
		String startTime = (String) paramMap.get(QueryParamEnum.START_TIME);
		if (startTime == null) {
			logger.error("startTime is empty,please check invoke paramter first.");
			return false;
		}

		String endTime = (String) paramMap.get(QueryParamEnum.END_TIME);
		if (endTime == null) {
			logger.error("endTime is empty,please check invoke paramter first.");
			return false;
		}

		if (customParamMap.get(TableAttributeEnum.TABLE_ATTR.getAttr()) == null) {
			logger.error("table attribute id is empty.");
			return false;
		}

		if (customParamMap.get(TableAttributeEnum.TABLE_ATTR_VALUE.getAttr()) == null) {
			logger.error("table attribute id value is empty.");
			return false;
		}

		if (customParamMap.get(TableAttributeEnum.COLUMN_INDEX.getAttr()) == null) {
			logger.error("table column index is empty.");
			return false;
		}
		return true;
	}

	@Override
	public int saveData(List<ScrabData> dataList) {
		scrabDAO.saveData(dataList);
		return 0;
	}

	@Override
	public boolean getLoginHttpClient(CloseableHttpClient client,
			String loginUrl, String[] keys, String[] values) {
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
			if (status != HttpStatus.SC_OK
					&& status != HttpStatus.SC_MOVED_TEMPORARILY) {
				logger.error("login failed,width keys[{}],valus[{}]", keys,
						values);
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

	@Override
	public int invokeDataHander() {
		return 0;
	}

	public void setScrabDAO(IScrabDAO scrabDAO) {
		this.scrabDAO = scrabDAO;
	}

	@Override
	public String getDataQueryHtml(CloseableHttpClient httpClient,
			String queryUrl) {
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

	@Override
	public List<ScrabData> execute(CloseableHttpClient client,
			String startDate, String endDate, String imgCode) {
		return null;
	}

	@Override
	public String[] genLoginKeys() {
		return null;
	}

	@Override
	public String[] genLoginValues(String imgCode) {
		return null;
	}

	@Override
	public Map<QueryParamEnum, String> genParamMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> genCustomParamMap(String startTime,
			String endTime, String productId, String channelId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getScrabData(DataGridModel page, ScrabData form)
			throws Exception {
		return scrabDAO.selectScrabDataPageList(page, form);
	}

	@Override
	public List<ScrabData> getScrabDataList(ScrabData form) throws Exception {
		return scrabDAO.selectScrabDataList(form);
	}

	@Override
	public void modifyData(List<ScrabData> list) throws Exception {
		scrabDAO.updateScrabData(list);
	}

	@Override
	public ScrabData findScrabData(ScrabData form) throws Exception {

		return scrabDAO.selectScrabData(form);
	}

	@Override
	public ScrabData findScrabDataByBookMark(ScrabData form) throws Exception {

		return scrabDAO.selectScrabDataByBookMark(form);
	}

	@Override
	public String getChannelIdByJsonPost(CloseableHttpClient client,
			String url, Map<String, String> paramMap) {
		HttpResponse response = null;
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		if (paramMap != null) {
			for (Map.Entry<String, String> map : paramMap.entrySet()) {
				formparams.add(new BasicNameValuePair(map.getKey(), map
						.getValue()));
			}
		}
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);
			response = client.execute(httppost);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK
					&& status != HttpStatus.SC_MOVED_TEMPORARILY) {
				logger.error("get channelId failed from fun.");
				return null;
			}
			String jsonResult = EntityUtils.toString(response.getEntity());
			if (response != null) {
				response.getEntity().getContent().close();
			}
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 先打开login页面
	 * 
	 * @param loginUrl
	 *            登录地址
	 * @param client
	 *            登录客户端
	 * */
	public void loginPage(String loginUrl, CloseableHttpClient client) {
		HttpClientContext context = HttpClientContext.create();
		HttpGet httpGet = new HttpGet(loginUrl);
		try {
			client.execute(httpGet, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String postByJson(CloseableHttpClient client, String url,
			String jsonParam) {
		StringEntity entity;
		try {
			entity = new StringEntity(jsonParam);
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);
			HttpResponse response = client.execute(httppost);
			HttpEntity httpEntity = response.getEntity();
			String html = EntityUtils.toString(httpEntity);
			if (httpEntity != null) {
				EntityUtils.consume(httpEntity);
			}
			return html;
		} catch (Exception e) {
			logger.error("error in postByJson,e=[{}]", e);
		}
		return null;
	}
}
