package utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.weso.corfu.generated.CorfuTransformer;

import exceptions.CorfuModelException;


public class CORFUTransformerXMLBind {

	private static Logger logger = Logger.getLogger(CORFUTransformerXMLBind.class);

	public static final String PACKAGE_NAME = CorfuTransformer.class.getPackage().getName();

	private Unmarshaller unmarshaller;
	private Marshaller marshaller;

	private CORFUTransformerXMLBind() {
		try {
			JAXBContext jc = JAXBContext.newInstance(PACKAGE_NAME);
			this.unmarshaller = jc.createUnmarshaller();
			this.marshaller = jc.createMarshaller();
			//            unmarshaller.setEventHandler(eventHandler);
			//            marshaller.setEventHandler(eventHandler);
		} catch(JAXBException e) {
			//
		}
	}

	public CorfuTransformer restoreIndicator(Node node) {
		try {
			return (CorfuTransformer) unmarshaller.unmarshal(node);  

			//return (IndicatorType) unmarshaller.unmarshal(node);
		} catch (JAXBException je) {
			je.printStackTrace();
			throw new CorfuModelException(je, "CorfuTransformer: Unmarshalling indicator.");
		}
	}


	//	        public Document serializeIndicator(Dataset concept) {
	//	                logger.debug("Serializing dataset XML");
	//	                try {
	//	                        Document doc = DocumentBuilderHelper.getEmptyDocument();
	//	                        marshaller.marshal(concept, doc);
	//	                        return doc;
	//	                } catch (JAXBException je) {
	//	                        throw new LodinModelException(je, "Dataset: Marshalling concept.");
	//	                } catch (DocumentBuilderException e) {
	//	                        throw new LodinModelException(e, "Dataset: Marshalling concept.");
	//	                }
	//	   
	//	        }

	/**
	 * Singleton field
	 */
	private static CORFUTransformerXMLBind instance;

	/**
	 * Singleton method
	 * 
	 * @return The singleton instance of this class 
	 * @throws XmlBindException
	 */
	public static CORFUTransformerXMLBind getInstance() {
		if (instance == null) {
			instance = new CORFUTransformerXMLBind();
		}
		return instance;
	}       
}
