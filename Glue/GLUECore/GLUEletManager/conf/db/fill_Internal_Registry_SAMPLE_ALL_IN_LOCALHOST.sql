/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletManager.
 * 
 * GlueletManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

--
-- SQL script for populating Internal Registry with data for all the implementation adapters created by GSIC.
--
-- It is assumed that all the GSIC tool implementation adapters are at localhost, along with a Wookie Widgets server
-- listening at http://localhost:8880/wookie, and a MediaWiki server listening at http://localhost/wiki
--
-- SOME USER AND PASSWORD VALUES HAVE TO BE PROVIDED; SEARCH FOR THEM AT ToolImplementations TABLE, BETWEEN SQUARE BRACKETS [ ]
--
-- @author 		David A. Velasco ;
-- @version 	2012052900 ;
-- 
-- Copyright 2011-2012 GSIC (UVA) ;
--;


-- ToolServices;
-- Data about providers of tool implementations. JUST FOR INFO;

INSERT INTO InternalRegistry.ToolServices VALUES
(1, 'http://docs.google.com/', CURRENT_TIMESTAMP),
(2, 'http://wikipedia.org/', CURRENT_TIMESTAMP),
(3, 'http://phdcomics.com/', CURRENT_TIMESTAMP),
(4, 'non defined', CURRENT_TIMESTAMP),
(5, 'http://dabbleboard.com/', CURRENT_TIMESTAMP),
(6, 'http://localhost:8880/wookie', CURRENT_TIMESTAMP),
(7, 'http://delfos.tel.uva.es/wikis/', CURRENT_TIMESTAMP),
(8, 'http://doodle-test.com', CURRENT_TIMESTAMP),
(9, 'http://developers.facebook.com/', CURRENT_TIMESTAMP),
(10, 'http://www.imsglobal.org/lti/', CURRENT_TIMESTAMP);


-- ImplementationAdapters;
-- Data about the implementation adapters available in your environment; 
-- Insert your proper host and port in every service URL;

INSERT INTO InternalRegistry.ImplementationAdapters VALUES
(1, 'GDataAdapter', 'http://localhost:8186/ToolAdapter/GData', CURRENT_TIMESTAMP),
(2, 'WebContentAdapter', 'http://localhost:8187/ToolAdapter/WebContent', CURRENT_TIMESTAMP),
(3, 'DabbleboardAdapter', 'http://localhost:8188/ToolAdapter/Dabbleboard', CURRENT_TIMESTAMP),
(4, 'WookieWidgetsAdapter', 'http://localhost:8189/ToolAdapter/WookieWidgets', CURRENT_TIMESTAMP),
(5, 'MediaWikiAdapter', 'http://localhost:8190/ToolAdapter/MediaWiki', CURRENT_TIMESTAMP),
(6, 'DoodleAdapter', 'http://localhost:8192/ToolAdapter/Doodle', CURRENT_TIMESTAMP),
(7, 'FacebookLiveStreamAdapter', 'http://localhost:8195/ToolAdapter/FacebookLiveStream', CURRENT_TIMESTAMP),
(8, 'BasicLTIAdapter', 'http://localhost:8193/ToolAdapter/BasicLTI', CURRENT_TIMESTAMP);


-- ImplementationModels;
-- Don't bother about this. And don't touch it.

INSERT INTO InternalRegistry.ImplementationModels VALUES
(1, 'GData3', 1, CURRENT_TIMESTAMP),
(2, 'WebContent', 2, CURRENT_TIMESTAMP),
(3, 'Dabbleboard LowREST', 3, CURRENT_TIMESTAMP),
(4, 'WookieWidgets REST', 4, CURRENT_TIMESTAMP),
(5, 'MediaWiki REST', 5, CURRENT_TIMESTAMP),
(6, 'Doodle REST', 6, CURRENT_TIMESTAMP),
(7, 'Facebook Live Stream', 7, CURRENT_TIMESTAMP),
(8, 'BasicLTI REST', 8, CURRENT_TIMESTAMP);


-- Tools;
-- Descriptive info about every single tool. Better info in near future.

INSERT INTO InternalRegistry.Tools VALUES
(1, 'Google Documents', '', '', CURRENT_TIMESTAMP),
(2, 'Google Spreadsheets', '', '', CURRENT_TIMESTAMP),
(3, 'Google Presentations', '', '', CURRENT_TIMESTAMP),
(4, 'Google Drawings', '', '', CURRENT_TIMESTAMP),
(5, 'Wikipedia', '', '', CURRENT_TIMESTAMP),
(6, 'PHD Comics', '', '', CURRENT_TIMESTAMP),
(7, 'Web Content', '', '', CURRENT_TIMESTAMP),
(8, 'Dabbleboard', '','', CURRENT_TIMESTAMP),
(9, 'Chat Widget', '','', CURRENT_TIMESTAMP),
(10, 'SharedDraw Widget', '', '', CURRENT_TIMESTAMP),
(11, 'Sudoku Widget', '', '', CURRENT_TIMESTAMP),
(12, 'Forum Widget', '', '', CURRENT_TIMESTAMP),
(13, 'Natter Chat Widget', '', '', CURRENT_TIMESTAMP),
(14, 'Bubble Game Widget', '', '', CURRENT_TIMESTAMP),
(15, 'Butterfly Paint Widget', '', '', CURRENT_TIMESTAMP),
(16, 'You Decide Widget', '', '', CURRENT_TIMESTAMP),
(17, 'MediaWiki', '', '', CURRENT_TIMESTAMP),
(18, 'Doodle', '', '', CURRENT_TIMESTAMP),
(19, 'Facebook Live Stream', '','', CURRENT_TIMESTAMP),
(20, ' BasicLTI PHP Provider', '','', CURRENT_TIMESTAMP);


-- ToolImplementations;
-- Compound entity grouping Tools, ImplementationModels and ToolServices.
-- Fill your proper data in the last column. See the manual of each adapter to find out more about them.

INSERT INTO InternalRegistry.ToolImplementations VALUES
(1, 1, 1, 1, CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GMAIL_USER]&pass=[GMAIL_PWD]'),
(2, 2, 1, 1, CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GMAIL_USER]&pass=[GMAIL_PWD]'),
(3, 3, 1, 1, CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GMAIL_USER]&pass=[GMAIL_PWD]'),
(4, 4, 1, 1, CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GMAIL_USER]&pass=[GMAIL_PWD]');
(5, 5, 2, 2, CURRENT_TIMESTAMP, null),
(6, 6, 2, 3, CURRENT_TIMESTAMP, null),
(7, 7, 2, 4, CURRENT_TIMESTAMP, null),
(8, 8, 3, 5, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Fapi.dabbleboard.com%2Fapi%2F&user=[DABBLEOARD_USER]&pass=[DABBLEOARD_PWD]'),
(9, 9, 4, 6, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Flocalhost:8880%2Fwookie&devKey=[WOOKIE_DEV_KEY]'),
(10, 10, 4, 6, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Flocalhost:8880%2Fwookie&devKey=[WOOKIE_DEV_KEY]'),
(11, 11, 4, 6, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Flocalhost:8880%2Fwookie&devKey=[WOOKIE_DEV_KEY]'),
(12, 12, 4, 6, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Flocalhost:8880%2Fwookie&devKey=[WOOKIE_DEV_KEY]'),
(13, 13, 4, 6, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Flocalhost:8880%2Fwookie&devKey=[WOOKIE_DEV_KEY]'),
(14, 14, 4, 6, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Flocalhost:8880%2Fwookie&devKey=[WOOKIE_DEV_KEY]'),
(15, 15, 4, 6, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Flocalhost:8880%2Fwookie&devKey=[WOOKIE_DEV_KEY]'),
(16, 16, 4, 6, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Flocalhost:8880%2Fwookie&devKey=[WOOKIE_DEV_KEY]'),
(17, 17, 5, 7, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Flocalhost%2Fwiki&user=[WIKI_USER]&pass=[WIKI_PWD]'),
(18, 18, 6, 8, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Fdoodle-test.com%2F'),
(19, 19, 7, 9, CURRENT_TIMESTAMP, 'apiID=[FACEBOOK_APLICATION_ID]'),
(20, 20, 8, 10, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Fdr-chuck.com%2Fims%2Fphp-simple%2Ftool.php',2,null);



-- relToolsWithToolTypes;

INSERT INTO InternalRegistry.relToolsWithToolTypes VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(5,5),
(6,6),
(7,7),
(8,4),
(9,8),
(10,4),
(11,9),
(12,10),
(13,8),
(14,9),
(15,9),
(16,16),
(17,1),
(18,16),
(19,8),
(20,17);
