package slh.capture.common;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;

public class EncryptUtils {
  public static final String encryptMD5(String source) {
    if (source == null) {
      source = "";
    }
    Sha256Hash hash = new Sha256Hash(source);
    return new Md5Hash(hash).toString();
  }

  public static void main(String[] args) {
    System.out.println(EncryptUtils.encryptMD5("admin123"));
  }
}
