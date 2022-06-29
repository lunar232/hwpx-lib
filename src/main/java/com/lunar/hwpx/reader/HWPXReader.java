package com.lunar.hwpx.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** HWPX 파일을 읽는다.
 * @author USER
 *
 */
public class HWPXReader {

	/** hwpx 파일의 전체 텍스트를 추출
	 * @param args
	 */
	public static String readText(String filePath){

		if(filePath == null || filePath.equals("") || !filePath.endsWith(".hwpx")) return null;

		//파싱할 파일
		File targetFile = new File(filePath);

		StringBuilder textStringBuilder = new StringBuilder();

		try(
			ZipFile zipFile = new ZipFile(targetFile);
			FileInputStream fis = new FileInputStream(zipFile.getName());
			ZipInputStream zis = new ZipInputStream(fis);
		){
			ZipEntry entry;

			while((entry = zis.getNextEntry()) != null){
				System.out.println(entry.getName());

				//section0, 1.. .xml에서 내용 추출
				if(entry.getName().contains("section") && entry.getName().endsWith(".xml")){

					NodeList nodeList = parseDocument(zipFile.getInputStream(entry), "//hp:t");

					for(int i = 0; i < nodeList.getLength(); i++){
						Node node = nodeList.item(i);

//						System.out.println(node.getNodeName());
//						System.out.println(node.getLocalName());
						textStringBuilder.append(node.getTextContent() + "\n");
					}

				}

			}

		}catch(Exception e){
			e.printStackTrace();
			return null;
		}

		String resultString = textStringBuilder.toString();

		return resultString.equals("") ? null : resultString;
	}

	/** 문서를 파싱하고 expression에 해당하는 NodeList를 반환
	 * @param ins
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	private static NodeList parseDocument(InputStream ins, String expression) throws Exception {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(ins);
		//doc.getDocumentElement().normalize();

		//XPath 생성
		XPath xPath = XPathFactory.newInstance().newXPath();
		xPath.setNamespaceContext(new XmlNamespaceResolver(doc));

		//쿼리식 설정
		//String expression = "//*[local-name()='font']";
//		expression = "//hh:font";
//		expression = "//hp:t";

		//expression이 비어 있으면 전체 노드 반환
		if(expression == null || expression.equals("")) {
			expression = "//*";		//전체 노드
		}

		XPathExpression expr = xPath.compile(expression);

		NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

		return nodeList;
	}
}
