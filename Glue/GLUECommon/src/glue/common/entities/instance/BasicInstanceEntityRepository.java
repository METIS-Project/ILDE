/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletCommon.
 * 
 * GlueletCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.common.entities.instance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BasicInstanceEntityRepository implements InstanceEntityRepository {

	/// class attributes ///
	
	/**
	 * Helper for date formatting
	 */
	protected static final SimpleDateFormat dateTimeFormat822 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
	
	/**
	 * Default name of the file providing persistence
	 */
	protected String DEFAULT_PERSISTENCE_FILENAME = "instances.txt";
	

	/// attributes /// 
	
	/**
	 * Map of GLUElet instances existing, accesible by their local identifier (index) 
	 */
	protected ConcurrentHashMap<Integer, InstanceEntity> instances;
	
	
	/**
	 * File providing peristence
	 */
	protected File persistenceFile;

	protected InstanceEntityFactory instanceEntityFactory;
	
	
	
	/// methods ///

	/**
	 * Constructor
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public BasicInstanceEntityRepository(InstanceEntityFactory instanceEntityFactory, String persistenceFileName) throws NumberFormatException, IOException, ParseException {
		// check instanceEntityFactory
		if (instanceEntityFactory == null)
			throw new IllegalArgumentException("Error while creating instance entity manager");
		this.instanceEntityFactory = instanceEntityFactory;
		
		// map of documents to load
		instances = new ConcurrentHashMap<Integer, InstanceEntity>();
		
		// load from persistence file
		if (persistenceFileName != null && persistenceFileName.length() > 0)
			persistenceFile = new File(persistenceFileName);
		else
			persistenceFile = new File(DEFAULT_PERSISTENCE_FILENAME);
		
		persistenceFile = persistenceFile.getCanonicalFile();	// to resolve UNIX soft links!!
		if (persistenceFile.exists()) {
			BufferedReader in = new BufferedReader(new FileReader(persistenceFile));
			while (in.ready()) {
				String indexSt = in.readLine();
				int index = Integer.parseInt(indexSt);
				String title = in.readLine();
				String updated = in.readLine();
				InstanceEntity instanceEntity = instanceEntityFactory.createLoadedInstanceEntity(index, title, dateTimeFormat822.parse(updated));
				instanceEntity.loadSpecificState(in);
				instances.put(index, instanceEntity);
			}
			in.close();
		}
	}
	
	
	
	/**
	 * Getter for existing GLUElet instances.
	 * 
	 * @param 	index	int 									Local (implementation adapter) identifier corresponding to the desired document. 
	 * @return			InstanceEntity		Immediately accesible data from the instance, or null if index is not valid.
	 */
	//@Override
	public InstanceEntity getInstanceEntity(int index) {
		return instances.get(index);		
	}

	

	//@Override
	public int addInstanceEntity(InstanceEntity instanceEntity) {
		int index;
		// conservative solution - locking instances
		/*synchronized (instances) {	 
			index = getFirstFreeIndex();
			instanceEntity.setIndex(index);
			instances.put(index, instanceEntity);
			saveEntities();
		}*/
		// a little more flexible solution - taking advantage of ConcurretnHashMap features
		synchronized (instances) {
			index = getFirstFreeIndex();
			instances.put(index, instanceEntity);
		}
		instanceEntity.setIndex(index);
		saveEntities();
		
		return index;
	}

	
	public void updateInstanceUsers(int index, List<String> users, String callerUser, Map<String, String> specificParams) {
		// conservative solution - locking instances
		/*synchronized (instances) {
			InstanceEntity instance = instances.get(index);
			instance.setUsers(users, callerUser, specificParams);	// changes are effective in the value contained in instances
			saveEntities();
		}*/
		// a little more flexible solution - taking advantage of ConcurretnHashMap features
		InstanceEntity instance = instances.get(index);
		synchronized(instance) {
			instance.setUsers(users, callerUser, specificParams);	// this way implementors of InstanceEntitiy interface don't have to worry about synchronization
																	// (but implementors of InstanceEntityRepository will have to :( ... )
		}
		saveEntities();
	}
	
	//@Override
	public void deleteInstanceEntity(int index) {
		// conservative solution - locking instances
		/*synchronized(instances) {
			instances.remove(index);
			saveEntities();
		}*/
		// a little more flexible solution - taking advantage of ConcurretnHashMap features
		instances.remove(index);
		saveEntities();
	}


	/**
	 * Saves in the file system the data from every existing instance entity.
	 * 
	 * Rough implementation: file is erased and fully created again. 
	 */
	//@Override
	public boolean saveEntities() {
		boolean saved = false;
		File backFile = null;
		
		synchronized (persistenceFile) {	// locking simultaneous access to persistence file (avoiding simultaneous write) 
			boolean safe = false;
			try {
				if (persistenceFile.exists()) {
					//backFile = new File(persistenceFile.getName()+ ".bak");
					backFile = new File(persistenceFile.getCanonicalPath()+ ".bak");
					safe = persistenceFile.renameTo(backFile);
					
				} else
					safe = true;	// no previous file, no backup needed
			} catch (IOException io) {
				System.err.println("*** FILE PERSISTENCE ERROR - persistence file couldn't be backed up; update won't be performed");		// TODO use a logger
			}
		
			try {
				if (safe) {
					persistenceFile.createNewFile();
					PrintStream out = new PrintStream(persistenceFile);
					InstanceEntity instanceEntity = null;
					Iterator<Integer> it = instances.keySet().iterator();	// taking advantage of ConcurrentHashMap features
					int index;
					while (it.hasNext()) {
						index = it.next();
						instanceEntity = instances.get(index);
						if (instanceEntity != null) {
							out.println(index);
							out.println(instanceEntity.getTitle());
							out.println(dateTimeFormat822.format(instanceEntity.getUpdated()));
							instanceEntity.saveSpecificState(out);
						}
						out.flush();
					}
					out.close();
					saved = true;
				
				} else {
					System.err.println("*** FILE PERSISTENCE ERROR - persistence file couldn't be backed up; update won't be performed");		// TODO use a logger
				}
			
			} catch (IOException io) {
				System.err.println("*** FILE PERSISTENCE ERROR - instances couldn't be saved");		// TODO use a logger
			
			} finally {
				if (backFile != null) {
					if (saved)
						backFile.delete();
					else
						backFile.renameTo(persistenceFile);
				}
			}
		}
		return saved;
	}
	
	
	//@Override
	public InstanceEntityFactory getInstanceEntityFactory() {
		return instanceEntityFactory;
	}

	
	/**
	 * Searches for the first positive integer index not assigned to any existing instance as local identifier.
	 * 
	 * @return	int		First positive integer index not assigned to any existing instance as local identifier.
	 */
	protected int getFirstFreeIndex() {
		Object[] keys = instances.keySet().toArray();
		Arrays.sort(keys);
		int index = 0;
		for (; index < keys.length && (Integer)keys[index] <= index; index++);
		return index;
	}
	
}
