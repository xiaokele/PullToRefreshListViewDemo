package com.example.administrator.pulltorefreshlistviewdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> listDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PullToRefreshListView lsitview = findViewById(R.id.listview);

        for (int i = 0; i < 10; i++) {
            listDatas.add("可乐" + i);
        }

        final ListAdapter listAdapter = new ListAdapter(this, listDatas);
        lsitview.setAdapter(listAdapter);
        lsitview.setOnRefreshingListener(new PullToRefreshListView.OnRefreshingListener() {
            @Override
            public void onRefreshing() {
                lsitview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lsitview.onRefreshComplete();
                        listDatas.clear();
                        for (int i = 0; i < 10; i++) {
                            listDatas.add("橙汁" + i);
                        }
                        listAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                lsitview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lsitview.onLoadmoreComplete();
                        for (int i = 0; i < 10; i++) {
                            listDatas.add("雪碧" + i);
                        }
                        listAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
    }

    class ListAdapter extends BaseAdapter {
        private List<String> datas;
        private Context mContext;

        public ListAdapter(Context context, List<String> datas) {
            this.datas = datas;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_listview, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvName.setText(datas.get(position));
            return convertView;
        }
    }

    class ViewHolder {

        private TextView tvName;

        public ViewHolder(View rootView) {
            tvName = rootView.findViewById(R.id.tv_name);
        }
    }

}
