--
-- SQL script TEMPLATE for adding GSIC's Apache Wookie adapter configuration data into the Internal Registry. 
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
([SERVIDE_ID], '[WOOKIE-SERVER URL]', CURRENT_TIMESTAMP);


-- ImplementationAdapters;
-- Data about the implementation adapters available in your environment; 
-- Insert your proper host and port in every service URL;

INSERT INTO InternalRegistry.ImplementationAdapters VALUES
([ADAPTER_ID], 'WookieWidgetsAdapter', 'http://[HOST:PORT]/ToolAdapter/WookieWidgets', CURRENT_TIMESTAMP);


-- ImplementationModels;
-- Don't bother about this. And don't touch it.

INSERT INTO InternalRegistry.ImplementationModels VALUES
([MODEL_ID], 'WookieWidgets REST', [ADAPTER_ID], CURRENT_TIMESTAMP);


-- Tools;
-- Descriptive info about every single tool. Better info in near future.

INSERT INTO InternalRegistry.Tools VALUES
([TOOL_ID_1], 'Chat Widget', '','', CURRENT_TIMESTAMP),
([TOOL_ID_2], 'SharedDraw Widget', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_3], 'Sudoku Widget', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_4], 'Forum Widget', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_5], 'Natter Chat Widget', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_6], 'Bubble Game Widget', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_7], 'Butterfly Paint Widget', '', '', CURRENT_TIMESTAMP),
([TOOL_ID_8], 'You Decide Widget', '', '', CURRENT_TIMESTAMP);


-- ToolImplementations;
-- Compound entity grouping Tools, ImplementationModels and ToolServices.
-- Fill your proper data in the last column. See the manual of each adapter to find out more about them.

INSERT INTO InternalRegistry.ToolImplementations VALUES
([TOOL_IMP_ID_1], [TOOL_ID_1], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
([TOOL_IMP_ID_2], [TOOL_ID_2], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
([TOOL_IMP_ID_3], [TOOL_ID_3], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
([TOOL_IMP_ID_4], [TOOL_ID_4], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
([TOOL_IMP_ID_5], [TOOL_ID_5], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
([TOOL_IMP_ID_6], [TOOL_ID_6], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
([TOOL_IMP_ID_7], [TOOL_ID_7], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
([TOOL_IMP_ID_8], [TOOL_ID_8], [MODEL_ID], [SERVICE_ID], CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]');


-- relToolsWithToolTypes;

INSERT INTO InternalRegistry.relToolsWithToolTypes VALUES
([TOOL_ID_1],8),
([TOOL_ID_2],4),
([TOOL_ID_3],9),
([TOOL_ID_4],10),
([TOOL_ID_5],8),
([TOOL_ID_6],9),
([TOOL_ID_7],9),
([TOOL_ID_8],1);