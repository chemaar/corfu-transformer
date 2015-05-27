package corfu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.weso.corfu.generated.CorfuTransformer;
import org.weso.corfu.generated.TypeMapping;

import utils.CORFUTransformerXMLBind;
import utils.DocumentBuilderHelper;
import utils.PrefixManager;
import utils.TransformerHelper;
import au.com.bytecode.opencsv.CSVReader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;

import exceptions.DocumentBuilderException;

public class MainCorfuTransformer {
	protected static Logger logger = Logger.getLogger(MainCorfuTransformer.class);
	public static final String Empty = "";
	
	public static void main(String []args) throws DocumentBuilderException, IOException{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		
		String xmlFile = "naive/naive-provider.xml";
		Node node = DocumentBuilderHelper.
				getDocumentFromInputStream(
						Thread.currentThread().getContextClassLoader().
						getResourceAsStream(xmlFile)).getFirstChild();
		CorfuTransformer transformation = CORFUTransformerXMLBind.getInstance().restoreIndicator(node);

		
		//Once configuration has been loaded
		InputStream in = new FileInputStream(new File(transformation.getConfig().getInputPath()));
		if( in != null){
			logger.info("Processing file "+transformation.getConfig().getInputPath());
			Model model = ModelFactory.createDefaultModel();
			Reader inputDataReader = new InputStreamReader(in);
			CSVReader reader = new CSVReader(inputDataReader, transformation.getConfig().getSeparator().charAt(0));
			String[] row = null;
			List<TypeMapping> mappings = transformation.getMapping();
			Resource company = null;
			int column = -1;
			String objectValue = MainCorfuTransformer.Empty;
			String lang  = MainCorfuTransformer.Empty;
			String propertyUri  = MainCorfuTransformer.Empty;
			boolean headerProcessed = Boolean.FALSE;
			boolean hasHeader = transformation.getConfig().isHeader();
			while((row = reader.readNext()) != null) {
				//Read and apply mappings
				if(hasHeader && !headerProcessed){
					headerProcessed = Boolean.TRUE;
				
				}else{
					company = createCompany(transformation,model,row);
					logger.info("Creating company: "+company.getURI());
					for(TypeMapping mapping:mappings){
						column = mapping.getColumn().intValue();
						objectValue = checkAndGetValue(column, row);
						//mapping.getGenerates();
						lang  = mapping.getLang();
						propertyUri = mapping.getPropertyUri();
						logger.info("Adding property: "+propertyUri+" with value: "+objectValue);
						addPropertyValue(model, company, propertyUri, objectValue, lang);
					}
					addPropertyValue(model, company, DCTerms.date.toString(), dateFormat.format(cal.getTime()).toString(), null);
					addPropertyValue(model, company, DCTerms.source.toString(), transformation.getConfig().getProvider(), null);
				}
				
			}
			reader.close();
			//Serialize model
			String outputName = transformation.getConfig().getOutputName();
			String file =transformation.getConfig().getOutputPath()+outputName;
			String outputFormat = transformation.getConfig().getOutputFormat().value();
			String uriBase = transformation.getConfig().getNamedGraph();
			logger.info("Serializing model to "+file);
			TransformerHelper.serializeModel(model, PrefixManager.getResourceBundle(), file, outputFormat, uriBase);
		}
	}
	private static Resource createCompany(
			CorfuTransformer transformation,
			Model model,
			String []row) {
		String id = MainCorfuTransformer.Empty;
		//if(transformation.getConfig().getAutogenerate()!=null){
			//Make something
		//}else{
			int column = transformation.getConfig().getId().getColumn().intValue();
			id = checkAndGetValue(column, row);
			
		//}
		String uri = transformation.getConfig().getBaseURI();
		Resource companyResource = model.createResource(uri+id);
		addPropertyValue(model, companyResource, transformation.getConfig().getId().getPropertyUri(), id, null);
		return companyResource;
	}
	
	public static String checkAndGetValue(int column, String []row){
		if(column>=0 && row!= null && column<row.length){
			return row[column];
		}
		return MainCorfuTransformer.Empty;
	}
	
	public static void addPropertyValue(Model model, 
			Resource resource, 
			String propertyUri, 
			String value, 
			String lang){
		Property property = model.getProperty(propertyUri);
		resource.addLiteral(property, value);
	}

}
