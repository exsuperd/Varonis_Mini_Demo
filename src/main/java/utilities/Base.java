package utilities;

import com.microsoft.playwright.*;
import extensions.ApiActions;
import extensions.UIActions;
import extensions.Verifications;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import pageObjects.ActiveDirectoryPage;
import pageObjects.HomePage;
import workFlows.ActiveDirectoryFlows;
import workFlows.HomePageFlows;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


public abstract class Base {

    // Core Playwright objects
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    public Page page;

    // Page objects
    public HomePage homePage;
    public ActiveDirectoryPage activeDirectoryPage;

    // Flows
    public HomePageFlows homePageFlows;
    public ActiveDirectoryFlows activeDirectoryFlows;

    // Utilities
    public UsefulMethods usefulMethods;
    public Verifications verifications;
    public UIActions uiActions;
    public ApiActions apiActions;

    //API objects
    public RequestSpecification httpRequest;
    public Response response;
    public JSONObject requestParameters;
    public JsonPath jp;
    public Document doc;

    protected void initCore() {
        //init UsefulMethods
        try {
            usefulMethods = new UsefulMethods(verifications);
            String baseUrl = usefulMethods.getData("baseURL");
            if (baseUrl == null || baseUrl.isEmpty()) {
                throw new IllegalStateException("Base URL configuration is missing");
            }
            // Initialize Playwright
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setArgs(List.of("--start-maximized")));
            context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(null));
            page = context.newPage();
            page.setDefaultNavigationTimeout(Integer.parseInt(usefulMethods.getData("defaultNavigationTimeout")));
            page.setDefaultTimeout(Integer.parseInt(usefulMethods.getData("defaultTimeout")));

            // Initialize utilities that need the page object
            uiActions = new UIActions(page);
            verifications = new Verifications(page);

            // Initialize API components - optional

            // Initialize pages
            homePage = new HomePage(page);
            activeDirectoryPage = new ActiveDirectoryPage(page);

            // Initialize flows
            homePageFlows = new HomePageFlows(this, homePage);
            activeDirectoryFlows = new ActiveDirectoryFlows(this, activeDirectoryPage, homePage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize core components", e);
        }
    }

    protected Page getPage() {
        if (page == null) {
            throw new IllegalStateException("Page is not initialized. Call initCore() first.");
        }
        return page;
    }

    protected void closePlaywright() {
        try {
            if (page != null) {
                page.close();
                page = null;
            }
            if (context != null) {
                context.close();
                context = null;
            }
            if (browser != null) {
                browser.close();
                browser = null;
            }
            if (playwright != null) {
                playwright.close();
                playwright = null;
            }
        } catch (Exception e) {
            System.err.println("Error during Playwright cleanup: " + e.getMessage());
        }
    }

    protected boolean hasPlaywrightResources() {
        return playwright != null || browser != null || context != null || page != null;
    }

    // Accessor methods for flows
    public Page getPageForFlow() {
        return page;
    }

    public UIActions getUiActions() {
        return uiActions;
    }

    public Verifications getVerifications() {
        return verifications;
    }

    public UsefulMethods getUsefulMethods() {
        return usefulMethods;
    }

    protected void initAPI(String baseURI) {
        RestAssured.baseURI = baseURI;
        httpRequest = RestAssured.given();
        httpRequest.header("Content-Type", "application/json");
        // Initialize ApiActions with this instance
        apiActions = new ApiActions(this);
    }

    // Add getter methods for API-related objects
    public RequestSpecification getHttpRequest() {
        return httpRequest;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setJsonPath(JsonPath jp) {
        this.jp = jp;
    }

    public JsonPath getJsonPath() {
        return jp;
    }

    public Connection con;
    public Statement stmt;
    public ResultSet rs;

    protected void initConnectionMYSQL(String dbURL, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbURL, username, password);
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println("Error occurred while connecting to DB, see details: " + e);
        }
    }

    protected void closeConnection() {
        try {
            con.close();
        } catch (Exception e) {
            System.out.println("Error occurred while closing DB connection, see details: " + e);
        }
    }
}