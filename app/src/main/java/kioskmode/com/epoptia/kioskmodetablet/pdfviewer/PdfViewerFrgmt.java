package kioskmode.com.epoptia.kioskmodetablet.pdfviewer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.databinding.PdfviewerFrgmtBinding;
import kioskmode.com.epoptia.interfaces.HttpConnectionListener;
import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;
import kioskmode.com.epoptia.kioskmodetablet.stationworkers.StationWorkersFrgmt;
import kioskmode.com.epoptia.kioskmodetablet.systemdashboard.SystemDashboardFrgmt;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;

import static android.app.Activity.RESULT_OK;

public class PdfViewerFrgmt extends Fragment {
    private static final String debugTag = PdfViewerFrgmt.class.getSimpleName();
    private View mView;
    private PdfviewerFrgmtBinding mBinding;
    private PDFView pdfView;
    private HttpURLConnection conn;
    private URL uri;
    private String path, url, cookie, stationName, workerUsername;
    private int workerId, stationId;
    private Handler mHandler;

    public static PdfViewerFrgmt newInstance(String path, int stationId, String cookie, String url, String stationName, String workerUsername, int workerId) {
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putInt("station_id", stationId);
        bundle.putString("cookie", cookie);
        bundle.putString("url", url);
        bundle.putString("station_name", stationName);
        bundle.putString("worker_username", workerUsername);
        bundle.putInt("worker_id", workerId);
        PdfViewerFrgmt pdfViewerFrgmt = new PdfViewerFrgmt();
        pdfViewerFrgmt.setArguments(bundle);
        return pdfViewerFrgmt;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null ) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.pdfviewer_frgmt, container, false);
            mView = mBinding.getRoot();
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            path = savedInstanceState.getString("path");
            cookie = savedInstanceState.getString("cookie");
            url = savedInstanceState.getString("url");
            stationId = savedInstanceState.getInt("station_id");
        } else {
            if (getArguments() != null) {
                path = getArguments().getString("path");
                cookie = getArguments().getString("cookie");
                url = getArguments().getString("url");
                stationId = getArguments().getInt("station_id");
                stationName = getArguments().getString("station_name");
                workerUsername = getArguments().getString("worker_username");
                workerId = getArguments().getInt("worker_id");
            }
        }

        if (stationName == null) stationName = SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.stationame));
        if (workerUsername == null) workerUsername = SharedPrefsUtl.getStringFlag(getActivity(), "worker_username");
        if (workerId == 0) workerId = SharedPrefsUtl.getIntFlag(getActivity(), "worker_id");

        pdfView = mBinding.pdfView;
        checkHttpConnection(path, new HttpConnectionListener<byte[]>(){
            public void on(final byte[] result){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdfView.fromBytes(result)
                                .scrollHandle(new DefaultScrollHandle(getActivity()))
                                .load();
                    }
                });
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                // This is where you do your work in the UI thread.
                // Your worker tells you in the message what to do.
            }
        };

        mBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.kioskModeLlt, SystemDashboardFrgmt.newInstance(stationId, cookie, url, stationName, workerUsername, workerId, "device"), getResources().getString(R.string.station_workers_frgmt))
                        .commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }

    @Override
    public void onPause() {
        super.onPause();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path", path);
        outState.putString("cookie", cookie);
        outState.putString("url", url);
        outState.putInt("station_id", stationId);
    }

    private void checkHttpConnection(final String url, final HttpConnectionListener<byte[]> onCompleteListener){
        new Thread() {
            @Override
            public void run() {
                byte[] byteArray = null;
                try {
                    uri = new URL(url);
                    conn =  (HttpURLConnection)uri.openConnection();
                    conn.connect();

                    InputStream inputStream = conn.getInputStream();
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] b = new byte[1024 * 8];
                        int bytesRead = 0;

                        while ((bytesRead = inputStream.read(b)) != -1) {
                            bos.write(b, 0, bytesRead);
                        }
                        byteArray = bos.toByteArray();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                onCompleteListener.on(byteArray);
            }
        }.start();
    }
}
