package com.rs.optometry_gateway;

/*
 *  
 * 验光仪UI状态控制数据
 * 
 */

class current_status
{
	//UI状态定义
	public static final int MODE_SPHERE=1;
	public static final int MODE_CYLINDER=2;
	public static final int MODE_CYLINDER_AXIS=3;
	public static final int MODE_PRISM1=4;
	public static final int MODE_PRISM2=5;
	public static final int MODE_ADD=6;
	public static final int MODE_PD=7;
	public static final int MODE_VA=8;	
	
	//使用的数据
	
	int far_near_status = 1; //远用近用标志  1为远用，2，为近用
	
	int active_mode=MODE_SPHERE; //UI状态模式
	
	//左右眼UI状况
	boolean right_active=true;
	boolean left_active=true;
	
	//左右眼控制状态
	boolean R_eye_enable = true;
	boolean L_eye_enable = true;	
	
	//镜片UI类型
	int lens_left=0;
	int lens_right=0;
	
	//镜片编码
	byte r_lens_code;	
	byte l_lens_code;	
			
	//球镜步长
	int s_sphere_step=25;
	
	//柱镜步长
	int s_cylinder_axis_step=1;
	
	//柱镜模式
	boolean s_cylinder_mode=false;
	
	//棱镜模式
	boolean s_prism_mode=true;
	
	//棱镜步长
	int s_prism_step=1;
	int s_prism_r_step=1;
	
	//瞳距步长
	int s_pd_step=5;
	

	//使用PD时使用
	boolean be_in_pd_test = false;
	boolean be_near_lamp_on = false;
	boolean light_on = false;
	
	//交叉柱镜
	boolean be_with_cc = false;
	int cc_mode = 0;
	int cc_side_l = 0;
	int cc_side_r = 0;	
	
	//裸眼状态
	boolean isnaked=false;
	
	//加光状态
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
