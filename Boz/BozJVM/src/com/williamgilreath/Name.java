/**
 * <p>Title:       Name - Dynamic Individual Strings </p>
 * <p>Description: Dynamic Sequence of Dotted String </p>
 * <p>Copyright:   Copyright (c) May 20 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.0
 */

//add hashCode as 32-bit int fnv hash, and equals?? Jul 28 2008

package com.williamgilreath;

public final class Name extends Object implements Cloneable, Comparable
{
    private final static int DELTA = 16;

    private String[] element = null;
    private int count = -1;

    public Name()
    {
        this.element = new String[DELTA];
        this.count = 0;
    } //end Name

    public final int compareTo(final Name name)
    {
        return this.toString().compareTo(name.toString());
    }//end compareTo

    public final int compareTo(final Object obj)
    {
        int result =  this.compareTo((Name)obj);
        //System.out.println("Name.compareTo() = "+result);
        return result;
    }//end compareTo

    public void clear()
    {
        this.removeAllElements();
    } //end clear

    private synchronized void removeAllElements()  //clear
    {
        for (int i = 0; i < count; i++)
        {
            element[i] = null;
        } //end for
        count = 0;
    } //end removeAllElements

    public final Object clone()
    {
        try
        {
            Name clone = (Name)super.clone();
            clone.element = (String[])this.element.clone();
            return clone;
        }
        catch (CloneNotSupportedException ex)
        {
            throw new InternalError(ex.toString());
        } //end try
    } //end clone

    public final Name copy()
    {
        return (Name)this.clone();
    } //end copy

    private final void ensureCapacity(int capacity)
    {
        if (element.length >= capacity)
        {
            return;
        } //end if

        int newCapacity = element.length * 2; //multiple, 16,32,64,128
        String[] newArray = new String[Math.max(newCapacity, capacity)];

        System.arraycopy(element, 0, newArray, 0, count);
        element = newArray;
    } //end ensureCapacity

    public final int size()
    {
        return count;
    } //end size

    public final boolean isEmpty()
    {
        return (count == 0);
    } //end isEmpty

    public final boolean has(String str)
    {
        boolean result = false;

        for(int x=0;x<this.count;x++)
        {
            if(this.element[x].equals(str))
            {
                return true;
            }//end if
        }//end for

        return result;
    }//end has

    public final String get(int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        } //end if

        return element[index];
    } //end get

    public final String head()
    {
        return this.get(0);
    } //end head

    public final String tail()
    {
        return this.get(count - 1);
    } //end tail

    public final String last(int index) //0= count-1, 1=count-2, 2=count-3
    {
        return this.get(count - 1 - index);
    } //end last

    public final void add(int index, String str)
    {
        if (index > count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " > " + count);
        } //end if

        if (count == element.length)
        {
            ensureCapacity(count + 1);
        } //end if

        System.arraycopy(element, index, element, index + 1, count - index);
        count++;
        element[index] = str;
    } //end add

    public final void push(String str)
    {
        this.add(0, str);
    }//end push

    public final void add(String str)
    {
        if (count == element.length)
        {
            ensureCapacity(count + 1);
        } //end if

        element[count++] = str;
    } //end add

    public final void append(String[] str)//append array of strings
    {
        for(int x=0;x<str.length;x++)
        {
            this.add(str[x]); //add at 0=>size-1, 1=>size, 2=>size+1, ... ,n=>size+n-1, n+1=>size+n
        }//end for

    }//end append

    public final void prepend(String[] str)
    {
        for(int x=0;x<str.length;x++)
        {
            this.add(x, str[x]); //add at 0=>0, 1=>1, 2=>2, ... n-1=>n-1, n=>n
        }//end for
    }//end prepend

    public final String[] toArray()
    {
        String[] newArray = new String[count];
        System.arraycopy(element, 0, newArray, 0, count);
        return newArray;
    } //end toArray

    public final String pop()
    {
        return this.remove(0);
    }//end pop

    public final String remove(int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        } //end if

        String temp = (String) element[index];

        count--;
        if (index < count)
        {
            System.arraycopy(element, index + 1, element, index, count - index);
        } //end if

        element[count] = null;
        return temp;
    } //end remove

    public final String toString()
    {
        StringBuffer str = new StringBuffer();

        for(int x=0;x<this.count-1;x++)
        {
            str.append(this.element[x]);
            str.append(".");
        } //end for

        str.append(this.element[this.count-1]);
        return str.toString();

    } //end toString

    public final boolean hasPrefix()
    {
    	return this.count > 1;
    }//end hasPrefix

    public final String prefix() //A.B.C.D => A.B.C
    {
        if(this.count < 2) return "";

        StringBuffer str = new StringBuffer();
        
        str.append(this.element[0]);
        for(int x=1;x<this.count-1;x++)
        {
        	str.append(".");
            str.append(this.element[x]);
           
        } //end for

        //str.append(this.element[this.count-1]);
        return str.toString();
    	
    }//end prefix
    
    public final String suffix() //A.B.C.D => D
    {
    	//System.out.println("Elements.length = "+this.count);
    	return this.element[this.count-1];
    }//end suffix

    public final static void main(String[] args)
    {
        /*
        Name sv = new Name();

        sv.add("0");
        sv.add("1");

        System.out.println("Name = "+sv+" "+sv.size());

        String[] append = new String[]{ "a","b","c"  };

        sv.append(append);

        System.out.println("Name = "+sv+" "+sv.size());

        sv.prepend(append);

        System.out.println("Name = "+sv+" "+sv.size());

        sv.push("push");
        System.out.println("Name = "+sv+" "+sv.size());

        sv.pop();
        System.out.println("Name = "+sv+" "+sv.size());

        System.out.println("Name.has(\"a\") = "+sv.has("a"));

        System.out.println("Name.has(\"z\") = "+sv.has("z"));
        //*/
        System.out.println();
        
        Name name = new Name();
        
        name.add("A");
        name.add("B");
        name.add("C");
        name.add("D");
        
        System.out.println("Name = "+name);
        
        System.out.println("hasPrefix = "+name.hasPrefix());
        System.out.println("prefix    = "+name.prefix());
        System.out.println("suffix    = "+name.suffix());
        System.out.println();
        
        name.clear();

        name.add("theName");
        
        System.out.println("Name = "+name);
        
        System.out.println("hasPrefix = "+name.hasPrefix());
        System.out.println("prefix    = "+name.prefix());
        System.out.println("suffix    = "+name.suffix());
        System.out.println();

        System.exit(0);
    }//end main

} //end class Name
