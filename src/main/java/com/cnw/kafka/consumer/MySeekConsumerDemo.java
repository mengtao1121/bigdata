package com.cnw.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

public class MySeekConsumerDemo {
    public static void main(String[] args) {
        //创建properties对象，设置配置信息
        Properties props = new Properties();
        props.put("bootstrap.servers","mt01:9092,mt02:9092,mt03:9092");
        props.put("group.id","group4");
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer",StringDeserializer.class.getName());
        props.put("value.deserializer",StringDeserializer.class.getName());

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(props);
        //订阅消息
        //consumer.subscribe(Arrays.asList("cnwTopic"));
        TopicPartition partition0 = new TopicPartition("cnwTopic", 0);
        consumer.assign(Arrays.asList(partition0));
        consumer.seek(partition0,0);

        while(true){
            try {
                //consumer.poll(1000);
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String,String> recod : records){
                    System.out.println("MySeekToBeginningConsumer消费的消息：partition="+recod.partition()
                            +",offset="+recod.offset()+",key="+recod.key()+",");
                }

            } catch (Exception e){
                e.printStackTrace();
            } finally {
                //关闭consumer,而且会通知group，这样就可以将其消费者的partition分配给其他consumer
                //consumer.close();
            }
        }
    }
}
