package com.cnw.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import util.MyCallback;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MyFirstProducer {
    public static void main(String[] args) {
        //创建Properties对象
        Properties kafkaprops = new Properties();
        kafkaprops.put("bootstrap.servers","mt01:9092,mt02:9092,mt03:9092");
        kafkaprops.put("key.serializer", StringSerializer.class.getName());
        kafkaprops.put("value.serializer", StringSerializer.class.getName());
        kafkaprops.put("partitioner.class", "util.MyPartitioner");

        //创建kafkaproducer,用于与kafka集群交互，发送消息
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(kafkaprops);

        //创建消息
//        ProducerRecord<String, String> record = new ProducerRecord<String,String>("cnwTopic", "hello kafka callback1");
        /**
         * Fire-and-forget----此方法用来发送消息到broker，不关注消息是否成功到达。
         * 大部分情况下，消息会成功到达broker，因为kafka是高可用的，producer会自动重试发送。
         * 但是，还是会有消息丢失的情况
         */
//        try {
//            //发送消息
//            kafkaProducer.send(record);
//            kafkaProducer.close();//flush操作
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        /**
         * Synchronous Send(同步发送)---发送一个消息，send()方法返回一个Future对象，
         * 使用此对象的get()阻塞方法可以查看send()方法是否执行成功。
         * get()有返回值才会继续发送消息
         */
//        Future<RecordMetadata> future = kafkaProducer.send(record);
//        try {
//            RecordMetadata recordMetadata = future.get();
//            String topic = recordMetadata.topic();
//            int partition = recordMetadata.partition();
//            long offset = recordMetadata.offset();
//            System.out.println("topic:"+topic+",partition"+partition+",offset:"+offset);
//            kafkaProducer.close();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        /**
         * Asynchronous Send(异步发送)---以回调函数的形式调用send()方法，
         * 当收到broker的响应，会触发回调函数执行。
         */
//        kafkaProducer.send(record,new MyCallback());
//        kafkaProducer.close();

        /**
         * 自定义分区器
         */
        ProducerRecord<String, String> record =
                new ProducerRecord<String,String>("cnwTopic", "1","11");
        kafkaProducer.send(record,new MyCallback());
        kafkaProducer.flush();
    }
}
