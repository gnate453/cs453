/*
  mj.lex
    Tokens/Symbols for MeggyJava language.
    NO dollars, but underscores anywhere in identifiers 
    (to avoid problems later when generating AVR labels from id-s)
    8 colors (not all the CPP colors)
    The values for the colors, buttons, and tones were found in
    MeggyJrSimple.h and MeggyJrSimple.cpp.
    Ignore single line comments: // until eol 
    ALSO: ignore C style comments, see http://ostermiller.org/findcomment.html
    Wim Bohm and Michelle Strout, 6/2011
*/
package mjparser;
import java_cup.runtime.Symbol;


public class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

	private int getInt(String str) {
		return Integer.parseInt(str);
	}
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	public Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NOT_ACCEPT,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NOT_ACCEPT,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NOT_ACCEPT,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NOT_ACCEPT,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NOT_ACCEPT,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NOT_ACCEPT,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NOT_ACCEPT,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NOT_ACCEPT,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NOT_ACCEPT,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NOT_ACCEPT,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NOT_ACCEPT,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NOT_ACCEPT,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NOT_ACCEPT,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NOT_ACCEPT,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NOT_ACCEPT,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NOT_ACCEPT,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NOT_ACCEPT,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NOT_ACCEPT,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NOT_ACCEPT,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NOT_ACCEPT,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NOT_ACCEPT,
		/* 131 */ YY_NOT_ACCEPT,
		/* 132 */ YY_NOT_ACCEPT,
		/* 133 */ YY_NOT_ACCEPT,
		/* 134 */ YY_NOT_ACCEPT,
		/* 135 */ YY_NOT_ACCEPT,
		/* 136 */ YY_NOT_ACCEPT,
		/* 137 */ YY_NOT_ACCEPT,
		/* 138 */ YY_NOT_ACCEPT,
		/* 139 */ YY_NOT_ACCEPT,
		/* 140 */ YY_NOT_ACCEPT,
		/* 141 */ YY_NOT_ACCEPT,
		/* 142 */ YY_NOT_ACCEPT,
		/* 143 */ YY_NOT_ACCEPT,
		/* 144 */ YY_NOT_ACCEPT,
		/* 145 */ YY_NOT_ACCEPT,
		/* 146 */ YY_NOT_ACCEPT,
		/* 147 */ YY_NOT_ACCEPT,
		/* 148 */ YY_NOT_ACCEPT,
		/* 149 */ YY_NOT_ACCEPT,
		/* 150 */ YY_NOT_ACCEPT,
		/* 151 */ YY_NOT_ACCEPT,
		/* 152 */ YY_NOT_ACCEPT,
		/* 153 */ YY_NOT_ACCEPT,
		/* 154 */ YY_NOT_ACCEPT,
		/* 155 */ YY_NOT_ACCEPT,
		/* 156 */ YY_NOT_ACCEPT,
		/* 157 */ YY_NOT_ACCEPT,
		/* 158 */ YY_NOT_ACCEPT,
		/* 159 */ YY_NOT_ACCEPT,
		/* 160 */ YY_NOT_ACCEPT,
		/* 161 */ YY_NOT_ACCEPT,
		/* 162 */ YY_NOT_ACCEPT,
		/* 163 */ YY_NOT_ACCEPT,
		/* 164 */ YY_NOT_ACCEPT,
		/* 165 */ YY_NOT_ACCEPT,
		/* 166 */ YY_NOT_ACCEPT,
		/* 167 */ YY_NOT_ACCEPT,
		/* 168 */ YY_NOT_ACCEPT,
		/* 169 */ YY_NOT_ACCEPT,
		/* 170 */ YY_NOT_ACCEPT,
		/* 171 */ YY_NOT_ACCEPT,
		/* 172 */ YY_NOT_ACCEPT,
		/* 173 */ YY_NOT_ACCEPT,
		/* 174 */ YY_NOT_ACCEPT,
		/* 175 */ YY_NOT_ACCEPT,
		/* 176 */ YY_NOT_ACCEPT,
		/* 177 */ YY_NOT_ACCEPT,
		/* 178 */ YY_NOT_ACCEPT,
		/* 179 */ YY_NOT_ACCEPT,
		/* 180 */ YY_NOT_ACCEPT,
		/* 181 */ YY_NOT_ACCEPT,
		/* 182 */ YY_NOT_ACCEPT,
		/* 183 */ YY_NOT_ACCEPT,
		/* 184 */ YY_NOT_ACCEPT,
		/* 185 */ YY_NOT_ACCEPT,
		/* 186 */ YY_NOT_ACCEPT,
		/* 187 */ YY_NOT_ACCEPT,
		/* 188 */ YY_NOT_ACCEPT,
		/* 189 */ YY_NOT_ACCEPT,
		/* 190 */ YY_NOT_ACCEPT,
		/* 191 */ YY_NOT_ACCEPT,
		/* 192 */ YY_NOT_ACCEPT,
		/* 193 */ YY_NOT_ACCEPT,
		/* 194 */ YY_NOT_ACCEPT,
		/* 195 */ YY_NOT_ACCEPT,
		/* 196 */ YY_NOT_ACCEPT,
		/* 197 */ YY_NOT_ACCEPT,
		/* 198 */ YY_NOT_ACCEPT,
		/* 199 */ YY_NOT_ACCEPT,
		/* 200 */ YY_NO_ANCHOR,
		/* 201 */ YY_NOT_ACCEPT,
		/* 202 */ YY_NOT_ACCEPT,
		/* 203 */ YY_NOT_ACCEPT,
		/* 204 */ YY_NOT_ACCEPT,
		/* 205 */ YY_NOT_ACCEPT,
		/* 206 */ YY_NOT_ACCEPT,
		/* 207 */ YY_NOT_ACCEPT,
		/* 208 */ YY_NOT_ACCEPT,
		/* 209 */ YY_NOT_ACCEPT,
		/* 210 */ YY_NOT_ACCEPT,
		/* 211 */ YY_NOT_ACCEPT,
		/* 212 */ YY_NOT_ACCEPT,
		/* 213 */ YY_NOT_ACCEPT,
		/* 214 */ YY_NOT_ACCEPT,
		/* 215 */ YY_NOT_ACCEPT,
		/* 216 */ YY_NOT_ACCEPT,
		/* 217 */ YY_NOT_ACCEPT,
		/* 218 */ YY_NO_ANCHOR,
		/* 219 */ YY_NOT_ACCEPT,
		/* 220 */ YY_NOT_ACCEPT,
		/* 221 */ YY_NOT_ACCEPT,
		/* 222 */ YY_NOT_ACCEPT,
		/* 223 */ YY_NOT_ACCEPT,
		/* 224 */ YY_NOT_ACCEPT,
		/* 225 */ YY_NO_ANCHOR,
		/* 226 */ YY_NOT_ACCEPT,
		/* 227 */ YY_NO_ANCHOR,
		/* 228 */ YY_NO_ANCHOR,
		/* 229 */ YY_NO_ANCHOR,
		/* 230 */ YY_NO_ANCHOR,
		/* 231 */ YY_NO_ANCHOR,
		/* 232 */ YY_NO_ANCHOR,
		/* 233 */ YY_NO_ANCHOR,
		/* 234 */ YY_NO_ANCHOR,
		/* 235 */ YY_NO_ANCHOR,
		/* 236 */ YY_NO_ANCHOR,
		/* 237 */ YY_NO_ANCHOR,
		/* 238 */ YY_NO_ANCHOR,
		/* 239 */ YY_NO_ANCHOR,
		/* 240 */ YY_NO_ANCHOR,
		/* 241 */ YY_NO_ANCHOR,
		/* 242 */ YY_NO_ANCHOR,
		/* 243 */ YY_NO_ANCHOR,
		/* 244 */ YY_NO_ANCHOR,
		/* 245 */ YY_NO_ANCHOR,
		/* 246 */ YY_NO_ANCHOR,
		/* 247 */ YY_NO_ANCHOR,
		/* 248 */ YY_NO_ANCHOR,
		/* 249 */ YY_NO_ANCHOR,
		/* 250 */ YY_NO_ANCHOR,
		/* 251 */ YY_NO_ANCHOR,
		/* 252 */ YY_NO_ANCHOR,
		/* 253 */ YY_NO_ANCHOR,
		/* 254 */ YY_NO_ANCHOR,
		/* 255 */ YY_NO_ANCHOR,
		/* 256 */ YY_NO_ANCHOR,
		/* 257 */ YY_NO_ANCHOR,
		/* 258 */ YY_NO_ANCHOR,
		/* 259 */ YY_NO_ANCHOR,
		/* 260 */ YY_NO_ANCHOR,
		/* 261 */ YY_NOT_ACCEPT,
		/* 262 */ YY_NOT_ACCEPT,
		/* 263 */ YY_NO_ANCHOR,
		/* 264 */ YY_NO_ANCHOR,
		/* 265 */ YY_NO_ANCHOR,
		/* 266 */ YY_NO_ANCHOR,
		/* 267 */ YY_NO_ANCHOR,
		/* 268 */ YY_NO_ANCHOR,
		/* 269 */ YY_NO_ANCHOR,
		/* 270 */ YY_NO_ANCHOR,
		/* 271 */ YY_NO_ANCHOR,
		/* 272 */ YY_NO_ANCHOR,
		/* 273 */ YY_NO_ANCHOR,
		/* 274 */ YY_NO_ANCHOR,
		/* 275 */ YY_NO_ANCHOR,
		/* 276 */ YY_NO_ANCHOR,
		/* 277 */ YY_NO_ANCHOR,
		/* 278 */ YY_NO_ANCHOR,
		/* 279 */ YY_NO_ANCHOR,
		/* 280 */ YY_NO_ANCHOR,
		/* 281 */ YY_NO_ANCHOR,
		/* 282 */ YY_NO_ANCHOR,
		/* 283 */ YY_NO_ANCHOR,
		/* 284 */ YY_NOT_ACCEPT,
		/* 285 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"67:9,70,68,67,70,69,67:18,70,12,67:4,10,67,4,5,3,1,13,2,16,66,64,65:2,61,65" +
":6,67,9,11,6,67:3,42,47,48,45,44,62,53,60,58,63,50,43,40,52,51,41,63,49,35," +
"59,56,57,55,63,54,63,14,67,15,67,63,67,18,21,34,38,24,27,36,32,19,63,46,23," +
"17,20,22,33,63,29,28,25,30,37,31,39,26,63,7,67,8,67:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,286,
"0,1:6,2,1:3,3,1:6,4,5,1:5,6,7,6:8,1,6:11,8,1:2,9,10,1:31,11,12,13,14,15,1,1" +
"6,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,4" +
"1,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,6" +
"6,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,9" +
"1,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,1" +
"12,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130," +
"131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149" +
",150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,16" +
"8,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,6" +
",187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,20" +
"5,206,207,208,209,210,211")[0];

	private int yy_nxt[][] = unpackFromString(212,71,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,260,85,200,269,260,271,273,274" +
",260,275,276,277,260,278,260,279,280,281,260,282,260:2,283,260:20,19,260:2," +
"20,19,84,88,21,86,22,-1:77,23,-1:74,24,-1:77,260,218,260:5,225,260:41,-1:66" +
",19,-1:2,19:2,-1:22,260:49,-1:6,26:67,-1:2,26,-1:16,139,-1:70,144,-1:70,157" +
",-1:55,83:2,87,83:67,-1:3,83,-1:62,26,-1:21,227,260:2,89,260:6,25,260:38,-1" +
":73,21,-1:3,83:2,87,83:62,35,83:4,-1:17,260:8,27,260:40,-1:45,94,-1:47,260:" +
"14,28,260:34,-1:30,96,-1:2,201,-1:5,98,-1,219,-1,226,-1:8,100,202,-1:10,261" +
",-1:28,260:3,29,260:45,-1:29,102,-1:63,260:7,30,260:41,-1:27,104,-1:65,260:" +
"7,31,260:41,-1:37,108,-1:55,260:7,32,260:41,-1:35,221,-1:57,260:11,33,260:3" +
"7,-1:41,203,-1:51,260:21,34,260:27,-1:25,206,-1:67,260:7,36,260:41,-1:30,11" +
"2,-1:62,260:7,37,260:41,-1:29,114,-1:63,260:11,38,260:37,-1:28,118,-1:63,90" +
",260:49,-1:46,130,131,-1:45,260:8,39,260:40,-1:39,132,-1:53,260:15,40,260:3" +
"3,-1:46,284,-1:46,260:17,41,260:31,-1:23,133,-1:69,260:3,42,260:45,-1:30,20" +
"8,-1:62,260:17,43,260:31,-1:27,134,-1:65,260:19,44,260:29,-1:29,47,-1:62,92" +
",260:49,-1:31,48,-1:61,260:3,45,260:45,-1:40,135,-1:52,260:11,46,260:37,-1:" +
"24,136,-1:81,209,-1:86,137,-1:50,49,-1:73,50,-1:66,140,-1:84,141,-1:78,143," +
"-1:43,51,-1:92,145,-1,146,147,-1,148,149,-1:4,150,-1:8,151,-1:26,152,-1:76," +
"153,-1:89,154,-1:57,155,-1:85,158,-1,159,-1,211,-1,160,-1,213,222,161,-1,16" +
"2,-1:41,163,-1:32,52,-1:70,53,-1:37,164,-1:32,54,-1:70,55,-1:37,165,-1:32,5" +
"6,-1:37,166,-1:32,57,-1:37,167,-1:32,58,-1:38,168,-1:64,59,-1:91,169,-1:51," +
"212,-1:68,60,-1:89,61,170,-1,171,-1,62,-1,172,-1:6,173,-1:56,223,-1:71,174," +
"-1:76,176,-1:81,214,-1:68,179,-1:73,63,-1:70,64,-1:70,65,-1:70,66,-1:70,67," +
"-1:34,68,-1:90,180,-1:49,181,-1:68,182,-1:67,220,-1:84,69,-1:93,184,-1:59,7" +
"0,-1:67,185,-1:72,216,-1:69,217,-1:78,187,-1:47,71,-1:69,189,-1:74,190,-1:8" +
"9,72,-1:64,73,-1:78,192,-1:77,195,-1:54,224,-1:47,74,-1:75,75,-1:65,76,-1:8" +
"2,196,-1:91,197,-1:69,77,-1:69,198,-1:63,78,-1:51,79,-1:89,80,-1:81,81,-1:7" +
"4,82,-1:28,260:7,91,260:41,-1:29,106,-1:68,207,-1:84,126,-1:54,124,-1:75,11" +
"6,-1:69,128,-1:69,122,-1:69,138,-1:87,142,-1:55,156,-1:90,175,-1:51,215,-1:" +
"94,177,-1:79,186,-1:34,188,-1:92,193,-1:69,194,-1:44,260:2,93,260:46,-1:29," +
"205,-1:82,191,-1:59,120,-1:89,178,-1:75,183,-1:65,199,-1:43,260:19,263,260:" +
"29,-1:29,110,-1:63,260:16,264,260:32,-1:22,260:5,266,260:43,-1:22,260:8,95," +
"260:40,-1:22,260:3,270,260:45,-1:22,260:11,97,260:37,-1:22,260:8,241,260:40" +
",-1:22,260:13,99,260:35,-1:22,260:2,101,260:46,-1:22,260:6,242,260:42,-1:22" +
",260,243,260:47,-1:22,260:2,245,260:46,-1:22,260:4,246,260:44,-1:22,260:12," +
"248,260:36,-1:22,260:2,103,260:46,-1:22,260:7,252,260:41,-1:22,260:11,105,2" +
"60:37,-1:22,260:8,253,260:40,-1:22,260:13,254,260:35,-1:22,260:6,107,260:42" +
",-1:22,260:6,255,260:42,-1:22,260:11,109,260:37,-1:22,260:2,256,260:46,-1:2" +
"2,260:9,111,260:39,-1:22,260:12,113,260:36,-1:22,260:8,115,260:40,-1:22,260" +
":3,259,260:45,-1:22,260:2,117,260:46,-1:22,260:12,119,260:36,-1:22,260:2,12" +
"1,260:46,-1:22,260:3,123,260:45,-1:22,260:9,125,260:39,-1:22,260,127,260:47" +
",-1:22,260:21,129,260:27,-1:27,204,-1:87,210,-1:48,260:19,249,260:29,-1:22," +
"260:5,250,260:43,-1:22,260:8,244,260:40,-1:22,260:6,268,260:42,-1:22,260,24" +
"7,260:47,-1:22,260:7,258,260:41,-1:22,260:5,228,260:3,229,260:39,-1:22,260:" +
"19,251,260:29,-1:22,260:7,230,260:41,-1:22,260:19,257,260:29,-1:22,260:6,23" +
"1,260:15,232,260:26,-1:22,260:12,233,260:2,234,260:33,-1:22,260,235,260:47," +
"-1:22,260:8,236,260:40,-1:22,260:7,265,260:41,-1:22,260:15,237,260:33,-1:22" +
",260:13,238,260:35,-1:22,260:6,267,260:42,-1:22,260:8,239,260:40,-1:22,260:" +
"5,240,260:43,-1:22,260:7,285,260:41,-1:24,262,-1:68,260:19,272,260:29,-1:5");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

  return new Symbol(sym.EOF, new SymbolValue(yyline, yychar+1, "EOF"));
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{return new Symbol(sym.PLUS, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -3:
						break;
					case 3:
						{return new Symbol(sym.MINUS, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -4:
						break;
					case 4:
						{return new Symbol(sym.TIMES, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -5:
						break;
					case 5:
						{return new Symbol(sym.LPAREN, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -6:
						break;
					case 6:
						{return new Symbol(sym.RPAREN, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -7:
						break;
					case 7:
						{return new Symbol(sym.ASSIGN, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -8:
						break;
					case 8:
						{return new Symbol(sym.LBRACE, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -9:
						break;
					case 9:
						{return new Symbol(sym.RBRACE, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -10:
						break;
					case 10:
						{return new Symbol(sym.SEMI, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -11:
						break;
					case 11:
						{ System.err.println("Illegal character: "+yytext()); }
					case -12:
						break;
					case 12:
						{return new Symbol(sym.LT, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -13:
						break;
					case 13:
						{return new Symbol(sym.NOT, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -14:
						break;
					case 14:
						{return new Symbol(sym.COMMA, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -15:
						break;
					case 15:
						{return new Symbol(sym.LBRACKET, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -16:
						break;
					case 16:
						{return new Symbol(sym.RBRACKET, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -17:
						break;
					case 17:
						{return new Symbol(sym.DOT, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -18:
						break;
					case 18:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -19:
						break;
					case 19:
						{return new Symbol(sym.INT_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), getInt(yytext())));}
					case -20:
						break;
					case 20:
						{return new Symbol(sym.INT_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 0));}
					case -21:
						break;
					case 21:
						{/*reset pos to -1, if 0, otherwise line 1 starts at 0, rest start at 1 */ yychar=-1;}
					case -22:
						break;
					case 22:
						{ /* ignore white space. */ }
					case -23:
						break;
					case 23:
						{return new Symbol(sym.EQUAL, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -24:
						break;
					case 24:
						{return new Symbol(sym.AND, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -25:
						break;
					case 25:
						{return new Symbol(sym.IF, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -26:
						break;
					case 26:
						{}
					case -27:
						break;
					case 27:
						{return new Symbol(sym.INT, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -28:
						break;
					case 28:
						{return new Symbol(sym.NEW, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -29:
						break;
					case 29:
						{return new Symbol(sym.MAIN, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -30:
						break;
					case 30:
						{return new Symbol(sym.BYTE, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -31:
						break;
					case 31:
						{return new Symbol(sym.ELSE, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -32:
						break;
					case 32:
						{return new Symbol(sym.TRUE, new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -33:
						break;
					case 33:
						{return new Symbol(sym.THIS, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -34:
						break;
					case 34:
						{return new Symbol(sym.VOID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -35:
						break;
					case 35:
						{}
					case -36:
						break;
					case 36:
						{return new Symbol(sym.FALSE, new SymbolValue(yyline+1, yychar+1, yytext(), 0));}
					case -37:
						break;
					case 37:
						{return new Symbol(sym.WHILE, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -38:
						break;
					case 38:
						{return new Symbol(sym.CLASS, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -39:
						break;
					case 39:
						{return new Symbol(sym.IMPORT, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -40:
						break;
					case 40:
						{return new Symbol(sym.LENGTH, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -41:
						break;
					case 41:
						{return new Symbol(sym.STATIC, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -42:
						break;
					case 42:
						{return new Symbol(sym.RETURN, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -43:
						break;
					case 43:
						{return new Symbol(sym.PUBLIC, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -44:
						break;
					case 44:
						{return new Symbol(sym.STRING, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -45:
						break;
					case 45:
						{return new Symbol(sym.BOOLEAN, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -46:
						break;
					case 46:
						{return new Symbol(sym.EXTENDS, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -47:
						break;
					case 47:
						{return new Symbol(sym.MEGGYTONE, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -48:
						break;
					case 48:
						{return new Symbol(sym.MEGGY, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -49:
						break;
					case 49:
						{return new Symbol(sym.MEGGYDELAY, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -50:
						break;
					case 50:
						{return new Symbol(sym.MEGGYCOLOR, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -51:
						break;
					case 51:
						{return new Symbol(sym.MEGGYBUTTON, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -52:
						break;
					case 52:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 36363));}
					case -53:
						break;
					case 53:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 48541));}
					case -54:
						break;
					case 54:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 54485));}
					case -55:
						break;
					case 55:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 32397));}
					case -56:
						break;
					case 56:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 61157));}
					case -57:
						break;
					case 57:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 40816));}
					case -58:
						break;
					case 58:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 45816));}
					case -59:
						break;
					case 59:
						{return new Symbol(sym.MEGGYSETPIXEL, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -60:
						break;
					case 60:
						{return new Symbol(sym.MEGGYGETPIXEL, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -61:
						break;
					case 61:
						{return new Symbol(sym.BUTTON_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 2));}
					case -62:
						break;
					case 62:
						{return new Symbol(sym.BUTTON_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -63:
						break;
					case 63:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 34323));}
					case -64:
						break;
					case 64:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 51427));}
					case -65:
						break;
					case 65:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 57724));}
					case -66:
						break;
					case 66:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 38526));}
					case -67:
						break;
					case 67:
						{return new Symbol(sym.TONE_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 43243));}
					case -68:
						break;
					case 68:
						{return new Symbol(sym.MEGGYTONESTART, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -69:
						break;
					case 69:
						{return new Symbol(sym.BUTTON_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 4));}
					case -70:
						break;
					case 70:
						{return new Symbol(sym.COLOR_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -71:
						break;
					case 71:
						{return new Symbol(sym.MEGGYSETAUXLEDS, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -72:
						break;
					case 72:
						{return new Symbol(sym.COLOR_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 0));}
					case -73:
						break;
					case 73:
						{return new Symbol(sym.COLOR_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 5));}
					case -74:
						break;
					case 74:
						{return new Symbol(sym.MEGGYCHECKBUTTON, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -75:
						break;
					case 75:
						{return new Symbol(sym.BUTTON_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 16));}
					case -76:
						break;
					case 76:
						{return new Symbol(sym.BUTTON_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 8));}
					case -77:
						break;
					case 77:
						{return new Symbol(sym.COLOR_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 4));}
					case -78:
						break;
					case 78:
						{return new Symbol(sym.COLOR_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 7));}
					case -79:
						break;
					case 79:
						{return new Symbol(sym.BUTTON_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 32));}
					case -80:
						break;
					case 80:
						{return new Symbol(sym.COLOR_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 2));}
					case -81:
						break;
					case 81:
						{return new Symbol(sym.COLOR_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 3));}
					case -82:
						break;
					case 82:
						{return new Symbol(sym.COLOR_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), 6));}
					case -83:
						break;
					case 84:
						{ System.err.println("Illegal character: "+yytext()); }
					case -84:
						break;
					case 85:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -85:
						break;
					case 86:
						{/*reset pos to -1, if 0, otherwise line 1 starts at 0, rest start at 1 */ yychar=-1;}
					case -86:
						break;
					case 88:
						{ System.err.println("Illegal character: "+yytext()); }
					case -87:
						break;
					case 89:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -88:
						break;
					case 91:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -89:
						break;
					case 93:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -90:
						break;
					case 95:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -91:
						break;
					case 97:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -92:
						break;
					case 99:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -93:
						break;
					case 101:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -94:
						break;
					case 103:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -95:
						break;
					case 105:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -96:
						break;
					case 107:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -97:
						break;
					case 109:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -98:
						break;
					case 111:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -99:
						break;
					case 113:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -100:
						break;
					case 115:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -101:
						break;
					case 117:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -102:
						break;
					case 119:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -103:
						break;
					case 121:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -104:
						break;
					case 123:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -105:
						break;
					case 125:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -106:
						break;
					case 127:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -107:
						break;
					case 129:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -108:
						break;
					case 200:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -109:
						break;
					case 218:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -110:
						break;
					case 225:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -111:
						break;
					case 227:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -112:
						break;
					case 228:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -113:
						break;
					case 229:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -114:
						break;
					case 230:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -115:
						break;
					case 231:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -116:
						break;
					case 232:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -117:
						break;
					case 233:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -118:
						break;
					case 234:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -119:
						break;
					case 235:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -120:
						break;
					case 236:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -121:
						break;
					case 237:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -122:
						break;
					case 238:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -123:
						break;
					case 239:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -124:
						break;
					case 240:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -125:
						break;
					case 241:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -126:
						break;
					case 242:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -127:
						break;
					case 243:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -128:
						break;
					case 244:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -129:
						break;
					case 245:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -130:
						break;
					case 246:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -131:
						break;
					case 247:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -132:
						break;
					case 248:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -133:
						break;
					case 249:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -134:
						break;
					case 250:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -135:
						break;
					case 251:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -136:
						break;
					case 252:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -137:
						break;
					case 253:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -138:
						break;
					case 254:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -139:
						break;
					case 255:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -140:
						break;
					case 256:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -141:
						break;
					case 257:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -142:
						break;
					case 258:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -143:
						break;
					case 259:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -144:
						break;
					case 260:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -145:
						break;
					case 263:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -146:
						break;
					case 264:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -147:
						break;
					case 265:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -148:
						break;
					case 266:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -149:
						break;
					case 267:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -150:
						break;
					case 268:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -151:
						break;
					case 269:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -152:
						break;
					case 270:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -153:
						break;
					case 271:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -154:
						break;
					case 272:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -155:
						break;
					case 273:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -156:
						break;
					case 274:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -157:
						break;
					case 275:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -158:
						break;
					case 276:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -159:
						break;
					case 277:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -160:
						break;
					case 278:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -161:
						break;
					case 279:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -162:
						break;
					case 280:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -163:
						break;
					case 281:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -164:
						break;
					case 282:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -165:
						break;
					case 283:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -166:
						break;
					case 285:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -167:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
