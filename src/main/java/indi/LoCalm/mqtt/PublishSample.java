//package indi.LoCalm.mqtt;
//
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//
//
////<dependency>
////<groupId>org.eclipse.paho</groupId>
////<artifactId>org.eclipse.paho.client.mqttv3</artifactId>
////<version>1.2.5</version>
////</dependency>
//
///**
// * 发布示例
// */
//public class PublishSample {
//    // 连接地址
//    private static final String BROKER = "tcp://192.168.168.239:1883";
//    // 主题
//    private static final String TOPIC = "testtopic";
//    // 用户名
//    private static final String USERNAME = "admin";
//    // 密码
//    private static final String PASSWORD = "admin";
//    // 客户端 ID
//    private static final String CLIENT_ID = "publish_client";
//    // 内容
//    private static final String CONTENT = "Hello MQTT";
//    // QoS 0、最多交付一次
//    // QoS 1、至少交付一次
//    // QoS 2、只交付一次
//    private static final int QOS = 0;
//
//    public static void main(String[] args) {
//        try {
//            // 创建客户端
////            MqttClient client = new MqttClient(BROKER, CLIENT_ID);
//            MqttClient client = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
//            // 连接参数
//            MqttConnectOptions options = new MqttConnectOptions();
//            // 设置用户名和密码
//            options.setUserName(USERNAME);
//            options.setPassword(PASSWORD.toCharArray());
//            // 设置连接超时时间 (秒)
//            options.setConnectionTimeout(60);
//            // 设置心跳间隔 (秒)
//            options.setKeepAliveInterval(60);
//            // 连接
//            client.connect(options);
//            // 创建消息并设置 QoS
//            MqttMessage message = new MqttMessage(CONTENT.getBytes());
//            message.setQos(QOS);
//            // 发布消息
//            client.publish(TOPIC, message);
//            System.out.println("Message published");
//            System.out.println("topic: " + TOPIC);
//            System.out.println("message content: " + CONTENT);
//            // 关闭连接
//            client.disconnect();
//            // 关闭客户端
//            client.close();
//        } catch (MqttException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
