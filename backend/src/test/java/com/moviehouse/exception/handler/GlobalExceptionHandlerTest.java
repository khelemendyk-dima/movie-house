package com.moviehouse.exception.handler;

import com.moviehouse.exception.AlreadyExistsException;
import com.moviehouse.exception.NotFoundException;
import com.moviehouse.exception.PaymentRequiredException;
import com.moviehouse.exception.ServiceException;
import com.moviehouse.security.JwtAuthFilter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerTest.DummyController.class)
class GlobalExceptionHandlerTest {

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class DummyController {

        public record DummyDto(
                @NotBlank(message = "Name must not be blank")
                String name
        ) {}

        @PostMapping("/validation-error")
        public void throwValidationException(@RequestBody @Valid DummyDto ignoredDto) {
            // No-op, validation happens before method is called
        }

        @GetMapping("/bad-credentials")
        public void throwBadCredentialsException() {
            throw new BadCredentialsException("Invalid username or password");
        }

        @GetMapping("/service-exception")
        public void throwServiceException() {
            throw new ServiceException("Something went wrong");
        }

        @GetMapping("/payment-required")
        public void throwPaymentRequired() {
            throw new PaymentRequiredException("Payment required");
        }

        @GetMapping("/not-found")
        public void throwNotFound() {
            throw new NotFoundException("Not found");
        }

        @GetMapping("/already-exists")
        public void throwAlreadyExists() {
            throw new AlreadyExistsException("Already exists");
        }

        @GetMapping("/generic")
        public void throwGeneric() {
            throw new RuntimeException("Unexpected error");
        }
    }

    @Test
    void handleMethodArgumentNotValidException() throws Exception {
        String invalidRequest = """
        {
          "name": ""
        }
        """;

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/validation-error")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Name must not be blank"));
    }


    @Test
    void handleBadCredentialsException() throws Exception {
        mockMvc.perform(get("/bad-credentials"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void handleServiceException() throws Exception {
        mockMvc.perform(get("/service-exception"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Something went wrong"));
    }

    @Test
    void handlePaymentRequiredException() throws Exception {
        mockMvc.perform(get("/payment-required"))
                .andExpect(status().isPaymentRequired())
                .andExpect(jsonPath("$.status").value(HttpStatus.PAYMENT_REQUIRED.value()))
                .andExpect(jsonPath("$.message").value("Payment required"));
    }

    @Test
    void handleNotFoundException() throws Exception {
        mockMvc.perform(get("/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Not found"));
    }

    @Test
    void handleAlreadyExistsException() throws Exception {
        mockMvc.perform(get("/already-exists"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value("Already exists"));
    }

    @Test
    void handleGenericException() throws Exception {
        mockMvc.perform(get("/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message", containsString("Unexpected error")));
    }
}
