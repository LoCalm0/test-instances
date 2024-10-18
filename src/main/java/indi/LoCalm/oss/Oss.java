//package indi.LoCalm.oss;
//
//import com.aliyun.oss.ClientException;
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
//import com.aliyun.oss.OSSException;
//import com.aliyun.oss.model.OSSObject;
//import com.aliyun.oss.model.OSSObjectSummary;
//import com.aliyun.oss.model.ObjectListing;
//
//import java.io.*;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
////<dependency>
////<groupId>com.aliyun.oss</groupId>
////<artifactId>aliyun-sdk-oss</artifactId>
////<version>3.17.4</version>
////</dependency>
//public class Oss {
//    private static final String ENDPOINT = "https://oss-cn-hangzhou.aliyuncs.com";
//    private static final String ACCESS_KEY_ID = "";
//    private static final String SECRET_ACCESS_KEY = "";
//    private static final String BUCKET_NAME = "yxt-test1111";
//
//    public static void main(String[] args) {
////        deleteTheFile();
//        String fullPath = "https://yxt-test1111.oss-cn-hangzhou.aliyuncs.com/image/2024-07-13 17:19:13-icon_vuepress_reco.png";
//
//        try {
//            URL url = new URL(fullPath);
//            String path = url.getPath();
//            String trimmedPath = path.substring(1);
//            System.out.println(path);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void uploadTheFile() {
//        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
//        String fileName = "image";
//        String filePath = "D:/Software/图片/89751717_p0.jpg";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, SECRET_ACCESS_KEY);
//        try {
//            ossClient.putObject(BUCKET_NAME, fileName, new FileInputStream(filePath));
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//    }
//
//    public static void downloadTheFile() {
//        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
//        String objectName = "89751717_p0.jpg";
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, SECRET_ACCESS_KEY);
//
//        try {
//            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元数据。
//            OSSObject ossObject = ossClient.getObject(BUCKET_NAME, objectName);
//            // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
//            InputStream content = ossObject.getObjectContent();
//            if (content != null) {
//                Files.copy(content, Paths.get("D:/test/Skadi.jpg"));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//                while (true) {
//                    String line = reader.readLine();
//                    if (line == null) {
//                        break;
//                    }
//                    System.out.println("\n" + line);
//                }
//                // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
//                content.close();
//            }
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//    }
//
//    public static void enumerateTheDocuments() {
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, SECRET_ACCESS_KEY);
//
//        try {
//            // ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
//            ObjectListing objectListing = ossClient.listObjects(BUCKET_NAME);
//            // objectListing.getObjectSummaries获取所有文件的描述信息。
//            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
//                System.out.println(" - " + objectSummary.getKey() + "(size = " + objectSummary.getSize() + ")");
//            }
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//    }
//
//    public static void deleteTheFile() {
//        String objectName = "image/2024-07-13 17:19:13-icon_vuepress_reco.png";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, SECRET_ACCESS_KEY);
////        https://yxt-test1111.oss-cn-hangzhou.aliyuncs.com/image/2024-07-13 15:19:17-home-bg.jpg
//        try {
//            // 删除文件。
//            ossClient.deleteObject(BUCKET_NAME, objectName);
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//    }
//
//
//}
