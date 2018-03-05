package com.cn.social.wechart;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 将微信数据适配为标准模型
 *
 * @author chen
 * @date 2018-03-05 15:03
 */
public class WeChartAdapter implements ApiAdapter<WeChart> {
    private String openId;

    public WeChartAdapter() {
    }

    public WeChartAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(WeChart api) {
        return true;
    }

    @Override
    public void setConnectionValues(WeChart api, ConnectionValues values) {
        WeChartUserInfo userInfo = api.getUserInfo(openId);
        values.setProviderUserId(userInfo.getOpenid());
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(WeChart api) {
        return null;
    }

    @Override
    public void updateStatus(WeChart api, String message) {

    }
}
