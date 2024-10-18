//package indi.LoCalm.mqtt;
//
//import org.eclipse.paho.client.mqttv3.*;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//
///**
// * 订阅示例
// */
//public class SubscribeSample {
//
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
//    // QoS 0、最多交付一次
//    // QoS 1、至少交付一次
//    // QoS 2、只交付一次
//    private static final int QOS = 1;
//
//    public static void main(String[] args) {
//        try {
//            // 创建客户端
//            // MqttClient client = new MqttClient(BROKER, CLIENT_ID);
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
//            // 设置回调
//            client.setCallback(new MqttCallback() {
//
//                /**
//                 * 在断开连接时调用，主要用于重连
//                 */
//                public void connectionLost(Throwable cause) {
//                    System.out.println("connectionLost: " + cause.getMessage());
//                }
//
//                /**
//                 * 接收订阅的消息后执行的方法
//                 * @param topic 主题
//                 * @param message 消息
//                 */
//                public void messageArrived(String topic, MqttMessage message) {
//                    System.out.println("topic: " + topic);
//                    System.out.println("Qos: " + message.getQos());
//                    System.out.println("message content: " + new String(message.getPayload()));
//
//                }
//
//                /**
//                 * 接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用
//                 * @param token 令牌
//                 */
//                public void deliveryComplete(IMqttDeliveryToken token) {
//                    System.out.println("deliveryComplete---------" + token.isComplete());
//                }
//            });
//            // 连接
//            client.connect(options);
//            // 订阅
//            client.subscribe(TOPIC, QOS);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//
