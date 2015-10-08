package com.example.hideki.gyro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener{

    LocationManager l_manager;
    SensorManager s_manager;
    Sensor mAccelerometerSensor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // GPS part

        // get LocationManager Object
        l_manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Criteriaオブジェクトを生成
        Criteria criteria = new Criteria();
        // Accuracyを指定(低精度)
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        // PowerRequirementを指定(低消費電力)
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // ロケーションプロバイダの取得
        String provider = l_manager.getBestProvider(criteria, true);
        // 取得したロケーションプロバイダを表示
        TextView tv_provider = (TextView) findViewById(R.id.Provider);
        tv_provider.setText("Provider: " + provider);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        l_manager.requestLocationUpdates(provider, 0, 0, this);


        // AccelerometerSensor part
        s_manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = s_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    // when GPS changes
    @Override
    public void onLocationChanged(Location location) {
        Toast toast = Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT);
        toast.show();

        // 緯度の表示
        TextView tv_lat = (TextView) findViewById(R.id.Latitude);
        tv_lat.setText("Latitude: " + location.getLatitude());

        // 経度の表示
        TextView tv_lng = (TextView) findViewById(R.id.Longitude);
        tv_lng.setText("Longitude: " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 加速度センサの場合、以下の処理を実行
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            StringBuilder builder = new StringBuilder();

            // 数値の単位はm/s^2
            // X軸, Y軸, Z軸
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

//            builder.append("X: " + (x) + "\n");
//            builder.append("Y: " + (y) + "\n");
//            builder.append("Z: " + (z) + "\n");

            TextView txt_x = (TextView) findViewById(R.id.x);
            TextView txt_y = (TextView) findViewById(R.id.y);
            TextView txt_z = (TextView) findViewById(R.id.z);
            txt_x.setText("X: " + (x) + "\n");
            txt_y.setText("Y: " + (y) + "\n");
            txt_z.setText("Z: " + (z) + "\n");

            // Logに出力
            // Log.d(TAG, builder.toString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 200msに一度SensorEventを観測するリスナを登録
        s_manager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 非アクティブ時にSensorEventをとらないようにリスナの登録解除
        s_manager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}








