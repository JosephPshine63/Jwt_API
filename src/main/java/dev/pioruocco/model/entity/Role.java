package dev.pioruocco.model.entity;


//this enum represents the roles that a user can have
/*
in  a real application a user can have a large variety of roles, so for
development purposes we just set an admin e user role, will then bre
created some kind of Collection in the UserEntity that has all of its roles
 */
public enum Role {

    USER,
    ADMIN

}
