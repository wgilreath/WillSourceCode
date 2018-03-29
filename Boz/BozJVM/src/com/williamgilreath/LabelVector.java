package com.williamgilreath;

import java.text.DecimalFormat;

/*
 * @(#) LabelVector    Version 1.00   06:25:2010:23:31
 *
 * Title: LabelVector.java
 *
 * Description: Dynamic vector of labels used in compiler.
 *
 * Author: William F. Gilreath (will@williamgilreath.com)
 *
 * Copyright (c) Jun 25, 2010.  All Rights Reserved.
 *
 * License: This software is subject to the terms of the
 * GNU General Public License  (GPL)  available  at  the
 * following link: http://www.gnu.org/copyleft/gpl.html.
 *
 * You	must accept the terms of the GNU General  Public
 * License license  agreement to use this software.
 *
 */

public final class LabelVector 
{
    private int labelCounter = -1;
    private final DecimalFormat fmt = new DecimalFormat("0000");

    private final static int DELTA = 4;


    private Integer[] dataInt;
    private Boolean[] dataBoolCut;
    private Boolean[] dataBoolEnd;

    private int      count;

    public LabelVector()
    {
        this.dataInt      = new Integer[DELTA];
        this.dataBoolCut  = new Boolean[DELTA];
        this.dataBoolEnd  = new Boolean[DELTA];
        this.count        = 0;
        this.labelCounter = 0;

    }//end ObjectVector

    public final void clear()
    {

        for (int i = 0; i < count; i++)
        {
            dataInt[i] = null;
            dataBoolCut[i]= null;
            dataBoolEnd[i]= null;
        }//end for

        count = 0;

    }//end clear

    private final void checkCapacity(final int capacity)
    {
        if (dataInt.length >= capacity)
        {
            return;
        }//end if

        final int newCapacity = dataInt.length + DELTA;

        Integer[] newArrayInt = new Integer[newCapacity];

        System.arraycopy(dataInt, 0, newArrayInt, 0, count);
        dataInt = newArrayInt;

        Boolean[] newArrayBool = new Boolean[newCapacity];

        System.arraycopy(dataBoolCut, 0, newArrayBool, 0, count);
        dataBoolCut = newArrayBool;

        newArrayBool = new Boolean[newCapacity];

        System.arraycopy(dataBoolEnd, 0, newArrayBool, 0, count);
        dataBoolEnd = newArrayBool;

    }//end checkCapacity

    public final int size()
    {
        return count;

    }//end size

    public final boolean isEmpty()
    {
        return (count == 0);

    }//end isEmpty

    private final void insert(final int index, final Integer objInt, final Boolean objBool)
    {
        if (index > count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " > " + count);
        }//end if

        if (count == dataInt.length)
        {
            checkCapacity(count + 1);
        }//end if

        System.arraycopy(dataInt, index, dataInt, index + 1, count - index);
        dataInt[index] = objInt;

        System.arraycopy(dataBoolCut, index, dataBoolCut, index + 1, count - index);
        dataBoolCut[index] = objBool;

        System.arraycopy(dataBoolEnd, index, dataBoolEnd, index + 1, count - index);
        dataBoolEnd[index] = objBool;

        count++;

    }//end insert

    private final void remove(final int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        count--;

        if (index < count)
        {
            System.arraycopy(dataInt,     index + 1, dataInt,     index, count - index);
            System.arraycopy(dataBoolCut, index + 1, dataBoolCut, index, count - index);
            System.arraycopy(dataBoolEnd, index + 1, dataBoolEnd, index, count - index);
        }//end if

    }//end remove

    public final String toString()
    {
        StringBuffer str = new StringBuffer();

        str.append("[ ");
        for(int x=0;x<this.count;x++)
        {
            if(dataInt[x] != null)
            {
                str.append("<( ");
                str.append(dataInt[x].toString());
                str.append("=");
                str.append(dataBoolCut[x].toString());
                str.append(":");
                str.append(dataBoolEnd[x].toString());
                str.append(" )>");
                str.append(" ");
            }//end if

        }//end for

        str.append("]");
        return str.toString();

    }//end toString


    //label vector functionality
    public final void start()
    {
        this.insert(0, new Integer(this.labelCounter++), Boolean.FALSE);    
    }//end start

    public final void close()
    {
        this.remove(0);
    }//end close

    /*
    public final String getLabel() //Java only needs single label
    {
        if(this.dataBoolCut[0] || this.dataBoolEnd[0])
        {
            System.out.println("LabelVector getLabel: true");
            return "label_"+fmt.format(this.dataInt[0]).toString()+" ";
        }
        else
        {
            System.out.println("LabelVector getLabel: false");
            return "";
        }//end if

    }//end getLabel
    //*/
    
    public final String getLabelLoop() //Java only needs single label
    {
        if(this.dataBoolCut[0] || this.dataBoolEnd[0])
        {
            //System.out.println("LabelVector getLabel: true");
            return "label_"+fmt.format(this.dataInt[0]).toString()+": ";
        }
        else
        {
            //System.out.println("LabelVector getLabel: false");
            return "";
        }//end if

    }//end getLabelLoop

    public final String getLabelCut() //C# needs cut separately
    {
        if(this.dataBoolCut[0])
        {
            return "label_"+fmt.format(this.dataInt[0]).toString()+" ";
        }
        else
        {
            return "";
        }//end if

    }//end getLabelCut

    public final String getLabelEnd() //C# needs end separately
    {
        if(this.dataBoolEnd[0])
        {
            return "label_"+fmt.format(this.dataInt[0]).toString()+" ";
        }
        else
        {
            return "";
        }//end if

    }//end getLabelEnd

    public final void cut(final int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        this.dataBoolCut[index] = Boolean.TRUE;

        //System.out.println("LabelVector cut "+index+" is true");

    }//end cut

    public final void end(final int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        this.dataBoolEnd[index] = Boolean.TRUE;

    }//end end

    /*
    public final static void main(String[] args)
    {
        final LabelVector lv = new LabelVector();

        lv.start();
        lv.start();
        lv.start();
        lv.start();
        lv.start();

        lv.cut(0);
        lv.end(1);

        System.out.println();
        System.out.println("Label Vector: "+lv.toString());
        System.out.println();

        System.out.println("Label    : "+lv.getLabel());
        System.out.println("Label cut: "+lv.getLabelCut());
        System.out.println("Label end: "+lv.getLabelEnd());
        System.out.println();

        lv.close();

        System.out.println();
        System.out.println("Label Vector: "+lv.toString());
        System.out.println();

        System.out.println("Label    : "+lv.getLabel());
        System.out.println("Label cut: "+lv.getLabelCut());
        System.out.println("Label end: "+lv.getLabelEnd());
        System.out.println();
        lv.close();

        lv.start();
        lv.cut(0);

        lv.start();
        lv.end(0);

        System.out.println();
        System.out.println("Label Vector: "+lv.toString());
        System.out.println();
        
        System.out.println("Label    : "+lv.getLabel());
        System.out.println("Label cut: "+lv.getLabelCut());
        System.out.println("Label end: "+lv.getLabelEnd());
        System.out.println();

        lv.close();
        lv.close();
        lv.close();

        System.out.println();
        System.out.println("Label Vector: "+lv.toString());
        System.out.println();

        System.out.println("Label    : "+lv.getLabel());
        System.out.println("Label cut: "+lv.getLabelCut());
        System.out.println("Label end: "+lv.getLabelEnd());
        System.out.println();
        
        System.exit(0);
        
    }//end main
    //*/

}//end class LabelVector
