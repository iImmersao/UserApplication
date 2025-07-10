package com.iimmersao.userapplication.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpHeaders;
import java.util.Map;

public class HttpTestUtils {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static HttpResponse<String> sendRequest(String url, String method, String body, Map<String, String> headers) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method(method.toUpperCase(), body != null ? BodyPublishers.ofString(body) : BodyPublishers.noBody());

        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();
        return client.send(request, BodyHandlers.ofString());
    }

    public static HttpResponse<String> get(String url) throws Exception {
        return sendRequest(url, "GET", null, null);
    }

    public static HttpResponse<String> post(String url, String body) throws Exception {
        return sendRequest(url, "POST", body, Map.of("Content-Type", "application/json"));
    }

    public static HttpResponse<String> put(String url, String body) throws Exception {
        return sendRequest(url, "PUT", body, Map.of("Content-Type", "application/json"));
    }

    public static HttpResponse<String> patch(String url, String body) throws Exception {
        return sendRequest(url, "PATCH", body, Map.of("Content-Type", "application/json"));
    }

    public static HttpResponse<String> delete(String url) throws Exception {
        return sendRequest(url, "DELETE", null, null);
    }

    // Optional helper if you want to send a custom header
    public static HttpResponse<String> withAuth(String url, String method, String body, String basicAuth) throws Exception {
        return sendRequest(url, method, body, Map.of(
                "Authorization", "Basic " + basicAuth,
                "Content-Type", "application/json"
        ));
    }
}
