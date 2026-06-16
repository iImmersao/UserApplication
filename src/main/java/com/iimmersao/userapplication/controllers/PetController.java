package com.iimmersao.userapplication.controllers;

import com.iimmersao.springmimic.annotations.*;
import com.iimmersao.springmimic.client.RestClient;
import com.iimmersao.springmimic.web.PageRequest;
import com.iimmersao.userapplication.models.Pet;
import com.iimmersao.userapplication.models.PetUser;
import com.iimmersao.userapplication.services.CombinedService;
import com.iimmersao.userapplication.services.PetService;
import com.iimmersao.userapplication.services.UserService;
import fi.iki.elonen.NanoHTTPD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class PetController {
    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    @Inject
    RestClient restClient;

    @Inject
    private PetService petService;

    @Inject
    private UserService userService;

    @Autowired
    private CombinedService combinedService;

    @GetMapping("/external")
    public String callExternalService() throws Exception {
        // For example, proxying a public JSON API
        PetController.Post post = restClient.get("https://jsonplaceholder.typicode.com/posts/1", PetController.Post.class);
        return "Fetched post: " + post.title;
    }

    public static class Post {
        public int userId;
        public int id;
        public String title;
        public String body;
    }

    @PostMapping("/pets")
    @ResponseBody
    public Pet createPet(@RequestBody Pet pet) {
        return petService.save(pet);
    }

    @PostMapping("/petusers")
    @ResponseBody
    public Pet createPetAndUser(@RequestBody PetUser petuser) {
        combinedService.createPetAndUser(petuser.getPet(), petuser.getUser());
        return petuser.getPet();
    }

    @GetMapping("/pets/{id}")
    public Pet getPetById(@PathVariable("id") String id) {
        Pet result = null;
        Optional<Pet> pet = petService.findById(id);
        if (pet.isPresent()) {
            result = pet.get();
        }
        return result;
    }

    //@GetMapping("/users/email/{address}")

    @GetMapping("/pets")
    @ResponseBody
    public List<Pet> getPets(
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

        return petService.findAll(pageRequest);
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

    @PutMapping("/pets/{id}")
    public String updatePet(@PathVariable("id") String id, @RequestBody Pet updated) {
        updated.setId(id);
        petService.update(updated);
        return "Pet updated with ID: " + id;
    }

    @PatchMapping("/pets/{id}")
    public String patchPet(@PathVariable("id") String id, @RequestBody Pet patch) {
        Optional<Pet> existingOpt = petService.findById(id);
        if (existingOpt.isEmpty()) {
            return "Pet not found";
        }

        Pet existing = existingOpt.get();

        // Manually merge fields — or use a helper method
        if (patch.getPetname() != null) {
            existing.setPetname(patch.getPetname());
        }
        if (patch.getUsername() != null) {
            existing.setUsername(patch.getUsername());
        }

        petService.update(existing);
        return "Pet patched with ID: " + id;
    }

    @DeleteMapping("/pets/{id}")
    public String deletePet(@PathVariable("id") String id) {
        petService.deleteById(id);
        return "Pet deleted with ID: " + id;
    }

    @DeleteMapping("/pets")
    public String deleteAllPets() {
        petService.deleteAll();
        return "All pets deleted";
    }

}
