/**
 * Title:        XMLTokenizer Project
 * Description:  Class for type enumeration of different XML token types.
 * Copyright:    Copyright (C) 2002
 * @author       William F. Gilreath
 * @version      1.0
 *
 * This file is part of XML Tokenizer software project. XML Tokenizer is
 * free software; you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
package com.williamgilreath.xml;

public final class XMLTokenType
{
    //class attributes for enumType

    private static int          typeLength = -1;
    private static String[]     typeTable  = null;

    private int                 typeValue  = -1;
    private static boolean      typeReady  = false;

/*
    private XMLTokenType()
    {
        typeReady = false;
        typeValue = -1;

    }//end enumType()
//*/

    private XMLTokenType(int i)
    {
        if(typeReady)
        {
            typeValue = i;
        }
        else
        {
            typeValue = -1;

        }//end if
    }//end XMLTokenType

    //define the new constructors for the type

    private XMLTokenType()
    {
        initialize(new String[]
              {
                    "start_tag",
                    "close_tag",
                    "empty_tag",
                    "comment",
                    "cdata",
                    "pi",
                    "attribute_name",
                    "attribute_value",
                    "text",
                    //"entity",
                    "DTDelement"
            }
        );
    }//end XMLTokenType

    //constructors for defining enumerated type
    private void initialize(String[] s)
    {
        typeLength = s.length;
        typeTable  = new String[typeLength];

        for(int x=0;x < typeLength;x++)
        {
            typeTable[x] = s[x];

        }//end for

        typeReady = true;

    }//end initialize

    /**
     * Method to get the number of discrete value in the type definition.
     *
     */
    public static int getLength() //throws undefinedTypeException
    {
        //if type is not ready, throw exception
        if (!typeReady)
        {
            //throw undefinedTypeException;
            //throw new undefinedTypeException("length: undefinedTypeException");
            return -1;

        }//end if

        return typeLength;

    }//end getLength

    //value related methods
   /**
     * Method to get the integer value of the referenced type.
     *
     */
    public int valueOf()
    {
        return typeValue;

    }//end valueOf

   /**
     * Method to get the string representation of the referenced type.
     *
     */
    public String toString()
    {
        if(typeValue == -1)
        {
            return "null";
        }
        else
        {
            return(typeTable[typeValue]);

        }//end if

    }//end toString

    //define NULL and construct the type
    public static final XMLTokenType NULL       = new XMLTokenType();

    //define the constant objects
    public static final XMLTokenType START_TAG  = new XMLTokenType(1);
    public static final XMLTokenType CLOSE_TAG  = new XMLTokenType(2);
    public static final XMLTokenType EMPTY_TAG  = new XMLTokenType(3);
    public static final XMLTokenType COMMENT    = new XMLTokenType(4);
    public static final XMLTokenType CDATA      = new XMLTokenType(5);
    public static final XMLTokenType PI         = new XMLTokenType(6);
    public static final XMLTokenType ATTR_NAME  = new XMLTokenType(7);
    public static final XMLTokenType ATTR_VALUE = new XMLTokenType(8);
    public static final XMLTokenType TEXT       = new XMLTokenType(9);
    public static final XMLTokenType DTDELEMENT = new XMLTokenType(10);

    //define integer constants
    public static final int NULL_CONSTANT       = 0; //NULL.valueOf();
    public static final int START_TAG_CONSTANT  = 1; //= START_TAG.valueOf();
    public static final int CLOSE_TAG_CONSTANT  = 2; //= CLOSE_TAG.valueOf();
    public static final int EMPTY_TAG_CONSTANT  = 3; //= EMPTY_TAG.valueOf();
    public static final int COMMENT_CONSTANT    = 4; //= COMMENT.valueOf();
    public static final int CDATA_CONSTANT      = 5; //= CDATA.valueOf();
    public static final int PI_CONSTANT         = 6; //= PI.valueOf();
    public static final int ATTR_NAME_CONSTANT  = 7; //= ATTR_NAME.valueOf();
    public static final int ATTR_VALUE_CONSTANT = 8; //= ATTR_VALUE.valueOf();
    public static final int TEXT_CONSTANT       = 9; //= TEXT.valueOf();
    public static final int DTDELEMENT_CONSTANT = 10; //= DTDELEMENT.valueOf();

}//end XMLTokenType
