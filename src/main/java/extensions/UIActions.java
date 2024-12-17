package extensions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

public class UIActions {

    public final Page page;

    public UIActions(Page page) {
        this.page = page;
    }

    //Scroll methods

    public void scrollAndWaitForVisible(Locator element) {
        element.scrollIntoViewIfNeeded();
        element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void scrollToElementWithBehavior(Locator element) {
        element.evaluate("element => element.scrollIntoView({ behavior: 'smooth', block: 'center' })");
    }

    public void scrollToElementCustom(Locator element) {
        element.evaluate("""
                    element => {
                        const rect = element.getBoundingClientRect();
                        window.scrollTo({
                            top: window.pageYOffset + rect.top - 100,  // 100px above element
                            behavior: 'smooth'
                        });
                    }
                """);
    }

    public boolean safeScrollToElement(Locator element) {
        try {
            element.scrollIntoViewIfNeeded(new Locator.ScrollIntoViewIfNeededOptions()
                    .setTimeout(5000));  // 5 second timeout
            return true;
        } catch (TimeoutError e) {
            System.out.println("Could not scroll to element: " + e.getMessage());
            return false;
        }
    }

    public void scrollToBottom() {
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollUntilClickable(Locator element) {
        element.scrollIntoViewIfNeeded();
        element.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));
        assertTrue("Element should be enabled after scrolling", element.isEnabled());
    }

    public void printAllVisibleTextValues(Locator locator) {
        System.out.println(locator.allInnerTexts());
    }
}
