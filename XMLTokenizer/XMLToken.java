/**
 * Title:        XMLTokenizer Project
 * Description:  Object representing the returned parser entity for XML tag.
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

public final class XMLToken
{
    //elements of the 2-tuple token
    public XMLTokenType tokenType  = null;
    public String       tokenValue = null;

    //constructors for building
    /**
     * Construct an empty XMLToken object
     * with defaults of XMLTokenType.NULL for
     * token type and token text.
     */
    public XMLToken()
    {
        //null constructor so set NULL values
        tokenType  = XMLTokenType.NULL;
        tokenValue = XMLTokenType.NULL.toString();
    }//end XMLToken()

    /**
     * Construct an XMLToken with the string as
     * token text, and the appropriate token type
     * passed to construct the token.
     */
    public XMLToken(XMLTokenType xt, String st)
    {
        tokenType = xt;
        tokenValue = st;
    }//end XMLToken()

    //add Strin getTokenType() ??

   /**
     * Return the text of a XMLToken
     * @return String object representing
     * the XML token text
     */
    public String getTokenText()
    {

        return this.tokenValue;
    }//end getTokenText

    /**
     * Return the XMLToken in a 2-tuple
     * form of < type, text>
     * @return String object representing
     * the XML token
     */
    public final String asTuple()
    {
        if(tokenType == XMLTokenType.NULL)
            return "<NULL>";
        else
        if(tokenType == XMLTokenType.START_TAG)
            return "<START_TAG, "+tokenValue+">";
        else
        if(tokenType == XMLTokenType.CLOSE_TAG)
            return "<CLOSE_TAG, "+tokenValue+">";
        else
        if(tokenType == XMLTokenType.EMPTY_TAG)
            return "<EMPTY_TAG, "+tokenValue+">";
        else
        if(tokenType == XMLTokenType.COMMENT)
            return "<COMMENT, "+tokenValue+">";
        else
        if(tokenType == XMLTokenType.PI)
            return "<PI, "+tokenValue+">";
        else
        if(tokenType == XMLTokenType.CDATA)
            return "<CDATA, "+tokenValue+">";
        else
        if(tokenType == XMLTokenType.TEXT)

            return "<TEXT, "+tokenValue+">";
        else
        if(tokenType == XMLTokenType.ATTR_NAME)
            return "<ATTR_NAME, "+tokenValue+">";
        else
        if(tokenType == XMLTokenType.ATTR_VALUE)
            return "<ATTR_VALUE, "+tokenValue+">";
        //else
        //if(tokenType == XMLTokenType.ENTITY)
        //    return "<ENTITY, "+tokenValue+">";
        else
        if(tokenType == XMLTokenType.DTDELEMENT)
            return "<DTDELEMENT, "+tokenValue+">";
        else
            return "null";

    }//end asTuple

    /**
     * Generate the XMLToken in the
     * appropriate XML format such as
     * </END> <START> <EMPTY/> <? PI ?>
     * and so forth.
     * @return A string object representing
     * the XMLToken in textual form
     */
    public final String toString()
    {
        if(tokenType == XMLTokenType.NULL)
            return "<NULL>";
        else
        if(tokenType == XMLTokenType.START_TAG)
            return "<"+tokenValue+">";
        else
        if(tokenType == XMLTokenType.CLOSE_TAG)
            return "</"+tokenValue+">";
        else
        if(tokenType == XMLTokenType.EMPTY_TAG)
            return "<"+tokenValue+"/>";
        else
        if(tokenType == XMLTokenType.COMMENT)
            return "<!--"+tokenValue+"-->";
        else
        if(tokenType == XMLTokenType.PI)
            return "<?"+tokenValue+"?>";
        else
        if(tokenType == XMLTokenType.CDATA)
            return "<![CDATA["+tokenValue+"]]>";
        else
        if(tokenType == XMLTokenType.TEXT)
            return tokenValue;
        else
        if(tokenType == XMLTokenType.ATTR_NAME)
            return tokenValue;
        else
        if(tokenType == XMLTokenType.ATTR_VALUE)
            return "='"+tokenValue+"'";
        //else
        //if(tokenType == XMLTokenType.ENTITY)
        //    return "&"+tokenValue+";";
        else
        if(tokenType == XMLTokenType.DTDELEMENT)
            return "<!"+tokenValue+">";
        else
            return "null";

    }//end toString

}//end class XMLToken
