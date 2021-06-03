package com.yangyang.bookkeeping.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yangyang.bookkeeping.db.DBManager;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.User;

import cn.bmob.v3.BmobUser;

public class BookkeepingSettingActivity extends AppCompatActivity {
    BmobUser user = BmobUser.getCurrentUser(User.class);
    String uId = user.getObjectId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeeping_setting);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_setting_back:
                finish();
                break;
            case R.id.tv_setting_clear:
                showDeleteDialog();
                break;
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除提示")
                .setMessage("您确定要删除所有记录么？\n注意：删除后无法恢复，请慎重选择！")
                .setPositiveButton("取消",null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteAllAccount(uId);
                        Toast.makeText(BookkeepingSettingActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }
}
