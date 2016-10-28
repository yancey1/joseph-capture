package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.PaymentQuery;

public interface IPaymentQueryDAO {

  /**
   * 保存应付查询
   * 
   * @param form
   * @return
   * @throws Exception
   */
  void batchInsertPaymentQuery(List<PaymentQuery> list) throws Exception;

  /**
   * 删除应付查询信息
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int deletePaymentQuery(PaymentQuery form) throws Exception;

  /**
   * 编辑应付查询
   * 
   * @param form
   * @return
   * @throws Exception
   */
  void batchUpdatePaymentQuery(List<PaymentQuery> form) throws Exception;

  /**
   * 获取指定应付查询
   * 
   * @param form
   * @return
   * @throws Exception
   */
  PaymentQuery selectPaymentQuery(PaymentQuery form) throws Exception;

  /**
   * 不带分页的应付查询列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<PaymentQuery> selectPaymentQueryList(PaymentQuery form) throws Exception;

  /**
   * 根据渠道ID（cmId）查询不带分页的应付查询列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<PaymentQuery> selectPaymentQueryChannelList(PaymentQuery form) throws Exception;

  /**
   * 带分页的应付查询列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  Map<String, Object> selectPaymentQueryPageList(DataGridModel page, PaymentQuery form) throws Exception;

  /**
   * 结算单详情列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  Map<String, Object> selectPaymentQuerySettlePageList(DataGridModel page, PaymentQuery form) throws Exception;

  /**
   * 导出结算单详情列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<PaymentQuery> selectSettlePaymentQueryList(PaymentQuery form) throws Exception;

}
