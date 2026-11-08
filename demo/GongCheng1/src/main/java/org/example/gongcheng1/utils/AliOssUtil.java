package org.example.gongcheng1.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AliOssUtil {


    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    private static final String ENDPOINT = "https://oss-cn-beijing.aliyuncs.com";
    // 访问凭证不写死在源码里：
    // 优先读环境变量 OSS_ACCESS_KEY_ID / OSS_ACCESS_KEY_SECRET，
    // 其次读 classpath 下的 oss.properties 文件（该文件已在 .gitignore 中排除，不纳入版本库）。
    private static final Properties OSS_CONF = loadOssConf();
    private static final String ACCESS_KEY_ID = resolve("OSS_ACCESS_KEY_ID", "accessKeyId");
    private static final String ACCESS_KEY_SECRET = resolve("OSS_ACCESS_KEY_SECRET", "accessKeySecret");
    // 填写Bucket名称，例如examplebucket。
    private static final String BUCKET_NAME = "big-event-gongcheng1";

    private static Properties loadOssConf() {
        Properties properties = new Properties();
        try (InputStream in = AliOssUtil.class.getClassLoader().getResourceAsStream("oss.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (Exception ignored) {
            // 读不到就交给环境变量，由 resolve 统一判断并给出提示
        }
        return properties;
    }

    private static String resolve(String envKey, String propKey) {
        String value = System.getenv(envKey);
        if (value == null || value.trim().isEmpty()) {
            value = System.getProperty(envKey);
        }
        if (value == null || value.trim().isEmpty()) {
            value = OSS_CONF.getProperty(propKey);
        }
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                    "缺少 OSS 访问凭证：请设置环境变量 " + envKey + "，或在 oss.properties 中配置 " + propKey);
        }
        return value.trim();
    }


    public static String uploadFile(String objectName, InputStream in) throws Exception {

        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
        String region = "cn-beijing";

        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(ENDPOINT)
                .credentialsProvider(new DefaultCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET))
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
        String url = "";

        try {
            // 填写字符串。
            String content = "Hello OSS，你好世界";

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objectName,in);

            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            url = "http://" + BUCKET_NAME + "." + ENDPOINT.substring(ENDPOINT.lastIndexOf("/") + 1) + "/" + objectName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }

}
