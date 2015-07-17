package com.rs.optometry_gateway;

/*
 * UI数据列表
 * 
 */

class data_list
{
  int add_left = 0;
  int add_right = 0;
  int axis_left_far = 180;
  int axis_left_near = 180;
  int axis_right_far = 180;
  int axis_right_near = 180;
  int cylinder_left_far = 0;
  int cylinder_left_near = 0;
  int cylinder_right_far = 0;
  int cylinder_right_near = 0;
  int pd_far = 640;
  int pd_left_far = 320;
  int pd_left_near = 300;
  int pd_near = 600;
  int pd_right_far = 320;
  int pd_right_near = 300;
  int prism_left_h_far = 0;
  int prism_left_h_near = 0;
  int prism_left_p_far = 0;
  int prism_left_p_near = 0;
  int prism_left_r_far = 0;
  int prism_left_r_near = 0;
  int prism_left_v_far = 0;
  int prism_left_v_near = 0;
  int prism_right_h_far = 0;
  int prism_right_h_near = 0;
  int prism_right_p_far = 0;
  int prism_right_p_near = 0;
  int prism_right_r_far = 0;
  int prism_right_r_near = 0;
  int prism_right_v_far = 0;
  int prism_right_v_near = 0;
  int sphere_left_far = 0;
  int sphere_left_near = 0;
  int sphere_right_far = 0;
  int sphere_right_near = 0;
  String va_far = "VA";
  String va_l_far = "";
  String va_l_near = "";
  String va_near = "VA";
  String va_r_far = "";
  String va_r_near = "";

  public void copy_from_data(data_list paramdata_list)
  {
    int i = paramdata_list.sphere_left_far;
    this.sphere_left_far = i;
    int j = paramdata_list.sphere_left_near;
    this.sphere_left_near = j;
    int k = paramdata_list.sphere_right_far;
    this.sphere_right_far = k;
    int m = paramdata_list.sphere_right_near;
    this.sphere_right_near = m;
    int n = paramdata_list.add_left;
    this.add_left = n;
    int i1 = paramdata_list.add_right;
    this.add_right = i1;
    int i2 = paramdata_list.axis_left_far;
    this.axis_left_far = i2;
    int i3 = paramdata_list.axis_left_near;
    this.axis_left_near = i3;
    int i4 = paramdata_list.axis_right_far;
    this.axis_right_far = i4;
    int i5 = paramdata_list.axis_right_near;
    this.axis_right_near = i5;
    int i6 = paramdata_list.cylinder_left_far;
    this.cylinder_left_far = i6;
    int i7 = paramdata_list.cylinder_left_near;
    this.cylinder_left_near = i7;
    int i8 = paramdata_list.cylinder_right_far;
    this.cylinder_right_far = i8;
    int i9 = paramdata_list.cylinder_right_near;
    this.cylinder_right_near = i9;
    int i10 = paramdata_list.pd_far;
    this.pd_far = i10;
    int i11 = paramdata_list.pd_left_far;
    this.pd_left_far = i11;
    int i12 = paramdata_list.pd_left_near;
    this.pd_left_near = i12;
    int i13 = paramdata_list.pd_near;
    this.pd_near = i13;
    int i14 = paramdata_list.pd_right_far;
    this.pd_right_far = i14;
    int i15 = paramdata_list.pd_right_near;
    this.pd_right_near = i15;
    int i16 = paramdata_list.prism_left_h_far;
    this.prism_left_h_far = i16;
    int i17 = paramdata_list.prism_left_h_near;
    this.prism_left_h_near = i17;
    int i18 = paramdata_list.prism_left_p_far;
    this.prism_left_p_far = i18;
    int i19 = paramdata_list.prism_left_p_near;
    this.prism_left_p_near = i19;
    int i20 = paramdata_list.prism_left_r_far;
    this.prism_left_r_far = i20;
    int i21 = paramdata_list.prism_left_r_near;
    this.prism_left_r_near = i21;
    int i22 = paramdata_list.prism_left_v_far;
    this.prism_left_v_far = i22;
    int i23 = paramdata_list.prism_left_v_near;
    this.prism_left_v_near = i23;
    int i24 = paramdata_list.prism_right_h_far;
    this.prism_right_h_far = i24;
    int i25 = paramdata_list.prism_right_h_near;
    this.prism_right_h_near = i25;
    int i26 = paramdata_list.prism_right_p_far;
    this.prism_right_p_far = i26;
    int i27 = paramdata_list.prism_right_p_near;
    this.prism_right_p_near = i27;
    int i28 = paramdata_list.prism_right_r_far;
    this.prism_right_r_far = i28;
    int i29 = paramdata_list.prism_right_r_near;
    this.prism_right_r_near = i29;
    int i30 = paramdata_list.prism_right_v_far;
    this.prism_right_v_far = i30;
    int i31 = paramdata_list.prism_right_v_near;
    this.prism_right_v_near = i31;
    String str1 = paramdata_list.va_far;
    this.va_far = str1;
    String str2 = paramdata_list.va_l_far;
    this.va_l_far = str2;
    String str3 = paramdata_list.va_l_near;
    this.va_l_near = str3;
    String str4 = paramdata_list.va_near;
    this.va_near = str4;
    String str5 = paramdata_list.va_r_far;
    this.va_r_far = str5;
    String str6 = paramdata_list.va_r_near;
    this.va_r_near = str6;
  }

  public void reset_data()
  {
    this.sphere_left_far = 0;
    this.sphere_left_near = 0;
    this.sphere_right_far = 0;
    this.sphere_right_near = 0;
    this.add_left = 0;
    this.add_right = 0;
    this.axis_left_far = 180;
    this.axis_left_near = 180;
    this.axis_right_far = 180;
    this.axis_right_near = 180;
    this.cylinder_left_far = 0;
    this.cylinder_left_near = 0;
    this.cylinder_right_far = 0;
    this.cylinder_right_near = 0;
    this.pd_far = 640;
    this.pd_left_far = 320;
    this.pd_left_near = 300;
    this.pd_near = 600;
    this.pd_right_far = 320;
    this.pd_right_near = 300;
    this.prism_left_h_far = 0;
    this.prism_left_h_near = 0;
    this.prism_left_p_far = 0;
    this.prism_left_p_near = 0;
    this.prism_left_r_far = 0;
    this.prism_left_r_near = 0;
    this.prism_left_v_far = 0;
    this.prism_left_v_near = 0;
    this.prism_right_h_far = 0;
    this.prism_right_h_near = 0;
    this.prism_right_p_far = 0;
    this.prism_right_p_near = 0;
    this.prism_right_r_far = 0;
    this.prism_right_r_near = 0;
    this.prism_right_v_far = 0;
    this.prism_right_v_near = 0;
    this.va_far = " VA ";
    this.va_l_far = "";
    this.va_l_near = "";
    this.va_near = " VA ";
    this.va_r_far = "";
    this.va_r_near = "";
  }
  
  public boolean check_change(data_list check_list)
  {
	  if(this.sphere_left_far!=check_list.sphere_left_far)
		  return true;
	  
	  if(this.sphere_right_far!=check_list.sphere_right_far)
		  return true;	  
	  
	  if(this.sphere_right_near!=check_list.sphere_right_near)
		  return true;
	  
	  if(this.add_left!=check_list.add_left)
		  return true;	  
	  	  
	  if(this.add_right!=check_list.add_right)
		  return true;
	  
	  if(this.axis_left_far!=check_list.axis_left_far)
		  return true;	  
	  	  
	  if(this.axis_left_near!=check_list.axis_left_near)
		  return true;
	  
	  if(this.axis_right_far!=check_list.axis_right_far)
		  return true;	  
	  
	  if(this.axis_right_near!=check_list.axis_right_near)
		  return true;
	  
	  if(this.cylinder_left_far!=check_list.cylinder_left_far)
		  return true;	  
	  
	  if(this.cylinder_left_near!=check_list.cylinder_left_near)
		  return true;
	  
	  if(this.cylinder_right_far !=check_list.cylinder_right_far )
		  return true;	  
	  	  
	  if(this.cylinder_right_near !=check_list.cylinder_right_near )
		  return true;
	  
	  if(this.pd_far !=check_list.pd_far)
		  return true;	  
	  	  
	  if(this.pd_left_far !=check_list.pd_left_far)
		  return true;
	  
	  if(this.pd_left_near !=check_list.pd_left_near )
		  return true;	  
	  
	  if(this.pd_near !=check_list.pd_near )
		  return true;
	  
	  if(this.pd_right_far !=check_list.pd_right_far )
		  return true;	  
	  
	  if(this.pd_right_near !=check_list.pd_right_near )
		  return true;
	  
	  if(this.prism_left_h_far !=check_list.prism_left_h_far )
		  return true;	  
	  	  
	  if(this.prism_left_h_near !=check_list.prism_left_h_near )
		  return true;
	  
	  if(this.prism_left_p_far !=check_list.prism_left_p_far )
		  return true;	  
	  	  
	  if(this.prism_left_p_near !=check_list.prism_left_p_near )
		  return true;
	  
	  if(this.prism_left_r_far !=check_list.prism_left_r_far )
		  return true;	  
	  
	  if(this.prism_left_r_near !=check_list.prism_left_r_near )
		  return true;
	  
	  if(this.prism_left_v_far !=check_list.prism_left_v_far )
		  return true;	  
	  
	  if(this.prism_left_v_near !=check_list.prism_left_v_near )
		  return true;
	  
	  if(this.prism_right_h_far !=check_list.prism_right_h_far )
		  return true;	  
	  	  
	  if(this.prism_right_h_near !=check_list.prism_right_h_near )
		  return true;
	  
	  if(this.prism_right_p_far !=check_list.prism_right_p_far )
		  return true;	  
	  	  
	  if(this.prism_right_p_near !=check_list.prism_right_p_near )
		  return true;
	  
	  if(this.prism_right_r_far !=check_list.prism_right_r_far )
		  return true;	  
	  
	  if(this.prism_right_v_far  !=check_list.prism_right_v_far  )
		  return true;	  
	  	  
	  if(this.prism_right_v_near  !=check_list.prism_right_v_near  )
		  return true;
	  
	  return false;
  } 
  
  
  
}

