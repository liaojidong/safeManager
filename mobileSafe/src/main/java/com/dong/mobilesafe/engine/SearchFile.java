package com.dong.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchFile {
	
	 // 存放文件  
    private List<File> fileList = new ArrayList<File>();  
    private File file;// 搜索的目录  
    private String regex; // 正则表达式  
    
    
    public SearchFile(File file) {
    	this.file = file;
    	this.regex = ".";
    }
    
    
    public SearchFile(File file ,String regex) {
    	this.file = file;
    	this.regex = regex;
    }
    
    
    public List<File> allFiles() {
    	if(!fileList.isEmpty()) {
    		fileList.clear();
    	}
    	search(file);
    	return fileList;
    }
    
    
    private void search(File file) {
    	File[]files = file.listFiles();
    	if(files == null || files.length == 0) {
    		return;
    	}
    	for(File f: files) {
    		if(f.isDirectory()) {
    			search(f);
    		}else {
    			if(f.getName().matches(regex)) {
    				fileList.add(f);
    			}
    		}
    	}
    	
    }
    
    

}
