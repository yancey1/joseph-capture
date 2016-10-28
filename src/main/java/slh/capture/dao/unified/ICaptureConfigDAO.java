package slh.capture.dao.unified;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.common.unified.DistinctColumnEnum;
import slh.capture.domain.ScrabData;
import slh.capture.domain.unified.CaptureConfigEntity;

/**
 * 通用抓取查询条件DAO
 * 
 * @author xuwenqiang
 * */
public interface ICaptureConfigDAO {

  /**
   * 查询条件列表
   * 
   * @param form
   * 
   * @return List<QueryConditionEntity>
   * */
  public List<CaptureConfigEntity> selectCaptureConfigList(CaptureConfigEntity form);

  /**
   * 查询条件列表
   * 
   * @param form
   * 
   * @return List<QueryConditionEntity>
   * */
  public List<CaptureConfigEntity> selectCaptureConfigList(CaptureConfigEntity form, DistinctColumnEnum distinctColumnEnum);

  /**
   * 查询分页内容
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  public Map<String, Object> queryCaptureConfigList(DataGridModel page, CaptureConfigEntity form);
  
  
  /**
   * 保存
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public int saveCaptureConfig(CaptureConfigEntity form);

  /**
   * 根据主键ID查找对象 保存
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public CaptureConfigEntity findCaptureConfigById(CaptureConfigEntity form);

  /**
   * 修改
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public void updateCaptureConfigById(CaptureConfigEntity form);

  /**
   * 删除
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public void removeCaptureConfig(CaptureConfigEntity form);
  
  /**
   * 检查是否需要验证码
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<CaptureConfigEntity> checkCode(CaptureConfigEntity form);
  
  /**
   * 删除数据
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public void delete(ScrabData form);
}
