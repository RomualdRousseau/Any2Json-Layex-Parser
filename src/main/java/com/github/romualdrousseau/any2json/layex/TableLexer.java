package com.github.romualdrousseau.any2json.layex;

import java.util.ArrayList;

import com.github.romualdrousseau.any2json.Table;
import com.github.romualdrousseau.any2json.base.BaseCell;
import com.github.romualdrousseau.any2json.base.BaseRow;
import com.github.romualdrousseau.any2json.base.BaseTable;

public class TableLexer implements Lexer<BaseCell, TableLexer.Cursor> {

    class Cursor {

        public Cursor(final int colIndex, final int rowIndex) {
            this.colIndex = colIndex;
            this.rowIndex = rowIndex;
        }

        public int getColIndex() {
            return this.colIndex;
        }

        public int getRowIndex() {
            return this.rowIndex;
        }

        private final int colIndex;
        private final int rowIndex;
    }

    public TableLexer(final Table table, int rowOffset) {
        this.stack = new ArrayList<Cursor>();
        this.table = (BaseTable) table;
        this.colIndex = 0;
        this.rowIndex = rowOffset;
    }

    @Override
    public BaseCell read() {
        if (this.rowIndex >= this.table.getNumberOfRows()) {
            return BaseCell.EndOfStream;
        }

        if (this.colIndex >= this.table.getNumberOfColumns()) {
            this.colIndex = 0;
            this.rowIndex++;
            return BaseCell.EndOfRow;
        }

        final BaseRow row = this.table.getRowAt(this.rowIndex);
        if (row.isEmpty()) {
            this.colIndex = 0;
            this.rowIndex++;
            return BaseCell.EndOfRow;
        }

        final BaseCell cell = row.getCellAt(this.colIndex);
        this.colIndex += cell.getMergedCount();

        return cell;
    }

    @Override
    public BaseCell peek() {
        if (this.rowIndex >= this.table.getNumberOfRows()) {
            return BaseCell.EndOfStream;
        }

        if (this.colIndex >= this.table.getNumberOfColumns()) {
            return BaseCell.EndOfRow;
        }

        final BaseRow row = this.table.getRowAt(this.rowIndex);
        if (row.isEmpty()) {
            return BaseCell.EndOfRow;
        }

        return row.getCellAt(colIndex);
    }

    @Override
    public void push() {
        this.stack.add(new Cursor(this.colIndex, this.rowIndex));
    }

    @Override
    public Cursor pop() {
        return this.stack.remove(this.stack.size() - 1);
    }

    @Override
    public void seek(final Cursor c) {
        this.colIndex = c.getColIndex();
        this.rowIndex = c.getRowIndex();
    }

    private final ArrayList<Cursor> stack;
    private final BaseTable table;
    private int colIndex;
    private int rowIndex;
}
