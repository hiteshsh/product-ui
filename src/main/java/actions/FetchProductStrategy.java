package actions;

import model.Product;

public interface FetchProductStrategy {
    Product fetchProductDetails();
    FetchProductStrategy goTo();
    boolean findAndSelectProduct(String productName);
}
