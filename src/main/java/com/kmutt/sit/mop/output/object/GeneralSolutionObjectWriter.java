package com.kmutt.sit.mop.output.object;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.mop.manager.configulations.ConfigurationInterface;
import com.kmutt.sit.utils.JavaHelper;

public class GeneralSolutionObjectWriter<S extends Solution<?>> {	

	private Logger logger = Logger.getLogger(GeneralSolutionObjectWriter.class);
	
	@SuppressWarnings("resource")
	public void writeSolutionList(ConfigurationInterface config, String instanceKey, List<S> solutions) {
		
		String filePathName = JavaHelper.appendPathName(config.getOutputPath(), instanceKey + ".obj");
				
		try {
			FileOutputStream fout = new FileOutputStream(filePathName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(solutions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e, e);
		}
		
	}

}
