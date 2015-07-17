package com.rs.optometry_gateway;

/*
 * 本地通信接口，用于实现本地验光控制和蓝牙模块的通信
 * 
 */

public class Comm_Local extends Comm_Base implements Comm_IF_DataSend{

	Optometry_contrl local_ctrl=null;
	
	int SendCounter=-1;
	
	byte[] RBuff=new byte[20];
	int RBuff_Len=0;
	
	@Override
	public void Comm_IF_DataSend(byte[] mData) {
		Optometry_gateway.LogBytes("Local Recive:", mData);
		
		if(mData.length<5)
			return ;
		
		if(mData[0]!=0x55)
			return;
		
		
		
		
		for(int i=4;i<mData.length;i++)
		{
			RBuff[RBuff_Len]=mData[i];
			RBuff_Len++;
		}
		
		
		if(RBuff_Len>=2)
		{
			Optometry_packet.Queue_Receive(RBuff);
			RBuff_Len=0;
		}
		
		return;
	}
		
	
	public void SendOptometry(byte[] mData)
	{
		if(Receiver!=null)
   		  Receiver.Comm_IF_DataReceiver(mData);
		
		Optometry_gateway.LogBytes("SendData:", mData);
	}
	

	
	public void CheckSendPk()
	{
		Optometry_packet.Queue_CheckSend();

	}

}
