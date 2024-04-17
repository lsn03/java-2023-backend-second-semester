package edu.java.configuration.rate;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.model.scrapper.dto.response.ApiErrorResponse;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {
    private final Bucket bucket;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String ip = request.getHeader("Tg-Chat-Id");

        if (ip == null) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Filter for user {}", ip);
        var bucketReq = buckets.computeIfAbsent(ip, s -> bucket);
        ConsumptionProbe probe = bucketReq.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            filterChain.doFilter(request, response);
        } else {

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                "Слишком много запросов", "429", "ToManyRequestException", "Превышен лимит запросов", null);

            String jsonResponse = mapper.writeValueAsString(apiErrorResponse);

            response.getWriter().write(jsonResponse);

        }
    }
}
