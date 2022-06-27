import Utils.Customer;

import com.codeborne.selenide.CollectionCondition;

import com.codeborne.selenide.Condition;

import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;

import Utils.TestUser;

import Utils.Goods;


import static com.codeborne.selenide.Selenide.*;


public class q {

    private final String url = "https://www.saucedemo.com/";

    TestUser newTestUser = new TestUser("standard_user", "secret_sauce");


    @Test

    public void checkGoodsCountInBasket() {

        int goodsCount = 0;

        loginWithTestUser(newTestUser);

        for (Goods good : Goods.values()) {

            addGoodInBasket(good);

            goodsCount++;

        }

        element(By.cssSelector(".shopping_cart_link")).click();

        elements(By.cssSelector("cart_item")).shouldHave(CollectionCondition.size(goodsCount));

    }


    @Test

    public void checkGoodsCountInBubble() {

        int goodsCount = 0;

        loginWithTestUser(newTestUser);

        for (Goods good : Goods.values()) {

            addGoodInBasket(good);

            goodsCount++;

        }

        element(By.cssSelector(".shopping_cart_badge")).shouldHave(Condition.exactText(String.valueOf(goodsCount)));

    }


    @Test

    public void checkGoodsCountInBasketAfterOrder() {

        Customer customer = new Customer("Anastasiya", "Dolgova", "111111");

        loginWithTestUser(newTestUser);

        addGoodInBasket(Goods.SAUCE_LABS_BACKPACK);

        element(By.cssSelector(".shopping_cart_link")).shouldBe(Condition.visible).click();

        element(By.xpath("//button[@name='checkout']")).shouldBe(Condition.visible).click();

        element(By.xpath("//input[@name='firstName']")).setValue(customer.getFirstName()).click();

        element(By.xpath("//input[@name='lastName']")).setValue(customer.getLastName()).click();

        element(By.xpath("//input[@name='postalCode']")).setValue(customer.getPostalCode()).click();

        element(By.xpath("//input[@name='continue']")).shouldBe(Condition.visible).click();

        element(By.xpath("//button[@name='finish']")).shouldBe(Condition.visible).click();

        element(By.cssSelector(".shopping_cart_link")).shouldBe(Condition.visible).click();

        element(By.cssSelector(".shopping_cart_badge")).shouldHave(Condition.disappear);

    }


    @Test

    public void checkOpenEveryGood() {

        loginWithTestUser(newTestUser);

        for (Goods good : Goods.values()) {

            element(By.xpath("//div[text()='" + good.getGood() + "']")).click();

            element(By.xpath("//div[text()='" + good.getGood() + "']")).shouldHave(Condition.exactText(good.getGood()));

            back();

        }

    }


    @Test

    public void checkDeleteGoodsFromBasket() {

        loginWithTestUser(newTestUser);

        for (Goods good : Goods.values()) {

            addGoodInBasket(good);

        }

        element(By.cssSelector(".shopping_cart_link")).click();

        for (Goods good : Goods.values()) {

            element(By.xpath("//button[text()='Remove']")).click();

        }

        element(By.cssSelector(".shopping_cart_badge")).shouldHave(Condition.disappear);

    }


    private void loginWithTestUser(TestUser newTestUser) {

        open(url);

        element(By.xpath("//input[@name='user-name']")).setValue(newTestUser.getUserName()).click();

        element(By.xpath("//input[@name='password']")).setValue(newTestUser.getPassword()).click();

        element(By.xpath("//input[@name='login-button']")).shouldBe(Condition.visible).click();

    }


    private void addGoodInBasket(Goods good) {

        element(By.xpath("//div[text()='" + good.getGood() + "']")).click();

        element(By.xpath("//button[text()='Add to cart']")).shouldBe(Condition.visible).click();

        back();

    }

}