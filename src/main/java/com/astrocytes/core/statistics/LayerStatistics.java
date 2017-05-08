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

import com.astrocytes.application.widgets.primitives.SimpleLine;
import com.astrocytes.core.primitives.Point;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayerStatistics {
    private Map<Integer, Integer> horizontalLayers = new HashMap<Integer, Integer>();

    private void calculateAstrocytesInLayers(List<Point> astrocyteCenters, List<SimpleLine> layers) {
        for (int i = 0; i < layers.size() + 1; i++) {
            horizontalLayers.put(i, 0);
        }

        for (Point center : astrocyteCenters) {
            int numberOfLayer = 0;
            for (SimpleLine layer : layers) {
                if (layer.getyEnd() > center.getX()) {
                    break;
                }
                numberOfLayer++;
            }
            horizontalLayers.put(numberOfLayer, horizontalLayers.get(numberOfLayer) + 1);
        }
    }

    public boolean saveLayerStatisticsToXls(List<Point> astrocyteCenters, List<SimpleLine> layers, File fileToSave) {
        calculateAstrocytesInLayers(astrocyteCenters, layers);
        try {
            FileOutputStream outputStream = new FileOutputStream(fileToSave);

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Astrocytes By Layers");

            HSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Layer #");
            headerRow.createCell(1).setCellValue("Astrocytes Count");

            for (Map.Entry<Integer, Integer> count : horizontalLayers.entrySet()) {
                HSSFRow row = sheet.createRow(count.getKey() + 1);
                row.createCell(0).setCellValue(count.getKey() + 1);
                row.createCell(1).setCellValue(count.getValue());
            }

            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
