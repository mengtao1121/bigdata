package com.cnw.kafka.json.producer;

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

        //声明一个GSON对象，用于将Java对象转为JSON对象
        Gson gson = new Gson();
        //创建一个List对象，用于存储数据
        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
        //声明一个字符串变量，用于存储转换成的json字符串
        String jsonString = "";
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(kafkaprops);
        //一次发送多个java对象
        try {
            for (int i = 0; i < 10; i++) {
                HashMap<String, String> dbData = new HashMap<String, String>();
                dbData.put("id", i + "");
                dbData.put("name", "zhang" + i);
                dbData.put("age", i + "");
                dataList.add(dbData);
            }
            jsonString = gson.toJson(dataList);
            System.out.println("转换后的json="+ jsonString);
            //消息通过json字符串发送出去
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("cnwTopic","jj", jsonString);
            kafkaProducer.send(record,new MyCallback());
            kafkaProducer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
