package com.shsnc.base.user.service;

import com.shsnc.base.user.mapper.GroupModelMapper;
import com.shsnc.base.user.model.GroupModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by houguangqiang on 2017/6/7.
 */
@Service
public class GroupService {

    @Autowired
    private GroupModelMapper groupModelMapper;

    public GroupModel exitsGroup(GroupModel groupModel){
        return groupModelMapper.existGroup(groupModel);
    }

    public boolean exitsByGroupIds(List<Long> groupIds){
        if(CollectionUtils.isEmpty(groupIds)){
            return true;
        }
        List<Long> dbGroupIds = groupModelMapper.existByGroupIds(new ArrayList<>(new HashSet<>(groupIds)));
        return dbGroupIds.size() == groupIds.size();
    }

}
