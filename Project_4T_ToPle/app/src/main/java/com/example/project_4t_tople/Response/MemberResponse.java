package com.example.project_4t_tople.Response;

import com.example.project_4t_tople.adapter.MemberAdapter;
import com.example.project_4t_tople.model.TopleModel;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MemberResponse extends AsyncHttpResponseHandler {
    MemberAdapter adapter;

    public MemberResponse(MemberAdapter adapter) {
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
            JSONArray users = json.getJSONArray("users");
//            검색 결과 처리
            for (int i = 0; i < users.length(); i++) {
                JSONObject jsonObject = users.getJSONObject(i);
                JSONObject properties = jsonObject.getJSONObject("properties");

                TopleModel memberTest = new TopleModel();

                if (properties.has("update_prof")) {
                    memberTest.setThumb(properties.getString("update_prof"));
                } else if (properties.has("thumbnail_image")) {
                    memberTest.setThumb(properties.getString("thumbnail_image"));
                } else {
                    memberTest.setThumb("");
                }

                memberTest.setName(properties.getString("nickname"));
                memberTest.setPermit(jsonObject.getInt("permit"));
//                중복 체크 확인할 것
                adapter.add(memberTest);
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
