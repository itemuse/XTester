package com.xy.dialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.toast.XToast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XApp.init(getApplication());
        init();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.showNormalDialog:
                showNormalDialog();
                break;
            case R.id.showMultiBtnDialog:
                showMultiBtnDialog();
                break;
            case R.id.showListDialog:
                showListDialog();
                break;
            case R.id.showSingleChoiceDialog:
                showSingleChoiceDialog();
                break;
            case R.id.showMultiChoiceDialog:
                showMultiChoiceDialog();
                break;
            case R.id.showProgressDialog:
                showProgressDialog();
                break;
            case R.id.showInputDialog:
                showInputDialog();
                break;
            case R.id.showCustomizeDialog:
                showCustomizeDialog();
                break;
            case R.id.ExpandableListView:
                showRerverseDialog();
                break;
        }
    }

    private ArrayList<String> ParentList = new ArrayList<>();
    private ArrayList<ArrayList<String>> ChildLists = new ArrayList<>();
    private ExpandableListView mExpandableListView;

    /**????????????*/
    private int mParentmValue = 0;
    private int mChildValue = 0;
    /**??????????????????*/
    private int mParentmValue2 = 0;
    private int mChildValue2 = 0;
    protected void init() {
        ParentList = new ArrayList<>();
        for (CharSequence ss : getResources().getTextArray(R.array.parent_item)){
            ParentList.add((String)ss);
        }
        ArrayList<String> childLists1 = new ArrayList<>();
        for (CharSequence ss : getResources().getTextArray(R.array.child_1)){
            childLists1.add((String)ss);
        }
        ArrayList<String> childLists2 = new ArrayList<>();
        for (CharSequence ss : getResources().getTextArray(R.array.child_2)){
            childLists2.add((String)ss);
        }
        ArrayList<String> childLists3 = new ArrayList<>();
        for (CharSequence ss : getResources().getTextArray(R.array.child_3)){
            childLists3.add((String)ss);
        }
        ChildLists = new ArrayList<>();
        ChildLists.add(childLists1);
        ChildLists.add(childLists2);
        ChildLists.add(childLists3);
    }

    private void showRerverseDialog() {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_reserse,null);
        dialogView.findViewById(R.id.btn_reverse_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog();
            }
        });

        mExpandableListView = dialogView.findViewById(R.id.expandablelistview);
        final MultistageAdapter moAdapter = new MultistageAdapter(this,ParentList, ChildLists, mParentmValue, mChildValue);
        mExpandableListView.setAdapter(moAdapter);
        customizeDialog.setView(dialogView);
        customizeDialog.show();
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                mParentmValue2 =  groupPosition;
                mChildValue2 = 0;
                moAdapter.notifyDataSetChanged(mParentmValue2,mChildValue2);
                return false;
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int reverseTypePosition, int reverseCisPosition, long id) {
                mParentmValue2 =  reverseTypePosition;
                mChildValue2 =  reverseCisPosition;
                moAdapter.notifyDataSetChanged(mParentmValue2,mChildValue2);
                return false;
            }
        });
    }

    private void showConfirmDialog(){
        final String value = ParentList.get(mParentmValue2)+"/"+ChildLists.get(mParentmValue2).get(mChildValue2);
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_camera,null);
        ((TextView)dialogView.findViewById(R.id.title)).setText(value);
        ((TextView)dialogView.findViewById(R.id.message)).setText(mParentmValue2 != 2?getString(R.string.camera12_prompt):getString(R.string.camera10_prompt));
        ((ImageView)dialogView.findViewById(R.id.icon)).setImageDrawable(mParentmValue2 != 2?getDrawable(R.drawable.pin_icon12):getDrawable(R.drawable.pin_icon10));
        customizeDialog.setView(dialogView);
        final AlertDialog alertDialog = customizeDialog.create();
        dialogView.findViewById(R.id.canal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParentmValue =  mParentmValue2;
                mChildValue =  mChildValue2;
                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }
    /**???????????????**/
    private void showNormalDialog(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("DiaLog Title");
        normalDialog.setMessage("DiaLog Message");
        normalDialog.setPositiveButton("Positive",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XToast.getInstance().Text("Positive").show();
                    }
                });
        normalDialog.setNegativeButton("Negative",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XToast.getInstance().Text("Negative").show();
                    }
                });
        normalDialog.show();
    }
    /**??????????????? ???????????????????????????**/
    private void showMultiBtnDialog(){
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("DiaLog Title").setMessage("DiaLog Message");
        normalDialog.setPositiveButton("Positive",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XToast.getInstance().Text("Positive").show();
                    }
                });
        normalDialog.setNeutralButton("Neutral2",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XToast.getInstance().Text("Neutral2").show();
                    }
                });
        normalDialog.setNegativeButton("Negative", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                XToast.getInstance().Text("Negative").show();
            }
        });
        // ?????????????????????
        normalDialog.show();
    }
    /**?????????????????????**/
    private void showListDialog() {
        final String[] items = { "items1","items2","items3","items4" };
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(MainActivity.this);
        listDialog.setTitle("DiaLog Title");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                XToast.getInstance().Text("?????? "+items[which]).show();
            }
        });
        listDialog.show();
    }
    /**?????????????????????**/
    int mChoice;
    private void showSingleChoiceDialog(){
        final String[] items = { "items1","items2","items3","items4" };
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(MainActivity.this);
        singleChoiceDialog.setTitle("DiaLog Title");
        // ????????????????????????????????????????????????0
        singleChoiceDialog.setSingleChoiceItems(items, mChoice,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("Positive",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mChoice != -1) {
                            XToast.getInstance().Text("?????? "+items[mChoice]).show();
                        }
                    }
                });
        singleChoiceDialog.show();
    }
    /**?????????????????????**/
    ArrayList<String> mChoices = new ArrayList<>();
    final boolean initChoiceSets[]={false,false,false,false};
    private void showMultiChoiceDialog() {
        final String[] items = { "items1","items2","items3","items4" };
        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(this);
        multiChoiceDialog.setTitle("DiaLog Title");
        multiChoiceDialog.setMultiChoiceItems(items, initChoiceSets,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        initChoiceSets[which] = isChecked;
                        if (isChecked) {
                            mChoices.add(items[which]);
                        } else {
                            String  s  = items[which];
                            XLog.i("md"," findInterIdex(mChoices,s):  "+findInterIdex(mChoices,s));
                            mChoices.remove(findInterIdex(mChoices,s));
                        }
                    }
                });
        multiChoiceDialog.setPositiveButton("Positive",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int size = mChoices.size();
                        String str = "";
                        for (int i = 0; i < size; i++) {
                            str += mChoices.get(i) ;
                        }
                        XToast.getInstance().Text("????????? "+str).show();
                    }
                });
        multiChoiceDialog.show();
    }
    public static int findInterIdex(List<String> nums, String target){
        return Collections.binarySearch(nums,target);
    }
    /**???????????????**/
    private void showProgressDialog() {
        final int MAX_PROGRESS = 100;
        final ProgressDialog progressDialog =
                new ProgressDialog(MainActivity.this);
        progressDialog.setProgress(0);
        progressDialog.setTitle("ProgressDialog Title");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(MAX_PROGRESS);
        progressDialog.show();
        /* ???????????????????????????
         * ???????????????????????????100ms???????????????1
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                int progress= 0;
                while (progress < MAX_PROGRESS){
                    try {
                        Thread.sleep(100);
                        progress++;
                        progressDialog.setProgress(progress);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                // ???????????????????????????????????????
                progressDialog.cancel();
            }
        }).start();
    }
    /**???????????????**/
    private void showInputDialog() {
        final EditText editText = new EditText(MainActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("AlertDialog Title").setView(editText);
        inputDialog.setPositiveButton("Positive",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XToast.getInstance().Text("????????? "+editText.getText().toString()).show();
                    }
                }).show();
    }
    /**????????????????????????**/
    private void showCustomizeDialog() {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.dialog_customize,null);
        customizeDialog.setTitle("?????????AlertDialog Title");
        customizeDialog.setView(dialogView);
        dialogView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XToast.getInstance().Text("????????? " + ((EditText) dialogView.findViewById(R.id.edit_text)).getText().toString()).show();
            }
        });
        customizeDialog.show();
    }
}