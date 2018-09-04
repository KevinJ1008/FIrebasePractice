package com.kevinj1008.firebasepractice;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String name;
    public String email;
    public Friend friendId;

    public User() {

    }



    public User(String name, String email) {
        this.name = name;
        this.email = email;
//        this.friendId = friendId;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> add = new HashMap<>();
        add.put("name", name);
        add.put("email", email);
        return add;
    }

}
