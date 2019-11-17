package util;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class ProductUtility {

    public static void switchToNewWindow(){
        WebDriver driver= WebDriverRunner.getWebDriver();
        Set<String> windowHandles=driver.getWindowHandles();
        windowHandles.forEach(e-> driver.switchTo().window(e));
    }

    public static String[] splitBySpace(String key){
        return key.split("\\s+");
    }

    public static boolean isProductMatchedWithKeywords(String[] keywords, String productName){
        boolean isMatched=false;
        for (String keyword:
                keywords) {
            if (productName.contains(keyword)) {
                isMatched= true;
            }
            else {
                isMatched= false;
                break;
            }
        }
        return isMatched;
    }

    public static double convertAmountInDigits(String price){
        return Double.parseDouble(price.replaceAll("^\\D+",""));
    }

    public static void main(String[] args) {
        System.out.println(convertAmountInDigits("₹ 345.20"));
    }
}
