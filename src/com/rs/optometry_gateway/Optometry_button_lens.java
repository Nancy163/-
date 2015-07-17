package com.rs.optometry_gateway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;

/*
 * 
 * 镜片选择按钮对象
 * 
 */



public class Optometry_button_lens extends ImageButton {

	public int buttontype=0;
	public int buttonindex=0;
	public int buttoneye=0;
	
	public int active_id=-1;

	public Optometry_contrl mCtrl;
	
	public Optometry_button_lens(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.optometry_gateway); 	
		buttontype=a.getInt(R.styleable.optometry_gateway_buttontype, 0); 
		buttonindex = a.getInt(R.styleable.optometry_gateway_buttonindex, 0); 
		buttoneye=a.getInt(R.styleable.optometry_gateway_buttoneye, 0);
				
		return;	
	}
	
	public void Update(data_list opt_data,current_status opt_status)
	{

		if(buttontype==1)
		{	
			int lensid=0;
			
			if(buttoneye==1)
				lensid=opt_status.lens_right;
			else
				lensid=opt_status.lens_left;			

			switch(lensid)
			{
			case 0:
				setImageDrawable(getResources().getDrawable(R.drawable.lens0));
				break;
			case 1:
				setImageDrawable(getResources().getDrawable(R.drawable.lens1));
				break;
			case 2:
				setImageDrawable(getResources().getDrawable(R.drawable.lens2));
				break;
			case 3:
				setImageDrawable(getResources().getDrawable(R.drawable.lens3));
				break;				
			case 4:
				//setImageDrawable(getResources().getDrawable(R.drawable.lens4));
				if(buttoneye==1)
					setImageDrawable(getResources().getDrawable(R.drawable.lens4r));
				else
					setImageDrawable(getResources().getDrawable(R.drawable.lens4l));
				break;				
			case 5:
				if(buttoneye==1)
					setImageDrawable(getResources().getDrawable(R.drawable.lens5r));
				else
					setImageDrawable(getResources().getDrawable(R.drawable.lens5l));
				break;
			case 6:
				setImageDrawable(getResources().getDrawable(R.drawable.lens6));
				break;
			case 7:
				setImageDrawable(getResources().getDrawable(R.drawable.lens7));
				break;			
			case 8:
				setImageDrawable(getResources().getDrawable(R.drawable.lens8));
				break;
			case 9:
				setImageDrawable(getResources().getDrawable(R.drawable.lens9));
				break;
			case 10:
				setImageDrawable(getResources().getDrawable(R.drawable.lens10));
				break;	
			case 11:
				setImageDrawable(getResources().getDrawable(R.drawable.lens11));
				break;			
			case 12:
				if(opt_status.cc_side_l==1||opt_status.cc_side_r==1)
					setImageDrawable(getResources().getDrawable(R.drawable.lens12_2));
				else
					setImageDrawable(getResources().getDrawable(R.drawable.lens12_1));
				break;
			case 13:
				if(buttoneye==1)
					setImageDrawable(getResources().getDrawable(R.drawable.lens13r));
				else
					setImageDrawable(getResources().getDrawable(R.drawable.lens13l));
				break;
			case 14:
				setImageDrawable(getResources().getDrawable(R.drawable.lens14));
				break;					
				
			}
				
		}
			

	}
	
	public void OnClick(Context context)
	{	
		
		mCtrl=(Optometry_contrl)context;
		
		SelectDialog(context);			
		
	}
	
	
	private void SelectDialog(Context context)
	{
		String [] SelectList={"无特殊镜片","遮挡","45°偏振","135°偏振","水平马氏杆","红色镜片","圆偏振镜片","交叉棱镜","+1.5D","+2.0D","针孔","近用交叉柱镜","交叉柱镜","垂直棱镜"};//m_App.gwManager.GetBTDeviceList();
		int lensid=0;
		
		if(buttoneye==1)
		{
			lensid=mCtrl.Optometry_status.lens_right;
		}
		else
		{
			lensid=mCtrl.Optometry_status.lens_left;
			SelectList[4]="垂直马氏杆";
			SelectList[5]="绿色镜片";
			SelectList[13]="水平棱镜";
		}

			
		new AlertDialog.Builder(context).setTitle("选择特殊镜片").setSingleChoiceItems(SelectList, lensid,	
				new DialogInterface.OnClickListener() {           
	     public void onClick(DialogInterface dialog, int which) {
	    	 {
	    		 SelectChange(which);
	    		 dialog.dismiss();
	    	 }
	     }
	  }
	)
	.setNegativeButton("取消", null)
	.show();
	}
	
	private void SelectChange(int index)
	{
		/*
		if(buttoneye==1)
			mCtrl.Optometry_status.lens_right=index;
		else
			mCtrl.Optometry_status.lens_left=index;
			*/
		
		switch(index)
		{
		case 0:
			if(buttoneye==1)
			{
				mCtrl.Optometry_status.lens_right=0;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=0;
			}
			else
			{
				mCtrl.Optometry_status.lens_left=0;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=0;
			}
			
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			break;
			
		case 1:
			if(buttoneye==1)
			{
				mCtrl.Optometry_status.lens_right=1;
				mCtrl.Optometry_status.R_eye_enable=false;;
				mCtrl.Optometry_status.r_lens_code=(byte)0x0a;
			}
			else
			{
				mCtrl.Optometry_status.lens_left=1;
				mCtrl.Optometry_status.L_eye_enable=false;
				mCtrl.Optometry_status.l_lens_code=(byte)0x0a;
			}
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			break;	
			
		case 2:
			if(buttoneye==1)
			{
				mCtrl.Optometry_status.lens_right=2;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=(byte)0x01;
				
				mCtrl.Optometry_status.lens_left=3;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=(byte)0x02;				
			}
			else
			{
				mCtrl.Optometry_status.lens_right=3;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=(byte)0x02;
				
				mCtrl.Optometry_status.lens_left=2;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=(byte)0x01;
			}
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			break;	
			
		case 3:
			if(buttoneye==1)
			{
				mCtrl.Optometry_status.lens_right=3;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=(byte)0x02;
				
				mCtrl.Optometry_status.lens_left=2;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=(byte)0x01;	
			}
			else
			{
				mCtrl.Optometry_status.lens_right=2;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=(byte)0x01;
				
				mCtrl.Optometry_status.lens_left=3;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=(byte)0x02;	
			}
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			break;	
			
		case 4:
			if(buttoneye==1)
			{
				mCtrl.Optometry_status.lens_right=4;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=(byte)0x03;
			}
			else
			{
				mCtrl.Optometry_status.lens_left=4;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=(byte)0x03;
			}
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			break;				
			
		case 5:
				mCtrl.Optometry_status.lens_right=5;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=(byte)0x04;
				
				mCtrl.Optometry_status.lens_left=5;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=(byte)0x04;	
				
				Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);

			break;	
			
		case 6:
			mCtrl.Optometry_status.lens_right=6;
			mCtrl.Optometry_status.R_eye_enable=true;;
			mCtrl.Optometry_status.r_lens_code=(byte)0x0C;
			
			mCtrl.Optometry_status.lens_left=6;
			mCtrl.Optometry_status.L_eye_enable=true;
			mCtrl.Optometry_status.l_lens_code=(byte)0x0C;	
			
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			
			break;		
			
		case 7:	
			/* 对棱镜进行更新*/
			/*具体值，再测试*/
			break;
		
		case 8:
			if(buttoneye==1)
			{
				mCtrl.Optometry_status.lens_right=8;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=5;
			}
			else
			{
				mCtrl.Optometry_status.lens_left=8;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=5;
			}
			
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			break;	
			
		case 9:
			if(buttoneye==1)
			{
				mCtrl.Optometry_status.lens_right=9;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=6;
			}
			else
			{
				mCtrl.Optometry_status.lens_left=9;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=6;
			}
			
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			break;	
			
		case 10:
			if(buttoneye==1)
			{
				mCtrl.Optometry_status.lens_right=10;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=7;
			}
			else
			{
				mCtrl.Optometry_status.lens_left=10;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=7;
			}
			
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			break;				

		case 11:
			mCtrl.Optometry_status.lens_right=11;
			mCtrl.Optometry_status.R_eye_enable=true;;
			mCtrl.Optometry_status.r_lens_code=(byte)0x0B;
			
			mCtrl.Optometry_status.lens_left=11;
			mCtrl.Optometry_status.L_eye_enable=true;
			mCtrl.Optometry_status.l_lens_code=(byte)0x0B;	
			
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			break;	
			
			
		case 12:
			if(buttoneye==1)
				mCtrl.Select_CC(true);
			else
				mCtrl.Select_CC(false);			
			mCtrl.Optometry_status.active_mode=mCtrl.Optometry_status.MODE_CYLINDER_AXIS;
			break;	
			
		case 13:
			if(buttoneye==1)
			{
				mCtrl.Optometry_status.lens_right=13;
				mCtrl.Optometry_status.R_eye_enable=true;;
				mCtrl.Optometry_status.r_lens_code=(byte)0x08;
			}
			else
			{
				mCtrl.Optometry_status.lens_left=13;
				mCtrl.Optometry_status.L_eye_enable=true;
				mCtrl.Optometry_status.l_lens_code=(byte)0x08;
			}
			
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			break;				
		
		}
		
		mCtrl.UpdateCtrlView();	
	}	

}



