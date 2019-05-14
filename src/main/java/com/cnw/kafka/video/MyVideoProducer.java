package com.cnw.kafka.video;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;

public class MyVideoProducer {
    public static void main(String[] args) {
        //1.设置参数
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "mt01:9092");

        //设置max.request.size的值为5335302，大于2888844  1048576
        /**
         * max.request.size配置项意思是：producer发送的一个批次中的消息的总大小必须小于这个配置项的值。
         * max.request.size配置项的默认值为1048576，我们发送的视频大小为2888844，2888844>1048576。
         * 我们需要将此配置项的值调大，在这里我们设置为5335302。
         */
        kafkaProps.put("max.request.size", 5335302);
        kafkaProps.put("key.serializer", StringSerializer.class.getName());
        kafkaProps.put("value.serializer", StringSerializer.class.getName());
        //2.new一个kafkaProducer
        KafkaProducer<String, byte[]> kafkaProducer = new KafkaProducer<String, byte[]>(kafkaProps);
        //3.获取需要发送的消息
        try {
            //使用Java IO流读取取文件
            File file = new File("test.mp4");
            FileInputStream inputStream = new FileInputStream(file);
            //创建一个byte[]数组buffer,数组长度为文件内容的长度
            byte[] buffer = new byte[inputStream.available()];
            //将文件内容读入buffer数组
            inputStream.read(buffer);
            //发送消息的key为文件名称，value为buffer数组（文件内容）
            ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>("myFirstTopic", file.getName(), buffer);
            Future<RecordMetadata> future = kafkaProducer.send(record);
            //打印出已发送消息的相关消息
            RecordMetadata recordMetadata = future.get();
            //获取发送到哪个topic
            String topic = recordMetadata.topic();
            //获取发送到哪个partition
            int partition = recordMetadata.partition();
            //获取发送的offset
            long offset = recordMetadata.offset();
            System.out.println("serializedKeySize:"+recordMetadata.serializedKeySize());
            System.out.println("serializedValueSize:"+recordMetadata.serializedValueSize());
            System.out.println("topic="+topic+",partition="+partition+",offset="+offset);
            //g关闭kafkaProducer
            kafkaProducer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
