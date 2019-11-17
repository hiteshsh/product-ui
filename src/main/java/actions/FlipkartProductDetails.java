package actions;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import model.Product;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static util.ProductUtility.*;

public class FlipkartProductDetails implements FetchProductStrategy {
    private static final String url = "http://www.flipkart.com";
    private static final long timeoutInMS= 2000;
    private SelenideElement searchInputBox=$(byName("q"));
    private List<SelenideElement> allProductList= $$("._3BTv9X>img");
    private SelenideElement productTitle=$("._35KyD6");
    private List<SelenideElement> productDesc=$$("td");
    private SelenideElement productRating=$(".hGSR34");
    private SelenideElement productPrice=$("._1vC4OE._3qQ9m1");
    private SelenideElement closeLoginPopBtn=$("._2AkmmA._29YdH8");
    private SelenideElement productFromList=$("._3liAhj._1R0K0g");

    @Override
    public Product fetchProductDetails() {
        switchToNewWindow();

        Product product= new Product();
        product.setUrl(WebDriverRunner.currentFrameUrl());
        product.setName(productTitle.getText());
        String rating=productRating.getText();
        product.setRating(splitBySpace(rating)[0]);
        product.setAmount(convertAmountInDigits(productPrice.getText()));

        String description="";
        for (int i = 0; i < productDesc.size(); i+=2) {

            description+=productDesc.get(i)+"-"+productDesc.get(i+1);

        }
        product.setDescription(description);

        String deliveryType= $("._3nCwDW").getText();
        product.setDeliveryType(deliveryType);
        product.setFulfiledBy("Flipkart");
        return product;

    }

    @Override
    public FlipkartProductDetails goTo(){
        open(url);
        closeLoginPopBtn.waitUntil(visible,timeoutInMS).click();
        return this;
    }

    @Override
    public boolean findAndSelectProduct(String productName) {
        String searchKeywords[]=splitBySpace(productName);

        searchInputBox.val(productName).pressEnter();

        productFromList.waitUntil(visible,timeoutInMS);
        List<SelenideElement> allProducts= allProductList;

        boolean productFound;
        SelenideElement matchingProduct=null;
        for (SelenideElement prod:
                allProducts) {
            prod.scrollIntoView(false);
            String actualProductName=prod.getAttribute("alt");
            productFound= isProductMatchedWithKeywords(searchKeywords,actualProductName);
            if (productFound){
                matchingProduct=prod;
                break;
            }
        }
        if (matchingProduct!=null) {
            matchingProduct.click();
            return true;
        }
        System.out.println("In Flipkart No Matching Product found with name:"+productName);

        return false;
    }
}
