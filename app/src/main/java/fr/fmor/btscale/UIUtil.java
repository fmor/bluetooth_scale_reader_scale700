package fr.fmor.btscale;

import android.content.Context;
import android.widget.Toast;

public class UIUtil
{

    public static void ShowMessage(Context context, String message )
    {
        Toast.makeText( context, message, Toast.LENGTH_SHORT ).show();
    }



    public static void ShowError(Context context, String message )
    {
        Toast.makeText( context, "!!! " + message + " !!!", Toast.LENGTH_SHORT ).show();
    }

}
