package fr.fmor.scale700;

class Scale700
{
    public static  final String BTNAME_PARTIAL = "Scale 7";


    public static final int PROFILS_COUNT = 8;

    public static  final String UUID_SERVICE_0x0001 = "00001800-0000-1000-8000-00805f9b34fb";
    public static  final String UUID_SERVICE_0x0008 = "00001801-0000-1000-8000-00805f9b34fb";
    public static  final String UUID_SERVICE_0x0009 = "0000180a-0000-1000-8000-00805f9b34fb";
    public static  final String UUID_SERVICE_0x0019 = "00007892-0000-1000-8000-00805f9b34fb";




    public static final String UUID_CHAR_0x000B = "00002a25-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR_0x000E = "00002a27-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR_0x0011 = "00002a26-0000-1000-8000-00805f9b34fb";
    public static  final String UUID_CHAR_0x0014 = "00002a29-0000-1000-8000-00805f9b34fb";
    public static  final String UUID_CHAR_0x0017 = "00002a28-0000-1000-8000-00805f9b34fb";



    public  static final String UUID_CHAR_0x001C = "00008a24-0000-1000-8000-00805f9b34fb";
    public  static final String UUID_CHAR_0x001F = "00008a22-0000-1000-8000-00805f9b34fb";
    public  static final String UUID_CHAR_0x0023 = "00008a81-0000-1000-8000-00805f9b34fb";
    public  static final String UUID_CHAR_0x0025 = "00008a82-0000-1000-8000-00805f9b34fb";
    public  static final String UUID_CHAR_0x0026 = "00008a82-0000-1000-8000-00805f9b34fb";


    // Liste des profils


    public static final byte INDICATION_TYPE_KEY     = (byte) 0xA0;
    public static final byte INDICATION_TYPE_Req_Unknow_00 = (byte) 0xA1;
    public static final byte INDICATION_TYPE_PROFIL_USERNAME  = (byte) 0x83;



    public byte[]   m_Key     = new byte[4];

    public byte[]   m_SerialNumber = new byte[12];
    public String   m_HardwareRevision;
    public String   m_FirmwareRevision;
    public String   m_ManufacturerName;
    public String   m_SoftwareRevision;

    Scale700()
    {

        m_Key[0] = (byte) 0x00;
        m_Key[1] = (byte) 0x00;
        m_Key[2] = (byte) 0x00;
        m_Key[3] = (byte) 0x00;
    }

    Scale700( byte key_00, byte key_01, byte key_02, byte key_03 )
    {
        m_Key[0] = key_00;
        m_Key[1] = key_01;
        m_Key[2] = key_02;
        m_Key[3] = key_03;
    }


    public  String KeyAsHexString()
    {
        return StringUtil.ToHexString( m_Key );
    }



    public boolean hasKey()
    {
        if( m_Key[0] != (byte) 0x00 )
            return true;
        if( m_Key[1] != (byte) 0x00 )
            return true;
        if( m_Key[2] != (byte) 0x00 )
            return true;
        if( m_Key[3] != (byte) 0x00 )
            return true;
        return false;
    }



    public byte[] Req_Challenge( byte b00, byte b01, byte b02, byte b03 )
    {
        byte[] me = new byte[5];
        me[0] = (byte) 0x20;
        me[1] = (byte) ( b00 ^ m_Key[0] );
        me[2] = (byte) ( b01 ^ m_Key[1] );
        me[3] = (byte) ( b02 ^ m_Key[2] );
        me[4] = (byte) ( b03 ^ m_Key[3] );
        return me;
    }


    public byte[] Req_Unknow_00()
    {
        byte[] me = new byte[9];
        me[0] = (byte) 0x21;


        // Set id
        // 0Scale 70033000000
        // Quand on est en lecture c'est l'id qui va être diffusé.

        me[1] = (byte) 0x33;
        me[2] = (byte) 0x00;
        me[3] = (byte) 0x00;
        me[4] = (byte) 0x00;
        me[5] = (byte) 0x00;
        me[6] = (byte) 0x00;
        me[7] = (byte) 0x00;
        me[8] = (byte) 0x00;

/*
        me[1] = (byte) 0x33;
        me[2] = (byte) 0x35;
        me[3] = (byte) 0x34;
        me[4] = (byte) 0x62;
        me[5] = (byte) 0x34;
        me[6] = (byte) 0x33;
        me[7] = (byte) 0x64;
        me[8] = (byte) 0x38;

*/
        return me;
    }




    public byte[] Req_SetProfilLabel( int profildID )
    {
        byte[] me = new byte[20];
        me[0]  = (byte) 0x03;
        me[1]  = (byte) (profildID);
        me[2]  = (byte) 0x0;    // prénom
        me[3]  = (byte) 0x0;    // prénom
        me[4]  = (byte) 0x0;    // prénom
        me[5]  = (byte) 0x0;    // prénom
        me[6]  = (byte) 0x0;    // prénom
        me[7]  = (byte) 0x0;    // prénom
        me[8]  = (byte) 0x0;    // prénom
        me[9]  = (byte) 0x0;    // prénom
        me[10] = (byte) 0x0;    // prénom
        me[11] = (byte) 0x0;    // prénom
        me[12] = (byte) 0x0;    // prénom
        me[13] = (byte) 0x0;    // prénom
        me[14] = (byte) 0x0;    // prénom
        me[15] = (byte) 0x0;    // prénom
        me[16] = (byte) 0x0;    // prénom
        me[17] = (byte) 0x0;    // prénom
        me[18] = (byte) 0x0;    // prénom
        me[19] = (byte) 0x00;    // prénom
        return  me;
    }

    public byte[] Req_ClearProfilLabel( int profildID )
    {
        byte[] me = new byte[20];
        me[0]  = (byte) 0x03;
        me[1]  = (byte) (profildID);
        me[2]  = (byte) 0x20;    // prénom
        me[3]  = (byte) 0x00;    // prénom
        me[4]  = (byte) 0x00;    // prénom
        me[5]  = (byte) 0x00;    // prénom
        me[6]  = (byte) 0x00;    // prénom
        me[7]  = (byte) 0x00;    // prénom
        me[8]  = (byte) 0x00;    // prénom
        me[9]  = (byte) 0x00;    // prénom
        me[10] = (byte) 0x00;    // prénom
        me[11] = (byte) 0x00;    // prénom
        me[12] = (byte) 0x00;    // prénom
        me[13] = (byte) 0x00;    // prénom
        me[14] = (byte) 0x00;    // prénom
        me[15] = (byte) 0x00;    // prénom
        me[16] = (byte) 0x00;    // prénom
        me[17] = (byte) 0x00;    // prénom
        me[18] = (byte) 0x00;    // prénom
        me[19] = (byte) 0x00;    // prénom
        return  me;
    }


    public byte[] Req_InitProfilBioData( int profildID )
    {

        byte[] me = new byte[12];
        me[0] = (byte) 0x51;
        me[1] = (byte) 0x07;
        me[2] = (byte) profildID;    // pofil
        me[3] = (byte) 0x03;    // sexe ( F = 2 ;
        me[4] = (byte) 0x1d;    // age
        me[5] = (byte) 0x44;    // heigjt
        me[6] = (byte) 0xd7;
        me[7] = (byte) 0x00;
        me[8] = (byte) 0x00;
        me[9] = (byte) 0x00;
        me[10]= (byte) 0x00;
        me[11]= (byte) 0x00;
        return me;

    }

    public byte[] Req_InitProfilBioDataZero( int profildID )
    {

        byte[] me = new byte[12];
        me[0] = (byte) 0x51;
        me[1] = (byte) 0x07;
        me[2] = (byte) profildID;    // pofil
        me[3] = (byte) 0x02;    // sexe  ( F = 2 ;
        me[4] = (byte) 0x1d;    // age
        me[5] = (byte) 0xe8;    // heigjt
        me[6] = (byte) 0xd3;    // height  0xD + 0x0FFF  en millimetre   pour 1 m  :> D000 + 03E8
        me[7] = (byte) 0x00;
        me[8] = (byte) 0x00;
        me[9] = (byte) 0x00;
        me[10]= (byte) 0x00;
        me[11]= (byte) 0x00;
        return me;
    }


    public byte[] Req_Timestamp()
    {
        int i  = ( int ) System.currentTimeMillis() / 1000;
        i -= 1262217600;
        // date
        byte[] me = new byte[5];
        me[0] = (byte) 0x02;
        me[1] = (byte) ( (i >> 24) & 0xFF );
        me[2] = (byte) ( (i >> 16) & 0xFF );
        me[3] = (byte) ( (i >> 8 ) & 0xFF );
        me[4] = (byte) ( i & 0xFF  );
        return me;
    }
    public byte[] Req_Timestamp_Zero()
    {
        int i = 0;

        // date
        byte[] me = new byte[5];
        me[0] = (byte) 0x02;
        me[1] = (byte) ( (i >> 24) & 0xFF );
        me[2] = (byte) ( (i >> 16) & 0xFF );
        me[3] = (byte) ( (i >> 8 ) & 0xFF );
        me[4] = (byte) ( i & 0xFF  );
        return me;
    }



    public byte[] Req_Disconnect()
    {
        // disconnect
        byte[] me = new byte[1];
        me[0] = (byte) 0x22;
        return me;
    }




}
