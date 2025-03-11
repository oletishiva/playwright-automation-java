package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HomePage {
    private Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    public void navigate() {
        if (page == null) {
            throw new IllegalStateException("Page object is null! Ensure Playwright is initialized.");
        }
        page.navigate("https://www.webdriveruniversity.com");
    }

    /**
     * Clicks on the 'CONTACT US' link.
     */
    public void clickContactUs() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("CONTACT US Contact Us Form")).click();
    }

}
