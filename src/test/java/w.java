import com.codeborne.selenide.CollectionCondition;

import com.codeborne.selenide.Condition;

import com.codeborne.selenide.ElementsCollection;

import com.codeborne.selenide.SelenideElement;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;


import static com.codeborne.selenide.Selenide.$;

import static com.codeborne.selenide.Selenide.$$;

import static com.codeborne.selenide.Selenide.open;


public class w {


    private static final String BASE_URL = "https://www.saucedemo.com/";


    @BeforeEach

    void setUp() {

        open(BASE_URL);

        login();

    }


    @AfterEach

    void reset() {

        $(By.id("react-burger-menu-btn")).shouldBe(Condition.visible).click();

        $(By.id("reset_sidebar_link")).shouldBe(Condition.visible).click();

        $(By.id("logout_sidebar_link")).shouldBe(Condition.visible).click();

    }


    @Test

    @DisplayName("Тест проверяет добавление всех товаров в корзину")

    void addAllItemsToCartTest() {

        int i = 0;

        for (SelenideElement button : $$(By.xpath("//button[.='Add to cart']")).filter(Condition.visible)) {

            button.click();

            i++;

        }

        $(By.cssSelector(".shopping_cart_link")).click();

        $$(By.cssSelector(".cart_item")).filter(Condition.visible).shouldHave(CollectionCondition.size(i));

    }


    @Test

    @DisplayName("Тест проверяет что число на пузырьке равно количеству товаров в корзине")

    void numOnCartBubbleTest() {

        int i = 0;

        for (SelenideElement button : $$(By.xpath("//button[.='Add to cart']")).filter(Condition.visible)) {

            button.click();

            i++;

            $(By.xpath("//span[@class='shopping_cart_badge']")).shouldBe(Condition.exactText(String.valueOf(i)));

        }

    }


    @Test

    @DisplayName("Тест проверяет что после оформления заказа корзина пуста")

    void cartEmptyAfterOrderTest() {

        $(By.xpath("//button[.='Add to cart']")).shouldBe(Condition.visible).click();

        checkout();

        $(By.cssSelector(".shopping_cart_link")).shouldBe(Condition.visible).click();

        $$(By.cssSelector(".cart_item")).filter(Condition.visible).shouldBe(CollectionCondition.empty);

    }


    @Test

    @DisplayName("Тест проверяет открытие каждого товара")

    void openEveryItemTest() {

        int totalNumOfItems = $$(By.xpath("//div[@class='inventory_item_name']")).size();

        for (int i = 0; i < totalNumOfItems; i++) {

            SelenideElement itemName = $$(By.xpath("//div[@class='inventory_item_name']")).get(i);

            String name = itemName.getText();

            itemName.click();

            $(By.xpath("//div[@class='inventory_details_name large_size']")).shouldBe(Condition.visible)

                    .shouldHave(Condition.exactText(name));

            $(By.id("back-to-products")).click();

        }

    }


    @Test

    @DisplayName("Тест проверяет что корзина пуста после удаления товаров из самой корзины")

    void removeItemsFromCartTest() {

        for (SelenideElement button : $$(By.xpath("//button[.='Add to cart']")).filter(Condition.visible)) {

            button.click();

        }


        $(By.cssSelector(".shopping_cart_link")).shouldBe(Condition.visible).click();


        for (SelenideElement removeButton : $$(By.xpath("//button[.='Remove']")).filter(Condition.visible)) {

            removeButton.click();

        }


        $$(By.cssSelector(".cart_item")).filter(Condition.visible).shouldBe(CollectionCondition.empty);

    }


    private void login() {

        $(By.id("user-name")).setValue("standard_user");

        $(By.id("password")).setValue("secret_sauce");

        $(By.id("login-button")).shouldBe(Condition.visible).click();

    }


    private void checkout() {

        $(By.cssSelector(".shopping_cart_link")).shouldBe(Condition.visible).click();

        $(By.id("checkout")).shouldBe(Condition.visible).click();

        $(By.id("first-name")).setValue("Jack");

        $(By.id("last-name")).setValue("Reacher");

        $(By.id("postal-code")).setValue("193313");

        $(By.id("continue")).shouldBe(Condition.visible).click();

        $(By.id("finish")).shouldBe(Condition.visible).click();

        $(By.id("back-to-products")).shouldBe(Condition.visible).click();

    }

}