package fr.fmor.scale700;


import android.content.Context;

public class Scale700Api
{

    public static  final int MINIMUM_TIMEOUT_SECONDS = 1;



    // Toutes Bloquantes
    public synchronized  static  String ReadKeyAndInitProfile( Context context,  int timeoutSeconds, int profileID  )
    {
        Logger.log_debug( "ReadKeyAndInitProfile");

        if( timeoutSeconds < MINIMUM_TIMEOUT_SECONDS )
            timeoutSeconds = MINIMUM_TIMEOUT_SECONDS;

        if( profileID < 1 )
            profileID = 1;
        else if( profileID > 8 )
            profileID = 1;


        Command_01_ReadKeyAndInitProfile cmd = new Command_01_ReadKeyAndInitProfile( context, timeoutSeconds, profileID );
        if( cmd.execute() == false )
            return null;

        if( cmd.getKeyString() == null )
            return null;

        Logger.log_debug( "FOUND KEY : " + cmd.getKeyString() );
        return cmd.getKeyString();

    }



    // Toutes Bloquantes
    public synchronized static  int ReadWeight( Context context, int timeout, String key  )
    {
        Logger.log_debug( "ReadWeight");

        Command_02_ReadWeight cmd = new Command_02_ReadWeight( context, timeout, key );
        if( cmd.execute() == false )
            return  -1;

        if( cmd.getWeight() == -1 )
            return  -1;

        Logger.log_debug( "Poids lu : " + cmd.getWeight() );
        return cmd.getWeight();
    }


}
