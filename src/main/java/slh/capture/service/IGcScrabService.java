package slh.capture.service;

import java.util.List;

import slh.capture.domain.ScrabData;

public interface IGcScrabService extends IScrabService {

  public List<ScrabData> execute(String month);

}
