package org.bozlang;

/**
 * <p>Title: Boz Class for Literal Numeric - BozNumber      </p>
 * <p>Description: Literal Number Object for Boz Language</p>
 * <p>Copyright: Copyright (c) April 23, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.0
 */
//Note: Only RT.lit can create a literal Boz number 4-27-2008

public final class BozNumber implements IHandle //wrapper around native Java double
{
    private Double    number = null;  //use BigDecimal ??
    private BozString string = null;

    public final static BozNumber ZERO = new BozNumber(0);
    public final static BozNumber ONE  = new BozNumber(1);

    public BozNumber()
    {
        this(0.0);
    }//end constructor

    public BozNumber(final double dblNum)
    {
        this.number = new Double(dblNum);
    }//end BozNumber

    public BozNumber(final String dblNum)
    {
        this.number = Double.parseDouble(dblNum);
    }//end constructor

    public BozNumber(final long lngNum)
    {
        this.number = new Double(lngNum * 1.0);
    }//end constructor

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
    public final BozNumber _val()
    {
        return this;
    }//end val

    public final BozString _str()
    {
        if(this.string == null)
        {
            this.string = new BozString(this.number.toString());
        }//end if

        return this.string;
    }//end str

    public final String toString()
    {
        return number.toString();
    }//end toString

    public final Double toNumber()
    {
        return number;
    }//end toNumber

    public final boolean toBool()
    {
        throw new BozTrap("NonBoolTrap","Non Boolean value true or false.");
    }

    public final boolean _isNumber()
    {
        return true;
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
        throw new BozTrap("NonStreamNumberTrap","Number is non-stream");
    }//end reader

    public final IHandle _export(IHandle hdl)
    {
        throw new BozTrap("NonStreamNumberTrap","Number is non-stream");
    }//end writer

    public final IHandle _get()
    {
        return this;
    }//end get

    public final void _set(final IHandle hdl)
    {
       throw new BozTrap("NonNumberSetTrap","Number is immutable literal");
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
        throw new BozTrap("NonGlobAssociativeAccess","Access non-glob with key");
    }//end by

}//end class BozNumber
