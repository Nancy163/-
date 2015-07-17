package com.rs.optometry_gateway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;


/*
 * 
 * 瞳距按钮对象
 * 
 */


public class Optometry_button_pd extends Optometry_button {

	public Optometry_button_pd(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		this.active_id=current_status.MODE_PD;
	}
	
		
	@Override
	public void OnClick(Context context)
	{		
		super.OnClick(context);		
		mCtrl.Optometry_status.set_mode_pd(true);
		mCtrl.Optometry_status.R_eye_enable=true;
		mCtrl.Optometry_status.L_eye_enable=true;
		mCtrl.Optometry_status.r_lens_code=(byte)0x09;
		mCtrl.Optometry_status.l_lens_code=(byte)0x09;
		mCtrl.Optometry_status.lens_right=14;
		mCtrl.Optometry_status.lens_left=14;
		Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
	}
	
	@Override
	public boolean OnLongClick(Context context)
	{
		super.OnLongClick(context);
		if(buttontype==2)
		{
			if(mCtrl.Optometry_status.be_in_pd_test)
			{
				mCtrl.Optometry_status.light_on=!mCtrl.Optometry_status.light_on;
				Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
				return true;
			}
		}
		return false;
	}	

	
	@Override
	public void Update(data_list opt_data,current_status opt_status)
	{	
		super.Update(opt_data, opt_status);
		
		int sphere;		
		
		if(buttontype==1)  //数值
		{
			if(opt_status.far_near_status==1)
			{
				if(this.buttoneye==1)
					sphere=opt_data.pd_right_far;//.sphere_right_far;
				else
					sphere=opt_data.pd_left_far;
			}
			else
			{
				if(this.buttoneye==1)
					sphere=opt_data.pd_right_near;
				else
					sphere=opt_data.pd_left_near;
			}
			
			float d1 = sphere;
			float f = 0.1F * d1;
			
			String value = String.format("%+.1f  ", f);
			setText(value);			
		}
		
	}	
	
	

	
}	
