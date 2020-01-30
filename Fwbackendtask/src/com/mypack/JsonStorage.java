package com.mypack;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonStorage {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final int TTLinterval = 300; // 5 minutes
	private static final long defaultFileSize = 1073741824; // 1Gb
	private final static String defaultFilePath = "c:\\FW-JsonStorage";
	String currentPath = "";
	
	
	public JsonStorage(String optionalPath) {
		this.checkAndCreateFilePath(optionalPath);
	}
	public ObjectMapper getOMObject() {
		return new ObjectMapper();
	}

	public synchronized String delete(String key) {
		String log = "";
		try {
			if (!this.currentPath.equals("")) {
				File sourceFile = new File(this.currentPath);
				if (sourceFile.exists()) {
					JsonNode root = mapper.readTree(sourceFile);
					if (root != null) {
						if (root.get(key) != null) {
							if (checkTTLProperty(key, root)) {
								log = "Error :This key has expired,this key will no longer be available to Delete operations";
							} else {
								((ObjectNode) root).remove(key);
								log = "Info: " + key + " key & values has been deleted successfully";
								mapper.writeValue(sourceFile, root);
							}
						} else {
							log = "Info: Key is not found";
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return log;
	}

	private static Boolean checkTTLProperty(String key, JsonNode root) throws ParseException {
		Boolean status = false;
		String ttlValue = ((ObjectNode) root).path(key).path("TTL").asText();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		Date ttl = formatter.parse(ttlValue);
		Date currentDate = new Date();
		if (ttl.compareTo(currentDate) < 0) {
			status = true;
		}
		return status;
	}

	public synchronized String read(String key) {
		ArrayList<Object> listobj = new ArrayList<>();
		try {
			if (!this.currentPath.equals("")) {
				File sourceFile = new File(this.currentPath);
				if (sourceFile.exists()) {
					JsonNode root = mapper.readTree(sourceFile);
					if (root != null) {
						if (root.get(key) != null) {
							if (checkTTLProperty(key, root)) {
								listobj.add(
										"Error :This key has expired,the key will no longer be available to Read operations");
							} else {
								listobj.add(root.path(key));
							}
						} else {
							listobj.add("Info : Key is not found");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listobj.get(0).toString();
	}

	public synchronized String create(String key, ObjectNode values, String... timeToLive) {
		String log = "";
		double objectValueSize = getObjetSize(values);
		double sourecFileSize = getSoureFileSize();
		if (sourecFileSize > defaultFileSize) {
			log = "Error: you storage was full.please contect your fw-admin Team";
		} else if (key.length() > 32) {
			log = "Error: please check your key,key must be less then 32 characters";
		} else if (objectValueSize > 16) {
			log = "Error: please check your Value,value size should be less then 16 kb";
		} else {
			log = doCreateAction(key, values, timeToLive);
		}
		return log;
	}

	private static double getObjetSize(ObjectNode values) {
		double objectValueSize = 0.00;
		try {
			File tempFile = File.createTempFile("fw-tempFile", ".tmp");
			mapper.writeValue(tempFile, values);
			double kilobytes = ((double) tempFile.length() / 1024);
			objectValueSize = Math.round(kilobytes);
			// System.out.println("Info: value length:" + objectValueSize + "
			// kb");
			if (tempFile.delete()) {
				// System.out.println("Info:Temp file Deleted Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValueSize;
	}

	private double getSoureFileSize() {
		long fileSize = 0;
		try {
			File soureFi = new File(this.currentPath);
			fileSize = (((soureFi.length() / 1024) / 1024) / 1024);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileSize;
	}

	public  String doCreateAction(String key, ObjectNode values, String... tot) {
		JsonNode root = null;
		String log = "";
		try {
			String path = this.currentPath;
			if (!path.equals("")) {
				File sourceFile = new File(path);
				if (sourceFile.exists()) {
					root = mapper.readTree(sourceFile);
					if (root != null) {
						if (root.has(key)) {
							log = "Error: This key is already exists in our DB";
						} else {
							values.put("TTL", "" + getTimeToLiveValue().getTime() + "");
							((ObjectNode) root).set(key, values);
							mapper.writeValue(sourceFile, root);
							log = "Info: This " + key + " and Values " + values + " has been added successfully";
						}
					}
				} else {
					root = mapper.createObjectNode();
					values.put("TTL", "" + getTimeToLiveValue().getTime() + "");
					((ObjectNode) root).set(key, values);
					mapper.writeValue(sourceFile, root);
					log = "Info: This " + key + "and Value " + values + " has been added successfully";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return log;
	}

	private static Calendar getTimeToLiveValue() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, TTLinterval);
		return cal;
	}

	private String checkAndCreateFilePath(String optionalPath) {
		
		try {
			if (!optionalPath.equals("")) {
				File path = new File(optionalPath);
				if (path.exists()) {
					this.currentPath = optionalPath + "\\local_storage.json";
				} else {
					System.out.println("Error: Please Check Your Folder Path: "+optionalPath);
				}
			} else {
				File newFileObj = new File(defaultFilePath);
				if (newFileObj.exists()) {
					this.currentPath  = defaultFilePath + "\\local_storage.json";
				} else {
					if (newFileObj.mkdir()) {
					 this.currentPath  = defaultFilePath + "\\local_storage.json";
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentPath;
	}
}