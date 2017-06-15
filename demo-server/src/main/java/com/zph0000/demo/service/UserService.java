package com.zph0000.demo.service;

import com.zph0000.demo.dto.UserDto;

import java.util.List;

/**
 * Created by zph  Date：2017/6/12.
 */
public interface UserService {

    UserDto getUser(Long id);

    UserDto getUserByName(String name);

    List<UserDto> getUsers(String name);

    List<UserDto> getUsersByGroup(Long groupId);

    Integer getCount();

    void addUser(String name,Integer age);

}
