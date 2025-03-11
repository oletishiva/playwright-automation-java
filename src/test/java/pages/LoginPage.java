package pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    private String usernameField = "#username";
    private String passwordField = "#password";
    private String loginButton = "#login";

    public void navigate() {
        page.navigate("https://example.com/login");
    }

    public void enterUsername(String username) {
        page.fill(usernameField, username);
    }

    public void enterPassword(String password) {
        page.fill(passwordField, password);
    }

    public void clickLogin() {
        page.click(loginButton);
    }
}

