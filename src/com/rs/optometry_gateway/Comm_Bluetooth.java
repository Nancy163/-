package com.rs.optometry_gateway;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/*
 * ����ͨ��ģ��
 * ����ͨ��ģ�����ڶ�����ǽ���ͨ�Ŵ���
 * 
 * 
 */

public class Comm_Bluetooth extends Comm_Base implements Comm_IF_DataReceiver {
	
	
    private static final String TAG = "Comm Bluetooth";  	
	
	/*
	 * ���������߳�
	*/
    private BTConnectThread mConnectThread;
    private BTConnectedThread mConnectedThread;
      
    /*
     * ��������
    */
    private  String BtDeviceAddress;
    
    /*
     * �豸����������
    */
    private BluetoothAdapter mBtAdapter;
    BluetoothDevice Optometry_Device=null;
    
    /*
     * �����������
    
    */
    int BT_ReceiverCouter=0;
	
	//�豸UUID
    private static final UUID DEVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");        
	
 
    /*
     * ��ʼ��
    */
    public Comm_Bluetooth()
    {
    	mBtAdapter = BluetoothAdapter.getDefaultAdapter(); 	
    	
    	//���û�������豸
    	if(mBtAdapter==null)
    		Connect_status=STATE_ADAPTERFAIL;
    	
    	//��������豸δ����
    	if(!mBtAdapter.isEnabled())
    		Connect_status=STATE_ADAPTERFAIL;
 
    	return;
    }
 

    /*
     *���������ĵ�ַ 
    */
    public void BT_SetDeviceAddress(String DeviceAddress)
    {
    	BtDeviceAddress=DeviceAddress;
    }
    
    /*
     *��ȡ�����豸�б� 
    */
    public String[] GetBTDeviceList()
    {    	
    	String bt_name;
    	
    	Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
    	String [] DeviceList=new String[pairedDevices.size()];
    	
    	int ListNum=0;
    	
        for (BluetoothDevice device : pairedDevices) {
        	bt_name=device.getName();
        	bt_name+="\n";
        	bt_name+=device.getAddress();
        	DeviceList[ListNum]=bt_name;
        	ListNum++;
        }
    	return DeviceList;
    }
    
    /*
     *��ȡ�豸��ID��ַ 
    */
    public static String BT_GetAddress(String str)
    {
    	return str.substring(str.length()-17);
    }
    
   
    /*
     * ������������
     * @see com.rs.optometry_gateway.Comm_Base#Start()
     */
    
    @Override
    public synchronized boolean Start()  //��������
    {

    	   	
    	if(super.Start()==false)
    		return false;
    	
    	if(BtDeviceAddress==null)
    		return false;
    	  	
    	Log.e(TAG,"Bluetooth Start");	
    	
    	if(mConnectedThread!=null) {mConnectedThread.interrupt(); mConnectedThread.cancel(); mConnectedThread = null;}     	
    	if(mConnectThread!=null) {mConnectThread.cancel(); mConnectThread = null;}     	
    	
    	
    	SetConnectStatus(STATE_NONE);
        	
    	mConnectThread=new BTConnectThread();
    	if(mConnectThread.ConnectDevice()==false)
    	{
    		mConnectThread=null;
    		return false;
    	}

    	mConnectThread.start();	  
    	return true;
    }
    
    /*
     * �ر���������
     * @see com.rs.optometry_gateway.Comm_Base#Stop()
     */
    @Override
    public synchronized boolean Stop()   //�ر�����
    {  
    	SetConnectStatus(STATE_NONE);
        if (mConnectedThread != null) 
        {
        	mConnectedThread.interrupt();
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }
        
        if(mConnectThread != null) 
        {
        	mConnectThread.cancel(); 
        	mConnectThread = null;
        }  
        
        return true;

    } 
      

    /*
     * �������ݵ��豸
     * @see com.rs.optometry_gateway.Comm_Base#SendData(byte[])
     */
    @Override
    public synchronized void SendData(byte[] Comm_Data) {
       
    	//���λ��������״̬
    	if(Connect_status!=STATE_CONNECTED)
    		return;
    	
        BTConnectedThread r;
        synchronized (this)
        {
            r = mConnectedThread;
        }
        r.write(Comm_Data);
    }
    
    /*
     * c��ʼ�������豸
     */
    private synchronized void BT_Device_Init()         //��ʼ��BT�豸
    {

    	String BT_Address;
    	Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
        	BT_Address=device.getAddress();
        	if(BT_Address.equals(BtDeviceAddress))
        	{
        		Optometry_Device=device;
        		break;
        	}
        }    	
    }	   

    /*
     * ������������
     */
   public synchronized void BT_Connected(BluetoothSocket socket) //��ʼ��������ɺ󣬽�����������
   {   
	   
	   mConnectedThread = new BTConnectedThread(socket);
       mConnectedThread.start();
    } 
   
   
	
	//���ӽ��̣����ڿ������ӹ��̣����ӳɹ�����̽���	    
	private class BTConnectThread extends Thread {	    	
		private BluetoothSocket mmSocket;  
		public BTConnectThread()
		{
					
		}
		public boolean ConnectDevice() {
			
			SetConnectStatus(STATE_CONNECTING);   
			BluetoothSocket tmp = null;
		            		            
		    if(!mBtAdapter.isEnabled())
		    {
		        return false;
		    }	 
		        	
		    BT_Device_Init(); 
		        	
		    if(Optometry_Device==null)
		    {
		       //û�з����豸
		    	return false;
		    }   	        	
		    try {
		        tmp = Optometry_Device.createRfcommSocketToServiceRecord(DEVICE_UUID);
		    } catch (IOException e) {
		        Log.e(TAG, "create() failed", e);
		    }
		    mmSocket = tmp;
		    return true;
		}

		public void run() {
		    Log.i(TAG, "BEGIN ConnectThread");
		    setName("ConnectThread");
		    mBtAdapter.cancelDiscovery();

		    try {
		        mmSocket.connect();
		    } catch (IOException e) {
		    	Connect_Close();
		        // Close the socket
		        try {
		            mmSocket.close();
		        } catch (IOException e2) {
		            Log.e(TAG, "unable to close() socket during connection failure", e2);
		        }
		        SendManagerCmd(12);
		        return;
		    }

		    mConnectThread = null;
		    BT_Connected(mmSocket);
		}

		public void cancel() {
		    try {
		        mmSocket.close();
		        } catch (IOException e) 
		        {
		        Log.e(TAG, "close() of connect socket failed", e);
		        }
		        SendManagerCmd(11);
		    }
		}        
		        
		        

	//ͨ�Ž��̣����ӳɹ�����������ͨѶ
	private class BTConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		        
		public BTConnectedThread(BluetoothSocket socket) {
		    Log.d(TAG, "create ConnectedThread");
		    mmSocket = socket;
		    InputStream tmpIn = null;
		    OutputStream tmpOut = null;
		    // Get the BluetoothSocket input and output streams
		    try {
		        tmpIn = socket.getInputStream();
		        tmpOut = socket.getOutputStream();
		        } catch (IOException e) 
		        {
		            Log.e(TAG, "sockets not created", e);
		        }
		    
		    SendManagerCmd(10);
		    SetConnectStatus(STATE_CONNECTED);  
		    mmInStream = tmpIn;
		    mmOutStream = tmpOut;
		}

		public void run() {
		    Log.i(TAG, "BEGIN mConnectedThread");
		    byte[] buffer = new byte[0x100];
		    int len;

		    while (!this.isInterrupted()) {
		        try {
		            // Read from the InputStream
		            len = mmInStream.read(buffer);
		            if(len!=0)
		            {
		                BT_ReceiverCouter++;
		                if(Sender!=null)
		                {
		           			byte [] BufferData=new byte[len+4];
		           			System.arraycopy(buffer,0,BufferData,4,len);
		           			BufferData[0]=0x55;
		                	Sender.Comm_IF_DataSend(BufferData);
		                }    
		            }

		        } catch (IOException e) {              
		        	Connect_Close();
		            break;
		        }
		    }
		            
		    Log.e(TAG,"Receiver Exit");
		}

		/*
		 * ������д���豸 
	     */
		public void write(byte[] buffer) {			
		    if(mmOutStream!=null)
		    try {
		        mmOutStream.write(buffer,0,buffer.length);
		    } catch (IOException e) {
		        Log.e(TAG, "Exception  write", e);
		    }
		}

		public void cancel() {
		    Log.e(TAG,"Read thread cancel");
		        	
		    try {
				mmInStream.close();
				mmOutStream.close();
			} catch (IOException e1) {
				Log.e(TAG, "Close socket Error", e1);
			}
					
		    try {
		        mmSocket.close();
		    } catch (IOException e) {
		        Log.e(TAG, "close() of cancel  failed", e);
		    }	
		    
		    SendManagerCmd(11);
		            	
		}
	}



	@Override
	public void Comm_IF_DataReceiver(byte[] mData) {
		SendData(mData);
	}   
}
