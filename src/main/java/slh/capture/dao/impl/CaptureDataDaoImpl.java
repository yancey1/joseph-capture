package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import slh.capture.dao.ICaptureDataDao;
import slh.capture.domain.CaptureDomain;
import edu.hziee.common.dbroute.BaseDAO;

@Repository("captureDataDao")
public class CaptureDataDaoImpl extends BaseDAO implements ICaptureDataDao {

  @Override
  public Map<String, Object> getCaptureDataList(CaptureDomain domain) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("rows", super.queryForList("slh_capture.getCaptureDataList", domain));
    map.put("total", super.queryForCount("slh_capture.getCaptureDataCount", domain));
    return map;
  }

  @Override
  public void saveCaptureData(List<CaptureDomain> list) throws Exception {
    super.batchInsert("slh_capture.insertCaptureData", list);
  }

  @Override
  public int checkCaptureData(CaptureDomain domain) {
    return (Integer) super.queryForCount("slh_capture.getCaptureDataCount", domain);
  }

}
