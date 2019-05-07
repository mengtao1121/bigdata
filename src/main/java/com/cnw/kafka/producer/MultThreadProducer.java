package com.cnw.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import util.MyCallback;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultThreadProducer extends Thread{

    private KafkaProducer<String, String> kafkaProducer;
    private String topic;

    public MultThreadProducer(String topicName){
        //创建Properties对象
        Properties kafkaprops = new Properties();
        kafkaprops.put("bootstrap.servers","mt01:9092,mt02:9092,mt03:9092");
        kafkaprops.put("key.serializer", StringSerializer.class.getName());
        kafkaprops.put("value.serializer", StringSerializer.class.getName());

        //创建kafkaproducer,用于与kafka集群交互，发送消息
        kafkaProducer = new KafkaProducer<String, String>(kafkaprops);
        topic = topicName;
    }

    @Override
    public void run() {
        int msgNu = 0;
        for (int i = 0; i < 10; i++) {
            String mesContent = "msg_"+msgNu;
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, msgNu + "", mesContent);
            kafkaProducer.send(record,new MyCallback());
            msgNu++;
        }
        kafkaProducer.flush();
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 2; i++) {
            es.execute(new MultThreadProducer("cnwTopic"));
        }
    }
}
