/**
 This file is part of WebContentAdapter.

 WebContentAdapter is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 WebContentAdapter is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use WebContentAdapter for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when WebContentAdapter is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute WebContentAdapter and/or modify it under the
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
-- SQL script TEMPLATE for adding GSIC's web content adapter configuration data into the Internal Registry. 
--
-- Replace with your data where brackets [] appear.
-- DON'T MODIFY ANY OTHER THING.
--
-- @author 		David A. Velasco ;
-- @version 	2011110300 ;
-- 
-- Copyright 2011 GSIC (UVA) ;
--;


-- ToolServices;
-- Data about providers of tool implementations. JUST FOR INFO;

INSERT INTO InternalRegistry.ToolServices VALUES
([SERVIDE_ID_1], 'http://wikipedia.org/', CURRENT_TIMESTAMP),
([SERVIDE_ID_2], 'http://phdcomics.com/', CURRENT_TIMESTAMP),
([SERVIDE_ID_3], 'non defined', CURRENT_TIMESTAMP);


-- ImplementationAdapters;
-- Data about the implementation adapters available in your environment; 
-- Insert your proper host and port in every service URL;

INSERT INTO InternalRegistry.ImplementationAdapters VALUES
([ADAPTER_ID], 'WebContentAdapter', 'http://[HOST:PORT]/ToolAdapter/WebContent', CURRENT_TIMESTAMP);


-- ImplementationModels;
-- Don't bother about this. And don't touch it.

INSERT INTO InternalRegistry.ImplementationModels VALUES
([MODEL_ID], 'WebContent', [ADAPTER_ID], CURRENT_TIMESTAMP);


-- Tools;
-- Descriptive info about every single tool. Better info in near future.

INSERT INTO InternalRegistry.Tools VALUES
([TOOL_ID_1], 'Wikipedia', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_2], 'PHD Comics', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_3], 'Web Content', '', '', CURRENT_TIMESTAMP);


-- ToolImplementations;
-- Compound entity grouping Tools, ImplementationModels and ToolServices.
-- Fill your proper data in the last column. See the manual of each adapter to find out more about them.

INSERT INTO InternalRegistry.ToolImplementations VALUES
([TOOL_IMP_ID_1], [TOOL_ID_1], [MODEL_ID], [SERVICE_ID_1], CURRENT_TIMESTAMP, null),
([TOOL_IMP_ID_2], [TOOL_ID_2], [MODEL_ID], [SERVICE_ID_2], CURRENT_TIMESTAMP, null),
([TOOL_IMP_ID_3], [TOOL_ID_3], [MODEL_ID], [SERVICE_ID_3], CURRENT_TIMESTAMP, null);


-- relToolsWithToolTypes;

INSERT INTO InternalRegistry.relToolsWithToolTypes VALUES
([TOOL_ID_1],5),
([TOOL_ID_2],6),
([TOOL_ID_3],7);