package com.zph0000.demo.service;

import com.zph0000.demo.dto.GroupDto;

import java.util.List;

/**
 * Created by zph  Date：2017/6/12.
 */
public interface GroupService {

    GroupDto getGroup(Long id);

    List<GroupDto> init();

    List<GroupDto> getGroups();
}
