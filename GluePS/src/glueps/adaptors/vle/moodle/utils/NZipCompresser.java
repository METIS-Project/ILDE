package glueps.adaptors.vle.moodle.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Date;
/**
 * <p> Clase encargada de generar un zip
 * @author jcalvarez
 *
 */
public class NZipCompresser {

	private String m_basePath = null;
	private File m_dir = null;
	private String m_OutputFileName;

	public static void main(String args[]) {

		/*NZipCompresser c = new NZipCompresser(new File(args[0]), args[1]);
		try {
			c.compress();
		}

		catch (Exception e) {

			System.out.println(e.toString());
		}
		System.out.println("Fin.....");*/
	}

	public NZipCompresser() {

	}

	// This class gets directory and compress it reqursively
	// into zip file named outputFileName

	public NZipCompresser(File directory, String outputFileName) {

		m_dir = directory;
		m_OutputFileName = outputFileName;
	}

	public void compress() throws Exception {

		try {
			FileOutputStream zipFilename = new FileOutputStream(
					m_OutputFileName);
			ZipOutputStream zipoutputstream = new ZipOutputStream(zipFilename);
			m_basePath = m_dir.getPath();
			CompressDir(m_dir, zipoutputstream);
			zipoutputstream.setMethod(ZipOutputStream.DEFLATED);
			zipoutputstream.close();
		}

		catch (Exception e) {

			throw new Exception("Something wrong in compresser: " + e);
		}
	}

	public void setDirectory(File dir) {

		m_dir = dir;
	}

	public void setOutputFileName(String FileName) {

		m_OutputFileName = FileName;
	}

	public File getDirectory() {

		return m_dir;
	}

	public String getOutputFileName() {

		return m_OutputFileName;
	}

	// Walker through directory structure

	private void CompressDir(File f, ZipOutputStream zipoutputstream) {
		File directorio;
		System.out.println(f);
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			int i;
			if (files.length==0){
				String name=f.getPath().substring(m_basePath.length() + 1);
				ZipEntry zipentry= new ZipEntry(name+"/");
				try {
					zipoutputstream.putNextEntry(zipentry);
					zipoutputstream.closeEntry();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			for (i = 0; i < files.length; i++) {
			/*	if (files[i].toString().contains(".FOLDER")){
					//Creo directorio
					directorio=new File("c:"+files[i].toString().substring(0,files[i].toString().indexOf("."))+"/");
					directorio.mkdirs();					
				}*/
				if (files[i].isDirectory()) {
					System.out.println("calling CompressOneDir with:"
							+ files[i].getPath());					 
						CompressDir(files[i], zipoutputstream);					
				}
				if (files[i].isFile()) {
					System.out.println("adding file:" + files[i].getPath());
					addOneFile(files[i], zipoutputstream);
				}
			}
		}
		System.out.println("exiting:" + f);
	}

	// Actualy compress the file
	private void addOneFile(File file, ZipOutputStream zipoutputstream) {
		File directorio;
		String name=file.getPath().substring(m_basePath.length() + 1);
		ZipEntry zipentry=null;
		if (file.toString().contains(".FOLDER")){
				//Creo el directorio en el temporal,a partir del archivo que viene con .FOLDER
				directorio=new File("c:"+file.getPath().substring(0,file.toString().indexOf(".")));
				directorio.mkdirs();
				name=file.getPath().substring(file.getPath().indexOf("course_files"),file.getPath().indexOf("."));
				//Para que genere el directorio sin problema hay que poner una contrabarra al final
				//para que cree dentro del directorio course_files el directorio nombre
				// se debe poner en entry: course_files/nombre/
				zipentry = new ZipEntry(name+"/");
				try {
					zipoutputstream.putNextEntry(zipentry);
					zipoutputstream.closeEntry();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else{
			zipentry = new ZipEntry(file.getPath().substring(
				m_basePath.length() + 1));
			FileInputStream fileinputstream;
			CRC32 crc32 = new CRC32();

			byte[] rgb = new byte[1024];
			int n;

			// Compute CRC of input stream
			try {
				fileinputstream = new FileInputStream(file);
				while ((n = fileinputstream.read(rgb)) > -1) {
					crc32.update(rgb, 0, n);
				}
				fileinputstream.close();
			}

			catch (Exception e) {

				System.out.println("Error in computing CRC:");
				e.printStackTrace();
			}

			// Set Up Zip Entry
			zipentry.setSize(file.length());
			zipentry.setTime(file.lastModified());
			zipentry.setCrc(crc32.getValue());

			// Write Data
			try {
				zipoutputstream.putNextEntry(zipentry);
				fileinputstream = new FileInputStream(file);

				while ((n = fileinputstream.read(rgb)) > -1) {
					zipoutputstream.write(rgb, 0, n);
				}

				fileinputstream.close();
				zipoutputstream.closeEntry();
			}

			catch (Exception ex) {

				System.out.println("Error in writing data:");
				ex.printStackTrace();
			}			
		}//fin del if para el directorio
	
	}	
	
}
