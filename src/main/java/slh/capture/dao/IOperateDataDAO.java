package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.OperateData;

public interface IOperateDataDAO {
  /**
   * 保存经营数据查询
   * 
   * @param form
   * @return
   * @throws Exception
   */
  void batchInsertOperateData(List<OperateData> list) throws Exception;

  /**
   * 获取指定经营数据查询
   * 
   * @param form
   * @return
   * @throws Exception
   */
  OperateData selectOperateData(OperateData form) throws Exception;

  /**
   * 不带分页的经营数据查询列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<OperateData> selectOperateDataList(OperateData form) throws Exception;

  /**
   * 带分页的经营数据查询列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  Map<String, Object> selectOperateDataPageList(DataGridModel page, OperateData form) throws Exception;

  /**
   * 删除经营数据
   * 
   * @param form
   * @throws Exception
   */
  void deleteOperateData(OperateData form) throws Exception;

}
