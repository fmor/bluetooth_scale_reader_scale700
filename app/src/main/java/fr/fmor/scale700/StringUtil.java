package fr.fmor.scale700;


public class StringUtil {


    public static String ToHexString( byte[] bytes )
    {
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < bytes.length; ++i )
        {
            sb.append( String.format("%02X", bytes[i] ) );
        }
        return sb.toString();
    }

    private static  byte HexValue( char c )
    {
        if( c < 65 )
            return (byte) (c - 48);
        return (byte)  (c -55);
    }



    public static  byte[] ToByteArray( String hexString )
    {



        hexString = hexString.trim();
        hexString = hexString.toUpperCase();
        int len = hexString.length();


        for( int i = 0; i < len; ++i )
        {
            if( Character.isLetterOrDigit( hexString.charAt(i)) == false  )
            {
                Logger.log_error( "Malformed hex string at char " + i  );
                return null;
            }
        }


        if( len % 2 != 0 )
        {
            ++len;
            hexString = "0" + hexString;
        }

        byte[] me = new  byte[ len / 2 ];
        int b = 0;
        for( int i = 0; i < len; i += 2 )
        {
            char l = hexString.charAt( i );
            char r = hexString.charAt( i + 1 );

            byte bl = HexValue( l );
            byte br = HexValue( r );

            me[b] = ( byte )  ( ( bl << 4 ) + br );
            ++b;
        }
        return me;
    }






    public  static  String ToString( byte[] bytes )
    {
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < bytes.length; ++i )
        {
            if( bytes[i] == ((byte) 0x00) )
                break;
            sb.append( Character.toChars( bytes[i] ) );
        }
        return sb.toString();
    }



}

