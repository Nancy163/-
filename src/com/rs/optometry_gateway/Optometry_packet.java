package com.rs.optometry_gateway;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/*
 * 用于构建和管理发送验光仪指令
 */

public class Optometry_packet{
	
	static final int CMD_SHERE=1; /*设置球镜*/
	static final int CMD_STATUS_ALL=2; /*设置全状态*/
	static final int CMD_STATUS_1=3;/* 设置状态1*/
	static final int CMD_CYLINDER=4;/* 设置柱镜*/
	static final int CMD_CYLINDER_AXIS=5; /*设置柱镜轴位*/
	static final int CMD_PRISM=6;   /*设置棱镜，设置时需要区分远用和近用*/
	static final int CMD_ADD=7;   /*设置加光*/
	static final int CMD_NEARFAR=8; /*更新远近模式*/
	static final int CMD_NC=9; /*设置为裸眼*/
	static final int CMD_CV=10; /*设置为验光*/
	static final int CMD_RESET=11; /*系统复位验光*/
	static final int CMD_PD=12; /*系统复位验光*/
	static final int CMD_AXIS=13; /*系统复位验光*/	
	private static List<Optometry_Command> Command_Queue = new ArrayList<Optometry_Command>(); 
	
	static Comm_Local Optometry_Device=null;	
	static boolean LinkStatus=true;
	
	
/*
 * 球镜数据
 */
private static byte[] packet_sphere(data_list opt_data,current_status opt_status)
{
	byte [] sphere=new byte[4];
	
	int right_sphere;
	int left_sphere;
	
	if(opt_status.s_cylinder_mode)
	{
		if(opt_status.far_near_status==1)
		{
			right_sphere=opt_data.sphere_right_far+opt_data.cylinder_right_far;
			left_sphere=opt_data.sphere_left_far+opt_data.cylinder_left_far;
		}
		else
		{
			right_sphere=opt_data.sphere_right_near+opt_data.cylinder_right_near;
			left_sphere=opt_data.sphere_left_near+opt_data.cylinder_left_near;		
		}		
		
	}
	else
	{
		if(opt_status.far_near_status==1)
		{
			right_sphere=opt_data.sphere_right_far;
			left_sphere=opt_data.sphere_left_far;
		}
		else
		{
			right_sphere=opt_data.sphere_right_near;
			left_sphere=opt_data.sphere_left_near;		
		}
	}


	
	byte [] tran_data=Optometry_code.TranInt2Short(right_sphere);
	sphere[0]=tran_data[0];
	sphere[1]=tran_data[1];
	
	tran_data=Optometry_code.TranInt2Short(left_sphere);
	sphere[2]=tran_data[0];
	sphere[3]=tran_data[1];	
	
	return sphere;
}


/*
 * 柱镜数据
 */

private static byte[] packet_cylinder(data_list opt_data,current_status opt_status,boolean isfar)
{
	
	byte [] result=new byte[4];
	byte [] tran_data_right;
	byte [] tran_data_left;
	if(isfar)
	{
		tran_data_right=Optometry_code.TranInt2Short(opt_data.cylinder_right_far);		
		tran_data_left=Optometry_code.TranInt2Short(opt_data.cylinder_left_far);	
	}
	else
	{
		tran_data_right=Optometry_code.TranInt2Short(opt_data.cylinder_right_near);		
		tran_data_left=Optometry_code.TranInt2Short(opt_data.cylinder_left_near);	
	}
	
	tran_data_right[1]=(byte)(tran_data_right[1]|0x80);
	tran_data_left[1]=(byte)(tran_data_left[1]|0x80);	
	
	System.arraycopy(tran_data_right,0,result,0,2);
	System.arraycopy(tran_data_left,0,result,2,2);	
	
	return result;		
}

private static byte get_axis(int maxis)
{
	if(maxis>90)
		return (byte)(maxis-90);
	else
		return (byte)(maxis+90);
}

/*
 * 柱镜轴位
 * 
 */
private static byte[] packet_cylinder_axis(data_list opt_data,current_status opt_status,boolean isfar)
{
	byte [] result=new byte[2];
	
	int axis_right;
	int axis_left;	
	
	if(isfar)
	{
		axis_right=opt_data.axis_right_far;
		axis_left=opt_data.axis_left_far;
	}
	else
	{
		axis_right=opt_data.axis_right_near;
		axis_left=opt_data.axis_left_near;		
	}	
	
	if(opt_status.s_cylinder_mode)
	{
		result[0]=get_axis(axis_right);
		result[1]=get_axis(axis_left);	
	}
	else
	{
		result[0]=(byte)axis_right;
		result[1]=(byte)axis_left;		
	}
	
	
	return result;
}

/*
 * 
 * 棱镜设置
 * 
 */

private static byte[] packet_prism(data_list opt_data,current_status opt_status,boolean isfar)
{
	byte [] result=new byte[10];
	byte [] tempdata;
	if(isfar)
	{
		result[0]=(byte)(opt_data.prism_right_h_far);
		result[1]=(byte)(opt_data.prism_left_h_far);	
		result[2]=(byte)(opt_data.prism_right_v_far);
		result[3]=(byte)(opt_data.prism_left_v_far);
		result[4]=(byte)(opt_data.prism_right_p_far);
		result[5]=(byte)(opt_data.prism_left_p_far);
		
		tempdata=Optometry_code.TranInt2Short(opt_data.prism_right_r_far);	
		System.arraycopy(tempdata,0,result,6,2);
		
		tempdata=Optometry_code.TranInt2Short(opt_data.prism_left_r_far);	
		System.arraycopy(tempdata,0,result,8,2);		
		
	}
	else
	{
		result[0]=(byte)(opt_data.prism_right_h_near);
		result[1]=(byte)(opt_data.prism_left_h_near);	
		result[2]=(byte)(opt_data.prism_right_v_near);
		result[3]=(byte)(opt_data.prism_left_v_near);
		result[4]=(byte)(opt_data.prism_right_p_near);
		result[5]=(byte)(opt_data.prism_left_p_near);
		
		tempdata=Optometry_code.TranInt2Short(opt_data.prism_right_r_near);	
		System.arraycopy(tempdata,0,result,6,2);
		
		tempdata=Optometry_code.TranInt2Short(opt_data.prism_left_r_near);	
		System.arraycopy(tempdata,0,result,8,2);		
	}
	
	return result;	
	
}


/*
 * 加光设置
 * 
 */
private static byte[] packet_add(data_list opt_data,current_status opt_status)
{
	byte [] sphere=new byte[4];
		
	byte [] tran_data=Optometry_code.TranInt2Short(opt_data.add_right);
	sphere[0]=tran_data[0];
	sphere[1]=tran_data[1];
	
	tran_data=Optometry_code.TranInt2Short(opt_data.add_left);
	sphere[2]=tran_data[0];
	sphere[3]=tran_data[1];	
	
	return sphere;
}


/*
 * 状态1数据包
 * 
 */

private static byte packet_eye_status1(data_list paramdata_list, current_status paramcurrent_status)
{
	
	byte eyestatus=0;
	
	//PD模式
	if ((paramcurrent_status.be_in_pd_test) && (paramcurrent_status.light_on))
		eyestatus = (byte)(eyestatus | 0x40);

	//远近用
    if (paramcurrent_status.far_near_status == 2)
    	eyestatus = (byte)(eyestatus | 0x20); 
    
    //交叉柱镜，带圆圈的部分
    if (paramcurrent_status.be_with_cc)
    {
    	eyestatus = (byte)(eyestatus | 0x10); 
    	
    	//对应于交叉模式
      if ((paramcurrent_status.cc_side_l == 1) || (paramcurrent_status.cc_side_r == 1))
    	  eyestatus = (byte)(eyestatus | 0x08); 
     
      if (paramcurrent_status.R_eye_enable)
    	  eyestatus = (byte)(eyestatus | 0x04); 

    }
    
    
    if (!paramcurrent_status.R_eye_enable)
  	  eyestatus = (byte)(eyestatus|0x02); 

    if (!paramcurrent_status.L_eye_enable)
    	eyestatus = (byte)(eyestatus|0x01); 
    
    return eyestatus;
}

/*
 * 全状态数据包
 */
private static byte[] packet_eye_status(data_list paramdata_list, current_status paramcurrent_status)
{
	
	byte[] eyestatus=new byte[4];
	
	eyestatus[0]=packet_eye_status1(paramdata_list,paramcurrent_status);

    eyestatus[1]= paramcurrent_status.r_lens_code;
    eyestatus[2]= paramcurrent_status.l_lens_code;    
    

    if ((paramcurrent_status.far_near_status == 2) || (paramcurrent_status.be_in_add_test))
    {
      //近用照明灯
      if (paramcurrent_status.be_near_lamp_on)
    	  eyestatus[3] = (byte)0x80;
      else
    	  eyestatus[3] = (byte)0x0;
      
      //交叉柱镜 A/C切换
      if ((paramcurrent_status.be_with_cc) && (paramcurrent_status.cc_mode == 1))
    	  eyestatus[3] = (byte)(eyestatus[3] |0x40);

      //add 有效位
      if (paramcurrent_status.be_in_add_test)
    	  eyestatus[3] = (byte)(eyestatus[3] |0x20);

    }
 
    return eyestatus;
}

/*
 * 全验光仪数据包
 */
private static byte[] packet_Optometry(data_list opt_data,current_status opt_status)
{
	byte [] Optometry=new byte[40];
	byte [] Tran_data;
	
	//球镜数据
	Tran_data=packet_sphere(opt_data,opt_status);
	System.arraycopy(Tran_data,0,Optometry,0,4);
	
	//柱镜数据
	Tran_data=packet_cylinder(opt_data,opt_status,true);
	System.arraycopy(Tran_data,0,Optometry,4,4);
		
	Tran_data=packet_cylinder_axis(opt_data,opt_status,true);
	System.arraycopy(Tran_data,0,Optometry,8,2);	
	
	Tran_data=packet_prism(opt_data,opt_status,true);
	System.arraycopy(Tran_data,0,Optometry,10,10);	
	
	Tran_data=packet_add(opt_data,opt_status);	
	System.arraycopy(Tran_data,0,Optometry,20,4);
	
	Tran_data=packet_cylinder(opt_data,opt_status,false);
	System.arraycopy(Tran_data,0,Optometry,24,4);
		
	Tran_data=packet_cylinder_axis(opt_data,opt_status,false);
	System.arraycopy(Tran_data,0,Optometry,28,2);	
	
	Tran_data=packet_prism(opt_data,opt_status,false);
	System.arraycopy(Tran_data,0,Optometry,30,10);	
		
	return Optometry;
}


/*
 * 计算校验位
 */
private static byte Ecc_Stream(byte[] CmdStream)
{
	byte result=CmdStream[1];
	
	for(int i=2;i<CmdStream.length-1;i++)
		result=(byte)(result^CmdStream[i]);
	
	return result;
}

/*
 * 设置球镜命令
 */
private static byte[] Cmd_Shere(data_list opt_data,current_status opt_status)
{
	byte [] CmdStream=null;
	byte [] CmdData;
	
	CmdStream=new byte[9];
	CmdStream[0]=(byte)0xaa;
	CmdStream[1]=(byte)0x4;
	CmdStream[2]=(byte)0x00;
	CmdStream[3]=(byte)0x40;
	CmdData=packet_sphere(opt_data,opt_status);
	System.arraycopy(CmdData, 0, CmdStream, 4, CmdData.length);
	CmdStream[8]=Ecc_Stream(CmdStream);
	return CmdStream;
}

/*
 * 设置柱镜命令
 */

private static byte[] Cmd_Cylinder(data_list opt_data,current_status opt_status)
{
	byte [] CmdStream=null;
	byte [] CmdData;
	CmdStream=new byte[0X2d];
	CmdStream[0]=(byte)0xaa;
	CmdStream[1]=(byte)0x28;
	CmdStream[2]=(byte)0x00;
	CmdStream[3]=(byte)0x40;
	CmdData=packet_Optometry(opt_data,opt_status);
	System.arraycopy(CmdData, 0, CmdStream, 4, CmdData.length);
	CmdStream[0x2c]=Ecc_Stream(CmdStream);
	return CmdStream;
}

/*
 * 设置柱镜轴位
 */
private static byte[] Cmd_Cylinder_Axis(data_list opt_data,current_status opt_status)
{
	byte [] CmdStream=null;
	byte [] CmdData;
	CmdStream=new byte[7];
	CmdStream[0]=(byte)0xaa;
	CmdStream[1]=(byte)0x2;
	CmdStream[2]=(byte)0x00;
	if(opt_status.far_near_status==1)
	{
		CmdStream[3]=(byte)0x48;
		CmdData=packet_cylinder_axis(opt_data,opt_status,true);
	}
	else
	{
		CmdStream[3]=(byte)0x5c;
		CmdData=packet_cylinder_axis(opt_data,opt_status,false);
	}		
	System.arraycopy(CmdData, 0, CmdStream, 4, CmdData.length);
	CmdStream[6]=Ecc_Stream(CmdStream);	
	return CmdStream;
}

/*
 * 设置棱镜
 */
private static byte[] Cmd_Prism(data_list opt_data,current_status opt_status)
{
	byte [] CmdStream=null;
	byte [] CmdData;
	CmdStream=new byte[0xf];
	CmdStream[0]=(byte)0xaa;
	CmdStream[1]=(byte)0xa;
	CmdStream[2]=(byte)0x00;
	if(opt_status.far_near_status==1)
	{
		CmdStream[3]=(byte)0x4a;
		CmdData=packet_prism(opt_data,opt_status,true);
	}
	else
	{
		CmdStream[3]=(byte)0x5e;
		CmdData=packet_prism(opt_data,opt_status,false);
	}		
	System.arraycopy(CmdData, 0, CmdStream, 4, CmdData.length);
	CmdStream[0xe]=Ecc_Stream(CmdStream);	
	return CmdStream;
}

/*
 * 设置加光
 */
private static byte[] Cmd_Add(data_list opt_data,current_status opt_status)
{
	byte [] CmdStream=null;
	byte [] CmdData;
	CmdStream=new byte[9];
	CmdStream[0]=(byte)0xaa;
	CmdStream[1]=(byte)0x4;
	CmdStream[2]=(byte)0x00;
	CmdStream[3]=(byte)0x54;
	CmdData=packet_add(opt_data,opt_status);
	System.arraycopy(CmdData, 0, CmdStream, 4, CmdData.length);
	CmdStream[8]=Ecc_Stream(CmdStream);	
	
	return CmdStream;
}

/*
 * 设置状态
 */
private static byte[] Cmd_Status(data_list opt_data,current_status opt_status)
{
	byte [] CmdStream=null;
	byte [] CmdData;
	CmdStream=new byte[9];
	CmdStream[0]=(byte)0xaa;
	CmdStream[1]=(byte)0x4;
	CmdStream[2]=(byte)0x00;
	CmdStream[3]=(byte)0x28;
	CmdData=packet_eye_status(opt_data,opt_status);
	System.arraycopy(CmdData, 0, CmdStream, 4, CmdData.length);
	CmdStream[8]=Ecc_Stream(CmdStream);	
	
	return CmdStream;
}

/*
 * 设置瞳距
 */
private static byte[] Cmd_Pd(data_list opt_data,current_status opt_status)
{
	byte [] CmdStream=null;
	byte [] CmdData;
	CmdStream=new byte[9];
	CmdStream[0]=(byte)0xaa;
	CmdStream[1]=(byte)0x4;
	CmdStream[2]=(byte)0x00;
	CmdStream[3]=(byte)0x24;
		
    CmdStream[4]=(byte)(opt_data.pd_right_far / 5 & 0xFF);
    CmdStream[5]=(byte)(opt_data.pd_left_far / 5 & 0xFF);
    CmdStream[6]=(byte)(opt_data.pd_right_near / 5 & 0xFF);
    CmdStream[7]=(byte)(opt_data.pd_left_near / 5 & 0xFF);   
	CmdStream[8]=Ecc_Stream(CmdStream);	
	
	return CmdStream;
}


/*
 *裸眼模式
 */
private static byte[] packet_Optometry_NC(data_list opt_data,current_status opt_status)
{
	byte [] Optometry={	
	(byte)0xaa,(byte)0x28,(byte)0x00,(byte)0x40,
	(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x80,
	(byte)0xb4,(byte)0xb4,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
	(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
	(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x80,(byte)0xb4,(byte)0xb4,(byte)0x00,(byte)0x00,
	(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
	(byte)0x68};
		
	return Optometry;
}

/*
 * 设置复位
 */
private static byte[] packet_Optometry_Reset(data_list opt_data,current_status opt_status)
{
	byte [] Optometry={(byte)0xaa,(byte)0x01,(byte)0x00,(byte)0x28,(byte)0x80,(byte)0xa9};
	return Optometry;
}



/* 
 * 发送命令
 */
public static boolean SendDataCmd(data_list opt_data,current_status opt_status,int cmd)
{
	
	if(Queue_GetLen()>0&&cmd!=CMD_RESET)
		return false;
		
	byte [] CmdStream=null;
	//Command_Queue.clear();
	switch(cmd)
	{
	case CMD_SHERE:
		CmdStream=Cmd_Shere(opt_data,opt_status);
		Queue_Add(CmdStream);
		break;
	case CMD_CYLINDER:
		CmdStream=Cmd_Cylinder(opt_data,opt_status);
		Queue_Add(CmdStream);
		break;	
	case CMD_CYLINDER_AXIS:
		CmdStream=Cmd_Cylinder_Axis(opt_data,opt_status);
		Queue_Add(CmdStream);
		break;
	case CMD_PRISM:
		CmdStream=Cmd_Prism(opt_data,opt_status);
		Queue_Add(CmdStream);
		break;
	case CMD_ADD:
		CmdStream=Cmd_Add(opt_data,opt_status);
		Queue_Add(CmdStream);
		break;	
	case CMD_STATUS_ALL:
		CmdStream=Cmd_Status(opt_data,opt_status);
		Queue_Add(CmdStream);
		break;
	case CMD_NEARFAR:
		CmdStream=Cmd_Cylinder(opt_data,opt_status);
		Queue_Add(CmdStream);
		CmdStream=Cmd_Status(opt_data,opt_status);
		Queue_Add(CmdStream);
	    CmdStream=Cmd_Pd(opt_data,opt_status);
	    Queue_Add(CmdStream);		
		break;
	case CMD_PD:
	    CmdStream=Cmd_Pd(opt_data,opt_status);
	    Queue_Add(CmdStream);
		break;	
	case CMD_NC:
		 CmdStream=packet_Optometry_NC(opt_data,opt_status);
		 Queue_Add(CmdStream);
		 CmdStream=Cmd_Pd(opt_data,opt_status);
		 Queue_Add(CmdStream);		 
		break;
	case CMD_CV:
		CmdStream=Cmd_Cylinder(opt_data,opt_status);
		Queue_Add(CmdStream);
		 CmdStream=Cmd_Pd(opt_data,opt_status);
		 Queue_Add(CmdStream);
		 break;
	case CMD_RESET:
		Command_Queue.clear();
		CmdStream=packet_Optometry_Reset(opt_data,opt_status);
		Queue_Add(CmdStream);
		 break;				 	 
	}
	
	
	if(Optometry_Device!=null)
	{
		Queue_Send();
	}

	
	return true;
	}




/*
 *  数据包队列操作
 * 
 * 
 */


static void Queue_Add(byte[] data)
{
	synchronized (Command_Queue)
	{
		Command_Queue.add(new Optometry_Command(data));
	}
}

static byte[] Queue_GetData(int index)
{
	synchronized (Command_Queue)
	{
		byte [] result=null;
		if(index<Command_Queue.size())
		{
			result=Command_Queue.get(index).command.clone();
		}

		return result;
	}
}

static boolean Queue_Send()
{
	synchronized (Command_Queue)
	{
		if(Command_Queue.size()>0)
		{
			if(Command_Queue.get(0).times>=3)
				return false;
			byte [] command;
			command=Command_Queue.get(0).command;
			Optometry_Device.SendOptometry(command);
			Command_Queue.get(0).issend=true;
			Command_Queue.get(0).times++;
			Command_Queue.get(0).send_check=0;		
			return true;
		}
		
		return false;

	}
}


static public void Queue_Receive(byte rdata[])
{
	synchronized (Command_Queue)
	{
		if(Command_Queue.size()<1)
			return;
		
		if(rdata[0]==0xf&&rdata[1]==0xf)
		{
			if(Command_Queue.get(0).issend)
			{
				Command_Queue.remove(0);
			}
		}

	}	
	
	Queue_Send();
}

static void Queue_CheckSend()
{
	synchronized (Command_Queue)
	{
		if(Optometry_packet.Command_Queue.size()<1)
			return;
		
		if(!Optometry_packet.Command_Queue.get(0).issend)
			return;
		
		if(Optometry_packet.Command_Queue.get(0).send_check<5)
			Optometry_packet.Command_Queue.get(0).send_check++;
		else
		{
			//Optometry_packet.ReSendPack();
			if(Command_Queue.get(0).times<3)
			{
				//Optometry_Device.Send_PacketList();
				byte [] command;
				command=Command_Queue.get(0).command;
				Optometry_Device.SendOptometry(command);
				Command_Queue.get(0).issend=true;
				Command_Queue.get(0).times++;
				Command_Queue.get(0).send_check=0;					
			}
			else
			{
				Command_Queue.clear();
				LinkStatus=false;
			}				
		}
	  }		

}

static int Queue_GetLen()
{
	synchronized (Command_Queue)
	{
		return Command_Queue.size();
	}
}





}

