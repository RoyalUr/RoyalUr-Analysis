package com.sothatsit.royalur.analysis.reporting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Can be used to produce formatted markdown tables.
 *
 * @author Paddy Lamont
 */
public class TableGenerator {

   public final String[] headings;
   private final List<String[]> rows = new ArrayList<>();

    public TableGenerator(String... headings) {
        if (headings.length == 0)
            throw new IllegalArgumentException("Table must have at least one column");

        this.headings = headings;
    }

    /**
     * Adds the given row of values in {@param values} to this table.
     * @return {@code this}
     */
    public TableGenerator addRow(Object... values) {
        String[] valueStrings = new String[values.length];
        for (int index = 0; index < values.length; ++index) {
            valueStrings[index] = Objects.toString(values[index]);
        }
        return addRow(valueStrings);
    }

    /**
     * Adds the given row of values in {@param values} to this table.
     * @return {@code this}
     */
    public TableGenerator addRow(String... values) {
        if (values.length != headings.length) {
            throw new IllegalArgumentException(
                    "There must be the same number of values in each row as there are headings");
        }
        rows.add(values);
        return this;
    }

    /**
     * @return the generated markdown table.
     */
    public String generate() {
        // Determine the width of each column.
        int[] colWidths = new int[headings.length];
        for (int index = 0; index < headings.length; ++index) {
            colWidths[index] = headings[index].length();
        }
        for (String[] row : rows) {
            for (int index = 0; index < row.length; ++index) {
                colWidths[index] = Math.max(row[index].length(), colWidths[index]);
            }
        }

        // Generate the table.
        StringBuilder builder = new StringBuilder();
        appendRow(builder, headings, colWidths, ' ');

        String[] blankRow = new String[headings.length];
        Arrays.fill(blankRow, "");
        appendRow(builder, blankRow, colWidths, '-');

        for (String[] row : rows) {
            appendRow(builder, row, colWidths, ' ');
        }
        return builder.toString();
    }

    /**
     * Appends the row of values in {@param row} to {@param builder}, followed by a newline.
     */
    private static void appendRow(StringBuilder builder, String[] row, int[] colWidths, char padChar) {
        for (int index = 0; index < row.length; ++index) {
            builder.append(index == 0 ? "| " : " ");
            builder.append(ReportFormatter.pad(row[index], colWidths[index], padChar));
            builder.append(" |");
        }
        builder.append("\n");
    }
}
