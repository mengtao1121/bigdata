package com.cnw.kafka.json.consumer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import util.Person;

import java.util.Arrays;
import java.util.Properties;

public class MyJsonConsumer2 {
    public static void main(String[] args) {
        Properties properties = new Properties();
        //bootstrap.servers指定broker的地址清单，至少两个，防止其中一个宕机
        properties.put("bootstrap.servers","mt01,mt02,mt03");
        //指定key，value反序列化的类型
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        //指定该consumer属于哪个consumer组
        properties.put("group.id","cnwGroup");

        //创建kafka消费者
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        //订阅topic
        kafkaConsumer.subscribe(Arrays.asList("cnwTopic"));
        Gson gson = new Gson();
        try {
            //在指定topic中拉取消息
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record:records){
                //获取json字符串
                String jsonString = record.value();
                //转化为Person对象
                Person person = gson.fromJson(jsonString, Person.class);
                //打印获取的消息
                System.out.println("id:" + person.getId()
                                    + ",name:" + person.getName()
                                    + ",age:" + person.getAge());
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
        }
    }
}
