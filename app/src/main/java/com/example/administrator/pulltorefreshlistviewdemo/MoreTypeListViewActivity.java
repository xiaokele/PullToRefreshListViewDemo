package com.example.administrator.pulltorefreshlistviewdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据类型添加header和footer
 */
public class MoreTypeListViewActivity extends AppCompatActivity {

    private List<String> listDatas = new ArrayList<>();
    private LoadmoreHoldle mLoadmoreHoldle;
    private ListAdapter listAdapter;
    private ListView listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_type);

        listview = findViewById(R.id.listview);

        for (int i = 0; i < 20; i++) {
            listDatas.add("可乐" + i);
        }

        listAdapter = new ListAdapter(this, listDatas);
        listview.setAdapter(listAdapter);
    }

    class ListAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mDatas;
        private View mHoldleView;

        public ListAdapter(Context context, List<String> datas) {
            this.mContext = context;
            this.mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas.size()+1;
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getCount() - 1) {
                return 0;
            } else {
                return addViewType(position); //构造一个方法出来，方便子类修改，添加更多的样式
            }
        }

        public int addViewType(int position) {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BaseHoldle holdle;
            if (convertView == null) {
                if (getItemViewType(position) == 0) {  //type为0 表示应该加载加载更多的视图
                    holdle = getLoadmoreHoldle();
                } else {                               //否则为普通视图
                    holdle = getSpecialBaseHoldle(position);
                }
            } else {
                holdle = (BaseHoldle) convertView.getTag();
            }

            if (getItemViewType(position) == 0) {      //加载更多视图，请求网络获取数据
                if (havemore()) {
                    holdle.setDataAndRefreshHoldleView(LoadmoreHoldle.LOADMORE_LODING);
                    triggleLoadMoreData();
                } else {
                    holdle.setDataAndRefreshHoldleView(LoadmoreHoldle.LOADMORE_NONE);
                }
            } else {                                  //普通视图视图，请求网络获取数据
                String data = listDatas.get(position);
                holdle.setDataAndRefreshHoldleView(data);
            }

            mHoldleView = holdle.mHoldleView;
            mHoldleView.setScaleX(0.6f);
            mHoldleView.setScaleY(0.6f);
            ViewCompat.animate(mHoldleView).scaleX(1).scaleY(1).setDuration(400).setInterpolator(new OvershootInterpolator(4)).start();
            return mHoldleView;
        }
    }

    private void triggleLoadMoreData() {

        listview.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    listDatas.add("可乐-加载更多的" + i);
                }
                listAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    private boolean havemore() {
        return listDatas.size() < 100;
    }

    private BaseHoldle getLoadmoreHoldle() {
        if (mLoadmoreHoldle == null) {
            mLoadmoreHoldle = new LoadmoreHoldle();
        }
        return mLoadmoreHoldle;
    }

    private BaseHoldle getSpecialBaseHoldle(int position) {
        return new NormalHolde(position);
    }

    public class NormalHolde extends BaseHoldle {

        private TextView tvName;

        public NormalHolde(int position) {
        }

        @Override
        public void refreshHoldleView(Object data) {
            tvName.setText((String) data);
        }

        @Override
        public View ininViewHoldle() {
            View view = View.inflate(MoreTypeListViewActivity.this.getApplicationContext(), R.layout.item_listview, null);
            tvName = view.findViewById(R.id.tv_name);
            return view;
        }
    }

    public class LoadmoreHoldle extends BaseHoldle {

        public static final int LOADMORE_LODING = 0;
        public static final int LOADMORE_ERROR = 1;
        public static final int LOADMORE_NONE = 2;
        private int mCurretState;
        private View view;
        private TextView tvNotice;
        private View progressbar;
        private View iv;

        @Override
        public void refreshHoldleView(Object data) {
            mCurretState = (int) data;
            switch (mCurretState) {
                case LOADMORE_LODING:
                    view.setVisibility(View.VISIBLE);
                    break;
                case LOADMORE_ERROR:
                    view.setVisibility(View.GONE);
                    break;
                case LOADMORE_NONE:
                    view.setVisibility(View.VISIBLE);
                    iv.setVisibility(View.GONE);
                    progressbar.setVisibility(View.GONE);
                    tvNotice.setText("--我是有底线的哈--");
                    break;
            }
        }

        @Override
        public View ininViewHoldle() {
            view = View.inflate(MoreTypeListViewActivity.this.getApplicationContext(), R.layout.refresh_footer, null);
            progressbar = view.findViewById(R.id.pull_to_load_progress);
            iv = view.findViewById(R.id.iv_loadmore_up);
            tvNotice = view.findViewById(R.id.pull_to_load_text);
            return view;
        }
    }

    //holder基类，提取公共的方法
    public abstract class BaseHoldle<T> {
        public View mHoldleView;

        public T mdata;

        public BaseHoldle() {
            mHoldleView = ininViewHoldle();
            mHoldleView.setTag(this);
        }

        public void setDataAndRefreshHoldleView(T mdata) {
            this.mdata = mdata;
            refreshHoldleView(mdata);
        }

        public abstract void refreshHoldleView(T data);

        public abstract View ininViewHoldle();

    }

}
