package com.cn.exception;

import com.cn.pojo.RestCode;

/**
 * @author chenning
 */
public class FileUploadException extends GlobalException{
    public FileUploadException() {
        super(RestCode.FILE_UPLOAD_ERR);
    }
}
