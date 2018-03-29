package org.bozlang;

/**
 * <p>Title: Boz Language Handle for Literal, Object, Glob, Stream  </p>
 * <p>Description: Interface Handle for Boz Literal, Object, Glob, Stream</p>
 * <p>Copyright: Copyright (c) April 23, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.4
 */

/*  June 19, 2008.
    Methods are name mangled from Boz to host language/Java
        to be opaque, invisble; and non-clash or collide.

    1. prefix with underscore - non-invokeable in Boz, used in host language/Java
           or _method. Method is opaque or invisible.

    2. suffix with underscore - invokeable under name, but code synthesis in
           host language/Java or method_ invoked as "method" to avoid name conflict
           with host language/Java. Method toString in Boz is toString_() in code
           synthesized but invoked as "toString". Method is non-clash.

    September 04, 2008.
       removed _iarg method
       modified _icall, _iexec to take IHandle[] args

 */

public interface IHandle
{
   //static attributes and methods:
   //
   //public static IHandle _scall(final String mthd, IHandle[] args);
   //public static IHandle _sget(final String attr);
   //public static void    _sret(final IHandle hdl);

   /* unit access methods for method in code synthesis */
   public IHandle _icall(final String mthd, IHandle[] args);//invoke method by class - inherit
   public boolean _iexec(final String mthd, IHandle[] args);//invoke method of class - local


    /* unit access methods for attribute in code synthesis */
    public IHandle _iget(final String attr);

    /* unit method handling */
    //public IHandle _iarg(IHandle[] args);  //set method params passed to called method -- remove for new

    /* handle to scalar forms */
    public BozNumber _val(); //number form of handle
    public BozString _str(); //string form of handle

    /* handle to streaming-reader and writer */
    public IHandle _import(IHandle hdl); //import from input  stream into IHandle hdl
    public IHandle _export(IHandle hdl); //export from output stream into IHandle hdl

    /* handle containment access */
    public IHandle _get();
    public void    _set(IHandle hdl);  //_iret(IHandle hdl)??

    /* native language/Java scalar conversion - String or Number encoding - not invokeable*/
    public String  toString();
    public Double  toNumber();
    public boolean toBool();

    /* query for specific form/kind - number, string */
    public boolean _isNumber();
    public boolean _isString();
    public boolean _isStream();
    public boolean _isBool();

    /* determine if object is singular - 1 object or multiple - N glob */
    public boolean _isGlob();
    public boolean _isObject();

    /* determine if instance (user defined class) or non-instance */
    public boolean _isInstance();

    /* glob query parameters */
    //public IHandle _size();
    public long _size();

    /* glob access positioning */
    public IHandle _at(final IHandle   hdl);
    public IHandle _by(final IHandle[] hdl);  //use RT.box(): IHandle[] for multi-param lists

}//end interface IHandle
