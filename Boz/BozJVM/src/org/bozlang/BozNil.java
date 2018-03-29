package org.bozlang;

/**
 * <p>Title: Boz Class for Nullity - BozNil      </p>
 * <p>Description: Nil Object for Boz Language</p>
 * <p>Copyright: Copyright (c) April 23, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.0
 */

public final class BozNil implements IHandle
{
    private BozNil() {}

    private final static BozNil    NIL = new BozNil();
    private final static BozNumber NUM = new BozNumber(0.0);
    private final static BozString STR = new BozString("nil");

    public final static BozNil nil()
    {
        return BozNil.NIL;
    }//end nil

    public final BozNumber _val()
    {
        return BozNil.NUM;
    }//end val

    public final BozString _str()
    {
        return BozNil.STR;
    }//end str

    public final IHandle _icall(final String mthd, IHandle[] args)
    {
        throw new BozTrap("RuntimeTrap","Instance call to method not handled.");
    }//end _icall

    public final boolean _iexec(final String mthd, IHandle[] args)
    {
        throw new BozTrap("RuntimeTrap","Instance exec to method not handled.");
    }//end _iexec

    public final String toString()
    {
        return new String("nil");
    }//end toString

    public final Double toNumber()
    {
        return BozNil.NUM.toNumber();
    }//end toNumber

    public final boolean toBool()
    {
        throw new BozTrap("NonBoolTrap","Non Boolean value true or false.");
    }//end toBool

    public final boolean _isNumber()
    {
        return false;
    }//end isNumber

    public final boolean _isString()
    {
        return false;
    }//end isString

    public final boolean _isStream()
    {
        return false;
    }//end isStream

    public final boolean _isBool()
    {
        return false;
    }//end isBool

    public final boolean _isGlob(){ return false; }

    public final boolean _isInstance()
    {
        return false;
    }//end _isInstance

    public final IHandle _import(IHandle hdl)
    {
        throw new BozTrap("NonStreamNilTrap","Nil is non-stream");
    }//end reader

    public final IHandle _export(IHandle hdl)
    {
        throw new BozTrap("NonStreamNilTrap","Nil is non-stream");
    }//end writer

    public final IHandle _get()
    {
        return this;
    }//end get

    public final void _set(final IHandle hdl)
    {
       throw new BozTrap("NonNilSetTrap","Nil is immutable literal");
    }//end _set

    public final long _size()
    {
        throw new BozTrap("NonGlobSize","Size non-existent for non-glob.");
    }//end size

    public final boolean _isObject(){ return false; }

    public final IHandle _at(final IHandle hdl)
    {
        throw new BozTrap("NonGlobIndexAccess","Access non-glob with index");
    }//end at

    public final IHandle _by(final IHandle[] hdl)
    {
        throw new BozTrap("NonGlobAssociativeAccess","Access non-glob with key");
    }//end by

    public final IHandle _iget(final String attr)
    {
        throw new BozTrap("RuntimeTrap","Attribute: "+attr+" not found in class.");
    }//end _iget

/*
    public final IHandle _iarg(final IHandle[] args)
    {
        throw new BozTrap("RuntimeTrap","Arguments to method not handled.");
    }//end _iarg
*/

}//end class BozNil
