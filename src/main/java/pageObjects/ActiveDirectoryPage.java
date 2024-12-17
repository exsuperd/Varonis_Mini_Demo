package pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ActiveDirectoryPage {
    public final Page page;
    public final Locator freeRiskAssessmentLink;

    public ActiveDirectoryPage(Page page) {
        this.page = page;

      freeRiskAssessmentLink = page.locator("a.hero-with-trust-and-conversion-ctas:nth-child(2)");
    }
}