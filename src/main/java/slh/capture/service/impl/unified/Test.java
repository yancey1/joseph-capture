package slh.capture.service.impl.unified;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Test {

  public static void main(String[] args) {

    ClientConnectionManager cm = new MyBasicClientConnectionManager();
    DefaultHttpClient httpclient = new DefaultHttpClient(cm);
    try {
      HttpGet httpget = new HttpGet("http://115.236.43.210:2013/impartner/login?username=shouxin&password=123456");
      HttpResponse response = httpclient.execute(httpget);

      HttpEntity httpEntity = response.getEntity();
      String data = EntityUtils.toString(httpEntity);
      System.out.println(data);
      HttpPost httppost = new HttpPost("http://115.236.43.210:2013/impartner/Query?appid=&partner=&beginday=2014-09-01&endday=2014-09-19");
      response = httpclient.execute(httppost);
      httpEntity = response.getEntity();
      String html = EntityUtils.toString(response.getEntity());
      //{'totalCount':19,'root':[{"account_date":"","account_value":0,"add_user_num":0,"appId":"","app_id":"","app_name":"B_æé±¼","apru":"","arpu":"6.82758620689655172413793103448275862069E00","channel_name":"S_ä¸æµ·æ¬§è¾°","expdate":"20141001","hz_name":"600043003940448","id":"","incomePer":0,"incomefee":"198","lincomefee":198,"lotherfee":0,"mote_hz":"","new_user_num":"","otherfee":"0","payId":"","pay_type":"","price":"","project_id":"","provder":"","provderId":"","provinceName":"","proxyPer":0,"proxy_money":"","share_money":"","share_user_per":0,"spreadPer":0,"spread_money":"","spread_monry":"","totalfee":198,"usernum":"29"}
      System.out.println(html);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    }
  }
}
