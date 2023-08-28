package com.red.plus.blue.oprCombatSimulator.table;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public class Table<COLUMN_T, ROW_T, CELL_T> {

    @Builder.Default
    protected String title = "Table";

    @Builder.Default
    protected Function<COLUMN_T, String> columnHeaderGenerator = Object::toString;

    @Builder.Default
    protected Function<Integer, COLUMN_T> columnGenerator = index -> null;

    @Builder.Default
    protected Function<Integer, ROW_T> rowGenerator = index -> null;

    @Builder.Default
    protected Function<ROW_T, String> rowHeaderGenerator = Object::toString;

    @Builder.Default
    protected BiFunction<ROW_T, COLUMN_T, CELL_T> cellMapper = (row, column) -> null;

    @Builder.Default
    protected List<List<CELL_T>> values = List.of(new ArrayList<>());

    public CELL_T get(int row, int column) {
        return values.get(row).get(column);
    }

    public Table<COLUMN_T, ROW_T, CELL_T> compute(int rows, int columns) {
        values = new ArrayList<>();
        IntStream.range(0, rows)
                .forEach(row -> {
                    List<CELL_T> columnValues = new ArrayList<>();
                    final var rowObject = rowGenerator.apply(row);
                    IntStream.range(0, columns)
                            .forEach(column -> {
                                final var columnObject = columnGenerator.apply(column);
                                columnValues.add(cellMapper.apply(rowObject, columnObject));
                            });
                    values.add(columnValues);
                });
        return this;
    }

    public void print(int columnWidth, Function<CELL_T, String> cellToString) {
        System.out.println("------ " + this.title + " ------");
        final var stringFormat = "%" + (columnWidth - 1) + "s";
        final var tableHeader = Stream.concat(
                Stream.of(String.format(stringFormat, "")),
                IntStream.range(0, values.get(0).size())
                        .mapToObj(columnGenerator::apply)
                        .map(columnHeaderGenerator)
                        .map(str -> String.format(stringFormat, str))
        ).toList();
        System.out.println(String.join(",", tableHeader));

        IntStream.range(0, values.size()).forEach(rowIndex -> {
            final var line = new ArrayList<String>();
            final var rowHeader = String.format(stringFormat, rowHeaderGenerator.apply(rowGenerator.apply(rowIndex)));
            line.add(rowHeader);
            var row = values.get(rowIndex);
            IntStream.range(0, row.size()).forEach(columnIndex -> {
                final var cell = row.get(columnIndex);
                line.add(String.format(stringFormat, cellToString.apply(cell)));
            });
            System.out.println(String.join(",", line));
        });
    }

}
