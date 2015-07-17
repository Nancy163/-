package com.rs.optometry_gateway;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 *  ͨ�Ź���ģ�飬���ڹ���Զ�̡�����������ͨ��ģ�飻��ʵ��Զ�̷������������
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
	 * ��ȡ�����б�
	 */
	public String[] GetBTDeviceList()
	{
		return mBTDevice.GetBTDeviceList();
	}
	
	/*
	 * ����Զ�̷�����
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
	 * ������������
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
	 * ����Ƿ�����������
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
	 * ��ȡ��������״̬
	 */	
	public int GetBTDeviceStatus()
	{
		return mBTDevice.Connect_status;
	}
	
	/*
	 * ��ȡԶ������״̬
	 */		
	public int GetRemoteServerStatus()
	{
		return mService.Connect_status;
	}	
	
	
	/*
	 * ��ȡ�豸����״̬
	 */		
	public int GetDeviceCtrlStatus()
	{
		return DeviceCtrlStatus;
	}

	/*
	 * ������������
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
	 * ������ע��
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
	 * ��������״̬��Ϣ
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
	 * ��ȡ����Զ�̿���
	 */		
	private void Cmd_GetGwCtrl()
	{
		String jsonresult="";
        JSONObject jsonObj = new JSONObject();
        
        mService.Receiver=mBTDevice;
        mBTDevice.Sender=mService;       
        //��Զ������
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
	 * ��ȡ���ر��ؿ���
	 */		
	public void Cmd_GetLocalCtrl()	
	{
        mLocal.Receiver=mBTDevice;
        mBTDevice.Sender=mLocal;
        //��Զ������
    	DeviceCtrlStatus=2;
	}
	
	/*
	 * �ͷ����ؿ���
	 */		
	public void Cmd_ReleaseGwCtrl()
	{
		String jsonresult="";
        JSONObject jsonObj = new JSONObject();//pet����json��ʽ  
        
        mService.Receiver=null;
        mBTDevice.Sender=null;  
        
        //�ͷſ���
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
	 * Զ�������豸������������
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
	 * �豸���������Ӻ���״̬
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
    
    //��λ����
    public void ResetBt()
    {
    	mBTDevice.Reset();
    }  
    
    //��λԶ�̷�����
    public void ResetServer()
    {
    	mService.Reset();
    }     
    
    //�������״̬
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
    
    //������
    public void KeepAlive()
    {
    	mService.SendKeepAlive();  		
    }
    
    //��ⷢ����Ϣ
    public void CheckSend()
    {
    	if(DeviceCtrlStatus==2)
    		mLocal.CheckSendPk();
    }
	
    //�������ӹ�����Ϣ
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

	//����ͷ��ͷ�����ָ��
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
        case 21://��ȡ�ڵ���Ϣ
        	Cmd_UpdateGwStatus();
        	break;
        case 23://
        	Cmd_ConnectDevice();
        	break;       	
        case 25://��ȡ�豸����
        	Cmd_GetGwCtrl();
        	break;	
        case 27://��ȡ�豸����
        	Cmd_ReleaseGwCtrl();
        	break;	       	        
        }		
	}

}
