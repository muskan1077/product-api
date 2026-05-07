package com.market.product_api;

import com.market.product_api.entity.User;
import com.market.product_api.repository.ProductRepository;
import com.market.product_api.repository.UserRepository;
import com.market.product_api.service.CustomUserDetailsService;
import com.market.product_api.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registerShouldCreateUserWithEncodedPassword() throws Exception {
        String requestBody = """
                {
                  "username": "alice",
                  "password": "secret123"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.type").value("Bearer"));

        User savedUser = userRepository.findByUsername("alice").orElseThrow();
        assertThat(savedUser.getPassword()).isNotEqualTo("secret123");
        assertThat(passwordEncoder.matches("secret123", savedUser.getPassword())).isTrue();
        assertThat(savedUser.getRole()).isEqualTo("USER");
    }

    @Test
    void registerShouldRejectDuplicateUsername() throws Exception {
        userRepository.save(new User(null, "alice", passwordEncoder.encode("secret123"), "USER"));

        String requestBody = """
                {
                  "username": "alice",
                  "password": "anotherSecret"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    @Test
    void loginShouldReturnTokenForValidCredentials() throws Exception {
        userRepository.save(new User(null, "alice", passwordEncoder.encode("secret123"), "USER"));

        String requestBody = """
                {
                  "username": "alice",
                  "password": "secret123"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    void loginShouldRejectInvalidCredentials() throws Exception {
        userRepository.save(new User(null, "alice", passwordEncoder.encode("secret123"), "USER"));

        String requestBody = """
                {
                  "username": "alice",
                  "password": "wrong-password"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void productEndpointsShouldRejectMissingToken() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    void productEndpointsShouldRejectInvalidToken() throws Exception {
        mockMvc.perform(get("/products")
                        .header("Authorization", "Bearer invalid.token.value"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    void productEndpointsShouldAllowValidTokenAndKeepSuccessPayloadUnwrapped() throws Exception {
        userRepository.save(new User(null, "alice", passwordEncoder.encode("secret123"), "USER"));
        String token = jwtService.generateToken(userDetailsService.loadUserByUsername("alice"));

        String requestBody = """
                {
                  "name": "Keyboard",
                  "price": 2500
                }
                """;

        mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Keyboard"))
                .andExpect(jsonPath("$.price").value(2500.0))
                .andExpect(jsonPath("$.code").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist());
    }

    @Test
    void invalidProductPayloadShouldReturnGeneralizedError() throws Exception {
        userRepository.save(new User(null, "alice", passwordEncoder.encode("secret123"), "USER"));
        String token = jwtService.generateToken(userDetailsService.loadUserByUsername("alice"));

        String requestBody = """
                {
                  "name": "",
                  "price": 2500
                }
                """;

        mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Invalid request payload"));
    }

    @Test
    void missingProductPriceShouldReturnGeneralizedError() throws Exception {
        userRepository.save(new User(null, "alice", passwordEncoder.encode("secret123"), "USER"));
        String token = jwtService.generateToken(userDetailsService.loadUserByUsername("alice"));

        String requestBody = """
                {
                  "name": "Keyboard"
                }
                """;

        mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Invalid request payload"));
    }

    @Test
    void malformedProductPayloadShouldReturnGeneralizedError() throws Exception {
        userRepository.save(new User(null, "alice", passwordEncoder.encode("secret123"), "USER"));
        String token = jwtService.generateToken(userDetailsService.loadUserByUsername("alice"));

        String requestBody = """
                {
                  "name": "Keyboard",
                  "price": "free"
                }
                """;

        mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Invalid request payload"));
    }

    @Test
    void deletingMissingProductShouldReturnNotFound() throws Exception {
        userRepository.save(new User(null, "alice", passwordEncoder.encode("secret123"), "USER"));
        String token = jwtService.generateToken(userDetailsService.loadUserByUsername("alice"));

        mockMvc.perform(delete("/products/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"));
    }
}
