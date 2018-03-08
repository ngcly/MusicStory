package com.cn.social.wechart;

import org.springframework.social.ApiBinding;

/**
 * 微信
 *
 * @author chen
 * @date 2018-03-05 14:09
 */
public interface WeChart extends ApiBinding {
    WeChartUserInfo getUserInfo(String openId);
}
