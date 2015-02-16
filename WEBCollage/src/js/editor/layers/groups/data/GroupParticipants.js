/**
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Web Collage.

Web Collage is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Web Collage is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

var GroupParticipants = function(participants) {
    this.type = "participants";
    this.participants = participants;
};

GroupParticipants.prototype.getStudentCount = function() {
    var count = 0;
    for(var i = 0; i < this.participants.length; i++) {
        count += this.participants[i].length;
    }
    return count;
};

GroupParticipants.prototype.getGroupCount = function() {
    return this.participants.length;
};

GroupParticipants.prototype.getGroup = function(index) {
    return this.participants[index];
};

GroupParticipants.prototype.getGroups = function(index) {
    return this.participants;
};


GroupParticipants.prototype.setGroupCount = function(n) {
    for (var i = this.participants.length; i < n; i++) {
        this.participants.push([]);
    }
    
    for (var i = this.participants.length; i > n; i--) {
        this.participants.splice(-1, 1);
    }
}

GroupParticipants.prototype.find = function(studentid) {
    for(var i = 0; i < this.participants.length; i++) {
        if(this.participants[i].indexOf(studentid) >= 0) {
            return i;
        }
    }

    return -1;
}

GroupParticipants.prototype.moveFromTo = function(movements, students) {
    var available = [];

    for(var i = 0; i < students.length; i++) {
        if(this.find(students[i]) < 0) {
            available.push(students[i]);
        }
    }

    for(var i = 0; i < movements.length; i++) {
        if(movements[i].count < 0) {
            var removed = this.participants[movements[i].group].splice(movements[i].count, -movements[i].count);
            available = available.concat(removed);
        }
    }

    for(var i = 0; i < movements.length; i++) {
        if(movements[i].count > 0) {
            var taken = available.splice(0, movements[i].count);
            this.participants[movements[i].group] = this.participants[movements[i].group].concat(taken);
        }
    }
};

GroupParticipants.prototype.moveIds = function(remove, add) {
    for(var i = 0; i < remove.length; i++) {
        this.participants[remove[i].group].splice(this.participants[remove[i].group].indexOf(remove[i].id), 1);
    }
    for(var i = 0; i < add.length; i++) {
        this.participants[add[i].group].push(add[i].id);
    }
}
