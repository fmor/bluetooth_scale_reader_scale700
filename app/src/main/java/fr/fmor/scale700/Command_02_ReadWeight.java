package fr.fmor.scale700;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.SystemClock;

import java.util.UUID;

class Command_02_ReadWeight extends Command_00_Base
{

    private  int m_Weight;

    public Command_02_ReadWeight(Context context, int timeoutseconds ,  String key )
    {
        super( context, timeoutseconds );
        m_Scale700.m_Key = StringUtil.ToByteArray( key );
        m_Weight = -1;
    }


    public int getWeight()
    {
        return m_Weight;
    }


    @Override
    protected void runCommad()
    {
        byte[] bytes;

        BluetoothGattService s0x0019 = m_Gatt.getService( UUID.fromString( Scale700.UUID_SERVICE_0x0019) );

        BluetoothGattCharacteristic c0x001c = s0x0019.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x001C) );
        BluetoothGattCharacteristic c0x001f = s0x0019.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x001F) );
        BluetoothGattCharacteristic c0x0023 = s0x0019.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x0023) );
        BluetoothGattCharacteristic c0x0025 = s0x0019.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x0025) );
        BluetoothGattCharacteristic c0x0026 = s0x0019.getCharacteristic( UUID.fromString( Scale700.UUID_CHAR_0x0026) );
        AssertUtil.AssertNotNull( c0x001c );
        AssertUtil.AssertNotNull( c0x001f );
        AssertUtil.AssertNotNull( c0x0023 );
        AssertUtil.AssertNotNull( c0x0025 );
        AssertUtil.AssertNotNull( c0x0026 );

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
        Lock.LOCK.lock( 2 );


        Logger.log_debug( "************************************* Challenge ");
        if( isConnected() == false )
            return;
        bytes = c0x0025.getValue();
        if( bytes == null )
            return;
        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_Challenge( bytes[1], bytes[2], bytes[3], bytes[4] ) );
        SystemClock.sleep( 200L );


        // Liste des mise à jour depuis cette date
        Logger.log_debug( "Write timestamp");
        if( isConnected() == false )
            return;
        BTUtil.WriteReq( m_Gatt, c0x0023,  m_Scale700.Req_Timestamp() );

        // Ici on récupere l'utilisateur [2]
        Lock.LOCK.lock( 1 );

        // C0:17:01:01:1E:40:D6:00:00:00:00:00:00:00:00:00:00:00:00:00
        // c0:17:04:01:1b:44:d7:00:00:00:00:00:00:00:00:00:00:00:00:00

        int profilID= c0x0025.getValue()[2];
        Logger.log_debug( "********** MAJ Profil " + profilID );





        Logger.log_debug( "Write disconnect");
        if( isConnected() == false )
            return;
        BTUtil.WriteReq( m_Gatt, c0x0023, m_Scale700.Req_Disconnect() );
        Lock.LOCK.lock( 1 );


        // Attendre que les autres notifications soient transmises
        SystemClock.sleep( 2000L );

    }



    @Override
    public void onCharacteristicChanged( BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
    {
        Logger.log_debug( "-----------------------------------------");
        Logger.log_debug( "onCharacteristicChanged : UUID = " + characteristic.getUuid().toString() + " ,  VALUE = " +  StringUtil.ToHexString( characteristic.getValue() ) );


        if( characteristic.getUuid().equals( UUID.fromString(Scale700.UUID_CHAR_0x001C) ) )
        {
/*

            1F342600FE9BB5DCCD000000004D1500FF011900
            1FAC2600FE6AB5DCCD000000008E1300FF011900
            1F3E2600FE21B5DCCD000000001F1300FF011900
*/

            // 1F080200FE2E00000000000000000000FF010900
            // 0 -> Log type
            // 1  -> Poids 0x00FF
            // 2  -> Poids 0xFF00
            // 3 ->

            // Poids : 1F
            Logger.log_debug( "-------------> POID <------------------");
            Logger.log_debug( "-------------> POID <------------------");
            Logger.log_debug( "-------------> POID <------------------");
            Logger.log_debug( "-------------> POID <------------------");

            byte[] bytes = characteristic.getValue();


            // Poids
            int weight = (bytes[2] << 8) & 0x0000FF00;
            weight = weight + ( bytes[1] & 0x000000FF );


            // Plusieurs autre entrée suivent mais seule la première, la plus récente nous intéresse.
            if( m_Weight == -1 )
            {
                m_Weight = weight;
                Lock.LOCK.unlock();
            }


            Logger.log_debug( "Poids le plus récent : " + m_Weight  ) ;
            Logger.log_debug( "Poids lu : " + weight  ) ;



        }
        else
        {
            Lock.LOCK.unlock();
        }
    }





}
