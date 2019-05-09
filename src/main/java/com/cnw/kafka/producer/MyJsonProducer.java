package com.cnw.kafka.producer;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import util.MyCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class MyJsonProducer {
    public static void main(String[] args) {
        //创建Properties对象
        Properties kafkaprops = new Properties();
        kafkaprops.put("bootstrap.servers","mt01:9092,mt02:9092,mt03:9092");
        kafkaprops.put("key.serializer", StringSerializer.class.getName());
        kafkaprops.put("value.serializer", StringSerializer.class.getName());
        kafkaprops.put("partitioner.class", "util.MyPartitioner");

        //声明一个GSON对象，用于将Java对象转为JSON对象
        Gson gson = new Gson();
        //创建一个List对象，用于存储数据
        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
        try {
            for (int i = 0; i < 10; i++) {
                HashMap<String, String> dbData = new HashMap<String, String>();
                dbData.put("id",i+"");
                dbData.put("name","zhang"+i);
                dbData.put("age",i+"");
                dataList.add(dbData);

                String jsonString = gson.toJson(dataList);
                System.out.println("转换后的json="+ jsonString);
                KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(kafkaprops);
                ProducerRecord<String, String> record = new ProducerRecord<String, String>("cnwTopic", jsonString);
                kafkaProducer.send(record, new MyCallback());
                kafkaProducer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
