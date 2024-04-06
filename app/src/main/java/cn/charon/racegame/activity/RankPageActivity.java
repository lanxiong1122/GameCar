package cn.charon.racegame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import cn.charon.racegame.R;
import cn.charon.racegame.activity.CarActivity;
import cn.charon.racegame.history.HistoryAdapter;

public class RankPageActivity extends AppCompatActivity {
    SharedPreferences sp;//内置数据库 历史分数
    private ListView history;
    HistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_page);
        //获取本地数据库
        sp = getSharedPreferences("HistoryInfo",0x0000);
        initHistoryScore();
        history=findViewById(R.id.history_listview);
        adapter=new HistoryAdapter(this);
        history.setAdapter(adapter);
    }

    private void initHistoryScore() {
        //这里用到了gson 把json格式转化为map数据 成功获取到之前保存的分数
        Gson gson=new Gson();
        String strJson=sp.getString("scoreHistory","");
        Log.w("json_return" ,strJson);
        if(!strJson.equals(""))
        {
            CarActivity.scoreList=gson.fromJson(
                    strJson,new TypeToken<List<Map<String,String>>>() {
                    }.getType());
        }
    }
}