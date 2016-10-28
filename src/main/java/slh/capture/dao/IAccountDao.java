package slh.capture.dao;

import java.util.List;
import java.util.Map;

import slh.capture.domain.AccountDomain;

public interface IAccountDao {

  public Map<String, Object> getAccountList(AccountDomain domain);

  public void saveAccount(AccountDomain domain);

  public void modifyAccount(AccountDomain domain);

  public void deleteAccount(AccountDomain domain);

  public List<AccountDomain> getAccountListByObj(AccountDomain domain);
}
