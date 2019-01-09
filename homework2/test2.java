package homework2;

import java.io.File;
import java.lang.reflect.Field;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import homework2.Address;
import homework2.Person;

public class test2 {
	public static void main(String[] args) {
		Person person = new Person();
		readXml(person);
	}
	
	public static void readXml(Object person) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
			Document document = db.parse(new File("person.xml"));
			createBean(person, document);
			System.out.println(person);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createBean(Object person, Document document) throws Exception{
		Class clazz = person.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Element eroot = document.getDocumentElement();
		for (Field field : fields) {
			field.setAccessible(true);
			NodeList nodeList = eroot.getElementsByTagName(field.getName());
			Element element = (Element) nodeList.item(0);
			if(field.getType().equals(homework2.Address.class)) {			//不是基础类型，现用address代替
				//System.out.println(field.getType().toString().substring(6));
				Class clazz1 = Class.forName(field.getType().toString().substring(6));		//person可能没有创建address
				//Object oh = field.get(person);
				Object obj = clazz1.newInstance();				//新建address进行赋值
				createBean(obj, document);			
				field.set(person, obj);
			}else {								//基础类型直接赋值
				Object value = null;
				if(field.getType().equals(String.class)) {
					value = element.getTextContent();
				}else if(field.getType().equals(int.class)) {
					value = Integer.parseInt(element.getTextContent());
				}
				field.set(person, value);
			}
		}
	}
}
