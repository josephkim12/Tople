package com.example.project_4t_tople.Response;

import com.example.project_4t_tople.adapter.Detail_MemberAdapter;
import com.example.project_4t_tople.model.Detail_Todo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Detail_MemberResponse extends AsyncHttpResponseHandler {
    Detail_MemberAdapter adapter;

    public Detail_MemberResponse(Detail_MemberAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onStart() {
    }

    // 통신 성공시
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        // 통신 데이터 처리
        String content = new String(responseBody);
        try {
            JSONObject json = new JSONObject(content);
            JSONArray items = json.getJSONArray("items");
//            검색 결과 처리
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonObject = items.getJSONObject(i);
                Detail_Todo detail_todo = new Detail_Todo();

                if (jsonObject.getString("todo").equals("")) {
                    detail_todo.setTodo("지정된게 없습니다.");
                } else {
                    detail_todo.setTodo(jsonObject.getString("todo"));
                }
                if (jsonObject.getString("ex").equals("")) {
                    detail_todo.setTodo("지정된게 없습니다.");
                } else {
                    detail_todo.setEx(jsonObject.getString("ex"));
                }
                detail_todo.setDetail_item_img(jsonObject.getString("prof"));
                detail_todo.setIspay(jsonObject.getBoolean("ispay"));
                detail_todo.setId(jsonObject.getString("id"));
//                중복 체크 확인할 것
                adapter.add(detail_todo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 통신 실패시
    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
    }
}
