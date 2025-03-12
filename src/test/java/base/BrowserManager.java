package base;

import com.microsoft.playwright.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BrowserManager {
    // A ThreadLocal is like a personal locker for each thread, so they don't share data with other threads.
    // Think of threads as individual workers in a factory, each with their own toolbox (ThreadLocal).
    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>(); //used to create an instance of the Chromium, Firefox browser etc.
    private static final ThreadLocal<Browser> browser = new ThreadLocal<>(); //represents the browser instance.
    private static final ThreadLocal<BrowserContext> context = new ThreadLocal<>(); //is the isolated browser session.
    private static final ThreadLocal<Page> page = new ThreadLocal<>(); //is the single tab or window in the browser.
    public Properties properties;
    private static final Logger logger = Logger.getLogger(BrowserManager.class.getName());

    public BrowserManager() {
        properties = new Properties();
        //creates a path to a configuration file. If "config.path" isn't set,
        //it defaults to a file located in "src/main/resources/config.properties
        Path configPath = Paths.get(System.getProperty("config.path",
                Paths.get(System.getProperty("user.dir"), "src", "test", "resources",
                        "config.properties").toString()));
        try(InputStream input = Files.newInputStream(configPath)) {
            properties.load(input);
        }catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load properties file!", e);
        }
    }

    public Page getPage() {
        return page.get();
    }

    public void setPage(Page newPage) {
        page.set(newPage);
    }

    public BrowserContext getContext() {
        return context.get();
    }

    public byte[] takeScreenshot() {
        if (page.get() != null) {
            return page.get().screenshot();
        }
        return new byte[0];
    }

    public void setUp() {
        logger.info("Setting up Playwright...");
        // Get viewport size of screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        try {
            playwright.set(Playwright.create());

            String browserType = properties.getProperty("browser", "chromium");

            switch (browserType.toLowerCase()) {
                case "chromium":
                    browser.set(playwright.get().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)));
                    break;
                case "firefox":
                    browser.set(playwright.get().firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)));
                    break;
                default:
                    logger.warning("Unsupported browser type: " + browserType + ". Defaulting to chromium.");
                    browser.set(playwright.get().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)));
                    break;
            }
            context.set(browser.get().newContext(new Browser.NewContextOptions().setViewportSize(width, height)));
            page.set(context.get().newPage());

            // Set timeouts from properties file or use default values
            int navigationTimeout = Integer.parseInt(properties.getProperty("navigation.timeout", "30000"));
            int actionTimeout = Integer.parseInt(properties.getProperty("action.timeout", "15000"));
            page.get().setDefaultNavigationTimeout(navigationTimeout);
            page.get().setDefaultTimeout(actionTimeout);
            logger.info("Playwright setup complete!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to setup Playwright!!!", e);
        }
    }

    public void tearDown() {
        try {
            logger.info("Tearing down Playwright...");
            if (page.get() != null) page.get().close();
            if (context.get() != null) context.get().close();
            if (browser.get() != null) browser.get().close();
            if (playwright.get() != null) playwright.get().close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to close Playwright resources!!", e);
        }
        logger.info("Playwright teardown complete!");
    }
}
