package com.app.aztask.ui;

import com.app.aztask.R;
import com.app.aztask.TabsAdapter;
import com.app.aztask.data.User;
import com.app.aztask.net.CheckUserRegisterationWorker;
import com.app.aztask.util.Util;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

	private static String CLASS_NAME="MainActivity";
	private static Context appContext;
	private static int USER_ID;
	private static User loggedInUser;
	
    String PROJECT_NUMBER = "155962838252";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 90000;
//    private BroadcastReceiver mRegistrationBroadcastReceiver;
    
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appContext = getApplicationContext();

 
		LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		if ( !lm.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	        buildAlertMessageNoGps();
	    }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("NearBy Tasks"));
        tabLayout.addTab(tabLayout.newTab().setText("My Tasks"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
 
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final TabsAdapter adapter = new TabsAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
 
            }
 
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
 
            }
        });

		
		loggedInUser=isUserRegistered();
		USER_ID=(loggedInUser!=null ) ? loggedInUser.getUserId() : 0;
		
	}
	

	
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.w("MainActivity", "onResume");
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
//    }
//
//
//    //Unregistering receiver on activity paused 
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.w("MainActivity", "onPause");
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//    }
    

	@Override
	protected void onRestart() {
		super.onRestart();
//		    FragmentManager fm = getFragmentManager();
//		    FragmentTransaction ft = fm.beginTransaction();
//		    NearByTasksTab llf = new NearByTasksTab(this);
//		    ft.replace(R.id.nearbytasks,llf);
//		    ft.commit();
	}
	
	private User isUserRegistered() {
		String deviceId=Util.getDeviceId();
		try {
			User user=new CheckUserRegisterationWorker().execute(deviceId).get();
			Log.i(CLASS_NAME,"User is registered:"+user.getDeviceInfo().getDeviceId());
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public static Context getAppContext() {
        return appContext;
    }
	
	public static int getUserId(){
		return USER_ID;
	}

	public static boolean userRegistered(){
		return (USER_ID>0) ? true : false;
	}

	public static User getRegisteredUser(){
		return loggedInUser;
	}
	
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	 
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        int id = item.getItemId();
	        if (id == R.id.action_settings) {
	            return true;
	        }
	 
	        return super.onOptionsItemSelected(item);
	    }
	    
		  public void buildAlertMessageNoGps() {
			    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
			           .setCancelable(false)
			           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			               public void onClick(final DialogInterface dialog,final int id) {
			            	   Intent intent=new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			            	   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			                   appContext.startActivity(intent);
			               }
			           })
			           .setNegativeButton("No", new DialogInterface.OnClickListener() {
			               public void onClick(final DialogInterface dialog,final int id) {
			                    dialog.cancel();
			               }
			           });
			    final AlertDialog alert = builder.create();
			    alert.show();
			}


}
