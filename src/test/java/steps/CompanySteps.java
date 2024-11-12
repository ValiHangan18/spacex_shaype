package steps;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequest;
import com.spacex.graphql.model.CompanyQueryRequest;
import com.spacex.graphql.model.InfoResponseProjection;
import com.spacex.graphql.model.InfoTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.qameta.allure.Allure;
import org.testng.asserts.SoftAssert;
import utils.SpaceXService;

public class CompanySteps {

    CompanyQueryRequest request;
    InfoResponseProjection infoResponseProjection;
    InfoTO companyInfo;
    SoftAssert softAssert = new SoftAssert();

    @Given("I want to check company information")
    public void iWantToCheckCompanyInformation() {
        Allure.step("I want to check company information");
        request = CompanyQueryRequest.builder().build();
        //response attributes you want
        infoResponseProjection = new InfoResponseProjection().all$();
    }

    @When("I run the query")
    public void iRunTheQuery() {
        Allure.step("I run the query");
        //HTTP request body
        GraphQLRequest graphQLRequest = new GraphQLRequest(request, infoResponseProjection);
        Allure.addAttachment("graphQLRequest body", graphQLRequest.toQueryString());

        companyInfo = SpaceXService.getSpaceXClient().document(graphQLRequest.toQueryString()).retrieve("company").toEntity(InfoTO.class).block();
        Allure.addAttachment("Company information", companyInfo.toString());
    }

    @Then("I should see the company information {string} {string} {string} {double} {int} {int}")
    public void iShouldSeeTheCompanyInformation(String ceo, String name, String founder, Double valuation, Integer founded, Integer employees) {
        Allure.step("I should see the company information:");

        softAssert.assertEquals(companyInfo.getCeo(), ceo);
        Allure.step("CEO: " + companyInfo.getCeo());
        softAssert.assertEquals(companyInfo.getName(), name);
        Allure.step("Name: " + companyInfo.getName());
        softAssert.assertEquals(companyInfo.getFounder(), founder);
        Allure.step("Founder: " + companyInfo.getFounder());
        softAssert.assertEquals(companyInfo.getValuation(), valuation);
        Allure.step("Valuation: " + companyInfo.getValuation());
        softAssert.assertEquals(companyInfo.getFounded(), founded);
        Allure.step("Founded: " + companyInfo.getFounded());
        softAssert.assertEquals(companyInfo.getEmployees(), employees);
        Allure.step("Employees: " + companyInfo.getEmployees());

        softAssert.assertAll();    }

}