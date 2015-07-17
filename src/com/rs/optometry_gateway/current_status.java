package com.rs.optometry_gateway;

/*
 *  
 * �����UI״̬��������
 * 
 */

class current_status
{
	//UI״̬����
	public static final int MODE_SPHERE=1;
	public static final int MODE_CYLINDER=2;
	public static final int MODE_CYLINDER_AXIS=3;
	public static final int MODE_PRISM1=4;
	public static final int MODE_PRISM2=5;
	public static final int MODE_ADD=6;
	public static final int MODE_PD=7;
	public static final int MODE_VA=8;	
	
	//ʹ�õ�����
	
	int far_near_status = 1; //Զ�ý��ñ�־  1ΪԶ�ã�2��Ϊ����
	
	int active_mode=MODE_SPHERE; //UI״̬ģʽ
	
	//������UI״��
	boolean right_active=true;
	boolean left_active=true;
	
	//�����ۿ���״̬
	boolean R_eye_enable = true;
	boolean L_eye_enable = true;	
	
	//��ƬUI����
	int lens_left=0;
	int lens_right=0;
	
	//��Ƭ����
	byte r_lens_code;	
	byte l_lens_code;	
			
	//�򾵲���
	int s_sphere_step=25;
	
	//��������
	int s_cylinder_axis_step=1;
	
	//����ģʽ
	boolean s_cylinder_mode=false;
	
	//�⾵ģʽ
	boolean s_prism_mode=true;
	
	//�⾵����
	int s_prism_step=1;
	int s_prism_r_step=1;
	
	//ͫ�ಽ��
	int s_pd_step=5;
	

	//ʹ��PDʱʹ��
	boolean be_in_pd_test = false;
	boolean be_near_lamp_on = false;
	boolean light_on = false;
	
	//��������
	boolean be_with_cc = false;
	int cc_mode = 0;
	int cc_side_l = 0;
	int cc_side_r = 0;	
	
	//����״̬
	boolean isnaked=false;
	
	//�ӹ�״̬
	boolean be_in_add_test = false;
	

  public void reset()
  {
	  active_mode=MODE_SPHERE;
	  this.far_near_status = 1;	
	  this.right_active=true;
	  this.left_active=true;	
	  
	  this.isnaked=false;
	  this.lens_left=0;
	  this.lens_right=0;
	  
	  this.L_eye_enable = true;
	  this.R_eye_enable = true;
	  this.far_near_status = 1;
	  
	  this.be_with_cc = false;
	  this.cc_mode = 0;
	  this.cc_side_l = 0;
	  this.cc_side_r = 0;
	  this.be_in_add_test = false;
	  this.be_in_pd_test = false;
	  
	  this.light_on = false;

  }
  
  void set_mode_pd(boolean mode)
  {
	  if(mode)
	  {
		be_in_pd_test = true;
		light_on = true;
	  }
	  else
	  {
		be_in_pd_test = false;
		light_on = false;
		
	    if(r_lens_code==(byte)0x09)
	    {
	    	lens_right=0;
	    	r_lens_code=0;
	    }

		if(l_lens_code==(byte)0x09)
		{
			lens_left=0;
			l_lens_code=0;			
		}
	  }		  	  
  }
 
  void set_mode_cc(boolean mode)
  {		  
	  if(mode)
	  {
		  be_with_cc = true;
		  cc_mode = 1;
	  }
	  else
	  {
		  be_with_cc = false;
		  cc_mode = 0; 
		 
		  if((lens_right==1||lens_right==12))
		  {
			  lens_right=0;
		  }
		  
		  if((lens_left==1||lens_left==12))
		  {
			  lens_left=0;
		  }		    
	  }			
		
  }
}
