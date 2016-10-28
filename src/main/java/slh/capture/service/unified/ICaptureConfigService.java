package slh.capture.service.unified;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.common.unified.DistinctColumnEnum;
import slh.capture.domain.unified.CaptureConfigEntity;

/**
 * 抓取配置接口
 * 
 * @author xuwenqiang
 * */
public interface ICaptureConfigService {

  /**
   * 获取查询数据列表
   * 
   * */
  public List<CaptureConfigEntity> findCaptureConfigList(CaptureConfigEntity form);

  /**
   * 获取应用列表
   * 
   * @param form
   * 
   * @param return List<QueryConditionEntity>
   * */
  public List<CaptureConfigEntity> findAppList(CaptureConfigEntity form);

  /**
   * 获取渠道列表
   * 
   * @param form
   * 
   * @param return List<QueryConditionEntity>
   * */
  public List<CaptureConfigEntity> findChannelList(CaptureConfigEntity form);

  /**
   * 获取CP列表
   * 
   * @param form
   * 
   * @param return List<QueryConditionEntity>
   * */
  public List<CaptureConfigEntity> findCpList(CaptureConfigEntity form);

  /**
   * 获取CP列表
   * 
   * @param form
   * 
   * @param return List<QueryConditionEntity>
   * */
  public List<CaptureConfigEntity> findUserList(CaptureConfigEntity form);

  /**
   * 根据排重字段获取查询数据列表
   * 
   * @param form
   * @param distinctColumnEnum
   *          排重的字段
   * 
   * */
  public List<CaptureConfigEntity> findCaptureConfigList(CaptureConfigEntity form, DistinctColumnEnum distinctColumnEnum);

  /**
   * 获取查询数据列表分页
   * 
   * */
  public Map<String, Object> queryCaptureConfigList(DataGridModel page, CaptureConfigEntity form);

  /**
   * 保存配置
   * 
   * */
  public void saveCaptureConfig(CaptureConfigEntity form);

  /**
   * 根据主键ID查找对象
   * 
   * */
  public CaptureConfigEntity findCaptureConfigById(CaptureConfigEntity form);

  /**
   * 修改
   * 
   * */
  public void updateCaptureConfigById(CaptureConfigEntity form);

  /**
   * 删除
   * 
   * */
  public void removeCaptureConfig(CaptureConfigEntity form);
  
  /**
   * 检查是否需要验证码
   * 
   * */
  public List<CaptureConfigEntity> checkCode(CaptureConfigEntity form);
  
}
