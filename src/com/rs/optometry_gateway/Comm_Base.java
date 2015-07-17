package com.rs.optometry_gateway;


/*
 * ����ͨ��ģ�飺�ṩ���ͨ�ŵĹ���ģ��
 * 
 */


/*
 * Comm_IF_Manager:ͨ�Ź���ӿ�
 * mCmd: ������
 * 
 */

interface Comm_IF_Manager 
{
	void  Comm_IF_Manager(int mCmd);
}

/*
 * Comm_IF_Command:����ӿ�
 * mData: ��Ҫ���͵���������
 * 
 */

interface Comm_IF_Command 
{
	void  Comm_IF_Command(byte[] mData);
}

/*
 * Comm_IF_DataSend:���ݷ��ͽӿ�
 * mData: ��Ҫ���͵�����
 * 
 */

interface Comm_IF_DataSend 
{
	void  Comm_IF_DataSend(byte[] mData);
}

/*
 * Comm_IF_DataSend:���ݽ��մ���ӿ�
 * mData: ���յ�������
 * 
 */

interface Comm_IF_DataReceiver 
{
	void  Comm_IF_DataReceiver(byte[] mData);
}


/*
 * Comm_Base:������ͨ�Ŷ���
 *
 */


public class Comm_Base {
	final static int IDEL=0;
	final static int RUN=1;
	final static int BUSY=2;
	
	public static final int DATA_TYPE_NULL=0;
	public static final int DATA_TYPE_UP=1;	
	public static final int DATA_TYPE_DOWN=2;		
	
	public static final int STATE_ADAPTERFAIL = -1;       // ͨ�Žӿ�Ϊ��
    public static final int STATE_NONE = 0;       // ͨ�Žӿ�Ϊ��
    public static final int STATE_CONNECTING = 1; // ͨ�Žӿ�����������
    public static final int STATE_CONNECTED = 2;  // ͨ�Žӿ��Ѿ�����	
    public static final int STATE_CONFIG_ERROR = 3;  // ͨ�Žӿ��Ѿ�����	
    public static final int STATE_CONNECT_FALI = 4;  // ����ʧ��	
    public static final int STATE_CONNECT_CLOSE = 5;  // ���ӹر�	
    public static final int STATE_CONNECT_NETWORK_CLOSE = 6;  // ���ӹر�	
    
	public static final int NETWORK_CONNECT_SUCCES=101;
	public static final int NETWORK_CONNECT_CLOSE=102;
	public static final int NETWORK_CONNECT_FAIL=103;

	public static final int BT_CONNECT_SUCCES=103;	
	public static final int BT_CONNECT_FAIL=104;  
	
	private long Comm_StartTime=0;
	private long Comm_ConnectTimeOut=10000;
	
	int Comm_ReconnectTimes=5;
		
	
	//ģ��������
    Comm_IF_Manager Manager;
	
	//���ݽ��ն���
    Comm_IF_DataReceiver Receiver;
	
	//���ݷ��Ͷ���
	Comm_IF_DataSend Sender;    
	
	//ָ���
	Comm_IF_Command Command;

	
	//ģ��ͨ��״̬
	int Comm_status=IDEL;
	
	//ģ������״̬
	int Connect_status=STATE_NONE;
	
	//ģ��״̬
	int Module_status=0;
	
	//ģ������
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
	
	//ģ��ر�
	boolean Stop()
	{
	   return true;
	}
	
	//ģ�鸴λ
	void Reset()
	{
		Comm_ReconnectTimes=5;
		Connect_status=STATE_NONE;
	}	
	
	//��������
	void SendData(byte[] Comm_Data)
	{
		
	}
	
	//���͹����߷���ָ��ָ��
	void SendManagerCmd(int mCommand)
	{
		if(Manager!=null)
		{
			Manager.Comm_IF_Manager(mCommand);
		}			
	}	
	
	//��ʱ���	
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
	
	//���Ӽ��
	int CheckReConnect()
	{
		return Comm_ReconnectTimes;
	}
	
	//�Ƿ�������״̬
	boolean isConnecting()
	{
		if(Connect_status==STATE_CONNECTED||Connect_status==STATE_CONNECTING)
			return true;
		else 
			return false;
	}
	
	
	//�ر�����
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
    	
	
	//��������״̬
	void SetConnectStatus(int mstatus)
	{
		Connect_status=mstatus;
	}
	
    //����ʧ��
	public void ConnectFailed()
    {
    }
	
	//���ӳɹ�
    public void ConnectSuccess()
    {
    }
	
	//int ת  byte[] 
	public static byte[] intToByteArray(int integer) {
		int byteNum = (40 -Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer))/ 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
		byteArray[3 - n] = (byte) (integer>>> (n * 8));

		return (byteArray);
		}

	// byte[] ת int 
	public static int byteArrayToInt(byte[] b) {
	    int value= 0;
		       for (int i = 0; i < 4; i++) {
		           int shift= (4 - 1 - i) * 8;
		           value +=(b[i] & 0x000000FF) << shift;
		       }
		       return value;
		 }
	
}

