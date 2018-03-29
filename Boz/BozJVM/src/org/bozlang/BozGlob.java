package org.bozlang;
/**
 * <p>Title: Boz Class for Data Structure - BozGlob </p>
 * <p>Description: Boz Glob for Boz Language</p>
 * <p>Copyright: Copyright (c) May 09, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.0
 */
//Note only RT.Glob() : IHandle can create a BozGlob

public final class BozGlob implements IHandle
{
    private boolean  assocGlobFlag = false;
    private final AssociativeArray map = new AssociativeArray();

    private IHandle top = RT.nil();
    private String  key = null;

    public BozGlob()
    {
    }//end constructor


    public final AssociativeArray _map(){ return this.map; }

    public final void init_(final IHandle[] data)  //initArray(IHandle) : void
    {
        for(int x=0;x<data.length;x++)
        {
            this.map.add(new Integer(x), data[x]);
        }//end for

    }//end init_

    //check if Glob has duplicate keys, throw runtime error ?? Nov 27 2008
    public final void init_(final IHandle[] keys, final IHandle[] vals) //initAssoc
    {
        this.assocGlobFlag = true;
        for(int x=0;x<keys.length;x++)
        {
            this.map.add(keys[x].toString(), vals[x]);
        }//end for

    }//end init_
    
    public final String mapToString()
    { 
        StringBuffer result = new StringBuffer();
        
        result.append("<");
        if(this.assocGlobFlag) result.append("{");
        else                   result.append("[");
        
        result.append(" ");
        for(int x=0;x<this.map.size()-1;x++)
        {
            result.append(this.map.getKeyAt(x));
            result.append("=");
            result.append(this.map.getValueAt(x));
            result.append(", ");
        }//end for
 
        result.append(this.map.getKeyAt(this.map.size()-1));
        result.append("=");
        result.append(this.map.getValueAt(this.map.size()-1));

        result.append(" ");
        if(this.assocGlobFlag) result.append("}");
        else                   result.append("]");
        result.append(">");
        
        return result.toString();
    
    }//end mapToString

    public final IHandle _at(final IHandle hdl) //at by index x@<NUMBER> or x@<EXPR>
    {
        //System.out.println("BozGlob._at() "+hdl.toString());
        //IHandle result = RT.nil();

        if(!hdl._isNumber())
        {
            throw new BozTrap("GlobIndexTrap", "Index is not a numeric scalar");
        }//end if

        int index = hdl.toNumber().intValue();

        //System.out.println("BozGlob.at() raw index = "+index);

        if(index < 0)
        {
            index = this.map.size() + index;
        }//end if

        //System.out.println("BozGlob.at() computed index = "+index);

        //check if index is 0 <= index < size()
        if(index >= this.map.size() || index < 0)
        {
            throw new BozTrap("GlobIndexTrap", "Index exceeds bounds for glob");
        }//end if

        this.top = (IHandle) this.map.getValueAt(index);
        //System.out.println("BozGlob.at() top = "+this.top.toString());

        //this.key = (String)  this.map.getKeyAt(index);
        //System.out.println("BozGlob.at() key = "+this.key);

        return this;
    }//end at

    public final IHandle _by(final IHandle[] hdl) //x?<EXPRLIST>
    {
        StringBuffer str = new StringBuffer();

        for(int x=0;x<hdl.length-1;x++)
        {
            str.append(hdl[x].toString());
            str.append(",");
        }//end for

        str.append(hdl[hdl.length-1]);

        this.key = str.toString();

        this._by();

        return this;
    }//end by

    private final void _by()
    {
        if(this.map.has(this.key))
        {
            top = (IHandle) this.map.get(this.key);
        }
        else
        {
            top = RT.nil();
        }//end if

    }//end by

    public final BozNumber _val()  // %aglob => returns size of the glob
    {
        //return this.top._val();
        return new BozNumber(this.map.size());
    }//end val

    public final BozString _str()
    {
        //return this.top._str();
        //return new BozString(this.map.toString());
        return new BozString(this.mapToString());
    }//end str

    /* handle to streaming-reader and writer */
    public final IHandle _import(IHandle hdl)
    {
        throw new BozTrap("NonStreamGlobTrap","Glob is non-stream");
    }//end _import

    public final IHandle _export(IHandle hdl)
    {
        throw new BozTrap("NonStreamGlobTrap","Glob is non-stream");
    }//end _export

    public final IHandle _get()
    {
        IHandle result = this.top;

        if(this.top == RT.nil())
        {
            this.map.remove(this.key);
            this.key = null;
        }//end if

        return result;

    }//end _get

    public final void  _set(IHandle hdl)
    {
        IHandle obj = null;
        //check this.key == null => BozTrap("GlobSetTrap", "No position specified to set value in glob");
        if(this.key == null) throw new BozTrap("GlobSetTrap", "No position specified to set value in glob");

        if(this.map.has(this.key))
        {
            obj = (IHandle) this.map.get(this.key);
            obj._set(hdl);
        }
        else
        {
            obj = RT.obj(hdl);
            this.map.add(this.key, obj);
        }//end if

    }//end _set

    /* access by integer position and/or get integer size; used by
          do var : glob : BLOCK .                                  */
    //public final int _len(){ return this.map.size(); }  //?? remove but change code synthesis _len
    public final IHandle _of(final int pos){ return (IHandle) this.map.getValueAt(pos); }

    /* native language/Java scalar conversion - String or Number encoding */
    public String  toString(){ return this.mapToString();          }
    public Double  toNumber(){ return new Double(this.map.size()); }
    public boolean toBool(){ throw new BozTrap("NonBoolTrap","Non Boolean value true or false."); }

    /* query for specific form/kind - number, string */
    public boolean _isNumber(){ return this.top._isNumber(); }
    public boolean _isString(){ return this.top._isString(); }
    public boolean _isStream(){ return this.top._isStream(); }
    public boolean _isBool(){   return this.top._isBool();   }

    /* determine if object is singular - 1 object or multiple - N glob */
    public boolean _isGlob(){   return true; }  //need to determine if a glob??
    public boolean _isObject(){ return false; }

    public final boolean _isInstance()
    {
        return false;
    }//end _isInstance

    /* glob query parameters */
    public long _size() //used by do-statement to iterate through glob
    { 
        //return RT.obj(RT.lit(this.map.size()));
        return this.map.size();
    }//end _size
    
public final IHandle _my    = this;
private      IHandle _ival = RT.nil();

public final IHandle _iget(final String attr)
{
    throw new BozTrap("RuntimeTrap","Attribute: "+attr.substring(0,attr.length()-1)+" not found in class.");
}//end _iget

public final IHandle[] _idx = new IHandle[]{ _my };

public final IHandle _icall(final String mthd, IHandle[] args)
{                                           
  this._ival = RT.obj(RT.nil());            

  for(int x=0;x<_idx.length;x++)
  {           
      if(_idx[x]._iexec(mthd,args))
      {        
           this._ival = _idx[x]._get();     
           return this._ival;               
      }//end if                                

  }//end for                                         

 throw new BozTrap("RuntimeTrap","Method:"+mthd+" of: "+args.length+" parameters not found in class."); 

}//end _icall                                             

public final boolean _iexec(final String mthd, IHandle[] args)
{

    boolean result = true;

    result =  false;

    return result;
}//end _iexec

public final static IHandle _sget(final String attr)
{
    throw new BozTrap("RuntimeTrap","Attribute: "+attr.substring(0,attr.length()-1)+" not found in class.");
}//end _sget

public static IHandle    _sval = null;

public final static void _sret(final IHandle _rval)
{ 
    BozGlob._sval = _rval; 
}//end _sret

public final static IHandle _scall(final String mthd, final IHandle[] args)
{
    BozGlob._sval = RT.obj(RT.nil());
    throw new BozTrap("RuntimeTrap","Static method not found:  "+mthd);
}//end _scall

}//end class BozGlob
