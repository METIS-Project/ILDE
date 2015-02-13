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
