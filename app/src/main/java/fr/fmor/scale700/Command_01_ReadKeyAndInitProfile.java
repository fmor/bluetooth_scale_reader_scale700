package fr.fmor.scale700;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.SystemClock;

import java.util.UUID;


class Command_01_ReadKeyAndInitProfile extends Command_00_Base
{
    private int m_ProfilID;

    public Command_01_ReadKeyAndInitProfile(Context context, int timeoutseconds, int profilID )
    {
        super( context, timeoutseconds );
        m_ProfilID = profilID;
    }


    public String getKeyString()
    {
        if( m_Scale700.hasKey() )
            return m_Scale700.KeyAsHexString();
        return null;
    }




    @Override
    protected void runCommad()
    {
        byte[] bytes;

        BluetoothGattService s0x0019 = m_Gatt.getService( UUID.fromString( Scale700.UUID_SERVICE_0x0019) );
        BluetoothGattService s0x0009 = m_Gatt.getService( UUID.fromString( Scale700.UUID_SERVICE_0x0009) );

/*
        BluetoothGattCharacteristic c0x000B = s0x0009.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x000B) );
        BluetoothGattCharacteristic c0x000E = s0x0009.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x000E) );
        BluetoothGattCharacteristic c0x0011 = s0x0009.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x0011) );
        BluetoothGattCharacteristic c0x0014 = s0x0009.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x0014) );
        BluetoothGattCharacteristic c0x0017 = s0x0009.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x0017) );
*/
        BluetoothGattCharacteristic c0x001c = s0x0019.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x001C) );
        BluetoothGattCharacteristic c0x001f = s0x0019.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x001F) );
        BluetoothGattCharacteristic c0x0023 = s0x0019.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x0023) );
        BluetoothGattCharacteristic c0x0025 = s0x0019.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x0025) );
        BluetoothGattCharacteristic c0x0026 = s0x0019.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x0026) );

        Logger.log_debug( "EnableIndication c0x001c");
        if( isConnected() == false )
            return;
        BTUtil.EnableIndication( m_Gatt, c0x001c );
        Lock.LOCK.lock();

        Logger.log_debug( "EnableIndication c0x001f");
        if( isConnected() == false )
            return;
        BTUtil.EnableIndication( m_Gatt, c0x001f );
        Lock.LOCK.lock();

        Logger.log_debug( "EnableIndication c0x0026");
        if( isConnected() == false )
            return;
        BTUtil.EnableIndication( m_Gatt, c0x0026 );
        Lock.LOCK.lock( 2 ); // Desciptor confirm + Key

        Logger.log_debug( "************************************* Req_Unknow_00 ");
        if( isConnected() == false )
            return;
        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_Unknow_00() );
        Lock.LOCK.lock();   // INDICATION_TYPE_Req_Unknow_00


        Logger.log_debug( "************************************* Challenge ");
        if( isConnected() == false )
            return;
        bytes = c0x0025.getValue();
        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_Challenge( bytes[1], bytes[2], bytes[3], bytes[4] ) );
        Lock.LOCK.lock( Scale700.PROFILS_COUNT );


        Logger.log_debug( "************************************* Req_SetProfilLabel ");
        if( isConnected() == false )
            return;
//        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_SetProfilLabel( m_ProfilID ) );
        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_ClearProfilLabel( m_ProfilID ) );
        SystemClock.sleep( 200L );

        Logger.log_debug( "************************************* Req_InitProfilBioData ");
        if( isConnected() == false )
            return;
//        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_InitProfilBioData( m_ProfilID ) );
        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_InitProfilBioDataZero( m_ProfilID ) );
        SystemClock.sleep( 200L );

        Logger.log_debug( "************************************* Req_Timestamp ");
        if( isConnected() == false )
            return;
        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_Timestamp() );
//        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_Timestamp_Zero() );
        SystemClock.sleep( 200L );


        Logger.log_debug( "************************************* Req_Disconnect ");
        if( isConnected() == false )
            return;
        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_Disconnect() );
        SystemClock.sleep( 200L );

    }

    @Override
    public void onCharacteristicChanged( BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
    {
        Logger.log_debug( "-----------------------------------------");
        Logger.log_debug( "onCharacteristicChanged : UUID = " + characteristic.getUuid().toString() + " ,  VALUE = " +  StringUtil.ToHexString( characteristic.getValue() ) );


        // 0xA0
        if( characteristic.getValue()[0] == Scale700.INDICATION_TYPE_KEY )
        {
            Logger.log_debug( "INDICATION_TYPE_KEY");
            m_Scale700.m_Key[0] = characteristic.getValue()[1];
            m_Scale700.m_Key[1] = characteristic.getValue()[2];
            m_Scale700.m_Key[2] = characteristic.getValue()[3];
            m_Scale700.m_Key[3] = characteristic.getValue()[4];
        }

        // 0xA1
        else if( characteristic.getValue()[0] == Scale700.INDICATION_TYPE_Req_Unknow_00 )
        {
            Logger.log_debug( "INDICATION_TYPE_Req_Unknow_00");
        }
        // 0x83
        else if( characteristic.getValue()[0] == Scale700.INDICATION_TYPE_PROFIL_USERNAME )
        {
            final int profilID = characteristic.getValue()[1];
            /*
            byte[] name = new byte[18];
            name[0]  = characteristic.getValue()[2];
            name[1]  = characteristic.getValue()[3];
            name[2]  = characteristic.getValue()[4];
            name[3]  = characteristic.getValue()[5];
            name[4]  = characteristic.getValue()[6];
            name[5]  = characteristic.getValue()[7];
            name[6]  = characteristic.getValue()[8];
            name[7]  = characteristic.getValue()[9];
            name[8]  = characteristic.getValue()[10];
            name[9]  = characteristic.getValue()[11];
            name[10] = characteristic.getValue()[12];
            name[11] = characteristic.getValue()[13];
            name[12] = characteristic.getValue()[14];
            name[13] = characteristic.getValue()[15];
            name[14] = characteristic.getValue()[16];
            name[15] = characteristic.getValue()[17];
            name[16] = characteristic.getValue()[18];
            name[17] = characteristic.getValue()[19];
            String usernam = StringUtil.ToString(  characteristic.getValue() );
            */
            Logger.log_debug( "INDICATION_TYPE_PROFIL_USERNAME : Profil [" + profilID + "] = "   );

        }
        else
        {
            Logger.log_debug( "Indication code unknow : " + characteristic.getValue()[0] );
        }

        SystemClock.sleep( 200L );
        Lock.LOCK.unlock();
    }

}
