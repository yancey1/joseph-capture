package slh.capture.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import slh.capture.dao.IAccountDao;
import slh.capture.domain.AccountDomain;
import edu.hziee.common.dbroute.BaseDAO;

@Repository("accountDao")
public class AccountDaoImpl extends BaseDAO implements IAccountDao {

  @Override
  public Map<String, Object> getAccountList(AccountDomain domain) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("rows", super.queryForList("slh_account.getAccountList", domain));
    map.put("total", super.queryForCount("slh_account.getAccountCount", domain));
    return map;
  }

  @Override
  public void saveAccount(AccountDomain domain) {
    super.insert("slh_account.insertAccount", domain);
  }

  @Override
  public void modifyAccount(AccountDomain domain) {
    super.update("slh_account.modifyAccount", domain);
  }

  @Override
  public void deleteAccount(AccountDomain domain) {
    super.delete("slh_account.deleteAccount", domain.getId());
  }

  @Override
  public List<AccountDomain> getAccountListByObj(AccountDomain domain) {
    return super.queryForList("slh_account.getAccountListByObj", domain);
  }

}
