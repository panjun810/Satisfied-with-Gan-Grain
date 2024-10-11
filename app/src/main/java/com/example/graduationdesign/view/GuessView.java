package com.example.graduationdesign.view;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationdesign.R;
import com.example.graduationdesign.guess.GranaryGuess;
import com.example.graduationdesign.guess.IndicaRiceGuess;
import com.example.graduationdesign.guess.JaponicaRiceGuess;
import com.example.graduationdesign.guess.WheatGuess;

public class GuessView implements View.OnClickListener {
    private LayoutInflater mInflater;
    private Activity mContext;
    private View mCurrentView;

    private TextView tv_main_title,tv_back;
    private RelativeLayout rl_title_bar;

    private Spinner spinner;
    private String[] sort;

    private LinearLayout guess_wheat,guess_indica_rice,guess_japonica_rice;

    //重量，水分，杂质率（测算的公共部分）
    private EditText et_weight,et_water,et_impurity;

    //小麦的不完善颗粒率，容重
    private EditText et_wheat_guess1,et_wheat_guess2;

    //籼稻的整精米率，出糙率，谷外糙米率，黄粒米
    private EditText et_indica_rice_guess1,et_indica_rice_guess2,et_indica_rice_guess3,et_indica_rice_guess4;

    //粳稻的整精米率，出糙率，谷外糙米率，黄粒米
    private EditText et_japonica_rice_guess1,et_japonica_rice_guess2,et_japonica_rice_guess3,et_japonica_rice_guess4;

    //小麦测算，籼稻测算，粳稻测算按钮
    private Button btn_wheat,btn_indica_rice,btn_japonica_rice;

    private String weight,water,impurity;
    private String wheat_guess1,wheat_guess2;
    private String indica_rice_guess1,indica_rice_guess2,indica_rice_guess3,indica_rice_guess4;
    private String japonica_rice_guess1,japonica_rice_guess2,japonica_rice_guess3,japonica_rice_guess4;

    private TextView tv_sale_market,tv_market;

    //切换粮食测算方式的标签（托市粮和市场粮）
    private Boolean Switch;

    //切换粮食测算种类的标签（小麦，籼稻，粳稻）
    private String Flag;

    private GranaryGuess granaryGuess = new GranaryGuess();
    private WheatGuess wheatGuess;
    private JaponicaRiceGuess japonicaRiceGuess;
    private IndicaRiceGuess indicaRiceGuess;

    public GuessView(Activity context) {
        mContext = context;
        //为之后将Layout转化为view时用
        mInflater = LayoutInflater.from(mContext);
    }
    private  void createView() {
/*        mCurrentView.setVisibility(View.VISIBLE);*/
        initView();
    }

    private void initView() {
        mCurrentView = mInflater.inflate(R.layout.main_view_guess, null);
        initTitle();
        initWheat();
        initIndicaRice();
        initJaponicaRice();
        initSwith();
        initSpinner();
    }

    private void initSpinner(){
        spinner = (Spinner) mCurrentView.findViewById(R.id.sp_sort);
        sort = mCurrentView.getResources().getStringArray(R.array.sort);

        et_weight = (EditText) mCurrentView.findViewById(R.id.et_weight);
        et_water = (EditText) mCurrentView.findViewById(R.id.et_water);
        et_impurity = (EditText) mCurrentView.findViewById(R.id.et_impurity);

        Flag = "小麦";

        // 声明一个下拉列表的数组适配器
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(mContext,
                R.layout.item_spinner, sort);

        spinner.setAdapter(starAdapter); // 设置下拉框的数组适配器
        spinner.setSelection(0); // 设置下拉框默认显示第一项

        //选择监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*TODO adapterView: 代表触发事件的Spinner控件本身。
                   view: 代表被选中的选项所对应的视图。
                   i: 代表被选中的选项在适配器中的位置（索引）。
                   l: 代表被选中的选项的行ID（如果适用）。*/
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        //小麦显示，籼稻粳稻隐藏
                        guess_wheat.setVisibility(View.VISIBLE);
                        guess_indica_rice.setVisibility(View.GONE);
                        guess_japonica_rice.setVisibility(View.GONE);

                        ClearPublic();
                        ClearIndicaRice();
                        ClearJaponicaRice();
                        Flag = "小麦";
                        break;
                    case 1:
                        //小麦粳稻隐藏，籼稻显示
                        guess_wheat.setVisibility(View.GONE);
                        guess_indica_rice.setVisibility(View.VISIBLE);
                        guess_japonica_rice.setVisibility(View.GONE);

                        ClearPublic();
                        ClearWheat();
                        ClearJaponicaRice();
                        Flag = "籼稻";
                        break;
                    case 2:
                        //小麦籼稻隐藏，粳稻显示
                        guess_wheat.setVisibility(View.GONE);
                        guess_indica_rice.setVisibility(View.GONE);
                        guess_japonica_rice.setVisibility(View.VISIBLE);

                        ClearPublic();
                        ClearWheat();
                        ClearIndicaRice();
                        Flag = "粳稻";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void initSwith() {
        tv_sale_market =(TextView) mCurrentView.findViewById(R.id.tv_sale_market);
        tv_market=(TextView) mCurrentView.findViewById(R.id.tv_market);

        //默认是托市粮测算
        Switch = true;
        //托市粮测算的点击事件
        tv_sale_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_market.setBackgroundResource(R.drawable.border_on);
                tv_market.setTextColor(Color.parseColor("#ffffff"));

                tv_sale_market.setBackgroundResource(R.drawable.border_off);
                tv_sale_market.setTextColor(Color.parseColor("#33cc00"));
                tv_main_title.setText("托市收购价格测算");
                Switch = true;

                ClearPublic();
                ClearWheat();
                ClearIndicaRice();
                ClearJaponicaRice();
            }
        });
        //市场粮测算的点击事件
        tv_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_sale_market.setBackgroundResource(R.drawable.border_on);
                tv_sale_market.setTextColor(Color.parseColor("#ffffff"));

                tv_market.setBackgroundResource(R.drawable.border_off);
                tv_market.setTextColor(Color.parseColor("#33cc00"));
                tv_main_title.setText("市场收购价格测算");
                Switch = false;

                ClearPublic();
                ClearWheat();
                ClearIndicaRice();
                ClearJaponicaRice();
            }
        });
    }

    //设置进入价格测算页的默认状态
    public void SetGuessView(){
        spinner.setSelection(0);

        ClearPublic();
        ClearWheat();
        ClearIndicaRice();
        ClearJaponicaRice();

        Switch = true;
        Flag = "小麦";
        tv_market.setBackgroundResource(R.drawable.border_on);
        tv_market.setTextColor(Color.parseColor("#ffffff"));

        tv_sale_market.setBackgroundResource(R.drawable.border_off);
        tv_sale_market.setTextColor(Color.parseColor("#33cc00"));
        tv_main_title.setText("托市收购价格测算");

    }

    //清除公共文本：重量、水分、杂质率
    private void ClearPublic() {
        et_weight.setText("");
        et_water.setText("");
        et_impurity.setText("");
    }

    //切换时清除粳稻的文本
    private void ClearJaponicaRice(){
        et_japonica_rice_guess1.setText("");
        et_japonica_rice_guess2.setText("");
        et_japonica_rice_guess3.setText("");
        et_japonica_rice_guess4.setText("");
    }

    //切换时清除籼稻的文本
    private void ClearIndicaRice() {
        et_indica_rice_guess1.setText("");
        et_indica_rice_guess2.setText("");
        et_indica_rice_guess3.setText("");
        et_indica_rice_guess4.setText("");
    }

    //切换时清除小麦的文本
    private void ClearWheat() {
        et_wheat_guess1.setText("");
        et_wheat_guess2.setText("");
    }

    //实例化粳稻
    private void initJaponicaRice() {
        guess_japonica_rice=(LinearLayout)mCurrentView.findViewById(R.id.guess_japonica_rice);
        et_japonica_rice_guess1  = (EditText) mCurrentView.findViewById(R.id.et_japonica_rice_guess1);
        et_japonica_rice_guess2  = (EditText) mCurrentView.findViewById(R.id.et_japonica_rice_guess2);
        et_japonica_rice_guess3  = (EditText) mCurrentView.findViewById(R.id.et_japonica_rice_guess3);
        et_japonica_rice_guess4  = (EditText) mCurrentView.findViewById(R.id.et_japonica_rice_guess4);
        btn_japonica_rice = (Button) mCurrentView.findViewById(R.id.btn_japonica_rice);
        btn_japonica_rice.setOnClickListener(this);
    }

    //实例化籼稻
    private void initIndicaRice() {
        guess_indica_rice=(LinearLayout)mCurrentView.findViewById(R.id.guess_indica_rice);
        et_indica_rice_guess1  = (EditText) mCurrentView.findViewById(R.id.et_indica_rice_guess1);
        et_indica_rice_guess2  = (EditText) mCurrentView.findViewById(R.id.et_indica_rice_guess2);
        et_indica_rice_guess3  = (EditText) mCurrentView.findViewById(R.id.et_indica_rice_guess3);
        et_indica_rice_guess4  = (EditText) mCurrentView.findViewById(R.id.et_indica_rice_guess4);
        btn_indica_rice = (Button) mCurrentView.findViewById(R.id.btn_indica_rice);
        btn_indica_rice.setOnClickListener(this);
    }

    //实例化小麦
    private void initWheat() {
        guess_wheat=(LinearLayout)mCurrentView.findViewById(R.id.guess_wheat);
        et_wheat_guess1  = (EditText) mCurrentView.findViewById(R.id.et_wheat_guess1);
        et_wheat_guess2 = (EditText) mCurrentView.findViewById(R.id.et_wheat_guess2);
        btn_wheat = (Button) mCurrentView.findViewById(R.id.btn_wheat);
        btn_wheat.setOnClickListener(this);
    }

    private void initTitle() {
        tv_main_title=(TextView) mCurrentView.findViewById(R.id.tv_main_title);
        tv_main_title.setText("托市收购价格测算");
        tv_back=(TextView) mCurrentView.findViewById(R.id.tv_back);
        rl_title_bar=(RelativeLayout) mCurrentView.findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#33ff00"));
        tv_back.setVisibility(View.GONE);
    }


    public View getView() {
        if (mCurrentView == null) {
            createView();
        }
        return mCurrentView;
    }
    /**
     * 显示当前导航栏上方所对应的view界面
     */
    public void showView(){
        if(mCurrentView == null){
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        getPublic();
        switch (view.getId()){
            case R.id.btn_wheat:
                //判断输入框是否为空
                getWheat();
                isEmptyWheat();
                break;
            case R.id.btn_indica_rice:
                getIndicaRice();
                //判断输入框是否为空
                isEmptyIndicaRice();
                break;
            case R.id.btn_japonica_rice:
                getJaponicaRice();
                //判断输入框是否为空
                isEmptyJaponicaRice();
                break;

        }
    }

    private void isEmptyJaponicaRice() {
        japonicaRiceGuess = new JaponicaRiceGuess(et_japonica_rice_guess1,et_japonica_rice_guess2,et_japonica_rice_guess3,et_japonica_rice_guess4);
        granaryGuess.setGranaryGuessStrategy(japonicaRiceGuess);
        if (TextUtils.isEmpty(weight) || TextUtils.isEmpty(water) || TextUtils.isEmpty(impurity)){
            Toast.makeText(mContext,"请将信息补充完整！",Toast.LENGTH_SHORT).show();
            return;
        }else if(granaryGuess.ifIsEmpty()){
            Toast.makeText(mContext,"请将信息补充完整！",Toast.LENGTH_SHORT).show();
            return;
        }else{
            //输入的数据不为空则开始计算籼稻
            //使用策略上下文计算籼稻
            granaryGuess.getDouble(weight,water,impurity);
            granaryGuess.judgeLevel();
            granaryGuess.getTotal();
            granaryGuess.setDialog(mContext,Switch);
        }
    }

    private void isEmptyIndicaRice() {
        indicaRiceGuess = new IndicaRiceGuess(et_indica_rice_guess1,et_indica_rice_guess2,et_indica_rice_guess3,et_indica_rice_guess4);
        granaryGuess.setGranaryGuessStrategy(indicaRiceGuess);
        if (TextUtils.isEmpty(weight) || TextUtils.isEmpty(water) || TextUtils.isEmpty(impurity)){
            Toast.makeText(mContext,"请将信息补充完整！",Toast.LENGTH_SHORT).show();
            return;
        }else if(granaryGuess.ifIsEmpty()){
            Toast.makeText(mContext,"请将信息补充完整！",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            //输入的数据不为空则开始计算粳稻
            //使用策略上下文计算粳稻
            granaryGuess.getDouble(weight,water,impurity);
            granaryGuess.judgeLevel();
            granaryGuess.getTotal();
            granaryGuess.setDialog(mContext,Switch);
        }
    }

    private void isEmptyWheat() {
        wheatGuess = new WheatGuess(et_wheat_guess1,et_wheat_guess2);
        granaryGuess.setGranaryGuessStrategy(wheatGuess);
        if (TextUtils.isEmpty(weight) || TextUtils.isEmpty(water) || TextUtils.isEmpty(impurity)){
            Toast.makeText(mContext,"请将信息补充完整！",Toast.LENGTH_SHORT).show();
            return;
        }else if(granaryGuess.ifIsEmpty()){
            Toast.makeText(mContext,"请将信息补充完整！",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            //输入的数据不为空则开始计算小麦
            //使用策略上下文计算小麦
            granaryGuess.getDouble(weight,water,impurity);
            granaryGuess.judgeLevel();
            granaryGuess.getTotal();
            granaryGuess.setDialog(mContext,Switch);
        }
    }

    //获取重量、水分、杂质的计算数据
    private void getPublic() {
        weight = et_weight.getText().toString().trim();
        water = et_water.getText().toString().trim();
        impurity = et_impurity.getText().toString().trim();
    }


    //获取小麦的计算数据
    private void getWheat(){
        wheat_guess1 = et_wheat_guess1.getText().toString().trim();
        wheat_guess2 = et_wheat_guess2.getText().toString().trim();
    }

    //获取籼稻的计算数据
    private void getJaponicaRice() {
        japonica_rice_guess1 = et_japonica_rice_guess1.getText().toString().trim();
        japonica_rice_guess2 = et_japonica_rice_guess2.getText().toString().trim();
        japonica_rice_guess3 = et_japonica_rice_guess3.getText().toString().trim();
        japonica_rice_guess4 = et_japonica_rice_guess4.getText().toString().trim();
    }

    //获取粳稻的计算数据
    private void getIndicaRice() {
        indica_rice_guess1 = et_indica_rice_guess1.getText().toString().trim();
        indica_rice_guess2 = et_indica_rice_guess2.getText().toString().trim();
        indica_rice_guess3 = et_indica_rice_guess3.getText().toString().trim();
        indica_rice_guess4 = et_indica_rice_guess4.getText().toString().trim();
    }
}

