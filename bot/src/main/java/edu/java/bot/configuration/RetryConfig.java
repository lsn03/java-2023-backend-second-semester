package edu.java.bot.configuration;

import edu.java.bot.exception.ApiErrorException;
import java.time.Duration;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@ConditionalOnProperty(prefix = "app.retry")
public record RetryConfig(
    RetryType strategy,
    Long maxAttempts,
    Duration maxDelay,
    Duration startDelay,
    Set<Integer> statusCodes
) {

    public Retry getRetrySpec() {
        return switch (strategy) {
            case CONSTANT -> Retry.fixedDelay(maxAttempts, startDelay).doBeforeRetry(retrySignal -> log.info(
                "CONSTANT retry strategy. Attempt #{}",
                retrySignal.totalRetries() + 1
            )).filter(this::isRetryableException);
            case LINEAR -> Retry.from(retrySignalFlux -> retrySignalFlux
                .flatMap(retrySignal -> {
                    if (!isRetryableException(retrySignal.failure())) {

                        return Mono.error(retrySignal.failure());
                    }
                    long currentRetries = retrySignal.totalRetries() + 1;
                    Duration currentInterval = startDelay.multipliedBy(currentRetries);
                    log.info("LINEAR retry strategy. Attempt #{}", currentRetries);
                    if (currentRetries < maxAttempts) {
                        return Mono.delay(currentInterval);
                    } else {
                        return Mono.error(retrySignal.failure());
                    }
                }));
            case EXPONENTIAL -> Retry.backoff(maxAttempts, startDelay).doBeforeRetry(retrySignal -> log.info(
                "EXPONENTIAL retry strategy. Attempt #{}",
                retrySignal.totalRetries() + 1
            )).filter(this::isRetryableException);
        };
    }

    private boolean isRetryableException(Throwable throwable) {
        if (throwable instanceof ApiErrorException e) {
            return statusCodes.contains(Integer.parseInt(e.getErrorResponse().getCode()));
        }
        return false;
    }

}
