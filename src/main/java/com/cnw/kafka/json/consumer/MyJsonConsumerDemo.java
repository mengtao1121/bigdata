package com.cnw.kafka.json.consumer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.google.gson.Gson;

public class MyJsonConsumerDemo {
	public static void main(String[] args) {
		Properties props=new Properties();
		props.put("bootstrap.servers", "hadoop01:9092");
		props.put("group.id", "group1");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String,String> consumer=new KafkaConsumer<String,String>(props);
		consumer.subscribe(Arrays.asList("mySecondTopic"));
		Gson gson = new Gson();//����һ��GSON�������ڽ�JSON�ַ���תΪJava����
		try {
			while(true){
				ConsumerRecords<String, String> records=consumer.poll(100);
				for(ConsumerRecord<String, String> record : records){
					//��ȡ��Ϣ��ֵ����ֵ��һ��json�ַ���
					String jsonValue=record.value();
					//��json�ַ���ת��ΪList����
					List dataList = gson.fromJson(jsonValue, List.class);
					System.out.println("���յ������ݣ�");
					for(int i=0;i<dataList.size();i++){
						Map data=(Map)dataList.get(i);
						//��ȡ����
						System.out.println("id:"+data.get("id")
											+",name:"+data.get("name")
											+",sex:"+data.get("sex")
											+",address:"+data.get("address")
											+",profession:"+data.get("profession"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			consumer.close();
		}
	}

}
