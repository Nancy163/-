package com.rs.optometry_gateway;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;

/*
 * 远程通信模块，用于实现和远程服务器的通信
 * 
 * 
 */

public class Comm_RemoteService extends Comm_Base implements Comm_IF_DataSend{
	

	TcpClientThread TcpConnectedThread;	
	
	private String ServerDomainName="";
	private String ServerAddress="";
	private int ServerPort=80;
	
	private final int connecTimeout=5000;
	private final int KeepAliveTimeout=10000;
	
	byte SendID=0;
	
    public Comm_RemoteService()
    {
    }	
	
    @Override    
    public synchronized boolean Start()  //启动连接
    {   	
    	if(super.Start()==false)
    		return false;
    	//if(!isNetworkConnected(this))
    	
    	if(ServerAddress.length()==0&&ServerDomainName.length()==0)
    		return false;
    	
    	Optometry_gateway.Log("Start Connect Remote Server"); 	
    	
    	this.Stop();
    	
   		TcpConnectedThread = new TcpClientThread();
   		TcpConnectedThread.start();    
   		    	
    	return true;	
    }
    
    @Override    
    public synchronized boolean Stop()   //关闭连接
    {  
    	
    	if(TcpConnectedThread!=null)
    		TcpConnectedThread.Disconnect();
    	
    	TcpConnectedThread=null;
    	SetConnectStatus(STATE_NONE);
    	return true;
    }      
    
    /*
     * 设置服务器地址信息
     * 
     */
    public void SetServerAdd(String domainname,String address,int port)
    {
    	
    	ServerDomainName=domainname;
    	ServerAddress=address;
    	ServerPort=port;
    }
    
    
    @Override
    public synchronized void SendData(byte[] Comm_Data) {
    	if(Connect_status!=STATE_CONNECTED)
    		return;
    	
    	if(TcpConnectedThread!=null)
    		TcpConnectedThread.SendData(Comm_Data);  	
    }    
    
    /*
     * TCP 连接线程
     *    
     */
    class TcpClientThread extends Thread {
    	private Socket Tcp_Client = null; 
    	int receiver_counter=0;
 
    	OutputStream dout;  
    	InputStream din;  	
    	    	
       public TcpClientThread() {
        	SetConnectStatus(STATE_CONNECTING);
        }
          
        public void Disconnect()
        {
        	SetConnectStatus(STATE_NONE);  
        	
        	if(Tcp_Client==null)
        		return;
        	
        	try {
        		Tcp_Client.shutdownInput();
        		Tcp_Client.shutdownOutput();
        		dout.close();
        		din.close();        		
				Tcp_Client.close();
			} catch (IOException e) {
				Optometry_gateway.Log("Remote Server Disconnect Fail");
			}
        	
        	Tcp_Client=null;
        }

        public void run() 
        {
        	
  		  try {
  			  
  			  Optometry_gateway.Log("Remote Server Build Socket");
  			  Tcp_Client = new Socket();
  			  SocketAddress socketAddress=null;
  			  
  			  /*
  			   * 如果设置为域名，进行域名解析后进行连接；如果不是域名直接通过IP进行连接
  			   */
  			  if(ServerDomainName.length()>0)
  			  {
  				  InetAddress netAddress = InetAddress.getByName(ServerDomainName);
  				  socketAddress=new InetSocketAddress(netAddress, ServerPort); 
  			  }
  			  else
  				  socketAddress = new InetSocketAddress(ServerAddress, ServerPort); 
  			  
  			  
  			Tcp_Client.connect(socketAddress, connecTimeout);		
  			Tcp_Client.setTcpNoDelay(true);
  			  		
  			dout = Tcp_Client.getOutputStream();  
  			din = Tcp_Client.getInputStream();   

  			Tcp_Client.setSoTimeout(KeepAliveTimeout);
  			SetConnectStatus(STATE_CONNECTED);	
  			Manager.Comm_IF_Manager(Comm_Manager.NETWORK_CONNECT_SUCCES);		
  		  	}
  		  
  		  catch (UnknownHostException e) {  
  			 Optometry_gateway.Log("Remote Server DomainName Fail");
  			 Connect_Close(); 
  			  return;
           	 } catch (IOException e) 
           	 {  
           		 Optometry_gateway.Log("Remote Server Connect Fail");
           		 Connect_Close();
           		 e.printStackTrace();  
           		 return;
           	}       	
        	      	     	
            receiver_counter=0;

        	while(true)
        	{
       			if(Tcp_Client==null)
       				break;
       				
        		if(recvData()==false)
        		{
        			Optometry_gateway.Log("Remote Server Receiver Fail");
        			Connect_Close();        				
        			break;
        		}
        	}	
        }
   
        //发送数据
        public boolean SendData(byte[] data)
        {
        	//DatagramPacket sPacket;
        	
        	if(Tcp_Client==null)
        		return false;
        	
        	if(data.length<4)
        		return false;
        	
        	data[1]=SendID;   
        	SendID++;
        	
        	data[2]=(byte)(data.length&0xff);      
        	data[3]=(byte)(data.length>>8);           	
        	
        	try {
        		dout.write(data,0, data.length);       		
    		
        	} catch (IOException ie) {
        		Optometry_gateway.Log("Remote Server Send Fail");
        		Manager.Comm_IF_Manager(Comm_Manager.NETWORK_CONNECT_CLOSE);
        		Connect_Close();
        		return false;
        	}
        	//sPacket=null;	
        	return true;
        }
        
        //处理接收的数据
        private boolean DealReceiverData(byte[] buff,int len)
        {
        	int index=0;
        	int pack_len=0;
        	boolean error_flag=false;
        	while(index<len)
        	{
        		pack_len=buff[index+2]+buff[index+3]*0xff;
        		if(len-index<5||pack_len>(len-index))
        		{
        			Optometry_gateway.Log("RemoteServer Receiver Length error");
        			error_flag=true;
        			break;
        		}
        		        		
        		switch(buff[index])
        		{
        		case (byte)0xaa:   
        			byte[] cmd_buf=new byte[pack_len-4];
           			System.arraycopy(buff,index+4,cmd_buf,0,pack_len-4);
           			Command.Comm_IF_Command(cmd_buf);        			
        			break;
        		case (byte)0x55:
           			byte [] data_buf=new byte[pack_len-4];
        		    System.arraycopy(buff,index+4,data_buf,0,pack_len-4);
           			if(Receiver!=null)
           				Receiver.Comm_IF_DataReceiver(data_buf);        			
        			break;
        		case (byte)0x5a:
        			break;
        		default:
        			error_flag=true;
        			break;
        		}
        		
        		if(error_flag)
        		{
        			Optometry_gateway.Log("RemoteServer packet flag error");
        			break;
        		}
        		
        		index+=pack_len;       		
        	} 
        	
        	if(error_flag)
        		return false;
        	else
        		return true;
        }
       
        
        //接收数据
        private boolean recvData()
        {
        	int len;
        	byte Rebuffer[]=new byte[1024];  

        	try
        	{
        		if(din==null)
        			return true;
        		
        		len=din.read(Rebuffer);      		
        		if(len==-1)
        		{
        			Optometry_gateway.Log("Remote Server recvData Fail");
        			return true;
        		}
        		
           		receiver_counter++;
           		
           		if(DealReceiverData(Rebuffer,len)==false)
           			return false;
           		
           		/*
           		if(Rebuffer[0]==(byte)0xaa)
           		{
           			System.arraycopy(Rebuffer,1,Rebuffer,0,Rebuffer.length-1);
           			Command.Comm_IF_Command(Rebuffer);
           		}
           		
           		if(Rebuffer[0]==(byte)0x55)
           		{
           			byte [] BufferData=new byte[len-1];
           			System.arraycopy(Rebuffer,1,BufferData,0,len-1);
           			if(Receiver!=null)
           				Receiver.Comm_IF_DataReceiver(BufferData);
           		}       
           		*/  		
           		           		

        	} catch (IOException ie)
        	{
        		Optometry_gateway.Log("Remote Server KeepAlive Fail");
        		Manager.Comm_IF_Manager(Comm_Manager.NETWORK_CONNECT_CLOSE);	
        		Connect_Close();
          		return false;
        	} 	
        	return true;
        } 
    }


    //发送心跳包
    public void SendKeepAlive()
    {
    	if(Connect_status==STATE_CONNECTED)
    	{
    		byte[] keepalive={(byte)0x5a,(byte)0,(byte)0,(byte)0,(byte)0xa5};
    		SendData(keepalive);
    	}
    }

    //数据发送接口
	@Override
	public void Comm_IF_DataSend(byte[] mData) {
		SendData(mData);
		
	}        
    
}