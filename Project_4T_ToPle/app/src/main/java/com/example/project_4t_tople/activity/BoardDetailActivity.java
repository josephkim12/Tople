package com.example.project_4t_tople.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_4t_tople.R;
import com.example.project_4t_tople.adapter.RepleAdapter;
import com.example.project_4t_tople.model.Board;
import com.example.project_4t_tople.model.Mypage;
import com.example.project_4t_tople.model.Redat;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BoardDetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView detailSubject,detailName,detailContent;
    EditText repleText,bSubject,bContent;
    Button detailBack,repleSave,detailDelete,detailUpdate;
    ImageView detailImage;

    RepleAdapter repleAdapter;
    ArrayList<Redat> list;
    ListView listReple;

    //통신용 객체 선언
    AsyncHttpClient client;
    HttpResponse_BoardSelect response_boardSelect;
    HttpResponse_RepleInsert response_repleInsert;
    HttpResponse_RepleList response_replelist;
    HttpResponse_BoardDelete response_boardDelete;

    //특정 글 조회
    String URL_BoardDeatil = "http://175.198.87.149:8080/moim.4t.spring/testselectBoard.tople";
    //특정 글 삭제
    String URL_BoardDelete ="http://175.198.87.149:8080/moim.4t.spring/deleteBoard.tople";
    //댓글입력
    String URL_RepleInsert ="http://175.198.87.149:8080/moim.4t.spring/writeReDat.tople";
    //댓글리스트
    String URL_RepleList ="http://175.198.87.149:8080/moim.4t.spring/selectReDatList.tople";

    int listnum=0;
    String user_id;
    int lev=0;
    String filename;
    String thumb;
    Mypage mypage;
    Board board;

    String reple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);
        detailSubject=findViewById(R.id.detailSubject);
        detailName=findViewById(R.id.detailName);
        detailContent=findViewById(R.id.detailContent);
        detailImage=findViewById(R.id.detailImage);
        repleText=findViewById(R.id.repleText);
        detailBack=findViewById(R.id.detailBack);
        detailUpdate=findViewById(R.id.detailUpdate);
        detailDelete=findViewById(R.id.detailDelete);
        repleSave=findViewById(R.id.repleSave);
        listReple=findViewById(R.id.listReple);

        bSubject=findViewById(R.id.bSubject);
        bContent=findViewById(R.id.bContent);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        mypage= (Mypage) intent.getSerializableExtra("item");
        listnum = intent.getIntExtra("listnum",listnum);
        board=(Board) intent.getSerializableExtra("board");

        list=new ArrayList<>();
        repleAdapter = new RepleAdapter(this,R.layout.item_reple,list);
        listReple.setAdapter(repleAdapter);

        client=new AsyncHttpClient();
        response_boardSelect=new HttpResponse_BoardSelect(this);
        response_repleInsert=new HttpResponse_RepleInsert(this);
        response_replelist=new HttpResponse_RepleList(this);
        response_boardDelete=new HttpResponse_BoardDelete(this);

        detailUpdate.setOnClickListener(this);
        detailBack.setOnClickListener(this);
        detailDelete.setOnClickListener(this);
        repleSave.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boardDetail();
        repleList();
    }

    public void boardDetail(){
        RequestParams params = new RequestParams();
        params.put("listnum",listnum);
        client.post(URL_BoardDeatil,params,response_boardSelect);
    }

    public void repleList(){
        if (!repleAdapter.isEmpty()) repleAdapter.clear();
        RequestParams params = new RequestParams();
        params.put("listnum",listnum);
        client.post(URL_RepleList,params,response_replelist);
    }

    public void boardDelete(){
        RequestParams paramsdelete = new RequestParams();
        paramsdelete.put("listnum",listnum);
        client.post(URL_BoardDelete,paramsdelete,response_boardDelete);
    }

    public void repleInsert(){
        RequestParams params = new RequestParams();
        params.put("reple",reple);
        params.put("id",user_id);
        params.put("listnum",listnum);
        params.put("moimcode",mypage.getMoimcode());
        client.post(URL_RepleInsert,params,response_repleInsert);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()){
            case R.id.detailBack:
                finish();
                break;
            case R.id.detailDelete:
                boardDelete();
                break;
            case R.id.repleSave:
                reple =repleText.getText().toString().trim();
                if (reple.equals("") || reple == null) {
                    Toast.makeText(this, "답글 내용을 작성 하세요.",
                            Toast.LENGTH_SHORT).show();

                    return;
                }
                repleInsert();
                repleText.setText("");
                break;
            case R.id.detailUpdate:
                intent = new Intent(this,BoardUpdateActivity.class);
                intent.putExtra("subject",detailSubject.getText().toString().trim());
                intent.putExtra("content",detailContent.getText().toString().trim());
                intent.putExtra("id",user_id);
                intent.putExtra("lev",lev);
                intent.putExtra("filename",filename);
                intent.putExtra("thumb",thumb);
                intent.putExtra("listnum",listnum);
                intent.putExtra("item",mypage);
                intent.putExtra("moimcode",mypage.getMoimcode());

                startActivity(intent);
                break;
        }
    }

    class HttpResponse_BoardDelete extends AsyncHttpResponseHandler {
        Activity activity;
        public HttpResponse_BoardDelete(Activity activity) { this.activity = activity; }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt  = json.getString("result");
                if(rt.equals("OK")){
                    Toast.makeText(getApplicationContext(),"게시글 삭제 성공",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getApplicationContext(), "통신실패 \n BoardDetailActivity1"+statusCode, Toast.LENGTH_SHORT).show();
        }

    }

    class HttpResponse_RepleInsert extends  AsyncHttpResponseHandler{
        Activity activity;
        public HttpResponse_RepleInsert(Activity activity) { this.activity = activity; }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt  = json.getString("result");
                if(rt.equals("OK")){
                    Toast.makeText(getApplicationContext(),"댓글 저장 성공!",Toast.LENGTH_SHORT).show();
                    repleList();
                }else {
                    Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getApplicationContext(), "통신실패 BoardDetailActivity2"+statusCode, Toast.LENGTH_SHORT).show();
        }
    }

    //댓글 조회 통신
    class HttpResponse_RepleList extends AsyncHttpResponseHandler {
        Activity activity;

        public HttpResponse_RepleList(Activity activity) {
            this.activity = activity;
        }
        //통신 성공
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            String strJson = new String(responseBody);

            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray item = json.getJSONArray("items");
                for (int i=0; i<item.length(); i++) {
                    JSONObject temp = item.getJSONObject(i);

                    Redat redat_list = new Redat();
                    redat_list.setListnum(temp.getInt("listnum"));
                    redat_list.setId(temp.getString("id"));
                    redat_list.setRenum(temp.getInt("renum"));
                    redat_list.setReple(temp.getString("reple"));
                    redat_list.setRedate(temp.getString("redate"));
                    redat_list.setMoimcode(temp.getInt("moimcode"));
                    repleAdapter.add(redat_list);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //통신 실패
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 BoardDetailActivity 3"+statusCode, Toast.LENGTH_SHORT).show();
        }
    }

    //특정게시글 조회 통신
    class HttpResponse_BoardSelect extends AsyncHttpResponseHandler {
        Activity activity;

        public HttpResponse_BoardSelect(Activity activity) {
            this.activity = activity;
        }
        //통신 성공
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            String strJson = new String(responseBody);

            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray item = json.getJSONArray("items");
                for (int i=0; i<item.length(); i++) {
                    JSONObject temp = item.getJSONObject(i);
                    Board board_select = new Board();
                    board_select.setListnum(temp.getInt("listnum"));
                    if (!temp.getString("filename").equals("")) {
                        board_select.setFilename(temp.getString("filename"));
                    }
                    if (!temp.getString("thumb").equals("")) {
                        board_select.setThumb(temp.getString("thumb"));
                    }
                    board_select.setName(temp.getString("name"));

                    board_select.setSubject(temp.getString("subject"));
                    board_select.setId(temp.getString("id"));
                    board_select.setMoimcode(temp.getInt("moimcode"));
                    board_select.setLev(temp.getInt("lev"));
                    board_select.setEditdate(temp.getString("editdate"));
                    board_select.setContent(temp.getString("content"));

                    if(mypage.getPermit()<3){
                        detailDelete.setVisibility(View.VISIBLE);
                        detailUpdate.setVisibility(View.VISIBLE);
                    }

                    String id=board_select.getId();
                    if(id.equals(user_id)){
                        detailDelete.setVisibility(View.VISIBLE);
                        detailUpdate.setVisibility(View.VISIBLE);
                    }
                    detailSubject.setText(board_select.getSubject());
                    detailName.setText(board_select.getName());
                    detailContent.setText(board_select.getContent());
                    if(board_select.getFilename()!=null){
                        // Glide 사용
                        Glide.with(detailImage)
                             .load(board_select.getFilename())
                             .error(R.drawable.ic_error_w)
                             .placeholder(R.drawable.ic_empty_b)
                             .into(detailImage);
                    }else detailImage.setVisibility(View.GONE);

                    filename=board_select.getFilename();
                    lev=board_select.getLev();
                    thumb=board_select.getThumb();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //통신 실패
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패 BoardDetailActivity 4"+statusCode, Toast.LENGTH_SHORT).show();
        }
    }
}

