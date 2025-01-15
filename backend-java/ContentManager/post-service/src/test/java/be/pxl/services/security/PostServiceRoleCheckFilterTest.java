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

class PostServiceRoleCheckFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private PostRoleCheckFilter roleCheckFilter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void allowsEditorToPostPutDelete() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn("editor");
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/posts");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }

    @Test
    void forbidsNonEditorToPostPutDelete() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn("viewer");
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/posts");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).sendError(HttpServletResponse.SC_FORBIDDEN, "Only editors can perform this action.");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void allowsGetRequestRegardlessOfRole() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn("viewer");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/posts");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }
    @Test
    void forbidsNonEditorToPutPostDelete() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn("viewer");
        when(request.getMethod()).thenReturn("PUT");
        when(request.getRequestURI()).thenReturn("/api/posts");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Only editors can perform this action.");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void allowsRequestsToNonPostEndpoints() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn("viewer");
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/users");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }

    @Test
    void forbidsRequestWithoutRoleHeader() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn(null);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/posts");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Only editors can perform this action.");
        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void forbidsNonEditorToDelete() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn("viewer");
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getRequestURI()).thenReturn("/api/posts");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Only editors can perform this action.");
        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void allowsNonPostPaths() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn("viewer");
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/unknown");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }
    @Test
    void allowsOptionsRequests() throws ServletException, IOException {
        when(request.getHeader("X-User-Role")).thenReturn("viewer");
        when(request.getMethod()).thenReturn("OPTIONS");
        when(request.getRequestURI()).thenReturn("/api/posts");

        roleCheckFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }


}
