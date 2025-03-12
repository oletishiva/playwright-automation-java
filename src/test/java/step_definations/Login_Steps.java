package step_definations;

import base.BrowserManager;
import com.microsoft.playwright.Locator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class Login_Steps {
    public BrowserManager browserManager;
    private String alertText;

    public Login_Steps(BrowserManager browserManager) {
        this.browserManager = browserManager;
    }

    @And("I type a username {word}")
    public void i_type_a_username(String username) {
        browserManager.getPage().getByPlaceholder("Username").fill(username);
    }

    @And("I type a password {word}")
    public void i_type_a_password(String password) {
        browserManager.getPage().getByPlaceholder("Password").fill(password);
    }

    @And("I click on the login button")
    public void i_click_on_the_login_button() {
        browserManager.getPage().onceDialog(dialog -> {
            alertText = dialog.message();
            dialog.accept();
        });

        Locator loginButton = browserManager.getPage().locator("#login-button");
        loginButton.hover();
        loginButton.click(new Locator.ClickOptions().setForce(true));
    }

    @Then("I should be presented with an alert box which contains text {string}")
    public void i_should_be_presented_with_an_alert_box_which_contains_text(String expectedAlertText) {
        Assert.assertEquals(alertText, expectedAlertText, "The alert text does not match the expected text");
    }
}
