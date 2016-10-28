package slh.capture.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import slh.capture.dao.IAccountDao;
import slh.capture.domain.AccountDomain;
import slh.capture.service.IAccountService;

@Service("accountService")
public class AccountServiceImpl implements IAccountService {

  @Autowired
  private IAccountDao accountDao;

  @Override
  public Map<String, Object> getAccountList(AccountDomain domain) {
    return accountDao.getAccountList(domain);
  }

  @Override
  public void saveAccount(AccountDomain domain) {
    accountDao.saveAccount(domain);
  }

  @Override
  public void modifyAccount(AccountDomain domain) {
    accountDao.modifyAccount(domain);
  }

  @Override
  public void deleteAccount(AccountDomain domain) {
    accountDao.deleteAccount(domain);
  }

  @Override
  public List<AccountDomain> getAccountListByAccount(AccountDomain domain) {
    return accountDao.getAccountListByObj(domain);
  }

}
