package com.magus.trainingfirstapp.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.magus.trainingfirstapp.R;
import com.magus.trainingfirstapp.base.field.G;
import com.magus.trainingfirstapp.module.activity_life.ActivityA;
import com.magus.trainingfirstapp.module.circle_menu.CircleMenuActivity;
import com.magus.trainingfirstapp.module.images.DisplayingBitmapsActivity;
import com.magus.trainingfirstapp.module.myanim.MyAnimActivity;
import com.magus.trainingfirstapp.module.frgment_demo.FragmentMainActivity;
import com.magus.trainingfirstapp.module.networkusage_demo.NetworkActivity;
import com.magus.trainingfirstapp.module.other_activity.OtherActivity;
import com.magus.trainingfirstapp.module.swipe_menu.SwipeMenuDemoActvity;
import com.magus.trainingfirstapp.module.photobyintent.PhotoIntentActivity;
import com.magus.trainingfirstapp.utils.CommontUtils;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TrainingFirstActivity extends BaseActivity {

    private ImageView takePhotoThenToShowImg;
    private LinearLayout first_module_content_lly;
    private final int MODULE_BUTTON_ITEM_ID = G.FlagsConst.BUTTON_ITEM_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_training_first);
        first_module_content_lly = (LinearLayout) findViewById(R.id.first_module_content_lly);
        takePhotoThenToShowImg = (ImageView) findViewById(R.id.first_image_content);
        ((TextView)findViewById(R.id.first_show_tv)).setText(Build.MODEL);
        initModule();


    }

    private void initModule() {
        // 添加所有的按钮模块
        for(int i=0; i< G.ModuleConst.FIRST_ACTIVITY_MODULE_BUTTON_COUNT; i++){
            addButton(i);
        }
    }

    /**
     * 添加模块按钮
     * @param tagId
     */
    private void addButton(int tagId) {
        if (first_module_content_lly.getChildCount() >1){   //本来存放着一个ImageView因此当子布局大于1个的时候加线条
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommontUtils.dip2px(1)));
            textView.setBackgroundColor(getResources().getColor(R.color.first_module_line));
            first_module_content_lly.addView(textView);
        }

        Button button = new Button(this);
        button.setText(G.getModuleBtnName(this, tagId));
        button.setId(MODULE_BUTTON_ITEM_ID);
        button.setTag(tagId);
        button.setOnClickListener(this);
        button.setBackgroundResource(R.drawable.my_button_bg);
        first_module_content_lly.addView(button);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_training_fisrt_acitivity, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ShareActionProvider shareActionProvider = (ShareActionProvider) (menu.findItem(R.id.menu_item_share)).getActionProvider();
            shareActionProvider.setShareIntent(shareText());
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                showToast("action_settings");
                break;
            case R.id.menu_item_share:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    startActivity(shareText());
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()){
            case MODULE_BUTTON_ITEM_ID:
                intent = onModuleBtnClick((Integer) v.getTag());
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }
    }

    /**
     * 模块的点击事件
     * @param tagId
     */
    private Intent onModuleBtnClick(int tagId) {
        switch (tagId) {
            case 0:
                return new Intent(this, FragmentMainActivity.class);
            case 1:
                Uri number = Uri.parse("tel:10086");
                return new Intent(Intent.ACTION_DIAL, number);
            case 2:
                try {
                    Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
                    startActivity(new Intent(Intent.ACTION_VIEW, location));
                } catch (Exception e) {
                    showToast("没有地图");
                }
                return null;
            case 3:
                Uri webPage = Uri.parse(G.UrlConst.CSDN_BLOG);
                return  new Intent(Intent.ACTION_VIEW, webPage);
            case 4:
                sendMsg("ssss");
            case 5:
                Intent otherintent = new Intent(TrainingFirstActivity.this, OtherActivity.class);
                otherintent.putExtra("key", "key i come on");
                return otherintent;
            case 6:
                return Intent.createChooser(shareText(), "选啊你");
            case 10:
                return new Intent(TrainingFirstActivity.this, SwipeMenuDemoActvity.class);
            case 7:
                return new Intent(TrainingFirstActivity.this, PhotoIntentActivity.class);
            case 8:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, G.FlagsConst.REQUEST_IMAGE_CAPTURE);
                }
                return null;
            case 9:   //保存拍摄到的原图
                Intent takePictureOintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureOintent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (photoFile != null) {
                        takePictureOintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureOintent, G.FlagsConst.REQUEST_IMAGE_CAPTURE_O);
                    }
                }
                return null;
            case 11:
                return new Intent(TrainingFirstActivity.this, CircleMenuActivity.class);
            case 12:
                showToast("大家好");
                return new Intent(TrainingFirstActivity.this, ActivityA.class);
            case 13:
                return new Intent(TrainingFirstActivity.this, DisplayingBitmapsActivity.class);
            case 14:
                return new Intent(TrainingFirstActivity.this, MyAnimActivity.class);
            case 15:
                return new Intent(TrainingFirstActivity.this, NetworkActivity.class);
        }
        return null;
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(TrainingFirstActivity.this, OtherActivity.class);
        EditText editText = (EditText) findViewById(R.id.message_et);
        String message = editText.getText().toString();
        intent.putExtra(G.MessageConst.MESSAGE, message);
        startActivity(intent);
    }

    private Intent shareText() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.hellowShuaiYang));
        shareIntent.setType("text/plain");
        return shareIntent;
    }

    public void sendMsg(String content) {
        SmsManager smsManager = SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage(content);
        for (String text : divideContents) {
            smsManager.sendTextMessage("1000", null, text, null, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == G.FlagsConst.REQUEST_IMAGE_CAPTURE_O && resultCode == RESULT_OK) {
            int targetW = takePhotoThenToShowImg.getWidth();
            int targetH = takePhotoThenToShowImg.getHeight();

		/* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            }

		/* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
            takePhotoThenToShowImg.setImageBitmap(bitmap);
            galleryAddPic();
        }
        if (requestCode == G.FlagsConst.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {//展示图片
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            takePhotoThenToShowImg.setImageBitmap(imageBitmap);
        }
    }

    private String mCurrentPhotoPath;

    /**
     * 创建拍摄的图片的存储路径及文件名
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.d("TrainingFirstActivity", "storageDir:" + storageDir);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("image.getAbsolutePath()", image.getAbsolutePath() + "");
        return image;
    }

    /**
     * 将拍摄到的照片添加到Media Provider的数据库中
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}