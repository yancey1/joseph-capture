package slh.capture.service;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.PaymentQuery;

/**
 * 应收查询
 * 
 * @author oc_admin
 * 
 */
public interface IPaymentQueryService {

  void savePaymentQueryList(List<PaymentQuery> list) throws Exception;

  int removePaymentQuery(PaymentQuery form) throws Exception;

  void modifyPaymentQuery(List<PaymentQuery> ls) throws Exception;

  PaymentQuery findPaymentQuery(PaymentQuery form) throws Exception;

  List<PaymentQuery> findPaymentQueryList(PaymentQuery form) throws Exception;

  List<PaymentQuery> findPaymentQueryChannelList(PaymentQuery form) throws Exception;

  List<PaymentQuery> findSettlePaymentQueryList(PaymentQuery form) throws Exception;

  Map<String, Object> findPaymentQueryPageList(DataGridModel page, PaymentQuery form) throws Exception;

  Map<String, Object> findPaymentQuerySettlePageList(DataGridModel page, PaymentQuery form) throws Exception;

}
