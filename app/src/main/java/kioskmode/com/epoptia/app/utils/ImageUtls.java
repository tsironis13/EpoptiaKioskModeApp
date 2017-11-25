package kioskmode.com.epoptia.app.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by giannis on 20/11/2017.
 */

public class ImageUtls {

    private Context mContext;

    public ImageUtls(Context context) {
        this.mContext = context;
    }

    public File createImageFile(String extension) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File image;
        //check external storage availability
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            image = File.createTempFile(timeStamp, extension, mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        } else {
            image = File.createTempFile(timeStamp, extension, mContext.getFilesDir());
        }
        return image;
    }

    public Uri getUriForFile(File output) {
        return FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", output);
    }

    public MultipartBody.Part getRequestFileBody(File file) throws IllegalArgumentException {
//        RequestBody requestFile = RequestBody.create(MultipartBody.FORM, file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    public void deleteAppStorage(File file) {
        if (file != null) {
            boolean deleted = file.delete();
        }
    }

}
