package com.iwind.red_apple.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iwind.red_apple.Constant.ConstantString;
import com.iwind.red_apple.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：HuGuoJun
 * 2016/6/7 10:21
 * 邮箱：www.guojunkuaile@qq.com
 */
public class TaxKnowAdapter extends BaseAdapter {

    private Context mContext;
    private List<HashMap<String, String>> mList;

    public TaxKnowAdapter(Context mContext, List<HashMap<String, String>> mapList) {
        this.mContext = mContext;
        this.mList = mapList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tax_know, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_content.setText(Html.fromHtml(mList.get(position).get(ConstantString.PROBLEM_CONTENT)));
        holder.tv_read_count.setText(mList.get(position).get(ConstantString.ZANCOUTN));
        holder.tv_type.setText(mList.get(position).get(ConstantString.HLABEL) + "-" + mList.get
                (position).get(ConstantString.TAX_TYPE));
        holder.tv_title.setText(mList.get(position).get(ConstantString.PROBLEM_TITLE));
        return convertView;
    }


    private class ViewHolder {
        @ViewInject(R.id.tv_content)
        TextView tv_content;
        @ViewInject(R.id.tv_read_count)
        TextView tv_read_count;
        @ViewInject(R.id.tv_type)
        TextView tv_type;
        @ViewInject(R.id.tv_title)
        TextView tv_title;
    }
}
