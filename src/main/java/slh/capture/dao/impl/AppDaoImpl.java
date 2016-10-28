package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import edu.hziee.common.dbroute.BaseDAO;

import slh.capture.dao.IAppDao;
import slh.capture.domain.AppDomain;

@Repository("appDao")
public class AppDaoImpl extends BaseDAO implements IAppDao {
  
  
  
  @Override
  public Map<String, Object> getAppList(AppDomain domain) {
    Map<String, Object> map=new HashMap<String, Object>();
    map.put("rows", super.queryForList("slh_app.getAppList",domain));
    map.put("total", super.queryForCount("slh_app.getAppCount",domain));
    return map;
  }

  @Override
  public void saveApp(AppDomain domain) {
    super.insert("slh_app.insertApp", domain);
  }

  @Override
  public void modifyApp(AppDomain domain) {
    super.update("slh_app.modifyApp", domain);
  }

  @Override
  public void deleteApp(AppDomain domain) {
    super.delete("slh_app.deleteApp", domain.getId());
  }

  @Override
  public List<AppDomain> getAppListByApp(AppDomain domain) {
    return super.queryForList("slh_app.getAppListByApp",domain);
  }

}
