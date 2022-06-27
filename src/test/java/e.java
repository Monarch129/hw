import com.codeborne.selenide.*;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;

import utils.Items;


import static com.codeborne.selenide.Selenide.*;

import static org.openqa.selenium.By.cssSelector;


public class e {


    private final String url = "https://www.saucedemo.com/";

    private final String userName = "standard_user";

    private final String password = "secret_sauce";


    @BeforeEach

    public void logIn() {

        open(url);

        element(By.xpath("//input[@id=\"user-name\"]")).sendKeys(userName);

        element(By.xpath("//input[@id=\"password\"]")).sendKeys(password);

        element(By.xpath("//input[@id=\"login-button\"]")).shouldBe(Condition.visible).click();

    }


    @Test

    @DisplayName("Проверка что количество товаров в корзине соответствует количеству добавленных товаров")

    public void checkAddingItemsToShoppingCart() {

        ElementsCollection collection = $$x("//div[@class=\"inventory_item\"]");

        int collectionSize = collection.size();

        for (SelenideElement item : collection) {

            item.findElement(cssSelector("button")).click();

        }


        element(By.xpath("//a[@class=\"shopping_cart_link\"]")).click();

        ElementsCollection shoppingCart = $$x("//div[@class=\"cart_item\"]");

        shoppingCart.shouldHave(CollectionCondition.size(collectionSize));


    }


    @Test

    @DisplayName("Проверка что при добавлении товара в корзину появляется пузырек с корректным количеством товаров")

    public void checkShoppingCartBadge() {

        ElementsCollection collection = $$x("//div[@class=\"inventory_item\"]");

        int i = 0;

        for (SelenideElement item : collection){

            item.findElement(cssSelector("button")).click();

            i++;

            element(By.xpath("//span[@class=\"shopping_cart_badge\"]")).shouldBe

                    (Condition.visible).shouldHave(Condition.exactText(String.valueOf(i)));


        }


    }


    @Test

    @DisplayName("Проверка что можно оформить заказ и после оформления заказа корзина пуста")

    public void checkOutOrder_BinIsEmpty() {


        String firstName = "Helen";

        String lastName = "Star";

        String postCode = "135";



        element(By.xpath("//a[@class=\"shopping_cart_link\"]")).click();

        element(By.xpath("//button[@id=\"checkout\"]")).click();

        element(By.xpath("//input[@id=\"first-name\"]")).sendKeys(firstName);

        element(By.xpath("//input[@id=\"last-name\"]")).sendKeys(lastName);

        element(By.xpath("//input[@id=\"postal-code\"]")).sendKeys(postCode);

        element(By.xpath("//input[@id=\"continue\"]")).click();

        element(By.xpath("//button[@id=\"finish\"]")).click();

        element(By.xpath("//span[@class=\"title\"]")).shouldHave(Condition.exactText("Checkout: Complete!"));


        element(By.xpath("//a[@class=\"shopping_cart_link\"]")).click();

        ElementsCollection shoppingCart = $$x("//div[@class=\"cart_item\"]");

        shoppingCart.shouldHave(CollectionCondition.size(0));

    }


    @Test

    @DisplayName("Проверка открытия каждого товара")

    public void openAllItems() {

        for (Items item : Items.values()){

            element(By.xpath("//div[text()='" + item.getItem() + "']")).click();

            element(By.xpath("//div[text()='" + item.getItem() + "']")).shouldHave(Condition.exactText(item.getItem()));

            back();

        }


    }


    @Test

    @DisplayName("Проверка что товары из корзины можно удалить в самой корзине")

    public void deleteAllItems_BinIsEmpty() {

        ElementsCollection collection = $$x("//div[@class=\"inventory_item\"]");

        int i = 0;

        for (SelenideElement item : collection){

            item.findElement(cssSelector("button")).click();

            i++;

        }


        element(By.xpath("//a[@class=\"shopping_cart_link\"]")).click();

        ElementsCollection shoppingCart = $$x("//div[@class=\"cart_item\"]");

        element(By.xpath("//button[@class=\"btn btn_secondary btn_small cart_button\"]")).click();

        for (SelenideElement remove : collection){

            remove.click();

        }


        shoppingCart.shouldHave(CollectionCondition.size(0));

    }

}

