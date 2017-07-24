package com.keymanager.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class VNCAddressBookParser {
	public Map<String, String> extractVNCInfo(InputStream is) {
		SAXBuilder sax = new SAXBuilder();
		Document doc;
		Map<String, String> map = new HashMap<String, String>();
		try {
			doc = sax.build(is);
			Element root = doc.getRootElement();
			List<Element> folders = root.getChildren();
			for (Iterator<Element> i = folders.iterator(); i.hasNext();) {
				Element folder = (Element) i.next();
				System.out.println(folder.getAttributeValue("name"));
				List<Element> files = folder.getChildren();
				for (Iterator<Element> j = files.iterator(); j.hasNext();) {
					Element file = (Element) j.next();
					String fileElementName = file.getAttributeValue("name");
					System.out.println(fileElementName);
					String fileElements[] = fileElementName.split("-");
					List<Element> sections = file.getChildren();
					for (Iterator<Element> k = sections.iterator(); k.hasNext();) {
						Element section = (Element) k.next();
						if ("Connection".equals(section.getAttributeValue("name"))) {
							List<Element> hosts = section.getChildren();
							for (Iterator<Element> l = hosts.iterator(); l.hasNext();) {
								Element host = (Element) l.next();
								System.out.println(host.getAttributeValue("value"));
								if(host.getAttributeValue("name").equals("Host")) {
									map.put(fileElements[0], host.getAttributeValue("value") + ":" + fileElements[fileElements.length - 1]);
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return map;
	}
}
