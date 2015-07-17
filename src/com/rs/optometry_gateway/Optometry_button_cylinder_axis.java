package com.rs.optometry_gateway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;

/*
 * 
 * 柱镜轴位按钮对象
 * 
 */


public class Optometry_button_cylinder_axis extends Optometry_button {

	public Optometry_button_cylinder_axis(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		this.active_id=current_status.MODE_CYLINDER_AXIS;
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
	
	@Override
	public void Update(data_list opt_data,current_status opt_status)
	{
	
		super.Update(opt_data, opt_status);
		
		int value=0;		
		
		if(buttontype==1)  //数值
		{
			if(opt_status.far_near_status==1)
			{
				if(this.buttoneye==1)
					value=opt_data.axis_right_far;
				else
					value=opt_data.axis_left_far;
			}
			else
			{
				if(this.buttoneye==1)
					value=opt_data.axis_right_near;
				else
					value=opt_data.axis_left_near;
			}
			
			
			String value_str = String.format("%d", value);
			setText(value_str);			
		}
		
		if(buttontype==2) //控制     
		{
			
			String value_str = String.format("A(%d)", opt_status.s_cylinder_axis_step);
			setText(value_str);		
			
		}
		

		
	}	
	
	
	private void SelectDialog(Context context)
	{
		String [] SelectList={"1","5","180","135","90","45"};//m_App.gwManager.GetBTDeviceList();
		
		int index=-1;
		
		switch(mCtrl.Optometry_status.s_cylinder_axis_step)
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
			mCtrl.Optometry_status.s_cylinder_axis_step=1;
			break;
		case 1:
			mCtrl.Optometry_status.s_cylinder_axis_step=5;
			break;
		case 2:
			need_update=true;
			if(mCtrl.Optometry_status.far_near_status==1)
			{
				mCtrl.Optometry_data.axis_left_far=180;
				mCtrl.Optometry_data.axis_right_far=180;
			}
			else
			{
				mCtrl.Optometry_data.axis_left_near=180;
				mCtrl.Optometry_data.axis_right_near=180;
			}				
			break;
		case 3:
			need_update=true;
			if(mCtrl.Optometry_status.far_near_status==1)
			{
				mCtrl.Optometry_data.axis_left_far=135;
				mCtrl.Optometry_data.axis_right_far=135;
			}
			else
			{
				mCtrl.Optometry_data.axis_left_near=135;
				mCtrl.Optometry_data.axis_right_near=135;
			}				
			break;	
		case 4:
			need_update=true;
			if(mCtrl.Optometry_status.far_near_status==1)
			{
				mCtrl.Optometry_data.axis_left_far=90;
				mCtrl.Optometry_data.axis_right_far=90;
			}
			else
			{
				mCtrl.Optometry_data.axis_left_near=90;
				mCtrl.Optometry_data.axis_right_near=90;
			}				
			break;	
		case 5:
			need_update=true;
			if(mCtrl.Optometry_status.far_near_status==1)
			{
				mCtrl.Optometry_data.axis_left_far=45;
				mCtrl.Optometry_data.axis_right_far=45;
			}
			else
			{
				mCtrl.Optometry_data.axis_left_near=45;
				mCtrl.Optometry_data.axis_right_near=45;
			}				
			break;					
		}
		if(need_update)
			Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_CYLINDER_AXIS);
		
		mCtrl.UpdateCtrlView();
		
	}	
		
	
}	
