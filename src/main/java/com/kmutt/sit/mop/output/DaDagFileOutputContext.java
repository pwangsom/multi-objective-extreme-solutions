package com.kmutt.sit.mop.output;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;
import org.uma.jmetal.util.fileoutput.FileOutputContext;

@SuppressWarnings("serial")
public class DaDagFileOutputContext implements FileOutputContext {
	
	private Logger logger = Logger.getLogger(DaDagFileOutputContext.class);

	private static final String DEFAULT_SEPARATOR = ", ";

	protected String fileName;
	protected String separator;

	public DaDagFileOutputContext(String fileName) {
		this.fileName = fileName;
		this.separator = DEFAULT_SEPARATOR;
	}
	
	public DaDagFileOutputContext(String fileName, String separator) {
		this.fileName = fileName;
		this.separator = separator;
	}

	@Override
	public BufferedWriter getFileWriter() {
		
		FileOutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bufferedWriter = null;
		
		try {
			outputStream = new FileOutputStream(fileName);
			outputStreamWriter = new OutputStreamWriter(outputStream);
			bufferedWriter = new BufferedWriter(outputStreamWriter);
		} catch (FileNotFoundException e) {
			logger.error(e, e);
		}
		
		return bufferedWriter;
	}

	@Override
	public String getSeparator() {
		return separator;
	}

	@Override
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

}
