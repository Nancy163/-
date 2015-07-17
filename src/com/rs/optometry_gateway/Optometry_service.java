package com.rs.optometry_gateway;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


/*
 * ��̨�������
 */

public class Optometry_service extends Service {
	
	private static final String TAG = "optometry Service";  
	public static final String BROADCASTACTION = "com.rs.optometry_gateway";
	
	public static final int SERVICE_CMD_TIME=1;
	
	private Optometry_gateway m_App;
	
	Timer timer;
	int Counter=0;
	
	public Optometry_service() {
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	public void onCreate() {
		Log.v(TAG, "onCreate");
		Toast.makeText(this, "��غ͹����������", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		Log.v(TAG, "onDestroy");
		Toast.makeText(this, "��غ͹������ر�", Toast.LENGTH_SHORT);
	}	
	
	void ServiceTask_UpdateActive1()
	{
		// ��ʱ����
		// ���͹㲥
		//Intent intent = new Intent();
		//intent.setAction( BROADCASTACTION );
		//intent.putExtra( "CMD", SERVICE_CMD_TIME );
		//sendBroadcast( intent );				
		
	}
	
	void ServiceTask_UpdateActive()
	{
		// ��ʱ����
		// ���͹㲥
		if(m_App.gwConsole!=null)
			m_App.gwConsole.Update_console();
		
	}	
	
	
	long ServiceTaskCouter=0;
	
	void ServiceTask()
	{
		
		m_App.gwservice_running=true;
		ServiceTaskCouter++;
		/*
		 * 100������ݷ������
		 */
		m_App.gwManager.CheckSend();
		
		/*
		 * 500ms����״̬
		 */
		if(ServiceTaskCouter%5==1)
		{
			ServiceTask_UpdateActive();
		}
		
		
		/*
		 * 5000ms������ӹ���
		 */
		if(ServiceTaskCouter%50==1)
		{
			m_App.gwManager.CheckConnect();
		}
		
		/*
		 * 1000ms����������
		 */
		if(ServiceTaskCouter%10==1)
		{
			m_App.gwManager.KeepAlive();
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		
		m_App=(Optometry_gateway) getApplication();	
		m_App.gwLoadConfig();		
		m_App.gwManager.Start();
		timer = new Timer();	
		timer.schedule( new TimerTask()
		{

			@Override
			public void run()
			{
				ServiceTask();
			}
		}, 1000, 100 );

		return super.onStartCommand( intent, flags, startId );
	}	
	
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.v(TAG, "onStart");
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				int op = bundle.getInt("op");
				switch (op) {
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				}
			}
		}
	}	
	
	
	
}
