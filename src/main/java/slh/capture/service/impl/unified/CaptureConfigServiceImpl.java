package slh.capture.service.impl.unified;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.DataGridModel;
import slh.capture.common.unified.DistinctColumnEnum;
import slh.capture.dao.unified.ICaptureConfigDAO;
import slh.capture.domain.unified.CaptureConfigEntity;
import slh.capture.service.unified.ICaptureConfigService;

@Service("captureConfigService")
public class CaptureConfigServiceImpl implements ICaptureConfigService {


  @Autowired
  private ICaptureConfigDAO captureConfigDAO;

  @Override
  public List<CaptureConfigEntity> findCaptureConfigList(CaptureConfigEntity form) {
    return captureConfigDAO.selectCaptureConfigList(form);
  }
  @Override
  public List<CaptureConfigEntity> findAppList(CaptureConfigEntity form) {
    return this.findCaptureConfigList(form, DistinctColumnEnum.APP_NAME);
  }

  @Override
  public List<CaptureConfigEntity> findChannelList(CaptureConfigEntity form) {
    return this.findCaptureConfigList(form, DistinctColumnEnum.CHANNEL_CODE);
  }

  @Override
  public List<CaptureConfigEntity> findCpList(CaptureConfigEntity form) {
    return this.findCaptureConfigList(form, DistinctColumnEnum.CP_NAME);
  }

  @Override
  public List<CaptureConfigEntity> findUserList(CaptureConfigEntity form) {
    return this.findCaptureConfigList(form, DistinctColumnEnum.USER_NAME);
  }
  
  @Override
  public List<CaptureConfigEntity> findCaptureConfigList(CaptureConfigEntity form, DistinctColumnEnum distinctColumnEnum) {
    return captureConfigDAO.selectCaptureConfigList(form, distinctColumnEnum);
  }
  
  @Override
  public Map<String, Object> queryCaptureConfigList(DataGridModel page,CaptureConfigEntity form) {
    return captureConfigDAO.queryCaptureConfigList(page,form);
  }
  @Override
  public void saveCaptureConfig(CaptureConfigEntity form) {
    captureConfigDAO.saveCaptureConfig(form);
    
  }
  @Override
  public CaptureConfigEntity findCaptureConfigById(CaptureConfigEntity form) {
    return captureConfigDAO.findCaptureConfigById(form);
  }
  @Override
  public void updateCaptureConfigById(CaptureConfigEntity form) {
    captureConfigDAO.updateCaptureConfigById(form);
    
  }
  @Override
  public void removeCaptureConfig(CaptureConfigEntity form) {
    captureConfigDAO.removeCaptureConfig(form);
  }
  @Override
  public List<CaptureConfigEntity> checkCode(CaptureConfigEntity form) {
    return captureConfigDAO.checkCode(form);
  }
}
