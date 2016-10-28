package slh.capture.domain;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class JsonWmQuery {

  @SerializedName("resultCode")
  private int          resultCode;

  @SerializedName("resultDesc")
  private String       resultDesc;

  @SerializedName("affectedRows")
  private int          affectedRows;

  @SerializedName("columnNames")
  private List<String> columnNames;

  @SerializedName("rows")
  private List<String> rows;

  public int getResultCode() {
    return resultCode;
  }

  public void setResultCode(int resultCode) {
    this.resultCode = resultCode;
  }

  public String getResultDesc() {
    return resultDesc;
  }

  public void setResultDesc(String resultDesc) {
    this.resultDesc = resultDesc;
  }

  public int getAffectedRows() {
    return affectedRows;
  }

  public void setAffectedRows(int affectedRows) {
    this.affectedRows = affectedRows;
  }

  public List<String> getColumnNames() {
    return columnNames;
  }

  public void setColumnNames(List<String> columnNames) {
    this.columnNames = columnNames;
  }

  public List<String> getRows() {
    return rows;
  }

  public void setRows(List<String> rows) {
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
