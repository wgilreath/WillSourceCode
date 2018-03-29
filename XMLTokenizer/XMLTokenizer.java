/**
 *
 * Title:        XMLTokenizer - XML Tokenizer parser class.
 * Description:  Create a tokenizer to determine and return XML tokens from an input data stream.
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

import java.io.*;
import java.util.Vector;

public final class XMLTokenizer 
{
    //input data stream attribute
    private BufferedReader    in = null;
    private PrintWriter      err = null;

    //next symbol, symbol buffer, and count of symbols
    private int symbol           =  0;
    private int buffer           = -1;
    private int symbol_count     =  0;
    private int line_count       =  1;

    //text for token buffer
    private StringBuffer text          = new StringBuffer();

    //XMLToken being processed information
    private XMLToken     token         = null;
    private XMLTokenType token_type    = null;

    //queue for tokens generated
    private Vector       queue         = null;

    //boolean flags for enabling verbose errors and throwing exceptions
    private boolean verboseErrorFlag   = false;
    private boolean throwExceptionFlag = false;

    //constant for limit on lookahead bytes
    private static final int LOOKAHEAD_LIMIT = 4096;

    //helper method which setups up err, queue, throwException, encoding,
    /**
     * Helper method used by the constructors
     * @param reader is the input stream reader where the raw symbols read from
     * @param os is the output stream where the tokenizer messages sent
     */
    private final void initialize(Reader reader, OutputStream os)
    {
        clear();
        in    = new BufferedReader(reader); //provided InputStreamReader object with inputSream
        err   = new PrintWriter(os);        //not autoflush err stream, needs flush
        queue = new Vector();

        if(in.markSupported() == false)
        {
            throw new RuntimeException("Marking input stream not supported. Can not tokenize");

        }//end if

    }//end initialize


    private final void mark()
    {
          //System.out.println("DEBUG: mark()");
          try
          {
              in.mark(2*this.LOOKAHEAD_LIMIT+1);
              //return true;
          }
          catch(Exception e)
          {
              //return false;
              System.out.println("mark() Error!");
          }//end try

    }//end mark()


    private final void reset()
    {
          //System.out.println("DEBUG: reset()");
          try
          {
              in.reset();
          }
          catch(Exception e)
          {
             System.out.println("reset() Error!");
          }//end try

    }//end reset

    /**
      * constructor to take any Reader object
      * @param reader is the input stream reader where the raw symbols read from
      * @param os is the output stream where the tokenizer messages sent
      */
    public XMLTokenizer(Reader reader, OutputStream os)
    {
          initialize(reader, os);

    }//end XMLScanner

    /**
      * master constructor takes input stream, output stream,
      * @param is the input stream to read raw token characters/symbols
      * @param os the output stream to report tokenizer messages
      */
    public XMLTokenizer(InputStream is, OutputStream os)
    {
        InputStreamReader isr = new InputStreamReader(is); //use default character encoding
        initialize(isr, os);

    }//end XMLScanner

    //unget symbol only; method only called in attributeName; directly access buffer?
    /**
     * Method returns a symbol putting it in a buffer to be re-read by getSymbol
     */
    private final void ungetSymbol()
    {
       buffer = symbol;

    }//end ungetSymbol

    /**
      * Method sets the error verbosity of the tokenizer.
      * @param flag setting if verbosity on or off
      */
    public final void setVerbose(boolean flag)
    {
        verboseErrorFlag = flag;

    }//end setVerbose

    //configure enabling exceptions to be thrown
    /**
      * Method sets if to throw an exception on errors
      * or report the error and exit with error code.
      * @param flag setting if exceptions thrown or not
      */
    public final void setThrowsException(boolean flag)
    {
	    throwExceptionFlag = flag;

    }//end setThrowsException


    /**
     * Method clears or resets the tokenizer; token type, token symbol
     * and the token text are all cleared.
     */
    private final void clear()
    {
        token      = null;
        token_type = null;
        symbol     = 0;//? reset each time, buffer holds symbol if ungetsymbol

        //delete all characters, clearing string buffer
        text.delete(0, text.length());
    }//end clear

    //utility method to determine if input character
    //is separator/delimiter
    /**
     * Query method to determine if a symbol as an integer is a
     * separator symbol in XML
     */
    private final boolean isSeparator(int i)
    {
        if(Character.isWhitespace((char)i)
        || Character.isSpaceChar( (char)i))
        {
            return true;
        }
        else
        {
            return false;
        }//end if

    }//end isSeparator

    //utility method to move past all separator characters
    /**
     * Method skips over separator characters/symbols in the
     * input stream; separator symbols are tabs, newlines,
     * carriage returns, and other whitespaces.
     */
    private final void skipSeparator()
    {
        while(isSeparator(symbol))
        {
           getSymbol();
        }//end while

    }//end skipSeparator

    //utility method to get next character from input stream
    /**
     * Method used to get the next symbol as an integer (0..655350)
     * from the specified input stream.
     */
    private final int getSymbol()
    {
        try
        {
            if(buffer == -1)
            {
                symbol = in.read();

                symbol_count++;

                //normalize end of line characters
                //per XML specification part 2.11
                if(symbol == 13)//CR
                {
                    line_count++;
                    symbol_count = 0;
                    //symbol = in.read();


                    if(symbol != 10)//LF
                    {
                       buffer = symbol;
                    }//end if

                    symbol = 10;
                }//end if
            }
            else
            {
                //reset unget symbol flag
                symbol = buffer;

                buffer = -1;
            }//end if
        }
        catch(Exception e)
        {
            symbol = -1;
        }//end try

        //System.out.println("DEBUG getSymbol() symbol = "+symbol+"; $"+(char)symbol);
        return symbol;

    }//end getSymbol

    //utility method which creates token object
    //and enques the token
    /**
     * Utility method that creates a new token object
     */
    private final void makeToken()
    {
        //check if the text is empty
        if(text.length() == 0)
        {
            //tokenFlag = false;
            return;

        }//end if

        token = new XMLToken(token_type, text.toString());

        //enque the new token
        queue.add(token);


    }//end makeToken

    /**
      * Method that gets next token; the "main" of the class
      * @return XMLToken object of token type and token text
      */
    public final XMLToken getNextToken()
    {
        //check if the queue is empty
        if(queue.size() == 0)
        {
            getToken();
            if(queue.size() == 0)
            {
                return null;

            }//end if
        }//end if

        return (XMLToken) queue.remove(0);

    }//end getNextToken

    /**
      * get all tokens
      * @return Vector of XMLTokens (token_type, token_text)
      */
    public final Vector getAllTokens() //throws uncheck XMLScannerException
    {
        //create queue or buffer of tokens
        //queue used by scanner is for tokens
        //which are dequeued by getNextToken
        Vector tokens  = new Vector();

        try
        {
              while(hasNextToken())
              {
                  tokens.add(getNextToken());
              }//end while

        }
        catch(OutOfMemoryError oe)
        {
            tokens = null;
            error(23); //will rethrow as XMLTokenizerException or report and exit
        }
        finally			//add generic catch(Exception) ? report as unexpected error ?
        {
            return tokens;
        }

    }//end getAllTokens


    //method that processes next token
    private final void getToken()
    {
        clear(); //reset token, token_type, string, symbol
        getSymbol();
        tag();

    }//end getToken

    /** Query method to see if there are any more tokens in
      *  the input queue, or available from the input stream
      * @return Boolean indicating if there is another token
      */
    public final boolean hasNextToken()
    {
        if(queue.size() == 0)
        {
            getToken();

            if(queue.size() == 0)
            {
                return false;
            }
            else
            {
                return true;

            }//end if
        }
        else
        {
            return true;

        }//end if

    }//hasNextToken

    //methods related to XML tokens and tags
    //tag acts as "main" method for processing XML tag/token
    /**
     * Method that begins processing input symbols to identify
     * the next token from the input stream
     */
    private final void tag()
    {
        //System.out.println("DEBUG: tag()");
        //System.out.println("DEBUG: symbol = "+(char)symbol+" "+symbol);
        //check for end of stream
        if(symbol == -1)
        {
            return;

        }//end if

        if(symbol == '<')
        {
            getSymbol(); //get symbol following <

            //call specific xml token routines
            startTag();
        }
        else
        /*
        if(symbol == '&')
        {
            getSymbol();
            entity();
        }
        else //*/
        {
            text();
        }//end if

    }//end tag


    /**
     * Method determines if entity token in symbol stream
     */
    private boolean isEntity()
    {
          boolean isEntityFlag = false;
          //System.out.println("DEBUG: isEntity()");

          //mark the current stream position
          this.mark();

          //look at the next symbol after the & ampersand
          getSymbol();

          if(symbol == '&')
          {
              isEntityFlag = false;
          }
          else
          if(symbol == '#')
          {
              isEntityFlag = true;
          }
          else
          if(this.isSeparator(symbol))
          {
              isEntityFlag = false;
          }
          else
          {
              //lookahead in the inputstream for & or ;
              for(int x=0;x<this.LOOKAHEAD_LIMIT;x++)
              {

                    if(symbol == '&')
                    {
                        isEntityFlag = false;
                        break;
                    }
                    else
                    if(symbol == ';')
                    {
                        isEntityFlag = true;
                        break;
                    }//end if

                    getSymbol(); //get next symbol
              }//end for

          }//end if



          //reset to previous position
          this.reset();

          //System.out.println("DEBUG: isEntity() ="+ isEntityFlag);
          return isEntityFlag;
    }//end isEntity


    /**
     * Method handles the token as a text token
     */

    private final void text()
    {
        //System.out.println("DEBUG: text()");
        //System.out.println("DEBUG: symbol = "+(char)symbol+" "+symbol);

        text.append((char)symbol);
        token_type = XMLTokenType.TEXT;
        //getSymbol();

        /*
        // check symbol is not XML and alphanumeric
        // greater than space and lesser than delete
        if(isSeparator(symbol))
        {
            //only get whitespaces if verbatim, so concat
            //text.append((char)symbol);
            //text.append('$'); //for delimiter, remove in release

            text.append((char)symbol);
            token_type = XMLTokenType.TEXT;

        }
        else if(Character.isLetterOrDigit((char)symbol))
        {
            //text starts with letter
            text.append((char)symbol);
            token_type = XMLTokenType.TEXT;
        }//end if
        //*/

        while(true)
        {
            getSymbol();

            //add code to allow for \n\r\t
            /*
            if(Character.isLetterOrDigit((char)symbol))
            {
                text.append((char)symbol);
            }
            else if(symbol == '_')
            {
                text.append((char)symbol);
            }
            else//*/

            //simply append separator symbols, no breaks?
            if(isSeparator(symbol))
            {

                //append symbol, then make token
                text.append((char)symbol);

                //append $ for delimiter, remove for release
                //text.append('$');

                //change separator to space always?
                //text.append((char)symbol);
                //makeToken();
                //break;
            }
            else if(symbol == '&') //possible entity reference
            {
                //System.out.println("DEBUG text() encountered & symbol");

                //check if it is an entity
                /*
                if(this.isEntity() == true)
                {
                     makeToken(); //make text token
                     clear();
                     getSymbol();
                     entity();
                     break;
                }
                else
                {
                //*/
                     //System.out.println("DEBUG text() appending symbol"+(char)symbol+" "+symbol);
                     text.append('&');
                     //text.append((char)symbol);
                     //break;

               // }//end if

            }
            else if(symbol == -1)
            {
                makeToken();
                break;
            }
            else if(symbol == '<')
            {
                //System.out.println("Debug symbol = "+symbol+" "+(char)symbol);
                //System.out.println("buffer = "+this.buffer);
                //System.out.println("text = "+text);
                makeToken();

                //need to unget symbol and break?
                //can just clear() getSymbol() startTag()
                //like in tag();

                //ungetSymbol(); //re-use symbol for start of next
                clear();
                getSymbol();
                startTag();
                break;
            }
            else
            {
                text.append((char)symbol);
            }//end if

        }//end while

    }//end text


    //handle entity type
    /**
     * Method handles the input as an entity token
     * such as &#169; or &lt;
     */
/*  //no entities
    private final void entity()
    {

        //*
        //check for leading # for numeric entity reference &#169;
        if(symbol == '#')
        {
            text.append((char)symbol);
            getSymbol();
        }
        else //check if spurous & add check up to n-characters look-ahead for ; ??
        if(this.isSeparator(symbol)==true)
        {
            text.append((char)symbol);
            getSymbol();
            return;
        }
        else
        {
            text.append((char)symbol);
            getSymbol();
            //return;
        }//end if

        token_type = XMLTokenType.ENTITY;

        //check that the first symbol of entity is valid
        if(Character.isLetter((char)symbol)
        || Character.isDigit( (char)symbol)
        || (symbol == '_') ||(symbol == ':'))
        {
            text.append((char)symbol);
            getSymbol();
        }
        else
        if(symbol == -1)
        {
            error(17);
        }
        else
        {
           error(7);
        }//end if



        while(symbol != ';')
        {

            //code to check symbol is valid for entity
            if( Character.isLetter((char)symbol)
            ||  Character.isDigit( (char)symbol)
            ||(symbol == '_')||(symbol == '=')
            ||(symbol == ':')||(symbol == '-')||(symbol == '.') )
            {
                text.append((char)symbol);
                getSymbol();
            }
            else
            if(symbol == -1)
            {
                error(17);
                break;
            }
            else
            {
                this.dumpTokenQueue();
                System.err.println("symbol = "+(char)symbol+" "+symbol);
                error(7);
                break;
            }//end if
        }//end while

        //check entity is defined
        if(text.length() == 0)
        {
            error(6); //error that entity must have non-zero text string
            return;
        }
        else
        {
            makeToken();
        }//end if

    }//end entitity

    //*/

    private final void dumpTokenQueue()
    {
        System.out.println("DEBUG: start of dumpTokenQueue()");

        for(int x=0; x<this.queue.size(); x++)
        {
            System.out.print("DEBUG: tokenQueue["+x+"] = ");
            System.out.print( ((XMLToken)this.queue.get(x)).asTuple());
            System.out.println();

        }//end if



        System.out.println("DEBUG: endof of dumpTokenQueue()");
    }//end dumpTokenQueue



    /**
     * Method handles tokenizing one or many
     * XML attributes (name and value)
     */

    private final void attribute()
    {
        while(true)
        {
            attributeName();

            //exited attributeName, determine symbol
            if(symbol == '=')
            {
                clear(); //needed?
                getSymbol();
                attributeValue();
            }//end if


            //exited attributeValue, determine symbol
            //System.out.println("DEBUG: exited AttrValue symbol = "+(char)symbol+" #"+symbol);

            //check for <XYZ VAR="j" <TD>
            if(symbol == '<')
            {
                return;

            }//end if


            //empty tag symbol
            if(symbol == '/')
            {

                //dumpTokenQueue();

                makeEmpty();  //alter start_tag in queue to empty

                getSymbol(); //get symbol after /

		//verify next symbol is >
                if(symbol != '>')
                {
                    dumpTokenQueue();
                    error(8);
                }//end if

                //end of <TAG x='y'/>
                return;

            }//end if

            //start tag <TAG>
            if(symbol == '>')
            {
                return; //change to makeToken to create token?? attributeName and attributeValue create token

            }//end if

            //need? symbol is end of stream not separator
            if(symbol == -1)
            {
                error(17);
            }//end if

            /* //need not be a separator rewrite if it is getSymbol() ?
            //reach here symbol is separator
            if(!isSeparator(symbol))
            {
                this.dumpTokenQueue();
                error(16);
            }//end if
            //*/

            clear();
            getSymbol();
            //System.out.println("DEBUG: symbol after clear() = "+(char)symbol+" #"+symbol);
            //check for <XYZ VAR="j" <TD>
            if(symbol == '<')
            {
                return;

            }//end if

            continue; //continue while have another attribute name

        }//end while

    }//end attribute

    /**
     * Method handles tokenizing an unquoted attribute value attribute value
     */

    private final void nonQuotedAttributeValue()
    {
        token_type = XMLTokenType.ATTR_VALUE;

        do
        {
            text.append((char)symbol);
            getSymbol();
        }
        while(isSeparator(symbol) == false && symbol != '>');

        ungetSymbol();
        makeToken();

    }//end nonQuotedAttributeValue


    /**
     * Method handles tokenizing an attribute value
     */
    private final void attributeValue()
    {
        int quoteChar = -1;

        skipSeparator();

        //check for single quote (39) ' or double quote " (34)
        if(symbol != 34 && symbol != 39)
        {
            //not quote symbol, check for end of stream for better error reporting
            if(symbol == -1)
            {
                error(17);
            }
            else
            {
                //error(10);

                //call method to handle non standard attribute value
                nonQuotedAttributeValue();
                return;
            }//end if
        }
        else
        {
            quoteChar = symbol;

        }//end if

        token_type = XMLTokenType.ATTR_VALUE;

        getSymbol();

        //continue until single ' or double " quote
        //while(symbol != 34 && symbol != 39)
        while(symbol != quoteChar)
        {
            if(symbol == -1)
            {
                error(17);
            }//end if

            text.append((char)symbol);
            getSymbol();
        }//end while

        //make sure start enclosing quote is the same as ending quote
        if(symbol != quoteChar)
        {
            error(12);
        }//end if

        makeToken();
        clear();
        getSymbol(); //get next symbol after quote character
        //System.out.println("Symbol after attribute quote = "+(char)symbol+"  #"+symbol);

    }//end attributeValue

    /**
     * Method handles tokenizing an attribute name
     */
    private final void attributeName()
    {
        token_type = XMLTokenType.ATTR_NAME;

        //skip leading whitespaces
        skipSeparator();

        //check for end of stream before processing further
        if(symbol == -1)
        {
            error(17);
        }//end if

        //continue until end start > empty / or = attributeValue
        while(symbol != '/' && symbol != '>' && symbol != '=' && !this.isSeparator(symbol))
        {
            //append symbol to token text
            text.append((char)symbol);
            getSymbol();

            //check for end of stream
            if(symbol == -1)
            {
                error(17);
            }//end if

        }//end while

        //save the symbol exited on ?eliminate method call save ilocal variable?
        ungetSymbol();

        makeToken(); //create ATTR_NAME token, and return
        clear();     //clear the token_type, token_text

        //restore the symbol in the buffer
        getSymbol();

    }//end attributeName

    // add code to check/handle attributes
    /**
     * Method handles tokenizing a XML start tag
     * such as <START> or <XML>
     */
    private final void startTag()
    {
        //System.out.println("DEBUG: startTag()");
        //System.out.println("DEBUG: symbol = "+(char)symbol+" "+symbol);

        if(symbol == -1)
        {
            error(17);
        }
        else //check if it is letter or digit or underscore
        if(Character.isLetterOrDigit((char)symbol) || (symbol == '_'))
        {
            //concat symbol
            text.append((char)symbol);

            //set token type as START_TAG
            token_type = XMLTokenType.START_TAG;

            getSymbol();

            //change logic while symbol != '>' ??
            //make do-while so will go through at least once?
            while(Character.isLetterOrDigit((char)symbol) //letter or digit
            || symbol == '_' || symbol == ':'             //underscore or colon
            || symbol == '.' || symbol == '-')            //dot or dash
            {
                text.append((char)symbol);
                getSymbol();

                //rework to set tag type as empty, look ahead for >,
                //or continue as start tag
                if(symbol == '/')
                {
                    ///*
                    getSymbol();
                    if(symbol != '>') //for // or just /
                    {
                        text.append('/');
                        text.append((char)symbol); //was slash without > or not />
                    }
                    else
                    {
                        this.ungetSymbol();  //return > to the symbol stream
                        emptyTag(); //eliminate emptyTag and just set tokeType to empty??
                    } //*/
                    //emptyTag();
                }//end if
            }//end while

            //check if encountered end of file
            if(symbol == -1)
            {
                error(17);
            }
            else
            if(symbol == '>')
            {
               //make the token and return
               makeToken();

            }//end if

            //<START_TAG ATTRIBUTE_NAME
            if(isSeparator(symbol))
            {
                makeToken(); //make START_TAG token
                clear();
                getSymbol();
                attribute();
            }//end if
        }
        else //not <(char) then determine other types
        {
            switch(symbol)
            {
                case '/': getSymbol(); endTag(); break; //close tag
                case '?': getSymbol(); piTag();  break; //PI tag
                case '!': getSymbol(); CTag();   break; //comment or CDATA
                default : error(1); //error
            }//end switch
        }//end if

    }//end startTag

    /**
     * Method handles tokenizing a XML end tag
     * such as </END> or </XML>
     */

    private final void endTag()
    {
        //System.out.println("DEBUG: endTag()");
        token_type = XMLTokenType.CLOSE_TAG;

        //get end tag text
        while(Character.isLetterOrDigit((char)symbol)
        || symbol == '_' || symbol == ':'
        || symbol == '-' || symbol == '.')
        {
            text.append((char)symbol);
            getSymbol();

        }//end while

        skipSeparator();

        //check if exited while on end of stream symbol
        if(symbol == -1)
        {
            error(17);
        }
        else //check for >
        if(symbol != '>')
        {
            this.dumpTokenQueue();
            error(21);
        }
        else
        {
           //make token
           makeToken();

        }//end if

    }//endTag

    //would emptyTag() consolidated into startTag() improve performance??
    /**
     * Method handles tokenizing a XML empty tag
     * such as <EMPTY/>
     */
    private final void emptyTag()
    {
        //System.out.println("DEBUG: emptyTag()");
        //System.out.println("DEBUG: symbol = "+(char)symbol+" "+symbol);

        token_type = XMLTokenType.EMPTY_TAG;
        getSymbol();

        //check for end of stream
        if(symbol == -1)
        {
            error(17);
        }//end if

        //to verify next symbol is > following />
        if(symbol != '>')
        {
            error(8);
        }
        else
        {
          //make the token
          //START_TAG makes the empty token
          return;

        }//end if
    }//end emptyTag

    /**
     * Method searchs back through the token queue to
     * reset token type to empty tag which is initially
     * enqueued as a start tag.
     */

    private final void makeEmpty()
    {
        XMLToken element = null;

        //go through token queue, find first START_TAG token
        //change to token EMPTY_TAG, if don't find it, error
        if(queue.isEmpty())
        {
            error(15);
        }//end if

        for(int x = 0;x < queue.size(); x++)
        {
            element = (XMLToken) queue.elementAt(x);
            if(element.tokenType == XMLTokenType.START_TAG)
            {
                element.tokenType = XMLTokenType.EMPTY_TAG;
                return;
            }//end if

        }//end for

        //didn't find START_TAG so error
        error(15);

    }//end makeEmpty

    /**
     * Method tokenizes a XML processing instruction (PI) tag
     * such as <?SQL Select * From Table ?>
     */

    private final void piTag()
    {
        token_type = XMLTokenType.PI;

        //get tag text
        while(symbol != '?')
        {
            if(symbol == -1)
            {
                error(17);
            }//end if
            text.append((char)symbol);
            getSymbol();
        }//end while
        getSymbol();

        if(symbol == -1)
        {
            error(17);
        }
        else //check for closing >
        if(symbol != '>')
        {
            error(20);
        }
        else
        {
          //make the token
          makeToken();

        }//end if

    }//end piTag

    /**
     * Method acts as central point in tokenizing
     * a XML comment tag or CDATA tag; based upon the
     * tag the corresponding method is called
     */

    private final void CTag()
    {


        //check for comment <!-
        if(symbol == '-')
        {
            getSymbol();
            commentTag();
        }
        else //check for CDATA <![
        if(symbol == '[')
        {
            getSymbol();
            CDATATag();
        }
        else //check for DTD element <!DOCTYPE or <!ELEMENT
        if(Character.isLetter((char)symbol))
        {
           //System.out.println("DEBUG: check if DTDElement");
           if(this.isDTDElement() == true)
           {
                //System.out.println("DEBUG: DTDElement is true");
                DTDElement();
           }
           else
           {
                //System.out.println("DEBUG: DTDElement is false");
                //System.out.println("DEBUG: symbol = "+(char)symbol);
                startTag();

           }//end if isDTDElement()


           // DTDElement(); //is DTD element?? <!DOCTYPE else if <!A => <A startTag getSymbol(); startTag();

        }
        else //check for end of stream
        if(symbol == -1)
        {
            error(17);
        }
        else
        {
            //error(2);  //<!/  throw away !, make startTag or with </ or <? or <
            startTag();

        }//end if

    }//end CTag

    /**
     * Method tokenizes a XML comment tag
     * such as:
     * <!-- very short comment -->
     */

    private final void commentTag()
    {
        token_type = XMLTokenType.COMMENT;
        if(symbol == '-') //<!-
        {
            while(true)
            {
                getSymbol();

                //end of stream symbol
                if(symbol == -1)
                {
                    error(17);
                }//end if


                if(symbol == '-')
                {
                    getSymbol();
                    if(symbol == '-')
                    {
                        getSymbol();
                        if(symbol=='>')
                        {
                            makeToken();
                            return;
                        }
                        else
                        {
                             text.append('-');
                             text.append('-');
                             text.append((char)symbol);
                        }//end if

                    }
                    else
                    {
                        text.append('-');
                        text.append((char)symbol);
                    }//end if

                }
                else
                {
                    text.append((char)symbol);
                }//end if

            }//end while
        }
        else
        {
            error(3);
        }//end if

    }//end commentTag

   /**
     * Method tokenizes a <![ non-CDATA
     * such as <![endif]> produced by Office
     * promoted to a comment to hide
     */
    private final void doAsComment()
    {
          token_type = XMLTokenType.COMMENT;
          text.append('['); //prefix <!--[
          do
          {
              text.append((char)symbol);
              getSymbol();

          } while(symbol != '>');

    }//end doAsComment


    /**
     * Method tokenizes a XML CDATA tag
     * such as <![CDATA[0xCAFEBABE]]>
     */

    private final void CDATATag()
    {
        final char CDATA[]={'C','D','A','T','A','['};

        token_type = XMLTokenType.CDATA;

        //System.out.println("DEBUG: symbol = "+(char)symbol);
        if(symbol != 'C')
        {
              //System.out.println("CDATATag() symbol != C");
              doAsComment();
              makeToken();
              return;
              //error(4);

        }
        else
        {
              //match CDATA[
              for(int x = 0;x < CDATA.length;x++)
              {
                  if(CDATA[x] == symbol)
                  {
                      getSymbol();
                      continue;
                  }
                  else
                  {
                      error(4);
                  }//end if
              }//end for

              //getSymbol();
              if(symbol == -1)
              {
                  error(17);
              }//end if

              while(symbol != ']')
              {
                  text.append((char)symbol);
                  getSymbol();
                  if(symbol == -1)
                  {
                      error(17);
                  }//end if
              }//end while

              getSymbol();
              if(symbol != ']')
              {
                  error(4);
              }//end if

              getSymbol(); //get '>'

              //check for end of stream
              if(symbol == -1)
              {
                  error(17);
              }
              else
              if(symbol != '>')
              {
                  error(19);
              }
              else
              {
                  //make token
                  makeToken();
              }//end if

        }//end if symbol != C

    }//end CDATA

   /**
     * Method determines if DTD element ahead in stream; for HTML
     * looks at symbol after !, if non-character is not DTD
     * also looks for DOCTYPE as follow-up string although in pure
     * XML it would be <!ELEMENT or <!ENTITY ??
     * <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     *  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
     */

    private boolean isDTDElement()
    {
        boolean isDTDElementFlag = true;
        final char upper_DOCTYPE[]={'D','O','C','T','Y','P','E'};
        final char lower_DOCTYPE[]={'d','o','c','t','y','p','e'};

        //System.out.println("DEBUG: isDTDElement symbol = "+symbol+" "+(char)symbol);
        //mark stream so can lookahead
        this.mark();


        if(Character.isLetterOrDigit((char)symbol) == false)
        {
            //System.out.println("Not character or digit; isDTDELEMENT = false");
            isDTDElementFlag = false;

        }//end if  //make else
        else
        {


              //match DOCTYPE set as true, early exit if false
              isDTDElementFlag = true; //set for true
              for(int x = 0;x < upper_DOCTYPE.length;x++)
              {
                  if(upper_DOCTYPE[x] != symbol && lower_DOCTYPE[x] != symbol)
                  {
                      isDTDElementFlag = false;
                      //System.out.println("DOCTYPE[x] != symbol; isDTDELEMENT = false");
                      break;//break out of for
                  }//end if
                  getSymbol();
              }//end for

        }//end if

        //restore to previous position in stream
        this.reset();

        //already had first symbol if DTDElement from <!DOCTYPE
        //resetting stream will not return, so reset buffer with D
        if(isDTDElementFlag == true)
        {
              buffer = 'D';
        }//end if

        return isDTDElementFlag;
    }//end isDTDElement




    /**
     * Method handles tokenizing a XML data type definition (DTD)
     * element that can be included within a XML document such as:
     *
     * <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     *  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
     */

    private final void DTDElement()
    {
        //count angle brackets < and >
        //when 0 > is the end of the DTD element
        int angleBracketCounter = 1;

        //set the token type as ELEMENT
        token_type = XMLTokenType.DTDELEMENT;

        while(true)
        {
            //check the symbol
            //count angle brackets for multi-line DTD
            if(symbol == '<')
            {
                angleBracketCounter++;
                //text.append((char)symbol);
            }//end if

            //check for end of DTD element
            if(symbol == '>')
            {
                angleBracketCounter--;

                if(angleBracketCounter == 0)
                {
                    //text.append((char)symbol);
                    makeToken();
                    return;
                }//end if

            }//end if >

            //add limit on size of DTD element length??

            //on end of file, report error
            if(symbol == -1)
            {
                error(22);
            }//end if

            //append symbol to text
            text.append((char)symbol);
            getSymbol();

        }//end while

    }//end DTDElement

    //have error verbosity option boolean?
    //add error printwriter stream, passed in constructor?
    //create error categories in range of 10 digits
    //00..09: general
    //10..19: start_tag
    //...etc?

    /**
     * Method that reports the error and exits with a error return
     * code, or throws an exception is setThrowsException is true.
     * @param error code
     * @exception XMLTokenizerException
     */

    private final void error(int e) //throws unchecked XMLScannerException
    {
        //rewrite for both throwing exceptions, and reporting errors
        String errorMessage = null;

        //only report verbose if non-exception and verbose enabled
        if(verboseErrorFlag == true)//&& throwExceptionFlag == false)
        {
            err.println();
            //err.print("Error: " + e);
            err.print("Error: " + e + " on line " + line_count +" at symbol " + symbol_count + ".");
            err.println();


            err.print(text);
            err.println("#"+symbol);

            for(int x =  0;x < text.length(); x++)
            {
                err.print(".");
            }//end for

            err.print(".^^ ");
//*/
        }//end if

        //err.println();
        err.print(e+": ");




        switch(e)
        {
            case 0: errorMessage = "Invalid input symbol: "+(char)symbol;
            break;

            case 1: errorMessage = "Expected  [ / ! or text not symbol: "+(char)symbol;
            break;

            case 2: errorMessage = "Expected - or [ not symbol: "+(char)symbol;
            break;

            case 3: errorMessage = "Invalid symbol for comment is: "+(char)symbol+" US-ASCII #"+symbol;
            case 4: errorMessage = "Invalid symbol for CDATA is "+(char)symbol;
            break;

            case 5: errorMessage = "Invalid symbol for text is: "+(char)symbol;
            break;

            case 6: errorMessage = "Entity references require non-zero text length";
            break;

            case 7: errorMessage = "Invalid symbol for entity reference.";
            break;

            case 8: errorMessage = "Empty tag must be followed by > invalid symbol: "+(char)symbol;
            break;

            case 9: errorMessage = "Separator symbol invalid in attribute name";
            break;

            case 10: errorMessage = "Attribute values must be enclosed with single or double quotes.";
            break;

            case 11: errorMessage = "Attribute values may not contain a separator character";
            break;

            case 12: errorMessage = "Ending quote symbol: "+(char)symbol+" for attribute value must match ending quote symbol: "+(char)symbol;
            break;

            case 13: errorMessage = "Invalid symbol: "+(char) symbol+" for attribute encountered; expected separator character.";
            break;

            case 14: errorMessage = "No start tag found for attributes.";
            break;

            case 15: errorMessage = "No start tag found to convert to empty tag.";
            break;

            case 16: errorMessage = "Invalid symbol: "+(char)symbol+" for attribute.";
            break;

            //change error 17 for specific tags/end of stream conditions?
            case 17: errorMessage = "Unexpected end of input within a tag or token.";
            break;

            case 18: errorMessage = "Comment tag/token must end with >; not symbol: "+(char)symbol;
            break;

            case 19: errorMessage = "CDATA tag/token must end with >; not symbol: "+(char)symbol;
            break;

            case 20: errorMessage = "Processing Instruction tag/token must end with >; not symbol: "+(char)symbol;
            break;

            case 21: errorMessage = "End tag/token must end with >; not symbol: "+(char)symbol;
            break;

            case 22: errorMessage = "Unexpected end of input within DTD element.";
            break;

            case 23: errorMessage = "Out of Memory Error. Insufficient memory to continue.";
            break;

            default: errorMessage = "Unknown error type encountered with error code: "+e;

        }//end switch

        //flush the error stream after reporting error
        if(throwExceptionFlag == false)
        {
             err.println(errorMessage);
             err.flush();

            //exit do not continue scanning
			//exit with error status > 0
            System.exit(e+1);
        }
        else
        {
            //throw unchecked exception
            throw new XMLTokenizerException(errorMessage); //" TAG TEXT: "+this.text+" TOKEN TYPE: "+this.token_type.toString());
        }//end if

        //dump the queue
        err.println("Dumping token queue:");
        err.println();
        for(int x=0;x< this.queue.size(); x++)
        {
              err.println( ((XMLToken)queue.get(x)).asTuple());

        }//end for



    }//end error
}