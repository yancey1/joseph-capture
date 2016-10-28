package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IOperateDataDAO;
import slh.capture.domain.OperateData;
import edu.hziee.common.dbroute.BaseDAO;

public class OperateDataDAOImpl extends BaseDAO implements IOperateDataDAO {

  @Override
  public void batchInsertOperateData(List<OperateData> list) throws Exception {
    super.batchInsert("slh_operate_data.insert_slh_operate_data", list);
  }

  @Override
  public OperateData selectOperateData(OperateData form) throws Exception {
    return (OperateData) super.queryForObject("slh_operate_data.select_slh_operate_data", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<OperateData> selectOperateDataList(OperateData form) throws Exception {
    return super.queryForList("slh_operate_data.select_slh_operate_data_list", form);
  }

  @Override
  public Map<String, Object> selectOperateDataPageList(DataGridModel page, OperateData form) throws Exception {
    form = (form == null ? new OperateData() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_operate_data.select_slh_operate_data_page_list_count", form));
    results.put("rows", super.queryForList("slh_operate_data.select_slh_operate_data_page_list", form));
    return results;
  }

  @Override
  public void deleteOperateData(OperateData form) {
    super.delete("slh_operate_data.delete_slh_operate_data", form);
  }

}
