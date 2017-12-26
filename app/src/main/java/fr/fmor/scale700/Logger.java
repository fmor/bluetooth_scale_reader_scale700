package fr.fmor.scale700;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.List;

class Logger
{

    private final static String TAG = "fmor";


    public static void log_debug( String message )
    {
        Log.d( TAG, message );
    }
    public static void log_error( String message )
    {
        Log.e( TAG, message );
    }


    public static  void LogCharacteristics( BluetoothGattService service )
    {
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        for( BluetoothGattCharacteristic characteristic : characteristics )
        {
            Logger.log_debug( "Char : " + characteristic.getUuid().toString() );
        }
    }

    public static void LogDescriptors( BluetoothGattCharacteristic characteristic  )
    {
        Logger.log_debug( "Char : " + characteristic.getUuid().toString() );
        for( BluetoothGattDescriptor descriptor : characteristic.getDescriptors() )
        {
            Logger.log_debug( "   Descriptor  : " + descriptor.getUuid().toString()  );
        }
    }




}
