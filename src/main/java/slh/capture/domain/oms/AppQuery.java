package slh.capture.domain.oms;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AppQuery {
  @SerializedName("products")
  private List<AppQueryResult> products;

  public List<AppQueryResult> getProducts() {
    return products;
  }

  public void setProducts(List<AppQueryResult> products) {
    this.products = products;
  }
  
}
