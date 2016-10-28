package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import edu.hziee.common.dbroute.BaseDAO;

public class CapturePlatDAOImpl extends BaseDAO implements ICapturePlatDAO {

  @Override
  public Map<String, Object> selectCapturePlatPageList(DataGridModel page, CapturePlat form) throws Exception {
    form = (form == null ? new CapturePlat() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_capture_plat.query_slh_count", form));
    results.put("rows", super.queryForList("slh_capture_plat.select_slh_model_page_list", form));
    return results;
  }

  @Override
  public int insertCapturePlat(CapturePlat form) {
    int id = (Integer) super.insert("slh_capture_plat.insert_slh_model", form);
    return id;
  }

  @Override
  public int updateCapturePlat(CapturePlat form) {
    return super.update("slh_capture_plat.update_slh_model", form);
  }

  @Override
  public CapturePlat queryCapturePlat(CapturePlat form) {
    return (CapturePlat) super.queryForObject("slh_capture_plat.select_slh_model", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CapturePlat> queryCapturePlatList(CapturePlat form) {
    return super.queryForList("slh_capture_plat.select_slh_model_list", form);
  }

  @Override
  public int deleteCapturePlat(CapturePlat form) throws Exception {
    return super.delete("slh_capture_plat.delete_slh_model", form);
  }

}
