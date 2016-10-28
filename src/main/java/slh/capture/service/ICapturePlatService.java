package slh.capture.service;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.CapturePlat;

public interface ICapturePlatService {

  public void saveCapturePlat(CapturePlat form) throws Exception;

  public int modifyCapturePlat(CapturePlat form) throws Exception;

  public CapturePlat findCapturePlatById(CapturePlat form) throws Exception;

  public List<CapturePlat> findCapturePlatList(CapturePlat form) throws Exception;

  public Map<String, Object> findCapturePlatPageList(DataGridModel page, CapturePlat form) throws Exception;

  public int removeCapturePlat(CapturePlat form) throws Exception;

}
