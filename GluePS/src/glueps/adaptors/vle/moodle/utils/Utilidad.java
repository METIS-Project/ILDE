/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package glueps.adaptors.vle.moodle.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utilidad {
	/**
	 * <p>Función encargada de generar un zip</p>
	 * @param fileList: Lista de ficheros que van en el zip
	 * @param directory: Directorio donde se encuentran los archivos
	 * @param nameZip: Nombre con que se va a generar el zip
	 */
 public void generarZip(ArrayList fileList,String directory, String nameZip){
	 //String[] filenames = new String[]{"filename1", "filename2"};
	 ArrayList filenames=fileList;
     // Create a buffer for reading the files
     byte[] buf = new byte[1024];
     try {
    	 //Creo directorio
    	 
    	 
    	 // Create the ZIP file
    	 String outFilename = nameZip+".zip";
    	 ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
    	 // Compress the files
    	 for (int i=0; i<filenames.size();i++){
	            FileInputStream in = new FileInputStream(directory+filenames.get(i).toString());
	            // Add ZIP entry to output stream.
	            out.putNextEntry(new ZipEntry(directory+filenames.get(i).toString()));
	            // Transfer bytes from the file to the ZIP file
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }
	            // Complete the entry
	            out.closeEntry();
	            in.close();
	        }
	        // Complete the ZIP file
	        out.close();
	    } catch (IOException e) {
	    }
 }
 
 /*public void copiarArchivos(String origen,String destino){
	 File fOrigen = new File(origen);
	 File direcDestino = null;
	 String comando="";
	 if ((origen !="") && (destino!="")
			 && (origen !=null) && (destino!=null)){
		 if (fOrigen.exists())
		 {
			 //Compruebo si existe el directorio destino		
			 	direcDestino=new File(destino);
			 	//Si no existe lo creo
			 	if (!direcDestino.isDirectory())
			 		direcDestino.mkdir();
			 	 //Creo el string con el comando a ejecutar en ms-dos
				 comando="xcopy  /E "+fOrigen.toString()+" "+direcDestino.toString();
				 try{
					   //Ejecuto el comando
					   Runtime.getRuntime().exec(comando);
					}catch(Exception ex){
					   System.out.println("Ha ocurrido un error al ejecutar el comando. Error: "+ex);
					}		 
		 }else{
			 System.out.println("El directorio "+ origen + "no existe");
		 }	 
 	}else{
 		System.out.println("Tanto el directorio origen: "+origen + " Como el directorio destino: "+destino +"tiene que ser diferentes null o vacio");		
 	}
	 	 //Elimino Directorio
	 if (direcDestino.delete())
		 System.out.println("El fichero "+ direcDestino.toString()+ " ha sido borrado correctamente");
	 else
		 System.out.println("El fichero "+ direcDestino.toString()+ " no se ha podido borrar");
 } */
 /**
  * Copia todo el contenido de un directorio a otro directorio
  * @param srcDir
  * @param dstDir
  * @throws IOException
  */
 public void copyDirectory(File srcDir, File dstDir)
 {
     try{
         if (srcDir.isDirectory()) {
             if (!dstDir.exists()) {
                 dstDir.mkdir();
             }

             String[] children = srcDir.list();
             for (int i=0;i<children.length;i++){
                 copyDirectory(new File(srcDir, children[i]),
                     new File(dstDir, children[i]));
             }
         } else {
             copyFile(srcDir, dstDir);
         }
     }
     catch(Exception e)
     {
         System.out.println(e);
     }
 }

 /**
  * Copia un solo archivo
  * @param s
  * @param t
  * @throws IOException
  */
 public void copyFile(File s, File t)
 {
     try{
           FileChannel in = (new FileInputStream(s)).getChannel();
           FileChannel out = (new FileOutputStream(t)).getChannel();
           in.transferTo(0, s.length(), out);
           in.close();
           out.close();
     }
     catch(Exception e)
     {
         System.out.println(e);
     }
 }

 public boolean deleteDirectory(File directory) {  
	  File[] files = directory.listFiles();  
	    for(int i = 0; i < files.length; i++) {  
	      if(files[i].isDirectory()) {  
	    	  this.deleteDirectory(files[i]);  
	      } else {  
	    	  files[i].delete();  
	      }  
	   }  
	   return directory.delete();  
	}  
}
