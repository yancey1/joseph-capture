package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IReciveQueryDAO;
import slh.capture.domain.ReciveQuery;
import edu.hziee.common.dbroute.BaseDAO;

public class ReciveQueryDAOImpl extends BaseDAO implements IReciveQueryDAO {

  @Override
  public void batchInsertReciveQuery(List<ReciveQuery> list) throws Exception {
    super.batchInsert("slh_recivequery.insert_slh_recivequery", list);
  }

  @Override
  public int deleteReciveQuery(ReciveQuery form) throws Exception {
    return super.delete("slh_recivequery.delete_slh_recivequery", form);
  }

  @Override
  public void batchUpdateReciveQuery(List<ReciveQuery> form) throws Exception {
    super.batchUpdate("slh_recivequery.update_slh_recivequery", form);
  }

  @Override
  public ReciveQuery selectReciveQuery(ReciveQuery form) throws Exception {
    return (ReciveQuery) super.queryForObject("slh_recivequery.select_slh_recivequery", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ReciveQuery> selectReciveQueryList(ReciveQuery form) throws Exception {
    return super.queryForList("slh_recivequery.select_slh_recivequery_list", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ReciveQuery> selectReciveQuerySettleList(ReciveQuery form) throws Exception {
    return super.queryForList("slh_recivequery.select_slh_recivequery_settle_list", form);
  }

  @Override
  public Map<String, Object> selectReciveQueryPageList(DataGridModel page, ReciveQuery form) throws Exception {
    form = (form == null ? new ReciveQuery() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_recivequery.select_slh_recivequery_page_list_count", form));
    results.put("rows", super.queryForList("slh_recivequery.select_slh_recivequery_page_list", form));
    return results;
  }

  @Override
  public Map<String, Object> selectReciveQuerySettlePageList(DataGridModel page, ReciveQuery form) throws Exception {
    form = (form == null ? new ReciveQuery() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_recivequery.select_slh_recivequery_settle_page_list_count", form));
    results.put("rows", super.queryForList("slh_recivequery.select_slh_recivequery_settle_page_list", form));
    return results;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ReciveQuery> selectSettleReciveQueryList(ReciveQuery form) throws Exception {

    return super.queryForList("slh_recivequery.select_slh_recivequery_settle_set_list", form);
  }

}
