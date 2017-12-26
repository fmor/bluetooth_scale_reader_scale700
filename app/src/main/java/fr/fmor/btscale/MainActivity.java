package fr.fmor.btscale;

import android.bluetooth.BluetoothAdapter;
import android.content.ServiceConnection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import fr.fmor.scale700.BTUtil;
import fr.fmor.scale700.Scale700Api;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_ENABLE_BT = 01;

    private BluetoothAdapter m_BluetoothAdapter = null;
    private ServiceConnection m_ServiceConnection = null;



    int  m_TimeoutSeconds;
    String m_Key;

    Button m_Btn_ReadKeyAndInitProfile[];
    Button m_Btn_Read;
    Button m_Btn_Read_38BB5CF5;
    Button m_Btn_shown_help_init;
    Button m_Btn_shown_help_read;

    TextView m_TextView_Weight;
    WebView m_WebViewHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_Key = null;
        m_TimeoutSeconds = 10;


        setTitle( "Scale 700 ( timeout : " + m_TimeoutSeconds +" )");


        m_TextView_Weight  = (TextView) findViewById( R.id.textView_Weight );
        m_TextView_Weight.setText( "0.0" );

        m_WebViewHelp = (WebView) findViewById( R.id.WebView_help );
        m_WebViewHelp.loadDataWithBaseURL( null, getString(R.string.help_init), "text/html", "utf-8", null );


        // Buttons
        m_Btn_shown_help_init= (Button) findViewById(R.id.btn_shown_help_init);
        m_Btn_shown_help_read = (Button) findViewById(R.id.btn_shown_help_read);

        m_Btn_ReadKeyAndInitProfile = new Button[8];
        m_Btn_Read = (Button) findViewById( R.id.btn_read );
        m_Btn_ReadKeyAndInitProfile[0] = (Button) findViewById( R.id.btn_init_profil_1 );
        m_Btn_ReadKeyAndInitProfile[1] = (Button) findViewById( R.id.btn_init_profil_2 );
        m_Btn_ReadKeyAndInitProfile[2] = (Button) findViewById( R.id.btn_init_profil_3 );
        m_Btn_ReadKeyAndInitProfile[3] = (Button) findViewById( R.id.btn_init_profil_4 );
        m_Btn_ReadKeyAndInitProfile[4] = (Button) findViewById( R.id.btn_init_profil_5 );
        m_Btn_ReadKeyAndInitProfile[5] = (Button) findViewById( R.id.btn_init_profil_6 );
        m_Btn_ReadKeyAndInitProfile[6] = (Button) findViewById( R.id.btn_init_profil_7 );
        m_Btn_ReadKeyAndInitProfile[7] = (Button) findViewById( R.id.btn_init_profil_8 );



        m_Btn_shown_help_init.setOnClickListener( this );
        m_Btn_shown_help_read.setOnClickListener( this );

        for( int i = 0; i < m_Btn_ReadKeyAndInitProfile.length; ++i )
        {
            m_Btn_ReadKeyAndInitProfile[i].setOnClickListener( this );
        }

        m_Btn_Read.setOnClickListener( this );
        m_Btn_Read.setEnabled( false );


        m_Btn_Read_38BB5CF5 = (Button) findViewById( R.id.btn_read_38BB5CF5 );
        m_Btn_Read_38BB5CF5.setOnClickListener( this );
        if( true )
        {
            m_Btn_Read_38BB5CF5.setVisibility( View.VISIBLE );
        }





    }







    @Override
    public void onClick(View view)
    {

        if( BTUtil.IsBlueToothEnable(this) == false )
        {
            BTUtil.RequestBluetoothActivation( this );
            return;
        }




        // Click on btn_init_profil*
        for( int i =  0; i < m_Btn_ReadKeyAndInitProfile.length; ++i )
        {
            if( view == m_Btn_ReadKeyAndInitProfile[i] )
            {
                int profilID = i + 1;
                String k = Scale700Api.ReadKeyAndInitProfile( this, m_TimeoutSeconds,  profilID );
                if( k == null )
                {
                    m_Key = null;
                    m_Btn_Read.setEnabled( false );
                    UIUtil.ShowError( this, "Failed to get KEY" );
                    return;
                }
                m_Key = k;
                m_Btn_Read.setEnabled( true );
                UIUtil.ShowMessage( this, "Got KEY :  " + m_Key );
                return;
            }
        }


        if( view == m_Btn_Read )
        {
            if( m_Key == null )
            {
                UIUtil.ShowError( this, "Read key first");
                return;
            }
            final int weight = Scale700Api.ReadWeight( this, m_TimeoutSeconds, m_Key );
            setWeight( weight );
        }
        else if( view == m_Btn_Read_38BB5CF5 )
        {
            final int weight = Scale700Api.ReadWeight( this, m_TimeoutSeconds, "38BB5CF5" );
            setWeight( weight );
        }


        else if( view == m_Btn_shown_help_init )
        {
            m_WebViewHelp.loadDataWithBaseURL( null, getString(R.string.help_init), "text/html", "utf-8", null );
        }
        else if( view == m_Btn_shown_help_read )
        {
            m_WebViewHelp.loadDataWithBaseURL( null, getString(R.string.help_read), "text/html", "utf-8", null );
        }

        else
        {
            Log.e( TAG, "onClick() : view not handled" );
        }

    }


    private void setWeight( int weight )
    {
        if( weight == -1 )
        {
            UIUtil.ShowError( this, "Failed to read weight" );
            m_TextView_Weight.setText( "Failed to read" );
            return;
        }

        final float weightF = weight / 100.f;
        m_TextView_Weight.setText( weightF + " Kg" );
    }



}
