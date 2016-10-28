package slh.capture.common;

public enum TableAttributeEnum {

  TABLE_ATTR("table_attr", "表格ID"), TABLE_ATTR_VALUE("table_attr_value", "表格ID值"), COLUMN_INDEX("column_index", "索引位");

  private String attr;
  private String desc;

  TableAttributeEnum(String attr, String desc) {
    this.attr = attr;
    this.desc = desc;
  }

  public String getAttr() {
    return attr;
  }

  public void setAttr(String attr) {
    this.attr = attr;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

}
