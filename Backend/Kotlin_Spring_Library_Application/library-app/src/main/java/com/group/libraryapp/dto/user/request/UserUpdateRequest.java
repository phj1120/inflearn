package com.group.libraryapp.dto.user.request;

public class UserUpdateRequest {

    private long id;
    private String name;

    public UserUpdateRequest(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
