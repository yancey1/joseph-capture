package slh.capture.service;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.ReciveQuery;

/**
 * 应收查询
 * 
 * @author oc_admin
 * 
 */
public interface IReciveQueryService {

  void saveReciveQueryList(List<ReciveQuery> list) throws Exception;

  int removeReciveQuery(ReciveQuery form) throws Exception;

  void modifyReciveQuery(List<ReciveQuery> form) throws Exception;

  ReciveQuery findReciveQuery(ReciveQuery form) throws Exception;

  List<ReciveQuery> findReciveQueryList(ReciveQuery form) throws Exception;

  List<ReciveQuery> findReciveQuerySettleList(ReciveQuery form) throws Exception;

  List<ReciveQuery> findSettleReciveQueryList(ReciveQuery form) throws Exception;

  Map<String, Object> findReciveQueryPageList(DataGridModel page, ReciveQuery form) throws Exception;

  Map<String, Object> findReciveQuerySettlePageList(DataGridModel page, ReciveQuery form) throws Exception;

}
