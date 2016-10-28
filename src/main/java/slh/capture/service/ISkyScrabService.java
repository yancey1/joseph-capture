package slh.capture.service;

import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;

import slh.capture.domain.ScrabData;

public interface ISkyScrabService extends IScrabService {

  public List<ScrabData> execute(CloseableHttpClient client, String month, String randCode);
}
