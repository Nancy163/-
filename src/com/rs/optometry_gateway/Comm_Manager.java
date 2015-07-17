package com.rs.optometry_gateway;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 *  通信管理模块，用于管理远程、蓝牙、本地通信模块；并实现远程服务器管理控制
 */

public class Comm_Manager extends Comm_Base implements Comm_IF_Manager,Comm_IF_Command{
	
	public static final int CTRLMODE_NONO=0;	
	public static final int CTRLMODE_SERVER=1;	 
	public static final int CTRLMODE_LOCAL=2;	
	
	public Comm_RemoteService mService=null;
	public Comm_Bluetooth mBTDevice=null;
	public Comm_Local mLocal=null;
	
	
	private int DeviceStatus=0;
	
	Optometry_gateway m_App;
	
	private int DeviceCtrlStatus=0;
	
	public Comm_Manager()
	{
		mService=new Comm_RemoteService();
		mBTDevice=new Comm_Bluetooth();
		mLocal=new Comm_Local();
		
		Optometry_packet.Optometry_Device=mLocal;
		
		mService.Manager=this;
		mService.Command=this;		
		mBTDevice.Manager=this;
	
	}
	
	/*
	 * 获取蓝牙列表
	 */
	public String[] GetBTDeviceList()
	{
		return mBTDevice.GetBTDeviceList();
	}
	
	/*
	 * 连接远程服务器
	 */	
	public synchronized void ConnectServer()
	{
		if(m_App.gwServerIP.length()==0&&m_App.gwServerName.length()==0)
		{
			mService.Comm_status=STATE_CONFIG_ERROR;
			return;
		}
		
		if(!isNetworkConnected(this.m_App.gwConsole))
		{
			mService.SetConnectStatus(STATE_CONNECT_NETWORK_CLOSE);
			return ;
		}
		
		mService.SetServerAdd(m_App.gwServerName,m_App.gwServerIP,m_App.gwServerPort);
		mService.Start();
	}
	
	
	/*
	 * 启动蓝牙连接
	 */		
	public synchronized void ConnectBTDevice()
	{
		if(m_App.gwDeviceMac.length()==0)
		{
			mBTDevice.Comm_status=STATE_CONFIG_ERROR;
			return;
		}
		mBTDevice.BT_SetDeviceAddress(m_App.gwDeviceMac);
		mBTDevice.Start();
	}	
	
	/*
	 * 检测是否有网络连接
	 */
    public boolean isNetworkConnected(Context context) { 
    	if (context != null) { 
    	ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
    	.getSystemService(Context.CONNECTIVITY_SERVICE); 
    	NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
    	if (mNetworkInfo != null) { 
    	return mNetworkInfo.isAvailable(); 
    	} 
    	} 
    	return false; 
    	}	
	
	
	/*
	 * 获取蓝牙连接状态
	 */	
	public int GetBTDeviceStatus()
	{
		return mBTDevice.Connect_status;
	}
	
	/*
	 * 获取远程连接状态
	 */		
	public int GetRemoteServerStatus()
	{
		return mService.Connect_status;
	}	
	
	
	/*
	 * 获取设备控制状态
	 */		
	public int GetDeviceCtrlStatus()
	{
		return DeviceCtrlStatus;
	}

	/*
	 * 发送命令数据
	 */
	private void SendCmdData(String Cmd)
	{
		byte[] CmdString={0x0};
		byte[] CmdData=null;
		
		try {
			CmdString=Cmd.getBytes("UTF8");
		} 
		
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		
		CmdData=new byte[CmdString.length+4];
		CmdData[0]=(byte)0xaa;
		System.arraycopy(CmdString, 0, CmdData, 4, CmdString.length);
		
		mService.SendData(CmdData);
	}
	
	
	/*
	 * 服务器注册
	 */
	private void RegisterSever()
	{
		String jsonresult="";
        JSONObject jsonObj = new JSONObject();
        try {
			jsonObj.put("CMD", 11);
			jsonObj.put("ID", m_App.gwID);			
	        jsonObj.put("TYPE", 1);  
	        jsonObj.put("NAME", m_App.gwName);  
	        jsonObj.put("INFO", m_App.gwInfo);  	        
				
		} catch (JSONException e) {
			e.printStackTrace();
		}

        jsonresult=jsonObj.toString();	
        SendCmdData(jsonresult);
	}
	
	/*
	 * 更新网关状态信息
	 */	
	private void Cmd_UpdateGwStatus()
	{
		String jsonresult="";
        JSONObject jsonObj = new JSONObject();

        try {
			jsonObj.put("CMD", 22);
			jsonObj.put("ID", m_App.gwID);			
	        jsonObj.put("DEVICE_STATUS", DeviceStatus);  
	        jsonObj.put("CTRL_STATUS", DeviceCtrlStatus);          
				
		} catch (JSONException e) {
			e.printStackTrace();
		}

        jsonresult=jsonObj.toString();	
        SendCmdData(jsonresult);
	}	
	
	/*
	 * 获取网关远程控制
	 */		
	private void Cmd_GetGwCtrl()
	{
		String jsonresult="";
        JSONObject jsonObj = new JSONObject();
        
        mService.Receiver=mBTDevice;
        mBTDevice.Sender=mService;       
        //被远程连接
    	DeviceCtrlStatus=1;
        try {
			jsonObj.put("CMD", 26);
			jsonObj.put("RESULT", 1);			       
				
		} catch (JSONException e) {
			e.printStackTrace();
		}

        jsonresult=jsonObj.toString();	
        SendCmdData(jsonresult);
	}	
	
	/*
	 * 获取网关本地控制
	 */		
	public void Cmd_GetLocalCtrl()	
	{
        mLocal.Receiver=mBTDevice;
        mBTDevice.Sender=mLocal;
        //被远程连接
    	DeviceCtrlStatus=2;
	}
	
	/*
	 * 释放网关控制
	 */		
	public void Cmd_ReleaseGwCtrl()
	{
		String jsonresult="";
        JSONObject jsonObj = new JSONObject();//pet对象，json形式  
        
        mService.Receiver=null;
        mBTDevice.Sender=null;  
        
        //释放控制
    	DeviceCtrlStatus=0;     

        try {
			jsonObj.put("CMD", 28);
			jsonObj.put("RESULT", 1);			       
				
		} catch (JSONException e) {
			e.printStackTrace();
		}

        jsonresult=jsonObj.toString();	
        SendCmdData(jsonresult);
	}		
	
	/*
	 * 远程蓝牙设备重新连接请求
	 */
	private void Cmd_ConnectDevice()
	{
		String jsonresult="";
        JSONObject jsonObj = new JSONObject();
          
        try {
			jsonObj.put("CMD", 24);
			jsonObj.put("RESULT", 1);			       
				
		} catch (JSONException e) {
			e.printStackTrace();
		}		
        jsonresult=jsonObj.toString();	
        SendCmdData(jsonresult);		
	
		ConnectBTDevice();
		return;
	}
	
	/*
	 * 设备俩就重连接后发送状态
	 */
	private void Cmd_SendDeviceStatus()
	{

		String jsonresult="";
        JSONObject jsonObj = new JSONObject();
        
    	DeviceCtrlStatus=0;     

        try {
			jsonObj.put("CMD", 24);
			jsonObj.put("RESULT", DeviceStatus);			       
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
        jsonresult=jsonObj.toString();	
        SendCmdData(jsonresult);		
		return;
	}	
	
	
    @Override
    public  boolean Start()
    {
    	ConnectBTDevice();
    	ConnectServer();
    	return true;
    }
    
    @Override
    public void Reset()
    {
    	mService.Reset();
    	mBTDevice.Reset();
    }
    
    //复位蓝牙
    public void ResetBt()
    {
    	mBTDevice.Reset();
    }  
    
    //复位远程服务器
    public void ResetServer()
    {
    	mService.Reset();
    }     
    
    //检测连接状态
    public void CheckConnect()
    {   	
    	if(mBTDevice.CheckConnectTimeOut())
    	{
    		mBTDevice.Stop();

    	}
    	
    	if(mService.CheckConnectTimeOut())
    	{
    		mService.Stop();
    		
    	}
    	  	
    	
    	if(!mBTDevice.isConnecting())
    		ConnectBTDevice();
    	
    	if(!mService.isConnecting())
    	{
    		mService.Comm_ReconnectTimes=5;
    		ConnectServer();  		
    	}
    			
    }
    
    //心跳线
    public void KeepAlive()
    {
    	mService.SendKeepAlive();  		
    }
    
    //检测发送信息
    public void CheckSend()
    {
    	if(DeviceCtrlStatus==2)
    		mLocal.CheckSendPk();
    }
	
    //处理连接管理信息
	@Override
	public void Comm_IF_Manager(int mCmd) {
		switch(mCmd)
		{
		case NETWORK_CONNECT_SUCCES:
			RegisterSever();
			break;
		case NETWORK_CONNECT_CLOSE:
			if(DeviceCtrlStatus==1)
				Cmd_ReleaseGwCtrl();
			break;
		case BT_CONNECT_SUCCES:
			DeviceStatus=1;
			Cmd_SendDeviceStatus();
			break;
		case BT_CONNECT_FAIL:
			DeviceStatus=0;
			Cmd_SendDeviceStatus();
			break;			
		}
	}

	//编码和发送服务器指令
	@Override
	public void Comm_IF_Command(byte[] mData) {
		String Cmd=null;
		int Cmd_ID=-1;
		try {
			Cmd=new String(mData,"UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
        JSONObject jsonObject=null;
        try {
			jsonObject=new JSONObject(Cmd);
			Cmd_ID=jsonObject.getInt("CMD");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}//		
		
        switch(Cmd_ID)
        {
        case 21://获取节点信息
        	Cmd_UpdateGwStatus();
        	break;
        case 23://
        	Cmd_ConnectDevice();
        	break;       	
        case 25://获取设备控制
        	Cmd_GetGwCtrl();
        	break;	
        case 27://获取设备控制
        	Cmd_ReleaseGwCtrl();
        	break;	       	        
        }		
	}

}
