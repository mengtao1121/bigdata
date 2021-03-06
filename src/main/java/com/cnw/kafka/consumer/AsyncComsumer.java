package com.cnw.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class AsyncComsumer {
    public static void main(String[] args) {
        //kafkaConsumer配置
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "mt01:9092,mt02:9092");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("bootstrap.servers", StringDeserializer.class.getName());
        //指定kafkaConsumer属于哪个群组
        properties.put("group.id", "cnwGroup");
        //禁掉kafka自动提交offset
        properties.put("enable.auto.commit", "false");
        //根据properties创建kafkaConsumer
        KafkaConsumer<String,String> kafkaConsumer = new KafkaConsumer<String,String>(properties);
        //kafkaConsumer订阅topic
        kafkaConsumer.subscribe(Collections.singleton("cnwTopic"));

        try {
            while(true) {
                //kafkaConsumer拉取消息，100表示拉取100ms内的消息,这个值取决于应用程序对响应速度的要求
                ConsumerRecords<String,String> records = kafkaConsumer.poll(100);
                //遍历获取的消息进行业务处理
                for(ConsumerRecord<String,String> record : records){
                    //具体业务处理
                    System.out.println("MyFirstComsumer消费的消息："+"partition="+record.partition()
                            +",offset="+record.offset()+",key="+record.key()+",value="+record.value());
                }
                //异步提交，至少消费一次
                kafkaConsumer.commitAsync(new OffsetCommitCallback() {
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                        if (exception != null){
                            exception.printStackTrace();
                        }
                    }
                });
            }
        } finally {
            kafkaConsumer.close();
        }
    }
}
