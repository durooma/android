package com.durooma.android.api;

public class Api {

    private static Environment<DuroomaApi> env;
    private static DuroomaApi api;

    public static DuroomaApi get() {
        if (env == null) {
            env = new LocalEnvironment();
        }
        if (api == null) {
            api = env.createService();
        }
        return api;
    }

}
