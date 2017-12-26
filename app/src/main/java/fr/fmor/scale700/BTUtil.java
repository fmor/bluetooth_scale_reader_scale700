package fr.fmor.scale700;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;


public class BTUtil {





    public static void EnableIndication(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic )
    {
        Logger.log_debug( "EnableIndication for char : " + characteristic.getUuid().toString() );

        gatt.setCharacteristicNotification( characteristic, true );

        BluetoothGattDescriptor descriptor = characteristic.getDescriptors().get(0);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }


    public static boolean Write( BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] value )
    {
        Logger.log_debug( "Write : " + StringUtil.ToHexString( value) );

        boolean b;
        characteristic.setWriteType( BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE );
        characteristic.setValue( value );
        b = gatt.writeCharacteristic( characteristic );
        if( b == false )
        {
            Logger.log_error( "Failed to write characteristic");
        }

        return  b;
    }

    public static boolean WriteReq( BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] value )
    {
        Logger.log_debug( "WriteReq : " + StringUtil.ToHexString( value) );

        boolean b;
        characteristic.setWriteType( BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT );
        characteristic.setValue( value );
        b = gatt.writeCharacteristic( characteristic );
        if( b == false )
        {
            Logger.log_error( "Failed to write characteristic");
        }
        return  b;
    }



    public static boolean IsBlueToothEnable( Context context )
    {
        // Init BT
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        if( adapter == null )
            return false;
        return adapter.isEnabled();
    }


    public static void RequestBluetoothActivation( Context context )
    {
        Intent intent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE) ;
        context.startActivity( intent );
    }




}
