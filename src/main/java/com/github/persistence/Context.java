package com.github.persistence;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Context {
    private static ThreadLocal<Context> instance;

    private Response response;
    private String username;
    private String repoName;

    public static synchronized Context getContext() {
        if (instance == null) {
            instance = ThreadLocal.withInitial(Context::new);
        }
        return instance.get();
    }
}
