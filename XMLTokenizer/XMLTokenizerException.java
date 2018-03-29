/**
 * Title:        XMLTokenizer Project
 * Description:  Exception object thrown by XMLTok parser.
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

public final class XMLTokenizerException extends RuntimeException
{
    XMLTokenizerException(String exceptionMessage)
    {
        super(exceptionMessage);

    }//end XMLTokenizerException

}//end class XMLTokenizerException