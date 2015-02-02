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
-- SQL script TEMPLATE for populating Internal Registry with data for all the implementation adapters created by GSIC. 
--
-- Replace with your data where brackets [] appear.
-- DON'T MODIFY ANY OTHER THING.
--
-- @author 		David A. Velasco ;
-- @version 	2011092900 ;
-- 
-- Copyright 2011 GSIC (UVA) ;
--;


-- ToolServices;
-- Data about providers of tool implementations. JUST FOR INFO;

INSERT INTO InternalRegistry.ToolServices VALUES
(1, 'http://docs.google.com/', CURRENT_TIMESTAMP),
(2, 'http://wikipedia.org/', CURRENT_TIMESTAMP),
(3, 'http://phdcomics.com/', CURRENT_TIMESTAMP),
(4, 'non defined', CURRENT_TIMESTAMP),
(5, 'http://dabbleboard.com/', CURRENT_TIMESTAMP),
(6, '[WOOKIE-SERVER URL]', CURRENT_TIMESTAMP),
(7, '[MEDIAWIKI-SERVER URL]', CURRENT_TIMESTAMP),
(8, 'http://doodle-test.com', CURRENT_TIMESTAMP),
(9, 'http://developers.facebook.com/', CURRENT_TIMESTAMP),
(10, 'http://www.imsglobal.org/lti/', CURRENT_TIMESTAMP);


-- ImplementationAdapters;
-- Data about the implementation adapters available in your environment; 
-- Insert your proper host and port in every service URL;

INSERT INTO InternalRegistry.ImplementationAdapters VALUES
(1, 'GDataAdapter', 'http://[HOST:PORT]/ToolAdapter/GData', CURRENT_TIMESTAMP),
(2, 'WebContentAdapter', 'http://[HOST:PORT]/ToolAdapter/WebContent', CURRENT_TIMESTAMP),
(3, 'DabbleboardAdapter', 'http://[HOST:PORT]/ToolAdapter/Dabbleboard', CURRENT_TIMESTAMP),
(4, 'WookieWidgetsAdapter', 'http://[HOST:PORT]/ToolAdapter/WookieWidgets', CURRENT_TIMESTAMP),
(5, 'MediaWikiAdapter', 'http://[HOST:PORT]/ToolAdapter/MediaWiki', CURRENT_TIMESTAMP),
(6, 'DoodleAdapter', 'http://[HOST:PORT]/ToolAdapter/Doodle', CURRENT_TIMESTAMP),
(7, 'FacebookLiveStreamAdapter', 'http://[HOST:PORT]/ToolAdapter/FacebookLiveStream', CURRENT_TIMESTAMP),
(8, 'BasicLTIAdapter', 'http://[HOST:PORT]/ToolAdapter/BasicLTI', CURRENT_TIMESTAMP);


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
(1, 1, 1, 1, CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GOOGLE USER NAME]&pass=[GOOGLE PASSWORD]'),
(2, 2, 1, 1, CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GOOGLE USER NAME]&pass=[GOOGLE PASSWORD]'),
(3, 3, 1, 1, CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GOOGLE USER NAME]&pass=[GOOGLE PASSWORD]'),
(4, 4, 1, 1, CURRENT_TIMESTAMP, 'feedURL=https%3A%2F%2Fdocs.google.com%2Ffeeds%2Fdefault%2Fprivate%2Ffull%2F&user=[GOOGLE USER NAME]&pass=[GOOGLE PASSWORD]'),
(5, 5, 2, 2, CURRENT_TIMESTAMP, null),
(6, 6, 2, 3, CURRENT_TIMESTAMP, null),
(7, 7, 2, 4, CURRENT_TIMESTAMP, null),
(8, 8, 3, 5, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Fapi.dabbleboard.com%2Fapi%2F&user=[DABBLEBOARD USER NAME]&pass=[DABBLEBOARD PASSWORD]'),
(9, 9, 4, 6, CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
(10, 10, 4, 6, CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
(11, 11, 4, 6, CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
(12, 12, 4, 6, CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
(13, 13, 4, 6, CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
(14, 14, 4, 6, CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
(15, 15, 4, 6, CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
(16, 16, 4, 6, CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED WOOKIE-SERVER URL]&devKey=[URL-FORMATTED DEVELOPER KEY WITHOUT / CHARACTERS]'),
(17, 17, 5, 7, CURRENT_TIMESTAMP, 'feedURL=[URL-FORMATTED MEDIAWIKI-SERVER URL]&user=[MEDIAWIKI USER NAME]&pass=[MEDIAWIKI PASSWORD]'),
(18, 18, 6, 8, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Fdoodle-test.com%2F'),
(19, 19, 7, 9, CURRENT_TIMESTAMP, 'apiID=[FACEBOOK_APLICATION_ID]'),
(20, 20, 8, 10, CURRENT_TIMESTAMP, 'feedURL=http%3A%2F%2Fdr-chuck.com%2Fims%2Fphp-simple%2Ftool.php');


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