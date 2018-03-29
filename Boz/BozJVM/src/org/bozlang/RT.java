package org.bozlang;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * <p>Title: Boz Class for Runtime - RT      </p>
 * <p>Description: Runtime Class for Boz Language</p>
 * <p>Copyright: Copyright (c) April 25, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.2
 */

//add math functions abs, sin, cos ?? 5-10-2008 -- no.

//add trace functionality for RT functions ??

//add functionality for literals to avoid duplicate objects/references 5-22-2008 ??
//   yes for literals, literal table.
//_env - set runtime environment, such as eoln, total memory ?? 6-01-2008

//add RT.new(String) : IHandle -- create instance with name but in underlying
// host language??  RT.new("org.bozlang.TestObject"); ??  11-15-2008

public final class RT
{
    private RT() {}

    private final static BozString EOLN = new BozString(System.getProperty("line.separator")); //=new BozString("\n\r"); //Win-platform

    //private final static AssociativeArray litMap = new AssociativeArray(); //literal map

    private static IHandle _argv = null;

    private final static void _args(final String[] args)
    {
        final IHandle[] result = new IHandle[args.length];

        for(int x=0;x<args.length;x++)
        {
            result[x] = RT.lit(args[x]);
        }//end for

        RT._argv = RT.obj(RT.glob(result)); //create object containing glob of args as indexed
    }//end _args

    //initialize runtime for program
    public final static void init(final String[] args) //other initialization parameters ??
    {
        RT._args(args);
    }//end init

    //halt runtime for program ??
    public final static void halt()   //halt with integer RT.halt(1) ?? 9-15-2008
    {
        System.exit(0);
    }//end halt

    public final static IHandle args()
    {
        return RT._argv;
    }//end args

    public final static void exit(final IHandle hdl)
    {
        if(hdl._isNumber())
        {
            System.exit(hdl.toNumber().intValue());
        }
        else
            throw new BozTrap("RuntimeTrap","Handle for exit is non-number."); //RT.snap??

    }//end exit

    public final static IHandle eoln()  //predefined function eoln
    {
        //return EOLN;
        return RT.obj(EOLN);
    }//end eoln

    public final static IHandle obj(final IHandle hdl)
    {
        return new BozObject(hdl);
        //IHandle result = new BozObject();
        //result._set(hdl);
        //return result;
    }//end object

    public final static BozGlob toGlob(final IHandle hdl) //cast IHandle to glob for runtime code synthesis
    {
        if(hdl._isGlob())
        {
            return (BozGlob)hdl;
        }
        else
            throw new BozTrap("RuntimeTrap","Handle references non-glob instance."); //RT.snap??
    }//end toGlob

    public final static IHandle glob() // < >
    {
        return new BozGlob();
    }//end object

    //Glob(IHandle[]) //initialize with list of Boz Objects    < obj0, obj1, ... objN-1 >
    //Glob(IHandle[],IHandle[]) //initialize with key, values  < obj0:expr0, ... , objN-1:exprN-1 >

    public final static IHandle glob(final IHandle[] init)  //globArray
    {
        IHandle result = new BozGlob();
        BozGlob glob   = (BozGlob) result;

        glob.init_(init);

        return result;

    }//end glob

    public final static IHandle glob(final IHandle[] keys, final IHandle[] vals) //globAssoc
    {
        IHandle result = new BozGlob();

        BozGlob glob   = (BozGlob) result;

        glob.init_(keys,vals);

        return result;

    }//end glob

    //private final static IHandle NILOBJECT = RT.obj(RT.nil());
    /*
    public final static IHandle nilObj() //create new Boz Object initialized to nil
    {
        return new BozObject(BozNil.nil());
    }//end nilObj
    //*/

    //RT.ret(this, RT.nil() );
    public final static void ret(final IHandle obj, final IHandle val)
    {
        obj._set(val);
    }//end Return

    public final static void assign(final IHandle lval, final IHandle rval)
    {
         lval._set(rval._get());
    }//end Assign

    public final static IHandle io() //predefined function io
    {
        //return BozIO.io();
        return RT.obj(BozIO.io());
    }//end io

    //die - return memory to runtime, reference is now nil directly.

    public final static IHandle die(IHandle hdl) //predefined function die
    {
        //check hdl is object
        RT.isObj(hdl);  //query hdl.isObject() -- snap trap ?? 6-8-08

        hdl = null;

        System.gc(); //check some computed threshold like 0.75 of total memory? before gc ??

        hdl = RT.nil();  //RT.NilObject() ?? nil inside an object, or nil == null for Boz?? 5-22-2008

        return RT.nil(); //return hdl so   y = die: x; => x==y == nil 6-9-2008
    }//end die

    public final static void kill(final String str) //report error and exit with status=1 error 6-24-2008
    {
        System.err.println();
        System.err.println(str);
        System.err.println();
        System.exit(1);
    }//end kill

    public final static void kill(final BozTrap _bt)
    {
        RT.kill("Boz Trap: "+_bt.toString());
    }//end kill

    public final static void kill(final Exception _ex)
    {
        RT.kill("PlatformException: "+_ex.getMessage());
    }//end kill

    public final static void kill(final Error _er)
    {
        RT.kill("RuntimeError: "+_er.getMessage());
    }//end kill

    public final static IHandle lit(final char chr)
    {
        IHandle result = null;

        /*
        final String key = Character.toString(chr);

        if(RT.litMap.has(key))
        {
            return (BozString) RT.litMap.get(key);
        }

        result = new BozString(chr);

        RT.litMap.add(key, result);
        */
        result = new BozString(chr);

        return result;

    }//end lit

    public final static IHandle lit(final String str)
    {
        //add literals to literal table to avoid duplicates?? 4-25-08
        //orderedMap.has(str)
        //orderedMap.get(str) : IHandle

        IHandle result = null;

        if(RT.isNumber(str))
        {
            result = new BozNumber(str);
        }
        else
        {
            result = new BozString(str);
        }//end if

        //orderedMap.add(str,result);
        return result;

    }//end lit

    public final static IHandle isStream(final IHandle hdl) //check and return handle if stream
    {
        if(hdl._isStream()) return hdl;

        throw new BozTrap("RuntimeTrap","Handle references non-stream instance.");
    }//end isStream

    public final static IHandle isGlob(final IHandle hdl)
    {
        if(hdl._isGlob()) return hdl;

        throw new BozTrap("RuntimeTrap","Handle references non-glob instance."); //use RT.snap ??

    }//end isGlob

    public final static IHandle isObj(final IHandle hdl)
    {
        if(hdl._isObject()) return hdl;

        throw new BozTrap("RuntimeTrap","Handle references non-object instance.");

    }//end isObj

    public final static boolean isNumber(String str) //from Apache org.apache.commons.lang.NumberUtils 4-27-08
    {
        if(str == null || str.length() == 0)
        {
            return false;
        }//end if

        char[] chars = str.toCharArray();
        int    sz    = chars.length;

        boolean hasExp      = false;
        boolean hasDecPoint = false;
        boolean allowSigns  = false;
        boolean foundDigit  = false;

        // deal with any possible sign up front
        int start = (chars[0] == '-') ? 1 : 0;

        if (sz > start + 1)
        {
            if (chars[start] == '0' && chars[start + 1] == 'x')
            {
                int i = start + 2;

                if (i == sz)
                {
                    return false; // str == "0x"
                }//end if

                //checking hex (it can't be anything else)
                for (; i < chars.length; i++)
                {
                    if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F'))
                    {
                        return false;
                    }//end if
                }//end for
                return true;
            }//end if
        }//end if

        sz--; // don't want to loop to the last char, check it afterwords
              // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit))
        {
            if (chars[i] >= '0' && chars[i] <= '9')
            {
                foundDigit = true;
                allowSigns = false;
            }
            else if (chars[i] == '.')
            {
                if (hasDecPoint || hasExp)
                {
                    // two decimal points or dec in exponent
                    return false;
                }//end if
                hasDecPoint = true;
            }
            else if (chars[i] == 'e' || chars[i] == 'E')
            {
                // we've already taken care of hex.
                if (hasExp)
                {
                    // two E's
                    return false;
                }//end if
                if (!foundDigit)
                {
                    return false;
                }//end if
                hasExp     = true;
                allowSigns = true;
            }
            else if (chars[i] == '+' || chars[i] == '-')
            {
                if (!allowSigns)
                {
                    return false;
                }//end if
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            }
            else
            {
                return false;
            }//end if

            i++;
        }//end while

        if (i < chars.length)
        {
            if (chars[i] >= '0' && chars[i] <= '9')
            {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E')
            {
                // can't have an E at the last byte
                return false;
            }
            if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F'))
            {
                return foundDigit;
            }
            if (chars[i] == 'l'|| chars[i] == 'L')
            {
                // not allowing L with an exponent
                return foundDigit && !hasExp;
            }//end if
            // last character is illegal
            return false;
        }//end if

        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;

    }//end isNumber

    public final static IHandle lit(final long lngNum)
    {
        return new BozNumber(lngNum);
    }//end lit

    public final static IHandle lit(final double dblNum)
    {
        return new BozNumber(dblNum);
    }//end lit

    public final static IHandle True() //predefined function - true ?
    {
        return BozBool.TRUE;
    }//end True

    public final static IHandle False() //predefined function - false ?
    {
        return BozBool.FALSE;
    }//end False

    public final static IHandle nil() //predefined function nil
    {
        return BozNil.nil();
    }//end nil

    //May 17, 2008 - return the handle - which is redundant, or return
    //    the value of the handle object -- ?? --handle Oct 20 2008
    public final static IHandle expr(final IHandle hdl)
    {
        return hdl;
        /*
        if(hdl._isObject())
        {
            return hdl._get();
        }
        else
            return hdl;
        //*/
        //return hdl._get();  //causes bug of returning value in object instead of object reference 10-20-08
    }//end expr

    public static BozTrap bozTrap = null; //most recent bozTrap snapped; use Vector ?? 4-27-2008

    public final static IHandle trap() //predefined function trap
    {
        IHandle result = BozNil.nil();

        if(RT.bozTrap != null)
            result = RT.bozTrap;
        
        //return result;
        return RT.obj(result); //wrap trap recently snapped in object
    }//end trap

    public final static BozTrap snap(final IHandle trap, final IHandle ctx)
    {
        return RT.snap(trap._str(), ctx._str());
    }//end snap

    public final static BozTrap snap(final IHandle trap)
    {
        return RT.snap(trap._str());
    }//end snap

    private final static BozTrap snap(final BozString trap)
    {
        RT.bozTrap = new BozTrap(trap);
        return bozTrap;
    }//end snap

    private final static BozTrap snap(final BozString trap, final BozString ctx)
    {
        RT.bozTrap = new BozTrap(trap, ctx);
        return bozTrap;
    }//end snap

    public final static boolean boolExpr(final IHandle hdl)
    {
        if(hdl._isBool() == false)
        {
            throw new BozTrap("NonBoolTrap","Non Boolean value true or false.");
        }//end if

        return hdl.toBool();

    }//end boolExpr

    public final static IHandle bool(final boolean boolVal)
    {
        if(boolVal)
        {
            return BozBool.TRUE;
        }
        else
        {
            return BozBool.FALSE;
        }//end if

    }//end Bool

    public final static IHandle _reader() //standard input stream
    {
        IHandle        result = BozNil.nil();
        BufferedReader reader = new BufferedReader( new InputStreamReader( System.in));

        //System.out.println("### RT._reader() ");
        try
        {
            //String str = reader.readLine();
            //System.out.println("### RT._reader() = "+str);

            result = RT.lit(reader.readLine());
            //System.out.println("### RT._reader() result = ");
            //result = RT.lit(str);
            //System.out.println("### RT._reader() result = "+result.toString());

        }
        catch(IOException io)
        {
            System.err.println("Error: "+io.getMessage());
            throw new BozTrap("RuntimeTrap","Error BozIO import: "+io.getMessage());
        }//end try

        //System.out.println("### RT._reader() return = "+result.toString());

        return result;

    }//end _reader

    public final static void _writer(final IHandle hdl) //standard io stream
    {
        System.out.print(hdl.toString());
    }//end _writer

    public final static int fnvhash(final String str)
    {
        int    seed = 0x811c9dc5;
        byte[] buf  = str.getBytes();

        for (int i = 0; i < str.length(); i++)
        {
          seed += (seed << 1) + (seed << 4) + (seed << 7) + (seed << 8) + (seed << 24);  //fnvA is ^= +=
          seed ^= buf[i];
        }//end for

        return seed;

    }//end fnvhash

    public final static IHandle not(final IHandle hdl)
    {
        if(hdl._isBool() == false)
        {
            throw new BozTrap("NonBoolNotTrap","Error: Not boolean true or false value.");
        }//end if

        boolean bool = RT.boolExpr(hdl);
        return RT.bool(!bool);

    }//end not

    public final static IHandle lt(final IHandle lval, final IHandle rval)
    {
        IHandle result = BozBool.FALSE;

        if(lval.toString().compareTo(rval.toString()) < 0)
        {
            result = BozBool.TRUE;
        }//end if

        return result;
    }//end lt

    public final static IHandle le(final IHandle lval, final IHandle rval)
    {
        IHandle result = BozBool.FALSE;

        if(lval.toString().compareTo(rval.toString()) <= 0)
        {
            result = BozBool.TRUE;
        }//end if

        return result;
    }//end le

    public final static IHandle gt(final IHandle lval, final IHandle rval)
    {
        IHandle result = BozBool.FALSE;

        if(lval.toString().compareTo(rval.toString()) > 0)
        {
            result = BozBool.TRUE;
        }//end if

        return result;
    }//end gt

    public final static IHandle ge(final IHandle lval, final IHandle rval)
    {
        IHandle result = BozBool.FALSE;

        if(lval.toString().compareTo(rval.toString()) >= 0)
        {
            result = BozBool.TRUE;
        }//end if

        return result;
    }//end ge

    public final static IHandle eq(final IHandle lval, final IHandle rval)
    {
        IHandle result = BozBool.FALSE;

        if(lval.toString().compareTo(rval.toString()) == 0)
        {
            result = BozBool.TRUE;
        }//end if

        return result;
    }//end eq

    public final static IHandle ne(final IHandle lval, final IHandle rval)
    {
        IHandle result = BozBool.FALSE;

        if(lval.toString().compareTo(rval.toString()) != 0)
        {
            result = BozBool.TRUE;
        }//end if

        return result;
    }//end ne

    public final static IHandle inc(final IHandle val)
    {
        if(val._isNumber())
        {
            //val = new BozNumber(val.toNumber()+1.0);
            val._set(new BozNumber(val.toNumber()+1.0));
            return val;
        }
        else
        {
            throw new BozTrap("IncNonNumberTrap","Error: Increment on non-number scalar.");
        }//end if

    }//end inc

    public final static IHandle comp(final IHandle val)
    {
        if(val._isNumber())
        {
            long lngNum = val.toNumber().longValue();

            //extract upper-32 bits into int -> complement
            //extract lower-32 bits into int -> complement
            //re-assemble into long as BozNumber
            return new BozNumber(~lngNum);
        }
        else
        {
            throw new BozTrap("CompNonNumberTrap","Error: Complement on non-number scalar.");
        }//end if

    }//end comp

    public final static IHandle dec(final IHandle val)
    {
        if(val._isNumber())
        {
            //return new BozNumber(val.toNumber()-1.0);
            val._set(new BozNumber(val.toNumber()-1.0));
            return val;
        }
        else
        {
            throw new BozTrap("DecNonNumberTrap","Error: Decrement on non-number scalar.");
        }//end if

    }//end dec

    public final static Double number(final IHandle hdl)
    {
        return hdl.toNumber();
    }//end number

    public final static String string(final IHandle hdl)
    {
        return hdl.toString();
    }//end string

    public final static IHandle str(final IHandle hdl)
    {
        return hdl._str();
    }//end str

    public final static IHandle val(final IHandle hdl)
    {
        return hdl._val();
    }//end val

    public final static IHandle pos(final IHandle hdl)
    {
        if(hdl._isNumber())
        {
            return new BozNumber(hdl.toNumber()*+1.0);
        }
        else
        {
            throw new BozTrap("PosNonNumberTrap","Error: Positive on non-number scalar.");
        }//end if

    }//end pos

    public final static IHandle neg(final IHandle hdl)
    {
        if(hdl._isNumber())
        {
            return new BozNumber(hdl.toNumber()*-1.0);
        }
        else
        {
            throw new BozTrap("NegNonNumberTrap","Error: Negation on non-number scalar.");
        }//end if

    }//end pos

    public final static IHandle sub(final IHandle lval, final IHandle rval)
    {
        if(lval._isNumber() && rval._isNumber())
        {
             return new BozNumber(lval.toNumber()-rval.toNumber());
        }
        else
        {
            throw new BozTrap("SubNonNumberTrap","Error: Subtraction on non-number scalar.");
        }//end if

    }//end sub

    public final static IHandle pow(final IHandle lval, final IHandle rval)
    {
        if(lval._isNumber() && rval._isNumber())
        {
             return new BozNumber(Math.pow(lval.toNumber(),rval.toNumber()));
        }
        else
        {
            throw new BozTrap("PowNonNumberTrap","Error: Power on non-number scalar.");
        }//end if

    }//end pow

    public final static IHandle rsh(final IHandle lval, final IHandle rval)
    {
        if(lval._isNumber() && rval._isNumber())
        {
             long longL = lval.toNumber().longValue();
             long longR = rval.toNumber().longValue();
             long result = longL >> longR;
             return new BozNumber(result);
        }
        else
        {
            throw new BozTrap("RshNonNumberTrap","Error: Left shift on non-number scalar.");
        }//end if

    }//end lsh

    public final static IHandle lsh(final IHandle lval, final IHandle rval)
    {
        if(lval._isNumber() && rval._isNumber())
        {
             long longL = lval.toNumber().longValue();
             long longR = rval.toNumber().longValue();
             long result = longL << longR;
             return new BozNumber(result);
        }
        else
        {
            throw new BozTrap("LshNonNumberTrap","Error: Right shift on non-number scalar.");
        }//end if

    }//end rsh

    public final static IHandle mul(final IHandle lval, final IHandle rval)
    {
        if(lval._isNumber() && rval._isNumber())
        {
             return new BozNumber(lval.toNumber()*rval.toNumber());
        }
        else
        {
            throw new BozTrap("MulNonNumberTrap","Error: Multiply on non-number scalar.");
        }//end if

    }//end mul

    public final static IHandle div(final IHandle lval, final IHandle rval)
    {
        if(lval._isNumber() && rval._isNumber())
        {
             return new BozNumber(lval.toNumber()/rval.toNumber());
        }
        else
        {
            throw new BozTrap("DivNonNumberTrap","Error: Division on non-number scalar.");
        }//end if

    }//end div

    public final static IHandle mod(final IHandle lval, final IHandle rval)
    {
        if(lval._isNumber() && rval._isNumber())
        {
             return new BozNumber(lval.toNumber()%rval.toNumber());
        }
        else
        {
            throw new BozTrap("ModNonNumberTrap","Error: Modulus on non-number scalar.");
        }//end if

    }//end mod

    public final static IHandle addNum(final IHandle lval, final IHandle rval)
    {
        return new BozNumber(lval.toNumber()+rval.toNumber());
    }//end add

    public final static IHandle addStr(final IHandle lval, final IHandle rval)
    {
        return new BozString(lval.toString()+rval.toString());
    }//end cat

    public final static IHandle add(final IHandle lval, final IHandle rval)
    {
        if(lval._isNumber() && rval._isNumber())
        {
             return RT.addNum(lval,rval);
        }
        else
        if(lval._isString() && rval._isString())
        {
            return RT.addStr(lval,rval);
        }
        else
        {
            throw new BozTrap("AddNonNumberTrap","Error: Addition on mixed scalars.");
        }//end if

    }//end add

    public final static IHandle xor(final IHandle lval, final IHandle rval)
    {
        //boolean xor
        if(lval._isBool() && rval._isBool())
        {
            boolean lbool = RT.boolExpr(lval);
            boolean rbool = RT.boolExpr(rval);

            return RT.bool(lbool ^ rbool);

        }
        else //bitwise xor
        if(lval._isNumber() && rval._isNumber())
        {
           long longL = lval.toNumber().longValue();
           long longR = rval.toNumber().longValue();

           return new BozNumber(longL ^ longR);
        }
        else
        {
            throw new BozTrap("XorMixedScalarTrap","Error: Logical-bitwise and mixed scalars.");

        }//end if

    }//end xor

    public final static IHandle and(final IHandle lval, final IHandle rval) //&&
    {
        boolean lbool = RT.boolExpr(lval);
        boolean rbool = RT.boolExpr(rval);

        return RT.bool(lbool && rbool);

    }//end and

    public final static IHandle ior(final IHandle lval, final IHandle rval) //||
    {
        boolean lbool = RT.boolExpr(lval);
        boolean rbool = RT.boolExpr(rval);

        return RT.bool(lbool || rbool);

    }//end ior

    public final static IHandle andb(final IHandle lval, final IHandle rval) //&
    {
        //boolean and
        if(lval._isBool() && rval._isBool())
        {
            boolean lbool = RT.boolExpr(lval);
            boolean rbool = RT.boolExpr(rval);

            return RT.bool(lbool & rbool);

        }
        else //bitwise and
        if(lval._isNumber() && rval._isNumber())
        {
           long longL = lval.toNumber().longValue();
           long longR = rval.toNumber().longValue();

           return new BozNumber(longL & longR);
        }
        else
        {
            throw new BozTrap("AndMixedScalarTrap","Error: Logical-bitwise and mixed scalars.");

        }//end if

    }//end andb

    public final static IHandle iorb(final IHandle lval, final IHandle rval) //|
    {
        //boolean or
        if(lval._isBool() && rval._isBool())
        {
            boolean lbool = RT.boolExpr(lval);
            boolean rbool = RT.boolExpr(rval);

            return RT.bool(lbool | rbool);

        }
        else //bitwise and
        if(lval._isNumber() && rval._isNumber())
        {
           long longL = lval.toNumber().longValue();
           long longR = rval.toNumber().longValue();

           return new BozNumber(longL | longR);
        }
        else
        {
            throw new BozTrap("IorMixedScalarTrap","Error: Logical-bitwise ior mixed scalars.");
        }//end if

    }//end iorb

    /* convert handles to 1-dimension array of handles from 0 to 16-handles */
    public final static IHandle[] box()
    {
        return new IHandle[]{};
    }//end box

    public final static IHandle[] box(final IHandle h0)
    {
        return new IHandle[]{ h0 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1)
    {
        return new IHandle[]{ h0, h1 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2)
    {
        return new IHandle[]{ h0, h1, h2 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3)
    {
        return new IHandle[]{ h0, h1, h2, h3 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5, final IHandle h6)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5, h6 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5, final IHandle h6, final IHandle h7)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5, h6, h7 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5, final IHandle h6, final IHandle h7, final IHandle h8)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5, h6, h7, h8 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5, final IHandle h6, final IHandle h7, final IHandle h8, final IHandle h9)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5, h6, h7, h8, h9 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5, final IHandle h6, final IHandle h7, final IHandle h8, final IHandle h9, final IHandle h10)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5, final IHandle h6, final IHandle h7, final IHandle h8, final IHandle h9, final IHandle h10,final IHandle h11)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5, final IHandle h6, final IHandle h7, final IHandle h8, final IHandle h9, final IHandle h10,final IHandle h11, final IHandle h12)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5, final IHandle h6, final IHandle h7, final IHandle h8, final IHandle h9, final IHandle h10,final IHandle h11,final IHandle h12,final IHandle h13)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12, h13 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5, final IHandle h6, final IHandle h7, final IHandle h8, final IHandle h9, final IHandle h10,final IHandle h11, final IHandle h12,final IHandle h13, final IHandle h14)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12, h13, h14 };
    }//end box

    public final static IHandle[] box(final IHandle h0, final IHandle h1, final IHandle h2, final IHandle h3, final IHandle h4, final IHandle h5, final IHandle h6, final IHandle h7, final IHandle h8, final IHandle h9, final IHandle h10,final IHandle h11,final IHandle h12,final IHandle h13,final IHandle h14,final IHandle h15)
    {
        return new IHandle[]{ h0, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12, h13, h14, h15 };
    }//end box

}//end class RT
