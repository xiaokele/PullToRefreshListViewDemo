package com.example.administrator.pulltorefreshlistviewdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * swiperefresh实现的下拉
 */
public class SwipeRefreshListViewActivity extends AppCompatActivity {

    private SwipeRefreshLayout srl;

    private List<String> listDatas = new ArrayList<>();
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_swiperefresh);

        srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listDatas.clear();
                        for (int i = 0; i < 10; i++) {
                            listDatas.add("可乐-刷新" + i);
                        }
                        srl.setRefreshing(false);
                        listAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });

        for (int i = 0; i < 20; i++) {
            listDatas.add("可乐" + i);
        }

        ListView lsitview = findViewById(R.id.lv);
        listAdapter = new ListAdapter(this, listDatas);
        lsitview.setAdapter(listAdapter);
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
