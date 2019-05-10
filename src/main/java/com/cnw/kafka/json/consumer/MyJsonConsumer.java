package com.cnw.kafka.json.consumer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;

public class MyJsonConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        //bootstrap.servers指定broker的地址清单，至少两个，防止其中一个宕机
        properties.put("bootstrap.servers","mt01,mt02,mt03");
        //指定key，value反序列化的类型
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        //指定该consumer属于哪个consumer组
        properties.put("group.id","cnwGroup");

        //创建消费者
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        //订阅消费的topic
        kafkaConsumer.subscribe(Arrays.asList("cnwTopic"));
        Gson gson = new Gson();
        ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
        try {
            //拉取指定topic的消息
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);

            //处理获取的消息
            for (ConsumerRecord<String, String> record : records){
                //record.value()是json对象，ArrayList由发送消息的java对象定
                mapList = gson.fromJson(record.value(),ArrayList.class);
                for (int i = 0; i < mapList.size(); i++) {
                    HashMap<String, String> hashMap = mapList.get(i);
                    System.out.println("id:"+hashMap.get("id")
                                        + ",name:"+hashMap.get("name")
                                        + ",age:"+hashMap.get("age"));
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
        }


    }
}
