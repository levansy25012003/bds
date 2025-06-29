package com.example.bds.component;

import com.example.bds.model.TaiKhoan;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {


    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response); // ai cũng cho qua hết
                return;
            } // kiểm tra xem có phải là token bypass không

            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String email = jwtTokenUtil.extractEmail(token);
            if (email != null &&  SecurityContextHolder.getContext().getAuthentication() == null) {
                TaiKhoan userDetails = (TaiKhoan) userDetailsService.loadUserByUsername(email);
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(
                            userDetails,null, userDetails.getAuthorities());
                    authenticationToken .setDetails((new WebAuthenticationDetailsSource().buildDetails(request)));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
//                filterChain.doFilter(request, response);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorize");
        }
    }

    private boolean isBypassToken(HttpServletRequest request) {
        AntPathMatcher matcher = new AntPathMatcher();
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of("/auth/register", "POST"),
                Pair.of("/auth/login", "POST"),
                Pair.of("/auth/google", "POST"),
                Pair.of("/auth/login-email", "POST"),
                Pair.of("/auth/login-phone", "POST"),
                Pair.of("/user/reset-password-required", "POST"),
                Pair.of("/user/reset-password-verify", "POST"),
                Pair.of("/post/public", "GET"),
                Pair.of("/post/one/*", "GET"),
                Pair.of("/payment/vnpay-return", "GET")
        );
        for (Pair<String, String> byPassToken : byPassTokens) {
            if(
                    matcher.match(byPassToken.getFirst(), request.getServletPath()) &&
                    request.getMethod().equals(byPassToken.getSecond()))
            {
                return true;
            }
        }
        return false;
    }
}
