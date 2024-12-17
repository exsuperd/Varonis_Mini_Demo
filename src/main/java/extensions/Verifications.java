package extensions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class Verifications {

    public final Page page;

    public Verifications(Page page) {
        this.page = page;
    }


    //List verifications

    public static void verifyGivenStringContainsAllStringArrayValues(String testedString, String[] expectedValues) {
        for (String value : expectedValues) {
            assertTrue(testedString.contains(value));
        }
    }

    public void verifyCorrectTextInEachListIndex(List<Locator> list, String[] expectedValues) {
        for (int i = 0; i < list.size(); i++) {
            assertEquals(expectedValues[i], (list.get(i).innerText()));
        }
    }

    public void verifyCorrectTextInEachStringListIndex(List<String> list, String[] expectedValues) {
        for (int i = 0; i < list.size(); i++) {
            assertEquals(expectedValues[i], (list.get(i)));
        }
    }

    public void verifyExpectedTextIsIncludedInEachListIndex(List<Locator> list, String[] expectedValues) {
        for (int i = 0; i < list.size(); i++) {
            assertTrue(list.get(i).innerText().contains(expectedValues[i]));
            System.out.println("Match found!");
        }
    }

    public void verifyExpectedTextIsIncludedInEachStringListIndex(List<String> list, String[] expectedValues) {
        for (int i = 0; i < list.size(); i++) {
            assertTrue(list.get(i).contains(expectedValues[i]));
            System.out.println("Match found!");
        }
    }

    public static void verifyAListDoesNotContainsCertainValues(List<Locator> list, String[] nonExpectedValues) {
        for (int i = 0; i < list.size(); i++) {
            assertNotEquals(list.get(i).innerText(), nonExpectedValues[i]);
        }
    }

    public void verifyVisibilityOfAllListIndex(List<Locator> list) {
        for (Locator locator : list) {
            assertTrue(locator.isVisible());
        }
    }

    public void verifyInvisibilityOfAllListIndex(List<Locator> list) {
        for (Locator locator : list) {
            assertFalse(locator.isVisible());
        }
    }

    public void attributeValueInEachListIndex(List<Locator> list, String attribute, String[] expectedValues) {
        for (int i = 0; i < list.size(); i++) {
            assertEquals(expectedValues[i], (list.get(i).getAttribute(attribute)));
        }
    }

    public void allListWebElementsAreEnabled(List<Locator> list) {
        for (Locator locator : list) {
            assertTrue(locator.isEnabled());
        }
    }

    public void allListWebElementsAreDisabled(List<Locator> list) {
        for (Locator locator : list) {
            assertFalse(locator.isEnabled());
        }
    }

    public void allElementsAreEmpty(List<Locator> list) {
        for (Locator locator : list) {
            assertTrue((locator.getAttribute("value").isEmpty()));
        }
    }

    public void allElementsAreNotEmpty(List<Locator> list) {
        for (Locator locator : list) {
            assertFalse((locator.getAttribute("value").isEmpty()));
        }
    }

    public void listSize(List<Locator> list, int expectedListSize) {
        System.out.println("List size is: " + list.size());
        assertEquals(expectedListSize, list.size());
    }

    public void stringListSize(List<String> list, int expectedSize) {
        {
            System.out.println("The actual size of the list is " + list.size());
            assertTrue((list.size() == expectedSize));
        }
    }

    //In case all list indexes have the same text  - after sorting
    public void repeatableTextInEachListIndex(List<Locator> list, String repeatableText) {
        for (Locator locator : list) {
            assertTrue(locator.innerText().equalsIgnoreCase(repeatableText));
        }
    }

    public void repeatableTextInEachInputListIndex(List<Locator> list, String repeatableText) {
        for (Locator locator : list) {
            assertTrue(locator.getAttribute("value").equalsIgnoreCase(repeatableText));
        }
    }

    public void getAndVerifyColorOfEachWebElement(List<Locator> findMyColorList, String[] ExpectedRGBAValues) {
        for (int i = 0; i < findMyColorList.size(); i++) {
            String elementColor = findMyColorList.get(i).evaluate("elementColor => getComputedStyle(elementColor)." +
                    "('color')").toString();
            System.out.println("Element color is " + elementColor);
            assertEquals(ExpectedRGBAValues[i], elementColor);
        }
    }

    public void getAndVerifyBackgroundColorOfEachWebElement(List<Locator> findMyBackgroundColorList, String[] ExpectedRGBAValues) {
        for (int i = 0; i < findMyBackgroundColorList.size(); i++) {
            String elementBackgroundColor = findMyBackgroundColorList.get(i).evaluate("elementBackgroundColor => getComputedStyle(elementColor)." +
                    "('backgroundColor')").toString();
            System.out.println("Element background color is " + elementBackgroundColor);
            assertEquals(ExpectedRGBAValues[i], elementBackgroundColor);
        }
    }

    public void verifyPDAnnotationsListSize(List<PDAnnotation> list, int expectedSize) {
        {
            System.out.println("The actual size of the list is " + list.size());
            assertTrue((list.size() == expectedSize));
        }
    }

    //useful when all hovered item has the same color
    public void getAndVerifyHoveredColorOfLocatorsList(List<Locator> list, String expectedHoveredColor) {
        List<String> hoveredColors = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).hover();
            page.waitForTimeout(500);
            hoveredColors.add(list.get(i).evaluate
                    ("element=>window.getComputedStyle(element).color").toString());
            assertEquals(expectedHoveredColor, hoveredColors.get(i));
        }
        System.out.println("Hover colors are: " + hoveredColors);
    }

    public void getAndVerifyHoveredBackgroundColorOfLocatorsList(List<Locator> list, String[] expectedHoveredLocatorColors) {
        List<String> hoveredBackgroundColors = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).hover();
            page.waitForTimeout(500);
            hoveredBackgroundColors.add(list.get(i).evaluate
                    ("element=>window.getComputedStyle(element).backgroundColor").toString());
            assertEquals(expectedHoveredLocatorColors[i], hoveredBackgroundColors.get(i));
        }
        System.out.println("Hover colors are: " + hoveredBackgroundColors);
    }

// Single element verifications

    public void getAndVerifyLocatorColor(Locator locator, String expectedColor) {
        String locatorColor = locator.evaluate
                ("element=>window.getComputedStyle(element).color").toString();
        System.out.println("Locator color is: " + locatorColor);
        assertEquals(expectedColor, locatorColor);
    }

    public void getAndVerifyLocatorBackgroundColor(Locator locator, String expectedBackGroundColor) {
        String locatorBackgroundColor = locator.evaluate
                ("element=>window.getComputedStyle(element).backgroundColor").toString();
        System.out.println("Locator color is: " + locatorBackgroundColor);
        assertEquals(expectedBackGroundColor, locatorBackgroundColor);
    }

    public void getAndVerifyLocatorFontSize(Locator locator, String expectedFontSize) {
        String locatorFontSize = locator.evaluate
                ("element=>window.getComputedStyle(element).fontSize").toString();
        System.out.println("Locator color is: " + locatorFontSize);
        assertEquals(expectedFontSize, locatorFontSize);
    }

    public void getAndVerifyHoveredLocatorColor(Locator locator, String expectedHoveredLocatorColor) {
        String defaultLocatorColor = locator.evaluate
                ("element=>window.getComputedStyle(element).color").toString();
        System.out.println("Default Locator color is: " + defaultLocatorColor);
        locator.hover();
        page.waitForTimeout(500);
        String hoveredLocatorColor = locator.evaluate
                ("element=>window.getComputedStyle(element).color").toString();
        System.out.println("Hovered locator color is: " + hoveredLocatorColor);
        assertEquals(expectedHoveredLocatorColor, hoveredLocatorColor);
        assertNotEquals(defaultLocatorColor, hoveredLocatorColor);
    }

    public void getAndVerifyHoveredLocatorBackgroundColor(Locator locator, String expectedHoveredLocatorBackgroundColor) {
        String defaultLocatorBackgroundColor = locator.evaluate
                ("element=>window.getComputedStyle(element).backgroundColor").toString();
        System.out.println("Default Locator color is: " + defaultLocatorBackgroundColor);
        locator.hover();
        page.waitForTimeout(500);
        String hoveredLocatorBackgroundColor = locator.evaluate
                ("element=>window.getComputedStyle(element).color").toString();
        System.out.println("Hovered locator background color is: " + hoveredLocatorBackgroundColor);
        assertEquals(expectedHoveredLocatorBackgroundColor, hoveredLocatorBackgroundColor);
        assertNotEquals(defaultLocatorBackgroundColor, hoveredLocatorBackgroundColor);
    }
}
