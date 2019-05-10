package com.cnw.kafka.json.producer;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import util.MyCallback;
import util.Person;

import java.util.Properties;

public class MyJsonProducer2 {
    public static void main(String[] args) {
        //创建Properties对象
        Properties kafkaprops = new Properties();
        kafkaprops.put("bootstrap.servers","mt01:9092,mt02:9092,mt03:9092");
        kafkaprops.put("key.serializer", StringSerializer.class.getName());
        kafkaprops.put("value.serializer", StringSerializer.class.getName());
        kafkaprops.put("partitioner.class", "util.MyPartitioner");

        //创建kafka生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(kafkaprops);
        //一条一条消息的发送
        Person person;
        Gson gson = new Gson();
        try {
            for (int i = 0; i < 10; i++) {
                person = new Person("id"+i+"","zhangsan"+i+"",i+10);
                String jsonPerson = gson.toJson(person);
                System.out.println("转换后的json字符串:"+jsonPerson);
                //生成kafka发送的消息
                ProducerRecord<String,String> record = new ProducerRecord<String,String>("cnwTopic", jsonPerson);
                kafkaProducer.send(record, new MyCallback());
            }
            kafkaProducer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
