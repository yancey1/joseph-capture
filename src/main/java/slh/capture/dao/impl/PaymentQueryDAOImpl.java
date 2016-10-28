package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IPaymentQueryDAO;
import slh.capture.domain.PaymentQuery;
import edu.hziee.common.dbroute.BaseDAO;

public class PaymentQueryDAOImpl extends BaseDAO implements IPaymentQueryDAO {

  @Override
  public void batchInsertPaymentQuery(List<PaymentQuery> list) throws Exception {
    super.batchInsert("slh_paymentquery.insert_slh_paymentquery", list);
  }

  @Override
  public int deletePaymentQuery(PaymentQuery form) throws Exception {
    return super.delete("slh_paymentquery.delete_slh_paymentquery", form);
  }

  @Override
  public void batchUpdatePaymentQuery(List<PaymentQuery> form) throws Exception {
    super.batchUpdate("slh_paymentquery.update_slh_paymentquery", form);
  }

  @Override
  public PaymentQuery selectPaymentQuery(PaymentQuery form) throws Exception {
    return (PaymentQuery) super.queryForObject("slh_paymentquery.select_slh_paymentquery", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<PaymentQuery> selectPaymentQueryList(PaymentQuery form) throws Exception {
    return super.queryForList("slh_paymentquery.select_slh_paymentquery_list", form);
  }

  @Override
  public Map<String, Object> selectPaymentQueryPageList(DataGridModel page, PaymentQuery form) throws Exception {
    form = (form == null ? new PaymentQuery() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_paymentquery.select_slh_paymentquery_page_list_count", form));
    results.put("rows", super.queryForList("slh_paymentquery.select_slh_paymentquery_page_list", form));
    return results;
  }

  @Override
  public Map<String, Object> selectPaymentQuerySettlePageList(DataGridModel page, PaymentQuery form) throws Exception {
    form = (form == null ? new PaymentQuery() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_paymentquery.select_slh_paymentquery_settle_page_list_count", form));
    results.put("rows", super.queryForList("slh_paymentquery.select_slh_paymentquery_settle_page_list", form));
    return results;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<PaymentQuery> selectPaymentQueryChannelList(PaymentQuery form) throws Exception {
    return super.queryForList("slh_paymentquery.select_slh_paymentquery_channel_list", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<PaymentQuery> selectSettlePaymentQueryList(PaymentQuery form) throws Exception {
    return super.queryForList("slh_paymentquery.select_slh_paymentquery_settle_list", form);
  }

}
