package slh.capture.common;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpFactory {

  /**
   * 创建一个httpClient
   * @return
   */
  public static CloseableHttpClient createHttpClient() {

    HttpClientBuilder clientBuilder = HttpClientBuilder.create();
    RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000)
        .setStaleConnectionCheckEnabled(true).build();
    clientBuilder.setDefaultRequestConfig(defaultRequestConfig);
    CloseableHttpClient httpClient = clientBuilder.build();
    return httpClient;
  }
}
