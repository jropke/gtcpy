package com.nnit.guiacopaco.ui;

import com.nnit.guiacopaco.R;

import android.content.Context;

public class SplashImageProviderManager {
	public static String RANDOM_PROVIDER = "random";
	
	private static SplashImageProviderManager _instance = null;
	private Context context = null;
	
	private SplashImageProviderManager(Context context){
		this.context = context;
	}

	public static SplashImageProviderManager getInstance(Context context){
		if(_instance == null){
			_instance = new SplashImageProviderManager(context);
		}
		return _instance;
	}
	
	public ISplashImageProvider getSplashImageProvider(String providerName){
		if(providerName.equalsIgnoreCase(RANDOM_PROVIDER)){
			return new RandomSplashImageProvider(context.getResources(), 
					new int[]{R.drawable.splash, R.drawable.splash1, R.drawable.splash2, 
				R.drawable.splash3, R.drawable.splash4, R.drawable.splash5, 
				R.drawable.splash6, R.drawable.splash7, R.drawable.splash8});
		}else{
			return new DefaultSplashImageProvider(context.getResources());
		}
	}
}
