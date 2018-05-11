package com.example.fqzhang.myapplication;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.debug.hv.ViewServer;
import com.example.Seriable;
import com.example.fqzhang.myapplication.Util.CustomTranslateUtil;
import com.example.fqzhang.myapplication.Util.ZUtil;
import com.example.fqzhang.myapplication.fragment.FlexboxLayoutFragment;
import com.example.fqzhang.myapplication.fragment.MDialogFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
@Seriable
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.list_container)
    protected RelativeLayout container;
    @BindView(R.id.show_content)
    protected FrameLayout content;
    public FragmentManager fragmentManager;
    private List<View> transitionViewList = new ArrayList<>();
    private List<String> datas = new ArrayList<>();
    private TextView reloadTextView, getfirstVisibleTv, chatTextView, emailTextView, telTextView,flexboxLayout;
    private ListView showlistView;
    private MyAdapter mAdapter;
    private boolean isReLoad;
    private LinearLayout bottomView;
    private float moveY = 0.0f;
    private boolean isVisible = false;
    private PopupWindow pop;
    private SharedPreferences sp;
    private boolean isFirstShow = false;
    private FloatingActionButton fab;
    private View mTopView;
    int screenWidth = 0;
    int screenHeight = 0;
    private MyAdapter.OnClickListener mAdapterOnClickListener= new MyAdapter.OnClickListener(){
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void bottomExpand(boolean isExpand) {
            if (isExpand) {
                initDataNew();
            } else {
                initData();
            }
            mAdapter.notifyDataSetInvalidated();
        }
    };
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this,R.layout.activity_main,null);
        view.setBackground(ZUtil.getGradientBG(R.color.colorAccent));
        setContentView(view);
        ButterKnife.bind(this);
        ViewServer.get(this).addWindow(this);
        initData();
        initView();
        bindData(false);
        setListener();
        reloadTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (reloadTextView.getMeasuredWidth() > 0) {
                    showPop();
                    reloadTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    screenWidth = MainActivity.this.getWindow().getDecorView().getWidth();
                    screenHeight = MainActivity.this.getWindow().getDecorView().getHeight();
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isReLoad && hasFocus) {
            bindData(isReLoad);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity", "onResume");
        clearListTransitionAnims();
        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity", "onStop");
    }

    private void showDialog() {
        //isReLoad = true;
        int alertDialogStyle = R.attr.alertDialogStyle;
/*        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog);
        builder.setIcon(android.R.drawable.btn_dialog)
                .setTitle("提醒")
                .setMessage("hello world!")
                .setCancelable(true)
                .setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // bindData();
            }
        });
        builder.create().show();*/
        MDialogFragment dialogFragment = MDialogFragment.newInstance();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(dialogFragment, "mdialogFragment");
        ft.commit();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        reloadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
                mAdapter.setDatas(datas);
                mAdapter.notifyDataSetChanged();
                Log.e("zfq", "重新绑定了");
                showDialog();
            }
        });
        getfirstVisibleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int headerViewsCount = showlistView.getHeaderViewsCount();
                final int firstVisiblePosition = showlistView.getFirstVisiblePosition();
                Toast.makeText(MainActivity.this, headerViewsCount + ":" + firstVisiblePosition, Toast.LENGTH_SHORT).show();
                showlistView.setSelection(1);
                // showlistView.scrollTo(0,0);
                int top = (showlistView.getChildAt(0) == null) ? 0 : v.getTop();
//                showlistView.setSelectionFromTop(firstVisiblePosition,top);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getfirstVisibleTv.setText("当前" + firstVisiblePosition);
                    }
                });
            }
        });
        showlistView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e("zfq", firstVisibleItem + ":" + visibleItemCount + ":" + totalItemCount);
            }
        });
        showlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final int pos = position - 1;
                if (!datas.get(pos).contains(":")) {
                    view.setEnabled(false);
                    return;
                }
                //TODO 动画
                startExitTransition(showlistView,position,300);
  /*              CustomTranslateUtil.getInstance().hideViewWithAlpha(view, CustomTranslateUtil.DOWN_TO_TOP, 300, 200, true);
                transitionViewList.add(view);*/

                showlistView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", pos);
                        bundle.putString("detail", datas.get(pos));
                        bundle.putInt("sourceType",position%2);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        MainActivity.this.overridePendingTransition(0,0);
                    }
                },300);
            }
        });
        showlistView.setOnTouchListener(new View.OnTouchListener() {
            private float lastY = 0;
            private float startY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int motionEvent = event.getActionMasked();
                switch (motionEvent) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveY = event.getY();
                        if (moveY - lastY < -10 && moveY - startY < -20) {
                            doTopAndBottomAnimation(-1);
                        } else if (moveY - lastY > 10 && moveY - startY > 20) {
                            doTopAndBottomAnimation(1);
                        }
                        lastY = moveY;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
        chatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop != null && pop.isShowing()) {
                    sp.edit().putBoolean("isFirstShow", true).commit();
                    pop.dismiss();
                    pop = null;
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "this is a floatActionButton!", Toast.LENGTH_SHORT).show();
            }
        });
        fab.setOnTouchListener(onTouchListener);
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        int mLastX = 0;
        int mLastY = 0;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int currentX = (int) event.getRawX();
            int currentY = (int) event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastX = currentX;
                    mLastY= currentY;
                    break;
                case MotionEvent.ACTION_UP:
                    int distance = currentX - mLastX + currentY - mLastY;
                    Log.e("DIstance",distance+"");
                    if (Math.abs(distance)<20) {
                        //当变化太小的时候什么都不做 OnClick执行
                    }else {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    int deltaX =  currentX - mLastX;
                    int deltaY = currentY - mLastY;
                    int l = v.getLeft() + deltaX;
                    int b = v.getBottom() + deltaY;
                    int r = v.getRight() + deltaX;
                    int t = v.getTop() + deltaY;
                    // 下面判断移动是否超出屏幕
                    if (l < 0) {
                        l = 0;
                        r = v.getWidth();
                    }
                    if (t < 0) {
                        t = 0;
                        b = v.getHeight();
                    }
                    if (r > screenWidth) {
                        r = screenWidth;
                        l = r - v.getWidth();
                    }
                    if (b > screenHeight) {
                        b = screenHeight;
                        t = b - v.getHeight();
                    }
                    v.layout(l,t,r,b);
                    v.postInvalidate();
                    mLastX = currentX;
                    mLastY = currentY;
                    break;
            }
            return false;
        }
    };
    private void showPop() {
        sp = getPreferences(Context.MODE_APPEND);
        boolean isFirstShow = sp.getBoolean("isFirstShow", false);
        if (!isFirstShow) {
            TextView tv = new TextView(this);
            tv.setTextColor(Color.parseColor("#aaddee"));
            tv.setText("点这里，开始聊天！");
            tv.setTextSize(16);
            pop = new PopupWindow(tv, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#eeeeee")));
            pop.setOutsideTouchable(false);
            pop.setFocusable(false);
            pop.showAsDropDown(reloadTextView, 15, 0);
        }
    }

    private void doTopAndBottomAnimation(int deltaY) {
        if (deltaY > 0 && isVisible) {
            isVisible = false;
            moveY = moveY - 0;
            //topViewInAnimation(350);
            bottomViewInAnimation(350);
        }
        if (deltaY < 0 && !isVisible) {
            isVisible = true;
            moveY = moveY + 0;
            //topViewOutAnimation();
            bottomViewOutAnimation();
        }
    }

    public void bottomViewInAnimation(int durationTime) {
        final ObjectAnimator animPushIn = ObjectAnimator.ofFloat(bottomView, "translationY", bottomView.getHeight(), 0);
        animPushIn.setInterpolator(new LinearInterpolator());
        animPushIn.setDuration(durationTime);
        animPushIn.start();
    }

    public void bottomViewOutAnimation() {
        final ObjectAnimator animPushOut = ObjectAnimator.ofFloat(bottomView, "translationY", 0, bottomView.getHeight());
        animPushOut.setInterpolator(new LinearInterpolator());
        animPushOut.setDuration(350);
        animPushOut.start();
    }

    private void bindData(boolean isReload) {

        if (isReload) {
            List<String> list = new ArrayList<>();
            list.add("张顺1:zzz");
            list.add("张顺2:zzz");
            list.add("张顺3:zzz");
            mAdapter.setDatas(list);
            mAdapter.notifyDataSetInvalidated();
        } else {
            mAdapter = new MyAdapter(datas, this);
            mAdapter.setOnClickListener(mAdapterOnClickListener);
            //showlistView.setLayoutAnimation(getAnimationController());
            showlistView.addHeaderView(View.inflate(this, R.layout.listheader, null));
            showlistView.setAdapter(mAdapter);

        }
    }

    private void initView() {
        mTopView = findViewById(R.id.ll_btn);
        reloadTextView = (TextView) findViewById(R.id.tv_reload);
        showlistView = (ListView) findViewById(R.id.lv_main);
        showlistView.setSelector(R.drawable.itemselector);
        bottomView = (LinearLayout) findViewById(R.id.bottom_layout);
        getfirstVisibleTv = (TextView) findViewById(R.id.tv_getFirstVisible);
        chatTextView = (TextView) findViewById(R.id.tv_chat);
        emailTextView = (TextView) findViewById(R.id.tv_email);
        telTextView = (TextView) findViewById(R.id.tv_tel);
        fab = (FloatingActionButton) findViewById(R.id.bMain_Float);
    }
    @OnClick(R.id.flexboxLayout)
    public void showFlexLayout(){
        container.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
        FlexboxLayoutFragment fragment = new FlexboxLayoutFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.show_content,fragment,FlexboxLayoutFragment.class.getName());
        ft.commit();

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        datas.clear();
        Random r = new Random();
        for (int i = 0; i < 30; i++) {
            int num = r.nextInt(50);
            if (i < 10) {
                if (i == 0) {
                    datas.add("Z");
                }
                datas.add("张三" + num + ":" + "张三 hello world" + num);
                if (i == 9) {
                    datas.add("展开∨");
                }
            } else if (i < 20) {
                if (i == 10) {
                    datas.add("Q");
                }
                datas.add("钱一" + num + ":" + "钱一 hello world" + num);
            } else {
                if (i == 20 ) {
                    datas.add("W");
                }
                datas.add("王二" + num + ":" + "王二 hello world" + num);
            }
        }

    }
    private void initDataNew() {
        datas.clear();
        Random r = new Random();
        for (int i = 0; i < 30; i++) {
            int num = r.nextInt(50);
            if (i < 10) {
                if (i == 0 ) {
                    datas.add("Z");
                }
                if (i == 9) {
                    for (int j = 0;j<5;j++) {
                        datas.add("张三" + num + j + ":" + "张三 hello world" + num + j);
                    }
                    datas.add("收起∧");
                    continue;
                }
                datas.add("张三" + num + ":" + "张三 hello world" + num);
            } else if (i < 20) {
                if (i == 10) {
                    datas.add("Q");
                }
                datas.add("钱一" + num + ":" + "钱一 hello world" + num);
            } else {
                if (i == 20) {
                    datas.add("W");
                }
                datas.add("王二" + num + ":" + "王二 hello world" + num);
            }
        }
    }

    static class MyAdapter extends BaseAdapter {
        private final int VIEW_TYPE_COUNT = 3;
        private final int TYPE_NORMAL = 0;
        private final int TYPE_CHAR = 1;
        private final int TYPE_BOTTOM = 2;
        private List<String> datas;
        private LayoutInflater inflater;
        private Context context;
        private OnClickListener onClickListener;

        public void setOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public MyAdapter(List datas, Context context) {
            this.datas = datas;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getItemViewType(int position) {
            if (datas.get(position).contains(":")) {
                return TYPE_NORMAL;
            } else if(datas.get(position).length() == 1){
                return TYPE_CHAR;
            } else {
                return TYPE_BOTTOM;
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        public void setDatas(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            boolean hasConvertView = convertView != null;
            int type = getItemViewType(position);
            ViewHolder holder = null;
            if (type == TYPE_NORMAL) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item, null, false);
                    holder = new ViewHolderNormal(convertView);
                    //findView(TYPE_NORMAL, convertView, holder);
                    convertView.setTag(holder);
                    //setItemAnim(convertView, position);
                } else {
                    holder = (ViewHolderNormal) convertView.getTag();
                }
                String[] data = datas.get(position).split(":");
                ((ViewHolderNormal)holder).tvName.setText(data[0]);
                ((ViewHolderNormal)holder).tvItem.setText(data[1]);
            } else if (type == TYPE_CHAR){
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.type, null, false);
                    holder = new ViewHolderType(convertView);
                   // findView(TYPE_CHAR, convertView, holderType);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolderType) convertView.getTag();
                }
                ((ViewHolderType)holder).tvType.setText(datas.get(position));

            } else if (type == TYPE_BOTTOM) {
                convertView = inflater.inflate(R.layout.type, null, false);
                final String data = datas.get(position);
                final TextView tv = (TextView) convertView.findViewById(R.id.tv_type);
                tv.setText(data);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundColor(Color.parseColor("#ffffff"));
                /*tv.setTag(true);*/
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
      /*                  boolean expand = (boolean) tv.getTag();
                        if (expand) {
                            tv.setText("展开∨");
                        } else {
                            tv.setText("收起∧");
                        }
                        tv.setTag(!expand);*/
                        boolean expand = "展开∨".equals(data);
                        if (onClickListener != null) {
                            onClickListener.bottomExpand(expand);
                        }
                    }
                });
            }
            setItemAnim(convertView, position,hasConvertView);
            return convertView;
        }

        private void findView(int type, View convertView, ViewHolder holder) {
            if (type == TYPE_NORMAL) {

                ((ViewHolderNormal) holder).tvName = (TextView) convertView.findViewById(R.id.tv_name);
                ((ViewHolderNormal) holder).tvItem = (TextView) convertView.findViewById(R.id.tv_item);
            } else {
                ((ViewHolderType) holder).tvType = (TextView) convertView.findViewById(R.id.tv_type);
            }
        }

        class ViewHolderNormal implements ViewHolder {
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_item)
            TextView tvItem;

            public ViewHolderNormal(View view) {
                ButterKnife.bind(this, view);
            }
        }

        class ViewHolderType implements ViewHolder {
            @BindView(R.id.tv_type)
            TextView tvType;

            ViewHolderType(View view) {
                ButterKnife.bind(this, view);
            }
        }

        private void setItemAnim(View convertView, int position,boolean hasConvertView) {
            if (!hasConvertView) {
                convertView.clearAnimation();
                Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.bottom_to_top);
                animation1.setStartOffset(100 * position);
                animation1.setFillAfter(true);
                AnimationSet set = new AnimationSet(false);
     /*       if (position%2 == 0) {
                Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.right_to_left);

                set.addAnimation(animation2);
            } else {
                Animation animation3 = AnimationUtils.loadAnimation(context, R.anim.left_to_right);
                set.addAnimation(animation3);
            }*/
                //set.setStartOffset(200*position);
                set.addAnimation(animation1);
                convertView.setAnimation(set);
            }
        }
        interface OnClickListener {
            void bottomExpand(boolean isExpand);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
        Log.e("MainActivity", "onDestroy");
        ViewServer.get(this).removeWindow(this);
    }

    /**
     * Layout动画
     *
     * @return
     */
    protected LayoutAnimationController getAnimationController() {
        int duration = 500;
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

    interface ViewHolder {

    }

    public static void initFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag, int postion) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(postion, targetFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 清楚listview动画集合
     */
    private void clearListTransitionAnims() {
        if (null != showlistView) {
            showlistView.clearDisappearingChildren();
        }
        for (View view : transitionViewList) {
            view.clearAnimation();
        }
        transitionViewList.clear();
    }


    /**
     * 进中间页或下一程的动画
     *
     * @param adapterView
     * @param position
     * @param duration
     */
    private void startExitTransition(AdapterView<?> adapterView, int position, int duration) {
        int firstPosition = adapterView.getFirstVisiblePosition();
        int endPosition = adapterView.getLastVisiblePosition();

        for (int i = firstPosition; i <= endPosition; i++) {
            int index = i - firstPosition;
            View itemView = adapterView.getChildAt(index);
            if (i < position) {
             CustomTranslateUtil.getInstance().hideViewWithAlpha(itemView, CustomTranslateUtil.DOWN_TO_TOP, duration, 200, true);
                transitionViewList.add(itemView);
            } else if (i > position) {
                CustomTranslateUtil.getInstance().hideViewWithAlpha(itemView, CustomTranslateUtil.TOP_TO_DOWN, duration, 200, true);
                transitionViewList.add(itemView);
            }
        }

        // fade out top and bottom view
        AlphaAnimation fadeAnim = new AlphaAnimation(1, 0);
        fadeAnim.setFillAfter(true);
        fadeAnim.setDuration(200);

        if (mTopView.getVisibility() == View.VISIBLE) {
            mTopView.startAnimation(fadeAnim);
            transitionViewList.add(mTopView);
        }

        if (bottomView.getVisibility() == View.VISIBLE) {
            bottomView.startAnimation(fadeAnim);
            transitionViewList.add(bottomView);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( content.getVisibility() == View.VISIBLE) {
            container.setVisibility(View.VISIBLE);
            content.setVisibility(View.INVISIBLE);
            return false;
        }
        //fragmentManager.getFragments().clear();
        return super.onKeyDown(keyCode, event);
    }

    private long lastClickTime = 0;
    @Override
    public void onBackPressed() {
        long currentClickTime = new Date().getTime();
        if ((currentClickTime - lastClickTime) < 1000) {
            finish();
        } else {
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_LONG).show();
        }
        lastClickTime = currentClickTime;

    }
}
