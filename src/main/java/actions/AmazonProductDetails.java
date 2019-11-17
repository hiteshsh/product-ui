package actions;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import model.Product;

import java.util.List;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.*;
import static util.ProductUtility.*;

public class AmazonProductDetails implements FetchProductStrategy {

    private static final String url ="http://www.amazon.in";
    private static final long timeoutInMS= 2000;
    private SelenideElement searchInputBox=$("#twotabsearchtextbox");
    private List<SelenideElement> allProductList= $$(".s-image");
    private SelenideElement productTitle=$("#productTitle");
    private SelenideElement productDesc=$("#feature-bullets");
    private SelenideElement productRating=$("#acrPopover");
    private SelenideElement productPrice=$("#priceblock_ourprice");
    //private SelenideElement deliveryTpe=$("#ddmDeliveryMessage + span");

    @Override
    public Product fetchProductDetails(){

        switchToNewWindow();

        Product product= new Product();
        product.setUrl(WebDriverRunner.currentFrameUrl());
        product.setName(productTitle.getText());
        String rating=productRating.getAttribute("title");
        System.out.println("Rating:"+rating);
        product.setRating(splitBySpace(rating)[0]);
        product.setDescription(productDesc.getText());
        product.setAmount(convertAmountInDigits(productPrice.getText()));

        String deliveryType= $(byId("ddmDeliveryMessage")).find("span").getText();
        product.setDeliveryType(deliveryType);
        product.setFulfiledBy("Amazon");
        return product;
    }

    public boolean findAndSelectProduct(String productName){
        String searchKeywords[]=splitBySpace(productName);
        searchInputBox.val(productName).pressEnter();

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
        System.out.println("In Amazon Not Matching Product found with name:"+productName);

        return false;
    }

    @Override
    public AmazonProductDetails goTo(){
        open(url);
        return this;
    }

}
