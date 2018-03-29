package org.bozlang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>Title: Boz Class for Standard Input-Output - BozIO</p>
 * <p>Description: Standard IO Object for Boz Language</p>
 * <p>Copyright: Copyright (c) April 23, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.0
 */
//Note: Only RT.io or io function can return BozIO instance class; 4-27-2008.

public final class BozIO implements IHandle
{
    public BozIO() {}

    private final static BozIO IO = new BozIO();

    public final static BozIO io(){ return BozIO.IO; }

    // >>>
    public final IHandle _import(IHandle hdl) //read from input stream into IHandle hdl
    {
        //use System.in.readln() ??
        //hdl = readln();
        //System.out.println("### before BozIO._import()");
        //System.out.println("### BozIO._import() handle = "+hdl.toString());
        //System.out.println("### handle is Object = "+hdl._isObject());

        //*
        IHandle result = RT._reader();

        if(hdl._isObject())
        {
             hdl._set(result);
        }
        else
        {
            hdl = RT.obj(result);
            //hdl = result;

        }
        //*/
        //System.out.println("### BozIO._import() handle = "+hdl.toString());

        //hdl._set(RT._reader());   //read stream into buffer, copy buffer until separator character into handle??
        //System.out.println("### after BozIO._import()");
        //System.out.println("### BozIO._import() handle = "+hdl.toString());

        return this;
    }//end reader

    // <<<
    public final IHandle _export(IHandle hdl) //read from output stream into IHandle hdl
    {
        //use write ??
        //System.out.print(hdl._get().toString());  //flush
        RT._writer(hdl); //or hdl._get() ??
        //
        return this;
    }//end writer


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
        throw new BozTrap("IOToStringTrap", "Error: IO does not convert to native string form.");
    }//end toString

    public final Double toNumber()
    {
        throw new BozTrap("IOToNumberTrap", "Error: IO does not convert to native number form.");
    }//end toNumber

    public final boolean toBool()
    {
        throw new BozTrap("NonBoolTrap","Non Boolean value true or false.");
    }//end toBool

    public final BozNumber _val()
    {
        throw new BozTrap("IOToNumberTrap", "Error: IO does not convert to number form.");
    }//end val

    public final BozString _str()
    {
        throw new BozTrap("IOToStringTrap", "Error: IO does not convert to string form.");
    }//end str

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
        return true;
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

    public final IHandle _get()
    {
        return this;
    }//end get

    public final void _set(final IHandle hdl)
    {
       throw new BozTrap("NonIOSetTrap","IO is immutable literal");
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
}//end class BozIO
