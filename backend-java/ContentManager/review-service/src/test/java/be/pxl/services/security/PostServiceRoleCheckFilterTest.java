package be.pxl.services.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

class ReviewServiceRoleCheckFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private ReviewRoleCheckFilter roleCheckFilter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void allowsEditorAccessToReviewEndpoints() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-User-Role")).thenReturn("editor");
        when(request.getRequestURI()).thenReturn("/api/reviews");

        // Act
        roleCheckFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);  // Controleer dat filterchain doorgaat
        verify(response, never()).sendError(anyInt(), anyString());
    }

    @Test
    void forbidsNonEditorAccessToReviewEndpoints() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-User-Role")).thenReturn("viewer");
        when(request.getRequestURI()).thenReturn("/api/reviews");

        // Act
        roleCheckFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response, times(1)).sendError(HttpServletResponse.SC_FORBIDDEN, "Only editors can access review endpoints.");
        verify(filterChain, never()).doFilter(request, response);  // Controleer dat de filter niet doorgaat
    }

    @Test
    void allowsOtherEndpointsRegardlessOfRole() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-User-Role")).thenReturn("viewer");
        when(request.getRequestURI()).thenReturn("/api/posts");

        // Act
        roleCheckFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);  // Controleer dat andere endpoints doorgaan
        verify(response, never()).sendError(anyInt(), anyString());
    }

    @Test
    void forbidsAccessToReviewEndpointsWhenRoleHeaderIsMissing() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-User-Role")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/api/reviews");

        // Act
        roleCheckFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response, times(1)).sendError(HttpServletResponse.SC_FORBIDDEN, "Only editors can access review endpoints.");
        verify(filterChain, never()).doFilter(request, response);  // Controleer dat de filter niet doorgaat
    }

    @Test
    void allowsNonReviewEndpointsWhenRoleHeaderIsMissing() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-User-Role")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/api/users");

        // Act
        roleCheckFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);  // Controleer dat andere endpoints doorgaan
        verify(response, never()).sendError(anyInt(), anyString());
    }
    @Test
    void allowsNonReviewPaths() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn("viewer");
        when(request.getRequestURI()).thenReturn("/api/unknown");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }
    @Test
    void forbidsReviewAccessWithEmptyRole() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn("");
        when(request.getRequestURI()).thenReturn("/api/reviews");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).sendError(HttpServletResponse.SC_FORBIDDEN, "Only editors can access review endpoints.");
        verify(filterChain, never()).doFilter(request, response);
    }


}
