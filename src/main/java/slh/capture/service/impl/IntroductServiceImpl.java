package slh.capture.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.common.DataGridModel;
import slh.capture.dao.IIntroductDAO;
import slh.capture.domain.IntroductionContent;
import slh.capture.service.IIntroductService;

@Service("/introductService")
public class IntroductServiceImpl implements IIntroductService {

  @Autowired
  private IIntroductDAO introductDAO;

  @Override
  public int saveIntroductionContent(IntroductionContent form) throws Exception {
    return introductDAO.insertIntroductionContent(form);
  }

  @Override
  public int removeIntroductionContent(IntroductionContent form) throws Exception {

    return 0;
  }

  @Override
  public int modifyIntroductionContent(IntroductionContent form) throws Exception {
    return introductDAO.updateIntroductionContent(form);
  }

  @Override
  public IntroductionContent findIntroductionContent(IntroductionContent form) throws Exception {
    return introductDAO.selectIntroductionContent(form);
  }

  @Override
  public List<IntroductionContent> findIntroductionContentList(IntroductionContent form) throws Exception {
    // TODO Auto-generated method stub
    return introductDAO.selectIntroductionContentList(form);
  }

  @Override
  public Map<String, Object> findIntroductionContentPageList(DataGridModel page, IntroductionContent form) throws Exception {
    return introductDAO.selectIntroductionContentPageList(page, form);
  }

  @Override
  public void deleteIntroductionContent(Integer incId) throws Exception {
    // TODO Auto-generated method stub
    introductDAO.deleteIntroductionContent(incId);
  }

}
