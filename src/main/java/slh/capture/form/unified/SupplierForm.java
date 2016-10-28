package slh.capture.form.unified;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import slh.capture.annotation.ExportExcelAnnotation;
import slh.capture.common.CustomDateTimeSerializer;

/**
 * 内容供应商
 * 
 * @author ck
 * 
 */
public class SupplierForm {

  @ExportExcelAnnotation(index = 0)
  private String  company;        // CP名称

  @ExportExcelAnnotation(index = 1)
  private String  companyAllName; // CP全称

  @ExportExcelAnnotation(index = 2)
  private String  linkMan;        // TP商务

  @ExportExcelAnnotation(index = 3)
  private String  qq;             // 商务QQ

  @ExportExcelAnnotation(index = 4)
  private String  emaill;         // 商务邮箱

  @ExportExcelAnnotation(index = 5)
  private String  phone;          // 商务电话

  @ExportExcelAnnotation(index = 6)
  private String  address;        // 商务地址

  @ExportExcelAnnotation(index = 7)
  private String  financeMan;     // CP财务

  @ExportExcelAnnotation(index = 8)
  private String  financePhone;   // 财务电话

  @ExportExcelAnnotation(index = 9)
  private String  accountName;    // 开户名

  @ExportExcelAnnotation(index = 10)
  private String  accountNumber;  // 开户账号

  @ExportExcelAnnotation(index = 11)
  private String  bankName;       // 开户行名称

  @ExportExcelAnnotation(index = 12)
  private String  invoiceTypeName;
  private Integer invoiceType;    // 发票类型

  @ExportExcelAnnotation(index = 13)
  private String  dutyParagraph;  // 税号

  @ExportExcelAnnotation(index = 14)
  private String  financeAddress; // 账单邮寄地址

  @ExportExcelAnnotation(index = 15)
  private Date    createTime;     // 创建时间
  private Integer supplierId;
  private Date    modifyTime;

  public String getCompanyAllName() {
    return companyAllName;
  }
  public void setCompanyAllName(String companyAllName) {
    this.companyAllName = companyAllName;
  }
  public String getFinanceAddress() {
    return financeAddress;
  }
  public void setFinanceAddress(String financeAddress) {
    this.financeAddress = financeAddress;
  }
  public Date getModifyTime() {
    return modifyTime;
  }
  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }
  public String getCompany() {
    return company;
  }
  public void setCompany(String company) {
    this.company = company;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }

  public String getQq() {
    return qq;
  }
  public void setQq(String qq) {
    this.qq = qq;
  }
  public String getEmaill() {
    return emaill;
  }
  public void setEmaill(String emaill) {
    this.emaill = emaill;
  }

  public String getLinkMan() {
    return linkMan;
  }
  public void setLinkMan(String linkMan) {
    this.linkMan = linkMan;
  }
  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }
  public String getFinanceMan() {
    return financeMan;
  }
  public void setFinanceMan(String financeMan) {
    this.financeMan = financeMan;
  }
  public String getFinancePhone() {
    return financePhone;
  }
  public void setFinancePhone(String financePhone) {
    this.financePhone = financePhone;
  }
  public String getAccountName() {
    return accountName;
  }
  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }
  public String getAccountNumber() {
    return accountNumber;
  }
  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }
  public String getBankName() {
    return bankName;
  }
  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getInvoiceTypeName() {
    return invoiceTypeName;
  }
  public void setInvoiceTypeName(String invoiceTypeName) {
    this.invoiceTypeName = invoiceTypeName;
  }
  public Integer getInvoiceType() {
    return invoiceType;
  }
  public void setInvoiceType(Integer invoiceType) {
    if (invoiceType != null) {
      if (invoiceType == 0) {
        setInvoiceTypeName("增值税普通发票");
      } else if (invoiceType == 1) {
        setInvoiceTypeName("增值税专用发票");
      } else if (invoiceType == 2) {
        setInvoiceTypeName("对私结算");
      }
    }
    this.invoiceType = invoiceType;
  }
  public String getDutyParagraph() {
    return dutyParagraph;
  }
  public void setDutyParagraph(String dutyParagraph) {
    this.dutyParagraph = dutyParagraph;
  }

  @JsonSerialize(using = CustomDateTimeSerializer.class)
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  public Integer getSupplierId() {
    return supplierId;
  }
  public void setSupplierId(Integer supplierId) {
    this.supplierId = supplierId;
  }

}
