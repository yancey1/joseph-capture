package slh.capture.dao.impl.unified;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import slh.capture.common.DataGridModel;
import slh.capture.common.unified.DistinctColumnEnum;
import slh.capture.dao.unified.ICaptureConfigDAO;
import slh.capture.domain.ScrabData;
import slh.capture.domain.unified.CaptureConfigEntity;
import edu.hziee.common.dbroute.BaseDAO;
import edu.hziee.common.lang.MapUtil;

@SuppressWarnings("unchecked")
@Service("captureConfigDAO")
public class CaptureConfigDAOImpl extends BaseDAO implements ICaptureConfigDAO {

  @Override
  public List<CaptureConfigEntity> selectCaptureConfigList(CaptureConfigEntity form) {
    Map<String, Object> map = MapUtil.objectToMap(form);

    return super.queryForList("captureConfig.SELECT_CAPTURECONFIG_LIST", map);
  }

  @Override
  public List<CaptureConfigEntity> selectCaptureConfigList(CaptureConfigEntity form, DistinctColumnEnum distinctColumnEnum) {
    Map<String, Object> map = MapUtil.objectToMap(form);
    String sql = "captureConfig.SELECT_CP_NAME_LIST";
    if (distinctColumnEnum == DistinctColumnEnum.CHANNEL_CODE) {
      sql = "captureConfig.SELECT_CHANNEL_CODE_LIST";
    } else if (distinctColumnEnum == DistinctColumnEnum.APP_NAME) {
      sql = "captureConfig.SELECT_APP_NAME_LIST";
    } else if (distinctColumnEnum == DistinctColumnEnum.USER_NAME) {
      sql = "captureConfig.SELECT_USER_NAME_LIST";
    }

    return super.queryForList(sql, map);
  }

  @Override
  public Map<String, Object> queryCaptureConfigList(DataGridModel page, CaptureConfigEntity form) {
    form = (form == null ? new CaptureConfigEntity() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("captureConfig.query_slh_count", form));
    results.put("rows", super.queryForList("captureConfig.select_slh_model_page_list", form));
    return results;
  }

  @Override
  public int saveCaptureConfig(CaptureConfigEntity form) {
    int id = (Integer) super.insert("captureConfig.insertCaptureConfig", form);
    return id;
  }

  @Override
  public CaptureConfigEntity findCaptureConfigById(CaptureConfigEntity form) {
    return (CaptureConfigEntity) super.queryForObject("captureConfig.findCaptureConfigById", form);
  }

  @Override
  public void updateCaptureConfigById(CaptureConfigEntity form) {
    super.update("captureConfig.updateCaptureConfigById", form);
  }

  @Override
  public void removeCaptureConfig(CaptureConfigEntity form) {
    super.delete("captureConfig.removeCaptureConfig", form);
  }

  @Override
  public List<CaptureConfigEntity> checkCode(CaptureConfigEntity form) {
    // TODO Auto-generated method stub
    return super.queryForList("captureConfig.checkCode",form);
  }

  @Override
  public void delete(ScrabData form) {
    super.delete("slh_scrab.deleteScr", form);
    
  }
}
