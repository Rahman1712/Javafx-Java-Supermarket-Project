
package com.ar.numbertoword;

public class ConvertNumToWord {
	private static final String[] UNITS = { "", " one", " two", " three", " four", " five", " six", " seven", " eight",
			" nine" };
	private static final String[] TWODIGITS = { " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen",
			" sixteen", " seventeen", " eighteen", " nineteen" };
	private static final String[] TENMULTIPLES = { "", "", " twenty", " thirty", " fourty", " fifty", " sixty",
			" seventy", " eighty", " ninety" };
	private static final String[] PLACEVALUES = { "", " thousand", " lakh", " crore", " arb", " kharab" };

	public static String convertNumber(long number) {
		String word = "";
		int index = 0;
		boolean firstIteration = true;
		int divisor;
		do {
			divisor = firstIteration ? 1000 : 100; // take 3 or 2 digits based on iteration
			int num = (int) (number % divisor);
			if (num != 0) {
				String str = ConversionForUptoThreeDigits(num);
				word = str + PLACEVALUES[index] + word;
			}
			index++; // next batch of digits
			number = number / divisor;
			firstIteration = false;
		} while (number > 0);
		return word;
	}

	private static String ConversionForUptoThreeDigits(int number) {
		String word = "";
		int num = number % 100;
		if (num < 10) {
			word = word + UNITS[num];
		} else if (num < 20) {
			word = word + TWODIGITS[num % 10];
		} else {
			word = TENMULTIPLES[num / 10] + UNITS[num % 10];
		}
		word = (number / 100 > 0) ? UNITS[number / 100] + " hundred" + word : word;
		return word;
	}

}
