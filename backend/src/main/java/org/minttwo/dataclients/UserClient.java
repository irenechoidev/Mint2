package org.minttwo.dataclients;

import lombok.NonNull;
import org.minttwo.models.User;

import java.util.UUID;

public class UserClient extends DataClient<User> {
    public UserClient(Db db) {
        super(db);
    }

    public void createUser(@NonNull User user) {
        String id = UUID.randomUUID().toString();
        user.setId(id);

        this.insert(user);
    }

    public User getUserById(@NonNull String id) {
        return this.getById(User.class, id);
    }
}
