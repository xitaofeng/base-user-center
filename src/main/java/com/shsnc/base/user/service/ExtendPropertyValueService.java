package com.shsnc.base.user.service;

import com.shsnc.base.user.mapper.ExtendPropertyModelMapper;
import com.shsnc.base.user.mapper.ExtendPropertyValueMapper;
import com.shsnc.base.user.mapper.UserInfoModelMapper;
import com.shsnc.base.user.model.ExtendPropertyModel;
import com.shsnc.base.user.model.ExtendPropertyValueModel;
import com.shsnc.base.user.model.UserInfoModel;
import com.shsnc.base.user.support.Assert;
import com.shsnc.base.util.config.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by houguangqiang on 2017/6/8.
 */
@Service
public class ExtendPropertyValueService {

    @Autowired
    private ExtendPropertyValueMapper extendPropertyValueMapper;
    @Autowired
    private UserInfoModelMapper userInfoModelMapper;
    @Autowired
    private ExtendPropertyModelMapper extendPropertyModelMapper;

    /**
     * 新增用户扩展属性值
     * @param extendPropertyValueModel 扩展属性值
     * @return 返回新增数据id
     */
    public Long addExtendPropertyValue(ExtendPropertyValueModel extendPropertyValueModel) throws BizException {
        Long propertyId = extendPropertyValueModel.getPropertyId();
        Assert.notNull(propertyId,"属性id不能为空！");
        ExtendPropertyModel extendPropertyModel = extendPropertyModelMapper.selectByPrimaryKey(propertyId);
        Assert.notNull(extendPropertyModel, String.format("属性id：%s不存在！",propertyId));

        Long userId = extendPropertyValueModel.getUserId();
        Assert.notNull(userId,"用户id不能为空！");
        UserInfoModel userInfoModel = userInfoModelMapper.selectByPrimaryKey(userId);
        Assert.notNull(userInfoModel,String.format("用户id：%s不存在！",userId));

        Assert.notNull(extendPropertyValueModel.getPropertyValue(),"属性值不能为空！");
        extendPropertyValueMapper.insertSelective(extendPropertyValueModel);
        return extendPropertyValueModel.getPropertyValueId();
    }

    /**
     * 批量新增用户扩展属性值
     * @param extendPropertyValueModels 扩展属性值列表
     * @return 返回新增记录id列表
     * @throws BizException 业务异常
     */
    public boolean batchAddExtendPropertyValue(List<ExtendPropertyValueModel> extendPropertyValueModels) throws BizException {
        Assert.notEmpty(extendPropertyValueModels);
        Set<Long> propertyIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        for (ExtendPropertyValueModel extendPropertyValueModel : extendPropertyValueModels){
            Long propertyId = extendPropertyValueModel.getPropertyId();
            Assert.notNull(propertyId,"属性id不能为空！");
            Long userId = extendPropertyValueModel.getUserId();
            Assert.notNull(userId,"用户id不能为空！");
            Assert.notNull(extendPropertyValueModel.getPropertyValue(),"属性值不能为空！");

            propertyIds.add(propertyId);
            userIds.add(userId);
        }
        List<Long> dbUserIds = userInfoModelMapper.getUserIdsByUserIds(userIds);
        Assert.isTrue(dbUserIds.size() == userIds.size(),"含有不存在的用户id！");

        List<Long> dbPropertyIds = extendPropertyModelMapper.getPropertyIdsByPropertyIds(propertyIds);
        Assert.isTrue(dbPropertyIds.size() == propertyIds.size(), "含有不存在的属性id！");

        List<ExtendPropertyValueModel> dbExtendPropertyValueModels = extendPropertyValueMapper.getPropertyByUserIds(userIds);
        Map<Long,ExtendPropertyValueModel> dbExtendPropertyValueModelsMap = new HashMap<>();
        for (ExtendPropertyValueModel extendPropertyValueModel : extendPropertyValueModels){
            ExtendPropertyValueModel dbExtendPropertyValueModel = extendPropertyValueMapper.getByUserIdAndPropertyId(extendPropertyValueModel.getUserId(),extendPropertyValueModel.getPropertyId());
            if(dbExtendPropertyValueModel != null){
                if(!dbExtendPropertyValueModel.getPropertyValue().equals(extendPropertyValueModel.getPropertyValue())){
                    dbExtendPropertyValueModel.setPropertyValue(extendPropertyValueModel.getPropertyValue());
                    extendPropertyValueMapper.updateByPrimaryKeySelective(dbExtendPropertyValueModel);
                }
            } else {
                extendPropertyValueMapper.insertSelective(extendPropertyValueModel);
            }
        }

        return true;
    }

    /**
     * 更新用户扩展属性值
     * @param extendPropertyValueModel 扩展属性值
     * @return 数据有更新返回true，否则返回false
     */
    public boolean updateExtendPropertyValue(ExtendPropertyValueModel extendPropertyValueModel) throws BizException {
        Long propertyValueId = extendPropertyValueModel.getPropertyValueId();
        Assert.notNull(propertyValueId,"属性值id不能为空！");
        Assert.notNull(extendPropertyValueModel.getPropertyValue(),"属性值不能为空！");
        ExtendPropertyValueModel dbExtendPropertyValue = extendPropertyValueMapper.selectByPrimaryKey(propertyValueId);
        Assert.notNull(dbExtendPropertyValue, String.format("属性值id：%s不存在！",propertyValueId));

        Long userId = extendPropertyValueModel.getUserId();
        Assert.isTrue(userId != null && userId.equals(dbExtendPropertyValue.getUserId()),"不能改变属性所属的用户！");

        Long propertyId = extendPropertyValueModel.getPropertyId();
        if(propertyId != null && !propertyId.equals(dbExtendPropertyValue.getPropertyId())){
            ExtendPropertyModel extendPropertyModel = extendPropertyModelMapper.selectByPrimaryKey(propertyId);
            Assert.notNull(extendPropertyModel, String.format("属性id：%s不存在！",propertyId));
        }

        return extendPropertyValueMapper.updateByPrimaryKeySelective(extendPropertyValueModel) > 0;
    }

    /**
     * 批量更新用户扩展属性值，如果没有id表示新增，有id表示更新
     * @param extendPropertyValues 扩展属性值列表
     * @return 始终返回true
     * @throws BizException 业务异常
     */
    public boolean batchUpdateExtendPropertyValue(List<ExtendPropertyValueModel> extendPropertyValues) throws BizException {
        Assert.notEmpty(extendPropertyValues);

        List<ExtendPropertyValueModel> addExtendPropertyValues = new ArrayList<>();
        List<ExtendPropertyValueModel> updateExtendPropertyValues = new ArrayList<>();
        for (ExtendPropertyValueModel extendPropertyValueModel : extendPropertyValues){
            if(extendPropertyValueModel.getPropertyValueId() == null){
                addExtendPropertyValues.add(extendPropertyValueModel);
            } else {
                updateExtendPropertyValues.add(extendPropertyValueModel);
            }
        }
        if(!addExtendPropertyValues.isEmpty()){
            batchAddExtendPropertyValue(addExtendPropertyValues);
        }
        if(!updateExtendPropertyValues.isEmpty()){
            for (ExtendPropertyValueModel extendPropertyValueModel : updateExtendPropertyValues){
                updateExtendPropertyValue(extendPropertyValueModel);
            }
        }
        return true;
    }

    /**
     * 删除一个用户扩展属性值
     * @param propertyValueId 扩展属性值id
     * @return 有数据更新返回true，否则返回false
     * @throws BizException 业务异常
     */
    public boolean deleteExtendPropertyValue(Long propertyValueId) throws BizException {
        Assert.notNull(propertyValueId,"属性值id不能为空！");
        return extendPropertyValueMapper.deleteByPrimaryKey(propertyValueId) > 0;
    }

    /**
     * 根据用户id获取用户扩展属性列表
     * @param userId 用户id
     * @return 返回用户扩展属性列表
     */
    public List<ExtendPropertyValueModel> getExtendPropertyValueByUserId(Long userId){
        ExtendPropertyValueModel extendPropertyValueModel = new ExtendPropertyValueModel();
        extendPropertyValueModel.setUserId(userId);
        return extendPropertyValueMapper.findExtendPropertyValueList(extendPropertyValueModel);
    }

}
