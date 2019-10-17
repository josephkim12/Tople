package com.example.project_4t_tople.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_4t_tople.R;
import com.example.project_4t_tople.activity.BoardDetailActivity;
import com.example.project_4t_tople.model.Redat;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class RepleAdapter extends ArrayAdapter<Redat> implements View.OnClickListener {
    Activity activity;
    int resource;
    Button repleDelete;
    int renum=0;

    //댓글 삭제
    String URL_RepleDelete ="http://175.198.87.149:8080/moim.4t.spring/deleteRedat.tople";
    //통신용 객체 선언
    AsyncHttpClient client;
    HttpResponse_RepleDelete response_repleDelete;

    public RepleAdapter(Context context, int resource, List<Redat> objects) {
        super(context, resource, objects);
        activity=(Activity)context;
        this.resource=resource;
        client= new AsyncHttpClient();
        response_repleDelete=new HttpResponse_RepleDelete(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        Redat item = getItem(position);

        if (item != null) {

            TextView repleContent = convertView.findViewById(R.id.repleContent);

            repleContent.setText(item.getReple());


            repleDelete=convertView.findViewById(R.id.repleDelete);
            renum=item.getRenum();

            repleDelete.setOnClickListener(this);

        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.repleDelete:
                RequestParams params = new RequestParams();
                params.put("renum",renum);
                //Toast.makeText(activity,"renum"+renum,Toast.LENGTH_SHORT).show();
                client.post(URL_RepleDelete,params,response_repleDelete);
                break;

        }
    }

    class HttpResponse_RepleDelete extends AsyncHttpResponseHandler {
        Activity activity;
        public HttpResponse_RepleDelete(Activity activity) { this.activity = activity; }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt  = json.getString("result");
                if(rt.equals("OK")){
                    Toast.makeText(activity,"삭제성공",Toast.LENGTH_SHORT).show();
                    ((BoardDetailActivity)getContext()).repleList();
                }else {
                    Toast.makeText(activity,"실패",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패"+statusCode, Toast.LENGTH_SHORT).show();
        }

    }
}