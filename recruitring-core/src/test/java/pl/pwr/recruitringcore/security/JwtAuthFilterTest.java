package pl.pwr.recruitringcore.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import pl.pwr.recruitringcore.dto.UserDTO;
import pl.pwr.recruitringcore.service.UserService;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private UserService userService;
    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private String validToken;
    private String invalidToken;

    @BeforeEach
    void setUp() {
        String testSecretKey = "S3cr3tK3yV3ryC0mpl3x";
        String encodedSecretKey = Base64.getEncoder().encodeToString(testSecretKey.getBytes());
        ReflectionTestUtils.setField(jwtAuthFilter, "secretKey", encodedSecretKey);
        Algorithm algorithm = Algorithm.HMAC256(encodedSecretKey);
        validToken = JWT.create()
                .withSubject("testUser")
                .withExpiresAt(Date.from(Instant.now().plusSeconds(60)))
                .withClaim("role", "ROLE_USER")
                .sign(algorithm);
        invalidToken = "InvalidToken";
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnUnauthorizedForInvalidToken() throws ServletException, IOException {
        // GIVEN
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // WHEN
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // THEN
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldReturnUnauthorizedForExpiredToken() throws ServletException, IOException {
        // GIVEN
        Algorithm algorithm = Algorithm.HMAC256("secret-key");
        String expiredToken = JWT.create()
                .withSubject("testUser")
                .withExpiresAt(Date.from(Instant.now().minusSeconds(60)))
                .withClaim("role", "ROLE_USER")
                .sign(algorithm);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + expiredToken);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // WHEN
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // THEN
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldPassFilterWithoutAuthorizationHeader() throws ServletException, IOException {
        // GIVEN
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // WHEN
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // THEN
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldClearSecurityContextWhenTokenVerificationFails() throws ServletException, IOException {
        // GIVEN
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + validToken);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // WHEN
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // THEN
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }

    @Test
    void shouldAuthenticateUserWithValidToken() throws ServletException, IOException {
        // GIVEN
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + validToken);
        MockHttpServletResponse response = new MockHttpServletResponse();

        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("testUser");
        userDTO.setRole("ROLE_USER");
        when(userService.findUserByLogin("testUser")).thenReturn(userDTO);

        // WHEN
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // THEN
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("testUser", ((UserDTO) authentication.getPrincipal()).getLogin());
        assertEquals("ROLE_USER", authentication.getAuthorities().iterator().next().getAuthority());
    }
}
