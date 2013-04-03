package de.uni_koblenz.jgralab.java_extractor.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class UnicodeEscapeAwareInputStream extends InputStream {

	/**
	 * -2 = no input was read <br>
	 * -1 = end of input was reached <br>
	 * else = next character
	 */
	private int nextChar = -2;

	private boolean isOddNumberOfBackslashes = false;

	private InputStream wrappedInputStream = null;

	private Charset charset = null;

	private ByteBuffer currentUnicodeChar = null;

	public UnicodeEscapeAwareInputStream(InputStream inputStream,
			String encoding) {
		wrappedInputStream = inputStream;
		charset = Charset.forName(encoding);
	}

	@Override
	public int read() throws IOException {
		if (nextChar == -2) {
			nextChar = wrappedInputStream.read();
			if (nextChar == '\\') {
				isOddNumberOfBackslashes = true;
			}
		}
		if (nextChar == -1) {
			return nextChar;
		}
		int currentChar = -1;
		if (currentUnicodeChar == null) {
			currentChar = wrappedInputStream.read();
			if (isOddNumberOfBackslashes && nextChar == '\\'
					&& currentChar == 'u') {
				int nextUnicodeChar = readUnicodeEscape();
				if (Character.isSurrogate((char) nextUnicodeChar)) {
					// this is one unicode character consisting of two chars
					wrappedInputStream.mark(3);
					nextChar = wrappedInputStream.read();
					if (nextChar != '\\') {
						wrappedInputStream.reset();
					}
					nextChar = wrappedInputStream.read();
					if (nextChar != 'u') {
						wrappedInputStream.reset();
					}
					int secondUnicodeChar = readUnicodeEscape();
					currentUnicodeChar = charset
							.encode(new String(new char[] {
									(char) nextUnicodeChar,
									(char) secondUnicodeChar }));
				} else {
					currentUnicodeChar = charset.encode(new String(
							new char[] { (char) nextUnicodeChar }));
				}
				nextChar = getNextUnicodeByte();
				if (!currentUnicodeChar.hasRemaining()) {
					currentUnicodeChar = null;
					currentChar = wrappedInputStream.read();
				} else {
					currentChar = getNextUnicodeByte();
					if (!currentUnicodeChar.hasRemaining()) {
						currentUnicodeChar = null;
					}

				}
				isOddNumberOfBackslashes = false;
			}
			if (currentChar == '\\') {
				isOddNumberOfBackslashes = !isOddNumberOfBackslashes;
			} else {
				isOddNumberOfBackslashes = false;
			}
		} else {
			currentChar = getNextUnicodeByte();
			if (!currentUnicodeChar.hasRemaining()) {
				currentUnicodeChar = null;
			}
		}
		int returnChar = nextChar;
		nextChar = currentChar;
		return returnChar;
	}

	private int getNextUnicodeByte() {
		int nextByte = currentUnicodeChar.get();
		// -256 == 0b11111111_11111111_11111111_00000000
		// 255 == 0b00000000_00000000_00000000_11111111
		return nextByte & 255;
	}

	private int readUnicodeEscape() throws IOException {
		int firstHex, secondHex, thirdHex, fourthHex;
		do {
			firstHex = wrappedInputStream.read();
		} while (firstHex == 'u');
		if (isHexDigit(firstHex)) {
			throwIOException(1, firstHex);
		}
		secondHex = wrappedInputStream.read();
		if (isHexDigit(secondHex)) {
			throwIOException(1, secondHex);
		}
		thirdHex = wrappedInputStream.read();
		if (isHexDigit(thirdHex)) {
			throwIOException(1, thirdHex);
		}
		fourthHex = wrappedInputStream.read();
		if (isHexDigit(fourthHex)) {
			throwIOException(1, fourthHex);
		}
		return Integer.parseInt("" + ((char) firstHex) + ((char) secondHex)
				+ ((char) thirdHex) + ((char) fourthHex), 16);
	}

	private boolean isHexDigit(int firstHex) {
		return firstHex == -1
				|| !((firstHex >= 'a' && firstHex <= 'f')
						|| (firstHex >= 'A' && firstHex <= 'F') || (firstHex >= '0' && firstHex <= '9'));
	}

	private void throwIOException(int position, int unexpectedChar)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		if (unexpectedChar == -1) {
			sb.append("The current unicode escape ended unexpectedly after the ");
		} else {
			sb.append("Unexpacted character " + ((char) unexpectedChar)
					+ " at ");
		}
		switch (position) {
		case 1:
			sb.append("first");
			break;
		case 2:
			sb.append("second");
			break;
		case 3:
			sb.append("third");
			break;
		case 4:
			sb.append("fourth");
			break;
		default:
			sb.append("UNKNOWN");
		}
		if (unexpectedChar == -1) {
			sb.append(" character.");
		} else {
			sb.append(" position in the current unicode escape.");
		}
		throw new IOException(sb.toString());
	}

	@Override
	public void close() throws IOException {
		wrappedInputStream.close();
		super.close();
	}

}
