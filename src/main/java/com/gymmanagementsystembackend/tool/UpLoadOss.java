package com.gymmanagementsystembackend.tool;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.boot.exception.SystemException;
import com.gymmanagementsystembackend.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class UpLoadOss {
    private String endpoint = "https://oss-cn-beijing.aliyuncs.com/";
    private String accessKeyId = System.getenv("ALIYUN_USER_ID");
    private String accessKeySecret = System.getenv("ALIYUN_USER_SECRET");
    private String bucketName = "gym-front";
    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile multipartFile) {
        String mes="";
        try {
            if (multipartFile!=null){
                if (accessKeyId!=null&&accessKeySecret!=null){
                    // 获取上传的文件的输入流
                    InputStream inputStream = multipartFile.getInputStream();

                    // 避免文件覆盖
                    String originalFilename = multipartFile.getOriginalFilename();
                    String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

                    //上传文件到 OSS
                    OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                    ossClient.putObject(bucketName, fileName, inputStream);

                    //文件访问路径
                    String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;

                    // 关闭ossClient
                    ossClient.shutdown();
                    return url;// 把上传到oss的路径返回
                }else {
                    mes="身份校验有误！";
                }
            }else {
                mes="未找到您上传的文件！";
            }
        }catch (Exception e){
            e.printStackTrace();;
            throw new SystemException("12-01-00","系统出错！请稍等...");
        }
        throw new BusinessException("12-01-00",mes);
    }
}
