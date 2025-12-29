package com.github.requests;

import com.ibm.icu.impl.Assert;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.awaitility.core.ConditionTimeoutException;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static org.awaitility.Awaitility.with;

public class GenericRetryClient<T extends Response> {
    @SneakyThrows
    public T waitForStatusCode(Callable<T> function, int expectedStatusCode) {
        T response = function.call();
        if (response.getStatusCode() != expectedStatusCode) {
            response = retryOnStatusCode(function, expectedStatusCode);
        }
        return response;
    }

    @SneakyThrows
    private T retryOnStatusCode(Callable<T> function, int expectedStatusCode) {
        T resp;
        try {
            resp = with()
                    .pollInterval(500, TimeUnit.MILLISECONDS)
                    .await()
                    .atMost(5, TimeUnit.SECONDS)
                    .until(function, isStatus(expectedStatusCode));
        } catch (ConditionTimeoutException e) {
            resp = function.call();
            Assert.fail(
                    String.format(
                            "------------ Retry Failed: HTTP code was <%s> instead of <%s> ------------ %n Response Body: <%s>",
                            resp.getStatusCode(),
                            expectedStatusCode,
                            resp.body().prettyPrint()
                    )
            );
        }
        return resp;
    }

    private static Predicate<Response> isStatus(int expectedStatusCode) {
        return p -> p.getStatusCode() == expectedStatusCode;
    }
}
