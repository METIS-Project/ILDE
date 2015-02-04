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
-- SQL script for removing GLUE databases;
-- ;
-- @author	 	David A. Velasco ;
-- @version 	2011092900 ;
-- 
-- Copyright 2011 GSIC (UVA) ;
--;

-- Erase every table. Order matters because of foreign keys ;

DROP TABLE GlueletsRepository.Gluelets;
DROP TABLE InternalRegistry.relToolsWithToolTypes;
DROP TABLE InternalRegistry.ToolTypes;
DROP TABLE InternalRegistry.ToolImplementations;
DROP TABLE InternalRegistry.Tools;
DROP TABLE InternalRegistry.ImplementationModels;
DROP TABLE InternalRegistry.ImplementationAdapters;
DROP TABLE InternalRegistry.ToolServices;


-- Erase databases

DROP SCHEMA GlueletsRepository;
DROP SCHEMA InternalRegistry;
