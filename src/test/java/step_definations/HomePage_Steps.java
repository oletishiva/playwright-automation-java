package step_definations;

import base.BaseTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.HomePage;

public class HomePage_Steps extends BaseTest {

    private HomePage homepage;

    @Given("I navigate to the webdriveruniversity homepage")
    public void i_navigate_to_the_webdriveruniversity_homepage() {
        homepage= new HomePage(page.get());
        homepage.navigate();
    }
    @When("I click on the contact us button")
    public void i_click_on_the_contact_us_button() {
        homepage.clickContactUs();
    }





}
