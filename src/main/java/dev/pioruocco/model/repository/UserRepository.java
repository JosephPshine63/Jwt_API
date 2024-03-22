package dev.pioruocco.model.repository;

import dev.pioruocco.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//this repository is defined solely for the user
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /*
    with these method we find a user by its username and the return type is Optiona<> beacuse
    we don't know if that username is effectively stored in the database(or if its been typer wrong)
     */
    Optional<User> findByUsername(String username);

}
