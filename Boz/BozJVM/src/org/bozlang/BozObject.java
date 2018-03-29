package org.bozlang;

/**
 * <p>Title: Boz Class for Instance Object - BozObject </p>
 * <p>Description: Boz Instance Object for Boz Language</p>
 * <p>Copyright: Copyright (c) May 05, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.0
 */
//Note only RT.Object(IHandle) : IHandle can create a BozObject

public final class BozObject implements IHandle
{
    //delegate Boz handle contained or aggregated by BozObject
    private IHandle _ival = RT.nil();

    //public BozObject(){}

    public BozObject(final IHandle hdl)
    {
        //System.out.println("BozObject() hdl        = "+hdl.toString());
        //System.out.println("BozObject() hdl._get() = "+hdl._get().toString());

        //this._ival = hdl;
        //this._ival = hdl._get();

        if(hdl._isObject())
        {
            this._ival = hdl._get();
        }
        else
        {
            this._ival = hdl;
        }//end if

    }//end constructor

    public final IHandle _icall(final String mthd, IHandle[] args)
    {
        this._ival._icall(mthd, args);
        return this;
    }//end _icall

    public final boolean _iexec(final String mthd, IHandle[] args)
    {
        return this._ival._iexec(mthd, args);  //error or forward call to wrapped instance ??
    }//end _iexec

    public final BozNumber _val()
    {
        return this._ival._val();
    }//end val

    public final BozString _str()
    {
        return this._ival._str();
    }//end str

    public final IHandle _import(IHandle hdl)
    {
        return this._ival._import(hdl);
    }//end _import

    public final IHandle _export(IHandle hdl)
    {
        return this._ival._export(hdl);
    }//end _export

    public final IHandle _get() //for non-glob return _ival, for glob return _ival._get()
    {
        return this._ival;   //check if glob, if so forward get to glob _ival ??  _ival._get()
    }//end get

    public final void _set(final IHandle hdl)
    {
       //System.err.println("### BozObject._set() handle = "+hdl.toString());
       //System.err.println("### BozObject._set() isObject:"+hdl._isObject());
       this._ival = hdl._get();
       /*
       if(hdl._isObject())
       {
           this._ival = hdl._get();
       }
       else
       {
           this._ival = hdl;
       }
       */
    }//end _set

    public final String  toString(){ return this._ival.toString(); }
    public final Double  toNumber(){ return this._ival.toNumber(); }
    public final boolean toBool(){   return this._ival.toBool();   }

    public final boolean _isNumber(){ return this._ival._isNumber(); }
    public final boolean _isString(){ return this._ival._isString(); }
    public final boolean _isStream(){ return this._ival._isStream(); }
    public final boolean _isBool(){   return this._ival._isBool();   }
    public final boolean _isGlob(){   return this._ival._isGlob();   }
    public final boolean _isObject(){ return true; }
    public final boolean _isInstance(){ return false; }

    public final long _size(){ return this._ival._size(); }

    public final IHandle _at(final IHandle hdl)
    {
        //throw new BozTrap("NonGlobIndexAccess","Access non-glob with index");
        return this._ival._at(hdl);
    }//end at

    public final IHandle _by(final IHandle[] hdl)
    {
        //throw new BozTrap("NonGlobAssociativeAccess","Access non-glob with key");
        return this._ival._by(hdl);
    }//end by

    public final IHandle _iget(final String attr)
    {
        //throw new BozTrap("RuntimeTrap","Attribute: "+attr+" not found in class.");
        return this._ival._iget(attr);
    }//end _iget

/*
    public final IHandle _iarg(final IHandle[] args)
    {
        //throw new BozTrap("RuntimeTrap","Arguments to method not handled.");
        this._ival._iarg(args);
        return this;
    }//end _iarg
*/
}//end class BozObject
