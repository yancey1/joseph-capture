package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.domain.AppDomain;

public interface IAppDao {

  public Map<String, Object> getAppList(AppDomain domain);

  public void saveApp(AppDomain domain);

  public void modifyApp(AppDomain domain);

  public void deleteApp(AppDomain domain);

  public List<AppDomain> getAppListByApp(AppDomain domain);

}
