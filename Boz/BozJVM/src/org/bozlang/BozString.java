package org.bozlang;

/**
 * <p>Title: Boz Class for Literal String - BozString </p>
 * <p>Description: Literal String Object for Boz Language</p>
 * <p>Copyright: Copyright (c) April 23, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.0
 */
//Note: Only RT.lit can create a literal Boz string 4-27-2008

public class BozString implements IHandle //wrapper around native Java string
{
    public final static BozString EMPTY = new BozString("");

    private String    string = null; //native Java string
    private BozNumber number = null; //hash of Java string

    public BozString()
    {
        this.string = new String("");
    }//end constructor

    public BozString(final char chr)
    {
        this.string = Character.toString(chr);
    }//end constructor

    public BozString(final String str)
    {
        this.string = str;
    }//end constructor

    public final IHandle _icall(final String mthd, IHandle[] args)
    {
        throw new BozTrap("RuntimeTrap","Instance call to method not handled.");
    }//end _icall

    public final boolean _iexec(final String mthd, IHandle[] args)
    {
        throw new BozTrap("RuntimeTrap","Instance exec to method not handled.");
    }//end _iexec

    public final BozNumber _val()
    {
        if(this.number == null)
        {
            //this.number =  new BozNumber(Double.longBitsToDouble(BozString.fnvhash(this.string)));
            this.number =  new BozNumber(Double.longBitsToDouble(RT.fnvhash(this.string)));

        }//end if

        return this.number;

    }//end val

    public final BozString _str()
    {
        return this;
    }//end str

    public final String toString()
    {
        return string;
    }//end toString

    public final Double toNumber()
    {
        if(this.number == null)
        {
            //this.number =  new BozNumber(Double.longBitsToDouble(BozString.fnvhash(this.string)));
            this.number =  new BozNumber(Double.longBitsToDouble(RT.fnvhash(this.string)));

        }//end if

        return this.number.toNumber();

    }//end toNumber

    public final boolean toBool()
    {
        throw new BozTrap("NonBoolTrap","Non Boolean value true or false.");
    }

    /*
    public final static long fnvhash(String str)
    {
        long result = 0xcbf29ce484222325L;

        for (int i = 0; i < str.length(); i++)
        {
            result ^= str.charAt(i);
            result *= 0x00000100000001b3L;
        }//end for

        return result;

    }//end fnvhash
    //*/
    
    public final boolean _isNumber()
    {
        return false;
    }//end isNumber

    public final boolean _isString()
    {
        return true;
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
        throw new BozTrap("NonStreamStringTrap","String is non-stream");
    }//end reader

    public final IHandle _export(IHandle hdl)
    {
        throw new BozTrap("NonStreamStringTrap","String is non-stream");
    }//end writer

    public final IHandle _get()
    {
        return this;
    }//end get

    public final void _set(final IHandle hdl)
    {
       throw new BozTrap("NonStringSetTrap","String is immutable literal");
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
/*
    public final IHandle _iarg(final IHandle[] args)
    {
        throw new BozTrap("RuntimeTrap","Arguments to method not handled.");
    }//end _iarg
*/
    public final IHandle _iget(final String attr)
    {
        throw new BozTrap("RuntimeTrap","Attribute: "+attr+" not found in class.");
    }//end _iget

    public final IHandle _by(final IHandle[] hdl)
    {
        throw new BozTrap("NonGlobAssociativeAccess","Access non-glob with key");
    }//end by

}//end class BozString
