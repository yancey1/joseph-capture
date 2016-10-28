package slh.capture.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IPaymentQueryDAO;
import slh.capture.domain.PaymentQuery;
import slh.capture.service.IPaymentQueryService;
import edu.hziee.common.dbroute.BaseDAO;

@Service("paymentQueryService")
public class PaymentQueryServiceImpl extends BaseDAO implements IPaymentQueryService {
  @Autowired
  private IPaymentQueryDAO paymentQueryDAO;

  @Override
  public void savePaymentQueryList(List<PaymentQuery> list) throws Exception {
    paymentQueryDAO.batchInsertPaymentQuery(list);
  }

  @Override
  public int removePaymentQuery(PaymentQuery form) throws Exception {
    return paymentQueryDAO.deletePaymentQuery(form);
  }

  @Override
  public void modifyPaymentQuery(List<PaymentQuery> list) throws Exception {
    paymentQueryDAO.batchUpdatePaymentQuery(list);

  }

  @Override
  public PaymentQuery findPaymentQuery(PaymentQuery form) throws Exception {
    return paymentQueryDAO.selectPaymentQuery(form);
  }

  @Override
  public List<PaymentQuery> findPaymentQueryList(PaymentQuery form) throws Exception {
    return paymentQueryDAO.selectPaymentQueryList(form);
  }

  @Override
  public Map<String, Object> findPaymentQueryPageList(DataGridModel page, PaymentQuery form) throws Exception {
    return paymentQueryDAO.selectPaymentQueryPageList(page, form);
  }

  @Override
  public Map<String, Object> findPaymentQuerySettlePageList(DataGridModel page, PaymentQuery form) throws Exception {
    return paymentQueryDAO.selectPaymentQuerySettlePageList(page, form);
  }

  @Override
  public List<PaymentQuery> findPaymentQueryChannelList(PaymentQuery form) throws Exception {

    return paymentQueryDAO.selectPaymentQueryChannelList(form);
  }

  @Override
  public List<PaymentQuery> findSettlePaymentQueryList(PaymentQuery form) throws Exception {

    return paymentQueryDAO.selectSettlePaymentQueryList(form);
  }

}
