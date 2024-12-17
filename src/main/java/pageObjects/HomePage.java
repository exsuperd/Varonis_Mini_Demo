package pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage {
    public final Page page;
    public final Locator homePageTopHeadersList;
    public final Locator languageSwitcher;
    public final Locator availableLanguagesList;
    public final Locator getDemoButton;
    public final Locator english;
    public final Locator deutsch;
    public final Locator francais;
    public final Locator useCaseLinksList;
    public final Locator cookiesApprovalDialogList; //Fake list
    public final Locator acceptCookiesButton;
    public final Locator backToHomePageLink;

    public HomePage(Page page) {
        this.page = page;

        homePageTopHeadersList = page.locator("div.site-navigation-menu-container div[role='button']");
        languageSwitcher = page.locator("div.site-navigation-language-switcher-icons");
        availableLanguagesList = page.locator("(//ul[@class='lang_list_class'])[2]/li/a");
        getDemoButton = page.locator("a.hero-with-cards-ctas");
        english = page.locator("(//ul[@class='lang_list_class'])[2]/li/a", new Page.LocatorOptions().setHasText("English"));
        deutsch = page.locator("(//ul[@class='lang_list_class'])[2]/li/a", new Page.LocatorOptions().setHasText("Deutsch"));
        francais = page.locator("(//ul[@class='lang_list_class'])[2]/li/a", new Page.LocatorOptions().setHasText("Français"));
        useCaseLinksList = page.locator("a.use-case-link");
        cookiesApprovalDialogList = page.locator("div[id='hs-eu-cookie-confirmation']");
        acceptCookiesButton = page.locator("button[id=hs-eu-confirmation-button]");
        backToHomePageLink = page.locator("a[aria-label='Varonis']");
    }
}
