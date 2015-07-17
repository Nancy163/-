package com.rs.optometry_gateway;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


/*
 * ����� UI���ƺ���ֵֵ����
 * 
 */
public class Optometry_contrl extends Activity implements OnClickListener,OnLongClickListener{

	Optometry_button_diopter control_button_diopter_right;
	Optometry_button_diopter control_button_diopter_ctrl;
	Optometry_button_diopter control_button_diopter_left;
	
	Optometry_button_cylinder control_button_cylinder_right;
	Optometry_button_cylinder control_button_cylinder_ctrl;
	Optometry_button_cylinder control_button_cylinder_left;
	
	Optometry_button_cylinder_axis control_button_cylinder_axis_right;
	Optometry_button_cylinder_axis control_button_cylinder_axis_ctrl;
	Optometry_button_cylinder_axis control_button_cylinder_axis_left;
	
	Optometry_button_prism1 control_button_prism1_right;
	Optometry_button_prism1 control_button_prism1_ctrl;
	Optometry_button_prism1 control_button_prism1_left;
	
	Optometry_button_prism2 control_button_prism2_right;
	Optometry_button_prism2 control_button_prism2_ctrl;
	Optometry_button_prism2 control_button_prism2_left;
	
	Optometry_button_add control_button_add_right;
	Optometry_button_add control_button_add_ctrl;
	Optometry_button_add control_button_add_left;	
	
	Optometry_button_pd control_button_pd_right;
	Optometry_button_pd control_button_pd_ctrl;
	Optometry_button_pd control_button_pd_left;	
	
	ImageButton control_button_addvalue_ctrl;
	ImageButton control_button_decvalue_ctrl;
	ImageButton control_button_cross1_select;
	ImageButton control_button_cross2_select;
	
	Optometry_button_lens control_button_lens_right;
	Optometry_button_lens control_button_lens_left;
	
	TextView control_text_pd_value;
		
	Button control_button_farnear;
	Button control_button_naked;
	Button control_button_optometry;
	Button control_button_reset;
	
	List<Optometry_button> control_button_list = new ArrayList<Optometry_button>(); 
	
	//�������
	boolean ByLongClickDeal=false;
	
	//�����UI����
	data_list Optometry_data;
	
	//�����UI״̬
	current_status Optometry_status;
	
	boolean OptometryConnected=false;
	
	Optometry_gateway m_App;
	
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_optometry_contrl);
		
		control_button_diopter_right=(Optometry_button_diopter)findViewById(R.id.control_button_diopter_right);
		control_button_diopter_left=(Optometry_button_diopter)findViewById(R.id.control_button_diopter_left);
		control_button_diopter_ctrl=(Optometry_button_diopter)findViewById(R.id.control_button_diopter_ctrl);		
		
		control_button_cylinder_right=(Optometry_button_cylinder)findViewById(R.id.control_button_cylinder_right);
		control_button_cylinder_left=(Optometry_button_cylinder)findViewById(R.id.control_button_cylinder_left);
		control_button_cylinder_ctrl=(Optometry_button_cylinder)findViewById(R.id.control_button_cylinder_ctrl);				
		
		//control_button_cylinder_axis_right=(Optometry_button_cylinder_axis)findViewById(R.id.control);
		control_button_cylinder_axis_right=(Optometry_button_cylinder_axis)findViewById(R.id.control_button_cylinder_axis_right);
		control_button_cylinder_axis_left=(Optometry_button_cylinder_axis)findViewById(R.id.control_button_cylinder_axis_left);
		control_button_cylinder_axis_ctrl=(Optometry_button_cylinder_axis)findViewById(R.id.control_button_cylinder_axis_ctrl);	
		
		control_button_prism1_right=(Optometry_button_prism1)findViewById(R.id.control_button_prism1_right);
		control_button_prism1_left=(Optometry_button_prism1)findViewById(R.id.control_button_prism1_left);
		control_button_prism1_ctrl=(Optometry_button_prism1)findViewById(R.id.control_button_prism1_ctrl);				
		
		control_button_prism2_right=(Optometry_button_prism2)findViewById(R.id.control_button_prism2_right);
		control_button_prism2_left=(Optometry_button_prism2)findViewById(R.id.control_button_prism2_left);
		control_button_prism2_ctrl=(Optometry_button_prism2)findViewById(R.id.control_button_prism2_ctrl);		

		control_button_add_right=(Optometry_button_add)findViewById(R.id.control_button_add_right);
		control_button_add_left=(Optometry_button_add)findViewById(R.id.control_button_add_left);
		control_button_add_ctrl=(Optometry_button_add)findViewById(R.id.control_button_add_ctrl);		
				
		control_button_pd_right=(Optometry_button_pd)findViewById(R.id.control_button_pd_right);
		control_button_pd_left=(Optometry_button_pd)findViewById(R.id.control_button_pd_left);
		control_button_pd_ctrl=(Optometry_button_pd)findViewById(R.id.control_button_pd_ctrl);		
				
		control_button_farnear=(Button)findViewById(R.id.control_button_farnear);
		control_button_farnear.setOnClickListener(this);
		control_button_naked=(Button)findViewById(R.id.control_button_naked);
		control_button_naked.setOnClickListener(this);
		control_button_optometry=(Button)findViewById(R.id.control_button_optometry);
		control_button_optometry.setOnClickListener(this);
		control_button_reset=(Button)findViewById(R.id.control_button_reset);
		control_button_reset.setOnClickListener(this);
		
		control_button_lens_right=(Optometry_button_lens)findViewById(R.id.control_button_lens_right);
		control_button_lens_right.setOnClickListener(this);
		control_button_lens_left=(Optometry_button_lens)findViewById(R.id.control_button_lens_left);		
		control_button_lens_left.setOnClickListener(this);
		
		control_button_cross1_select=(ImageButton)findViewById(R.id.control_button_cross1_select);
		control_button_cross1_select.setOnClickListener(this);
		
		control_button_cross2_select=(ImageButton)findViewById(R.id.control_button_cross2_select);
		control_button_cross2_select.setOnClickListener(this);		
	
		
		control_text_pd_value=(TextView)findViewById(R.id.control_text_pd_value);


	    control_button_list.add(control_button_diopter_left);
	    control_button_list.add(control_button_diopter_ctrl);
	    control_button_list.add(control_button_diopter_right);	    
 
	    control_button_list.add(control_button_cylinder_left);
	    control_button_list.add(control_button_cylinder_ctrl);
	    control_button_list.add(control_button_cylinder_right);	
	    
	    control_button_list.add(control_button_cylinder_axis_left);
	    control_button_list.add(control_button_cylinder_axis_ctrl);
	    control_button_list.add(control_button_cylinder_axis_right);
 
	    control_button_list.add(control_button_prism1_left);
	    control_button_list.add(control_button_prism1_ctrl);
	    control_button_list.add(control_button_prism1_right);	  	    
	    
	    control_button_list.add(control_button_prism2_left);
	    control_button_list.add(control_button_prism2_ctrl);
	    control_button_list.add(control_button_prism2_right);	
	    
	    control_button_list.add(control_button_add_left);
	    control_button_list.add(control_button_add_ctrl);
	    control_button_list.add(control_button_add_right);	
	    
	    control_button_list.add(control_button_pd_left);
	    control_button_list.add(control_button_pd_ctrl);
	    control_button_list.add(control_button_pd_right);	    
	    
	    
	    
	    for(int i=0;i<control_button_list.size();i++)
	    { 	
	    	control_button_list.get(i).setOnClickListener(this);
	    	control_button_list.get(i).setOnLongClickListener(this);
	    }
	    
	    control_button_addvalue_ctrl=(ImageButton)findViewById(R.id.control_button_addvalue_ctrl);	
	    control_button_addvalue_ctrl.setOnClickListener(this);
	    control_button_decvalue_ctrl=(ImageButton)findViewById(R.id.control_button_decvalue_ctrl);	
	    control_button_decvalue_ctrl.setOnClickListener(this);
	    
	    	    
	    //��������
	    Optometry_data=new data_list();
	    Optometry_status=new current_status();	 
	    
	    Optometry_data.reset_data();
	    Optometry_status.reset();    
	    
	    UpdateCtrlView();
	    
	    //Ӧ��
	    m_App=(Optometry_gateway) getApplication();	
	  
	    m_App.gwManager.Cmd_GetLocalCtrl();
	    if(m_App.gwManager.GetBTDeviceStatus()==Comm_Base.STATE_CONNECTED)
	    	OptometryConnected=true;
	    
	    return;
	    
	}

	/*
	 * ����Ի���
	 */
	 private void AlarMsg(String msg)
	 {
		 TextView title=new TextView(this);
		 title.setText(" ��ʾ��Ϣ");
		 title.setTextSize(18);
		 
		 Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("��ʾ");
		 builder.setIcon(android.R.drawable.ic_menu_info_details);
		 builder.setMessage(msg);	
		 builder.setPositiveButton("ȷ��", null);
		 builder.show();		 
	 }	
	 	
	

	 /*
	  * ����ѡ��
	  * @see android.view.View.OnClickListener#onClick(android.view.View)
	  */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub	
		//��ֵ��ť
		
		if(Optometry_packet.Queue_GetLen()>0)
			return;
		
		if(!OptometryConnected)
		{
			AlarMsg("�����û������");
		}
		
		if(!Optometry_status.isnaked)
		{
			if(ByLongClickDeal==false)
			{
				for(int i=0;i<control_button_list.size();i++)
				{
					if(control_button_list.get(i)==arg0)
					{
						control_button_list.get(i).OnClick(this);
						break;
					}
				}
			}
			
			//�Ӻ�
			if(control_button_addvalue_ctrl==arg0)
			{
				Add_DecValue(true);
			}	
			
			//����
			if(control_button_decvalue_ctrl==arg0)
			{
				Add_DecValue(false);
			}
			
			//Զ�ý����л�
			if(control_button_farnear==arg0)
			{
				if(Optometry_status.far_near_status==1)
				{
					Optometry_status.far_near_status=2;
					Optometry_status.be_near_lamp_on=true;
				}
				else
				{
					Optometry_status.far_near_status=1;
					Optometry_status.be_near_lamp_on=false;
				}
				
				Optometry_status.active_mode=current_status.MODE_SPHERE;
				Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_NEARFAR);				
			}
		}
		else
		{
			;//AlarMsg("��ǰ������״̬,�޷���������");
		}
		
		//����
		if(control_button_naked==arg0&&!Optometry_status.isnaked)
		{
			Optometry_status.isnaked=true;
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_NC);
		}
		
		//���
		if(control_button_optometry==arg0&&Optometry_status.isnaked)
		{
			Optometry_status.isnaked=false;
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_CV);
		}	
		
		//��λ
		if(control_button_reset==arg0)
		{
			ResetDialog();
		}		
		
		//�ұ����⾵Ƭ
		if(control_button_lens_right==arg0)
			control_button_lens_right.OnClick(this);
		
		//������⾵Ƭ		
		if(control_button_lens_left==arg0)
			control_button_lens_left.OnClick(this);	
		
		//��������ѡ��1
		if(control_button_cross1_select==arg0)
		{
			if(!Optometry_status.be_with_cc)
			{
				Select_CC(true);
			}
			Optometry_status.cc_side_r=0;
			Optometry_status.cc_side_l=0;
			Optometry_status.active_mode=current_status.MODE_CYLINDER_AXIS;
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_STATUS_ALL);
		}
		
		//��������ѡ��2		
		if(control_button_cross2_select==arg0)
		{
			if(!Optometry_status.be_with_cc)
			{
				Select_CC(false);
			}			
			
			Optometry_status.cc_side_r=1;
			Optometry_status.cc_side_l=1;	
			Optometry_status.active_mode=current_status.MODE_CYLINDER_AXIS;
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_STATUS_ALL);
			
		}
			
		UpdateCtrlView();
		ByLongClickDeal=false;
		return;
		
	}
	

	/*
	 * �������
	 * @see android.view.View.OnLongClickListener#onLongClick(android.view.View)
	 */
	@Override
	public boolean onLongClick(View arg0) {
		// TODO Auto-generated method stub
		
		for(int i=0;i<control_button_list.size();i++)
		{
			if(control_button_list.get(i)==arg0)
			{
				ByLongClickDeal=control_button_list.get(i).OnLongClick(this);
				if(ByLongClickDeal)
				{
					UpdateCtrlView();
				}
				break;
			}
		}
		
		return false;
	}	
	
	@Override
	public void onBackPressed() {
		BackDialog();
		return;
	}
	
	/*
	 * �˳��Ի���
	 */
	
	private void BackDialog()
	{
		 Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("��ʾ��Ϣ");
		 builder.setIcon(android.R.drawable.ic_menu_info_details);
		 builder.setMessage("�㽫Ҫ����������⣬�Ƿ�ȷ��");
		 builder.setNegativeButton("�������", 
				 new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						;
					}
				});
		 builder.setPositiveButton("ȷ��", 
				 new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						m_App.gwManager.Cmd_ReleaseGwCtrl();
						finish();
					}
				});	
		 
		 builder.setCancelable(true);
		 builder.create().show();  		
	}
	
	/*
	 * �������пؼ�
	 */
	public void UpdateCtrlView()
	{
		for(int i=0;i<control_button_list.size();i++)
		{
			control_button_list.get(i).Update(Optometry_data, Optometry_status);
		}
		
		if(Optometry_status.far_near_status==1)
			control_button_farnear.setText("Զ��");
		else
			control_button_farnear.setText("����");
		
		if(Optometry_status.isnaked)
		{
			control_button_naked.setTextColor(Color.parseColor("#ffffff"));
			control_button_optometry.setTextColor(Color.parseColor("#A0A0A0"));
		}
		else
		{
			control_button_optometry.setTextColor(Color.parseColor("#ffffff"));
			control_button_naked.setTextColor(Color.parseColor("#A0A0A0"));
		}
		
		control_button_lens_right.Update(Optometry_data, Optometry_status);
		control_button_lens_left.Update(Optometry_data, Optometry_status);
		
		Update_pd_value();
		
	}
	
	/*
	 * �Ӽ�ֵ
	 */
	public void Add_DecValue(boolean mode)
	{		
		data_list old_data=new data_list();
		old_data.copy_from_data(Optometry_data);
		
		boolean data_change;
		int valueMode=mode?1:-1;
		
		switch(Optometry_status.active_mode)
		{
		case current_status.MODE_SPHERE:
			if(Optometry_status.right_active)
			{
				if(Optometry_status.far_near_status==1)
					Optometry_data.sphere_right_far=sphere_computer(Optometry_data.sphere_right_far,valueMode);//Optometry_status.s_sphere_step*valueMode;
				else
					Optometry_data.sphere_right_near=sphere_computer(Optometry_data.sphere_right_near,valueMode);//Optometry_status.s_sphere_step*valueMode;
			}

			if(Optometry_status.left_active)
			{
				if(Optometry_status.far_near_status==1)
					Optometry_data.sphere_left_far=sphere_computer(Optometry_data.sphere_left_far,valueMode);
				else
					Optometry_data.sphere_left_near=sphere_computer(Optometry_data.sphere_left_near,valueMode);
			}		
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_SHERE);
			break;
			
		case current_status.MODE_CYLINDER:
			if(Optometry_status.left_active)
			{
				if(Optometry_status.far_near_status==1)
					Optometry_data.cylinder_left_far=cylinder_computer(Optometry_data.cylinder_left_far,Optometry_status.s_cylinder_mode,valueMode);
				else
					Optometry_data.cylinder_left_near=cylinder_computer(Optometry_data.cylinder_left_near,Optometry_status.s_cylinder_mode,valueMode);
			}	
			
			if(Optometry_status.right_active)
			{
				if(Optometry_status.far_near_status==1)
					Optometry_data.cylinder_right_far=cylinder_computer(Optometry_data.cylinder_right_far,Optometry_status.s_cylinder_mode,valueMode);
				else
					Optometry_data.cylinder_right_near=cylinder_computer(Optometry_data.cylinder_right_near,Optometry_status.s_cylinder_mode,valueMode);
			}	
			
			data_change=old_data.check_change(Optometry_data);
		
			if(data_change)
			{
				if(this.Optometry_status.be_with_cc)
				{
					Correction_CC(valueMode);
				}
							
				Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_CYLINDER);
			}		
			break;
			
		case current_status.MODE_CYLINDER_AXIS:
			if(Optometry_status.left_active)
			{
				if(Optometry_status.far_near_status==1)
					Optometry_data.axis_left_far=cylinder_axis_computer(Optometry_data.axis_left_far,valueMode);
				else
					Optometry_data.axis_left_near=cylinder_axis_computer(Optometry_data.axis_left_near,valueMode);
			}	
			
			if(Optometry_status.right_active)
			{
				if(Optometry_status.far_near_status==1)
					Optometry_data.axis_right_far=cylinder_axis_computer(Optometry_data.axis_right_far,valueMode);
				else
					Optometry_data.axis_right_near=cylinder_axis_computer(Optometry_data.axis_right_near,valueMode);
			}	
			
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_CYLINDER_AXIS);
			break;	
			
		case current_status.MODE_PRISM1:
			if(Optometry_status.s_prism_mode)
			{
				if(Optometry_status.left_active)
				{
					if(Optometry_status.far_near_status==1)
						Optometry_data.prism_left_h_far=prism_hv_computer(Optometry_data.prism_left_h_far,Optometry_data.prism_left_v_far,valueMode);//Optometry_data.prism_left_h_far+=Optometry_status.s_prism_step*valueMode;
					else
						//Optometry_data.prism_left_h_near+=Optometry_status.s_prism_step*valueMode;
						Optometry_data.prism_left_h_near=prism_hv_computer(Optometry_data.prism_left_h_near,Optometry_data.prism_left_v_near,valueMode);
				}	
			
				if(Optometry_status.right_active)
				{
					if(Optometry_status.far_near_status==1)
						//Optometry_data.prism_right_h_far+=Optometry_status.s_prism_step*valueMode*-1;
						Optometry_data.prism_right_h_far=prism_hv_computer(Optometry_data.prism_right_h_far,Optometry_data.prism_right_v_far,valueMode*-1);
					else
						//Optometry_data.prism_right_h_near+=Optometry_status.s_prism_step*valueMode*-1;
						Optometry_data.prism_right_h_near=prism_hv_computer(Optometry_data.prism_right_h_near,Optometry_data.prism_right_v_near,valueMode*-1);
				}		
			}
			else
			{
				if(Optometry_status.left_active)
				{
					if(Optometry_status.far_near_status==1)
						Optometry_data.prism_left_p_far=prism_p_computer(Optometry_data.prism_left_p_far,valueMode);
					else
						Optometry_data.prism_left_p_near=prism_p_computer(Optometry_data.prism_left_p_near,valueMode);
				}	
			
				if(Optometry_status.right_active)
				{
					if(Optometry_status.far_near_status==1)
						Optometry_data.prism_right_p_far=prism_p_computer(Optometry_data.prism_right_p_far,valueMode);
					else
						Optometry_data.prism_right_p_near=prism_p_computer(Optometry_data.prism_right_p_near,valueMode);
				}								
				
			}
			UpdatePrism();
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_PRISM);
			break;			

		case current_status.MODE_PRISM2:
			if(Optometry_status.s_prism_mode)
			{
				if(Optometry_status.left_active)
				{
					if(Optometry_status.far_near_status==1)
						//Optometry_data.prism_left_v_far+=Optometry_status.s_prism_step*valueMode;
						Optometry_data.prism_left_v_far=prism_hv_computer(Optometry_data.prism_left_v_far,Optometry_data.prism_left_h_far,valueMode);
					else
						//Optometry_data.prism_left_v_near+=Optometry_status.s_prism_step*valueMode;
						Optometry_data.prism_left_v_near=prism_hv_computer(Optometry_data.prism_left_v_near,Optometry_data.prism_left_h_near,valueMode);
				}	
			
				if(Optometry_status.right_active)
				{
					if(Optometry_status.far_near_status==1)
						//Optometry_data.prism_right_v_far+=Optometry_status.s_prism_step*valueMode*-1;
						Optometry_data.prism_right_v_far=prism_hv_computer(Optometry_data.prism_right_v_far,Optometry_data.prism_right_h_far,valueMode*-1);
					else
						//Optometry_data.prism_right_v_near+=Optometry_status.s_prism_step*valueMode*-1;
						Optometry_data.prism_right_v_near=prism_hv_computer(Optometry_data.prism_right_v_near,Optometry_data.prism_right_h_near,valueMode*-1);
				}		
			}
			else
			{
				if(Optometry_status.left_active)
				{
					if(Optometry_status.far_near_status==1)
						Optometry_data.prism_left_r_far=prism_r_computer(Optometry_data.prism_left_r_far,valueMode);
					else
						Optometry_data.prism_left_r_near=prism_r_computer(Optometry_data.prism_left_r_near,valueMode);
				}	
			
				if(Optometry_status.right_active)
				{
					if(Optometry_status.far_near_status==1)
						Optometry_data.prism_right_r_far=prism_r_computer(Optometry_data.prism_right_r_far,valueMode);
					else
						Optometry_data.prism_right_r_near=prism_r_computer(Optometry_data.prism_right_r_near,valueMode);
				}								
				
			}
			
			UpdatePrism();
			
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_PRISM);
			break;			
			
		case current_status.MODE_ADD:
			
			if(Optometry_status.far_near_status!=1)
				break;
			
			if(Optometry_status.right_active)
			{
				if(Optometry_status.far_near_status==1)
					Optometry_data.add_right=add_computer(Optometry_data.add_right,valueMode);

			}

			if(Optometry_status.left_active)
			{
				if(Optometry_status.far_near_status==1)
					Optometry_data.add_left=add_computer(Optometry_data.add_left,valueMode);
			}	
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_ADD);
			break;	
			
		case current_status.MODE_PD:
			if(Optometry_status.right_active)
			{
				if(Optometry_status.far_near_status==1)
					//Optometry_data.pd_right_far+=Optometry_status.s_pd_step*valueMode;
					Optometry_data.pd_right_far=pd_computer(Optometry_data.pd_right_far,Optometry_data.pd_left_far,valueMode);
				else
					//Optometry_data.pd_right_near+=Optometry_status.s_pd_step*valueMode;
					Optometry_data.pd_right_near=pd_computer(Optometry_data.pd_right_near,Optometry_data.pd_left_near,valueMode);
			}

			if(Optometry_status.left_active)
			{
				if(Optometry_status.far_near_status==1)
					//Optometry_data.pd_left_far+=Optometry_status.s_pd_step*valueMode;
					Optometry_data.pd_left_far=pd_computer(Optometry_data.pd_left_far,Optometry_data.pd_right_far,valueMode);
				else
					//Optometry_data.pd_left_near+=Optometry_status.s_pd_step*valueMode;
					Optometry_data.pd_left_near=pd_computer(Optometry_data.pd_left_near,Optometry_data.pd_right_near,valueMode);
			}	
			
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_PD);			
			break;			
		
		}
		
		UpdateCtrlView();
		
	}
	
	

	/*
	 * �򾵼���
	 * value: ��ǰֵ
	 * comp_mode:����ģʽ
	 * ���ֵΪ1675
	 * ��СֵΪ-1900
	 */
	
	private int sphere_computer(int value,int comp_mode)
	{
		int result=value+Optometry_status.s_sphere_step*comp_mode;	
		if(result>1675)
			return 1675;
		
		if(result<-1900)
			return -1900;
		
		return result;
		
	}
	
	/*
	 * ��������
	 * value: ��ǰֵ
	 * cylindermode:����ģʽ
	 * comp_mode:����ģʽ
	 * ���ֵΪ875
	 * ��СֵΪ-875
	 */
	private int cylinder_computer(int value,boolean cylindermode,int comp_mode)
	{
		if(cylindermode==true&&value==0&&comp_mode==-1)
			return 0;
		
		if(cylindermode==false&&value==0&&comp_mode==1)
			return 0;
		
		value+=25*comp_mode;
		
		if(value>875)
			return 875;
		
		if(value<-875)
			return -875;
			
		return value;
	}

	
	/*
	 * ������λ����
	 * value: ��ǰֵ
	 * comp_mode:����ģʽ
	 * ��Χ�� 0-180
	 */
	
	private int cylinder_axis_computer(int value,int comp_mode)
	{
		value+=Optometry_status.s_cylinder_axis_step*comp_mode;
		
		value=(value+180)%180;
		
		if(value==0)
			return 180;
				
		return value;
	}
	
	/*
	 * �⾵X-Yƽ�����
	 * value: ��ǰֵ
	 * other_value:��һ��HV������ֵ
	 * comp_mode:����ģʽ
	 * ���ֵ:  H*H+V*V=400X
	 * 
	 */
	private int prism_hv_computer(int value,int other_value,int comp_mode)
	{
		value+=Optometry_status.s_prism_step*comp_mode;
		
		
		int max_prism=(int)Math.sqrt(200*200-other_value*other_value);
		int min_prism;
				
		if(max_prism>200)
			max_prism=200;
		
		min_prism=max_prism*-1;
				
		if(value>max_prism)
			return max_prism;
		
		if(value<min_prism)
			return min_prism;
		
		return value;
	}
		
	/*
	 * �⾵P����
	 * value: ��ǰֵ
	 * comp_mode:����ģʽ
	 * ���ֵ:  20
	 */
	private int prism_p_computer(int value,int comp_mode)
	{
		
		value+=Optometry_status.s_prism_step*comp_mode;
		
		if(value>200)
			return 200;
		
		if(value<0)
			return 0;
		
		return value;
	}	
	
	/*
	 * �⾵�Ƕȼ���
	 * value: ��ǰֵ
	 * comp_mode:����ģʽ
	 * ��Χ�� 0-360 
	 */
	
	private int prism_r_computer(int value,int comp_mode)
	{
		
		value+=Optometry_status.s_prism_r_step*comp_mode;
		
		value=(value+360)%360;
		
		return value;
	}	
	
	/*
	 * �ӹ����
	 * value: ��ǰֵ
	 * comp_mode:����ģʽ
	 * ��Χ�� 0-1000 
	 */
	private int add_computer(int value,int comp_mode)
	{
		int result=value+25*comp_mode;	
		if(result>1000)
			return 1000;
		
		if(result<0)
			return 0;
		
		return result;		
		
	}
	
	/*
	 * ͫ�����
	 * value: ��ǰֵ
	 * other_value:��һ�ߵ�ֵ
	 * comp_mode:����ģʽ
	 * ��Χ������ 25-40 ���ܵ�ͫ�� 50-80
	 */
	private int pd_computer(int value,int other_value,int comp_mode)
	{
		//���� 25-40 ���ܵ�ͫ�� 50-80
		value+=Optometry_status.s_pd_step*comp_mode;
		
		if(value<250)
			return 250;
		
		if(value>400)
			return 400;	
		
		return value;
		
	}	
	
	/*
	 * UI����ͫ���ֵ
	 * 
	 */
	private void Update_pd_value()
	{
		int value;
		if(Optometry_status.far_near_status==1)
			value=Optometry_data.pd_right_far+Optometry_data.pd_left_far;
		else
			value=Optometry_data.pd_right_near+Optometry_data.pd_left_near;
		
		float d1 = value;
		float f = 0.1F * d1;
			
		String value_Str = String.format("PD:%.1f", f);	
		control_text_pd_value.setText(value_Str);
	}	
	
	/*
	 * ���PD���
	 * 
	 */
	public void Clear_PD()	
	{		
		if(Optometry_status.be_in_pd_test)
		{
			Optometry_status.set_mode_pd(false);
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_STATUS_ALL);						
		}	
		
	}
		
	
	
	/*
	 * ������ݸ�λ
	 */
	private void Optometry_Reset()
	{
		Optometry_data.reset_data();
		Optometry_status.reset();
		Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_RESET);
		UpdateCtrlView();
	}
	
	private void ResetDialog()
	{
		 Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("��ʾ��Ϣ");
		 builder.setIcon(android.R.drawable.ic_menu_info_details);
		 builder.setMessage("�ò�������λ���������������Ƿ�ȷ��");
		 builder.setNegativeButton("����", 
				 new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						;
					}
				});
		 builder.setPositiveButton("ȷ��", 
				 new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Optometry_Reset();
					}
				});	
		 
		 builder.setCancelable(true);
		 builder.create().show();  		
	}
	

	
	//�۾���ѡȡ����
	public void Optometry_CheckeyeActive()
	{
	}
	
	//����ȡ��
	 public void Optometry_cylinder_Not()
	 {
		 /* step 1, �򾵶�����Ϊ ��ǰ���� + ȡ��ǰ����������*/
		 Optometry_data.sphere_left_far+= Optometry_data.cylinder_left_far;
		 Optometry_data.sphere_right_far+= Optometry_data.cylinder_right_far;
		 
		 Optometry_data.sphere_left_near+= Optometry_data.cylinder_left_near;
		 Optometry_data.sphere_right_near+= Optometry_data.cylinder_right_near;	
		 
		 /*step2 ���������л�   */
		 Optometry_data.cylinder_left_far*=-1;
		 Optometry_data.cylinder_right_far*=-1;
				
		 Optometry_data.cylinder_left_near*=-1;
		 Optometry_data.cylinder_right_near*=-1;	
		
		
		/* step3 ��λ���� ����90��ƫת,��180��ȡģ */
		 Optometry_data.axis_left_far=(Optometry_data.axis_left_far+90)%180;
		 Optometry_data.axis_right_far=(Optometry_data.axis_right_far+90)%180;		
		 Optometry_data.axis_left_near=(Optometry_data.axis_left_near+90)%180;				
		 Optometry_data.axis_right_near=(Optometry_data.axis_right_near+90)%180;		 
		 	 
	 }

	 /*
	  * ��ȡ�⾵Pֵ
	  */
	private int GetPrismLen(int prism_h,int prism_v)
	{
		return (int)Math.sqrt(prism_h*prism_h+prism_v*prism_v);
	}
	
	/*
	 * ��ȡ�⾵�Ƕ�
	 * 
	 */
	
	private int GetPrismAngle(int prism_h,int prism_v)
	{
		int angle=(int)Math.toDegrees(Math.atan2(prism_v,prism_h));
		return (angle+360)%360;
		
	}
	
	/*
	 * ���⾵���ݽ���XY��PR��ͬ��
	 * 
	 * P=sqrt(H*H+V*V)
	 * R=atan(v,h);
	 * 
	 * H=cos(R)*P;
	 * V=sin(R)*P;
	 * 
	 */
	public void UpdatePrism()
	{
		if(Optometry_status.far_near_status==1)
		{
		if(Optometry_status.s_prism_mode)
		{
			Optometry_data.prism_left_p_far=GetPrismLen(Optometry_data.prism_left_h_far,Optometry_data.prism_left_v_far);
			Optometry_data.prism_right_p_far=GetPrismLen(Optometry_data.prism_right_h_far,Optometry_data.prism_right_v_far);
		
			Optometry_data.prism_left_r_far=GetPrismAngle(Optometry_data.prism_left_h_far,Optometry_data.prism_left_v_far);
			Optometry_data.prism_right_r_far=GetPrismAngle(Optometry_data.prism_right_h_far,Optometry_data.prism_right_v_far);
		}
		else
		{
			Optometry_data.prism_left_h_far=(int) (Math.cos(Math.toRadians(Optometry_data.prism_left_r_far))*Optometry_data.prism_left_p_far);
			Optometry_data.prism_right_h_far=(int) (Math.cos(Math.toRadians(Optometry_data.prism_right_r_far))*Optometry_data.prism_right_p_far);
			
			Optometry_data.prism_left_v_far=(int) (Math.sin(Math.toRadians(Optometry_data.prism_left_r_far))*Optometry_data.prism_left_p_far);
			Optometry_data.prism_right_v_far=(int) (Math.sin(Math.toRadians(Optometry_data.prism_right_r_far))*Optometry_data.prism_right_p_far);			
		}
		}
		else
		{
		if(Optometry_status.s_prism_mode)
		{
			Optometry_data.prism_left_p_near=GetPrismLen(Optometry_data.prism_left_h_near,Optometry_data.prism_left_v_near);
			Optometry_data.prism_right_p_near=GetPrismLen(Optometry_data.prism_right_h_near,Optometry_data.prism_right_v_near);
		
			Optometry_data.prism_left_r_near=GetPrismAngle(Optometry_data.prism_left_h_near,Optometry_data.prism_left_v_near);
			Optometry_data.prism_right_r_near=GetPrismAngle(Optometry_data.prism_right_h_near,Optometry_data.prism_right_v_near);
		}
		else
		{
			Optometry_data.prism_left_h_near=(int) (Math.sin(Math.toRadians(Optometry_data.prism_left_r_near)*Optometry_data.prism_left_p_near));
			Optometry_data.prism_right_h_near=(int) (Math.sin(Math.toRadians(Optometry_data.prism_right_r_near)*Optometry_data.prism_right_p_near));
			
			Optometry_data.prism_left_v_near=(int) (Math.cos(Math.toRadians(Optometry_data.prism_left_r_near)*Optometry_data.prism_left_p_near));
			Optometry_data.prism_right_v_near=(int) (Math.cos(Math.toRadians(Optometry_data.prism_right_r_near)*Optometry_data.prism_right_p_near));			
		}
		}
		
		
	}
	
	/*
	 * 
	 * ���뽻������ģʽ
	 * 
	 */
	
	public void Select_CC(boolean right_eye)
	{
		if(right_eye)
		{
			Optometry_status.lens_right=12;
			Optometry_status.R_eye_enable=true;;
			Optometry_status.r_lens_code=(byte)0x00;
			Optometry_status.right_active=true;
			
			Optometry_status.lens_left=1;
			Optometry_status.L_eye_enable=false;
			Optometry_status.l_lens_code=(byte)0x00;	
			Optometry_status.left_active=false;
			
			Optometry_status.set_mode_cc(true);
		}
		else
		{
			Optometry_status.lens_right=1;
			Optometry_status.R_eye_enable=false;;
			Optometry_status.r_lens_code=(byte)0x00;
			Optometry_status.right_active=false;
			
			Optometry_status.lens_left=12;
			Optometry_status.L_eye_enable=true;
			Optometry_status.l_lens_code=(byte)0x00;
			Optometry_status.left_active=true;
			
			Optometry_status.set_mode_cc(true);
		}
		Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_STATUS_ALL);		
		
	}
	

	/*
	 * �����������򾵵�����
	 * 
	 */
	
	void Correction_Sphere(int changemode)
	{
		if(Optometry_status.right_active)
		{
			if(Optometry_status.far_near_status==1)
				Optometry_data.sphere_right_far=sphere_computer(Optometry_data.sphere_right_far,changemode);//Optometry_status.s_sphere_step*valueMode;
			else
				Optometry_data.sphere_right_near=sphere_computer(Optometry_data.sphere_right_near,changemode);//Optometry_status.s_sphere_step*valueMode;
		}

		if(Optometry_status.left_active)
		{
			if(Optometry_status.far_near_status==1)
				Optometry_data.sphere_left_far=sphere_computer(Optometry_data.sphere_left_far,changemode);
			else
				Optometry_data.sphere_left_near=sphere_computer(Optometry_data.sphere_left_near,changemode);
		}	
		
	}
	
	/*
	 * ������������
	 * valueMode���Ӽ�ģʽ
	 * ������ʽ����������ÿ����50�ȣ�������25��
	 * 
	 */
	void Correction_CC(int valueMode)
	{
		int changemode=0;
		int current_value=0;
		
		Optometry_status.s_sphere_step=25;
		
		if(!Optometry_status.s_cylinder_mode)
			changemode=valueMode*-1;
		else
			changemode=valueMode;
		
		
		if(Optometry_status.right_active)
		{
			if(Optometry_status.far_near_status==1)
				current_value=Optometry_data.cylinder_right_far;
			else
				current_value=Optometry_data.cylinder_right_near;
		}

		if(Optometry_status.left_active)
		{
			if(Optometry_status.far_near_status==1)
				current_value=Optometry_data.cylinder_left_far;
			else
				current_value=Optometry_data.cylinder_left_near;
		}			
		
		
		int change=(current_value/25)%2;
		
		
		if(change==0)
		{
			if(changemode==1)
			{
				Correction_Sphere(changemode);
			}						
		}
		else
		{
			if(changemode==-1)
			{
				Correction_Sphere(changemode);
			}
			
		}
		
	}
	
	/*
	 * �رս�������
	 * 
	 */
	public void Clear_CC()	
	{		
		if(Optometry_status.be_with_cc)
		{
			Optometry_status.set_mode_cc(false);
			Optometry_packet.SendDataCmd(Optometry_data,Optometry_status,Optometry_packet.CMD_STATUS_ALL);						
		}	
		
	}	
	
	/*
	 * ���������Ƭ����
	 */
	public void CheckLens(int select_mode)
	{
		switch(select_mode)
		{
		case 1: //ѡ������
			if(Optometry_status.lens_right==1)
				Optometry_status.lens_right=0;
			
			if(Optometry_status.lens_left==0)
				Optometry_status.lens_left=1;
			break;
		case 2: //ѡ������
			if(Optometry_status.lens_right==0)
				Optometry_status.lens_right=1;
			
			if(Optometry_status.lens_left==1)
				Optometry_status.lens_left=0;			
			
			break;
		case 3: //ѡ��˫��
			if(Optometry_status.lens_right==1)
				Optometry_status.lens_right=0;
			
			if(Optometry_status.lens_left==1)
				Optometry_status.lens_left=0;				
			
			break;
		}
	}
	

}
