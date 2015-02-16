/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Basic LTI Adapter.
 * 
 * Basic LTI Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Basic LTI Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

--
-- SQL script TEMPLATE for adding GSIC's BasicLTI adapter configuration data into the Internal Registry. 
--
-- Replace with your data where brackets [] appear.
-- DON'T MODIFY ANY OTHER THING.
--
-- @author 		Javier Enrique Hoyos ;
-- @version 	2012092501 ;
-- 
-- Copyright 2012 GSIC (UVA) ;
--;


-- ToolServices;
-- Data about providers of tool implementations. JUST FOR INFO;

INSERT INTO InternalRegistry.ToolServices VALUES
([SERVIDE_ID], '[BASICLTI-SERVER URL]', CURRENT_TIMESTAMP);


-- ImplementationAdapters;
-- Data about the implementation adapters available in your environment; 
-- Insert your proper host and port in every service URL;

INSERT INTO InternalRegistry.ImplementationAdapters VALUES
([ADAPTER_ID], 'BasicLTIAdapter', 'http://[HOST:PORT]/ToolAdapter/BasicLTI', CURRENT_TIMESTAMP);


-- ImplementationModels;
-- Don't bother about this. And don't touch it.

INSERT INTO InternalRegistry.ImplementationModels VALUES
([MODEL_ID], 'BasicLTI REST', [ADAPTER_ID], CURRENT_TIMESTAMP);


-- Tools;
-- Descriptive info about every single tool. Better info in near future.

INSERT INTO InternalRegistry.Tools VALUES
([TOOL_ID_1], 'Noteflight', '','', CURRENT_TIMESTAMP),
([TOOL_ID_2], 'ToolTest', '', '', CURRENT_TIMESTAMP);


-- ToolImplementations;
-- Compound entity grouping Tools, ImplementationModels and ToolServices.
-- Fill your proper data in the last column. See the manual of each adapter to find out more about them.

INSERT INTO InternalRegistry.ToolImplementations VALUES
([TOOL_IMP_ID_1], [TOOL_ID_1], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Falario.testdomain.noteflight.com%2F'),
([TOOL_IMP_ID_2], [TOOL_ID_2], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Fwww.imsglobal.org%2Fdevelopers%2FBLTI%2Ftool.php');


-- relToolsWithToolTypes;

INSERT INTO InternalRegistry.relToolsWithToolTypes VALUES
([TOOL_ID_1],17),
([TOOL_ID_2],18);
