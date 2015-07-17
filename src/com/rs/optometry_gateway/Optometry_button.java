package com.rs.optometry_gateway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

/*
 * 
 * UI按钮基本对象
 * 
 */


public class Optometry_button extends Button {

	public int buttontype=0;
	public int buttonindex=0;
	public int buttoneye=0;
	
	public int active_id=-1;

	public Optometry_contrl mCtrl;
	
	public Optometry_button(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.optometry_gateway); 	
		buttontype=a.getInt(R.styleable.optometry_gateway_buttontype, 0); 
		buttonindex = a.getInt(R.styleable.optometry_gateway_buttonindex, 0); 
		buttoneye=a.getInt(R.styleable.optometry_gateway_buttoneye, 0);
		
		
		
		switch(buttontype)
		{
		case 1:  //数值
			if(buttoneye==1) //左眼
				setBackgroundResource(R.drawable.control_button_left);
			if(buttoneye==2) //右眼
				setBackgroundResource(R.drawable.control_button_right);
			break;
		case 2:  //控制
			setBackgroundColor(Color.parseColor("#d6e2f9"));
			break;
		case 3:
			break;
		}
		
		
		return;	
	}
	
	public void Update(data_list opt_data,current_status opt_status)
	{

		if(buttontype==1)
		{
			boolean eyebeactive=false;
			
			if(buttoneye==1&&opt_status.right_active)
				eyebeactive=true;
			
			if(buttoneye==2&&opt_status.left_active)
				eyebeactive=true;			
			
			
			if(active_id==opt_status.active_mode&&eyebeactive)
			{
			   if(buttoneye==1)
				  setBackgroundResource(R.drawable.control_button_left_active);
			   if(buttoneye==2)
				  setBackgroundResource(R.drawable.control_button_right_active);	
			}
			else
			{
			   if(buttoneye==1)
				  setBackgroundResource(R.drawable.control_button_left);
			   if(buttoneye==2)
				  setBackgroundResource(R.drawable.control_button_right);				
			}
			
				
		}
			

	}
	
	public void OnClick(Context context)
	{	
		boolean cc_mode=false;
		
		mCtrl=(Optometry_contrl)context;
		mCtrl.Optometry_status.active_mode=active_id;		
		
		if((mCtrl.Optometry_status.active_mode==current_status.MODE_CYLINDER
				||mCtrl.Optometry_status.active_mode==current_status.MODE_CYLINDER_AXIS)&&mCtrl.Optometry_status.be_with_cc)
		{
			cc_mode=true;
		}
			
		if(buttontype==1) //如果点击单个按钮
		{
			if(buttoneye==1)
			{
				mCtrl.Optometry_status.right_active=true;
				mCtrl.Optometry_status.R_eye_enable=true;
				mCtrl.Optometry_status.left_active=false;
				mCtrl.Optometry_status.L_eye_enable=false;
				
				if(cc_mode)
					mCtrl.Select_CC(true);
				else
					mCtrl.CheckLens(1);
				
				
			}
			if(buttoneye==2)
			{
				mCtrl.Optometry_status.right_active=false;
				mCtrl.Optometry_status.R_eye_enable=false;
				mCtrl.Optometry_status.left_active=true;
				mCtrl.Optometry_status.L_eye_enable=true;
				if(cc_mode)
					mCtrl.Select_CC(false);		
				else
					mCtrl.CheckLens(2);				
			}				
		}
			
		if(buttontype==2&&!cc_mode) //如果点击控制按钮		
		{
			mCtrl.Optometry_status.right_active=true;
			mCtrl.Optometry_status.R_eye_enable=true;
			mCtrl.Optometry_status.left_active=true;
			mCtrl.Optometry_status.L_eye_enable=true;
			mCtrl.CheckLens(3);	
		}	
		
		if(!cc_mode)
			mCtrl.Clear_CC();
		
		if(mCtrl.Optometry_status.active_mode!=current_status.MODE_PD)
			mCtrl.Clear_PD();	
		
		if(mCtrl.Optometry_status.active_mode==current_status.MODE_ADD)
			mCtrl.Optometry_status.be_in_add_test=true;
		else
			mCtrl.Optometry_status.be_in_add_test=false;
	}
	
	
	public boolean OnLongClick(Context context)
	{
		mCtrl=(Optometry_contrl)context;		
		return false;
	}




}



