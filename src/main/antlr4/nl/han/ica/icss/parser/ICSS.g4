grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';

// Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

// Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

// Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

// General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

// All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---
selector: LOWER_IDENT | ID_IDENT | CLASS_IDENT;

declaration: LOWER_IDENT;

variableReference: CAPITAL_IDENT;

operatorPrio: MUL;
operator: PLUS | MIN;

expression: PIXELSIZE
	| PERCENTAGE
	| COLOR
	| SCALAR
	// preferably TRUE and FALSE are separated from styling literals, but to
	// make the parsing easier, they aren't
	| TRUE
	| FALSE
	| variableReference;

operation: expression
	| operation operatorPrio operation
	| operation operator operation;

// TODO: Shouldn't `expression` be a `operation`?
variableAssignment: variableReference ASSIGNMENT_OPERATOR expression SEMICOLON;

styling: ifStatement | variableAssignment | (declaration COLON operation SEMICOLON);

elseStatement: ELSE OPEN_BRACE styling* CLOSE_BRACE;

ifStatement: IF BOX_BRACKET_OPEN variableReference BOX_BRACKET_CLOSE
	OPEN_BRACE styling* CLOSE_BRACE elseStatement?;

styleRule: selector OPEN_BRACE styling* CLOSE_BRACE;

stylesheet: (variableAssignment | styleRule)* EOF;
