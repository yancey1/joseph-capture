package slh.capture.domain;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class JsonDyQuery {

  @SerializedName("total")
  private int          total;

  @SerializedName("rows")
  private List<JsonDyQueryResult> rows;

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public List<JsonDyQueryResult> getRows() {
    return rows;
  }

  public void setRows(List<JsonDyQueryResult> rows) {
    this.rows = rows;
  }
  

  /*public static void main(String[] args) {
    JsonWmQuery wm=new JsonWmQuery();
    wm.setAffectedRows(1);
    List<String> list=new ArrayList<String>();
    list.add("1");
    list.add("2");
    wm.setColumnNames(list);
    wm.setResultCode(3);
    wm.setResultDesc("5");
    String str[]={"2","3","4"};
    wm.setRows(str);
    List<String> list2=new ArrayList<String>();
    list2.add("111");
    list2.add("2222");
    Gson gson = new Gson();
    System.out.println(gson.toJson(wm));
  }*/
}
