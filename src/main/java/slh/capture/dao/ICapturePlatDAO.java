package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.CapturePlat;

/**
 * 抓取数据所需的基本信息
 * 
 * @author 常坤 2013-12-27
 * 
 */
public interface ICapturePlatDAO {
  /**
   * 查询分页内容
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  public Map<String, Object> selectCapturePlatPageList(DataGridModel page, CapturePlat form) throws Exception;

  /**
   * 删除
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int deleteCapturePlat(CapturePlat form) throws Exception;

  /**
   * 插入
   * 
   * @param form
   * @return
   */
  int insertCapturePlat(CapturePlat form);

  /**
   * 修改
   * 
   * @param form
   * @return
   */
  int updateCapturePlat(CapturePlat form);

  /**
   * 查询
   * 
   * @param form
   * @return
   */
  CapturePlat queryCapturePlat(CapturePlat form);

  /**
   * 查询不带分页
   * 
   * @param form
   * @return
   */
  List<CapturePlat> queryCapturePlatList(CapturePlat form);

}
