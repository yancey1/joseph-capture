package slh.capture.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import slh.capture.domain.OptionEntity;
import slh.capture.domain.ScrabData;
import slh.capture.domain.unified.CaptureConfigEntity;
import slh.capture.form.unified.CaptureQueryConditionForm;
import edu.hziee.common.lang.DateUtil;

public class HtmlParserUtils {

  private static List<OptionEntity> getSelectOptionProperties(SelectTag sTag) {
    List<OptionEntity> optionPropertyList = new ArrayList<OptionEntity>();
    NodeList childList = sTag.getChildren();
    if (childList != null) {
      for (int x = 0; x < childList.size(); x++) {
        Node childTag = childList.elementAt(x);
        if (childTag instanceof OptionTag) {
          OptionTag optionTag = (OptionTag) childTag;
          OptionEntity optionEntity = new OptionEntity();
          optionEntity.setText(optionTag.toPlainTextString());
          optionEntity.setValue(optionTag.getAttribute("value"));
          optionPropertyList.add(optionEntity);
        }
      }
    }
    return optionPropertyList;
  }

  /**
   * 获取select组件里的value值
   * 
   * @param html
   * @param attributeName
   *          属性名
   * @param attributeValue
   *          属性值
   * @return
   */
  public static List<String> getSelectTagValues(String html, String attributeName, String attributeValue) {
    List<String> productList = new ArrayList<String>();
    Parser parser;
    try {
      parser = new Parser(html);
      NodeFilter hasAttriFilter = new HasAttributeFilter(attributeName, attributeValue);
      NodeList nodes = parser.extractAllNodesThatMatch(hasAttriFilter);
      for (int i = 0; i < nodes.size(); i++) {
        Node tag = (Node) nodes.elementAt(i);
        if (tag instanceof SelectTag) {
          SelectTag sTag = (SelectTag) tag;
          String value = sTag.getAttribute(attributeName);
          if (value.equals(attributeValue)) {
            NodeList childList = tag.getChildren();
            if (childList != null) {
              List<OptionEntity> optionList = HtmlParserUtils.getSelectOptionProperties(sTag);
              for (OptionEntity entity : optionList) {
                productList.add(entity.getValue());
              }
            }
          }
        }
      }
    } catch (ParserException e) {
      e.printStackTrace();
    }
    return productList;
  }

  /**
   * 获取input组件里的value值
   * 
   * @param html
   * @param attributeName
   *          属性名
   * @param attributeValue
   *          属性值
   * @return
   */
  public static String getInputTagValues(String html, String attributeName, String attributeValue) {
    String s = "";
    Parser parser;
    try {
      parser = new Parser(html);
      NodeFilter hasAttriFilter = new HasAttributeFilter(attributeName, attributeValue);
      NodeList nodes = parser.extractAllNodesThatMatch(hasAttriFilter);
      for (int i = 0; i < nodes.size(); i++) {
        Node tag = (Node) nodes.elementAt(i);
        if (tag instanceof InputTag) {
          InputTag iTag = (InputTag) tag;
          String value = iTag.getAttribute(attributeName);
          if (value.equals(attributeValue)) {
            s = iTag.getAttribute("value");
            break;
          }

        }
      }
    } catch (ParserException e) {
      e.printStackTrace();
    }
    return s;
  }

  /**
   * 获取input组件里的href属性值值
   * 
   */
  public static List<String> getLinkTagValues(String html) {

    List<String> list = new ArrayList<String>();
    String s = "";
    Parser parser;
    try {
      parser = new Parser(html);
      NodeList nodeList = parser.extractAllNodesThatMatch(new NodeFilter() {
        // 实现该方法,用以过滤标签
        public boolean accept(Node node) {
          if (node instanceof LinkTag)// 标记
            return true;
          return false;
        }

      });
      // 打印
      for (int i = 0; i < nodeList.size(); i++) {
        LinkTag n = (LinkTag) nodeList.elementAt(i);
        s = n.getAttribute("href");
        s = s.replace(" 00:00:00.0", "");

        list.add("https://bill.fivesky.net/bill/" + s);
      }
    } catch (ParserException e) {
      e.printStackTrace();
    }
    return list;
  }

  /**
   * 获取table中的数据
   * 
   * @param html
   *          页面结构元素
   * @return
   */
  public static List<ScrabData> getTableDataValues(String html, String attributeName, String attributeValue, Map<Integer, DataNameEnum> dataIndex) {

    List<ScrabData> scrabDataList = new ArrayList<ScrabData>();
    Parser parser = null;
    try {
      parser = new Parser(html);
      NodeFilter hasAttriFilter = new HasAttributeFilter(attributeName, attributeValue);
      NodeList nodes = parser.extractAllNodesThatMatch(hasAttriFilter);
      
      for (int i = 0; i < nodes.size(); i++) {
        Node childNode = nodes.elementAt(i);
        if (childNode instanceof Div) {// 若是div就迭代它下面的table
          nodes = childNode.getChildren();
          break;
        }
      }
      
      for (int i = 0; i < nodes.size(); i++) {
        Node childNode = nodes.elementAt(i);
        if (childNode instanceof TableTag) {
          TableTag tag = (TableTag) childNode;
          TableRow[] rows = tag.getRows();
          for (int row = 0; row < rows.length; row++) {
            TableRow tr = rows[row];
            TableColumn[] td = tr.getColumns();
            if (td.length > 1) {
              ScrabData data = new ScrabData();
              boolean isValid = true;
              for (Map.Entry<Integer, DataNameEnum> map : dataIndex.entrySet()) {
                if (map.getValue().equals(DataNameEnum.BIZ_DATE)) {
                  String bizDate = td[map.getKey()].toPlainTextString().trim();

                  if (bizDate.contains(" 00:00:00.0")) {
                    bizDate = bizDate.replace(" 00:00:00.0", "");
                  }

                  if (DateUtil.inFormat(bizDate, DateUtil.DEFAULT_PATTERN)) {
                    Date date = DateUtil.parseDate(bizDate, DateUtil.DEFAULT_PATTERN);

                    bizDate = DateUtil.formatDate(date, DateUtil.TRADITION_PATTERN);
                  }

                  Date date = DateUtil.parseDate(bizDate, DateUtil.TRADITION_PATTERN);

                  bizDate = DateUtil.formatDate(date, DateUtil.TRADITION_PATTERN);

                  data.setBizDate(bizDate);
                  if (!DateUtil.inFormat(bizDate, DateUtil.TRADITION_PATTERN)) {
                    isValid = false;
                    break;
                  }
                } else if (map.getValue().equals(DataNameEnum.PRODUCT)) {
                  String productName = td[map.getKey()].toPlainTextString().trim();
                  if (productName.startsWith("&#")) {
                    productName = decode(productName);
                  }
                  data.setProductName(productName);
                } else if (map.getValue().equals(DataNameEnum.CHANNEL_ID)) {
                  String channelId = td[map.getKey()].toPlainTextString();
                  data.setChannelId(channelId.trim());
                } else if (map.getValue().equals(DataNameEnum.BIZ_TYPE)) {
                  String bizTypeText = td[map.getKey()].toPlainTextString();
                  if (bizTypeText.toLowerCase().indexOf(BizTypeEnum.CPA.getKey()) != -1) {
                    data.setBizType(BizTypeEnum.CPA.getCode());
                  } else if (bizTypeText.toLowerCase().indexOf(BizTypeEnum.CPS.getKey()) != -1) {
                    data.setBizType(BizTypeEnum.CPS.getCode());
                  } else if (bizTypeText.toLowerCase().indexOf(BizTypeEnum.CPC.getKey()) != -1) {
                    data.setBizType(BizTypeEnum.CPC.getCode());
                  }
                } else if (map.getValue().equals(DataNameEnum.BIZ_AMOUNT)) {
                  String bizAmount = td[map.getKey()].toPlainTextString();
                  data.setBizAmount(bizAmount.trim());
                }
              }
              if (isValid)
                scrabDataList.add(data);
            }
          }
        }
      }
    } catch (ParserException e) {
      e.printStackTrace();
    }
    return scrabDataList;
  }
  
  //聚塔TABLE抓取
  public static List<ScrabData> getJtTableDataValues(String html, CaptureConfigEntity config,CaptureQueryConditionForm entity,List<CaptureConfigEntity> entityList) {
    List<ScrabData> scrabDataList = new ArrayList<ScrabData>();
    Parser parser = null;
    try {
      parser = new Parser(html);
      NodeFilter hasAttriFilter = new HasAttributeFilter(config.getTableAttr(), config.getTableAttrValue());
      NodeList nodes = parser.extractAllNodesThatMatch(hasAttriFilter);
      for (int i = 0; i < nodes.size(); i++) {
        Node childNode = nodes.elementAt(i);
        if (childNode instanceof TableTag) {
          TableTag tag = (TableTag) childNode;
          TableRow[] rows = tag.getRows();
          // 获取产品名称
          TableRow prodcutNameRow = rows[1];
          TableHeader[] th = prodcutNameRow.getHeaders();
          List<String> productNameList = new ArrayList<String>();
          for (int j = 1; j < th.length; j++) {
            productNameList.add(th[j].toPlainTextString().trim());
          }
          if (rows.length > 1) {
            for (int row = 2; row < rows.length-2; row++) {
              TableRow tr = rows[row];
              TableColumn[] td = tr.getColumns();
              // 获取日期
              String bizDate = td[0].toPlainTextString().trim();
              if(StringUtils.isNotBlank(bizDate)){
                String ary[]=bizDate.split("-");
                if(Integer.parseInt(ary[1])<10){
                  bizDate=ary[0]+"-0"+ary[1];
                }else{
                  bizDate=ary[0]+"-"+ary[1];
                }
                if(Integer.parseInt(ary[2])<10){
                  bizDate+="-0"+ary[2];
                }else{
                  bizDate+="-"+ary[2];
                }
              }
              for (int d = 0; d < productNameList.size() - 1; d++) {
                ScrabData data = new ScrabData();
                data.setBizDate(bizDate);
                String productName = productNameList.get(d);
                data.setProductName(productName);
                String bizAmount = td[d + 1].toPlainTextString();
                data.setBizAmount(bizAmount);
                data.setChannelId(entity.getChannelCode());
                for (CaptureConfigEntity bean : entityList) {
                  if(bean.getChannelCode().trim().equals(entity.getChannelCode().trim())&&productName.contains(bean.getAppName())){
                    data.setChannelId(bean.getChannelCode());
                    data.setProductName(bean.getAppName());
                    break;
                  }
                }
                data.setUserName(entity.getUserName());
                data.setBizType(config.getBizType());
                scrabDataList.add(data);
              }
            }
          }
        }
      }
    } catch (ParserException e) {
      e.printStackTrace();
    }
    return scrabDataList;
  }
  
  public static List<ScrabData> getTableZzValues(String html, String attributeName, String attributeValue, Map<Integer, DataNameEnum> dataIndex) {
    List<ScrabData> scrabDataList = new ArrayList<ScrabData>();
    Parser parser = null;
    try {
      parser = new Parser(html);
      NodeFilter hasAttriFilter = new HasAttributeFilter(attributeName, attributeValue);
      NodeList nodes = parser.extractAllNodesThatMatch(hasAttriFilter);
      for (int i = 0; i < nodes.size(); i++) {
        Node childNode = nodes.elementAt(i);
        if (childNode instanceof Div) {// 若是div就迭代它下面的table
          nodes = childNode.getChildren();
          break;
        }
      }
      for (int i = 0; i < nodes.size(); i++) {
        Node childNode = nodes.elementAt(i);
        if (childNode instanceof TableTag) {
          TableTag tag = (TableTag) childNode;
          TableRow[] rows = tag.getRows();
          for (int row = 0; row < rows.length-1; row++) {
            TableRow tr = rows[row];
            TableColumn[] td = tr.getColumns();
            if (td.length > 1) {
              ScrabData data = new ScrabData();
              boolean isValid = true;
              for (Map.Entry<Integer, DataNameEnum> map : dataIndex.entrySet()) {
                if (map.getValue().equals(DataNameEnum.BIZ_DATE)) {
                  String bizDate = td[map.getKey()].toPlainTextString().trim();
                  if (bizDate.contains(" 00:00:00.0")) {
                    bizDate = bizDate.replace(" 00:00:00.0", "");
                  }
                  if (DateUtil.inFormat(bizDate, DateUtil.DEFAULT_PATTERN)) {
                    Date date = DateUtil.parseDate(bizDate, DateUtil.DEFAULT_PATTERN);
                    bizDate = DateUtil.formatDate(date, DateUtil.TRADITION_PATTERN);
                  }
                  Date date = DateUtil.parseDate(bizDate, DateUtil.TRADITION_PATTERN);
                  bizDate = DateUtil.formatDate(date, DateUtil.TRADITION_PATTERN);
                  data.setBizDate(bizDate);
                  if (!DateUtil.inFormat(bizDate, DateUtil.TRADITION_PATTERN)) {
                    isValid = false;
                    break;
                  }
                } else if (map.getValue().equals(DataNameEnum.PRODUCT)) {
                  String productName = td[map.getKey()].toPlainTextString().trim();
                  if (productName.startsWith("&#")) {
                    productName = decode(productName);
                  }
                  data.setProductName(productName);
                } else if (map.getValue().equals(DataNameEnum.CHANNEL_ID)) {
                  String channelId = td[map.getKey()].toPlainTextString();
                  data.setChannelId(channelId.trim());
                } else if (map.getValue().equals(DataNameEnum.BIZ_TYPE)) {
                  String bizTypeText = td[map.getKey()].toPlainTextString();
                  if (bizTypeText.toLowerCase().indexOf(BizTypeEnum.CPA.getKey()) != -1) {
                    data.setBizType(BizTypeEnum.CPA.getCode());
                  } else if (bizTypeText.toLowerCase().indexOf(BizTypeEnum.CPS.getKey()) != -1) {
                    data.setBizType(BizTypeEnum.CPS.getCode());
                  } else if (bizTypeText.toLowerCase().indexOf(BizTypeEnum.CPC.getKey()) != -1) {
                    data.setBizType(BizTypeEnum.CPC.getCode());
                  }
                } else if (map.getValue().equals(DataNameEnum.BIZ_AMOUNT)) {
                  String bizAmount = td[map.getKey()].toPlainTextString();
                  data.setBizAmount(bizAmount.trim());
                }
              }
              if (isValid)
                scrabDataList.add(data);
            }
          }
        }
      }
    } catch (ParserException e) {
      e.printStackTrace();
    }
    return scrabDataList;
  }
  
  
  public static Parser createParser(String inputHTML) {
    Lexer mLexer = new Lexer(new Page(inputHTML));
    return new Parser(mLexer, new DefaultParserFeedback(DefaultParserFeedback.QUIET));
  }

  /**
   * 获取星华晨table中的数据
   * 
   * @param html
   *          页面结构元素
   * @return
   */
  public static List<ScrabData> getXhcTableDataValues(String html, String attributeName, String attributeValue, Map<Integer, DataNameEnum> dataIndex) {

    List<ScrabData> scrabDataList = new ArrayList<ScrabData>();
    Parser parser = null;
    try {
      parser = createParser(html);
      NodeFilter hasAttriFilter = new HasAttributeFilter(attributeName, attributeValue);
      NodeList nodes = parser.extractAllNodesThatMatch(hasAttriFilter);
      for (int i = 0; i < nodes.size(); i++) {
        Node childNode = nodes.elementAt(i);
        if (childNode instanceof TableTag) {
          TableTag tag = (TableTag) childNode;
          TableRow[] rows = tag.getRows();
          for (int row = 0; row < rows.length; row++) {
            TableRow tr = rows[row];
            TableColumn[] td = tr.getColumns();
            if (td.length > 1) {
              ScrabData data = new ScrabData();
              boolean isValid = true;
              for (Map.Entry<Integer, DataNameEnum> map : dataIndex.entrySet()) {
                if (map.getValue().equals(DataNameEnum.BIZ_DATE)) {
                  String bizDate = td[map.getKey()].toPlainTextString().trim();
                  data.setBizDate(bizDate);
                  if (!DateUtil.inFormat(bizDate, DateUtil.TRADITION_PATTERN)) {
                    isValid = false;
                    break;
                  }
                } else if (map.getValue().equals(DataNameEnum.PRODUCT)) {
                  String productName = td[map.getKey()].toPlainTextString().trim();
                  if (productName.startsWith("&#")) {
                    productName = decode(productName);
                  }
                  data.setProductName(productName);
                } else if (map.getValue().equals(DataNameEnum.CHANNEL_ID)) {
                  String channelId = td[map.getKey()].toPlainTextString();
                  data.setChannelId(channelId.trim());
                } else if (map.getValue().equals(DataNameEnum.BIZ_TYPE)) {
                  String bizTypeText = td[map.getKey()].toPlainTextString();
                  if (bizTypeText.toLowerCase().indexOf(BizTypeEnum.CPA.getKey()) != -1) {
                    data.setBizType(BizTypeEnum.CPA.getCode());
                  } else if (bizTypeText.toLowerCase().indexOf(BizTypeEnum.CPS.getKey()) != -1) {
                    data.setBizType(BizTypeEnum.CPS.getCode());
                  } else if (bizTypeText.toLowerCase().indexOf(BizTypeEnum.CPC.getKey()) != -1) {
                    data.setBizType(BizTypeEnum.CPC.getCode());
                  }
                } else if (map.getValue().equals(DataNameEnum.BIZ_AMOUNT)) {
                  String bizAmount = td[map.getKey()].toPlainTextString();
                  data.setBizAmount(bizAmount.trim());
                }
              }
              if (isValid)
                scrabDataList.add(data);
            }
          }
        }
      }
    } catch (ParserException e) {
      e.printStackTrace();
    }
    return scrabDataList;
  }

  /**
   * 获取古川table中的数据
   * 
   * @param html
   *          页面结构元素
   * @return
   */
  public static List<ScrabData> getGcTableDataValues(String html, String attributeName, String attributeValue,String year) {

    List<ScrabData> scrabDataList = new ArrayList<ScrabData>();
    Parser parser = null;
    try {
      parser = new Parser(html);
      NodeFilter hasAttriFilter = new HasAttributeFilter(attributeName, attributeValue);
      NodeList nodes = parser.extractAllNodesThatMatch(hasAttriFilter);
      for (int i = 0; i < nodes.size(); i++) {
        Node childNode = nodes.elementAt(i);
        if (childNode instanceof TableTag) {
          TableTag tag = (TableTag) childNode;
          TableRow[] rows = tag.getRows();

          // 获取产品名称
          TableRow prodcutNameRow = rows[1];
          TableHeader[] th = prodcutNameRow.getHeaders();
          List<String> productNameList = new ArrayList<String>();
          for (int j = 1; j < th.length; j++) {
            productNameList.add(th[j].toPlainTextString().trim());
          }
          if (rows.length > 5) {
            for (int row = 2; row < rows.length - 3; row++) {

              TableRow tr = rows[row];
              TableColumn[] td = tr.getColumns();

              // 获取日期
              String bizDate = td[0].toPlainTextString().trim();

              int a = 0;

              for (int index = 0; index < bizDate.length(); index++) {

                String w = bizDate.substring(index, index + 1);
                // 而java采用unicode编码，汉字的范围是 "\u4e00"（一）到"\u9fa5"（龥）
                if (w.compareTo("\u4e00") > 0 && w.compareTo("\u9fa5") < 0) {
                  a = index;
                  break;
                }
              }

              //Calendar cal = Calendar.getInstance();
              //int year = cal.get(Calendar.YEAR);
              int month = Integer.valueOf(bizDate.substring(0, a));

              if (month < 10) {
                bizDate = year + "-0" + month + "-" + bizDate.substring(a + 1);
              } else {
                bizDate = year + "-" + month + "-" + bizDate.substring(a + 1);
              }

              for (int d = 0; d < productNameList.size() - 1; d++) {
                ScrabData data = new ScrabData();

                data.setBizDate(bizDate);

                String productName = productNameList.get(d);
                data.setProductName(productName);

                String bizAmount = td[d + 1].toPlainTextString();

                data.setBizAmount(bizAmount);
                scrabDataList.add(data);
              }

            }

          }
        }
      }

    } catch (ParserException e) {
      e.printStackTrace();
    }
    return scrabDataList;

  }
  private static String decode(String str) {
    String[] tmp = str.split(";&#|&#|;");
    StringBuffer sb = new StringBuffer("");
    for (int i = 0; i < tmp.length; i++) {
      if (tmp[i].matches("\\d{5}")) {
        sb.append((char) Integer.parseInt(tmp[i]));
      } else {
        sb.append(tmp[i]);
      }
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    String string = "2014-1-15";
    Date date = DateUtil.parseDate(string, DateUtil.TRADITION_PATTERN);

    System.out.println(DateUtil.formatDate(date, DateUtil.TRADITION_PATTERN));

  }

}
