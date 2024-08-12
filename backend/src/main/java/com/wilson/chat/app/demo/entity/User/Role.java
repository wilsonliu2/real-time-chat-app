package com.wilson.chat.app.demo.entity.User;

import java.util.Arrays;
import java.util.List;

public enum Role {
    // Define the USER role with USER permissions
    USER(Arrays.asList(Permission.USER)),

    // Define the ADMIN role with both USER and ADMIN permissions
    ADMIN(Arrays.asList(Permission.USER, Permission.ADMIN));

    // Field to store the list of permissions for each role
    private List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}