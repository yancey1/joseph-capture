package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IScrabDAO;
import slh.capture.domain.ScrabData;
import edu.hziee.common.dbroute.BaseDAO;

public class ScrabDAOImpl extends BaseDAO implements IScrabDAO {

  @Override
  public void saveData(List<ScrabData> dataList) {
    super.batchInsert("slh_scrab.insert_slh_scrab", dataList);
  }

  @Override
  public Map<String, Object> selectScrabDataPageList(DataGridModel page, ScrabData form) throws Exception {
    form = (form == null ? new ScrabData() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_scrab.select_slh_scrab_page_list_count", form));
    results.put("rows", super.queryForList("slh_scrab.select_slh_scrab_page_list", form));
    return results;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ScrabData> selectScrabDataList(ScrabData form) throws Exception {
    return super.queryForList("slh_scrab.select_slh_scrab_list", form);
  }

  @Override
  public ScrabData selectScrabData(ScrabData form) throws Exception {

    return (ScrabData) super.queryForObject("slh_scrab.select_slh_scrab", form);
  }

  @Override
  public void updateScrabData(List<ScrabData> list) throws Exception {
    super.batchUpdate("slh_scrab.update_slh_scrab", list);
  }

  @Override
  public ScrabData selectScrabDataByBookMark(ScrabData form) throws Exception {

    return (ScrabData) super.queryForObject("slh_scrab.select_slh_scrab_by_book_mark", form);
  }

}
