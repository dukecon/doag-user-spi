package org.dukecon.keycoak.user;

import org.dukecon.keycoak.user.doag.DoagUser;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
class UserCache {

    private static final Map<String, DoagUser> cache = new HashMap<>();

    static int getCount() {
        return cache.size();
    }

    static void addUser(DoagUser user) {
        cache.put(user.getUsername(), user);
    }

    static List<DoagUser> getUsers() {
        return cache.entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(DoagUser::getUsername))
                .collect(Collectors.toList());
    }

    static List<DoagUser> findUsers(String search) {
        return cache.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(u -> (u.getUsername() + ";" + u.getFirstName() + ";" + u.getLastName()).contains(search))
                .sorted(Comparator.comparing(DoagUser::getUsername))
                .collect(Collectors.toList());
    }

}
