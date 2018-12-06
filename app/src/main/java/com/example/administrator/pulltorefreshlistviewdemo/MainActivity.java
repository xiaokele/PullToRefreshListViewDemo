package com.example.administrator.pulltorefreshlistviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void headerAndFooterRefresh(View view) {
        startActivity(new Intent(this, HeaderAndFooterListViewActivity.class));
    }

    public void moreTypeRefresh(View view) {
        startActivity(new Intent(this, MoreTypeListViewActivity.class));
    }

    public void swipeRefresh(View view) {
        startActivity(new Intent(this, SwipeRefreshListViewActivity.class));
    }

    public void moreTypeRefresh2(View view) {
        startActivity(new Intent(this, MoreTypeListView2Activity.class));
    }
}
