package com.durooma.android.model;

import android.support.v4.util.ArrayMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Account extends AccountBody {

    private long id;

    public Account(long id, String name, double initialBalance) {
        super(name, initialBalance);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private static Map<Long, Account> cache;

    public static void updateCache(List<Account> accounts) {
        if (cache == null) {
            cache = new ArrayMap<>();
        }
        cache.clear();
        for (Account account : accounts) {
            cache.put(account.getId(), account);
        }
    }

    public static Account get(long id) {
        return cache.get(id);
    }

    public static Collection<Account> all() {
        return cache.values();
    }

    public static boolean hasCache() {
        return cache != null;
    }

}
