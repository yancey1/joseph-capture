package slh.capture.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IChannelMangeDAO;
import slh.capture.domain.ChannelMange;
import slh.capture.service.IChannelMangeService;

/**
 * 渠道代理应用信息
 * 
 * @author ck 2013-10-11
 * 
 */
@Service("/channelMange")
public class ChannelMangeServiceImpl implements IChannelMangeService {

  @Autowired
  private IChannelMangeDAO ChannelMangeDAO;

  @Override
  public int saveChannelMange(ChannelMange form) throws Exception {
    return ChannelMangeDAO.insertChannelMange(form);
  }

  @Override
  public int removeChannelMange(ChannelMange form) throws Exception {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int modifyChannelMange(ChannelMange form) throws Exception {
    return ChannelMangeDAO.updateChannelMange(form);
  }

  @Override
  public ChannelMange findChannelMange(ChannelMange form) throws Exception {
    return ChannelMangeDAO.selectChannelMange(form);
  }

  @Override
  public List<ChannelMange> findChannelMangeList(ChannelMange form) throws Exception {

    return ChannelMangeDAO.selectChannelMangeList(form);
  }

  @Override
  public Map<String, Object> findChannelMangePageList(DataGridModel page, ChannelMange form) throws Exception {
    return ChannelMangeDAO.selectChannelMangePageList(page, form);
  }

  @Override
  public void deleteChannelMange(Integer ChannelMangeId) throws Exception {

    ChannelMangeDAO.deleteChannelMange(ChannelMangeId);
  }

}
