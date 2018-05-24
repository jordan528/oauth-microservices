package com.qualica.test;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@Ignore
public class AuthorizationServerLiveTest {

    @Test
    public void whenObtainingAccessToken_thenOK() {
        final Response authServerResponse = obtainAccessToken("fooClientIdPassword", "secret", "john", "123");
        final String accessToken = authServerResponse.jsonPath().getString("access_token");
        assertNotNull(accessToken);
    }

    //

    private Response obtainAccessToken(final String clientId, final String clientSecret, final String username, final String password) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        return RestAssured.given().auth().preemptive().basic(clientId, clientSecret).and().with().params(params).when().post("http://localhost:8081/oauth-authorization-server/oauth/token");
        // response.jsonPath().getString("refresh_token");
        // response.jsonPath().getString("access_token")
    }

    private String obtainRefreshToken(String clientId, final String refreshToken) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "refresh_token");
        params.put("client_id", clientId);
        params.put("refresh_token", refreshToken);
        final Response response = RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when().post("http://localhost:8081/oauth-authorization-server/oauth/token");
        return response.jsonPath().getString("access_token");
    }

    private void authorizeClient(String clientId) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("response_type", "code");
        params.put("client_id", clientId);
        params.put("scope", "read,write");
        final Response response = RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when().post("http://localhost:8081/oauth-authorization-server/oauth/authorize");
    }
}