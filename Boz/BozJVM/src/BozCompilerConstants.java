/* Generated By:JavaCC: Do not edit this line. BozCompilerConstants.java */

/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface BozCompilerConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 8;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 9;
  /** RegularExpression Id. */
  int CLASS = 11;
  /** RegularExpression Id. */
  int CUT = 12;
  /** RegularExpression Id. */
  int DO = 13;
  /** RegularExpression Id. */
  int DONE = 14;
  /** RegularExpression Id. */
  int EF = 15;
  /** RegularExpression Id. */
  int ELSE = 16;
  /** RegularExpression Id. */
  int END = 17;
  /** RegularExpression Id. */
  int IF = 18;
  /** RegularExpression Id. */
  int IT = 19;
  /** RegularExpression Id. */
  int MY = 20;
  /** RegularExpression Id. */
  int ON = 21;
  /** RegularExpression Id. */
  int SNAP = 22;
  /** RegularExpression Id. */
  int TRY = 23;
  /** RegularExpression Id. */
  int ARGS = 24;
  /** RegularExpression Id. */
  int CATCH = 25;
  /** RegularExpression Id. */
  int CAUSE = 26;
  /** RegularExpression Id. */
  int DIE = 27;
  /** RegularExpression Id. */
  int ECHO = 28;
  /** RegularExpression Id. */
  int EOL = 29;
  /** RegularExpression Id. */
  int EXIT = 30;
  /** RegularExpression Id. */
  int FALSE = 31;
  /** RegularExpression Id. */
  int NEW = 32;
  /** RegularExpression Id. */
  int NIL = 33;
  /** RegularExpression Id. */
  int SCAN = 34;
  /** RegularExpression Id. */
  int TRUE = 35;
  /** RegularExpression Id. */
  int INTEGER_LITERAL = 36;
  /** RegularExpression Id. */
  int DECIMAL_LITERAL = 37;
  /** RegularExpression Id. */
  int HEX_LITERAL = 38;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 39;
  /** RegularExpression Id. */
  int EXPONENT = 40;
  /** RegularExpression Id. */
  int CHARACTER_LITERAL = 41;
  /** RegularExpression Id. */
  int STRING_LITERAL = 42;
  /** RegularExpression Id. */
  int IDENTIFIER = 43;
  /** RegularExpression Id. */
  int LETTER = 44;
  /** RegularExpression Id. */
  int PART_LETTER = 45;
  /** RegularExpression Id. */
  int LPAREN = 46;
  /** RegularExpression Id. */
  int RPAREN = 47;
  /** RegularExpression Id. */
  int LBRACE = 48;
  /** RegularExpression Id. */
  int RBRACE = 49;
  /** RegularExpression Id. */
  int LBRACKET = 50;
  /** RegularExpression Id. */
  int RBRACKET = 51;
  /** RegularExpression Id. */
  int SEMICOLON = 52;
  /** RegularExpression Id. */
  int COMMA = 53;
  /** RegularExpression Id. */
  int DOT = 54;
  /** RegularExpression Id. */
  int COLON = 55;
  /** RegularExpression Id. */
  int ASSIGN = 56;
  /** RegularExpression Id. */
  int GT = 57;
  /** RegularExpression Id. */
  int LT = 58;
  /** RegularExpression Id. */
  int BANG = 59;
  /** RegularExpression Id. */
  int TIC = 60;
  /** RegularExpression Id. */
  int TILDE = 61;
  /** RegularExpression Id. */
  int HOOK = 62;
  /** RegularExpression Id. */
  int AT = 63;
  /** RegularExpression Id. */
  int DOLLAR = 64;
  /** RegularExpression Id. */
  int EQ = 65;
  /** RegularExpression Id. */
  int LE = 66;
  /** RegularExpression Id. */
  int GE = 67;
  /** RegularExpression Id. */
  int NE = 68;
  /** RegularExpression Id. */
  int OR = 69;
  /** RegularExpression Id. */
  int AND = 70;
  /** RegularExpression Id. */
  int INC = 71;
  /** RegularExpression Id. */
  int DEC = 72;
  /** RegularExpression Id. */
  int PLUS = 73;
  /** RegularExpression Id. */
  int MINUS = 74;
  /** RegularExpression Id. */
  int STAR = 75;
  /** RegularExpression Id. */
  int SLASH = 76;
  /** RegularExpression Id. */
  int BIT_AND = 77;
  /** RegularExpression Id. */
  int BIT_OR = 78;
  /** RegularExpression Id. */
  int XOR = 79;
  /** RegularExpression Id. */
  int POW = 80;
  /** RegularExpression Id. */
  int REM = 81;
  /** RegularExpression Id. */
  int LSHIFT = 82;
  /** RegularExpression Id. */
  int RSHIFT = 83;
  /** RegularExpression Id. */
  int ISGLOB = 84;
  /** RegularExpression Id. */
  int SIZEOF = 85;
  /** RegularExpression Id. */
  int ATRANGE = 86;
  /** RegularExpression Id. */
  int ATIMAGE = 87;
  /** RegularExpression Id. */
  int ATCLEAR = 88;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int IN_SINGLE_LINE_COMMENT = 1;
  /** Lexical state. */
  int IN_MULTI_LINE_COMMENT = 2;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "\"#\"",
    "\"/*\"",
    "<SINGLE_LINE_COMMENT>",
    "\"*/\"",
    "<token of kind 10>",
    "\"class\"",
    "\"cut\"",
    "\"do\"",
    "\"done\"",
    "\"ef\"",
    "\"else\"",
    "\"end\"",
    "\"if\"",
    "\"it\"",
    "\"my\"",
    "\"on\"",
    "\"snap\"",
    "\"try\"",
    "\"args\"",
    "\"catch\"",
    "\"cause\"",
    "\"die\"",
    "\"echo\"",
    "\"eol\"",
    "\"exit\"",
    "\"false\"",
    "\"new\"",
    "\"nil\"",
    "\"scan\"",
    "\"true\"",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<CHARACTER_LITERAL>",
    "<STRING_LITERAL>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<PART_LETTER>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "\":\"",
    "\"=\"",
    "\">\"",
    "\"<\"",
    "\"!\"",
    "\"`\"",
    "\"~\"",
    "\"?\"",
    "\"@\"",
    "\"$\"",
    "\"==\"",
    "\"<=\"",
    "\">=\"",
    "\"!=\"",
    "\"||\"",
    "\"&&\"",
    "\"++\"",
    "\"--\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"&\"",
    "\"|\"",
    "\"^\"",
    "\"**\"",
    "\"%\"",
    "\"<<\"",
    "\">>\"",
    "\"??\"",
    "\"%%\"",
    "\"::\"",
    "\"?:\"",
    "\"!!\"",
  };

}
