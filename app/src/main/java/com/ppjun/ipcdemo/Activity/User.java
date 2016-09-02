package com.ppjun.ipcdemo.Activity;

/**
 * @Package :com.ppjun.ipcdemo.Activity
 * @Description :
 * @Author :Rc3
 * @Created at :2016/9/2 15:55.
 */
public class User {

    int userId;
    String userName;
    int age;

    public User(int userId, String userName, int age) {
        this.userId = userId;
        this.userName = userName;
        this.age = age;
    }

    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }
}
