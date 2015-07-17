package com.rs.optometry_gateway;


/*
 * 基础通信模块：提供多层通信的构建模型
 * 
 */


/*
 * Comm_IF_Manager:通信管理接口
 * mCmd: 命令编号
 * 
 */

interface Comm_IF_Manager 
{
	void  Comm_IF_Manager(int mCmd);
}

/*
 * Comm_IF_Command:命令接口
 * mData: 需要发送的命令数据
 * 
 */

interface Comm_IF_Command 
{
	void  Comm_IF_Command(byte[] mData);
}

/*
 * Comm_IF_DataSend:数据发送接口
 * mData: 需要发送的数据
 * 
 */

interface Comm_IF_DataSend 
{
	void  Comm_IF_DataSend(byte[] mData);
}

/*
 * Comm_IF_DataSend:数据接收处理接口
 * mData: 接收到的数据
 * 
 */

interface Comm_IF_DataReceiver 
{
	void  Comm_IF_DataReceiver(byte[] mData);
}


/*
 * Comm_Base:基本的通信对象
 *
 */


public class Comm_Base {
	final static int IDEL=0;
	final static int RUN=1;
	final static int BUSY=2;
	
	public static final int DATA_TYPE_NULL=0;
	public static final int DATA_TYPE_UP=1;	
	public static final int DATA_TYPE_DOWN=2;		
	
	public static final int STATE_ADAPTERFAIL = -1;       // 通信接口为空
    public static final int STATE_NONE = 0;       // 通信接口为空
    public static final int STATE_CONNECTING = 1; // 通信接口正在连接中
    public static final int STATE_CONNECTED = 2;  // 通信接口已经连接	
    public static final int STATE_CONFIG_ERROR = 3;  // 通信接口已经连接	
    public static final int STATE_CONNECT_FALI = 4;  // 连接失败	
    public static final int STATE_CONNECT_CLOSE = 5;  // 连接关闭	
    public static final int STATE_CONNECT_NETWORK_CLOSE = 6;  // 连接关闭	
    
	public static final int NETWORK_CONNECT_SUCCES=101;
	public static final int NETWORK_CONNECT_CLOSE=102;
	public static final int NETWORK_CONNECT_FAIL=103;

	public static final int BT_CONNECT_SUCCES=103;	
	public static final int BT_CONNECT_FAIL=104;  
	
	private long Comm_StartTime=0;
	private long Comm_ConnectTimeOut=10000;
	
	int Comm_ReconnectTimes=5;
		
	
	//模块管理对象
    Comm_IF_Manager Manager;
	
	//数据接收对象
    Comm_IF_DataReceiver Receiver;
	
	//数据发送对象
	Comm_IF_DataSend Sender;    
	
	//指令发送
	Comm_IF_Command Command;

	
	//模块通信状态
	int Comm_status=IDEL;
	
	//模块连接状态
	int Connect_status=STATE_NONE;
	
	//模块状态
	int Module_status=0;
	
	//模块启动
	boolean Start()
	{
		
       if(Connect_status==STATE_ADAPTERFAIL)
    		return false;
    	
       if(Connect_status==STATE_CONNECTING)
    		return false;	
    	
       if(Comm_ReconnectTimes<0)
    		return false;
		
	   Comm_ReconnectTimes--;	   
	   Comm_StartTime=System.currentTimeMillis();
	   return true;
	}
	
	//模块关闭
	boolean Stop()
	{
	   return true;
	}
	
	//模块复位
	void Reset()
	{
		Comm_ReconnectTimes=5;
		Connect_status=STATE_NONE;
	}	
	
	//发送数据
	void SendData(byte[] Comm_Data)
	{
		
	}
	
	//发送管理者发送指令指令
	void SendManagerCmd(int mCommand)
	{
		if(Manager!=null)
		{
			Manager.Comm_IF_Manager(mCommand);
		}			
	}	
	
	//超时检测	
	boolean CheckConnectTimeOut()
	{
		long CurrentTime;
		if(Connect_status!=STATE_CONNECTING)
			return false;
		
		CurrentTime=System.currentTimeMillis();
		if((CurrentTime-Comm_StartTime)>Comm_ConnectTimeOut)
		{
			return true;
		}
		
		return false;
	}
	
	//连接检测
	int CheckReConnect()
	{
		return Comm_ReconnectTimes;
	}
	
	//是否在连接状态
	boolean isConnecting()
	{
		if(Connect_status==STATE_CONNECTED||Connect_status==STATE_CONNECTING)
			return true;
		else 
			return false;
	}
	
	
	//关闭连接
    public void Connect_Close()
    {
    	switch(Connect_status)
    	{
    	case STATE_CONNECTING:
    		SetConnectStatus(STATE_CONNECT_FALI);
    		break;
    	case STATE_CONNECTED:
    		SetConnectStatus(STATE_CONNECT_CLOSE);
    		break;
    	default:
    		SetConnectStatus(STATE_NONE);
    		break;
    		
    	}
    }
    	
	
	//设置连接状态
	void SetConnectStatus(int mstatus)
	{
		Connect_status=mstatus;
	}
	
    //连接失败
	public void ConnectFailed()
    {
    }
	
	//连接成功
    public void ConnectSuccess()
    {
    }
	
	//int 转  byte[] 
	public static byte[] intToByteArray(int integer) {
		int byteNum = (40 -Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer))/ 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
		byteArray[3 - n] = (byte) (integer>>> (n * 8));

		return (byteArray);
		}

	// byte[] 转 int 
	public static int byteArrayToInt(byte[] b) {
	    int value= 0;
		       for (int i = 0; i < 4; i++) {
		           int shift= (4 - 1 - i) * 8;
		           value +=(b[i] & 0x000000FF) << shift;
		       }
		       return value;
		 }
	
}

