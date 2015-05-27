package com.test.keepalive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG="Main";
    private Intent commIntent;
    private MsgReceiver msgReceiver;
    private IntentFilter filter;
    private Button btnSave;
    private Button btnLogin;
    private Button btnLogout;
    private EditText txtUserName;
    private EditText txtPassword;
    private TextView txtMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        loadPrefs();
        msgReceiver=new MsgReceiver();
        filter=new IntentFilter();
        filter.addAction(getString(R.string.actionFilter));

        commIntent=new Intent(getString(R.string.actionFilter));
    }
    @Override
    public void onResume(){
        super.onResume();
        registerReceiver(msgReceiver, filter);
    }
    @Override
    public void onPause(){
        unregisterReceiver(msgReceiver);
        super.onPause();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                savePrefs();
                txtMessage.setText("保存成功");
                break;
            case R.id.btn_login:
              if(!Utils.isWIFI(this)){
                  txtMessage.setText("请打开WIFI");
                  return;
              }if(!Utils.isECNU(this)){
                txtMessage.setText("请连接ECNU");
                return;
            }
                Intent it=new Intent(this,LoginService.class);
                it.putExtra(getString(R.string.stateFlag),true);
                startService(it);
            break;
            case R.id.btn_logout:
                if(Utils.isWIFI(this)&&Utils.isECNU(this)){
                    Log.d(TAG,"logout...");
                    Intent intent=new Intent(this,LoginService.class);
                    intent.putExtra(getString(R.string.stateFlag),false);
                    startService(intent);
                }
                break;
        }
    }
    private void findView()
    {
        txtUserName=(EditText)findViewById(R.id.txt_username);
        txtPassword=(EditText)findViewById(R.id.txt_password);
        txtMessage=(TextView)findViewById(R.id.txt_message);
        btnSave=(Button)findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        btnLogin=(Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnLogout=(Button)findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
    }
    private void savePrefs()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefName),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString(getString(R.string.username),txtUserName.getText().toString() );
        editor.putString(getString(R.string.password),txtPassword.getText().toString() );
        editor.commit();
    }
    private void loadPrefs()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefName), Context.MODE_PRIVATE);
        txtUserName.setText(sharedPref.getString(getString(R.string.username),""));
        txtPassword.setText(sharedPref.getString(getString(R.string.password),""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent= new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(getString(R.string.action));
            boolean isSuccess=intent.getBooleanExtra(getString(R.string.isSuccess),false);
            msg+=isSuccess?"成功":"失败";
            Log.d(TAG,msg);
            txtMessage.setText(msg);
        }

    }
}
