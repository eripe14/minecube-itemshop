package com.eripe14.itemshop.user;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserRepository {

    CompletableFuture<Void> update(User user);

    CompletableFuture<User> findUser(UUID uniqueId);

    CompletableFuture<Set<User>> getAllUsers();

}