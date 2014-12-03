package glueps.adaptors.ld;

import java.io.File;

import glueps.adaptors.ld.edit2.EDIT2Adaptor;
import glueps.adaptors.ld.glueps.GluepsAdaptor;
import glueps.adaptors.ld.imsld.IMSLDAdaptor;
import glueps.adaptors.ld.pp.PPAdaptor;
import glueps.adaptors.ld.ppc.PPCAdaptor;
import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.mediawiki.MediaWikiAdaptor; 
import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.LearningEnvironment;

public final class LDAdaptorFactory {

	private GLUEPSManagerApplication app;
	private String UPLOAD_DIRECTORY;
	private String SCHEMA_LOCATION;
	
	public static final String IMSLD_TYPE = "IMS LD";
	public static final String PPC_TYPE = "PPC";
	public static final String T2_TYPE = "T2";
	public static final String GLUEPS_TYPE = "GLUEPS";
	public static final String PP_TYPE = "PP";

	public LDAdaptorFactory(GLUEPSManagerApplication applicationRest){
		
		this.app = applicationRest;
		this.UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
		this.SCHEMA_LOCATION = app.getAppPath()+"/schemas/";
		
	}
	
	public ILDAdaptor getLDAdaptor(String ldType, String designId){
		if(ldType==null) return null;
		
		if(ldType.equals(IMSLD_TYPE)){
			String uploadDir = UPLOAD_DIRECTORY + designId + File.separator;
			IMSLDAdaptor adaptor = new IMSLDAdaptor(SCHEMA_LOCATION, uploadDir);
			return adaptor;
		}else if(ldType.equals(PPC_TYPE)){
			
			PPCAdaptor adaptor = new PPCAdaptor(designId);
			
			return adaptor;
			
		}else if(ldType.equals(T2_TYPE)){
			
			EDIT2Adaptor adaptor = new EDIT2Adaptor(designId);
			
			return adaptor;
			
		}else if(ldType.equals(GLUEPS_TYPE)){
			
			GluepsAdaptor adaptor = new GluepsAdaptor(designId);
			
			return adaptor;
			
		}else if(ldType.equals(PP_TYPE)){
		
			PPAdaptor adaptor = new PPAdaptor(designId);
		
			return adaptor;
		
		}else return null;
		
	}
}
