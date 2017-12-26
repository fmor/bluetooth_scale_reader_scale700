package fr.fmor.scale700;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.SystemClock;

import java.util.UUID;



abstract  class Command_00_Base extends BluetoothGattCallback implements BluetoothAdapter.LeScanCallback
{

    protected  BluetoothGatt m_Gatt;
    protected Scale700 m_Scale700;
    protected  Context m_Context;
    protected Object m_Lock;

    private boolean m_ServiceDiscovered;
    protected int m_State;
    private  int m_TimeoutSecond;

    public Command_00_Base( Context context, int timeoutSecond )
    {
        m_Context = context;
        m_TimeoutSecond = timeoutSecond;

        m_Gatt = null;
        m_Scale700 = new Scale700();
        m_Lock = new Object();
    }

    public  boolean execute()
    {
        Logger.log_debug( "execute() start" );

        m_State = BluetoothProfile.STATE_DISCONNECTED;
        m_Gatt = null;
        m_ServiceDiscovered = false;

        // Init BT
        final BluetoothManager bluetoothManager = (BluetoothManager) m_Context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter adapter = bluetoothManager.getAdapter();
        AssertUtil.AssertNotNull( adapter );
        AssertUtil.AssetTrue( adapter.isEnabled() );

        adapter.startLeScan( this );

        boolean me = false;

        synchronized (  m_Lock )
        {
            try {
                Lock.LOCK.setEnable( true );
                m_Lock.wait( m_TimeoutSecond * 1000 );
                adapter.stopLeScan( this );

                if( m_ServiceDiscovered )
                {
                    Logger.log_debug( "Running command -------------------- ><<");
                    runCommad();
                    Logger.log_debug( "Disconnecting gatt");
                    m_Gatt.close();
                    m_Gatt.disconnect();
                    me = true;
                }
                else if( m_Gatt != null )
                {
                    m_Gatt.close();
                    m_Gatt.disconnect();
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Logger.log_debug( "execute() end" );
        return me;
    }




    protected abstract  void runCommad();

    public  boolean isConnected()
    {
        return m_State == BluetoothProfile.STATE_CONNECTED;
    }


    @Override
    public void onLeScan(BluetoothDevice device , int i, byte[] bytes )
    {
        Logger.log_debug( "Scanning Found device : " + device.getName() );

        // Pas top !
        if( device.getName().contains( Scale700.BTNAME_PARTIAL ) );
        {
            Logger.log_debug( "Connecting to scale : " + device.getName() );
            device.connectGatt( m_Context, false, this );
        }
    }



    @Override
    public void onConnectionStateChange( BluetoothGatt gatt, int status, int newState)
    {
        Logger.log_debug("onConnectionStateChange :  " + newState );

        if( m_State == newState )
            throw  new RuntimeException();

        if (newState == BluetoothProfile.STATE_CONNECTED)
        {
            Logger.log_debug("onConnectionStateChange :  STATE_CONNECTED");
            m_Gatt = gatt;
            gatt.discoverServices();
        }
        else if( newState == BluetoothProfile.STATE_DISCONNECTED)
        {
            // Une déconnexion peut arriver en pleine commande du thread bt,
            // On libère les lock et interdit tout nouveau lock
            Logger.log_debug("onConnectionStateChange :  STATE_DISCONNECTED");
            Lock.LOCK.setEnable( false );
        }

        m_State =  newState;
    }


    @Override
    public void onServicesDiscovered( BluetoothGatt gatt, int status)
    {
        Logger.log_debug( "onServicesDiscovered");
        if (status != BluetoothGatt.GATT_SUCCESS)
        {
            throw new RuntimeException();
        }

        m_ServiceDiscovered = true;
        synchronized ( m_Lock )
        {
            this.m_Lock.notify();
        }
    }





    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
    {
        Logger.log_debug( "------------------>onCharacteristicRead");
        if (status != BluetoothGatt.GATT_SUCCESS)
        {
            throw  new RuntimeException();
        }

        // Pour le fun

        // Serial number
        if( characteristic.getUuid().equals( UUID.fromString(Scale700.UUID_CHAR_0x000B)) )
        {
            m_Scale700.m_SerialNumber = characteristic.getValue();
        }

        // Hardware revision
        else if( characteristic.getUuid().equals( UUID.fromString(Scale700.UUID_CHAR_0x000E)) )
        {
            m_Scale700.m_HardwareRevision = StringUtil.ToString( characteristic.getValue() );
        }

        // Firmware Revision
        else if( characteristic.getUuid().equals( UUID.fromString(Scale700.UUID_CHAR_0x0011)))
        {
            m_Scale700.m_FirmwareRevision = StringUtil.ToString( characteristic.getValue() );

        }

        // Manufacturer Name
        else if( characteristic.getUuid().equals( UUID.fromString(Scale700.UUID_CHAR_0x0014)))
        {
            m_Scale700.m_ManufacturerName = StringUtil.ToString( characteristic.getValue() );
        }

        // Software Revision
        else if( characteristic.getUuid().equals( UUID.fromString(Scale700.UUID_CHAR_0x0017)))
        {
            m_Scale700.m_SoftwareRevision = StringUtil.ToString( characteristic.getValue() );
        }

        else
        {
            Logger.log_debug( "Charact read not handled : " + characteristic.getUuid().toString()  );
        }


        SystemClock.sleep( 200L );
        Lock.LOCK.unlock();

    }






    @Override
    public void onDescriptorWrite (BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status )
    {
        Logger.log_debug( "onDescriptorWrite : "  + descriptor.getCharacteristic().getUuid().toString() );
        SystemClock.sleep( 200L );
        Lock.LOCK.unlock();



    }

}
