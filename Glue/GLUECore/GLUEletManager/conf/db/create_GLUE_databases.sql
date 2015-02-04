/**
 This file is part of GlueletManager.

 GlueletManager is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GlueletManager is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GlueletManager for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GlueletManager is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GlueletManager and/or modify it under the
   terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>
 ]
*/

--
-- SQL script for creating GLUE databases ;
-- ;
-- @author	 	David A. Velasco ;
-- @version 	2011092900 ;
-- 
-- Copyright 2011 GSIC (UVA) ;
--;

-- Creation of Internal Registry: to store data about available tools

CREATE SCHEMA InternalRegistry;

CREATE TABLE InternalRegistry.Tools (
	id				int(11)			NOT NULL,
	name			varchar(255)	NOT NULL,
	description		varchar(2048)	NOT NULL,
	dataFormat		varchar(255)	NOT NULL,
	updated     	datetime        NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE InternalRegistry.ImplementationAdapters (
	id				int(11)			NOT NULL,
	name			varchar(255)	NOT NULL,
	url				varchar(255)	NOT NULL,
	updated     	datetime        NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE InternalRegistry.ImplementationModels (
	id				int(11)			NOT NULL,
	name			varchar(255)	NOT NULL,
	adapterId		int(11)			NOT NULL,
	updated     	datetime        NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (adapterId) REFERENCES InternalRegistry.ImplementationAdapters(id)
);

CREATE TABLE InternalRegistry.ToolServices (
	id				int(11)			NOT NULL,
	url				varchar(255)	NOT NULL,
	updated     	datetime        NOT NULL,
	PRIMARY KEY	(id)
);

CREATE TABLE InternalRegistry.ToolImplementations (
	id				int(11)			NOT NULL,
	toolId			int(11)			NOT NULL,
	modelId			int(11)			NOT NULL,
	toolServiceId	int(11)			NOT NULL,
	updated     	datetime        NOT NULL,
	parameters		varchar(4096),
	PRIMARY KEY (id),
	FOREIGN KEY (toolId) REFERENCES InternalRegistry.Tools(id),
	FOREIGN KEY (modelId) REFERENCES InternalRegistry.ImplementationModels(id),
	FOREIGN KEY (toolServiceId) REFERENCES InternalRegistry.ToolServices(id)
);

CREATE TABLE InternalRegistry.ToolTypes (
	id				int(11)         NOT NULL,
	name			varchar(255)	NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE InternalRegistry.relToolsWithToolTypes (
	toolId			int(11)			NOT NULL,
	toolTypeId		int(11)			NOT NULL,
	PRIMARY KEY (toolId, toolTypeId),
	FOREIGN KEY (toolId) REFERENCES InternalRegistry.Tools(id),
	FOREIGN KEY (toolTypeId) REFERENCES InternalRegistry.ToolTypes(id)
);


-- Creation of Gluelets Repository: provides persistence for Gluelets

CREATE SCHEMA GlueletsRepository;

CREATE TABLE GlueletsRepository.Gluelets (
        id						int(11)			NOT NULL,
        url						VARCHAR(255)	NOT NULL,
        toolImplementationId	int(11)			NOT NULL,
        vle						VARCHAR(255)  	NOT NULL,
        updated     			datetime        NOT NULL,
		parameters				varchar(4096),
        PRIMARY KEY (id),
        FOREIGN KEY (toolImplementationId) REFERENCES InternalRegistry.ToolImplementations(id)
);

-- ToolTypes;

INSERT INTO InternalRegistry.ToolTypes VALUES
(1, 'AsynchronousTextEditor'),
(2, 'SpreadsheetTool'),
(3, 'SlideComposer'),
(4, 'DrawingTool'),
(5, 'Encyclopedia'),
(6, 'Comic'),
(7, 'Any web content'),
(8, 'Chat'),
(9, 'Game'),
(10, 'Forum'),
(11, 'Whiteboard'),
(12, 'MapBrowser'),
(13, 'ElectronicCalendar'),
(14, 'DocumentRepository'),
(15, 'QuestionnaireManagementTool'),
(16, 'VotingTool'),
(17, 'BasicLTIToolTest');
