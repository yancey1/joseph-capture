package slh.capture.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.dao.IAppDao;
import slh.capture.domain.AppDomain;
import slh.capture.service.IAppService;

@Service("appService")
public class AppServiceImpl implements IAppService {

  @Autowired
  private IAppDao appDao;

  @Override
  public Map<String, Object> getAppList(AppDomain domain) {
    return appDao.getAppList(domain);
  }

  @Override
  public void saveApp(AppDomain domain) {
    appDao.saveApp(domain);
  }

  @Override
  public void modifyApp(AppDomain domain) {
    appDao.modifyApp(domain);
  }

  @Override
  public void deleteApp(AppDomain domain) {
    appDao.deleteApp(domain);
  }

  @Override
  public List<AppDomain> getAppListByApp(AppDomain domain) {
    return appDao.getAppListByApp(domain);
  }

}
