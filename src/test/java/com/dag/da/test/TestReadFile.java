package com.dag.da.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class TestReadFile {

	private static Logger logger = Logger.getLogger(TestReadFile.class);

	public static void main(String[] args) {

		TestReadFile reader = new TestReadFile();
		reader.readFile();

	}

	public void accessPath() {
		File directory = new File("MOEAD_Weights");
		System.out.println(directory.getAbsolutePath());
	}

	public void readFile() {
		try {

			InputStream in = new FileInputStream("MOEAD_Weights" + "/" + "W3D_100.dat");
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			
			String aux = br.readLine();
			
			while (aux != null) {				
				logger.info(aux);
				
				aux = br.readLine();
			}
			
			br.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e, e);
		}

	}
}
