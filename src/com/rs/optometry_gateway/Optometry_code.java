package com.rs.optometry_gateway;

/*
 * 
 * 数据编码转换
 * 
 */

/*
 * 
 *  INT 转 BYTE[2]
 */
public class Optometry_code {
	public static byte[] TranInt2Short(int mData)
	{
		//unsigned short tran_data=mData>=0?mData&0x7fff:((mData*-1)&0x7fff)|0x8000;
		int TranData=Math.abs(mData);
		byte[] result=new byte[2];
		result[0]=(byte)(TranData&0xff);
		result[1]=(byte)((TranData>>8)&0x7f);
		
		if(mData<0)
			result[1]=(byte)(result[1]|0x80);

		return result;
	}
	
	
	/*
	 * 
	 *  INT 转 BYTE[1]
	 */
	
	public static byte TranInt2Char(int mData)
	{
		
		//result=mData>=0?mData&0x7f:((mData*-1)&0x7f)|0x80;
		byte result;
		
		result=(byte)Math.abs(mData);
		result=(byte)(result&0x7f);
		
		if(mData<0)
			result=(byte)(result|0x80);		
		
		return result;
	}


}
