package com.rs.optometry_gateway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;

/*
 * 
 * 加光按钮对象
 * 
 */

public class Optometry_button_add extends Optometry_button {

	public Optometry_button_add(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		this.active_id=current_status.MODE_ADD;
		
	}
			
	@Override
	public void OnClick(Context context)
	{	
		if(((Optometry_contrl)context).Optometry_status.far_near_status!=1)
			return;
		
		super.OnClick(context);
		mCtrl.Optometry_CheckeyeActive();
		Optometry_packet.SendDataCmd(mCtrl.Optometry_data,mCtrl.Optometry_status,Optometry_packet.CMD_STATUS_ALL);
	}
	
	@Override
	public boolean OnLongClick(Context context)
	{
		super.OnLongClick(context);
		SelectDialog(context);
		return true;
	}
	
	@Override
	public void Update(data_list opt_data,current_status opt_status)
	{
	
		super.Update(opt_data, opt_status);
		
		int sphere=0;	
		
		if(opt_status.far_near_status==1)
			super.setTextColor(Color.parseColor("#000000"));
		else
			super.setTextColor(Color.parseColor("#A0A0A0"));
		
		
		if(buttontype==1)  //数值
		{
			if(opt_status.far_near_status==1)
			{
				if(this.buttoneye==1)
					sphere=opt_data.add_right;
				else
					sphere=opt_data.add_left;
			}

			
			float d1 = sphere;
			float f = 0.01F * d1;
			
			String value = String.format("%+.2f  ", f);
			setText(value);			
		}
		
		if(buttontype==2) //控制     
		{
			setText("ADD");		
			
		}
			
	}	
	
	
	private void SelectDialog(Context context)
	{
		String [] SelectList={"0.25D","0.5D","1D","3D"};//m_App.gwManager.GetBTDeviceList();
		
		int index=-1;
		
		switch(mCtrl.Optometry_status.s_sphere_step)
		{
		case 25:
			index=0;
			break;
		case 50:
			index=1;
			break;	
		case 100:
			index=2;
			break;				
		case 300:
			index=3;
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
		switch(index)
		{
		case 0:
			mCtrl.Optometry_status.s_sphere_step=25;
			break;
		case 1:
			mCtrl.Optometry_status.s_sphere_step=50;
			break;
		case 2:
			mCtrl.Optometry_status.s_sphere_step=100;
			break;
		case 3:
			mCtrl.Optometry_status.s_sphere_step=300;
			break;			
		}
		
		mCtrl.UpdateCtrlView();
		
	}
	
}	
