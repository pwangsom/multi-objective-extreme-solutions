package com.kmutt.sit.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.math3.util.Precision;
import org.apache.log4j.Logger;
import org.uma.jmetal.solution.Solution;

import com.kmutt.sit.cloud.CloudScheduling;
import com.kmutt.sit.mop.parameter.Configuration;
import com.kmutt.sit.pegasus.dax.DaxNode;

public class JavaHelper {

	private static Logger logger = Logger.getLogger(JavaHelper.class);
	private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	public static String getTimeDouble(Double time) {
		return String.format("%10.2f", time);
	}

	public static boolean isNull(Object test) {
		if (test instanceof String) {
			return isNull((String) test);
		}

		return test == null;
	}

	public static boolean isNull(String test) {
		return test == null || test.length() == 0 || test.equalsIgnoreCase("null") || test.trim().length() == 0;
	}

	public static String getStackTraceString(Exception e) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		e.printStackTrace(printWriter);
		return writer.toString();
	}

	public static String getStringArryaList(ArrayList<String> array) {
		Iterator<String> itr = array.iterator();
		StringBuilder sb = new StringBuilder();
		while (itr.hasNext()) {
			sb.append(itr.next() + "\n");
		}
		return sb.toString();
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	
	
	public static Date getCurrentDate(){
		Date result = null;
		
		try {
			result = convertStringTodate(formatter.format(new Date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.debug(e);
		}
		
		return result;		
	}
	
	public static Date convertStringTodate(String dateString){
		Date result = null;
		
		try {
			result = (Date) formatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.debug(e);
		}
		
		return result;
	}
	
	public static XMLGregorianCalendar convertTimestampToXMLtime(Timestamp timestamp){
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(timestamp);
		XMLGregorianCalendar xmlTime = null;
		try {
			xmlTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			logger.debug(e);
		}
		return xmlTime;
	}
	
	public static XMLGregorianCalendar convertDateToXMLtime(Date date){
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Timestamp(date.getTime()));
		XMLGregorianCalendar xmlTime = null;
		try {
			xmlTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		return xmlTime;
	}

	public static void printNodeMap(Map<String, DaxNode> map) {		
		Set<Entry<String, DaxNode>> s = map.entrySet();
		Iterator<Entry<String, DaxNode>> it = s.iterator();
		
		int i = 0;
		
		while (it.hasNext()) {
			Entry<String, DaxNode> entry = it.next();
			String key = (String) entry.getKey();
			
			i++;
			
			logger.info("No. " + i  + ": " + key);
		}
	}
	
	public static String getStringOfNodeList(Map<String, DaxNode> map) {		
		Set<Entry<String, DaxNode>> s = map.entrySet();
		Iterator<Entry<String, DaxNode>> it = s.iterator();
		
		String result = "";
		boolean first = true;
		while (it.hasNext()) {
			Entry<String, DaxNode> entry = it.next();
			String key = (String) entry.getKey();
			DaxNode node = entry.getValue();
			
			if(first) {
				first = false;
				result += key + "{" + node.getMatrixId() + "}";
			} else {
				result += ", " + key + "{" + node.getMatrixId() + "}";
			}
		}
		
		return result;
	}
	
	public static Double getRandomOverheadRuntime() {
		double min = Configuration.MIN_OVERHEAD_RUNTIME;
		double max = Configuration.MAX_OVERHEAD_RUNTIME;
				
		return getRandomDouble(min, max);
	}
	
	public static Double getRandomOverheadCommutime() {
		double min = Configuration.MIN_OVERHEAD_COMMU;
		double max = Configuration.MAX_OVERHEAD_COMMU;
				
		return getRandomDouble(min, max);
	}
	
	public static Double getRandomDouble(Double min, Double max) {
		Random r = new Random();
		return Precision.round(min + (max - min) * r.nextDouble(), Configuration.PRECISION_SCALE);
		
	}
	
	public static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	public static String appendPathName(String path, String firstAppend, String secondAppend) {
		return appendPathName(appendPathName(path, firstAppend), secondAppend);
	}
	
	public static String appendPathName(String path, String append) {
		if(path.contains("\\")) {
			path += "\\" + append;
		} else {
			path += "/" + append;			
		}
		
		return path;
	}
	
	public static String appendPathName(String... strings) {
		
		String path = strings[0];
		String connector;
		
		if(path.contains("\\")) {
			connector = "\\";
		} else {
			connector = "/";			
		}
		
		for(int i = 1; i < strings.length; i++) {
			if(path.contains("\\")) {
				path += connector + strings[i];
			} else {
				path += connector + strings[i];			
			}
		}
		
		return path;
	}
	
	public static void writeStringToFile(String fullPathFileName, String content) {
		try {
			Files.write(Paths.get(fullPathFileName), content.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	public static Integer[][] createZeroIntegerMatrix(int size) {
		return createIntegerMatrix(size, size, 0);
	}	
	
	public static Integer[][] createIntegerMatrix(int size, int defaultValue) {
		return createIntegerMatrix(size, size, defaultValue);
	}
	
	public static Integer[][] createIntegerMatrix(int row, int column, int defaultValue) {

		Integer[][] matrix = new Integer[row][column];

		matrix = IntStream.range(0, row)
				.mapToObj(x -> IntStream.range(0, column).mapToObj(y -> Integer.valueOf(defaultValue)).toArray(Integer[]::new))
				.toArray(Integer[][]::new);

		return matrix;
	}
	
	public static void printIntegerMatrix(Integer[][] matrix, int size) {
		for(int i = 0; i < size; i++) {
			logger.debug(String.format("%3d -> ", i) + Arrays.asList(matrix[i]).stream()
					.map(n -> n < 1 ? "-" : String.valueOf(n))
					.collect(Collectors.toList()).toString());
		}
	}
	
	public static void printIntegerMatrix(Integer[][] matrix) {
		
		int row = Arrays.asList(matrix).size();
		
		for(int i = 0; i < row; i++) {
			logger.debug(String.format("%3d -> ", i) + Arrays.asList(matrix[i]).stream()
					.map(n -> n < 1 ? "-" : String.valueOf(n))
					.collect(Collectors.toList()).toString());
		}
	}
	
	public static int getMaxValueIntegerMatrix(Integer[][] matrix) {
		Stream<Integer> intStream = Stream.of(matrix).flatMap(Arrays::stream);
		int max = intStream.max(Comparator.comparing(Integer::intValue)).get();
		
		return max;
	}
	
	public static int getMinValueIntegerMatrix(Integer[][] matrix) {
		Stream<Integer> intStream = Stream.of(matrix).flatMap(Arrays::stream);
		int min = intStream.min(Comparator.comparing(Integer::intValue)).get();
		
		return min;
	}
	
	public static int[] getMaxMinValueIntegerMatrix(Integer[][] matrix) {
		
		int[] result = {0,0};
		
		Stream<Integer> intStream = Stream.of(matrix).flatMap(Arrays::stream);
		result[0] = intStream.max(Comparator.comparing(Integer::intValue)).get();
		result[1] = intStream.min(Comparator.comparing(Integer::intValue)).get();
		
		return result;
	}
	
	public static List<DaxNode> getSortedNodeListBySchedulingType(List<DaxNode> unsortedNodeList, CloudScheduling.SchedulingType schedulingType){		
		
		Comparator<DaxNode> comparator = Comparator.comparing(DaxNode::getLevelId);
		
		if(schedulingType == CloudScheduling.SchedulingType.MIN) {
			comparator = comparator.thenComparing(Comparator.comparing(DaxNode::getActualRuntime));			
		} else if(schedulingType == CloudScheduling.SchedulingType.MAX) {
			comparator = comparator.thenComparing(Comparator.comparing(DaxNode::getActualRuntime).reversed());			
		} else {
			comparator = comparator.thenComparing(Comparator.comparing(DaxNode::getMatrixId));				
		}
		
		return unsortedNodeList.stream().sorted(comparator).collect(Collectors.toList());		
	}
	
	public static void displayTwoSetOfSolution(List<? extends Solution<?>> first, List<? extends Solution<?>> second) {

		logger.debug("");
		logger.info("First set size: " + first.size() + ", Second set size: " + second.size());
		
		if(first.size() == second.size()) {
			IntStream.range(0, first.size()).forEach(i -> {
				logger.debug("");
				logger.info(i + ": first  : value -> " + first.get(i).toString());
				logger.info(i + ": second : value -> " + second.get(i).toString());
			});
		}
		
		logger.debug("");
	}
}
