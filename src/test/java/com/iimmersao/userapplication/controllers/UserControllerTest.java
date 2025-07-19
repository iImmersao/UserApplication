package com.iimmersao.userapplication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iimmersao.userapplication.Main;
import com.iimmersao.userapplication.models.User;
import org.junit.jupiter.api.*;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static com.iimmersao.userapplication.utils.HttpTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    private static final int PORT = 8081;
    private static final String BASE_URL = "http://localhost:" + PORT;

    private static String createdUserId;

    private static Thread serverThread;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void startServer() {
        serverThread = new Thread(() -> {
            try {
                Main.main(new String[]{});  // Launches the application
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        serverThread.setDaemon(true); // Important: allows tests to exit even if server is running
        serverThread.start();

        // Optionally: Wait briefly to ensure the server has time to start
        try {
            Thread.sleep(5000); // adjust if necessary
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterAll
    public static void stopServer() {
        serverThread.interrupt();
    }

    @Test
    @Order(1)
    void shouldCreateUser() throws Exception {
        String body = """
                {
                  "username": "testuser",
                  "email": "test@example.com"
                }
                """;

        HttpResponse<String> response = post(BASE_URL + "/users", body);

        String responseJson = response.body(); // e.g. from HTTP client
        User user = objectMapper.readValue(responseJson, User.class);

        assertEquals(200, response.statusCode());
        assertEquals("1", user.getId());

        createdUserId = extractIdFromResponse(user.getId());
        assertNotNull(createdUserId);
    }

    @Test
    @Order(2)
    void shouldFetchUserById() throws Exception {
        HttpResponse<String> response = get(BASE_URL + "/users/" + createdUserId);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("testuser"));
    }

    @Test
    @Order(3)
    void shouldReturnUserListWithPagination() throws Exception {
        HttpResponse<String> response = get(BASE_URL + "/users?page=0&size=10");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("testuser")); // JSON array
    }

    @Test
    @Order(4)
    void shouldUpdateUser() throws Exception {
        String updatedBody = """
                {
                  "username": "updateduser",
                  "email": "updated@example.com"
                }
                """;

        HttpResponse<String> response = put(BASE_URL + "/users/" + createdUserId, updatedBody);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("User updated with ID"));
    }

    @Test
    @Order(5)
    void shouldPatchUser() throws Exception {
        String patchBody = """
                {
                  "email": "patched@example.com"
                }
                """;

        HttpResponse<String> response = patch(BASE_URL + "/users/" + createdUserId, patchBody);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("User patched with ID"));
    }

    @Test
    @Order(6)
    void shouldDeleteUser() throws Exception {
        HttpResponse<String> response = delete(BASE_URL + "/users/" + createdUserId);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("User deleted with ID"));
    }

    @Test
    @Order(7)
    void shouldReturnNotFoundForDeletedUser() throws Exception {
        HttpResponse<String> response = get(BASE_URL + "/users/" + createdUserId);
        assertEquals(200, response.statusCode());

        String responseJson = response.body(); // e.g. from HTTP client
        HttpHeaders headers = response.headers();
        Map<String, List<String>> headerFields = headers.map();
        List<String> field = headerFields.get("content-type");
        assertEquals("text/plain", field.getFirst());
        assertEquals("", responseJson);
    }

    @Test
    @Order(8)
    void shouldDeleteAllUsers() throws Exception {
        // Create a user first
        String body = """
                {
                  "username": "anotheruser",
                  "email": "another@example.com"
                }
                """;
        post(BASE_URL + "/users", body);

        // Then delete all
        HttpResponse<String> response = delete(BASE_URL + "/users");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("All users deleted"));
    }

    private static String extractIdFromResponse(String response) {
        // Parses "User created with ID: <id>"
        return response.replaceAll(".*ID:\\s*", "").trim();
    }
}
