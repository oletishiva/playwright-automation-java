package step_definations;

import base.BrowserManager;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class Homepage_Steps {
    public BrowserManager browserManager;

    public Homepage_Steps(BrowserManager browserManager) {
        this.browserManager = browserManager;
    }

    @Given("I navigate to the webdriveruniversity homepage")
    public void i_navigate_to_the_webdriveruniversity_homepage() {
        browserManager.getPage().navigate("https://www.webdriveruniversity.com/");
    }

    @When("I click on the contact us button")
    public void i_click_on_the_contact_us_button() {
        browserManager.setPage(browserManager.getContext().waitForPage(() -> {
            browserManager.getPage().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("CONTACT US Contact Us Form")).click();
        }));

        browserManager.getPage().bringToFront();
    }

    @When("I click on the login portal button")
    public void i_click_on_the_login_portal_button() {
        browserManager.setPage(browserManager.getContext().waitForPage(() -> {
            browserManager.getPage().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("LOGIN PORTAL Login Portal")).click();
        }));

        browserManager.getPage().bringToFront();
    }
}
