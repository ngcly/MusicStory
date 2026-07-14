package com.cn.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 通用文件存储服务接口
 *
 * @author ngcly
 */
public interface StorageService {
    /**
     * 上传文件
     *
     * @param file 文件对象
     * @param folder 存储子文件夹
     * @return 文件的访问路径
     */
    String uploadFile(MultipartFile file, String folder);

    /**
     * 删除文件
     *
     * @param url 文件的访问路径
     */
    void deleteFile(String url);
}
