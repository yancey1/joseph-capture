package slh.capture.service;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.OperateData;

public interface IOperateDataService {

  void saveOperateDataList(List<OperateData> list) throws Exception;

  OperateData findOperateData(OperateData form) throws Exception;

  List<OperateData> findOperateDataList(OperateData form) throws Exception;

  Map<String, Object> findOperateDataPageList(DataGridModel page, OperateData form) throws Exception;

  void removeOperateData(OperateData form) throws Exception;

}
