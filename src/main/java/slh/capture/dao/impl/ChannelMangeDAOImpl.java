package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IChannelMangeDAO;
import slh.capture.domain.ChannelMange;
import edu.hziee.common.dbroute.BaseDAO;

/**
 * 渠道代理应用
 * 
 * @author ck 2013-10-11
 * 
 */
public class ChannelMangeDAOImpl extends BaseDAO implements IChannelMangeDAO {

  @Override
  public int insertChannelMange(ChannelMange form) throws Exception {
    Object obj = super.insert("slh_channelmange.insert_slh_channelmange", form);
    return obj == null ? 0 : (Integer) obj;
  }

  @Override
  public void deleteChannelMange(Integer cbId) throws Exception {
    super.delete("slh_channelmange.delete_slh_channelmange", cbId);
  }

  @Override
  public int updateChannelMange(ChannelMange form) throws Exception {
    return super.update("slh_channelmange.update_slh_channelmange", form);
  }

  @Override
  public ChannelMange selectChannelMange(ChannelMange form) throws Exception {
    return (ChannelMange) super.queryForObject("slh_channelmange.select_slh_channelmange", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ChannelMange> selectChannelMangeList(ChannelMange form) throws Exception {
    return super.queryForList("slh_channelmange.select_slh_channelmange_list", form);
  }

  @Override
  public Map<String, Object> selectChannelMangePageList(DataGridModel page, ChannelMange form) throws Exception {
    form = (form == null ? new ChannelMange() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_channelmange.select_slh_channelmange_page_list_count", form));
    results.put("rows", super.queryForList("slh_channelmange.select_slh_channelmange_page_list", form));
    return results;
  }

}
