
import org.json.simple.JSONObject;

import org.testng.annotations.Test;
import utilities.Base;

import java.util.HashMap;

public class APIAutomationDemo extends Base {

    //General
    private final int expectedGetStatusCode = 200;

    //OpenWeatherMap
    private final String weatherMapBaseURL = "https://api.openweathermap.org/data/2.5/weather";
    private final String city = "Herzliya,IL";
    private final String openWeatherMapToken = "af1e08d48403c6ebe06bfd23c323c4b4";
    private final String countryPath = "sys.country";
    private final String expectedCountry = "IL";

    //JSONPlaceholder
    private final String jsonPlaceholderBaseURL = "https://jsonplaceholder.typicode.com/posts";
    private final String uniquePath = "body";
    private final int expectedTotalNumberOfRecords = 100;
    private final String expectedBodyTargetText = "mollitia quas illo";
    private final int expectedTargetTextRecordID = 27;
    private final String nonExistingText = "Varonis";
    private final int recordIDToDelete = 50;
    private final int expectedDeleteStatusCode = 200;
    private final int queriedID = 5;
    private final String[] expectedJsonObjectQueriedIDValues = {"1", "nesciunt quas odio", "repudiandae veniam quaerat sunt sed\n" +
            "alias aut fugiat sit autem sed est\n" +
            "voluptatem omnis possimus esse voluptatibus quis\n" +
            "est aut tenetur dolor neque"};


    //Go Rest
    private final String goRestBaseURL = "https://gorest.co.in/public/v2/users";
    private final String goRestToken = "5c5b63b92e7728b54d7b0a0ece03d691b624b9855a5a6015472be0f822d23d54";

    @Test
    public void test01_VerifyMatchingCountryToSelectedCity() {
        String fullApiUrl = weatherMapBaseURL + "?units=metric&q=" + city + "&appid=" + openWeatherMapToken;
        System.out.println(fullApiUrl);
        initAPI(fullApiUrl);
        apiActions.verifySpecificRecordProperty(fullApiUrl, expectedGetStatusCode,
                countryPath, expectedCountry);
    }

    @Test
    public void test02_VerifySeveralValues() {
        initAPI(jsonPlaceholderBaseURL);
        apiActions.verifySeveralProperties(jsonPlaceholderBaseURL + "/" + queriedID, expectedGetStatusCode,
                expectedJsonObjectQueriedIDValues);
    }

    @Test
    public void test03_verifyTotalNumORecords() {
        initAPI(jsonPlaceholderBaseURL);
        apiActions.verifyNumbersOfRecords(jsonPlaceholderBaseURL, expectedGetStatusCode, uniquePath, expectedTotalNumberOfRecords);
    }

    @Test
    public void test04_verifyIDAccordingToTextSearch() {
        initAPI(jsonPlaceholderBaseURL);
        apiActions.verifyWhichRecordIDContainsSpecificValue(jsonPlaceholderBaseURL, expectedGetStatusCode, uniquePath,
                expectedBodyTargetText, expectedTargetTextRecordID);
    }

    @Test
    public void test05_verifySpecificTextIsNotIncludedInAnyRecordBody() {
        initAPI(jsonPlaceholderBaseURL);
        apiActions.verifySpecificTextNotIncluded(jsonPlaceholderBaseURL, expectedGetStatusCode, uniquePath,
                nonExistingText);
    }

    @Test
    public void test06_updatingAnExistingRecord() {
        initAPI(goRestBaseURL);
        HashMap<String, Object> dictionary = new HashMap<>();
        dictionary.put("id", 7508503);
        dictionary.put("status", "active");
        JSONObject parameters = new JSONObject();
        apiActions.put(goRestBaseURL + goRestToken, parameters, dictionary);
    }

    @Test
    public void test07_addingNewRecord() {
        initAPI(goRestBaseURL);
        HashMap<String, Object> dictionary = new HashMap<>();
        dictionary.put("id", 7508504);
        dictionary.put("name", "Moshe Cohen");
        dictionary.put("email", "moshec@hotmail.com");
        dictionary.put("gender", "male");
        dictionary.put("status", "active");
        JSONObject parameters = new JSONObject();
        apiActions.post(goRestBaseURL + goRestToken, parameters, dictionary);
    }

    @Test
    public void test08_deletingAnExistingRecord() {
        initAPI(jsonPlaceholderBaseURL);
        apiActions.deleteSpecificRecord(jsonPlaceholderBaseURL + "/" + recordIDToDelete, expectedDeleteStatusCode);
    }
}
