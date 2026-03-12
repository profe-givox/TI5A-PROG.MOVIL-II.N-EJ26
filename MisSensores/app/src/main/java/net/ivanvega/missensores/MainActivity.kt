package net.ivanvega.missensores

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.ricknout.composesensors.accelerometer.getAccelerometerSensor
import dev.ricknout.composesensors.accelerometer.isAccelerometerSensorAvailable
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState
import net.ivanvega.missensores.ui.theme.MisSensoresTheme

class MainActivity : ComponentActivity(), SensorEventListener {

    private  var mLight: Sensor? = null
    private var mSensor: Sensor? = null
    var _sensorManager: SensorManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MisSensoresTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    /*Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )*/
                }
            }
        }
        _sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        var lsS =  _sensorManager!!.getSensorList(Sensor.TYPE_ALL)

        lsS.forEach {
            Log.i("Sensor", "name: $it.name vendor: ${it.vendor}" +
                    " version: ${it.version} type: ${it.type} maxRange: ${it.maximumRange}" +
                    " resolution: ${it.resolution} ")
        }

        if (_sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            // Success! There's a magnetometer.
            Log.i("Sensor M", "Success! There's a magnetometer. " +
                    "")

        } else {
            // Failure! No magnetometer.
            Log.i("Sensor M", "Failure! There is NOT a magnetometer. " +
                    "")
        }


        if (_sensorManager!!.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            val gravSensors: List<Sensor> = _sensorManager!!.getSensorList(Sensor.TYPE_GRAVITY)
            // Use the version 3 gravity sensor.
            mSensor = gravSensors.firstOrNull { it.vendor.contains("Google LLC") && it.version == 3 }
        }

        if (mSensor == null) {
            // Use the accelerometer.
            mSensor = if (_sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                _sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            } else {
                // Sorry, there are no accelerometers on your device.
                // You can't play this game.
                Log.w("Sensor de juegos", "Sorry, there are no accelerometers on your device.\n" +
                        "You can't play this game.")
                null
            }
        }

        mSensor.let {
            Log.i("Sensor", "name: $it.name vendor: ${it!!.vendor}" +
                    " version: ${it!!.version} type: ${it!!.type} maxRange: ${it!!.maximumRange}" +
                    " resolution: ${it.resolution} ")
        }

        mLight = _sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)




    }

    override fun onResume() {
        super.onResume()
        _sensorManager!!.
        registerListener(this, mLight,
            SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onPause() {
        super.onPause()
        _sensorManager!!.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        val lux = event!!.values[0]
        // Do something with this sensor value.
        Log.d("Sensor", "Sensor: $lux ")
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val estadode = remember  { mutableStateOf("")}

    DisposableEffect (Unit) {
        val sensorManager = ctx.getSystemService(SENSOR_SERVICE) as SensorManager
        val sa = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val listener = object : SensorEventListener {


            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

            override fun onSensorChanged(event: SensorEvent?) {
                val str =  "Acelerometro:  x: ${event!!.values[0]} y: ${event.values[1]} z: ${event.values[2]}"
                estadode.value = str

            }

        }
        sensorManager.registerListener(listener,
            sa,
            SensorManager.SENSOR_DELAY_NORMAL
            )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    Text(
        text = "Hello $name!\n" + estadode.value,
        modifier = modifier
    )
}

@Composable
fun DisposeableEffect(x0: Unit, content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // Check if accelerometer sensor is available
    val available = isAccelerometerSensorAvailable()

    // Get accelerometer sensor
    val sensor = getAccelerometerSensor()

    // Remember accelerometer sensor value as State that updates as SensorEvents arrive
    val sensorValue by rememberAccelerometerSensorValueAsState()

    // Accelerometer sensor values. Also available: sensorValue.timestamp, sensorValue.accuracy
    val (x, y, z) = sensorValue.value

    Text(
        text = "Hello $name! \n" +
                "Available: $available \n" +
                "Sensor: $sensor \n" +
                "Sensor value: $sensorValue \n" +
                "x: $x, y: $y, z: $z",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MisSensoresTheme {
        Greeting("Android")
    }
}