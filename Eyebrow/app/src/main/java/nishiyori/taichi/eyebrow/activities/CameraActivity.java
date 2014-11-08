package nishiyori.taichi.eyebrow.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import nishiyori.taichi.eyebrow.R;

/**
 *
 * Created by Taichi on 2014/11/09.
 */
public class CameraActivity extends Activity {
    private static final String TAG = "CameraActivity";

    private Camera mCamera;

    private SurfaceHolder.Callback mSurfaceListener = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "#surfaceCreated(SurfaceHolder)");
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(TAG, "#surfaceDestroyed(SurfaceHolder)");
            mCamera.release();
            mCamera = null;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i(TAG, "#surfaceChanged(SurfaceHolder, int, int, int)");
            mCamera.stopPreview();
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            // TODO 一つ目を取得する
            Camera.Size size = previewSizes.get(0);
            parameters.setPreviewSize(size.width, size.height);
            mCamera.setParameters(parameters);
            // 画面の向き
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            String str;
            switch(config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                mCamera.setDisplayOrientation(90);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                mCamera.setDisplayOrientation(0);
                break;
            }
            mCamera.startPreview();
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "#onCreate(savedInstanceState)");
        setContentView(R.layout.activity_camera);

        SurfaceView mySurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        SurfaceHolder holder = mySurfaceView.getHolder();
        holder.addCallback(mSurfaceListener);
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
}