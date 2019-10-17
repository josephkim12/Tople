package com.example.project_4t_tople.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.model.Calendar;
import com.example.project_4t_tople.model.Detail_Todo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DetailMemberUpdates extends AppCompatActivity implements View.OnClickListener {
    EditText detail_update_todo, detail_update_ex;
    CheckBox detail_update_ispay;
    Button detail_update_bt1, detail_update_bt2, detail_update_getout;
    String detail_update_URL = "http://175.198.87.149:8080/moim.4t.spring/updateSchemem.tople";
    String detail_delete_URL = "http://175.198.87.149:8080/moim.4t.spring/deleteSchemem.tople";
    AsyncHttpClient client;

    Calendar calendar_item;
    Detail_Todo detail_todo_item;
    String user_id, todo_update, ex;
    boolean ispay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_member_updates);

        detail_update_todo = findViewById(R.id.detail_update_todo);
        detail_update_ex = findViewById(R.id.detail_update_ex);
        detail_update_ispay = findViewById(R.id.detail_update_ispay);
        detail_update_getout = findViewById(R.id.detail_update_getout);
        detail_update_bt1 = findViewById(R.id.detail_update_bt1);
        detail_update_bt2 = findViewById(R.id.detail_update_bt2);
        calendar_item = (Calendar) getIntent().getSerializableExtra("calendar_item");

        client = new AsyncHttpClient();
        user_id = getIntent().getStringExtra("user_id");

        detail_todo_item = (Detail_Todo) getIntent().getSerializableExtra("detail_todo");

        if (detail_todo_item.getTodo().equals("1")) {
            detail_update_todo.setHint("지정된 값이 없습니다.");
        } else {
            detail_update_todo.setText(detail_todo_item.getTodo());
        }

        if (detail_todo_item.getEx().equals("1")) {
            detail_update_ex.setHint("지정된 값이 없습니다.");
        }else {
            detail_update_ex.setText(detail_todo_item.getEx());
        }

        detail_update_ispay.setChecked(detail_todo_item.isIspay());

        detail_update_bt1.setOnClickListener(this);
        detail_update_bt2.setOnClickListener(this);
        detail_update_getout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RequestParams params = null;
        switch (v.getId()) {
            case R.id.detail_update_bt1:
                todo_update = detail_update_todo.getText().toString().trim();
                ex = detail_update_ex.getText().toString().trim();
                ispay = detail_update_ispay.isChecked();

                params = new RequestParams();
                params.put("sch_schnum", calendar_item.getSch_schnum());
                params.put("id", user_id);
                params.put("todo", todo_update);
                params.put("ex", ex);
                params.put("ispay", ispay);
                Log.d("[test]_detailmember", "sch_schnum : " + calendar_item.getSch_schnum() + " / " + user_id + " / " + ispay);

                client.post(detail_update_URL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        Log.d("[test]_detailmember", "여기 들어옴");
                        String strJson = new String(responseBody);
                        try {
                            JSONObject json = new JSONObject(strJson);
                            String rt = json.getString("result");
                            if (rt.equals("OK")) {
                                Toast.makeText(DetailMemberUpdates.this, "수정성공", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailMemberUpdates.this, "수정실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        Log.d("[test]_detailmember", "여기 들어옴 : " + statusCode);
                    }
                });
                finish();
                break;
            case R.id.detail_update_bt2:
                finish();
                break;
            case R.id.detail_update_getout:
                params = new RequestParams();
                params.put("sch_schnum", calendar_item.getSch_schnum());
                params.put("id", user_id);
                Log.d("[test]_detailmember", "값 : " + calendar_item.getSch_schnum() + " / " + user_id);

                client.post(detail_delete_URL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        Log.d("[test]_detailmember", "여기 들어옴");
                        String strJson = new String(responseBody);
                        try {
                            JSONObject json = new JSONObject(strJson);
                            String rt = json.getString("result");
                            if (rt.equals("OK")) {
                                Toast.makeText(DetailMemberUpdates.this, "수정 성공", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailMemberUpdates.this, "수정실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        Log.d("[test]_detailmember", "여기 들어옴 : " + statusCode);
                    }
                });
                finish();
                break;
        }
    }
}
