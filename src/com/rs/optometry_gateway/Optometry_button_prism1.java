package com.rs.optometry_gateway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;


/*
 * 
 * 棱镜按钮1对象
 * 
 */


public class Optometry_button_prism1 extends Optometry_button {

	public Optometry_button_prism1(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		this.active_id=current_status.MODE_PRISM1;
		
	}
	
	@Override
	public void Update(data_list opt_data,current_status opt_status)
	{
	
		super.Update(opt_data, opt_status);
		
		int value;		
		
		if(buttontype==1)  //数值
		{
			if(opt_status.far_near_status==1)
			{
				if(this.buttoneye==1)
					if(opt_status.s_prism_mode)
						value=opt_data.prism_right_h_far;
					else
						value=opt_data.prism_right_p_far;
						
				else
					if(opt_status.s_prism_mode)
						value=opt_data.prism_left_h_far;
					else
						value=opt_data.prism_left_p_far;
			}
			else
			{
				if(this.buttoneye==1)
					if(opt_status.s_prism_mode)
						value=opt_data.prism_right_h_near;
					else
						value=opt_data.prism_right_p_near;
						
				else
					if(opt_status.s_prism_mode)
						value=opt_data.prism_left_h_near;
					else
						value=opt_data.prism_left_p_near;
			}
			
			float d1 = value;
			float f = 0.1F * d1;
			
			String value_str;
			if(opt_status.s_prism_mode)
			{
				int flag_value;
				
				if(this.buttoneye==1)
					flag_value=value*-1;
				else
					flag_value=value;
				
			  if(flag_value>0)
				  value_str = String.format("BO%.1f  ", Math.abs(f));
			  else
				  value_str = String.format("BI%.1f  ", Math.abs(f));
			}
			else
				value_str = String.format("%.1f  ", f);			
			
			setText(value_str);				
		}
		
		if(buttontype==2) //控制     
		{
			float d1 = opt_status.s_prism_step;
			float f = 0.1F * d1;
			String value_str;
			if(opt_status.s_prism_mode)
			  value_str = String.format("H(%.1f)", f);
			else
			  value_str= String.format("P(%.1f)", f);
			setText(value_str);		
			
		}
		

		
	}	
		
	@Override
	public void OnClick(Context context)
	{		
		super.OnClick(context);
		mCtrl.Optometry_CheckeyeActive();
		Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
	}
	@Override
	public boolean OnLongClick(Context context)
	{
		super.OnLongClick(context);
		SelectDialog(context);
		return false;
	}
	
	private void SelectDialog(Context context)
	{
		String [] SelectList=null;
		
		
		if(mCtrl.Optometry_status.s_prism_mode)
			SelectList=new String[]{"0.1","0.5","P-R","0","D3U3"};
		else
			SelectList=new String[]{"0.1","0.5","X-Y","0"};
				
		int index=-1;
		
		switch(mCtrl.Optometry_status.s_prism_step)
		{
		case 1:
			index=0;
			break;
		case 5:
			index=1;
			break;				
		}
			
		new AlertDialog.Builder(context).setTitle("选择").setSingleChoiceItems(SelectList, index,	
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
		boolean need_update=false;
		switch(index)
		{
		case 0:
			mCtrl.Optometry_status.s_prism_step=1;
			break;
		case 1:
			mCtrl.Optometry_status.s_prism_step=5;
			break;
		case 2:
			mCtrl.Optometry_status.s_prism_mode=!mCtrl.Optometry_status.s_prism_mode;	
			break;
		case 3:
			need_update=true;
			if(mCtrl.Optometry_status.s_prism_mode)
			{
				if(mCtrl.Optometry_status.far_near_status==1)
				{
					mCtrl.Optometry_data.prism_right_h_far=0;
					mCtrl.Optometry_data.prism_left_h_far=0;					
				}
				else
				{
					mCtrl.Optometry_data.prism_right_h_near=0;
					mCtrl.Optometry_data.prism_left_h_near=0;
				}
			}
			else
			{
				if(mCtrl.Optometry_status.far_near_status==1)
				{
					mCtrl.Optometry_data.prism_right_p_far=0;
					mCtrl.Optometry_data.prism_left_p_far=0;				
				}
				else
				{
					mCtrl.Optometry_data.prism_right_p_near=0;
					mCtrl.Optometry_data.prism_left_p_near=0;	
				}
			}	
			mCtrl.UpdatePrism();
			break;	
		case 4:
			need_update=true;
			if(mCtrl.Optometry_status.s_prism_mode)
			{
				if(mCtrl.Optometry_status.far_near_status==1)
				{
					mCtrl.Optometry_data.prism_right_h_far=0;
					mCtrl.Optometry_data.prism_left_h_far=0;		
					mCtrl.Optometry_data.prism_right_v_far=-30;
					mCtrl.Optometry_data.prism_left_v_far=30;						
				}
				else
				{
					mCtrl.Optometry_data.prism_right_h_near=0;
					mCtrl.Optometry_data.prism_left_h_near=0;
					mCtrl.Optometry_data.prism_right_v_near=-30;
					mCtrl.Optometry_data.prism_left_v_near=30;					
				}
				mCtrl.UpdatePrism();
			}	
			
			break;				
			
		}
		
		if(need_update)
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_PRISM);
		
		mCtrl.UpdateCtrlView();
		
	}		
	
}	
