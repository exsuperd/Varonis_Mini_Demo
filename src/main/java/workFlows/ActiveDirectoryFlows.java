package workFlows;

import com.microsoft.playwright.Page;
import extensions.UIActions;
import extensions.Verifications;
import pageObjects.ActiveDirectoryPage;
import pageObjects.HomePage;
import utilities.Base;
import utilities.UsefulMethods;

public class ActiveDirectoryFlows {

    private final ActiveDirectoryPage activeDirectoryPage;
    private final HomePage homePage;
    public final Page page;
    public final UIActions uiActions;
    public final Verifications verifications;
    public final UsefulMethods usefulMethods;
    public final String expectedHoveredColor = "rgb(0, 119, 255)";

    public ActiveDirectoryFlows(Base base, ActiveDirectoryPage activeDirectoryPage, HomePage homePage) {
        this.activeDirectoryPage = activeDirectoryPage;
        this.homePage = homePage;
        this.page = base.getPageForFlow();
        this.uiActions = base.getUiActions();
        this.verifications = base.getVerifications();
        this.usefulMethods = base.getUsefulMethods();
    }


    public void verifyFreeAssessmentLinkButtonHoverColor() throws InterruptedException {
        uiActions.scrollAndWaitForVisible(homePage.useCaseLinksList.all().get(11));
        homePage.useCaseLinksList.all().get(11).click();
        page.waitForURL("https://www.varonis.com/coverage/active-directory");
        verifications.getAndVerifyHoveredLocatorColor(activeDirectoryPage.freeRiskAssessmentLink, expectedHoveredColor);
    }
}
