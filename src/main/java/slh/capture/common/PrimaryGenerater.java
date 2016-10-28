package slh.capture.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrimaryGenerater {

  private static PrimaryGenerater primaryGenerater = null;

  private PrimaryGenerater() {
  }

  /**
   * 取得PrimaryGenerater的单例实现
   * 
   * @return
   */
  public static PrimaryGenerater getInstance() {
    if (primaryGenerater == null) {
      synchronized (PrimaryGenerater.class) {
        if (primaryGenerater == null) {
          primaryGenerater = new PrimaryGenerater();
        }
      }
    }
    return primaryGenerater;
  }

  /**
   * 生成下一个编号
   */
  public synchronized String generaterNextNumber(String sno) {
    String id = null;
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    if (sno.equals("0")) {
      id = formatter.format(date) + "-10001";
    } else {
      DecimalFormat df = new DecimalFormat("00000");
      System.out.println(sno.substring(3, 11));
      if (sno.substring(3, 11).equals(formatter.format(date))) {
        id = formatter.format(date) + "-" + df.format(1 + Integer.parseInt(sno.substring(12, 17)));
      } else {
        id = formatter.format(date) + "-10001";
      }
    }
    return id;
  }

}
