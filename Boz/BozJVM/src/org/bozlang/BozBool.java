package org.bozlang;

/**
 * <p>Title: Boz Class for Literal Boolean - BozBool      </p>
 * <p>Description: Literal Boolean Object for Boz Language</p>
 * <p>Copyright: Copyright (c) April 27, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.0
 */
//Note: Only RT.lit can create a literal Boz boolean 4-27-2008

public final class BozBool implements IHandle //wrapper around native Java double
{
    private BozNumber number = null;
    private BozString string = null;

    public final static String FALSE_STR = "false";
    public final static String TRUE_STR  = "true";

    public final static BozBool FALSE = new BozBool(BozNumber.ZERO, FALSE_STR);
    public final static BozBool TRUE  = new BozBool(BozNumber.ONE,  TRUE_STR);

    public final IHandle _icall(final String mthd, IHandle[] args)
    {
        throw new BozTrap("RuntimeTrap","Instance call to method not handled.");
    }//end _icall

    public final boolean _iexec(final String mthd, IHandle[] args)
    {
        throw new BozTrap("RuntimeTrap","Instance exec to method not handled.");
    }//end _iexec

/*
    public final IHandle _iarg(final IHandle[] args)
    {
        throw new BozTrap("RuntimeTrap","Arguments to method not handled.");
    }//end _iarg
*/
    private BozBool(final BozNumber number, final String string)
    {
        this.number = number;
        this.string = new BozString(string);
    }//end constructor

    public final static BozBool True()
    {
        return BozBool.TRUE;
    }//end True

    public final static BozBool False()
    {
        return BozBool.FALSE;
    }//end False

    public final boolean toBool()
    {
        if(this.number == BozNumber.ONE)
        {
            return true;
        }
        else
        {
            return false;
        }//end if

    }//end toBoolean

    public final BozNumber _val()
    {
        return this.number;
    }//end val

    public final BozString _str()
    {
        return this.string;
    }//end str

    public final String toString()
    {
        return string.toString();
    }//end toString

    public final Double toNumber()
    {
        return number.toNumber();
    }//end toNumber

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
        return true;
    }//end isBool

    public final boolean _isInstance()
    {
        return false;
    }//end _isInstance

    public final boolean _isGlob(){ return false; }

    public final IHandle _import(IHandle hdl)
    {
        throw new BozTrap("NonStreamBoolTrap","Bool is non-stream");
    }//end reader

    public final IHandle _export(IHandle hdl)
    {
        throw new BozTrap("NonStreamBoolTrap","Bool is non-stream");
    }//end writer

    public final IHandle _get()
    {
        return this;
    }//end get

    public final void _set(final IHandle hdl)
    {
       throw new BozTrap("NonBoolSetTrap","Bool is immutable literal");
    }//end _set

    public final long _size()
    {
        throw new BozTrap("NonGlobSize","Size non-existent for non-glob.");
    }//end size

    public final boolean _isObject(){ return false; }

    public final IHandle _at(final IHandle hdl)
    {
        throw new BozTrap("NonGlobIndexAccess","Access non-glob with index.");
    }//end at

    public final IHandle _iget(final String attr)
    {
        throw new BozTrap("RuntimeTrap","Attribute: "+attr+" not found in class.");
    }//end _iget

/*
    public final IHandle by(final IHandle hdl)
    {
        throw new BozTrap("NonGlobAssociativeAccess","Access non-glob with key");
    }//end by
*/
    public final IHandle _by(final IHandle[] hdl)
    {
        throw new BozTrap("NonGlobAssociativeAccess","Access non-glob with key.");
    }//end by

}//end class BozBool
