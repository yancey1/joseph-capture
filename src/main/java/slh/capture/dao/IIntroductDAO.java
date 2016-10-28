package slh.capture.dao;

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
public interface IIntroductDAO {

  /**
   * 保存引入代理应用
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int insertIntroductionContent(IntroductionContent form) throws Exception;

  /**
   * 删除引入代理应用信息
   * 
   * @param form
   * @return
   * @throws Exception
   */
  void deleteIntroductionContent(Integer useId) throws Exception;

  /**
   * 编辑引入代理应用
   * 
   * @param form
   * @return
   * @throws Exception
   */
  int updateIntroductionContent(IntroductionContent form) throws Exception;

  /**
   * 获取指定引入代理应用
   * 
   * @param form
   * @return
   * @throws Exception
   */
  IntroductionContent selectIntroductionContent(IntroductionContent form) throws Exception;

  /**
   * 不带分页的引入代理应用列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  List<IntroductionContent> selectIntroductionContentList(IntroductionContent form) throws Exception;

  /**
   * 带分页的引入代理应用列表
   * 
   * @param page
   * @param form
   * @return
   * @throws Exception
   */
  Map<String, Object> selectIntroductionContentPageList(DataGridModel page, IntroductionContent form) throws Exception;

}
