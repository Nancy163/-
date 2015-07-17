package com.rs.optometry_gateway;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/*
 *  ���ؿ���UI
 * 
 */

public class Optometry_console extends Activity  implements OnClickListener,Comm_IF_Manager,OnItemSelectedListener  {
	
	
	Optometry_gateway m_App;
	
	/* config */
	Button console_button_device;
	Button console_button_servername;
	Button console_button_serverip;
	Button console_button_serverport;
	TextView console_text_device_mac;
	
	Button console_button_gatawayid;
	Button console_button_gatawayname;	
	Button console_button_gatewayinfo;
	
    /* status */
	TextView console_text_device;
	TextView console_text_server;
	TextView console_text_contrl;
	
	
	/*contrl*/
	Button console_button_connectdevice;
	Button console_button_connectserver;
	Button console_button_restartserver;
	
	Button console_button_local;
	Button console_button_exit;
	Button console_button_debug;
	
	/**/
	
	final int INPUTID_SERVERNAME=1;
	final int INPUTID_SERVERIP=2;
	final int INPUTID_SERVERPORT=3;
	final int INPUTID_GATEWAYID=4;	
	final int INPUTID_GATEWAYNAME=5;		
	final int INPUTID_GATEWAYINFO=6;		
	
	//����
    //BroadcastMain  broadcastMain;
	
	
	String[] bluedevice_name_list;
	
	
	private TextView ConnectMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_optometry_gateway);

		m_App=(Optometry_gateway) getApplication();	
		m_App.gwConsole=this;
		
		/* config */
		console_button_device=(Button) findViewById(R.id.console_button_device);
		console_button_device.setOnClickListener(this);
		console_button_servername=(Button) findViewById(R.id.console_button_servername);
		console_button_servername.setOnClickListener(this);
		console_button_serverip=(Button) findViewById(R.id.console_button_serverip);
		console_button_serverip.setOnClickListener(this);
		console_button_serverport=(Button) findViewById(R.id.console_button_serverport);
		console_button_serverport.setOnClickListener(this);
		
		console_text_device_mac=(TextView) findViewById(R.id.console_text_device_mac);
		
		console_button_gatawayid=(Button) findViewById(R.id.console_button_gatawayid);
		console_button_gatawayid.setOnClickListener(this);
		console_button_gatawayname=(Button) findViewById(R.id.console_button_gatawayname);
		console_button_gatawayname.setOnClickListener(this);
		console_button_gatewayinfo=(Button) findViewById(R.id.console_button_gatewayinfo);
		console_button_gatewayinfo.setOnClickListener(this);
		
	    /* status */
		console_text_device=(TextView) findViewById(R.id.console_text_device);
		console_text_server=(TextView) findViewById(R.id.console_text_server);
		console_text_contrl=(TextView) findViewById(R.id.console_text_contrl);	
		
		/*contrl*/
		console_button_connectdevice=(Button) findViewById(R.id.console_button_connectdevice);
		console_button_connectdevice.setOnClickListener(this);
		console_button_connectserver=(Button) findViewById(R.id.console_button_connectserver);
		console_button_connectserver.setOnClickListener(this);
		console_button_restartserver=(Button) findViewById(R.id.console_button_restartserver);
		console_button_restartserver.setOnClickListener(this);		
		
		
		console_button_local=(Button) findViewById(R.id.console_button_local);
		console_button_local.setOnClickListener(this);
		console_button_exit=(Button) findViewById(R.id.console_button_exit);
		console_button_exit.setOnClickListener(this);
		
	
		if(!m_App.gwservice_running)
		{
			Intent intent = new Intent();
			intent.setClass(this, Optometry_service.class);
			startService(intent);		
		}
        /*
		broadcastMain = new BroadcastMain();
		IntentFilter filter = new IntentFilter();
		filter.addAction( Optometry_service.BROADCASTACTION );
		registerReceiver( broadcastMain, filter );    
		*/   
        	
		m_App.gwConsole=this;
		UpdateAll();
   
	}
	
	/*
	 * ���շ��񷢳���Broadcast
	 * 
	 */
	/*
	public class BroadcastMain extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			int BroadcaseCmd = intent.getExtras().getInt("CMD");
			
			switch(BroadcaseCmd)
			{
			case Optometry_service.SERVICE_CMD_TIME:
				Message msg = LocalHandler.obtainMessage();
				msg.what = 01;
				LocalHandler.sendMessage( msg );
				break;
			
			}
		}
	}	
	*/
	
	public void Update_console()
	{
		Message msg = LocalHandler.obtainMessage();
		msg.what = 01;
		LocalHandler.sendMessage( msg );	
	}
	
	
	/*
	 * �����豸������״̬
	 * 
	 */
	private void UpdateAll()
	{
		console_button_device.setText(m_App.gwDeviceName);
		console_text_device_mac.setText(m_App.gwDeviceMac);
		
		if(m_App.gwDeviceName.length()==0)
			console_button_device.setText("               ");
		else
			console_button_device.setText(m_App.gwDeviceName);
		
		if(m_App.gwServerName.length()==0)
			console_button_servername.setText("               ");
		else
			console_button_servername.setText(m_App.gwServerName);
		
		if(m_App.gwServerIP.length()==0)
			console_button_serverip.setText("               ");
		else
			console_button_serverip.setText(m_App.gwServerIP);
		
		console_button_serverport.setText(String.valueOf(m_App.gwServerPort));
		console_button_gatawayid.setText(String.valueOf(m_App.gwID));
		
		if(m_App.gwName.length()==0)
			console_button_gatawayname.setText("               ");
		else
			console_button_gatawayname.setText(m_App.gwName);
		
		
		if(m_App.gwInfo.length()==0)
			console_button_gatewayinfo.setText("               ");
		else
			console_button_gatewayinfo.setText(m_App.gwInfo);		
	}
	
	

	/*
	 * ��������״̬
	 * 
	 */
	private void UpdateSetStatus(TextView view,int status)
	{
		switch(status)
		{
		case Comm_Base.STATE_NONE:
			view.setText("δ����");
			break;
		case Comm_Base.STATE_CONNECTING:
			view.setText("������");
			break;
		case Comm_Base.STATE_CONNECTED:
			view.setText("���ӳɹ�");
			break;
		case Comm_Base.STATE_CONFIG_ERROR:
			view.setText("���ô���");
			break;
		case Comm_Base.STATE_CONNECT_FALI:
			view.setText("����ʧ��");
			break;
		case Comm_Base.STATE_CONNECT_CLOSE:
			view.setText("���ӹر�");
			break;	
		case Comm_Base.STATE_CONNECT_NETWORK_CLOSE:
			view.setText("����δ����");
			break;				
		default:
			view.setText("���ô���");
			break;					
		}
		
	}
	
	/*
	 * ��������״̬
	 */
	private void UpdateServiceStatus()
	{
		UpdateSetStatus(console_text_device,m_App.gwManager.GetBTDeviceStatus());
		UpdateSetStatus(console_text_server,m_App.gwManager.GetRemoteServerStatus());
		
		switch(m_App.gwManager.GetDeviceCtrlStatus())
		{
		case 0:
			console_text_contrl.setText("δ����");
			break;
		case 1:
			console_text_contrl.setText("Զ�̿���");
			break;		
		case 2:
			console_text_contrl.setText("���ؿ���");
			break;						
		}
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		switch (arg0.getId()) {
		case R.id.console_button_device:
			CheckDevice();
			break;
		case R.id.console_button_servername:
			DialogInput("�������������",INPUTID_SERVERNAME);
			break;		
		case R.id.console_button_serverip:
			DialogInput("���������IP",INPUTID_SERVERIP);
			break;		
		case R.id.console_button_serverport:
			DialogInput("����������˿�",INPUTID_SERVERPORT);
			break;
		case R.id.console_button_gatawayid:
			DialogInput("��������ID",INPUTID_GATEWAYID);
			break;		
		case R.id.console_button_gatawayname:
			DialogInput("������������",INPUTID_GATEWAYNAME);
			break;	
		case R.id.console_button_gatewayinfo:
			DialogInput("����������Ϣ",INPUTID_GATEWAYINFO);
			break;
		case R.id.console_button_connectdevice:
			m_App.gwManager.ResetBt();
			m_App.gwManager.ConnectBTDevice();
			break;		
		case R.id.console_button_connectserver:
			m_App.gwManager.ResetServer();
			m_App.gwManager.ConnectServer();
			break;	
		case R.id.console_button_restartserver:
			m_App.gwManager.Reset();
			break;
		case R.id.console_button_local:
			ContrlDialog();
			break;
		case R.id.console_button_exit:
			ExitDialog();
			break;					
		}		
	}
	
	
	
	private void ContrlDialog()
	{
		 Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("��ʾ��Ϣ");
		 builder.setIcon(android.R.drawable.ic_menu_info_details);
		 builder.setMessage("��Ҫ����������⣬Զ����⽫���жϣ��Ƿ�ȷ��");
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
						Intent intent1=new Intent(Optometry_console.this,Optometry_contrl.class);
						startActivity(intent1);
				    	overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.fade_out);
					}
				});	
		 
		 builder.setCancelable(true);
		 builder.create().show();  		
	}
	
	private void ExitDialog()
	{
		 Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("��ʾ��Ϣ");
		 builder.setIcon(android.R.drawable.ic_menu_info_details);
		 builder.setMessage("�ò�����Ҫ�˳�ϵͳ����������");
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
						//unregisterReceiver(broadcastMain);
						Intent intent = new Intent();
			    		intent.setClass(Optometry_console.this,Optometry_service.class);
			    		stopService(intent);						
						
						m_App.gwManager.Stop();
						System.exit(0);
					}
				});	
		 
		 builder.setCancelable(true);
		 builder.create().show();  		
	}		
	
	 private void ToastMsg(String msg)
	 {
		 Toast tmsg = Toast.makeText(this, msg, Toast.LENGTH_LONG);     
		 tmsg.setGravity(Gravity.CENTER, tmsg.getXOffset() / 2, tmsg.getYOffset() / 2);       
		 tmsg.show(); 	
	 }	
	 
	
	Handler LocalHandler = new Handler() {  
        public void handleMessage(Message msg) {   
             switch (msg.what) {   
				case 01:
					UpdateServiceStatus();
					break;
                  case 10:   
                	  ToastMsg("���������ɹ�");  
                	  ConnectMsg.setText("���ӳɹ�");
                       break;   
                  case 11:   
                	  ToastMsg("�ر����������ɹ�");  
                	  ConnectMsg.setText("�رճɹ�");
                       break;  
                  case 12:   
                	  ToastMsg("��������ʧ��");  
                	  ConnectMsg.setText("����ʧ��");
                       break;                        
             }   
             super.handleMessage(msg);   
        }   
   };	
	

	@Override
	public void Comm_IF_Manager(int Command ) {
		// TODO Auto-generated method stub
		if(Command==10)
		{
            Message message = new Message();   
            message.what = Command;    
            this.LocalHandler.sendMessage(message);				
		}
		
		if(Command==11)
		{
            Message message = new Message();   
            message.what = Command;    
            this.LocalHandler.sendMessage(message);				
		}	
		
		if(Command==12)
		{
            Message message = new Message();   
            message.what = Command;    
            this.LocalHandler.sendMessage(message);				
		}			
	}
		

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
    	//if(arg0==btdevice_select)
    		//bt_selectdevice=bluedevice_name_list[arg2];	
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * ��������
	 * 
	 */
	private void BTDeviceSelect(int mDevice)
	{
		//BTDeviceSelect();
		String [] DeviceSelect=bluedevice_name_list[mDevice].split("\n");
		m_App.gwDeviceName=DeviceSelect[0];
		m_App.gwDeviceMac=DeviceSelect[1];
		
		m_App.gwSaveConfig();		
		UpdateAll();
		
	}
	
	/*
	 * ѡ�������豸
	 * 
	 */
	private void CheckDevice()
	{
		bluedevice_name_list=m_App.gwManager.GetBTDeviceList();
		new AlertDialog.Builder(this).setTitle("��ѡ��").setSingleChoiceItems(bluedevice_name_list, 0,	
				new DialogInterface.OnClickListener() {           
	     public void onClick(DialogInterface dialog, int which) {
	    	 {
	    		 BTDeviceSelect(which);
	    		 dialog.dismiss();
	    	 }
	     }
	  }
	)
	.setNegativeButton("ȡ��", null)
	.show();
	}
	
	/*
	 * IP��ַ�������
	 */
	
	int InputDialogID=-1;
	EditText InputText=null;
	
	private void InputSetIPAddress(EditText mInput)
	{
    	InputText.setKeyListener(new NumberKeyListener()
    	{
    		public int getInputType()
    		{
    			return android.text.InputType.TYPE_CLASS_NUMBER;  
    		}
    		
    		@Override
    		protected char[] getAcceptedChars()
    		{
    			char[] listChar={'0','1','2','3','4','5','6','7','8','9','.'};
    			return listChar;
    		}
    	});	
	}
	
	/*
	 * IP�˿��������
	 */
	private void InputSetIPPort(EditText mInput)
	{
    	InputText.setKeyListener(new NumberKeyListener()
    	{
    		public int getInputType()
    		{
    			return android.text.InputType.TYPE_CLASS_NUMBER;  
    		}
    		
    		@Override
    		protected char[] getAcceptedChars()
    		{
    			char[] listChar={'0','1','2','3','4','5','6','7','8','9'};
    			return listChar;
    		}
    	});	
	}	
	
	
	/*
	 * ��������Ի���
	 */
	
    private void DialogInput(String InputMsg,int Inputid)
    {
    	InputDialogID=Inputid;
    	InputText=new EditText(this);
    
    	if(Inputid==INPUTID_SERVERIP)
    		InputSetIPAddress(InputText);
    			
    	if(Inputid==INPUTID_SERVERPORT)
    		InputSetIPPort(InputText);
    	
    	if(Inputid==INPUTID_GATEWAYID)
    		InputSetIPPort(InputText); 	
    	
    	
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(InputMsg);
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setView(InputText);
		builder.setNegativeButton("ȷ��", 
				 new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DialogInputResult(InputText.getText().toString(),InputDialogID);
					}
				});
		 builder.setPositiveButton("����", 
				 new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});	
		 
		 builder.setCancelable(true);
		 builder.create().show();    		 
    }	
    
    /*
     * ��������������
     */
    private void DialogInputResult(String InputData,int Inputid)
    {
    	if(InputData.length()==0)
    		return;
    	
    	switch(Inputid)
    	{
    	case INPUTID_SERVERNAME:
    		m_App.gwServerName=InputData;
    		m_App.gwServerIP="";
    		break;
    	case INPUTID_SERVERIP:
    		m_App.gwServerName="";
    		m_App.gwServerIP=InputData;
    		break;
    	case INPUTID_SERVERPORT:
    		m_App.gwServerPort=Integer.parseInt(InputData);
    		break;
    	case INPUTID_GATEWAYID:
    		m_App.gwID=Integer.parseInt(InputData);
    		break;
    	case INPUTID_GATEWAYNAME:
    		m_App.gwName=InputData;
    		break;
    	case INPUTID_GATEWAYINFO:
    		m_App.gwInfo=InputData;
    		break;
    	}

    	m_App.gwSaveConfig();
    	
    	UpdateAll();
    	return;
    }
	
	

}
