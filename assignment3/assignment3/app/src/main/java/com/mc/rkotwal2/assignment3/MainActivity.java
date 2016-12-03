package com.mc.rkotwal2.assignment3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.view.View;

import static android.R.id.progress;

public class MainActivity extends Activity implements SensorEventListener {
    public static final String LOG_TAG = "AndroidLibSvm";

    String appFolderPath;
    String systemPath;


    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("jnilibsvm");
    }


    DatabaseHelper db;
    public ArrayList sensordata;
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    TextView tv;
    RadioGroup rg ;
    RadioButton rb1;
    RadioButton rb2,selectedRadioButton ;
    String ActivityName;
    boolean flag=true;
    long initialTimestamp;
    boolean model=false;
    boolean check=false;
    int kernel=0;
    String s1,s2,s3;
    //boolean dataset_selected=false;
    String kernel_type="";
    String svmTrainOptions="";
    String ker;

    int count=0;
    long lastTimeStamp=System.currentTimeMillis();
    private final float NOISE = (float) 2.0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensordata = new ArrayList();
        setContentView(R.layout.activity_main);
        systemPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        System.out.println("-------------------systemPath:------------------------------------"+systemPath);
        appFolderPath = systemPath+"libsvm/";

        // 1. create necessary folder to save model files
        CreateAppFolderIfNeed();
        copyAssetsDataIfNeed();

        rg = (RadioGroup) findViewById(R.id.action_radio);
        rb1 = (RadioButton) findViewById(R.id.k1);
        rb2 = (RadioButton) findViewById(R.id.k2);
        // 2. assign model/output paths
        final String dataTrainPath = appFolderPath+"heart_scale1 ";
        final String dataPredictPath = appFolderPath+"heart_scale1 ";
        final String modelPath = appFolderPath+"model ";
        final String outputPath = appFolderPath+"predict ";

        final String accuracy;
        final TextView tv = (TextView) findViewById(R.id.sample_text);

        // 3. make SVM train


       // svmTrainOptions = "-t 2 ";


        Button train=(Button)findViewById(R.id.training);
        train.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                int selectedId = rg.getCheckedRadioButtonId();
                selectedRadioButton = (RadioButton) findViewById(selectedId);
                ker = selectedRadioButton.getText().toString();
                if(ker.equals("Radial basis"))
                {
                    kernel_type="2";
                    svmTrainOptions = "-t 2 ";}
                else if(ker.equals("Sigmoid"))
                { kernel_type="3";
                    svmTrainOptions = "-t 3 ";}
                else
                  kernel_type="";

                if (kernel_type != "") {
                    final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Training", "Wait while training...");
                    new Thread() {
                        public void run() {
                            try {
                                // just doing some long operation
                                sleep(2000);
                            } catch (Exception e) {
                            }
                            progressDialog.dismiss();
                            //model=true;
                        }
                    }.start();
                    //progressDialog.dismiss();
                    jniSvmTrain(svmTrainOptions + dataTrainPath + modelPath);
                    Toast.makeText(MainActivity.this, "Model File created!!", Toast.LENGTH_LONG).show();
                    check = true;

                }
                else
                    Toast.makeText(MainActivity.this, "Choose Kernel Type first!!", Toast.LENGTH_LONG).show();
            }


        });
        //if(model==true) {

            //model = false;
        //}
        // 4. make SVM predict

        Button predict=(Button)findViewById(R.id.predict);
        predict.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (check == false)
                    Toast.makeText(MainActivity.this, "Create Model file first!!", Toast.LENGTH_LONG).show();
                else {
                    final ProgressDialog pDialog = ProgressDialog.show(MainActivity.this, "Predicting accuracy", "Wait while predicting...");
                    new Thread() {
                        public void run() {
                            try {
                                // just doing some long operation
                                //System.out.println("Inside thread-----------------------------");
                                sleep(2000);
                            } catch (Exception e) {
                            }
                            pDialog.dismiss();
                        }
                    }.start();
                    //progressDialog.dismiss();
                    tv.setText(jniSvmPredict(dataPredictPath + modelPath + outputPath));
                    Toast.makeText(MainActivity.this, "Accuracy :"+jniSvmPredict(dataPredictPath + modelPath + outputPath), Toast.LENGTH_LONG).show();
                    check=false;
                }
            }
        });


        db = new DatabaseHelper(this);
        // db.delete();
        /*
        if(db.isTableExists("testing_data"))
        {
            db.createnewtable();
        }
        else
        {
            db.fetch();
        }*/
        //db.fetch(db1);
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);


       //tv.setText(Double.toString(accuracy) + "%");
       // tv.setText(accuracy);
       // tv.setText(stringFromJNI());



    }
    /*public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.d1:
                if (checked)
                {s1="heart_scale1";
                    s2="heart_scale_train1";
                    s3="heart_scale_predict1";
                    dataset_selected=true;
                    break;}
            case R.id.d2:
                if (checked)
                {s1="totaldata";
                    s2="trainingdata";
                    s3="testingdata";
                    dataset_selected=true;
                    break;}

            case R.id.d3:
                if (checked)
                {s1="my_scaled_total";
                    s2="my_scaled_training";
                    s3="my_scaled_testing";
                    dataset_selected=true;
                    break;}
        }
    }*/
  /*  public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.k1:
                if (checked)
                {
                    kernel_type="2";
                    break;}
            case R.id.k2:
                if (checked)
                {
                    Toast.makeText(MainActivity.this, "second", Toast.LENGTH_LONG).show();
                    kernel_type="3";
                    break;
                }
        }
    }*/

    /*
    * Some utility functions
    * */

    private void CreateAppFolderIfNeed(){
        // 1. create app folder if necessary
        File folder = new File(appFolderPath);

        if (!folder.exists()) {
            folder.mkdir();
            Log.d(LOG_TAG,"Appfolder is not existed, create one");
        } else {
            Log.w(LOG_TAG,"WARN: Appfolder has not been deleted");
        }
    }

    private void copyAssetsDataIfNeed(){
        String assetsToCopy[] = {"heart_scale_predict1","heart_scale_train1","heart_scale1"};

        //String targetPath[] = {C.systemPath+C.INPUT_FOLDER+C.INPUT_PREFIX+AudioConfigManager.inputConfigTrain+".wav", C.systemPath+C.INPUT_FOLDER+C.INPUT_PREFIX+AudioConfigManager.inputConfigPredict+".wav",C.systemPath+C.INPUT_FOLDER+"SomeoneLikeYouShort.mp3"};

        for(int i=0; i<assetsToCopy.length; i++){
            String from = assetsToCopy[i];
            String to = appFolderPath+from;

            // 1. check if file exist
            File file = new File(to);
            if(file.exists()){
                Log.d(LOG_TAG, "copyAssetsDataIfNeed: file exist, no need to copy:"+from);
            } else {
                // do copy
                boolean copyResult = copyAsset(getAssets(), from, to);
                Log.d(LOG_TAG, "copyAssetsDataIfNeed: copy result = "+copyResult+" of file = "+from);
            }
        }
    }

    private boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "[ERROR]: copyAsset: unable to copy file = "+fromAssetPath);
            return false;
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public native  String stringFromJNI();
    //public native void jniSvmTrain();
    // connect the native functions
    public native void jniSvmTrain(String cmd);
    private native String jniSvmPredict(String cmd);

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (flag) {
            initialTimestamp = System.currentTimeMillis();
            flag = false;
        }
        TextView tvX = (TextView) findViewById(R.id.x_axis);
        TextView tvY = (TextView) findViewById(R.id.y_axis);
        TextView tvZ = (TextView) findViewById(R.id.z_axis);
        ImageView iv = (ImageView) findViewById(R.id.image);
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp - initialTimestamp < 100000)
            ActivityName = "Eating";
        else if ((currentTimestamp - initialTimestamp) > 100000 && (currentTimestamp - initialTimestamp) < 200000)
            ActivityName = "Walking";
        else
            ActivityName = "Running";

        if (currentTimestamp - lastTimeStamp > 25) {
            lastTimeStamp = currentTimestamp;
            // db.put(x,y,z,ActivityName);
        }

        //sensordata.add(db);


    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}