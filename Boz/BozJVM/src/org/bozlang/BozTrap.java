package org.bozlang;

/**
 * <p>Title: Boz Exception Class - Trap      </p>
 * <p>Description: Trap Object for Boz Language Exceptions</p>
 * <p>Copyright: Copyright (c) April 23, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.1
 */

//BozTrap generalize to object, takes an object or string when snapped. 06-01-2008
public final class BozTrap extends RuntimeException implements IHandle
{
    private BozString trap_ = null;
    private BozString ctx_  = null;
    private BozNumber num_  = null;

    public BozTrap(final BozString trap)
    {
        this.trap_ = trap;
        this.ctx_  = BozString.EMPTY;
        this.num_  = new BozNumber(trap.toString().length());
    }//end constructor

    public BozTrap(final BozString trap, final BozString ctx)
    {
        this.trap_ = trap;
        this.ctx_  = ctx;
        this.num_  = new BozNumber(trap.toString().length());
    }//end constructor

    public BozTrap(final String trap)
    {
        super("");
        this.trap_ = new BozString(trap);
        this.ctx_  = BozString.EMPTY;
        this.num_  = new BozNumber(trap.toString().length());
    }//end BozTrap

    //specific trap kind and specific context message
    public BozTrap(final String trap, final String ctx)
    {
        super(ctx);
        this.trap_ = new BozString(trap);
        this.ctx_  = new BozString(ctx);
        this.num_  = new BozNumber(trap.toString().length());
    }//end BozTrap
    
    public final IHandle _icall(final String mthd, IHandle[] args)
    {
        if(mthd.equals("cause"))
        {
            switch(args.length)
            {
                case 0  : return this.cause_();
                default : throw new BozTrap("RuntimeTrap","Method:"+mthd+" of: "+args.length+" parameters not found in class."); 
            }//end switch
        }
        else
        {
            throw new BozTrap("RuntimeTrap","Method:"+mthd+" of: "+args.length+" parameters not found in class.");
        }//end if     

    }//end _icall

    public final boolean _iexec(final String mthd, IHandle[] args)
    {
        throw new BozTrap("RuntimeTrap","Instance exec to method not handled.");
    }//end _iexec

    public final boolean equals(Object obj)
    {
        return RT.fnvhash(obj.toString()) == this.hashCode();
    }//end equals

    public final int hashCode()
    {  
        return RT.fnvhash(this.toString()); 
    }//end hashCode

    public final String toString()
    {
        return this.trap_.toString(); //+":  "+this.ctx_.toString();
    }//end toString

    public final BozNumber _val()
    {
        return this.num_;
    }//end _val

    public final BozString _str()
    {
        return this.trap_;
    }//end _str

    public final BozString cause_() //BozTrap specific method
    {
        return this.ctx_;
    }//end cause_
        
    public final Double toNumber()
    {
         return this.num_.toNumber();
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

    public final boolean _isGlob()
    { 
        return false; 
    }//end _isGlob

    public final boolean _isInstance()
    {
        return false;
    }//end _isInstance

    public final IHandle _import(IHandle hdl)
    {
        throw new BozTrap("NonStreamTrap","Trap is non-stream");
    }//end reader

    public final IHandle _export(IHandle hdl)
    {
        throw new BozTrap("NonStreamTrap","Trap is non-stream");
    }//end writer

    public final IHandle _get()
    {
        return this;
    }//end get

    public final void _set(final IHandle hdl)
    {
       throw new BozTrap("NonTrapSetTrap","Trap is immutable object");
    }//end _set

    public final long _size()
    {
        throw new BozTrap("NonGlobSize","Size non-existent for non-glob.");
    }//end size

    public final boolean _isObject(){ return false; }

    public final IHandle _at(final IHandle hdl)
    {
        throw new BozTrap("NonGlobIndexAccess","Access non-glob with index");
    }//end _at

    public final IHandle _by(final IHandle[] hdl)
    {
        throw new BozTrap("NonGlobAssociativeAccess","Access non-glob with key");
    }//end _by

    public final IHandle _iget(final String attr)
    {
        throw new BozTrap("RuntimeTrap","Attribute: "+attr.substring(0,attr.length()-1)+" not found in class.");
    }//end _iget

}//end class BozTrap
