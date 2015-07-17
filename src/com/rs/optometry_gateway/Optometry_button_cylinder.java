package com.rs.optometry_gateway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;


/*
 * 
 * 柱镜按钮对象
 * 
 */


public class Optometry_button_cylinder extends Optometry_button {

	public Optometry_button_cylinder(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		this.active_id=current_status.MODE_CYLINDER;
		
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
					sphere=opt_data.cylinder_right_far;//sphere_right_far;
				else
					sphere=opt_data.cylinder_left_far;//sphere_left_far;
			}
			else
			{
				if(this.buttoneye==1)
					sphere=opt_data.cylinder_right_near;
				else
					sphere=opt_data.cylinder_right_near;
			}
			
			float d1 = sphere;
			float f = 0.01F * d1;
			
			String value = String.format("%+.2f  ", f);
			setText(value);			
		}
		
		if(buttontype==2) //控制     
		{
			String mode;
			if(opt_status.s_cylinder_mode)
				mode="+";
			else
				mode="-";
			
			String value = String.format("C(%s)",mode);
			setText(value);		
			
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
		String [] SelectList={"+","-"};//m_App.gwManager.GetBTDeviceList();
		
		int index=-1;
		
		if(mCtrl.Optometry_status.s_cylinder_mode)
			index=0;
		else
			index=1;
					
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
		boolean m_mode=true;
		switch(index)
		{
		case 0:
			m_mode=true;
			break;
		case 1:
			m_mode=false;
			break;		
		}
		
		if(mCtrl.Optometry_status.s_cylinder_mode!=m_mode)
		{
			mCtrl.Optometry_cylinder_Not();
		}
		
		mCtrl.Optometry_status.s_cylinder_mode=m_mode;
		
		mCtrl.UpdateCtrlView();
		
	}	
	
}	
