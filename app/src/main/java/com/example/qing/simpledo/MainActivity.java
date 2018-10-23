package com.example.qing.simpledo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MessageAdapter.CheckItemListener{


    //时间线
    private MessageAdapter mMessageAdapter;
    private List<messageClass> myMessage=new ArrayList<>();
    private Button send;
    private EditText sendMessage;
    private LinearLayout outEdit;
    private MyDatabaseHelper dbHelper;

    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper=new MyDatabaseHelper(this,"time.db",null,1);
        initData();
        //使用Toolbar，上方的标题栏
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //为左上加上导航按钮，并设置图片，和响应
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navViewe=(NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }

        //设置  划开左侧菜单后的点击事件
        navViewe.setCheckedItem(R.id.nav_call);  //将Call菜单设置为默认选中
        navViewe.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(item.getItemId()==R.id.nav_friends){    //响应逻辑
                    Toast.makeText(MainActivity.this,"nav_friends",Toast.LENGTH_LONG).show();
                }
             // mDrawerLayout.closeDrawers();  //将滑动菜单关闭
                return true;
            }
        });

        findView();
        RecyclerView relist=(RecyclerView)findViewById(R.id.items);
        relist.setLayoutManager(new LinearLayoutManager(this));
       //新建适配器
        mMessageAdapter=new MessageAdapter(this,myMessage,this);
        //传入布局
        relist.setAdapter(mMessageAdapter);


    }  //oncreate的括号


    //右上侧创建菜单，加载toolbar.xml菜单文件
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override   //设置菜单相应事件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  //相应左侧打开滑动页面的事件（展开左侧菜单）
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup://添加计划界面
//                Intent intent=new Intent(MainActivity.this,PutiMessageActivity.class);
//                startActivity(intent);
                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }


    //时间线
    private void findView() {
        send=(Button)findViewById(R.id.send);
        sendMessage=(EditText)findViewById(R.id.input_text);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempMess=sendMessage.getText().toString();
                if(!"".equals(tempMess)){
                    messageAdd(tempMess);
                    sendMessage.setText("");
                }
            }
        });

    }
    private void initData() {
        //读取数据
        ReadDatas();
    }


    //处理Item点击选中回调事件
    public void itemChecked(messageClass checkBean, boolean isChecked) {

        //处理Item点击选中回调事件
        for (messageClass temp : myMessage) {
            if(checkBean.equals(temp)){

                checkBean.setCheck(isChecked);
//              dataArray.remove(temp);
//              dataArray.add(checkBean);
            }
        }
    }

    public void messageChecked(messageClass chenckBean,String inform){
        for (messageClass temp : myMessage) {
            if (chenckBean.equals(temp)) {
                chenckBean.setEditmessages(inform);
            }
        }
    }

    public void messageAdd(String mess){
        myMessage.add(new messageClass(false,mess));
        mMessageAdapter.notifyItemChanged(0);
    }
    public void messageDel(messageClass chenckBean){
        myMessage.remove(chenckBean);
        mMessageAdapter.notifyDataSetChanged();
    }


    //保存
    public void saveDatas(List<messageClass> datas){
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //删除全部
        db.delete("time",null,null);

        //重新写入
        ContentValues values=new ContentValues();
        for (messageClass temp :datas){
            values.put("checks",temp.getCheck());
            values.put("mess",temp.getEditmessages());
            db.insert("time",null,values);
            values.clear();
        }
    }


    //读取
    public void ReadDatas(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //读取全部
        Cursor cursor =db.query("time",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String a=cursor.getString(cursor.getColumnIndex("mess"));
                int b=cursor.getInt(cursor.getColumnIndex("checks"));
                boolean b1;
                if (b==1){
                    b1=true;
                }else{
                    b1=false;
                }
                myMessage.add(new messageClass(b1,a));
                // Toast.makeText(MainActivity.this,a,Toast.LENGTH_LONG).show();
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveDatas(myMessage);
    }


    /**
     * 点击非编辑区域收起键盘
     * 获取点击事件
     */
    @CallSuper
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() ==  MotionEvent.ACTION_DOWN ) {
            View view = getCurrentFocus();
            if (isShouldHideKeyBord(view, ev)) {
                hideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判定当前是否需要隐藏
     */
    protected boolean isShouldHideKeyBord(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
