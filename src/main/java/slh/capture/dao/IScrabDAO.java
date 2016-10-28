/**
 * 保存数据抓取
 */
package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.ScrabData;

public interface IScrabDAO {

  /**
   * 保存抓取的数据
   * 
   * @param dataList
   * @return
   */
  public void saveData(List<ScrabData> dataList);

  /**
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public ScrabData selectScrabData(ScrabData form) throws Exception;

  /**
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public ScrabData selectScrabDataByBookMark(ScrabData form) throws Exception;

  /**
   * 修改抓取数据
   * 
   * @param form
   * @throws Exception
   */
  public void updateScrabData(List<ScrabData> list) throws Exception;

  /**
   * 不带分页的抓取数据列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  List<ScrabData> selectScrabDataList(ScrabData form) throws Exception;
  /**
   * 带分页的抓取数据列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  Map<String, Object> selectScrabDataPageList(DataGridModel page, ScrabData form) throws Exception;

}
