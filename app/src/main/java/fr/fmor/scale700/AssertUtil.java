package fr.fmor.scale700;

class AssertUtil {


    public static  void AssertNotNull( Object o )
    {
        if( o == null )
            throw  new RuntimeException( "AssertNotNull : " + o.getClass().getSimpleName()  );
    }


    public static  void AssertIsAlphaNum( char c )
    {
        if( Character.isLetterOrDigit(c) == false )
            throw new RuntimeException( "AssertIsAlphaNum : '" + c + "'' is not a alphanumeric ");

    }

    public static  void AssetTrue( boolean b )
    {
        if( b == false )
            throw  new RuntimeException( "AssetTrue : " + b );
    }

}
