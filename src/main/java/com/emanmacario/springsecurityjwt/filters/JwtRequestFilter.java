package com.emanmacario.springsecurityjwt.filters;

import com.emanmacario.springsecurityjwt.services.MyUserDetailsService;
import com.emanmacario.springsecurityjwt.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("unused")
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    // Examines the incoming request for JWT in the header, and sees if JWT is valid.
    // If valid JWT, get user details from user details service and save in security context.

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Get the authorization header from the HTTP request
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Extract the JWT and username from the payload
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            System.out.println("USERNAME: " + username);
        }

        // Only continue if the context does not contain an authenticated user
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Retrieve user details from the service
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Check if JWT is valid (i.e. not expired)
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }
}
