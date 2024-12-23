package pages;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class LambdaTestApiClient {
    private static final String BASE_URL = "https://ecommerce-playground.lambdatest.io";
    
    public Response searchProducts(String searchTerm) {
        return given()
                .baseUri(BASE_URL)
                .queryParam("route", "product/search")
                .queryParam("search", searchTerm)
                .when()
                .get("/index.php")
                .then()
                .extract()
                .response();
    }
    
    public Response getCategoryProducts(String categoryPath) {
        return given()
                .baseUri(BASE_URL)
                .queryParam("route", "product/category")
                .queryParam("path", categoryPath)
                .when()
                .get("/index.php")
                .then()
                .extract()
                .response();
    }
}
