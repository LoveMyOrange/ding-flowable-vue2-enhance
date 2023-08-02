package com.dingding.mid.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * minio文件上传工具类
 */
@Component
@Slf4j
public class MinioUploadUtil {
    @Resource(name = "minioClient")
    private MinioClient minioClient;

    /**
     * 上传文件
     *
     * @param file
     * @param bucketName
     * @param fileName
     * @return
     */
    public void uploadFile(MultipartFile file, String bucketName, String fileName) {
        //判断文件是否为空
        if (null == file || 0 == file.getSize()) {
            log.error("文件不能为空");
        }
        //判断存储桶是否存在
        bucketExists(bucketName);
        //文件名
        if(file != null){
            String originalFilename = file.getOriginalFilename();
            //新的文件名 = 存储桶文件名_时间戳.后缀名
            assert originalFilename != null;
            //开始上传
            try {
                @Cleanup InputStream inputStream = file.getInputStream();
                minioClient.putObject(
                        PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                                inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }


    /**
     * 下载文件
     *
     * @param fileName   文件名
     * @param bucketName 桶名（文件夹）
     * @return
     */
    public void downFile(String fileName, String bucketName, String downName) {
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
            //下载文件
            HttpServletResponse response = ServletUtil.getResponse();
            HttpServletRequest request = ServletUtil.getRequest();
            try {
                @Cleanup BufferedInputStream bis = new BufferedInputStream(inputStream);
                if (StringUtils.isNotEmpty(downName)) {
                    fileName = downName;
                }
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/plain");
                if(fileName.contains(".svg")){
                    response.setContentType("image/svg+xml");
                }
                //编码的文件名字,关于中文乱码的改造
                String codeFileName = "";
                String agent = request.getHeader("USER-AGENT").toLowerCase();
                if (-1 != agent.indexOf("msie") || -1 != agent.indexOf("trident")) {
                    //IE
                    codeFileName = URLEncoder.encode(fileName, "UTF-8");
                } else if (-1 != agent.indexOf("mozilla")) {
                    //火狐，谷歌
                    codeFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
                } else {
                    codeFileName = URLEncoder.encode(fileName, "UTF-8");
                }
                response.setHeader("Content-Disposition", "attachment;filename=" + XSSEscape.escape(new String(codeFileName.getBytes(), "utf-8")));
                @Cleanup OutputStream os = response.getOutputStream();
                int i;
                byte[] buff = new byte[1024 * 8];
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                }
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 返回图片
     *
     * @param fileName   文件名
     * @param bucketName 桶名（文件夹）
     * @return
     */
    public void dowloadMinioFile(String fileName, String bucketName) {
        try {
            @Cleanup InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
            @Cleanup ServletOutputStream outputStream1 = ServletUtil.getResponse().getOutputStream();
            //读取指定路径下面的文件
            @Cleanup OutputStream outputStream = new BufferedOutputStream(outputStream1);
            //创建存放文件内容的数组
            byte[] buff = new byte[1024];
            //所读取的内容使用n来接收
            int n;
            //当没有读取完时,继续读取,循环
            while ((n = inputStream.read(buff)) != -1) {
                //将字节数组的数据全部写入到输出流中
                outputStream.write(buff, 0, n);
            }
            //强制将缓存区的数据进行输出
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取資源
     *
     * @param fileName
     * @param bucketName
     */
    public String getFile(String fileName, String bucketName) {
        String objectUrl = null;
        try {
            objectUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return objectUrl;
    }

    /**
     * 下载文件
     *
     * @param fileName   文件名称
     * @param bucketName 存储桶名称
     * @return
     */
    public InputStream downloadMinio(String fileName, String bucketName) {
        try {
            @Cleanup InputStream stream =
                    minioClient.getObject(
                            GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            return stream;
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return null;
        }
    }

    /**
     * 获取全部bucket
     *
     * @return
     */
    public List<String> getAllBuckets() throws Exception {
        return minioClient.listBuckets().stream().map(Bucket::name).collect(Collectors.toList());
    }

    /**
     * 根据bucketName删除信息
     *
     * @param bucketName bucket名称
     */
    public void removeBucket(String bucketName) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 删除一个对象
     *
     * @param name
     * @return
     */
    public boolean removeFile(String bucketName, String name) {
        boolean isOK = true;
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(name).build());
        } catch (Exception e) {
            e.printStackTrace();
            isOK = false;
        }
        return isOK;
    }

    /**
     * 检查存储桶是否已经存在(不存在不创建)
     *
     * @param name
     * @return
     */
    public boolean bucketExists(String name) {
        boolean isExist = false;
        try {
            isExist = minioClient.bucketExists(getBucketExistsArgs(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    /**
     * 检查存储桶是否已经存在(不存在则创建)
     *
     * @param name
     * @return
     */
    public void bucketExistsCreate(String name) {
        try {
            minioClient.bucketExists(getBucketExistsArgs(name));
            minioClient.makeBucket(getMakeBucketArgs(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    /**
     * String转MakeBucketArgs
     *
     * @param name
     * @return
     */
    public static MakeBucketArgs getMakeBucketArgs(String name) {
        return MakeBucketArgs.builder().bucket(name).build();
    }

    /**
     * String转BucketExistsArgs
     *
     * @param name
     * @return
     */
    public static BucketExistsArgs getBucketExistsArgs(String name) {
        return BucketExistsArgs.builder().bucket(name).build();
    }




}
