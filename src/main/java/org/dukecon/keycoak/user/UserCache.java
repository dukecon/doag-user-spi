package org.dukecon.keycoak.user;

import org.dukecon.keycoak.user.doag.DoagUser;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
class UserCache {

    private static final Map<String, Map<String, DoagUser>> globalCache = new HashMap<>();

    static int getCount(final String realm) {
        Map<String, DoagUser> realmCache = globalCache.get(realm);
        return null == realmCache ? 0 : realmCache.size();
    }

    static void addUser(final String realm, final DoagUser user) {
        Map<String, DoagUser> realmCache = globalCache.computeIfAbsent(realm, k -> new HashMap<>());
        realmCache.put(user.getUsername(), user);
    }

    static List<DoagUser> getUsers(final String realm) {
        Map<String, DoagUser> realmCache = globalCache.get(realm);
        if (null == realmCache) {
            return Collections.emptyList();
        }

        return realmCache.entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(DoagUser::getUsername))
                .collect(Collectors.toList());
    }

    static List<DoagUser> findUsers(final String realm, final String search) {
        Map<String, DoagUser> realmCache = globalCache.get(realm);
        if (null == realmCache) {
            return Collections.emptyList();
        }

        return realmCache.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(u -> (u.getUsername() + ";" + u.getFirstName() + ";" + u.getLastName()).contains(search))
                .sorted(Comparator.comparing(DoagUser::getUsername))
                .collect(Collectors.toList());
    }

}
