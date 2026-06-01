package client;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.ServerError;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverURL;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    public AuthData register(UserData user) throws DataAccessException {
        HttpRequest request = buildRequest("POST", "/user", user);
        HttpResponse<String> response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData login(UserData user) throws DataAccessException {
        HttpRequest request = buildRequest("POST", "/session", user);
        HttpResponse<String> response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logout(AuthData auth) throws DataAccessException {
        HttpRequest request = buildRequest("DELETE", "/session", auth);
        HttpResponse<String> response = sendRequest(request);
        handleResponse(response, null);
    }

    public String login() {
        return null;
    }

    public String create() {
        return null;
    }

    public void join() {

    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        HttpRequest.Builder request;
        HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.noBody();
        if (body != null && body.getClass() != AuthData.class) {
            requestBody = HttpRequest.BodyPublishers.ofString(new Gson().toJson(body));
        }
        request = HttpRequest.newBuilder().uri(URI.create(serverURL + path)).method(method, requestBody);
        if (body != null && body.getClass() == AuthData.class) {
            request.setHeader("Authorization", ((AuthData) body).authToken());
        }
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws DataAccessException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new DataAccessException("Server error", ex);
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws DataAccessException {
        int status = response.statusCode();
        if (status / 100 != 2) {
            String body = response.body();
            if (!body.isEmpty()) {
                throw new DataAccessException(new Gson().fromJson(body, body.getClass()));
            }
            throw new DataAccessException("Failure! Status: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }
}
