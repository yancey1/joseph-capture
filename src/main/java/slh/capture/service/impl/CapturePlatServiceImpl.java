package slh.capture.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.DataGridModel;
import slh.capture.dao.ICapturePlatDAO;
import slh.capture.domain.CapturePlat;
import slh.capture.service.ICapturePlatService;

@Service("capturePlatService")
public class CapturePlatServiceImpl implements ICapturePlatService {

  @Autowired
  private ICapturePlatDAO CapturePlatDAO;

  @Override
  public void saveCapturePlat(CapturePlat form) throws Exception {
    CapturePlatDAO.insertCapturePlat(form);
  }

  @Override
  public int modifyCapturePlat(CapturePlat form) throws Exception {
    return CapturePlatDAO.updateCapturePlat(form);
  }

  @Override
  public CapturePlat findCapturePlatById(CapturePlat form) throws Exception {
    return CapturePlatDAO.queryCapturePlat(form);
  }

  @Override
  public List<CapturePlat> findCapturePlatList(CapturePlat form) throws Exception {
    return CapturePlatDAO.queryCapturePlatList(form);
  }

  @Override
  public Map<String, Object> findCapturePlatPageList(DataGridModel page, CapturePlat form) throws Exception {
    return CapturePlatDAO.selectCapturePlatPageList(page, form);
  }

  @Override
  public int removeCapturePlat(CapturePlat form) throws Exception {
    return CapturePlatDAO.deleteCapturePlat(form);
  }

}
