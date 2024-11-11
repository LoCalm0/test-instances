package indi.LoCalm.sql;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * @author LoCalm
 */
public enum Identity {

	EQUAL,
	NOT_EQUAL,

	LIKE,
	NOT_LIKE,
	LIKE_LEFT,
	LIKE_RIGHT,

	GE,
	LE,
	GT,
	LT,

	DATE_GE,
	DATE_GT,

	DATE_EQUAL,

	DATE_LE,
	DATE_LT,

	NULL,
	NOT_NULL,

	NUMBER_IN,
	STRING_IN,
	DECIMAL_IN,

	BETWEEN;


	@Nullable
	@Contract(pure = true)
	public static Identity of(String identity) {
		switch (identity) {
			case "equal":
				return EQUAL;
			case "notequal":
				return NOT_EQUAL;
			case "like":
				return LIKE;
			case "notlike":
				return NOT_LIKE;
			case "likeleft":
				return LIKE_LEFT;
			case "likeright":
				return LIKE_RIGHT;
			case "ge":
				return GE;
			case "le":
				return LE;
			case "gt":
				return GT;
			case "lt":
				return LT;
			case "datege":
				return DATE_GE;
			case "dategt":
				return DATE_GT;
			case "dateequal":
				return DATE_EQUAL;
			case "datele":
				return DATE_LE;
			case "datelt":
				return DATE_LT;
			case "null":
				return NULL;
			case "notnull":
				return NOT_NULL;
			case "numberin":
				return NUMBER_IN;
			case "stringin":
				return STRING_IN;
			case "decimalin":
				return DECIMAL_IN;
			case "between":
				return BETWEEN;
			default:
				return null;
		}
	}

}
