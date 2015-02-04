/**
 This file is part of GDataAdapterApi3.

 GDataAdapterApi3 is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GDataAdapterApi3 is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GDataAdapterApi3 for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GDataAdapterApi3 is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GDataAdapterApi3 and/or modify it under the
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
-- SQL script TEMPLATE for adding GSIC's Google Docs adapter configuration data into the Internal Registry. 
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
([SERVIDE_ID], 'http://docs.google.com/', CURRENT_TIMESTAMP);


-- ImplementationAdapters;
-- Data about the implementation adapters available in your environment; 
-- Insert your proper host and port in every service URL;

INSERT INTO InternalRegistry.ImplementationAdapters VALUES
([ADAPTER_ID], 'GDataAdapter', 'http://[HOST:PORT]/ToolAdapter/GData', CURRENT_TIMESTAMP);


-- ImplementationModels;
-- Don't bother about this. And don't touch it.

INSERT INTO InternalRegistry.ImplementationModels VALUES
([MODEL_ID], 'GData3', [ADAPTER_ID], CURRENT_TIMESTAMP);


-- Tools;
-- Descriptive info about every single tool. Better info in near future.

INSERT INTO InternalRegistry.Tools VALUES
([TOOL_ID_1], 'Google Documents', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_2], 'Google Spreadsheets', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_3], 'Google Presentations', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_4], 'Google Drawings', '', '', CURRENT_TIMESTAMP);


-- ToolImplementations;
-- Compound entity grouping Tools, ImplementationModels and ToolServices.
-- Fill your proper data in the last column. See the manual of each adapter to find out more about them.

INSERT INTO InternalRegistry.ToolImplementations VALUES
([TOOL_IMP_ID_1], [TOOL_ID_1], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GOOGLE USER NAME]&pass=[GOOGLE PASSWORD]'),
([TOOL_IMP_ID_2], [TOOL_ID_2], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GOOGLE USER NAME]&pass=[GOOGLE PASSWORD]'),
([TOOL_IMP_ID_3], [TOOL_ID_3], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GOOGLE USER NAME]&pass=[GOOGLE PASSWORD]'),
([TOOL_IMP_ID_4], [TOOL_ID_4], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GOOGLE USER NAME]&pass=[GOOGLE PASSWORD]');


-- relToolsWithToolTypes;

INSERT INTO InternalRegistry.relToolsWithToolTypes VALUES
([TOOL_ID_1],1),
([TOOL_ID_2],2),
([TOOL_ID_3],3),
([TOOL_ID_4],4);