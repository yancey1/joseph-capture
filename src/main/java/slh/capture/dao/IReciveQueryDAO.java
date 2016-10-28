package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.ReciveQuery;

public interface IReciveQueryDAO {

  /**
   * 保存应收查询
   * 
   * @param form
   * @return
   * @throws Exception
   */
  void batchInsertReciveQuery(List<ReciveQuery> list) throws Exception;

  /**
   * 删除应收查询信息
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int deleteReciveQuery(ReciveQuery form) throws Exception;

  /**
   * 编辑应收查询
   * 
   * @param form
   * @return
   * @throws Exception
   */
  void batchUpdateReciveQuery(List<ReciveQuery> form) throws Exception;

  /**
   * 获取指定应收查询
   * 
   * @param form
   * @return
   * @throws Exception
   */
  ReciveQuery selectReciveQuery(ReciveQuery form) throws Exception;

  /**
   * 不带分页的应收查询列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<ReciveQuery> selectReciveQueryList(ReciveQuery form) throws Exception;

  /**
   * 不带分页的应收查询列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<ReciveQuery> selectReciveQuerySettleList(ReciveQuery form) throws Exception;

  /**
   * 带分页的应收查询列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  Map<String, Object> selectReciveQueryPageList(DataGridModel page, ReciveQuery form) throws Exception;

  /**
   * 带分页的结算单详情查询列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  Map<String, Object> selectReciveQuerySettlePageList(DataGridModel page, ReciveQuery form) throws Exception;

  /**
   * 导出结算单详情查询列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<ReciveQuery> selectSettleReciveQueryList(ReciveQuery form) throws Exception;

}
