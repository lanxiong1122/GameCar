package cn.charon.racegame.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.charon.racegame.R;
import cn.charon.racegame.activity.CarActivity;

public class HistoryAdapter extends BaseAdapter {

    private Context mContext;
    LayoutInflater layoutInflater;// 布局装载对象
    public HistoryAdapter(Context context) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
    }
    /*
     * 数据集中有多少条
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return CarActivity.scoreList.size();
    }

    /*
     * 获取数据集中制定位置关联的数据
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return CarActivity.scoreList== null ?
                null : CarActivity.scoreList.get(position);
    }

    /*
     * 获取与列表中指定位置关联的行id
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /*
     * 视图相关操作
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        // 初始化视图及控件
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = layoutInflater.inflate(R.layout.item_listview_history, parent,false);
            myHolder.time =  convertView.findViewById(R.id.game_time);
            myHolder.score=convertView.findViewById(R.id.game_score);
            myHolder.rank=convertView.findViewById(R.id.ranking);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        // 将数据设置在控件上
        myHolder.time.setText(CarActivity.scoreList.get(CarActivity.scoreList.size()-1-position).get("time"));
        myHolder.score.setText(CarActivity.scoreList.get(CarActivity.scoreList.size()-1-position).get("score"));
        myHolder.rank.setText(Integer.parseInt(CarActivity.scoreList.get(position).get("ranking"))+1+"");
        return convertView;
    }
    //对应视图中的各个控件
    class MyHolder {
        TextView time;
        TextView score;
        TextView rank;
    }


}