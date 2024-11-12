import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequest;
import com.spacex.graphql.model.*;
import exception.MyCustomException;
import com.google.common.base.Stopwatch;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.testng.AllureTestNg;
import org.springframework.graphql.client.GraphQlTransportException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.DateUtils;
import utils.SpaceXService;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Listeners({AllureTestNg.class})
public class PocSuite {

    SoftAssert softAssert = new SoftAssert();

    @Test
    @Description("Test to verify the latest launch details")
    public void testLaunchLatestAll() {

        Allure.step("Make the query");
        LaunchLatestQueryRequest request = LaunchLatestQueryRequest.builder().build();

        //all the attributes of a maxDepth=3 in graph.
        Allure.step("Getting all the attributes of a maxDepth=3 in graph.");
        LaunchResponseProjection responseProjection = new LaunchResponseProjection().all$();

        //HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        LaunchTO latestLaunch = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString())
                .retrieve("launchLatest").toEntity(LaunchTO.class).block();
        Allure.addAttachment("Latest launch details", latestLaunch.toString());

        Allure.step("Checking the attributes of the latest launch:");

        softAssert.assertNotNull(latestLaunch.getId(), "Latest launch id is null");
        Allure.step("Latest launch id: " + latestLaunch.getId());

        softAssert.assertNotNull(latestLaunch.getMission_name(), "Mission name is empty");
        Allure.step("Mission name: " + latestLaunch.getMission_name());

        softAssert.assertTrue(DateUtils.isValidDate(latestLaunch.getLaunch_date_local()),
                "Launch date local is not valid");
        Allure.step("Launch date local is valid: " + latestLaunch.getLaunch_date_local());

        softAssert.assertTrue(DateUtils.isValidUnixDateFormat(latestLaunch.getLaunch_date_unix()),
                "Launch date unix is not valid");
        Allure.step("Launch date unix is valid: " + latestLaunch.getLaunch_date_unix());

        softAssert.assertTrue(DateUtils.isValidUTCDateFormat(latestLaunch.getLaunch_date_utc()),
                "Launch date UTC is not valid");
        Allure.step("Launch date UTC is valid: " + latestLaunch.getLaunch_date_utc());

        softAssert.assertAll();
    }

    @Test
    @Description("Test to verify past launches")
    public void testPastLaunches() {

        Allure.step("Make the query");
        LaunchesPastQueryRequest request = new LaunchesPastQueryRequest();

        //return max 100 results
        Allure.step("Setting limit to 100");
        request.setLimit(100);

        //response attributes you want
        LaunchResponseProjection responseProjection = new LaunchResponseProjection()
                .id().is_tentative().launch_date_local().launch_date_unix().launch_date_utc().launch_success()
                .launch_year().mission_id().mission_name().static_fire_date_utc().static_fire_date_unix()
                .tentative_max_precision().upcoming();

        //HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        List<LaunchTO> results = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString())
                .retrieve("launchesPast").toEntityList(LaunchTO.class).block();
        Allure.addAttachment("Past launches", results.toString());

        softAssert.assertEquals(results.size(), 100, "Number of past launches is not 100");
        Allure.step("Number of past launches: " + results.size());

        for (LaunchTO currentLaunch : results) {

            Allure.step("######################################");

            softAssert.assertNotNull(currentLaunch.getId(), "Latest launch id is null");
            Allure.step("Latest launch id: " + currentLaunch.getId());

            softAssert.assertNotNull(currentLaunch.getMission_name(), "Mission name is empty");
            Allure.step("Mission name: " + currentLaunch.getMission_name());

            softAssert.assertTrue(DateUtils.isValidDate(currentLaunch.getLaunch_date_local()),
                    "Launch date local is not valid");
            Allure.step("Launch date local is valid: " + currentLaunch.getLaunch_date_local());

            softAssert.assertTrue(DateUtils.isValidUnixDateFormat(currentLaunch.getLaunch_date_unix()),
                    "Launch date unix is not valid");
            Allure.step("Launch date unix is valid: " + currentLaunch.getLaunch_date_unix());

            softAssert.assertTrue(DateUtils.isValidUTCDateFormat(currentLaunch.getLaunch_date_utc()), "Launch date UTC is not valid");
            Allure.step("Launch date UTC is valid: " + currentLaunch.getLaunch_date_utc());

        }
        softAssert.assertAll();
    }

    @Test
    @Description("Test to verify the latest launch details with some fields")
    public void testLaunchLatestSomeFields() {

        Allure.step("Make the query");
        LaunchLatestQueryRequest request = LaunchLatestQueryRequest.builder().build();

        Allure.step("Getting some fields in graph.");
        LaunchResponseProjection responseProjection = new LaunchResponseProjection().id().mission_id().mission_name();

        //HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        LaunchTO latestLaunch = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString()).retrieve("launchLatest").toEntity(LaunchTO.class).block();
        assert latestLaunch != null;
        Allure.addAttachment("Latest launch details", latestLaunch.toString());

        Allure.step("Checking the attributes of the latest launch:");

        softAssert.assertNotNull(latestLaunch.getId(), "Latest launch id is null");
        Allure.step("Latest launch id: " + latestLaunch.getId());

        softAssert.assertNotNull(latestLaunch.getMission_name(), "Mission name is empty");
        Allure.step("Mission name: " + latestLaunch.getMission_name());

        softAssert.assertNull(latestLaunch.getLaunch_date_local(), "Launch date local is not null");
        Allure.step("Launch date local is null");

        softAssert.assertNull(latestLaunch.getLaunch_date_unix(), "Launch date unix is not null");
        Allure.step("Launch date unix is null");

        softAssert.assertNull(latestLaunch.getLaunch_date_utc(), "Launch date UTC is not null");
        Allure.step("Launch date UTC is null");

        softAssert.assertAll();
    }


    @Test
    @Description("Test to verify all information about the company")
    public void testCompanyAll() {

        Allure.step("Make the query");
        CompanyQueryRequest request = CompanyQueryRequest.builder().build();

        Allure.step("Getting all the attributes of a maxDepth=3 in graph.");
        InfoResponseProjection infoResponseProjection = new InfoResponseProjection().all$();

        //HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, infoResponseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        InfoTO companyInfo = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString()).retrieve("company").toEntity(InfoTO.class).block();
        Allure.addAttachment("Latest launch details", companyInfo.toString());

        softAssert.assertEquals(companyInfo.getCeo(), "Elon Musk");
        Allure.step("CEO: " + companyInfo.getCeo());
        softAssert.assertEquals(companyInfo.getName(), "SpaceX");
        Allure.step("Name: " + companyInfo.getName());
        softAssert.assertEquals(companyInfo.getFounder(), "Elon Musk");
        Allure.step("Founder: " + companyInfo.getFounder());
        softAssert.assertEquals(companyInfo.getValuation(), 74000000000.0);
        Allure.step("Valuation: " + companyInfo.getValuation());
        softAssert.assertEquals(companyInfo.getFounded(), Integer.valueOf(2002));
        Allure.step("Founded: " + companyInfo.getFounded());
        softAssert.assertEquals(companyInfo.getEmployees(), Integer.valueOf(9500));
        Allure.step("Employees: " + companyInfo.getEmployees());

        softAssert.assertAll();
    }

    @Test(priority = 1)
    @Description("Test to find past launches by year")
    public void testFindPastLaunchesByYear() {

        String yearAsString = "2022";

        Allure.step("Make the query");
        LaunchesPastQueryRequest request = new LaunchesPastQueryRequest();

        LaunchFindTO launchFindTO = new LaunchFindTO();
        launchFindTO.setLaunch_year(yearAsString);
        request.setFind(launchFindTO);
        Allure.step("Setting launch year to " + yearAsString);

        //response attributes you want
        LaunchResponseProjection responseProjection = new LaunchResponseProjection()
                .id().mission_name().launch_year();

        //HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        List<LaunchTO> results = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString())
                .retrieve("launchesPast").toEntityList(LaunchTO.class).block();
        Allure.addAttachment("Past launches", results.toString());

        for (LaunchTO currentLaunch : results) {

            Allure.step("######################################");

            softAssert.assertNotNull(currentLaunch.getId(), "Latest launch id is null");
            Allure.step("Latest launch id: " + currentLaunch.getId());

            softAssert.assertEquals(currentLaunch.getLaunch_year(), yearAsString);
            Allure.step("Launch year: " + currentLaunch.getLaunch_year());
        }

        softAssert.assertAll();
    }

    @Test
    @Description("Test to verify behavior with invalid field names")
    public void testNegativeInvalidFieldNames() {

        Allure.step("Make the query");
        LaunchLatestQueryRequest request = LaunchLatestQueryRequest.builder().build();

        Allure.step("Getting invalid fields in graph.");

        //bad query on purpose to get 400 response
        VolumeResponseProjection responseProjection = new VolumeResponseProjection().cubic_feet();

        //HTTP request bod
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        try {
            SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString()).retrieve("launchLatest").toEntity(LaunchTO.class).block();
            softAssert.fail("Expected an exception due to invalid field names");

        } catch (GraphQlTransportException graphQlTransportException) {
            if(graphQlTransportException.getCause() instanceof MyCustomException){
                MyCustomException myCustomException= (MyCustomException) graphQlTransportException.getCause();
                System.out.println("Failed with status code " + myCustomException.getHttpStatusCode());
                System.out.println("Failed with body " + myCustomException.getBody());
                Allure.step("Caught expected exception: " + myCustomException.getHttpStatusCode());
                Allure.addAttachment("Failed body", myCustomException.getBody());
            }

        } catch (Exception e) {
            softAssert.fail("Expected an exception due to invalid field names");
        }

        softAssert.assertAll();
    }


    @Test
    @Description("Test to verify behavior with invalid variables")
    public void testNegativeInvalidVariables() {

        Allure.step("Make the query");
        LaunchesPastQueryRequest request = new LaunchesPastQueryRequest();
        Allure.step("Setting invalid limit to 100");
        request.setLimit(100);

        Allure.step("Getting all the attributes of a maxDepth=3 in graph.");
        InfoResponseProjection infoResponseProjection = new InfoResponseProjection().all$();

        //HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, infoResponseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        try {
            SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString()).retrieve("launchLatest").toEntity(LaunchTO.class).block();
            softAssert.fail("Expected an exception due to invalid field names");

        } catch (GraphQlTransportException graphQlTransportException) {
            if(graphQlTransportException.getCause() instanceof MyCustomException){
                MyCustomException myCustomException= (MyCustomException) graphQlTransportException.getCause();
                System.out.println("Failed with status code " + myCustomException.getHttpStatusCode());
                System.out.println("Failed with body " + myCustomException.getBody());
                Allure.step("Caught expected exception: " + myCustomException.getHttpStatusCode());
                Allure.addAttachment("Failed body", myCustomException.getBody());
            }

        } catch (Exception e) {
            softAssert.fail("Expected an exception due to invalid field names");
        }

        softAssert.assertAll();
    }

    @Test
    @Description("Test to verify behavior with omitted required fields")
    public void testNegativeOmittedRequiredFields() {

        Allure.step("Make the query");
        CompanyQueryRequest request = CompanyQueryRequest.builder().build();

        Allure.step("Getting no fields in graph.");
        InfoResponseProjection infoResponseProjection = new InfoResponseProjection();

        //HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, infoResponseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        try {
            SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString()).retrieve("launchLatest").toEntity(LaunchTO.class).block();
            softAssert.fail("Expected an exception due to invalid field names");

        } catch (GraphQlTransportException graphQlTransportException) {
            if(graphQlTransportException.getCause() instanceof MyCustomException){
                MyCustomException myCustomException= (MyCustomException) graphQlTransportException.getCause();
                System.out.println("Failed with status code " + myCustomException.getHttpStatusCode());
                System.out.println("Failed with body " + myCustomException.getBody());
                Allure.step("Caught expected exception: " + myCustomException.getHttpStatusCode());
                Allure.addAttachment("Failed body", myCustomException.getBody());
            }

        } catch (Exception e) {
            softAssert.fail("Expected an exception due to invalid field names");
        }
        softAssert.assertAll();
    }

    @Test
    @Description("Performance test to verify response time of latest launch details")
    public void testLaunchLatestPerformance() {
        Allure.step("Make the query");
        LaunchLatestQueryRequest request = LaunchLatestQueryRequest.builder().build();

        Allure.step("Getting only id attribute in graph.");
        LaunchResponseProjection responseProjection = new LaunchResponseProjection().id();

        // HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        Stopwatch stopwatch = Stopwatch.createStarted();
        LaunchTO latestLaunch = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString())
                .retrieve("launchLatest").toEntity(LaunchTO.class).block();
        stopwatch.stop();

        long responseTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        Allure.addAttachment("Response time", responseTime + " ms");

        // performance target is 5 seconds (Checked 10 execution and average response time is 4.2 seconds)
        softAssert.assertTrue(responseTime < 5000, "Response time for only one attribute is too slow");
        Allure.step("Response time or only one attribute: " + responseTime + " ms");

        Allure.step("Getting all the attributes of a maxDepth=3 in graph.");
        responseProjection = new LaunchResponseProjection().all$();

        // HTTP request body
        graphQLRequest = new GraphQLRequest(request, responseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        stopwatch = Stopwatch.createStarted();
        latestLaunch = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString())
                .retrieve("launchLatest").toEntity(LaunchTO.class).block();
        stopwatch.stop();

        long responseTimeAll = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        Allure.addAttachment("Response time", responseTime + " ms");

        // performance target is 10 seconds (Checked 10 execution and average response time is 8.2 seconds)
        softAssert.assertTrue(responseTimeAll < 10000, "Response time is too slow");
        Allure.step("Response time: " + responseTimeAll + " ms");

        softAssert.assertAll();
    }

    @Test
    @Description("Test to verify if mission name 'SES-9' is displayed in response")
    public void testMissionNameSES9() {
        Allure.step("Make the query");
        LaunchesPastQueryRequest request = new LaunchesPastQueryRequest();

        // return max 100 results
        Allure.step("Setting limit to 100");
        request.setLimit(100);

        // response attributes you want
        LaunchResponseProjection responseProjection = new LaunchResponseProjection()
                .id().mission_name().launch_date_utc();

        // HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        List<LaunchTO> results = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString())
                .retrieve("launchesPast").toEntityList(LaunchTO.class).block();
        Allure.addAttachment("Past launches", results.toString());

        boolean missionFound = results.stream()
                .anyMatch(launch -> "SES-9".equals(launch.getMission_name()));

        softAssert.assertTrue(missionFound, "Mission name 'SES-9' not found in response");
        Allure.step("Mission name 'SES-9' found in response");

        softAssert.assertAll();
    }

    @Test
    @Description("Test to verify Capsule query with empty id")
    public void testNegativeCapsuleQueryWithEmptyId() {

        Allure.step("Make the query");
        CapsulesQueryRequest request = new CapsulesQueryRequest();

        Allure.step("Getting all the attributes of a maxDepth=3 in graph.");
        CapsuleResponseProjection responseProjection = new CapsuleResponseProjection().id("");

        // HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        try {
            SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString()).retrieve("capsules").toEntityList(CapsuleTO.class).block();
            softAssert.fail("Expected an exception due to invalid empty id");
        } catch (GraphQlTransportException graphQlTransportException) {
            if(graphQlTransportException.getCause() instanceof MyCustomException){
                MyCustomException myCustomException= (MyCustomException) graphQlTransportException.getCause();
                System.out.println("Failed with status code " + myCustomException.getHttpStatusCode());
                System.out.println("Failed with body " + myCustomException.getBody());
                Allure.step("Caught expected exception: " + myCustomException.getHttpStatusCode());
                Allure.addAttachment("Failed body", myCustomException.getBody());
            }
        } catch (Exception e) {
            softAssert.fail("Expected an exception due to invalid field names");
        }
        softAssert.assertAll();
    }

    @Test
    @Description("Test to verify all rockets information")
    public void testRocketsAll() {

        Allure.step("Make the query");
        RocketsQueryRequest request = RocketsQueryRequest.builder().build();

        Allure.step("Getting all the attributes of a maxDepth=3 in graph.");
        RocketResponseProjection responseProjection = new RocketResponseProjection().all$();

        // HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        List<RocketTO> rockets = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString())
                .retrieve("rockets").toEntityList(RocketTO.class).block();
        Allure.addAttachment("Rockets details", rockets.toString());

        for (RocketTO rocket : rockets) {
            Allure.step("######################################");

            softAssert.assertNotNull(rocket.getId(), "Rocket id is null");
            Allure.step("Rocket id: " + rocket.getId());

            softAssert.assertNotNull(rocket.getName(), "Rocket name is empty");
            Allure.step("Rocket name: " + rocket.getName());

            softAssert.assertNotNull(rocket.getType(), "Rocket type is empty");
            Allure.step("Rocket type: " + rocket.getType());
        }

        softAssert.assertAll();
    }

@Test
@Description("Test to verify limit and pagination for past launches with offset 10")
public void testLimitAndPaginationWithOffset10() {
    int limit = 10;
    int offset = 10;

    Allure.step("Make the query with limit and offset");
    LaunchesPastQueryRequest request = new LaunchesPastQueryRequest();
    request.setLimit(limit);
    request.setOffset(offset);

    Allure.step("Setting limit to " + limit + " and offset to " + offset);
    LaunchResponseProjection responseProjection = new LaunchResponseProjection()
            .id().mission_name().launch_date_utc();

    // HTTP request body
    GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
    Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

    List<LaunchTO> results = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString())
            .retrieve("launchesPast").toEntityList(LaunchTO.class).block();
    Allure.addAttachment("Past launches", results.toString());

    softAssert.assertEquals(results.size(), limit, "Number of past launches is not " + limit);
    Allure.step("Number of past launches: " + results.size());

    for (LaunchTO currentLaunch : results) {
        Allure.step("######################################");

        softAssert.assertNotNull(currentLaunch.getId(), "Launch id is null");
        Allure.step("Launch id: " + currentLaunch.getId());

        softAssert.assertNotNull(currentLaunch.getMission_name(), "Mission name is empty");
        Allure.step("Mission name: " + currentLaunch.getMission_name());

        softAssert.assertTrue(DateUtils.isValidUTCDateFormat(currentLaunch.getLaunch_date_utc()), "Launch date UTC is not valid");
        Allure.step("Launch date UTC is valid: " + currentLaunch.getLaunch_date_utc());
    }

    softAssert.assertAll();
}


@Test
@Description("Test to validate MissionTO")
public void testValidateMissionTO() {
    Allure.step("Make the query");
    MissionQueryRequest request = MissionQueryRequest.builder().build();

    Allure.step("Getting all the attributes of a maxDepth=3 in graph.");
    MissionResponseProjection responseProjection = new MissionResponseProjection().all$();

    // HTTP request body
    GraphQLRequest graphQLRequest = new GraphQLRequest(request, responseProjection);
    Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

    try {
        SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString())
                .retrieve("mission").toEntity(MissionTO.class).block();
        softAssert.fail("Expected an exception due to invalid empty id");
    } catch (GraphQlTransportException graphQlTransportException) {
        if(graphQlTransportException.getCause() instanceof MyCustomException){
            MyCustomException myCustomException= (MyCustomException) graphQlTransportException.getCause();
            System.out.println("Failed with status code " + myCustomException.getHttpStatusCode());
            System.out.println("Failed with body " + myCustomException.getBody());
            Allure.step("Caught expected exception: " + myCustomException.getHttpStatusCode());
            Allure.addAttachment("Failed body", myCustomException.getBody());
        }
    } catch (Exception e) {
        softAssert.fail("Expected an exception due to invalid field names");
    }
    softAssert.assertAll();
    }
}