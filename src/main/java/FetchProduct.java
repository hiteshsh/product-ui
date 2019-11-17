import actions.AmazonProductDetails;
import actions.FetchProductStrategy;
import actions.FlipkartProductDetails;
import com.codeborne.selenide.Configuration;
import com.google.gson.Gson;
import model.Product;
import model.Products;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FetchProduct {

    private WebDriver driver;

    public FetchProduct() {

        System.setProperty("webdriver.chrome.driver","./src/test/resources/webdrivers/chromedriver");
        driver= new ChromeDriver();
        //driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        //Configuration.startMaximized=true;
        //Configuration.timeout=15;
    }

    public static void main(String[] args) {

        List<Product> listOFProducts= new ArrayList<>();
        listOFProducts.add(getProductDetails(new AmazonProductDetails(),"Philips SBCHL140"));
        listOFProducts.add(getProductDetails(new FlipkartProductDetails(),"Philips SBCHL140"));


        listOFProducts.sort(Comparator.comparing(Product::getAmount));

        Products products= new Products();
        products.setProducts(listOFProducts);
        products.setRecommendation(listOFProducts.get(0).getFulfiledBy());

        String json = new Gson().toJson(products);
        System.out.println(json);

    }

    public static Product getProductDetails(FetchProductStrategy marketPlace, String productName){

        Product product=null;
        boolean productFound=marketPlace.goTo().findAndSelectProduct(productName);
        if (productFound) {
            product= marketPlace.fetchProductDetails();
        }

        return product;

    }

}
