package homework1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

//bean to xml
public class test1 {
	public static void main(String[] args) {
		Person person = new Person();
		person.setName("ddd");
		person.setAge(23);
		Address address = new Address("chonqqing", "Hong Guang Street");
		person.setAddress(address);
		writeXml(person);
	}
	
	public static void writeXml(Person person) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document document;
		try {
			db = dbFactory.newDocumentBuilder();
			document = db.newDocument();
			Element root = makeXml(person, document);
			document.appendChild(root);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File("person.xml")));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
			System.out.println("输出成功");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Element makeXml(Object person, Document document) throws Exception {
		Class clazz = person.getClass();
		Element root = document.createElement(clazz.getName().toString());
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Element element = document.createElement(field.getName().toString());
			System.out.println(field.getName());
			System.out.println(field.get(person));
			if(field.getType().equals(homework1.Address.class)) {		//当属性不为基本类型，目前为了简便，固定了address
				element.appendChild(makeXml(field.get(person), document));
			}else {
				element.appendChild(document.createTextNode(field.get(person).toString()));
			}
			root.appendChild(element);
		}
		return root;
	}
}
