package com.iwind.red_apple.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.easemob.easeui.ui.EaseBaseActivity;
import com.easemob.easeui.utils.ResponseUtils;
import com.easemob.easeui.widget.EaseTitleBar;
import com.easemob.easeui.widget.xlistview.XListView;
import com.iwind.red_apple.Adapter.MyAnswerAdapter;
import com.iwind.red_apple.App.MyApplication;
import com.iwind.red_apple.Constant.ConstantString;
import com.iwind.red_apple.Constant.ConstantUrl;
import com.iwind.red_apple.R;
import com.iwind.red_apple.Tax.AnswerDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：HuGuoJun
 * 2016/6/13 14:39
 * 邮箱：www.guojunkuaile@qq.com
 */
@ContentView(R.layout.act_myanswer)
public class MyAnswerActivity extends EaseBaseActivity {

    @ViewInject(R.id.title_bar)
    EaseTitleBar title_bar;
    @ViewInject(R.id.lv_myanswer)
    XListView lv_myanswer;
    private List<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private MyAnswerAdapter mMyAnswerAdapter;
    int page = 1;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        x.view().inject(this);
        InitView();
        ShowLoadingDialog();
        InitData();
        setOnClickListener();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void InitView() {
        title_bar.setLeftImageResource(R.drawable.ease_mm_title_back);
        title_bar.setTitle(getResources().getString(R.string.my_answer));
        lv_myanswer.setPullLoadEnable(true);
        lv_myanswer.setPullRefreshEnable(true);
    }

    @Override
    public void InitData() {
        RequestParams params = new RequestParams(ConstantUrl.BASE_URL + ConstantUrl
                .GET_MINE_ANSWER);
        params.addBodyParameter(ConstantString.USER_ID, MyApplication.getInstance().getUserid());
        params.addBodyParameter(ConstantString.TOKEN, MyApplication.getInstance().getToken());
        params.addBodyParameter(ConstantString.PAGE, page + "");
        params.addBodyParameter(ConstantString.ROWS, ConstantString.ROWCOUNT);
        Log(ConstantUrl.BASE_URL + ConstantUrl.GET_MINE_ANSWER + "?" + ConstantString.USER_ID +
                "=" + MyApplication.getInstance().getUserid() + "&" + ConstantString.TOKEN + "="
                + MyApplication.getInstance().getToken() + "&" + ConstantString.PAGE + "=" + page
                + "&" +
                ConstantString.ROWS + "=10");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                lv_myanswer.stopRefresh();
                lv_myanswer.stopLoadMore();
                Log(result);
                CloseLoadingDialog();
                if (ResponseUtils.isSuccess(context, ConstantString.RESULT_STATE, result,
                        ConstantString.STATE,
                        ConstantString.RESULT_INFO)) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray(ConstantString.ARRAY);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put(ConstantString.FORUMMESSAGEID, jsonArray.getJSONObject(i).getString
                                    (ConstantString.FORUMMESSAGEID));
                            hashMap.put(ConstantString.FORUM_TITLE, ResponseUtils.ParaseNull(jsonArray.getJSONObject
                                    (i).getString(ConstantString.FORUM_TITLE)));
                            hashMap.put(ConstantString.FORUMMESSAGE, ResponseUtils.ParaseNull(jsonArray.getJSONObject
                                    (i).getString(ConstantString.FORUMMESSAGE)));
                            hashMap.put(ConstantString.USER_PIC, ResponseUtils.ParaseNull(jsonArray.getJSONObject(i)
                                    .getString(ConstantString.USER_PIC)));
                            hashMap.put(ConstantString.NICK_NAME, ResponseUtils.ParaseNull(jsonArray.getJSONObject(i)
                                    .getString(ConstantString.NICK_NAME)));
                            hashMap.put(ConstantString.USER_PHONE, jsonArray.getJSONObject(i).getString
                                    (ConstantString.USER_PHONE));
                            hashMap.put(ConstantString.ZANCOUTN, ResponseUtils.ParaseNull(jsonArray.getJSONObject(i)
                                    .getString(ConstantString.ZANCOUTN)).equals("") ? "0" : ResponseUtils.ParaseNull
                                    (jsonArray.getJSONObject(i).getString(ConstantString.ZANCOUTN)));
                            mList.add(hashMap);
                        }
                        mMyAnswerAdapter = new MyAnswerAdapter(context, mList);
                        lv_myanswer.setAdapter(mMyAnswerAdapter);
                        mMyAnswerAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void setOnClickListener() {
        title_bar.setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_myanswer.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page =1;
                mList.clear();
                InitData();
            }

            @Override
            public void onLoadMore() {
                page++;
                InitData();
            }
        });
        lv_myanswer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(context, AnswerDetailActivity.class).putExtra(ConstantString.FORUMMESSAGEID,
                        mList.get(position - 1).get(ConstantString.FORUMMESSAGEID)).putExtra(ConstantString
                        .FORUM_TITLE, mList.get(position - 1).get(ConstantString.FORUM_TITLE)));
            }
        });
    }
}
