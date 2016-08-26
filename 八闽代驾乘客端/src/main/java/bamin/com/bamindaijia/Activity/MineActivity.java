package bamin.com.bamindaijia.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.UILUtils;
import com.aiton.administrator.shane_library.shane.widget.AvatarImageView;
import com.aiton.administrator.shane_library.shane.widget.WheelView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;

import bamin.com.bamindaijia.R;
import bamin.com.bamindaijia.constant.Constant;
import bamin.com.bamindaijia.model.User;
import cz.msebera.android.httpclient.Header;

public class MineActivity extends ZjbBaseActivity implements View.OnClickListener {

    private PopupWindow mPopupWindow;
    private User mUser;
    private boolean IsLogin;
    private AvatarImageView mImg_Avatar;
    private Uri photoUri;
    private final int PIC_FROM＿LOCALPHOTO = 0;
    private final int PIC_FROM_CAMERA = 1;
    private String mImage;
    private String mPhoneNum;
    private ACache aCache;
    private TextView mTv_Name;
    private static final String[] sexArr = new String[]{"男", "女"};
    private static final String[] ageArr = new String[]{"90后", "80后", "70后", "60后", "50后"};
    private PopupWindow mChoosePopupWindow;
    private TextView mTv_User_Sex;
    private WheelView wheelview;
    private TextView mTv_User_Age;
    private String imgUrl;
    private User.DatasEntity datas;
    private TextView mTv_QianMing;
    private TextView mTv_User_Phone;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        User.DatasEntity datas = mUser.getDatas();
        if (datas != null) {
            mImage = datas.getImage();
            mPhoneNum = datas.getPhone();
            name = datas.getName();
            String sex = datas.getReserve3();
            String age = datas.getReserve4();

            String phone = datas.getPhone();
            int id = datas.getId();
            if (TextUtils.isEmpty(mImage)) {
                String Path = "/upload/" + mPhoneNum + "upload.jpeg";
                File pictureFile = new File(Environment.getExternalStorageDirectory(), Path);
                if (pictureFile.exists()) {
                    Uri uri = Uri.fromFile(pictureFile);
                    Bitmap bitmap = decodeUriAsBitmap(uri);
                    mImg_Avatar.setImageBitmap(bitmap);
                    Log.e("checkLogin", "本地图片存在");
                } else {
                    Log.e("checkLogin", "本地图片不存在");
                    mImg_Avatar.setImageResource(R.mipmap.ic_avatar);
                }
            } else {
                if (mImage != null) {
                    Log.e("checkLogin", "图片URL" + mImage);
                    UILUtils.displayImageNoAnim(mImage, mImg_Avatar);
                }
            }
//            if (sex != null && !TextUtils.isEmpty(sex)) {
//                mTv_User_Sex.setText(sex);
//
//            }
//            if (age != null && !TextUtils.isEmpty(age)) {
//                mTv_User_Age.setText(age);
//
//            }
            if (name != null && !TextUtils.isEmpty(name)) {
                mTv_Name.setText(name);
            }

            if (!TextUtils.isEmpty(phone)) {
                mImg_Avatar.setFileName(phone + "upload");
                mTv_User_Phone.setText(phone);
            }
        }

    }


    @Override
    protected void setListeners() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        findViewById(R.id.rela_user_sex).setOnClickListener(this);
        findViewById(R.id.rela_user_avatar).setOnClickListener(this);
        findViewById(R.id.rela_user_name).setOnClickListener(this);
        findViewById(R.id.rela_uesr_age).setOnClickListener(this);
        findViewById(R.id.rela_user_qianming).setOnClickListener(this);

        //头像控件监听
        mImg_Avatar.setAfterCropListener(new AvatarImageView.AfterCropListener() {
            @Override
            public void afterCrop(Bitmap photo) {
                Toast.makeText(MineActivity.this, "设置新的头像成功", Toast.LENGTH_SHORT).show();

                Log.e("MainActivity", "afterCrop: --->>" + mImg_Avatar.getFileName());
                postFile(mImg_Avatar.getFileName());
            }
        });
    }

    @Override
    protected void initSP() {
        aCache = ACache.get(this);
        mUser = (User) aCache.getAsObject(Constant.ACACHE.USER);

        datas = mUser.getDatas();
        imgUrl = datas.getImage();


        if (mUser == null) {

            Log.e("getSP ", "user为空");
        } else {

            Log.e("getSP ", "存储的电话号码" + mUser.getDatas().getPhone());
            Log.e("getSP ", "user状态" + mUser.getStatus());
        }
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        mImg_Avatar = (AvatarImageView) findViewById(R.id.imageView_touXiang);
        mTv_Name = (TextView) findViewById(R.id.tv_user_name);
        mTv_User_Sex = (TextView) findViewById(R.id.tv_user_sex);
        mTv_User_Age = (TextView) findViewById(R.id.tv_user_age);
        mTv_QianMing = (TextView) findViewById(R.id.tv_user_qianming);
        mTv_User_Phone = (TextView) findViewById(R.id.tv_user_phone);
    }


//    /**
//     * 选择的popup
//     */
//    private void setChoosePopupWindows(String[] arr) {
//        View inflate = getLayoutInflater().inflate(R.layout.popup_user_choose, null);
//
//        inflate.findViewById(R.id.popup_choose_close).setOnClickListener(this);
//        inflate.findViewById(R.id.popup_choose_sure).setOnClickListener(this);
//        wheelview = (WheelView) inflate.findViewById(R.id.wheel_view_wv);
//        //设置wheel的内容
//        wheelview.setItems(Arrays.asList(arr));
//
//        //最后一个参数为true，点击PopupWindow消失,宽必须为match，不然肯呢个会导致布局显示不完全
//        mChoosePopupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        //设置外部点击无效
//        mChoosePopupWindow.setOutsideTouchable(false);
//        //设置背景变暗
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 1f;
//        getWindow().setAttributes(lp);
//        //当popup关闭后背景恢复
//        mChoosePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
//            }
//        });
//        //设置popupwindows动画
//        mChoosePopupWindow.setAnimationStyle(R.style.AnimFromButtomToTop);
//        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//        BitmapDrawable bitmapDrawable = new BitmapDrawable();
//        mChoosePopupWindow.setBackgroundDrawable(bitmapDrawable);
//        mChoosePopupWindow.showAtLocation(inflate, Gravity.BOTTOM, 0, 0);
//    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String userName = mTv_Name.getText().toString().trim();
        String image = mUser.getDatas().getImage();
        if (userName.equals("请填写姓名")) {
            userName = "";
        }
        switch (v.getId()) {
            case R.id.imageView_back:
                intent.putExtra(Constant.INTENT_KEY.USER_NAME,userName);
                intent.putExtra(Constant.INTENT_KEY.USER_AVATAR,image);
                setResult(Constant.REQUEST_RESULT_CODE.USER_INFO,intent);
                finishTo();
                break;

            case R.id.rela_user_name:
                intent.setClass(this, ChangeNameActivity.class);
                intent.putExtra(Constant.INTENT_KEY.CHANGE_NAME, mTv_Name.getText().toString());
                startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.CHANGE_NAEM);

                break;
//            case R.id.rela_user_sex://性别选择
//                setChoosePopupWindows(sexArr);
//                break;
//            case R.id.popup_choose_close:
//                mChoosePopupWindow.dismiss();
//                break;
//            case R.id.popup_choose_sure:
//                String seletedItem = wheelview.getSeletedItem();
//                //如果选中的信息长度为1，则为性别信息，否则为年龄
//                if (seletedItem.length() == 1) {
//                    mTv_User_Sex.setText(seletedItem);
//
//                } else {
//                    mTv_User_Age.setText(seletedItem);
//                }
//                Log.e("MineActivity", "length: --->>" + seletedItem.length());
//
//                Log.e("MineActivity", "seletedItem: --->>" + seletedItem);
//
//
//                mChoosePopupWindow.dismiss();
//                break;
//            case R.id.rela_uesr_age://年龄选择
//                setChoosePopupWindows(ageArr);
//                break;
//            case R.id.rela_user_qianming://签名选择
//                intent.setClass(this,QianmingActivity.class);
//                startActivityForResultTo(intent,Constant.REQUEST_RESULT_CODE.QIAM_MING);
//                break;
//            case R.id.rela_user_sex://性别选择
//                setChoosePopupWindows(sexArr);
//                break;
//            case R.id.popup_choose_close:
//                mChoosePopupWindow.dismiss();
//                break;
//            case R.id.popup_choose_sure:
//                String seletedItem = wheelview.getSeletedItem();
//                //如果选中的信息长度为1，则为性别信息，否则为年龄
//                if (seletedItem.length() == 1) {
//                    mTv_User_Sex.setText(seletedItem);
//
//                } else {
//                    mTv_User_Age.setText(seletedItem);
//                }
//                Log.e("MineActivity", "length: --->>" + seletedItem.length());
//
//                Log.e("MineActivity", "seletedItem: --->>" + seletedItem);
//
//
//                mChoosePopupWindow.dismiss();
//                break;
//            case R.id.rela_uesr_age://年龄选择
//                setChoosePopupWindows(ageArr);
//                break;
//            case R.id.rela_user_qianming://签名选择
//                intent.setClass(this, QianMingActivity.class);
//                startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.QIAM_MING);
//                break;
            default:
                break;
        }
    }

//

    @Override
    protected void onStop() {
        super.onStop();
        pushUserInfo();


    }

    private void pushUserInfo() {
        String name = mTv_Name.getText().toString().trim();
//        String age = mTv_User_Age.getText().toString().trim();

        if (name != "请填写姓名" && name != mUser.getDatas().getName()) {


//        if (age != "请选择") {
//            datas.setReserve4(age);
//        }

            String url = Constant.Url.USER_INFO;
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("id", mUser.getDatas().getId());
            params.put("name", name);
            params.put("phone", mUser.getDatas().getPhone());

            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    try {
                        mUser = GsonUtils.parseJSON(s, User.class);
                        if (mUser.getStatus() == 0) {

                            aCache.put(Constant.ACACHE.USER, mUser, 30 * ACache.TIME_DAY);

                            Log.e("MineActivity", "用户名字: --->>" + mUser.getDatas().getName());

                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(MineActivity.this, "用户信息保存失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //保存用户信息在本地


        Log.e("MineActivity", "onStop:imgUrl>>" + imgUrl);
        Log.e("MineActivity", "onStop:Name>>" + name);
//        Log.e("MineActivity", "onStop:imgUrl>>" + age);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_RESULT_CODE.CHANGE_NAEM) {
            if (resultCode == Constant.REQUEST_RESULT_CODE.CHANGE_NAEM) {
                String name = data.getStringExtra(Constant.INTENT_KEY.CHANGE_NAME);
                Log.e("MineActivity", "name: --->>" + name);

                mTv_Name.setText(name);
            }
        }
        if (requestCode == Constant.REQUEST_RESULT_CODE.QIAM_MING) {
            if (resultCode == Constant.REQUEST_RESULT_CODE.QIAM_MING) {
                String qianming = data.getStringExtra(Constant.INTENT_KEY.QIAN_MING);
                mTv_QianMing.setText(qianming);
            }
        }
        //在拍照、选取照片、裁剪Activity结束后，调用的方法
        if (mImg_Avatar != null) {
            mImg_Avatar.onActivityResult(requestCode, resultCode, data);
        }

    }

    /**
     * 上传图片
     *
     * @param uploadFile
     */
    public void postFile(Uri uploadFile) {
        String path = uploadFile.getPath();
        Log.e("postFile", "path" + path);
        File file = new File(path);

        String actionUrl = Constant.Url.CUSTOMERIMAGE;
        if (file.exists() && file.length() > 0) {
            RequestParams params = new RequestParams();
            params.put("customer_id", mUser.getDatas().getId());
            Log.e("postFile", "用户ID" + mUser.getDatas().getId());
            try {
                params.put("data", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post(actionUrl, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.e("onSuccess: ", "---> " + new String(responseBody));
                    imgUrl = new String(responseBody);
                    datas.setImage(imgUrl);
                    mUser.setDatas(datas);
                    aCache.put(Constant.ACACHE.USER, mUser, 30 * ACache.TIME_DAY);
                    Log.e("MineActivity", "imgUrl:>>" + imgUrl);


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("onFailure", "失败");
                }
            });
        } else {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
        }
        ImageLoader.getInstance().clearDiskCache();
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

}
