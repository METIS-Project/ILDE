/**
 * @author Eloy
 */
var CheckBoxManager = {
	createCheckBox : function(id, where, label, whatToDoOnChange) {
		var newElement = where.appendChild(document.createElement("div"));
		var checkbox = new dijit.form.CheckBox( {
			id : id,
			name : id,
			onChange : whatToDoOnChange
		}, newElement);
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", id);
		labelElement.innerHTML = " " + label;
		where.appendChild(labelElement);
		where.appendChild(document.createElement("br"));

		return checkbox;
	},

	createRadioButton : function(value, name, where, label, whatToDoOnChange) {
		var newElement = where.appendChild(document.createElement("div"));
		var checkbox = new dijit.form.RadioButton( {
			value : value,
			name : name,
			onChange : whatToDoOnChange
		}, newElement);
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", checkbox.id);
		labelElement.innerHTML = " " + label;
		where.appendChild(labelElement);
		where.appendChild(document.createElement("br"));

		return checkbox;
	}
};
