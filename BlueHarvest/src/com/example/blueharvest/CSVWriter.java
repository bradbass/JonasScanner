package com.example.blueharvest;

/**
 * @author braba
 *
 */

import java.io.PrintWriter;
import java.io.Writer;

class CSVWriter {

    private final PrintWriter pw;
    private final char separator;
    private final char quotechar;
    private final char escapechar;
    private final String lineEnd;

    private static final char DEFAULT_ESCAPE_CHARACTER = '"';
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE_CHARACTER = '"';
    private static final char NO_QUOTE_CHARACTER = '\u0000';
    private static final char NO_ESCAPE_CHARACTER = '\u0000';
    private static final String DEFAULT_LINE_END = "\n";

    /**
     * Constructs CSVWriter using a comma for the separator.
     *
     * @param writer        the writer to an underlying CSV source.
     */
    public CSVWriter(Writer writer) {
        this(writer, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER,
            DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);
    }

    /**
     * Constructs CSVWriter with supplied separator, quote char, escape char and line ending.
     *
     * @param writer        the writer to an underlying CSV source.
     * @param separator     the delimiter to use for separating entries
     * @param quotechar     the character to use for quoted elements
     * @param escapechar    the character to use for escaping quotechars or escapechars
     * @param lineEnd       the line feed terminator to use
     */
    private CSVWriter(Writer writer, char separator, char quotechar, char escapechar, String lineEnd) {
        this.pw = new PrintWriter(writer);
        this.separator = separator;
        this.quotechar = quotechar;
        this.escapechar = escapechar;
        this.lineEnd = lineEnd;
    }

    /**
     * Writes the next line to the file.
     *
     * @param nextLine      a string array with each comma-separated element as a separate
     *            entry.
     */
    public void writeNext(String[] nextLine) {

        if (nextLine == null)
                return;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nextLine.length; i++) {

            if (i != 0) {
                sb.append(separator);
            }

            String nextElement = nextLine[i];
            if (nextElement == null)
                continue;
            if (quotechar !=  NO_QUOTE_CHARACTER)
                sb.append(quotechar);
            for (int j = 0; j < nextElement.length(); j++) {
                char nextChar = nextElement.charAt(j);
                if (escapechar != NO_ESCAPE_CHARACTER && nextChar == quotechar) {
                        sb.append(escapechar).append(nextChar);
                } else if (escapechar != NO_ESCAPE_CHARACTER && nextChar == escapechar) {
                        sb.append(escapechar).append(nextChar);
                } else {
                    sb.append(nextChar);
                }
            }
            if (quotechar != NO_QUOTE_CHARACTER)
                sb.append(quotechar);
        }

        sb.append(lineEnd);
        pw.write(sb.toString());

    }

// --Commented out by Inspection START (5/15/13 12:04 PM):
//    /**
//     * Flush underlying stream to writer.
//     *
//     * @throws IOException if bad things happen
//     */
//    public void flush() throws IOException {
//
//        pw.flush();
//
//    }
// --Commented out by Inspection STOP (5/15/13 12:04 PM)

    /**
     * Close the underlying stream writer flushing any buffered content.
     *
     *
     */
    public void close() {
        pw.flush();
        pw.close();
    }

}
