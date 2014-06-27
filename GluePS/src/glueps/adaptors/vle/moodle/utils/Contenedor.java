package glueps.adaptors.vle.moodle.utils;

import glueps.core.model.Activity;

public class Contenedor {
	
	/*Tipo del modulo*/
	private String tipoModulo;
	/**/
	private String subTipoModulo; 
	/*Identificador del modulo*/
	private int modId;
	/*Nombre del modulo*/
	private String modName;
	/**/
	private String GroupingId;
	
	private String groupId;
	/**/
	private String Location; 
	/**/
	private int numSection; 
	/*Para indicar si es un nodo donde se ha creado una sección*/
	private boolean createSection; 
	/*De momento guardo toda la actividad, ya veremos luego. Si elimino parametros de arriba
	 * y dejo actividad o elimino actividad y añado nuevos atributos.
	 * */
	private String idActivity; 
	/**/
	private String AllTest;
	/**/
	private String Options;
	/**/
	private String referens;	
	/**/
	private String popUP=null;
	
	public String getTipoModulo() {
		return tipoModulo;
	}
	public void setTipoModulo(String tipoModulo) {
		this.tipoModulo = tipoModulo;
	}
	public String getSubTipoModulo() {
		return subTipoModulo;
	}
	public void setSubTipoModulo(String subTipoModulo) {
		this.subTipoModulo = subTipoModulo;
	}
	public int getModId() {
		return modId;
	}
	public void setModId(int modId) {
		this.modId = modId;
	}
	public String getModName() {
		return modName;
	}
	public void setModName(String modName) {
		this.modName = modName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupingId() {
		return GroupingId;
	}
	public void setGroupingId(String groupingId) {
		GroupingId = groupingId;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public int getNumSection() {
		return numSection;
	}
	public void setNumSection(int numSection) {
		this.numSection = numSection;
	}
	public boolean isCreateSection() {
		return createSection;
	}
	public void setCreateSection(boolean createSection) {
		this.createSection = createSection;
	}
	public String getIdActivity() {
		return idActivity;
	}
	public void setIdActivity(String activity) {
		this.idActivity = activity;
	}
	public String getAllTest() {
		return AllTest;
	}
	public void setAllTest(String allTest) {
		AllTest = allTest;
	}
	public String getOptions() {
		return Options;
	}
	public void setOptions(String options) {
		Options = options;
	}
	public String getReferens() {
		return referens;
	}
	public void setReferens(String referens) {
		this.referens = referens;
	}
	public String getPopUP() {
		return popUP;
	}
	public void setPopUP(String popUP) {
		this.popUP = popUP;
	}

	

}
