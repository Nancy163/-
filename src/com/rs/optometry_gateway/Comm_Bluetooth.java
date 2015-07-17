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
 * 蓝牙通信模块
 * 蓝牙通信模块用于对验光仪进行通信传输
 * 
 * 
 */

public class Comm_Bluetooth extends Comm_Base implements Comm_IF_DataReceiver {
	
	
    private static final String TAG = "Comm Bluetooth";  	
	
	/*
	 * 蓝牙连接线程
	*/
    private BTConnectThread mConnectThread;
    private BTConnectedThread mConnectedThread;
      
    /*
     * 蓝牙配置
    */
    private  String BtDeviceAddress;
    
    /*
     * 设备蓝牙管理器
    */
    private BluetoothAdapter mBtAdapter;
    BluetoothDevice Optometry_Device=null;
    
    /*
     * 蓝牙管理计数
    
    */
    int BT_ReceiverCouter=0;
	
	//设备UUID
    private static final UUID DEVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");        
	
 
    /*
     * 初始化
    */
    public Comm_Bluetooth()
    {
    	mBtAdapter = BluetoothAdapter.getDefaultAdapter(); 	
    	
    	//如果没有蓝牙设备
    	if(mBtAdapter==null)
    		Connect_status=STATE_ADAPTERFAIL;
    	
    	//如果蓝牙设备未开启
    	if(!mBtAdapter.isEnabled())
    		Connect_status=STATE_ADAPTERFAIL;
 
    	return;
    }
 

    /*
     *设置蓝牙的地址 
    */
    public void BT_SetDeviceAddress(String DeviceAddress)
    {
    	BtDeviceAddress=DeviceAddress;
    }
    
    /*
     *获取蓝牙设备列表 
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
     *获取设备的ID地址 
    */
    public static String BT_GetAddress(String str)
    {
    	return str.substring(str.length()-17);
    }
    
   
    /*
     * 启动蓝牙连接
     * @see com.rs.optometry_gateway.Comm_Base#Start()
     */
    
    @Override
    public synchronized boolean Start()  //启动连接
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
     * 关闭蓝牙连接
     * @see com.rs.optometry_gateway.Comm_Base#Stop()
     */
    @Override
    public synchronized boolean Stop()   //关闭连接
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
     * 发送数据到设备
     * @see com.rs.optometry_gateway.Comm_Base#SendData(byte[])
     */
    @Override
    public synchronized void SendData(byte[] Comm_Data) {
       
    	//如果位处于连接状态
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
     * c初始化蓝牙设备
     */
    private synchronized void BT_Device_Init()         //初始化BT设备
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
     * 建立数据连接
     */
   public synchronized void BT_Connected(BluetoothSocket socket) //初始化连接完成后，建立数据连接
   {   
	   
	   mConnectedThread = new BTConnectedThread(socket);
       mConnectedThread.start();
    } 
   
   
	
	//连接进程，用于控制连接过程，连接成功后进程结束	    
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
		       //没有发现设备
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
		        
		        

	//通信进程，连接成功后用于蓝牙通讯
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
		 * 将数据写入设备 
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
