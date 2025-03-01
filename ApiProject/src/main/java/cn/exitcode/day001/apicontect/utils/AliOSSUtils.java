package cn.exitcode.day001.apicontect.utils;

import cn.exitcode.day001.apicontect.service.ConfigService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.MulticastChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class AliOSSUtils  {



    private String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    private String accessKeyId = "*******";
    private String accessKeySecret = "********";
    private String bucketName =  "********";
    public String upload(MultipartFile file) throws IOException {
        //获取文件输入流
        InputStream inputStream = file.getInputStream();
        String filename =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +file.getOriginalFilename();
        //上传文件到阿里云
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, filename, inputStream);

        //文件访问路径
        String url = "https://" + bucketName + "." + endpoint.split("//")[1] + "/" + filename;
        ossClient.shutdown();
        return url;
    }
}
