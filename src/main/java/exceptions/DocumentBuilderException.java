package exceptions;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class DocumentBuilderException extends Exception {

	public DocumentBuilderException(ParserConfigurationException e) {
		// TODO Auto-generated constructor stub
	}

	public DocumentBuilderException(SAXException e) {
		// TODO Auto-generated constructor stub
	}

	public DocumentBuilderException(IOException e) {
		// TODO Auto-generated constructor stub
	}

	public DocumentBuilderException(DocumentBuilderException e) {
		// TODO Auto-generated constructor stub
	}

}
