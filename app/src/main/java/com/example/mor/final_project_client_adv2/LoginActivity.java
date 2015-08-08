package com.example.mor.final_project_client_adv2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
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

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;


public class LoginActivity extends ActionBarActivity {

    AccountManager accountManager;
    private Account[] accounts;
    Spinner spinner;
    DefaultHttpClient httpClient = new DefaultHttpClient();
    Account account;
    ServerInfo si;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountManager = AccountManager.get(getApplicationContext());
        accounts = accountManager.getAccountsByType("com.google");

        ArrayList<String> accountList = new ArrayList<String>();
        for (Account account : accounts) {
            accountList.add(account.name);
        }

        spinner = (Spinner) findViewById(R.id.act_login_spinner_account);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(dataAdapter);

        ImageButton startAuth = (ImageButton) findViewById(R.id.act_login_send_btn);
        startAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner = (Spinner) findViewById(R.id.act_login_spinner_account);
                account = accounts[spinner.getSelectedItemPosition()];

                accountManager.getAuthToken(account, "ah", null, false, new OnTokenAcquired(httpClient, LoginActivity.this), null);
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
    public HttpClient getClient () {
        return this.httpClient;
    }
}
