

import org.testng.annotations.*;
import org.testng.annotations.Listeners;
import utilities.Base;

@Listeners(utilities.Listeners.class)
public class VaronisWebAutomationDemo extends Base {

    private boolean setupComplete = false;

    private final String[] expectedHomepageTopHeadersTexts = {"PLATFORM", "COVERAGE", "WHY VARONIS", "COMPANY", "RESOURCES"};
    private final String misspelledDeutsch = "Duch";
    private final String expectedGetDemoEnglishText = "Get a demo";
    private final String expectedGetDemoDeutschText = "Eine Demo beantragen";
    private final String expectedGetDemoFrancaisText = "Obtenez une démo";
    private final String[] expectedHomePageUseCasesTexts = {"DSPM", "Data Discovery & Classification", "Cloud DLP", "AI Security",
            "UEBA", "Managed Data Detection & Response (MDDR)", "Policy Automation", "SSPM", "Data Access Governance",
            "Compliance Management", "Email Security", "Active Directory Security"};
    private final String expectedHoveredUseCasesColor = "rgb(0, 119, 255)";

    @BeforeClass
    public void classSetup() {
        try {
            initCore();
            usefulMethods.deleteAllFilesInAGivenPath("test-output/screenshots");
            String url = usefulMethods.getData("baseURL");
            System.out.println("Navigating to: " + url);
            page.navigate(url);
            homePageFlows.acceptCookies();
            setupComplete = true;
        } catch (Exception e) {
            System.err.println("Test setup failed: " + e.getMessage());
            closePlaywright();
            throw new RuntimeException("Test setup failed", e);
        }
    }

    @Test
    public void test_01_SelectLanguageFunctionality() {
        homePageFlows.verifyLanguageSelectorFunctionality(misspelledDeutsch, expectedGetDemoEnglishText, expectedGetDemoDeutschText
                , expectedGetDemoFrancaisText);
    }

    @Test
    public void test_02_verifyHomepageTopHeaders() {
        homePageFlows.verifyHomePageTopHeaders(expectedHomepageTopHeadersTexts);
    }

    @Test
    public void test_03_verifyHomepageUseCasesAreVisibleAndClickable() {
        homePageFlows.verifyHomePageUseCasesAreVisibleAndEnabled();
    }

    @Test
    public void test_04_verifyHomepageUseCasesTexts() {
        homePageFlows.verifyHomePageUseCasesTexts(expectedHomePageUseCasesTexts);
    }

    @Test
    public void test_05_verifyHomepageUseCasesBackgroundColorWhenHovered() {
        homePageFlows.verifyHomePageUseCasesBackgroundColorWhenHovered(expectedHoveredUseCasesColor);
    }

    @Test
    public void test_06_verifyActiveDirectoryRiskAssessmentLinkColors() throws InterruptedException {
        activeDirectoryFlows.verifyFreeAssessmentLinkButtonHoverColor();
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (hasPlaywrightResources()) {
            closePlaywright();
        }
    }
}


