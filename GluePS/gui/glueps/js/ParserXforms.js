dojo.require("dojox.xml.DomParser");
dojo.require("dojox.xml.parser");
var parserXforms = {

	camposFormulario:"",
	//Contiene el xforms devuelto por glue
	data:"",
    init: function(){

    },
    getNodes:function(expreRegular,node,attribute){
    	if (node==null){
		    var listNodes=new Array();	
		   	var root=$(this.data).find("."); 
			for(var i=0; i<root.length; i++){
			 if (root[i].localName.search(expreRegular) !=-1){
				 listNodes.push(root[i]);
			 }		 
			}
		    return listNodes;	
    	}else{
    		//Buscamos un nodo dentro de otro
    		if (attribute==null){
    			//JUAN: Modifico para que si hay más de un nodo se devuelva un array
    		    var listNodes=new Array();	
    			for(var j=0; j<node.childNodes.length;j++){
    				if ((node.childNodes[j].localName !=null )&&(node.childNodes[j].localName.search(expreRegular)!=-1)){
    					listNodes.push(node.childNodes[j]);
    				}    				
    			}
    			if (listNodes.length == 1){
					return listNodes[0];
    			} else {
    				return listNodes;
    			}
    		}else{
    		//Buscamos atributo de un nodo	
    			for(var k=0; k<node.attributes.length; k++){
    				if ((node.attributes[k].localName !=null )&&(node.attributes[k].localName.search(attribute)!=-1)){
    					return node.attributes[k];
    				}    				
    			}    			
    		}	    		
    	}
    },
    /**
     * Configuration form specific set up. Abstract method overriden from moodleform inheritance.
     * All the data fields must be defined inside, including the optional ones.
     * Custom data passed through the second parameter of class constructor can be found in _customdata inherited attribute.
     * In this case, an XForms definition file saved in _customdata is parsed an UI elements are generated to satisfy its contents.
     *
     * LIMITATION: only <inpyut> and <upload> IU elements are parsed.
	 *
	 * @return	boolean		returns true if any filed has been printed or false in any other case.
     */
    printformfields:function(){
    	this.camposFormulario=new Array();
    	//Para obtener todos los nodos que comienzan por xf:
		var xformUINodes = this.getNodes(/^xf:/,null,null);    
		
		var printed = 0;
		
		 for (var i=0; i<xformUINodes.length;i++) {
			var UINode = xformUINodes[i];
			var tagType=UINode.localName.substring(UINode.localName.indexOf(":")+1,UINode.localName.length);
			switch(tagType){
			
			// Add more cases to parser more xforms elements
			
			// this case is to parse a input field
			case "input":
				this.printXFormsInput(UINode);
				i++;
				printed++;
				break;
				
			// this case is to parse a file field
			case "upload":
				this.printXFormsUpload(UINode);
				i++;
				printed++;
				break;
			
			case "submit":
				i++;
				break;
			
			case "label":
				if (UINode.parentNode.localName != "xf:item"){
					//$this->printXFormsLabel($UINode->textContent);
					this.printXFormsLabel(UINode);
				//$e = $UINode->getElementsByTagName("label")->item(0);
				//$this->printXFormsUpload($UINode);
				printed++;
				}

				break;
				
				// this case is to parse a select1 (drop select) field
			case "select1":
				this.printXFormsSelect1(UINode);
				i++;
				printed++;
				break;
				
			case "select":
				this.printXFormsSelect(UINode);
				i++;
				printed++;
				break;
			
			// Nothing to do in dafult case.
			default:
				;
			}			
		 }
		 
		 // si no se han imprimido campos.. ¿no hay nada que configurar?
		 if (printed <= 0){
			return false;
		 }
		 
		 // si que se ha imprimido algun campo del formulario.
		 return true;
	},
	/**
     * Reads a XForms 'input' element definition and inserts a proper moodleform input element in the form.
     *
     * LIMITATION: use '@ref' attribute for generating the name of the form element; it doesn't consider 'bind' XForms elements/attributes
     * LIMITATION: XForms label is considered as required
     * LIMITATION: XSD type checks are not considered
     *
     * @param $UINode   DOM node containing the XForms 'input' element
     */
	 printXFormsInput:function(UINode) {
        // extract data from XForms 'input' element
        var labels =this.getNodes(/^xf:label/,UINode,null);
        var refs = this.getNodes(null,UINode,/^ref/);
        var fieldid = this.encodeXFormsInfo("input", "", refs.nodeValue);
        
        var element_name = this.encodeXFormsInfo("input", "", refs.nodeValue);
		
        //new text element in form
        var label = labels.textContent.trim();
        var nodoDefect=refs.nodeValue.substring(refs.nodeValue.lastIndexOf("/")+1,refs.nodeValue.length);
        //var defaultInput = this.getNodes( new RegExp("^"+nodoDefect),null,null)[0].textContent;
        var nodes = this.getNodes( new RegExp("^"+nodoDefect),null,null);
        //var defaultInput = nodes[nodes.length-1].textContent;
        
        //Utilizamos el parser de dojo para obtener correctamente los nodos hijos
        var root = dojox.xml.DomParser.parse(parserXforms.data);
        //Se obtiene el nodo más interno
        var cadena = nodes[nodes.length-1].nodeName.toLowerCase();
		if (root.getElementsByTagName(cadena).length == 0)
		{
			cadena = cadena.substring(3);
		}
		var nodos=root.getElementsByTagName(cadena);
		//Se obtiene el nodo más interno
		var nodo = nodos[nodos.length-1];
		if (nodo.childNodes.length > 0)
		{
			var defaultInput = nodo.childNodes[0].nodeValue;
		}
		else{
			var defaultInput = "";
		}
		
		
        var pInput=document.createElement("p");
        pInput.innerHTML=label;
        var nodoInput=document.createElement("input");
        name="name";
        nodoInput.setAttribute("name",fieldid);
        nodoInput.setAttribute("type","text");
        nodoInput.setAttribute("value",defaultInput);
        pInput.appendChild(nodoInput);
        this.camposFormulario.push(pInput);
        //this.camposFormulario.push("<p>"+label+"<input name='"+fieldid+"' type='text' value='"+defaultInput+"'/></p>");
		//print ("<p>"+label+"<input name='"+fieldid+"' type='text' value='"+defaultInput+"'/></p>");
    },
    /**
     * Reads a XForms 'upload' element definition and inserts a proper moodleform upload element in the form.
     *
     * LIMITATION: use '@ref' attribute for generating the name of the form element; it doesn't consider 'bind' XForms elements/attributes
     * LIMITATION: XForms label is considered as required
     *
     * @param $UINode   DOM node containing the XForms 'upload' element
     */
    printXFormsUpload:function(UINode) {
	  
	  
		// extract data from XForms 'upload' element
    	var labels =this.getNodes(/^xf:label/,UINode,null);
    	var refs = this.getNodes(null,UINode,/^ref/);
      
		
		var label = labels.textContent;
		var fieldid = this.encodeXFormsInfo("upload", "", refs.nodeValue);

        // new upload element in form
		var input1Upload=document.createElement("input");
		input1Upload.setAttribute("type","hidden");
		input1Upload.setAttribute("name","MAX_FILE_SIZE");
		input1Upload.setAttribute("value","10000000");
		this.camposFormulario.push(input1Upload);
		
		//this.camposFormulario.push("<INPUT TYPE='hidden' name='MAX_FILE_SIZE' value='10000000' />");
		//print("<INPUT TYPE='hidden' name='MAX_FILE_SIZE' value='10000000' />");
		var pInput2Upload= document.createElement("p");
		pInput2Upload.innerHTML=label;
		var input2Upload=document.createElement("input");
		input2Upload.setAttribute("type","file");
		input2Upload.setAttribute("name","nameFile");
		input2Upload.setAttribute("id","nameFile");
		input2Upload.setAttribute("value",labels.textContent);
		pInput2Upload.appendChild(input2Upload);
		this.camposFormulario.push(pInput2Upload);
		//this.camposFormulario.push("<p>"+label+"<input name='"+fieldid+"' type='file'  size='20' value='"+labels.textContent+"' '></p>");
		//print("<p>"+label+"<input name='"+fieldid+"' type='file'  size='20' value='"+labels.textContent+"' '></p>");

        // extract XForms 'filename' element; considered as optional
        var filenames =this.getNodes(/^xf:filename/,UINode,null); 
        if (filenames !=null) {
            var element_name = this.encodeXFormsInfo("upload", "filename", refs.nodeValue);
            var refs = this.getNodes(null,filenames,/^ref/);
            var inputFileNameUpload=document.createElement("input");
    		inputFileNameUpload.setAttribute("type","hidden");
    		
    		inputFileNameUpload.setAttribute("name",element_name);
    		inputFileNameUpload.setAttribute("value",refs.nodeValue);
    		this.camposFormulario.push(inputFileNameUpload);
            
            //print("<INPUT TYPE='hidden' name='"+element_name+"' value='"+refs.nodeValue+"'>");
        }
        
        //Meto a pedal lo del mediatype  porque no me lo encuentra.
        var inputMediaTypesUpload=document.createElement("input");
		inputMediaTypesUpload.setAttribute("type","hidden");
		
		inputMediaTypesUpload.setAttribute("name","upload_mediatype__ins:data/ins:file");
		inputMediaTypesUpload.setAttribute("value","@ins:mediatype");
		this.camposFormulario.push(inputMediaTypesUpload);
        
        /*// extract XForms 'mediatype' element; considered as optional
        var mediatypes =this.getNodes(/^xf:mediatype/,UINode,null);
        if (mediatypes!=null) {
            var element_name = this.encodeXFormsInfo("upload", "mediatype", refs.nodeValue);
            var refs = this.getNodes(null,mediatypes,/^ref/);
            var inputMediaTypesUpload=document.createElement("input");
    		inputMediaTypesUpload.setAttribute("type","hidden");
    		
    		inputMediaTypesUpload.setAttribute("name",element_name);
    		inputMediaTypesUpload.setAttribute("value",refs.nodeValue);
    		this.camposFormulario.push(inputMediaTypesUpload);
           // print("<INPUT TYPE='hidden' name='"+element_name+"' value='"+refs.nodeValue+"'>");            
        }*/

    },
	/**
     * Reads a XForms 'label' element definition and inserts a proper moodleform label element in the form.
     *
     * LIMITATION: use '@ref' attribute for generating the name of the form element; it doesn't consider 'bind' XForms elements/attributes
     * LIMITATION: XForms label is considered as required
     *
     * @param $UINode   DOM node containing the XForms 'label' element
     */
    printXFormsLabel:function(nodeContent){

		/*
		$refs = $this->_xpathForDefinition->query("@ref", $nodeContent);
		$labels = $this->_xpathForDefinition->query("xf:label", $nodeContent);
		print(1 . $refs->item(0)->value);
		print(1 . $labels->item(0)->value);
		*/
    	var labelP=document.createElement("p");
    	labelP.innerHTML=nodeContent.textContent;
		this.camposFormulario.push(labelP);
		//print ("<p>"+nodeContent.textContent+"</p>");
	},
	
	//JUAN
	/**
     * Reads a XForms 'select1' element definition and inserts a proper moodleform select element in the form.
     *
     * LIMITATION: use '@ref' attribute for generating the name of the form element; it doesn't consider 'bind' XForms elements/attributes
     * LIMITATION: XForms label is considered as required
     *
     * @param $UINode   DOM node containing the XForms 'select1' element
     */
	 printXFormsSelect1:function(UINode) {
	        // extract data from XForms 'select1' element
	        var labels =this.getNodes(/^xf:label/,UINode,null);
	        var items =this.getNodes(/^xf:item/,UINode,null);
	        var refs = this.getNodes(null,UINode,/^ref/);
	        var fieldid = this.encodeXFormsInfo("select1", "", refs.nodeValue);
	        
	        var element_name = this.encodeXFormsInfo("select1", "", refs.nodeValue);
		 

	//        var fieldid = this.encodeXFormsInfo("select1", "", refs.nodeValue);
	        
	 //       var element_name = this.encodeXFormsInfo("select1", "", refs.nodeValue);
			
	        //new text element in form
	        var label = labels.textContent.trim();
	        var nodoDefect=refs.nodeValue.substring(refs.nodeValue.lastIndexOf("/")+1,refs.nodeValue.length);
	        //var defaultInput = this.getNodes( new RegExp("^"+nodoDefect),null,null)[0].textContent;
	        var nodes = this.getNodes( new RegExp("^"+nodoDefect),null,null);
	        //var defaultInput = nodes[nodes.length-1].textContent;
	        
	        //Utilizamos el parser de dojo para obtener correctamente los nodos hijos
	        var root = dojox.xml.DomParser.parse(parserXforms.data);
	        //Se obtiene el nodo m?s interno
	        var cadena = nodes[nodes.length-1].nodeName.toLowerCase();
			if (root.getElementsByTagName(cadena).length == 0)
			{
				cadena = cadena.substring(3);
			}
			var nodos=root.getElementsByTagName(cadena);
			//Se obtiene el nodo m?s interno
			var nodo = nodos[nodos.length-1];
			if (nodo.childNodes.length > 0)
			{
				var defaultInput = nodo.childNodes[0].nodeValue;
			}
			else{
				var defaultInput = "";
			}
	        
			var pSelect = document.createElement("p");
			pSelect.innerHTML = label;
			
			var nodoSelect = document.createElement("select");
			nodoSelect.id= fieldid;
			nodoSelect.name= fieldid;
        
	        //Recorro los items
			for(var j=0; j<items.length; j++){
		        var labels =this.getNodes(/^xf:label/,items[j],null);
		        var values =this.getNodes(/^xf:value/,items[j],null);
		        
		        var label = labels.textContent.trim();
		        var value = values.textContent.trim();
		        
				var option = new Option(label,value);
				nodoSelect.options[j] = option;

			}
			pSelect.appendChild(nodoSelect);
	        this.camposFormulario.push(pSelect);
	        
	        
	    },

		//JUAN
		/**
	     * Reads a XForms 'select' element definition and inserts a proper moodleform checkbox element in the form.
	     *
	     * LIMITATION: use '@ref' attribute for generating the name of the form element; it doesn't consider 'bind' XForms elements/attributes
	     * LIMITATION: XForms label is considered as required
	     *
	     * @param $UINode   DOM node containing the XForms 'select' element
	     */
		 printXFormsSelect:function(UINode) {
		        // extract data from XForms 'select1' element
		        var labels =this.getNodes(/^xf:label/,UINode,null);
		        var items =this.getNodes(/^xf:item/,UINode,null);
		        var refs = this.getNodes(null,UINode,/^ref/);
		        var fieldid = this.encodeXFormsInfo("select", "", refs.nodeValue);
		        
		        var element_name = this.encodeXFormsInfo("select", "", refs.nodeValue);
			 

		//        var fieldid = this.encodeXFormsInfo("select1", "", refs.nodeValue);
		        
		 //       var element_name = this.encodeXFormsInfo("select1", "", refs.nodeValue);
				
		        //new text element in form
		        var label = labels.textContent.trim();
		        var nodoDefect=refs.nodeValue.substring(refs.nodeValue.lastIndexOf("/")+1,refs.nodeValue.length);
		        //var defaultInput = this.getNodes( new RegExp("^"+nodoDefect),null,null)[0].textContent;
		        var nodes = this.getNodes( new RegExp("^"+nodoDefect),null,null);
		        //var defaultInput = nodes[nodes.length-1].textContent;
		        
		        //Utilizamos el parser de dojo para obtener correctamente los nodos hijos
		        var root = dojox.xml.DomParser.parse(parserXforms.data);
		        //Se obtiene el nodo m?s interno
		        var cadena = nodes[nodes.length-1].nodeName.toLowerCase();
				if (root.getElementsByTagName(cadena).length == 0)
				{
					cadena = cadena.substring(3);
				}
				var nodos=root.getElementsByTagName(cadena);
				//Se obtiene el nodo m?s interno
				var nodo = nodos[nodos.length-1];
				if (nodo.childNodes.length > 0)
				{
					var defaultInput = nodo.childNodes[0].nodeValue;
				}
				else{
					var defaultInput = "";
				}
		        
//				var pSelect = document.createElement("p");
//				pSelect.innerHTML = label;
//				
//				var nodoSelect = document.createElement("select");
//				nodoSelect.id= fieldid;
//				nodoSelect.name= fieldid;
				
				
				var pSelect = document.createElement("p");
				pSelect.innerHTML = label;
				
				var formCheckBox = document.createElement("form");
				formCheckBox.setAttribute("id", "formCheckBox~" +fieldid);
				formCheckBox.setAttribute("name", "formCheckBox~" +fieldid);
				formCheckBox.setAttribute("onsubmit", "return false;");
		//		formCheckBox.setAttribute("class", "CheckBoxClass");
				pSelect.appendChild(formCheckBox);

		        //Recorro los items
				for(var j=0; j<items.length; j++){
			        var labels =this.getNodes(/^xf:label/,items[j],null);
			        var values =this.getNodes(/^xf:value/,items[j],null);
			        
			        var label = labels.textContent.trim();
			        var value = values.textContent.trim();
			        
					var checkBoxEntry  = document.createElement("input");
					checkBoxEntry.setAttribute("type", "checkbox");
					checkBoxEntry.setAttribute("name", fieldid);
					checkBoxEntry.setAttribute("id", fieldid + value);
					checkBoxEntry.setAttribute("value", value);
					formCheckBox.appendChild(checkBoxEntry);
					var checkBoxLabel  = document.createElement("label");
					checkBoxLabel.innerHTML = label;
					checkBoxLabel.setAttribute("for", fieldid + value);
					formCheckBox.appendChild(checkBoxLabel);
					formCheckBox.appendChild(document.createElement("br"));

				}
		        this.camposFormulario.push(pSelect);
		        
		        
		    },
	
	
	
	
	/**
     * Create an XForms XML response with the data from the filled form.
    *
    * @return  string      Serialized XML containing the XForms instance with the data from the form.
    */
	buildXFormsInstance:function() {
		//var root= dojox.xml.DomParser.parse(parserXforms.data);
		var root=dojox.xml.parser.parse(parserXforms.data);
		var divXml=root.childNodes[0];
		var formData=document.forms['formToolConfiguration'];
		var xFormsResp;
		var nodeName;
		var elementTag;
		var path;
		var value;
		var attribute_name= new Array();
		var i=0;
		while(divXml.childNodes.length!=1){
			nodeName=divXml.childNodes[i].nodeName;
			//if ((nodeName!="xf:model")){
			if ((nodeName!="content")){
				divXml.removeChild(divXml.childNodes[i]);				
			}else{
				i++;
			}
		}
		
		//Recorro datos del formulario.
		for(var j=0; j<formData.length; j++){
			elementTag=formData[j].name.split("_")[0];
			//Rellenamos atributos del tag
			attributeName=name.split("_")[1];
			attributeName=name.split("_")[2];
			
			path=formData[j].name.split("_")[3];
			value=formData[j].value;
			if (elementTag=="input"){
					root=parserXforms.bindXFormsText(path, value,root);
			}else if (elementTag=="select1"){
				root=parserXforms.bindXFormsText(path, value,root);		
			}else if (elementTag=="select"){
				root=parserXforms.bindXFormsText(path, value,root);	
			}else{
				if(elementTag=="upload"){
					if (attribute_name.length ==0) {
                        // nothing to do now, uploaded contents are processed later

                    } else if (attribute_name == 'filename') {
                        // original name of an uploaded file; save
                        $filenames_to_save[$path] = $value;

                    } else if (attribute_name == 'mediatype') {
                        // original name of an uploaded file; save
                        $mediatypes_to_save[$path] = $value;
                    }	
				}				
			}				
		}		
		
		//JUAN: Procesamiento de checkboxes
		
		var forms=document.forms;
		for(var j=0; j<forms.length; j++){
			var formStartName=forms[j].name.split("~")[0];
			var fileId=forms[j].name.split("~")[1];
			if (formStartName == "formCheckBox"){

				//Se construye el campo del checkbox, con todos los valores separados por ;
			     var checkbox = new Array();
			     checkbox = document.getElementsByName(fileId);
			     var types = "";
			     for( var i=0; i < checkbox.length; i++ ) { 
			          if(checkbox[i].checked) {
			  			nameInput = checkbox[i].id + "";
						valueInput = checkbox[i].value;
						types = types + valueInput + ";";
			          }	          
			     }
			     types = types.substring(0, types.lastIndexOf(";"));
			     
				 elementTag=fileId.split("_")[0];			
				 path=fileId.split("_")[3];
				 if (elementTag=="select"){
						 root=parserXforms.bindXFormsText(path, types,root);
				 }
				
			}
		}

		
		
		//return dojox.xml.parser.innerXML(root);
		//Devolvemos a partir del nodo data
		var cadena = "ins:data";
		if (root.getElementsByTagName("ins:data").length == 0)
		{
			cadena = "data";
		}
		var nodoData=root.getElementsByTagName(cadena)[0];
		return dojox.xml.parser.innerXML(nodoData);
	},
	 /**
     * Insert a string as the text content of an XML node specified by an XPath path through the filled XForms instance.
     *
     * @param    string      $path           XPath path to the XML node (element or attribute), descendant of <instance>, where the value must be bound.
     * @param    string      $value          String value to be bound.
     */
	bindXFormsText:function(path,value,root){
		//var root2 = root.documentElement;
		var cadena = "ins:data";
		if (root.getElementsByTagName("ins:data").length == 0)
		{
			cadena = "data";
		}
		if (root.getElementsByTagName(cadena).length > 0 ){
			var nodoData=root.getElementsByTagName(cadena)[0].childNodes;
			for(var j=0; j<nodoData.length; j++){
				if (nodoData[j].nodeName==path.split("/")[1]){
					nodoData[j].textContent=value;
				}
			}
		}
		return root;
	}
    ,
    /**
     * Generates a name suitable for a moodleform UI element using data from an XForms input element.
     *
     * @param   string  $element    XForms element tag.
     * @param   string  $attribute  XForms attribute name.
     * @param   string  $path       XPath binding path.
     * @return  string              Name for a moodleform input control.
     */
    encodeXFormsInfo:function(element,attribute,path) {
        return element + "_" + attribute + "_" + path.replace("/","_");
    }
 };

