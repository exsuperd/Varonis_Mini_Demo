package workFlows;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import extensions.UIActions;
import extensions.Verifications;
import pageObjects.HomePage;
import utilities.Base;
import utilities.UsefulMethods;

import java.util.List;

import static org.testng.AssertJUnit.*;

public class HomePageFlows {

    private final HomePage homePage;
    public final Page page;
    public final UIActions uiActions;
    public final Verifications verifications;
    public final UsefulMethods usefulMethods;

    public HomePageFlows(Base base, HomePage homePage) {
        this.homePage = homePage;
        this.page = base.getPageForFlow();
        this.uiActions = base.getUiActions();
        this.verifications = base.getVerifications();
        this.usefulMethods = base.getUsefulMethods();
    }

    public void acceptCookies() {
        List<Locator> acceptCookiesDialogList = homePage.cookiesApprovalDialogList.all();
        if (!acceptCookiesDialogList.isEmpty() & acceptCookiesDialogList.get(0).isVisible()) {
            homePage.acceptCookiesButton.click();
            page.waitForTimeout(1000);
            assertFalse(acceptCookiesDialogList.get(0).isVisible());
        }
    }

    public void verifyHomePageTopHeaders(String[] expectedHomepageTopHeadersTexts) {
        List<String> allTexts = homePage.homePageTopHeadersList.allInnerTexts();
        List<String> allVisibleTexts = allTexts.subList(0, allTexts.size() - 1);
        System.out.println(allVisibleTexts);
        verifications.verifyCorrectTextInEachStringListIndex(allVisibleTexts, expectedHomepageTopHeadersTexts);
    }

    public void selectLanguage(String inputLanguage, String expectedGetDemoButtonText) {

        switch (inputLanguage) {
            case ("English"), ("english") -> {
                homePage.languageSwitcher.click();
                homePage.english.click();
                assertTrue(homePage.getDemoButton.getAttribute("href").contains("hsLang=en"));
                assertEquals(homePage.getDemoButton.innerText(), expectedGetDemoButtonText);
            }
            case ("Deutsch"), ("deutsch") -> {
                homePage.languageSwitcher.click();
                homePage.deutsch.click();
                assertTrue(homePage.getDemoButton.getAttribute("href").contains("hsLang=de"));
                assertEquals(homePage.getDemoButton.innerText(), expectedGetDemoButtonText);
            }
            case ("Francais"), ("francais") -> {
                homePage.languageSwitcher.click();
                homePage.francais.click();
                System.out.println(homePage.getDemoButton.innerText());
                assertTrue(homePage.getDemoButton.getAttribute("href").contains("hsLang=fr"));
                assertEquals(homePage.getDemoButton.innerText(), expectedGetDemoButtonText);
            }

            default -> {
                System.out.println("Invalid language name stated. Switching back to English. Please select a valid " +
                        "language next time");
                homePage.languageSwitcher.click();
                homePage.english.click();
                assertTrue(homePage.getDemoButton.getAttribute("href").contains("hsLang=en"));
                assertEquals(homePage.getDemoButton.innerText(), expectedGetDemoButtonText);
            }
        }
    }

    public void verifyLanguageSelectorFunctionality(String misspelled, String expectedEnglishText, String expectedDeutschText,
                                                    String expectedFrancaisText) {
        selectLanguage(misspelled, expectedEnglishText);
        selectLanguage("deutsch", expectedDeutschText);
        selectLanguage("Francais", expectedFrancaisText);
        selectLanguage("english", expectedEnglishText);
    }

    public void verifyHomePageUseCasesTexts(String[] expectedUseCasesTexts) {
        List<Locator> useCases = homePage.useCaseLinksList.all();
        verifications.attributeValueInEachListIndex(useCases, "aria-label", expectedUseCasesTexts);
    }

    public void verifyHomePageUseCasesAreVisibleAndEnabled() {
        List<Locator> useCases = homePage.useCaseLinksList.all();
        verifications.verifyVisibilityOfAllListIndex(useCases);
        verifications.allListWebElementsAreEnabled(useCases);
    }

    public void verifyHomePageUseCasesBackgroundColorWhenHovered(String expectedHoveredColor) {
        List<Locator> useCases = homePage.useCaseLinksList.all();
        verifications.getAndVerifyHoveredColorOfLocatorsList(useCases, expectedHoveredColor);
    }
}