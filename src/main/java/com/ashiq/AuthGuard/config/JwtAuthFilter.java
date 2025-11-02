package com.ashiq.AuthGuard.config;

import com.ashiq.AuthGuard.service.JwtService;
import com.ashiq.AuthGuard.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        List<String> rawAuthorities = null; // ✅ আগে থেকেই declare

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // ✅ Remove "Bearer "
        username = jwtService.extractUsername(jwt); // ✅ Usually email

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // ✅ Step 1: Load user
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {

                // ✅ Step 2: Extract authorities from JWT claims
                Claims claims = jwtService.extractAllClaims(jwt);
                rawAuthorities = claims.get("authorities", List.class); // ✅ assign to previously declared var

                List<GrantedAuthority> authorities = rawAuthorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // ✅ Step 3: Create Authentication with authorities
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken); // ✅ Auth set
            }
        }

        // ✅ Debugging logs
        System.out.println("➡️ JWT Received: " + jwt);
        System.out.println("📛 Extracted Username: " + username);
        System.out.println("🧾 Authorities from token: " + rawAuthorities);
        System.out.println("✅ Security Context Set: " + SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request, response);
    }

}
