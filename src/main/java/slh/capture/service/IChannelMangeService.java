package slh.capture.service;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.ChannelMange;

/**
 * 渠道代理应用信息
 * 
 * @author ck 2013-10-11
 * 
 */
public interface IChannelMangeService {

  int saveChannelMange(ChannelMange form) throws Exception;

  int removeChannelMange(ChannelMange form) throws Exception;

  int modifyChannelMange(ChannelMange form) throws Exception;
  void deleteChannelMange(Integer ChannelMangeId) throws Exception;

  ChannelMange findChannelMange(ChannelMange form) throws Exception;

  List<ChannelMange> findChannelMangeList(ChannelMange form) throws Exception;

  Map<String, Object> findChannelMangePageList(DataGridModel page, ChannelMange form) throws Exception;
}
