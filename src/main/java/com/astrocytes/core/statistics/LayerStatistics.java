/*
 * Copyright (c) Lobachevsky University, 2017. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal with the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * Developed by: Komarov Nikolay.
 */
package com.astrocytes.core.statistics;

import com.astrocytes.core.CoreConstants;
import com.astrocytes.core.primitives.Point;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class LayerStatistics {

    public boolean saveLayerStatisticsToXls(Map<Integer, List<Point>> layers,
                                            List<Point> astrocyteCenters, List<Point> neuronsCenters, File fileToSave) {
        try {
            FileOutputStream outputStream = new FileOutputStream(fileToSave);

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(CoreConstants.XLS_SPREADSHEET_TITLE);

            HSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue(CoreConstants.XLS_LAYERS_TITLE);

            for (int i = 0; i < layers.size() - 1; i++) {
                HSSFRow row = sheet.createRow(i + 1);
                HSSFCell cell = row.createCell(0);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(String.valueOf(i < 1 ? i + 1 : (i == 1 ? "2 - 3" : i + 2)));
            }

            if (astrocyteCenters != null) {
                headerRow.createCell(1).setCellValue(CoreConstants.XLS_ASTROCYTES_TITLE);

                for (Map.Entry<Integer, Integer> count : count(astrocyteCenters, layers).entrySet()) {
                    HSSFRow row = sheet.getRow(count.getKey() + 1);
                    HSSFCell cell = row.createCell(1);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(count.getValue());
                }
            }

            if (neuronsCenters != null) {
                int colIdx = astrocyteCenters != null ? 2 : 1;
                headerRow.createCell(colIdx).setCellValue(CoreConstants.XLS_NEURONS_TITLE);

                for (Map.Entry<Integer, Integer> count : count(neuronsCenters, layers).entrySet()) {
                    HSSFRow row = sheet.getRow(count.getKey() + 1);
                    HSSFCell cell = row.createCell(astrocyteCenters != null ? 2 : 1);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(count.getValue());
                }
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Map<Integer, Integer> count(List<Point> objs, Map<Integer, List<Point>> layers) {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        Collections.sort(objs, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return p1.getX() - p2.getX();
            }
        });

        for (int i = 0; i < layers.size() - 1; i++) {
            result.put(i, countLayer(objs, layers.get(i), layers.get(i + 1)));
        }

        return result;
    }

    private int countLayer(List<Point> objs, List<Point> first, List<Point> second) {
        if (objs.size() == 0) return 0;

        int result = 0;

        for (int x = 0; x < first.size(); x++) {
            int fy = first.get(x).getY();
            int sy = second.get(x).getY();

            int curIter = 0;
            while (curIter < objs.size() && objs.get(curIter).getX() <= x) {
                int objX = objs.get(curIter).getX();
                int y = objs.get(curIter).getY();

                if (objX == x && fy <= y && y < sy) {
                    result++;
                }

                curIter++;
            }
        }

        return result;
    }
}
