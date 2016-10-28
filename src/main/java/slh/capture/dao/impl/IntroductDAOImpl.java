package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IIntroductDAO;
import slh.capture.domain.IntroductionContent;
import edu.hziee.common.dbroute.BaseDAO;

public class IntroductDAOImpl extends BaseDAO implements IIntroductDAO {

  @Override
  public int insertIntroductionContent(IntroductionContent form) throws Exception {
    Object obj = super.insert("slh_introduct.insert_slh_introduct", form);
    return obj == null ? 0 : (Integer) obj;
  }

  @Override
  public void deleteIntroductionContent(Integer cbId) throws Exception {
    super.delete("slh_introduct.delete_slh_introduct", cbId);
  }

  @Override
  public int updateIntroductionContent(IntroductionContent form) throws Exception {
    return super.update("slh_introduct.update_slh_introduct", form);
  }

  @Override
  public IntroductionContent selectIntroductionContent(IntroductionContent form) throws Exception {
    return (IntroductionContent) super.queryForObject("slh_introduct.select_slh_introduct", form);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<IntroductionContent> selectIntroductionContentList(IntroductionContent form) throws Exception {
    return super.queryForList("slh_introduct.select_slh_introduct_list", form);
  }

  @Override
  public Map<String, Object> selectIntroductionContentPageList(DataGridModel page, IntroductionContent form) throws Exception {
    form = (form == null ? new IntroductionContent() : form);
    form.setPageInfo(page);
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total", super.queryForCount("slh_introduct.select_slh_introduct_page_list_count", form));
    results.put("rows", super.queryForList("slh_introduct.select_slh_introduct_page_list", form));
    return results;
  }

}
