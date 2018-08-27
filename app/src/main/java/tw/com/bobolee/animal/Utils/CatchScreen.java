package tw.com.bobolee.animal.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bohuilee on 2016/4/19.
 */
public class CatchScreen {
    private View CatchView;
    private Bitmap bitmap;
    private Context mContext;

    public CatchScreen(View view, Context context) {
        view.setDrawingCacheEnabled(true);
        this.CatchView = view;
        this.mContext = context;

        if (CatchView.getMeasuredHeight() <= 0) {
            WindowManager WM = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = WM.getDefaultDisplay();
            int specWidth = View.MeasureSpec.makeMeasureSpec(display.getWidth(), View.MeasureSpec.EXACTLY);
            int specHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            CatchView.measure(specWidth, specHeight);
            bitmap = Bitmap.createBitmap(CatchView.getMeasuredWidth(), CatchView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            CatchView.layout(0, 0, CatchView.getMeasuredWidth(), CatchView.getMeasuredHeight());
            CatchView.draw(c);
        } else {
            bitmap = Bitmap.createBitmap(CatchView.getMeasuredWidth(), CatchView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            CatchView.layout(CatchView.getLeft(), CatchView.getTop(), CatchView.getRight(), CatchView.getBottom());
            CatchView.draw(c);
        }
    }

    public void shareThoughIntent() {
        //Date now = new Date();
        //android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String now = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());

        try {

            File imagePath = new File(mContext.getCacheDir(), "images");
            imagePath.mkdirs();
            File imageFile = new File(imagePath, now + "_share.jpg");
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Uri uri = FileProvider.getUriForFile(mContext, "tw.com.bobolee.animal.fileprovider", imageFile);

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(shareIntent);
        } catch (Exception e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }
}
