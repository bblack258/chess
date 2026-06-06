package client;

import com.google.gson.Gson;
import dataaccesserrors.DataAccessException;
import dataaccesserrors.ErrorMessage;
import model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverURL;
    private AuthData auth;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    public AuthData register(UserData user) throws DataAccessException {
        HttpRequest request = buildRequest("POST", "/user", user);
        HttpResponse<String> response = sendRequest(request);
        AuthData auth = handleResponse(response, AuthData.class);
        this.auth = auth;
        return auth;
    }

    public AuthData login(UserData user) throws DataAccessException {
        HttpRequest request = buildRequest("POST", "/session", user);
        HttpResponse<String> response = sendRequest(request);
        AuthData auth = handleResponse(response, AuthData.class);
        this.auth = auth;
        return auth;
    }

    public void logout() throws DataAccessException {
        HttpRequest request = buildRequest("DELETE", "/session", null);
        HttpResponse<String> response = sendRequest(request);
        handleResponse(response, null);
    }

    public GameList listGames() throws DataAccessException {
        HttpRequest request = buildRequest("GET", "/game", null);
        HttpResponse<String> response = sendRequest(request);
        GameListResult result = handleResponse(response, GameListResult.class);
        if (result != null) {
            return result.games();
        }
        return null;
    }

    public String create(String gameName) throws DataAccessException {
        HttpRequest request = buildRequest("POST", "/game", new GameData(0, null,
                null, gameName, null, false));
        HttpResponse<String> response = sendRequest(request);
        handleResponse(response, GameData.class);
        return gameName;
    }

    public void join(JoinRequest joinRequest) throws DataAccessException {
        HttpRequest request = buildRequest("PUT", "/game", joinRequest);
        HttpResponse<String> response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        HttpRequest.Builder request;
        HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.noBody();
        if (body != null) {
            requestBody = HttpRequest.BodyPublishers.ofString(new Gson().toJson(body));
        }
        request = HttpRequest.newBuilder().uri(URI.create(serverURL + path)).method(method, requestBody);
        if (auth != null) {
            request.setHeader("Authorization", auth.authToken());
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
            throw new DataAccessException("Server error: " + ex.getMessage(), ex);
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws DataAccessException {
        int status = response.statusCode();
        if (status / 100 != 2) {
            String body = response.body();
            if (!body.isEmpty()) {
                throw new DataAccessException(new Gson().fromJson(body, ErrorMessage.class).message());
            }
            throw new DataAccessException("Failure! Status: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

}
