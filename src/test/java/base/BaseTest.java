package base;

import com.microsoft.playwright.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;

public class BaseTest {
    protected static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    protected static ThreadLocal<Browser> browser = new ThreadLocal<>();
    protected static ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    protected static ThreadLocal<Page> page = new ThreadLocal<>();

    @BeforeMethod
    @Parameters("browserType")
    public void setup(String browserType) {
        System.out.println("Initializing Playwright..."+ browserType);
        playwright.set(Playwright.create());

        switch (browserType.toLowerCase()) {
            case "firefox":
                System.out.println("Launching Firefox browser...");
                browser.set(playwright.get().firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)));
                break;
            case "webkit":
                System.out.println("Launching WebKit browser...");
                browser.set(playwright.get().webkit().launch(new BrowserType.LaunchOptions().setHeadless(false)));
                break;
            case "chromium":
            default:
                System.out.println("Launching Chromium browser...");
                browser.set(playwright.get().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)));
                break;
        }

        context.set(browser.get().newContext());
        page.set(context.get().newPage());

        // ✅ Enable tracing for debugging test failures
        context.get().tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
    }

    @AfterMethod
    public void teardown() {
        System.out.println("Cleaning up Playwright session...");

        if (context.get() != null) {
            context.get().tracing().stop(); // ✅ Stop tracing before closing context
            context.get().close();
        }

        if (page.get() != null) {
            page.get().close();
        }

        if (browser.get() != null) {
            browser.get().close();
        }

        if (playwright.get() != null) {
            playwright.get().close();
        }
    }
}
