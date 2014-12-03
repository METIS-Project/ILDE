/**
 * Funcionalidad asociada al learning environment type
 */
 
 var LearningEnvironmentType = {
 
	getId: function(){
		if (typeof JsonDB.deploy.learningEnvironment.leType != "undefined" ){
			return JsonDB.deploy.learningEnvironment.leType.id;
		}else{
			return null;
		}
	},
	
	getName: function(){
		if (typeof JsonDB.deploy.learningEnvironment.leType != "undefined" ){
			return JsonDB.deploy.learningEnvironment.leType.name;
		}else{
			return JsonDB.deploy.learningEnvironment.type;
		}
	},
	
	getDescription: function(){
		if (typeof JsonDB.deploy.learningEnvironment.leType != "undefined" ){
			return JsonDB.deploy.learningEnvironment.leType.description;
		}else{
			return "";
		}
	},
	
	isGetCourses: function(){
		if (typeof JsonDB.deploy.learningEnvironment.leType != "undefined" ){
			return JsonDB.deploy.learningEnvironment.leType.getCourses;
		}else{
			return true;
		}
	},
	
	isGetParticipants: function(){
		if (typeof JsonDB.deploy.learningEnvironment.leType != "undefined" ){
			return JsonDB.deploy.learningEnvironment.leType.getParticipants;
		}else{
			return true;
		}
	},
	
	isStaticDeploy: function(){
		if (typeof JsonDB.deploy.learningEnvironment.leType != "undefined" ){
			return JsonDB.deploy.learningEnvironment.leType.staticDeploy;
		}else{
			return true;
		}
	},
	
	isDynamicDeploy: function(){
		if (typeof JsonDB.deploy.learningEnvironment.leType != "undefined" ){
			return JsonDB.deploy.learningEnvironment.leType.dynamicDeploy;
		}else{
			return true;
		}
	},
	
	isAddTopic: function(){
		if (typeof JsonDB.deploy.learningEnvironment.leType != "undefined" ){
			return JsonDB.deploy.learningEnvironment.leType.addTopic;
		}else{
			return true;
		}
	},
	
	isMultiplePosts: function(){
		if (typeof JsonDB.deploy.learningEnvironment.leType != "undefined" ){
			return JsonDB.deploy.learningEnvironment.leType.multiplePosts;
		}else{
			return true;
		}
	}
 
 }