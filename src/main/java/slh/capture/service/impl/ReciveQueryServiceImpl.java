package slh.capture.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IReciveQueryDAO;
import slh.capture.domain.ReciveQuery;
import slh.capture.service.IReciveQueryService;
import edu.hziee.common.dbroute.BaseDAO;

@Service("reciveQueryService")
public class ReciveQueryServiceImpl extends BaseDAO implements IReciveQueryService {
  @Autowired
  private IReciveQueryDAO reciveQueryDAO;

  @Override
  public void saveReciveQueryList(List<ReciveQuery> list) throws Exception {
    reciveQueryDAO.batchInsertReciveQuery(list);
  }

  @Override
  public int removeReciveQuery(ReciveQuery form) throws Exception {
    return reciveQueryDAO.deleteReciveQuery(form);
  }

  @Override
  public void modifyReciveQuery(List<ReciveQuery> form) throws Exception {
    reciveQueryDAO.batchUpdateReciveQuery(form);
  }

  @Override
  public ReciveQuery findReciveQuery(ReciveQuery form) throws Exception {
    return reciveQueryDAO.selectReciveQuery(form);
  }

  @Override
  public List<ReciveQuery> findReciveQueryList(ReciveQuery form) throws Exception {
    return reciveQueryDAO.selectReciveQueryList(form);
  }

  @Override
  public List<ReciveQuery> findReciveQuerySettleList(ReciveQuery form) throws Exception {
    return reciveQueryDAO.selectReciveQuerySettleList(form);
  }

  @Override
  public Map<String, Object> findReciveQueryPageList(DataGridModel page, ReciveQuery form) throws Exception {
    return reciveQueryDAO.selectReciveQueryPageList(page, form);
  }

  @Override
  public Map<String, Object> findReciveQuerySettlePageList(DataGridModel page, ReciveQuery form) throws Exception {
    return reciveQueryDAO.selectReciveQuerySettlePageList(page, form);
  }

  @Override
  public List<ReciveQuery> findSettleReciveQueryList(ReciveQuery form) throws Exception {

    return reciveQueryDAO.selectSettleReciveQueryList(form);
  }

}
