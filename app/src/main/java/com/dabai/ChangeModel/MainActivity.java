package com.dabai.ChangeModel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.*;
import android.widget.*;
import android.support.design.widget.*;
import android.os.*;
import android.view.*;
import android.support.v7.app.*;
import android.content.*;
import org.apache.commons.codec.binary.*;
import java.io.*;
import android.view.inputmethod.*;

public class MainActivity extends AppCompatActivity {

	TextView info;
	EditText m1,m2,m3,m4,m5;
	String model,brand,manufacturer,product,device;
	
	String b1,b2,b3,b4,b5;
	String mDerive;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    
		info = (TextView)findViewById(R.id.info);
		
		m1 = (EditText)findViewById(R.id.main_activityEditText);
		m2 = (EditText)findViewById(R.id.main_activityEditText2);
		m3 = (EditText)findViewById(R.id.main_activityEditText3);
		m4 = (EditText)findViewById(R.id.main_activityEditText4);
		m5 = (EditText)findViewById(R.id.main_activityEditText5);
		
		
		model = Build.MODEL;
		brand = Build.BRAND;
		manufacturer = Build.MANUFACTURER;
		product = Build.PRODUCT;
		device = Build.DEVICE;
		
		getModel();
		
			}
	
			
	//菜单 
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.reboot:
			
				Snackbar.make(getWindow().getDecorView(),"重启?", Snackbar.LENGTH_LONG).setAction("嗯呢", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String a[]={"reboot"};
							new shell().execCommand(a,true);
							
						}
					}).show();	
					
					break;
        }
        return super.onOptionsItemSelected(item);
    }		
			
			
	//获取机型
	public void getModel(){
	
		info.setText("model: "+model+"\nbrand: "+brand+"\nmanufacturer: "+manufacturer+"\nproduct: "+product+"\ndevice: "+device);
		m1.setText(model);
		m2.setText(brand);
		m3.setText(manufacturer);
		m4.setText(product);
		m5.setText(device);
		
	}
	
	

	//恢复
	public void Recover(View v){
		snack("下个版本就添加了,真的不骗人(有空就加...有空就加...)");
	}
	
	
	//导入
	public void Import(View v){
		
		final EditText et = new EditText(this); 
		new AlertDialog.Builder(this)
			.setTitle("导入机型编码")
			.setView(et)
			.setPositiveButton("导入",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
					
						if(et.getText().toString().length()<30){
						snack("你输入的不是正确的机型代码!");
					}
					else{
					
						try{
					Base64 base = new Base64();
					mDerive = base.decode(et.getText().toString());
					String a[] = mDerive.split("@");
					m1.setText(a[0]);
					m2.setText(a[1]);
					m3.setText(a[2]);
					m4.setText(a[3]);
					m5.setText(a[4]);
					}catch(Exception e){
						snack("机型代码包含错误的信息");
					}
					}
						
				}
			}) 
			.setNeutralButton("剪切板导入", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					//判断包含@不
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
					
					ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					// 将文本内容放到系统剪贴板里。
					
					if(cm.getText().toString().length()>10 && cm.getText().toString().contains("@")){
						snack("你输入的不是正确的机型代码!");
					}
					else{
						try{
						Base64 base = new Base64();
						mDerive = base.decode(cm.getText().toString());
						String a[] = mDerive.split("@");
						m1.setText(a[0]);
						m2.setText(a[1]);
						m3.setText(a[2]);
						m4.setText(a[3]);
						m5.setText(a[4]);
						}catch(Exception e){
							snack("机型代码包含错误的信息");
						}
					}		
				}
			})
			.show();
		
		
	}
	
	//导出
	public void Derive(View v){
		
		String base64String=
		m1.getText().toString()+"@"
		+m2.getText().toString()+"@"
		+m3.getText().toString()+"@"
		+m4.getText().toString()+"@"
		+m5.getText().toString();
		
		Base64 base = new Base64();
		mDerive=base.encode(base64String);
		
		Snackbar.make(getWindow().getDecorView(),mDerive, Snackbar.LENGTH_LONG).setAction("复制", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					// 将文本内容放到系统剪贴板里。
					cm.setText(mDerive);
					snack("复制成功");			
				}
			}).show();	
	}
	
	
	
	public void Modify_magisk(View v){
		//magisk模块模式
		b1 = m1.getText().toString();
		b2 = m2.getText().toString();
		b3 = m3.getText().toString();
		b4 = m4.getText().toString();
		b5 = m5.getText().toString();
		
		new AlertDialog.Builder(this)
			.setTitle("magisk模式")
			.setMessage("magisk模块挂载机型信息即将更改为\nmodel:"+b1+"\nbrand: "+b2+"\nmanufacturer: "+b3+"\nproduct: "+b4+"\ndevice: "+b5+"\n\n需要注意的是,magisk模块方式修改的机型不会创建备份\n把模块关了就会恢复")
			.setPositiveButton("确认更改",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					changeModel(b1,b2,b3,b4,b5);
				}
			}) 
			.setNeutralButton("取消", null)
			.show();	
			
	}
	

	public void Modify(View v){
		//root模式
		b1 = m1.getText().toString();
		b2 = m2.getText().toString();
		b3 = m3.getText().toString();
		b4 = m4.getText().toString();
		b5 = m5.getText().toString();
		
		
		new AlertDialog.Builder(this)
			.setTitle("root模式")
			.setMessage("机型信息即将更改为\nmodel:"+b1+"\nbrand: "+b2+"\nmanufacturer: "+b3+"\nproduct: "+b4+"\ndevice: "+b5+"\n\n本次备份文件即将保存在\n/data/ChangeModel/"+model.replace(" ","")+".prop.bak")
			.setPositiveButton("确认更改",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					changeModel(b1,b2,b3,b4,b5);
				}
			}) 
			.setNeutralButton("取消", null)
			.show();	
		
	}
	
	
	
	
	
	public void snack(String a){
		Snackbar.make(getWindow().getDecorView(),a,Snackbar.LENGTH_LONG).show();
	}
	
	
	//更改magisk挂载型号
	public void changeModel_magisk(final String mmodel,final String mbrand,final String mmanufacturer,final String mproduct,final String mdevice)
	{

		new Thread() {
			@Override
			public void run()
			{
				super.run();
				//新线程操作
				String mode[]={"mount -o rw,remount /system"
					,"cp /magisk/changemodel/system.prop /data/system.prop"
					,"chmod 0644 /data/system.prop"
					,"sed -i 's/^ro.product.brand=.*/ro.product.brand="+mbrand+"/' /data/system.prop"
					,"sed -i 's/^ro.product.model=.*/ro.product.model="+mmodel+"/' /data/system.prop"
					,"sed -i 's/^ro.product.manufacturer=.*/ro.product.manufacturer="+mmanufacturer+"/' /data/system.prop"
					,"sed -i 's/^ro.product.device=.*/ro.product.device="+mdevice+"/' /data/system.prop"
					,"sed -i 's/^ro.build.product=.*/ro.build.product="+mproduct+"/' /data/system.prop"
					,"cp /data/system.prop /magisk/changemodel/system.prop"};

				new shell().execCommand(mode,true);
			
				runOnUiThread(new Runnable(){
						@Override
						public void run() {
							//更新UI操作
							snack("更改完成,重启生效");
						}

					});
			}
		}.start(); 

	}
	
	
	//改机型
	public void changeModel(final String mmodel,final String mbrand,final String mmanufacturer,final String mproduct,final String mdevice)
	{

		new Thread() {
			@Override
			public void run()
			{
				super.run();
				//新线程操作

				String mode[]={"mount -o rw,remount /system"
					,"mkdir /data/ChangeModel"
					,"cp /system/build.prop /data/ChangeModel/"+model.replace(" ","")+".prop.bak"
					,"cp /system/build.prop /data/build.prop"
					,"chmod 0644 /data/build.prop"
					,"sed -i 's/^ro.product.brand=.*/ro.product.brand="+mbrand+"/' /data/build.prop"
					,"sed -i 's/^ro.product.model=.*/ro.product.model="+mmodel+"/' /data/build.prop"
					,"sed -i 's/^ro.product.manufacturer=.*/ro.product.manufacturer="+mmanufacturer+"/' /data/build.prop"
					,"sed -i 's/^ro.product.device=.*/ro.product.device="+mdevice+"/' /data/build.prop"
					,"sed -i 's/^ro.build.product=.*/ro.build.product="+mproduct+"/' /data/build.prop"
					,"cp /data/build.prop /system/build.prop"};

				new shell().execCommand(mode,true);
				
				runOnUiThread(new Runnable(){
						@Override
						public void run() {
							//更新UI操作
							snack("更改完成,重启生效");
						}

					});
				
			}
		}.start(); 

	}
	
}
