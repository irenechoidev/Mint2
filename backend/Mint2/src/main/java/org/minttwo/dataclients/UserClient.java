package org.minttwo.dataclients;

import lombok.NonNull;
import org.minttwo.models.User;

public class UserClient extends DataClient<User> {
    public UserClient(Db db) {
        super(db);
    }

    public void createUser(@NonNull User user) {
        this.insert(user);
    }
}
