package utilities;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class DataProviders {

    //Using this structure to be able to add additional test cases to that class
    @DataProvider (name = "requestDemoForm")
    public static Object[][] requestDemoFormDataSet(Method m) {
        Object[][] testdata = null;
        if (m.getName().equalsIgnoreCase(""))
            testdata = new Object[][]{
                    {},
                    {}
            };
        return testdata;
    }
}
