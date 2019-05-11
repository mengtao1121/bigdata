package com.cnw.kafka.json.producer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;

public class MyJsonProducerDemo {

	public static void main(String[] args) {
		Properties kafkaProps = new Properties(); 
		kafkaProps.put("bootstrap.servers", "mt01:9092");
		kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaProps); 
		//����һ��GSON�������ڽ�Java����תΪJSON�ַ���
		Gson gson = new Gson();
		//����һ��List�������ڴ洢10������
		List<Map<String,String>> dataList=new ArrayList<Map<String,String>>();
		//����һ���ַ������������ڴ洢dataList����ת�����json�ַ���
		String jsonData="";
		try {
			//ѭ������10���������ݣ���ӵ�DataList������
			for(int i=0;i<10;i++){
				//ʹ��Map����ģ������ݿ��л�ȡ������
				Map<String,String> dbData=new HashMap<String, String>();
				dbData.put("id", i+"");//id ��¼����
				dbData.put("name", "zhangsan"+i);
				dbData.put("sex", "male");
				dbData.put("address", "����");
				dbData.put("profession", "programer");
				dataList.add(dbData);
			}
			//��dataList����ת�����json�ַ�����ֵ��jsonData����
			jsonData=gson.toJson(dataList);
			System.out.println("Gsonת�����JSON�ַ���Ϊ��"+jsonData);
			//������Ƿ��͵���ϢkeyΪnull����Ŀ�п��Ը���ҵ���߼�ָ��keyֵ��
			ProducerRecord<String, String> record =new ProducerRecord<String, String>("cnwTopic", jsonData);
			producer.send(record);
		    producer.close();
		} catch (Exception e) {
		    e.printStackTrace(); 
		}
	}

}
