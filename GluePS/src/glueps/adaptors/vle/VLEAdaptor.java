package glueps.adaptors.vle;

import java.util.Map;

import glueps.core.gluepsManager.GLUEPSManagerApplication;

public interface VLEAdaptor {
	
	public IVLEAdaptor getVLEAdaptor(GLUEPSManagerApplication applicationRest, Map<String, String> parameters);

}
