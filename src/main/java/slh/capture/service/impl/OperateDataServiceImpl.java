package slh.capture.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IOperateDataDAO;
import slh.capture.domain.OperateData;
import slh.capture.service.IOperateDataService;
import edu.hziee.common.dbroute.BaseDAO;

@Service("operateDataService")
public class OperateDataServiceImpl extends BaseDAO implements IOperateDataService {

  @Autowired
  private IOperateDataDAO operateDataDAO;

  @Override
  public void saveOperateDataList(List<OperateData> list) throws Exception {
    operateDataDAO.batchInsertOperateData(list);
  }

  @Override
  public OperateData findOperateData(OperateData form) throws Exception {
    return operateDataDAO.selectOperateData(form);
  }

  @Override
  public List<OperateData> findOperateDataList(OperateData form) throws Exception {
    return operateDataDAO.selectOperateDataList(form);
  }

  @Override
  public Map<String, Object> findOperateDataPageList(DataGridModel page, OperateData form) throws Exception {
    // TODO Auto-generated method stub
    return operateDataDAO.selectOperateDataPageList(page, form);
  }

  @Override
  public void removeOperateData(OperateData form) throws Exception {
    operateDataDAO.deleteOperateData(form);
  }

}
