//package com.cn.config;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.*;
//import com.amazonaws.util.IOUtils;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.net.URL;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author chenning
// */
//@Slf4j
//@RequiredArgsConstructor
//public class OssTemplate {
//    private final AmazonS3 amazonS3;
//
//    /**
//     * 创建Bucket
//     *
//     * @param bucketName bucket名称
//     */
//    public void createBucket(String bucketName) {
//        if (!amazonS3.doesBucketExistV2(bucketName)) {
//            amazonS3.createBucket((bucketName));
//        }
//    }
//
//    /**
//     * 获取所有的buckets
//     * AmazonS3: <a href="https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListBuckets.html">list buckets</a>
//     *
//     * @return List<Bucket>
//     */
//    public List<Bucket> getAllBuckets() {
//        return amazonS3.listBuckets();
//    }
//
//    /**
//     * 通过Bucket名称删除Bucket
//     * AmazonS3：<a href="https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteBucket.html">delete bucket</a>
//     *
//     * @param bucketName bucket名称
//     */
//    public void deleteBucket(String bucketName) {
//        amazonS3.deleteBucket(bucketName);
//    }
//
//    /**
//     * 上传对象
//     *
//     * @param bucketName  bucket名称
//     * @param objectName  文件名称
//     * @param stream      文件流
//     * @param contextType 文件类型
//     *                    AmazonS3：
//     *                    <a href="https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html">putObject</a>
//     */
//    @SneakyThrows
//    public void putObject(String bucketName, String objectName, InputStream stream, String contextType) {
//        putObject(bucketName, objectName, stream, stream.available(), contextType);
//    }
//
//    /**
//     * 上传对象
//     *
//     * @param bucketName bucket名称
//     * @param objectName 文件名称
//     * @param stream     文件流
//     *                   AmazonS3：
//     *                   <a href="https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html">putObject</a>
//     */
//    @SneakyThrows
//    public void putObject(String bucketName, String objectName, InputStream stream) {
//        putObject(bucketName, objectName, stream, stream.available(), MediaType.APPLICATION_OCTET_STREAM_VALUE);
//    }
//
//    /**
//     * 通过bucketName和objectName获取对象
//     *
//     * @param bucketName bucket名称
//     * @param objectName 文件名称
//     * @return S3Object
//     * AmazonS3：<a href="https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObject.html">getObject</a>
//     */
//    public S3Object getObject(String bucketName, String objectName) {
//        return amazonS3.getObject(bucketName, objectName);
//    }
//
//    /**
//     * 获取对象的url
//     *
//     * @param bucketName bucket名称
//     * @param objectName object名字
//     * @param expires    过期时长
//     * @return String
//     * AmazonS3：<a href="https://docs.aws.amazon.com/AmazonS3/latest/API/API_GeneratePresignedUrl.html">getObjectURL</a>
//     */
//    @SneakyThrows
//    public String getObjectURL(String bucketName, String objectName, Integer expires) {
//        LocalDateTime localDateTime = LocalDateTime.now().plusDays(expires);
//        URL url = amazonS3.generatePresignedUrl(bucketName, objectName,
//                Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
//        return url.toString();
//    }
//
//    /**
//     * 通过bucketName和objectName删除对象
//     *
//     * @param bucketName bucket名称
//     * @param objectName object名字
//     *                   AmazonS3：
//     *                   <a href="https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteObject.html">deleteObject</a>
//     */
//    public void deleteObject(String bucketName, String objectName) {
//        amazonS3.deleteObject(bucketName, objectName);
//    }
//
//    /**
//     * 根据bucketName和prefix获取对象集合
//     *
//     * @param bucketName bucket名称
//     * @param prefix     前缀
//     * @return List<S3ObjectSummary>
//     * AmazonS3：<a href="https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListObjects.html">getAllObject</a>
//     */
//    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix) {
//        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
//        return objectListing.getObjectSummaries();
//    }
//
//    /**
//     * @param bucketName  bucket名称
//     * @param objectName  object名
//     * @param stream      输入流
//     * @param size        大小
//     * @param contextType 类型
//     * @return PutObjectResult
//     */
//    @SneakyThrows
//    private PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size,
//                                      String contextType) {
//        byte[] bytes = IOUtils.toByteArray(stream);
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentLength(size);
//        objectMetadata.setContentType(contextType);
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//        return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
//    }
//
//}
