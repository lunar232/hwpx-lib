package com.lunar.hwpx.reader;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

public class XmlNamespaceResolver implements NamespaceContext {

	private Document sourceDocument;

	public XmlNamespaceResolver(Document document) {
		sourceDocument = document;
	}

	@Override
	public String getNamespaceURI(String prefix) {
		if(prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)){
			return sourceDocument.lookupNamespaceURI(null);
		} else {
			return sourceDocument.lookupNamespaceURI(prefix);
		}
	}

	@Override
	public String getPrefix(String namespaceURI) {
		return sourceDocument.lookupPrefix(namespaceURI);
	}

	@Override
	public Iterator getPrefixes(String namespaceURI) {
		return null;
	}

}
