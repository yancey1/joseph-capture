package slh.capture.service;

import java.util.List;
import java.util.Map;

import slh.capture.common.DataGridModel;
import slh.capture.domain.IntroductionContent;

/**
 * 引入代理应用
 * 
 * @author ck 2013-10-11
 * 
 */
public interface IIntroductService {

  int saveIntroductionContent(IntroductionContent form) throws Exception;

  int removeIntroductionContent(IntroductionContent form) throws Exception;

  int modifyIntroductionContent(IntroductionContent form) throws Exception;
  void deleteIntroductionContent(Integer IntroductionContentId) throws Exception;

  IntroductionContent findIntroductionContent(IntroductionContent form) throws Exception;

  List<IntroductionContent> findIntroductionContentList(IntroductionContent form) throws Exception;

  Map<String, Object> findIntroductionContentPageList(DataGridModel page, IntroductionContent form) throws Exception;
}
