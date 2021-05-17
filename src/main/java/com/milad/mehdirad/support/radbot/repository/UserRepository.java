package com.milad.mehdirad.support.radbot.repository;

import com.milad.mehdirad.support.radbot.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByUserId(long userId);


    User findUserByUserId(Long userId);

    List<User> findAll();
}
