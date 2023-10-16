package pri.wkz.mockitodemo.demos.security;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kuiZhi Wang
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String debugUserStr = request.getHeader("debug-user");
        try {
            if (!ObjectUtils.isEmpty(debugUserStr)) {
                AuthContext.setUserId(Long.parseLong(debugUserStr));
            }
            filterChain.doFilter(request, response);
        } finally {
            AuthContext.clean();
        }
    }
}
