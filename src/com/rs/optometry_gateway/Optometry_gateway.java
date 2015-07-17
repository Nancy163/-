package com.rs.optometry_gateway;

import java.io.File;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/*
 * 验光仪网关应用
 * 
 */

public class Optometry_gateway extends Application {
	
	Comm_Manager gwManager=null;
	Optometry_service gwService=null;
	Optometry_console gwConsole=null;
	
	boolean gwconfiged=false;
	
	boolean gwservice_running=false;
	
	String gwDeviceName="";
	String gwDeviceMac="";
	
	String gwServerName="";
	String gwServerIP="";
	int gwServerPort=5000;
	
	int gwID=0;
	String gwInfo="";
	String gwName="";
	
	//static Logger OptLog=null;
	static String Tag="Optometry";
	
	
	@Override   
	public void onCreate() { 
		configLog();
		Log("Optomertry Gatway Start");
		gwLoadConfig();
		gwManager=new Comm_Manager();
		gwManager.m_App=this;
		return;		
	}
	
	/*
	 * 保持网关配置
	 */
	public void gwSaveConfig()
	{
		SharedPreferences sharedPreferences = getSharedPreferences("gwConfig", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();//获取编辑器
		
		editor.putString("gwDeviceName", gwDeviceName);
		editor.putString("gwDeviceMac", gwDeviceMac);
		editor.putString("gwServerName", gwServerName);
		editor.putString("gwServerIP", gwServerIP);
		editor.putInt("gwServerPort", gwServerPort);
		
		editor.putInt("gwID", gwID);
		editor.putString("gwInfo", gwInfo);
		editor.putString("gwName", gwName);

		editor.commit();//提交修
	}
	
	/*
	 * 加载网关配置
	 */
	public void gwLoadConfig()
	{
		SharedPreferences sharedPreferences = getSharedPreferences("gwConfig", Context.MODE_PRIVATE);
		if(sharedPreferences==null)
			return;

		gwDeviceName=sharedPreferences.getString("gwDeviceName", "");
		gwDeviceMac=sharedPreferences.getString("gwDeviceMac", "");
		gwServerName=sharedPreferences.getString("gwServerName", "");
		gwServerIP=sharedPreferences.getString("gwServerIP", "");	
		gwServerPort=sharedPreferences.getInt("gwServerPort", 5000);
		
		gwID=sharedPreferences.getInt("gwID", 0);
		gwInfo=sharedPreferences.getString("gwInfo", "");
		gwName=sharedPreferences.getString("gwName", "");
		
	}
	
	public void configLog()
	{
		/*
	
		final LogConfigurator logConfigurator = new LogConfigurator();
		String LogFileName=Environment.getExternalStorageDirectory() + File.separator + "Optometry_log.txt";
		logConfigurator.setFileName(LogFileName);
		logConfigurator.setRootLevel(Level.DEBUG);
		logConfigurator.setLevel("org.apache", Level.ERROR);
		logConfigurator.configure();

		OptLog = Logger.getLogger(this.getClass());
		*/
	}		

	public static void Log(String mlog)
	{
		//OptLog.debug(mlog);
		Log.i(Tag, mlog);
	}
	
	public static void LogBytes(String info, byte[] CmdStream)
	{
		/*
		String result=info+": ";
		
		for(int i=0;i<CmdStream.length;i++)
		{
			result+=Integer.toHexString(CmdStream[i] & 0xFF);
			result+=" ";
		}
		
		OptLog.debug(result);
		*/
		
		String result=info+": ";
		
		for(int i=0;i<CmdStream.length;i++)
		{
			result+=Integer.toHexString(CmdStream[i] & 0xFF);
			result+=" ";
		}
		Log.i(Tag, result);
		
	}	

}
