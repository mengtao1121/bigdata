package com.cnw.kafka.video;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class MyVideoConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers","mt01:9092,mt02:9092");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", ByteArrayDeserializer.class.getName());
        properties.put("group.id","group");
        /**
         * max.partition.fetch.bytes配置项的意思是：对于每个partition，kafka broker返回的最大数据量。
         * max.partition.fetch.bytes配置项的默认值为1048576，小于我们发送的视频大小：2888844。
         * 我们需要将此配置项的值调大，在这里我们设置为10485760。扩大了十倍。
         */
        properties.put("max.partition.fetch.bytes", "10485760");

        //创建消费者
        KafkaConsumer<String, byte[]> kafkaConsumer = new KafkaConsumer<String, byte[]>(properties);
        kafkaConsumer.subscribe(Arrays.asList("myFirstTopic"));

        try {
            while (true) {
                //poll消息
                ConsumerRecords<String, byte[]> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, byte[]> record : records){
                    //获取消息key值，作为文件的名称
                    String fileName = record.key();
                    //获取消息value值，作为文件内容，使用IO流将内容写入文件
                    byte[] message = record.value();
                    FileOutputStream fileOutputStream = new FileOutputStream("E:" + fileName, false);
                    fileOutputStream.write(message);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
        }
    }
}
