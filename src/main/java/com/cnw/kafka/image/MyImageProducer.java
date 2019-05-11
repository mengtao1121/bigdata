package com.cnw.kafka.image;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import util.MyCallback;
import util.Person;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MyImageProducer {
    public static void main(String[] args) {
        //创建Properties对象
        Properties kafkaprops = new Properties();
        kafkaprops.put("bootstrap.servers","mt01:9092,mt02:9092,mt03:9092");
        kafkaprops.put("key.serializer", StringSerializer.class.getName());
        kafkaprops.put("value.serializer", ByteArraySerializer.class.getName());

        //创建kafka生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(kafkaprops);
        try {
            //使用Java IO流读取取文件
            File file = new File("kafka_logo.png");
            FileInputStream inputStream = new FileInputStream(file);
            //创建一个byte[]数组buffer,数组长度为文件内容的长度
            byte[] buffer = new byte[inputStream.available()];
            //将文件内容读入buffer数组
            inputStream.read(buffer);
            //发送消息的key为文件名称，value为buffer数组（文件内容）
            ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>("myFirstTopic", file.getName(), buffer);
            kafkaProducer.send(record,new MyCallback());
            kafkaProducer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
