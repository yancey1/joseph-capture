package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.ChannelMange;

/**
 * 渠道代理应用管理
 * 
 * @author ck 2013-10-11
 * 
 */
public interface IChannelMangeDAO {

  /**
   * 保存渠道代理应用
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int insertChannelMange(ChannelMange form) throws Exception;

  /**
   * 删除渠道代理应用信息
   * 
   * @param form
   * @return
   * @throws Exception
   */
  void deleteChannelMange(Integer useId) throws Exception;

  /**
   * 编辑渠道代理应用
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int updateChannelMange(ChannelMange form) throws Exception;

  /**
   * 获取指定渠道代理应用
   * 
   * @param form
   * @return
   * @throws Exception
   */
  ChannelMange selectChannelMange(ChannelMange form) throws Exception;

  /**
   * 不带分页的渠道代理应用列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<ChannelMange> selectChannelMangeList(ChannelMange form) throws Exception;

  /**
   * 带分页的渠道代理应用列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  Map<String, Object> selectChannelMangePageList(DataGridModel page, ChannelMange form) throws Exception;

}
