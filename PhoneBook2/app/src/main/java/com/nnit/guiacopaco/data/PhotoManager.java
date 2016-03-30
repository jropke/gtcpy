package com.nnit.guiacopaco.data;

import java.io.File;
import java.util.HashMap;

public class PhotoManager {
	private static PhotoManager _instance = null;
	
	private boolean bLoaded = false;
	private static Object lock = new Object();
	
	private HashMap<String, String> photos = new HashMap<String, String>();
	
	private PhotoManager(){
		
	}
	
	public static PhotoManager getInstance(){
		if(_instance == null){
			_instance = new PhotoManager();
		}
		return _instance;
	}
	
	public String getPhotoFilenameByInitials(String initials){
		if(!bLoaded){
			loadPhotosInfo();
		}
		if(photos.containsKey(initials.toLowerCase())){
			return photos.get(initials.toLowerCase());
		}
		return null;
	}
	
	public void reload(){
		synchronized(lock){
			photos.clear();
			bLoaded = false;
		}
	}
	
	protected boolean loadPhotosInfo(){
		synchronized(lock){			
			bLoaded =  loadPhotosInfo(new File(DataPackageManager.getInstance().getPhotoDirAbsolutePath()));
		}
		return bLoaded;
	}
	
	
	private boolean loadPhotosInfo(File rootDir) {
		if(!rootDir.exists()||(!rootDir.isDirectory())){
			return false;
		}
		
		File[] subFiles = rootDir.listFiles();
		for(File f: subFiles){
			if(f.isDirectory()){
				loadPhotosInfo(f);
			}else if(f.isFile()){
				String initials = getInitilas(f);
				if(initials != null){
					photos.put(initials.toLowerCase(), f.getAbsolutePath());
				}
			}	
		}
		
		return true;
	}
	
	private String getInitilas(File file){
		int pos = -1;
		String filename = file.getName();
		if((pos = filename.lastIndexOf(".")) != -1){
			String initials = filename.substring(0, pos);
			return initials;
		}else{
			return filename;
		}
		
	}

}
