package com.nnit.guiacopaco.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import com.nnit.guiacopaco.data.DataPackageManager;

public class ConfigManager {
	public static String CONFIG_FAVORITELIST = "favorite_list";
	public static String CONFIG_SHOWGUIDEPAGE = "show_guide_page";
	public static String CONFIG_START_WIDGETUPDATE_SERVICE="start_update_widget_service";
	public static String CONFIG_WIDGETUPDATE_INTERVAL="update_widget_interval";

	
	
	private static ConfigManager _instance = null;
	
	private boolean bLoaded = false;
	private Properties props = null;
	private static Object lock = new Object();
	
	private ConfigManager(){
		 props = new Properties();
	}
	
	public static ConfigManager getInstance(){
		if(_instance == null){
			_instance = new ConfigManager();
		}
		return _instance;
	}

	protected void loadConfiguration(){
		synchronized(lock){
			FileInputStream fis = null;
			String filename = DataPackageManager.getInstance().getPropertiesFilename();
			try{
				File f = new File(filename);
				if(f.exists() && f.isFile()){
					 fis = new FileInputStream(f);
					 props.load(fis);			 
				}
			}catch(Exception exp){
				exp.printStackTrace();
			}finally{
				if(fis != null){
					try {
						fis.close();
					} catch (IOException e) {
					}
					fis = null;
				}
			}
			bLoaded = true;
		}
	}
	
	
	protected boolean saveConfiguration(){
		synchronized(lock){
			FileOutputStream fos = null;
			String filename = DataPackageManager.getInstance().getPropertiesFilename();
			try{
				File f = new File(filename);
				if(f.exists() && f.isFile()){
					f.delete();
				}
				f.createNewFile();
				
				fos = new FileOutputStream(f);
				props.store(fos, null);
				fos.flush();
				return true;
			}catch(Exception exp){
				exp.printStackTrace();
				return false;
			}finally{
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
					}
					fos = null;
				}
			}
		}
	}
	
	public String getConfigure(String key){
		if(!bLoaded){
			loadConfiguration();
		}
		return props.getProperty(key);
	}
	
	public boolean saveConfigure(String key, String value){
		if(!bLoaded){
			loadConfiguration();
		}
		synchronized(lock){
			props.setProperty(key, value);
		}
		return saveConfiguration();
	}

	public void reloadConfigures() {
		synchronized(lock){
			props.clear();
			bLoaded = false;
		}
	}
}
