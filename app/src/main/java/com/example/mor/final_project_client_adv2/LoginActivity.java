package com.example.mor.final_project_client_adv2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * login activity
 * class to login to the servers
 */
public class LoginActivity extends ActionBarActivity {
    //member
    AccountManager accountManager;
    private Account[] accounts;
    Spinner spinner;
    Account account;
    ServerInfo si;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountManager = AccountManager.get(getApplicationContext());
        accounts = accountManager.getAccountsByType("com.google");

        final ArrayList<String> accountList = new ArrayList<String>();
        for (Account account : accounts) {
            accountList.add(account.name);
        }
        //spiner to choose the server
        spinner = (Spinner) findViewById(R.id.act_login_spinner_account);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(dataAdapter);
        //login button
        ImageButton startAuth = (ImageButton) findViewById(R.id.act_login_send_btn);
        startAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner = (Spinner) findViewById(R.id.act_login_spinner_account);
                account = accounts[spinner.getSelectedItemPosition()];
                String name = account.name;
                name = name.split("@")[0];
                Toast toast = Toast.makeText(getApplicationContext(),
                        "nickname = " + name,  Toast.LENGTH_SHORT);
                toast.show();
                // save the current nickname in the Shared Preferences
                SharedPreferences  myAccount= getSharedPreferences("MyAccount", MODE_PRIVATE);
                SharedPreferences.Editor editor = myAccount.edit();
                editor.putString("nickname", name);
                editor.commit();
                accountManager.getAuthToken(account, "ah", null, false, new OnTokenAcquired(LoginActivity.this), null);

            }
        });
        ActionBar mAction = getSupportActionBar();
        mAction.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
