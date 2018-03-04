package com.cn.social.qq;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 适配spring social 默认的返回信息
 * @author chenning
 * @date 2018/3/4 下午2:09
 */
public class QQAdapter implements ApiAdapter<QQ> {
    @Override
    public boolean test(QQ api) {
        return true;
    }

    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        QQUserInfo userInfo = api.getUserInfo();

        values.setProviderUserId(userInfo.getOpenId());
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        values.setProfileUrl(null);

    }

    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }

    @Override
    public void updateStatus(QQ api, String message) {

    }
}
