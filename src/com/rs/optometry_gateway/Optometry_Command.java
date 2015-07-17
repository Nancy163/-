package com.rs.optometry_gateway;

/*
 *  验光仪数据命令基本结构
 */

public class Optometry_Command {
	byte[] command=null;
	boolean issend=false;
	int times=0;
	long send_check=0;
	
	public Optometry_Command()
	{
		
	}
	
	public Optometry_Command(byte[] cmd)
	{
		command=cmd;
		issend=false;
		times=0;
		send_check=0;
	}	
	

}
