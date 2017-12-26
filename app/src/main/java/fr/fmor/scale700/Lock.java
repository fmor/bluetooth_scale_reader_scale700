package fr.fmor.scale700;


class Lock
{
    private String m_Name;

    private  boolean m_IsEnable;
    private  int m_Count;


    Lock( String name )
    {
        m_IsEnable = true;
        m_Count = 0;
        m_Name = name;
    }

    public synchronized void lock()
    {
        lock( 1 );
    }

    public synchronized void lock( int c )
    {
        if( m_IsEnable == false )
        {
            Logger.log_debug( "Lock is disable");
            return;
        }

        Logger.log_debug( "Lock : count = " + c + " " + m_Name );
        try {
            m_Count = c;
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public synchronized void unlock()
    {
        --m_Count;
        Logger.log_debug( "Unlock : m_Count = " + m_Count );
        if( m_Count == 0 )
            this.notify();
    }

    public synchronized  void unlockAll()
    {
        Logger.log_debug( "UnlockAll : m_Count = " + m_Count );
        m_Count = 0;
        this.notify();
    }

    public synchronized  void setEnable( boolean enable )
    {

        m_IsEnable = enable;
        if( m_IsEnable == false )
            unlockAll();
    }





    public static Lock LOCK = new Lock("Lock");




}
