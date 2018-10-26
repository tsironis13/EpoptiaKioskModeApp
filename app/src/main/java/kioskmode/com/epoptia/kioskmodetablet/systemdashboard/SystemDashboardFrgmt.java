package kioskmode.com.epoptia.kioskmodetablet.systemdashboard;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kioskmode.com.epoptia.pojo.LogoutWorkerRequest;
import kioskmode.com.epoptia.pojo.UploadImageResponse;
import kioskmode.com.epoptia.pojo.ValidateAdminResponse;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.app.utils.ImageUtls;
import kioskmode.com.epoptia.databinding.SystemDashboardFrgmtBinding;
import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;
import kioskmode.com.epoptia.kioskmodetablet.pdfviewer.PdfViewerFrgmt;
import kioskmode.com.epoptia.kioskmodetablet.stationworkers.StationWorkersFrgmt;
import kioskmode.com.epoptia.retrofit.APIClient;
import kioskmode.com.epoptia.retrofit.APIInterface;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by giannis on 5/9/2017.
 */

public class SystemDashboardFrgmt extends Fragment implements WebView.OnTouchListener {

    private static final String debugTag = SystemDashboardFrgmt.class.getSimpleName();
    private View mView;
    private SystemDashboardFrgmtBinding mBinding;
    private int stationId, workerId;
    private String cookie, url, subdomain, end_url, stationName, workerUsername;
    private APIInterface apiInterface, customHeadersApiInterface;
    private ImageUtls imageUtls;
    private static final int ACTION_IMAGE_CAPTURE = 900;
    private File output;
    private Uri photoURI;
    private int ordertrackID, delay;
    private boolean imageUploading, uploadImage, webviewIsDisabled;//uploadImage is used to check if image has to be uploaded to activities destroyed

    public static SystemDashboardFrgmt newInstance(int stationId, String cookie, String url, String stationName, String workerUsername, int workerId, String action) {
        Bundle bundle = new Bundle();
        bundle.putInt("station_id", stationId);
        bundle.putString("cookie", cookie);
        bundle.putString("url", url);
        bundle.putString("station_name", stationName);
        bundle.putString("worker_username", workerUsername);
        bundle.putInt("worker_id", workerId);
        //action => device, reboot
        bundle.putString("action", action);
        SystemDashboardFrgmt systemDashboardFrgmt = new SystemDashboardFrgmt();
        systemDashboardFrgmt.setArguments(bundle);
        return systemDashboardFrgmt;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null ) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.system_dashboard_frgmt, container, false);

            mView = mBinding.getRoot();
        }

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        SharedPrefsUtl.setBooleanPref(getActivity(), getResources().getString(R.string.is_working), true);
        imageUtls = new ImageUtls(getActivity());
        apiInterface = APIClient.getClient(SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.subdomain))).create(APIInterface.class);
        subdomain = SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.subdomain));
        if (savedInstanceState != null) {
            cookie = savedInstanceState.getString("cookie");
            end_url = savedInstanceState.getString("url");
            stationId = savedInstanceState.getInt("station_id");

            if (savedInstanceState.getBoolean("upload_image")) {
                ordertrackID = savedInstanceState.getInt("ordertrackid");
                output = (File) savedInstanceState.getSerializable("output");
                uploadImage(output);
            }
        } else {
            if (getArguments() != null) {
                if (getArguments().getString("action") != null && getArguments().getString("action").equals("device")) {
                    delay = 5;
                } else {
                    delay = 15000; //probably reboot
                }

                cookie = getArguments().getString("cookie");
                ((KioskModeActivity)getActivity()).setCookie(cookie);
                end_url = getArguments().getString("url");
                ((KioskModeActivity)getActivity()).setUrl(end_url);
                stationId = getArguments().getInt("station_id");
                stationName = getArguments().getString("station_name");
                workerUsername = getArguments().getString("worker_username");
                workerId = getArguments().getInt("worker_id");
            }
        }
        url = constructFullUrl(end_url);
        //Log.e(debugTag, "URL: "+url);
        if (stationName == null) stationName = SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.stationame));
        if (workerUsername == null) workerUsername = SharedPrefsUtl.getStringFlag(getActivity(), "worker_username");
        if (workerId == 0) workerId = SharedPrefsUtl.getIntFlag(getActivity(), "worker_id");
        setToolbarTitle();
        ((KioskModeActivity)getActivity()).getToolbarTextViewUsernameRight().setText(workerUsername);
        if (getActivity() != null && isAdded()) {
            initializeView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                uploadImage(output);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cookie", cookie);
        outState.putString("url", end_url);
        if (output != null) outState.putSerializable("output", output);
        outState.putInt("ordertrackid", ordertrackID);
        outState.putBoolean("upload_image", uploadImage);
        outState.putInt("station_id", stationId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.logoutWorkerItem).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutWorkerItem:
                if (isNetworkAvailable()) {
                    if (!imageUploading) {
                        LogoutWorkerRequest request = new LogoutWorkerRequest();
                        request.setAction("logout_worker");
                        request.setUserId(workerId);
                        String accessToken = SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.access_token));
                        request.setAccess_token(accessToken);
                        /**
                         GET List Resources
                         **/
                        Call<ValidateAdminResponse> responseCall = apiInterface.logoutWorker(request);
                        responseCall.enqueue(new Callback<ValidateAdminResponse>() {
                            @Override
                            public void onResponse(Call<ValidateAdminResponse> call, Response<ValidateAdminResponse> response) {
                                if (response.body().getCode() == 200) {
                                    if (getActivity() != null) {
                                        SharedPrefsUtl.removeStringkey(getActivity(), "cookie");

                                        getActivity().getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.kioskModeLlt, StationWorkersFrgmt.newInstance(stationId, stationName), getResources().getString(R.string.station_workers_frgmt))
                                                    .commit();
                                    }
                                } else {
                                    showSnackBrMsg(getResources().getString(R.string.error), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                                }
                            }

                            @Override
                            public void onFailure(Call<ValidateAdminResponse> call, Throwable t) {
                                showSnackBrMsg(getResources().getString(R.string.error), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                            }
                        });
                    }
                } else {
                    showSnackBrMsg(getResources().getString(R.string.no_connection), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.performClick();
        return webviewIsDisabled;
    }

    @SuppressLint("AddJavascriptInterface")
    public void initializeView() {
        mBinding.setLoading(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.setLoading(false);
                if (getActivity() != null && isAdded()) {
                    if (isNetworkAvailable()) {
                        if (mBinding.getHaserror()) mBinding.setHaserror(false);
                        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mBinding.webView.getContext());
                        CookieManager cookieManager = CookieManager.getInstance();
                        cookieManager.setAcceptCookie(true);
                        cookieManager.removeSessionCookie();
                        cookieManager.setCookie(url, cookie);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            cookieManager.flush();
                        } else {
                            cookieSyncManager.sync();
                        }
                        mBinding.webView.getSettings().setJavaScriptEnabled(true);
                        mBinding.webView.getSettings().setBuiltInZoomControls(true);
                        mBinding.webView.getSettings().setDisplayZoomControls(false);

                        mBinding.webView.addJavascriptInterface(new Object()
                        {
                            @JavascriptInterface           // For API 17+
                            public void performClick(int id)
                            {
                                ordertrackID = id;
                                if (!imageUploading) openCamera();
                            }
                        }, "uploadPhoto");
                        mBinding.webView.addJavascriptInterface(new Object()
                        {
                            @JavascriptInterface           // For API 17+
                            public void performClick(String path)
                            {
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.kioskModeLlt, PdfViewerFrgmt.newInstance(path, stationId, cookie, end_url, stationName, workerUsername, workerId), getResources().getString(R.string.pdfviewer_frgmt))
                                        .addToBackStack(getResources().getString(R.string.pdfviewer_frgmt))
                                        .commit();
                            }
                        }, "pdfView");
                        mBinding.webView.addJavascriptInterface(new Object()
                        {
                            @JavascriptInterface           // For API 17+
                            public void onHiddenInputFocusOnBarcodeHit()
                            {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideKeyboard();
                                    }
                                }, 500);
                            }
                        }, "barcode");
                        mBinding.webView.setWebViewClient(new WebViewClient());
                        mBinding.webView.loadUrl(url);
                    } else {
                        mBinding.setHaserror(true);
                        mBinding.setErrortext(getResources().getString(R.string.no_connection));
                    }
                }
            }
        }, delay);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private boolean isNetworkAvailable() {
        if (getActivity() == null) return false;
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void showSnackBrMsg(String msg, View container, int length) {
        Snackbar snackbar = Snackbar.make(container, msg, length);
        snackbar.show();
    }

    private void setToolbarTitle() {
        int currentOrientation = getResources().getConfiguration().orientation;

        String title;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            title = getResources().getString(R.string.workers_frgmt_title) + " " + stationName;
        } else {
            int stationNameLength = stationName.length();

            if (stationNameLength > 15) {
                String substr = stationName.substring(0, 15);

                title = getResources().getString(R.string.workers_frgmt_portrait_title) + " " + substr + "...";
            } else {
                title = getResources().getString(R.string.workers_frgmt_portrait_title) + " " + stationName;
            }
        }

        ((KioskModeActivity)getActivity()).getToolbarTextViewTitle().setText(title);
    }

    private void deleteAppStorageImage(File file) {
        imageUtls.deleteAppStorage(file);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                output = imageUtls.createImageFile(".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (output != null) {
                uploadImage = true;
                photoURI = imageUtls.getUriForFile(output);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = ClipData.newUri(getActivity().getContentResolver(), "photo", photoURI);
                    intent.setClipData(clip);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                else {
                    List<ResolveInfo> resInfoList = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        getActivity().grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                }
                startActivityForResult(intent, ACTION_IMAGE_CAPTURE);
            }
        }
    }

    private void uploadImage(File file) {
        MultipartBody.Part mfile;
        final Snackbar snackbar = Snackbar.make(mBinding.containerLnlt, getResources().getString(R.string.image_uploading), Snackbar.LENGTH_INDEFINITE);
        webviewIsDisabled = true;
        mBinding.webView.setOnTouchListener(this);
        snackbar.show();
        imageUploading = true;
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            String cookies = cookieManager.getCookie(url);

            customHeadersApiInterface = APIClient.getClientWithCustomHeaders(SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.subdomain)), cookies).create(APIInterface.class);
            mfile = imageUtls.getRequestFileBody(file);
            RequestBody action = RequestBody.create(okhttp3.MultipartBody.FORM, "upload_image");
            RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.access_token)));
            RequestBody order_line_track_id = RequestBody.create(okhttp3.MultipartBody.FORM, ordertrackID+"");
            Call<UploadImageResponse> uploadImageCall = customHeadersApiInterface.uploadImage(action, token, order_line_track_id, mfile);
            uploadImageCall.enqueue(new Callback<UploadImageResponse>() {
                @Override
                public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                    uploadImage = false;
                    snackbar.dismiss();
                    webviewIsDisabled = false;
                    if (response.body().getCode() == 200) {
                        Snackbar.make(mBinding.containerLnlt, getResources().getString(R.string.image_uploaded_successfully), Snackbar.LENGTH_LONG).show();
                        mBinding.webView.reload();
                    }
                    imageUploading = false;
                    deleteAppStorageImage(output);
                }

                @Override
                public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                    webviewIsDisabled = false;
                    uploadImage = false;
                    imageUploading = false;
                    snackbar.dismiss();
                    deleteAppStorageImage(output);
                }
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private String constructFullUrl(String url) {
        return "http://"+subdomain+".epoptia.com/"+url;
    }
}
