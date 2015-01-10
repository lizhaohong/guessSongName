package com.example.moocmusicz.util;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.moocmusicz.data.Const;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

public class Util {
	//获取对应id的view，方便调用
	public static View getView(Context context ,int layoutId) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(layoutId, null);
		return view;
	}
	
	 public static void openNetworkSettings(Context context,final Activity activity) {      
	        AlertDialog dialog = new AlertDialog.Builder(context)  
	                .setTitle("开启网络服务")  
	                .setMessage("本软件需要使用网络资源，是否开启网络？")  
	                .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	                      
	                    @Override  
	                    public void onClick(DialogInterface dialog, int which) {  
	                        // Go to the activity of settings of wireless  
	                        Intent intent =  new Intent(Settings.ACTION_WIFI_SETTINGS);  
	                        activity.startActivity(intent);  
	                        dialog.cancel();  
	                    }  
	                })  
	                .setNegativeButton("否", new DialogInterface.OnClickListener() {  
	                      
	                    @Override  
	                    public void onClick(DialogInterface dialog, int which) {  
	                        dialog.cancel();  
	                    }  
	                })  
	                .show();          
	    } 
	 //判断网络是否连接
	 public static boolean isNetworkConnected(Context context) {
			if (context != null) {
				ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
				if (mNetworkInfo != null) {
					return mNetworkInfo.isAvailable();
				}
			}
			return false;
		}
	 //界面跳转
	 public static void startActivity(Context context,Class className) {
		 Intent intent = new Intent();
		 intent.setClass(context,className);
		 context.startActivity(intent);
		 
		 ((Activity)context).finish();
	 }
	 
	 public static void saveData(Context context, int stageIndex, int coins) {
		 FileOutputStream fOutputStream = null;
		 
		 try {
			 fOutputStream = context.openFileOutput(Const.DATA_SAVE_FILE, Context.MODE_PRIVATE);
			
			DataOutputStream dataOutputStream = new DataOutputStream(fOutputStream);
			dataOutputStream.writeInt(stageIndex-1);
			dataOutputStream.writeInt(coins);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fOutputStream != null) {
				try {
					fOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	 }
	 
	 public static int[] loadData(Context context) {
		 FileInputStream fileInputStream = null;
		  
		 int data[] = new int [2];
	
		 try {
			fileInputStream = context.openFileInput(Const.DATA_SAVE_FILE);
			
			DataInputStream dataInputStream = new DataInputStream(fileInputStream);
			
			data[Const.DATA_FOR_STAGE_INDEX] = dataInputStream.readInt();
			data[Const.DATA_FOR_COIN_NUMBER] = dataInputStream.readInt();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fileInputStream!=null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		 return data;
	 }
}
