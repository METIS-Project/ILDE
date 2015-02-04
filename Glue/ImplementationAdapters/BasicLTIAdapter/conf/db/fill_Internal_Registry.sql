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
