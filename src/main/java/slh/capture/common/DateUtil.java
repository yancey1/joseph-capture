package slh.capture.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

  /**
   * 获得距离指定日期n天的那一天的日期 create date:2009-6-11 author:Administrator
   * 
   * @param date
   *          格式：2009-6-11
   * @param day
   * @return
   */
  public static String getDistanceDay(String date, int day) {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date d;
    Calendar c = Calendar.getInstance();
    try {
      d = dateFormat.parse(date);
      c.setTime(d);
      c.add(Calendar.DATE, day);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return dateFormat.format(c.getTime());
  }

  public static void main(String[] args) {
    String s = "2013-12-25";

    String date = getDistanceDay(s, 2);

    System.out.println(date);

  }

}
