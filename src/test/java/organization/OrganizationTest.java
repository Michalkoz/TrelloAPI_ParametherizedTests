package organization;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import trello.Organization;
import utils.Utils;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class OrganizationTest extends Utils {

        String fakeFunnyDisplayName = faker.funnyName().name();
        long fakeNumber = faker.number().randomNumber();

        private static Stream<Arguments> createOrganizationData(){

            return Stream.of(
                    Arguments.of("This is display name", "Testing is super","Test organization","https://www.testing.org.pl"),
                    Arguments.of("This is display name", "Testing is super","Test organization","http://www.testing.org.pl"),
                    Arguments.of("T", "Testing is super","Test organization","http://www.testing.org.pl"),
                    Arguments.of("This is display name", "","Test organization","http://www.testing.org.pl"),
                    Arguments.of("This is display name", "Testing is super","Test organization","123"),
                    Arguments.of("This is display name", "Testing is super","TEST","http://www.testing.org.pl"),
                    Arguments.of("This is display name", "Testing is super","Tes","http://www.testing.org.pl"),
                    Arguments.of("This is display name", "Testing is super","Test_organization","http://www.testing.org.pl"));
        }

        @DisplayName("Create organization with valid data")
        @ParameterizedTest(name = "Display name: {0}, desc: {1}, name: {2}, website: {3}")
        @MethodSource("createOrganizationData")
        public void createNewOrganizationTest(String displayName, String desc, String name, String website) {

            Organization organization = new Organization();
            organization.setDisplayName(displayName);
            organization.setDesc(desc);
            organization.setName(name);
            organization.setWebsite(website);

            Response response = given()
                    .spec(reqSpec)
                    .queryParam("displayName", organization.getDisplayName())
                    .queryParam("desc", organization.getDesc())
                    .queryParam("name", organization.getName())
                    .queryParam("website", organization.getWebsite())
                    .when()
                    .post(BASE_URL + "/" + ORGANIZATION)
                    .then()
                    .statusCode(SC_OK)
                    .extract()
                    .response();

            JsonPath jsonPath = response.jsonPath();
            String organizationId = jsonPath.getString("id");
            assertThat(jsonPath.getString("displayName")).isEqualTo(organization.getDisplayName());

            delete(BASE_URL + "/" + ORGANIZATION + "/" + organizationId);
        }
}
