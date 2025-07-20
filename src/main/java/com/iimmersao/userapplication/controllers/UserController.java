package com.iimmersao.userapplication.controllers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.iimmersao.springmimic.annotations.*;
import com.iimmersao.springmimic.client.RestClient;
import com.iimmersao.springmimic.web.PageRequest;
import com.iimmersao.userapplication.models.User;
import com.iimmersao.userapplication.services.UserService;
import fi.iki.elonen.NanoHTTPD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    RestClient restClient;

    @Inject
    private UserService userService;

    @GetMapping("/external")
    public String callExternalService() throws Exception {
        // For example, proxying a public JSON API
        Post post = restClient.get("https://jsonplaceholder.typicode.com/posts/1", Post.class);
        return "Fetched post: " + post.title;
    }

    public static class Post {
        public int userId;
        public int id;
        public String title;
        public String body;
    }

    @PostMapping("/users")
    @ResponseBody
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") String id) {
        User result = null;
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            result = user.get();
        }
        return result;
    }

    //@GetMapping("/users/email/{address}")

    @GetMapping("/users")
    @ResponseBody
    public List<User> getUsers(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            NanoHTTPD.IHTTPSession session
    ) {
        // Extract all query params
        Map<String, List<String>> rawQueryParams = decodeQueryParams(session.getQueryParameterString());

        // Known paging/sorting params
        Set<String> knownKeys = Set.of("page", "size", "sortBy");

        // Filter out paging/sorting from filters
        Map<String, Object> filters = rawQueryParams.entrySet().stream()
                .filter(entry -> !knownKeys.contains(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getFirst()) // use first value
                );

        PageRequest pageRequest = new PageRequest(
                page != null ? page : 0,
                size != null ? size : 10,
                sortBy,
                filters
        );

        return userService.findAll(pageRequest);
    }

    public Map<String, List<String>> decodeQueryParams(String queryString) {
        Map<String, List<String>> params = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) return params;

        for (String param : queryString.split("&")) {
            String[] pair = param.split("=", 2);
            String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
            String value = pair.length > 1 ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8) : "";

            params.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
        return params;
    }

    @PutMapping("/users/{id}")
    public String updateUser(@PathVariable("id") String id, @RequestBody User updated) {
        updated.setId(id);
        userService.update(updated);
        return "User updated with ID: " + id;
    }

    @PatchMapping("/users/{id}")
    public String patchUser(@PathVariable("id") String id, @RequestBody User patch) {
        Optional<User> existingOpt = userService.findById(id);
        if (existingOpt.isEmpty()) {
            return "User not found";
        }

        User existing = existingOpt.get();

        // Manually merge fields — or use a helper method
        if (patch.getUsername() != null) {
            existing.setUsername(patch.getUsername());
        }
        if (patch.getEmail() != null) {
            existing.setEmail(patch.getEmail());
        }

        userService.update(existing);
        return "User patched with ID: " + id;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        userService.deleteById(id);
        return "User deleted with ID: " + id;
    }

    @DeleteMapping("/users")
    public String deleteAllUsers() {
        userService.deleteAll();
        return "All users deleted";
    }

}

