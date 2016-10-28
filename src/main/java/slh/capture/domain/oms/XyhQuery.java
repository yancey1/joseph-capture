package slh.capture.domain.oms;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class XyhQuery {
  @SerializedName("cpid")
  private String               cpid;

  @SerializedName("currentpage")
  private String currentpage;

  @SerializedName("pagesize")
  private String pagesize;

  @SerializedName("result")
  private String result;
  
  @SerializedName("rows")
  private List<XyhQueryResult> rows;
  
  @SerializedName("totalrows")
  private String totalrows;
  
  public String getTotalrows() {
    return totalrows;
  }

  public void setTotalrows(String totalrows) {
    this.totalrows = totalrows;
  }

  public String getCpid() {
    return cpid;
  }

  public void setCpid(String cpid) {
    this.cpid = cpid;
  }

  public String getCurrentpage() {
    return currentpage;
  }

  public void setCurrentpage(String currentpage) {
    this.currentpage = currentpage;
  }

  public String getPagesize() {
    return pagesize;
  }

  public void setPagesize(String pagesize) {
    this.pagesize = pagesize;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public List<XyhQueryResult> getRows() {
    return rows;
  }

  public void setRows(List<XyhQueryResult> rows) {
    this.rows = rows;
  }

}
