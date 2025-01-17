package com.topideal.order.tools;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelConvertHTMLUtils {

    public static String getExcelInfo(Workbook wb, boolean isWithStyle) {
        StringBuffer sb = new StringBuffer("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset='utf-8'> ");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<table style='border-collapse:collapse;' width='80%'>");

        //获取第一个Sheet的内容
        Sheet sheet = wb.getSheetAt(0);
        Map<String, String> map[] = getRowSpanColSpanMap(sheet);

        int lastRowNum = sheet.getLastRowNum();
        for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                sb.append("<tr><td >  </td></tr>");
                continue;
            }
            sb.append("<tr>");
            int lastColNum = row.getLastCellNum();
            for (int colNum = 0; colNum < lastColNum; colNum++) {
                Cell cell = row.getCell(colNum);
                if (cell == null) {
                    sb.append("<td> </td>");
                    continue;
                }

                String stringValue = getCellValue(cell);
                if (map[0].containsKey(rowNum + "," + colNum)) {
                    String pointString = map[0].get(rowNum + "," + colNum);
                    map[0].remove(rowNum + "," + colNum);
                    int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
                    int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
                    int rowSpan = bottomeRow - rowNum + 1;
                    int colSpan = bottomeCol - colNum + 1;
                    sb.append("<td rowspan= '" + rowSpan + "' colspan= '" + colSpan + "' ");
                } else if (map[1].containsKey(rowNum + "," + colNum)) {
                    map[1].remove(rowNum + "," + colNum);
                    continue;
                } else {
                    sb.append("<td ");
                }

                //判断是否需要样式
                if (isWithStyle) {
                    dealExcelStyle(wb, sheet, cell, sb);
                }
                sb.append(">");
                if (stringValue == null || "".equals(stringValue.trim())) {
                    sb.append("   ");
                } else {
                    // 将ascii码为160的空格转换为html下的空格（ ）
                    sb.append(stringValue.replace(String.valueOf((char) 160), " "));
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    /**
     * 获取sheet中单元格的合并情况
     *
     * @param sheet
     * @return
     */
    private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {
        Map<String, String> map0 = new LinkedHashMap<>();
        Map<String, String> map1 = new LinkedHashMap<>();
        int mergedNum = sheet.getNumMergedRegions();

        for (int i = 0; i < mergedNum; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int topRow = range.getFirstRow();
            int topCol = range.getFirstColumn();
            int bottomRow = range.getLastRow();
            int bottomCol = range.getLastColumn();
            map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
            int tempRow = topRow;
            while (tempRow <= bottomRow) {
                int tempCol = topCol;
                while (tempCol <= bottomCol) {
                    map1.put(tempRow + "," + tempCol, "");
                    tempCol++;
                }
                tempRow++;
            }
            map1.remove(topRow + "," + topCol);
        }
        Map[] map = {map0, map1};
        return map;
    }

    /**
     * 获取表格单元格Cell内容
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        String result = new String();
        switch (cell.getCellType()) {
            case NUMERIC:// 数字类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
                break;
            case STRING:// String类型
                result = cell.getRichStringCellValue().toString();
                break;
            case BLANK:
                result = "";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    /**
     * 处理表格样式
     *
     * @param wb
     * @param sheet
     * @param cell
     * @param sb
     */
    private static void dealExcelStyle(Workbook wb, Sheet sheet, Cell cell, StringBuffer sb) {
        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle != null) {
            //单元格内容的水平对齐方式
            sb.append("align='" + convertAlignToHtml(cellStyle.getAlignment()) + "' ");
            //单元格中内容的垂直排列方式
            sb.append("valign='" + convertVerticalAlignToHtml(cellStyle.getVerticalAlignment()) + "' ");

            if (wb instanceof XSSFWorkbook) {
                XSSFFont xf = ((XSSFCellStyle) cellStyle).getFont();
                sb.append("style='");
                if (xf.getBold()) {
                    sb.append("font-weight: bold;"); // 字体加粗
                }

                if (xf.getItalic()) {
                    sb.append("font-style: italic;");
                }

                // 字体大小
                sb.append("font-size: " + xf.getFontHeightInPoints() + "px;");

                //字体系列
                sb.append("font-family: " + xf.getFontName() + ";");

                short height = cell.getRow().getHeight();
                sb.append("height:" + height/20 + "px;");

                int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
                sb.append("width:" + columnWidth + "px;");

                XSSFColor xc = xf.getXSSFColor();
                if (xc != null && !"".equals(xc)) {
                    // 字体颜色
                    sb.append("color:#" + xc.getARGBHex().substring(2) + ";");
                }
                XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();

                if (bgColor != null && !"".equals(bgColor)) {
                    // 背景颜色
                    sb.append("background-color:#" + bgColor.getARGBHex().substring(2) + ";");
                }

                sb.append(getBorderStyle(0, cellStyle.getBorderTop().getCode(), ((XSSFCellStyle) cellStyle).getTopBorderXSSFColor()));
                sb.append(getBorderStyle(1, cellStyle.getBorderRight().getCode(), ((XSSFCellStyle) cellStyle).getRightBorderXSSFColor()));
                sb.append(getBorderStyle(2, cellStyle.getBorderBottom().getCode(), ((XSSFCellStyle) cellStyle).getBottomBorderXSSFColor()));
                sb.append(getBorderStyle(3, cellStyle.getBorderLeft().getCode(), ((XSSFCellStyle) cellStyle).getLeftBorderXSSFColor()));
            } else if (wb instanceof HSSFWorkbook) {
                HSSFFont hf = ((HSSFCellStyle) cellStyle).getFont(wb);
                short fontColor = hf.getColor();
                sb.append("style='");
                HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式
                HSSFColor hc = palette.getColor(fontColor);
                if (hf.getBold()) {
                    sb.append("font-weight: bold;"); // 字体加粗
                }
                // 字体大小
                sb.append("font-size: " + hf.getFontHeight() / 2 + "%;");
                String fontColorStr = convertToStardColor(hc);

                if (fontColorStr != null && !"".equals(fontColorStr.trim())) {
                    // 字体颜色
                    sb.append("color:" + fontColorStr + ";");
                }

                short height = cell.getRow().getHeight();
                sb.append("height:" + height/20 + "px;");

                int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
                sb.append("width:" + columnWidth + "px;");
                short bgColor = cellStyle.getFillForegroundColor();
                hc = palette.getColor(bgColor);
                String bgColorStr = convertToStardColor(hc);
                if (bgColorStr != null && !"".equals(bgColorStr.trim())) {
                    // 背景颜色
                    sb.append("background-color:" + bgColorStr + ";");
                }
                sb.append(getBorderStyle(palette, 0, cellStyle.getBorderTop().getCode(), cellStyle.getTopBorderColor()));
                sb.append(getBorderStyle(palette, 1, cellStyle.getBorderRight().getCode(), cellStyle.getRightBorderColor()));
                sb.append(getBorderStyle(palette, 3, cellStyle.getBorderLeft().getCode(), cellStyle.getLeftBorderColor()));
                sb.append(getBorderStyle(palette, 2, cellStyle.getBorderBottom().getCode(), cellStyle.getBottomBorderColor()));
            }
            sb.append("' ");
        }
    }

    /**
     * 单元格内容的水平对齐方式
     *
     * @param alignment
     * @return
     */
    private static String convertAlignToHtml(HorizontalAlignment alignment) {
        String align = "left";
        switch (alignment) {
            case LEFT:
                align = "left";
                break;
            case CENTER:
                align = "center";
                break;
            case RIGHT:
                align = "right";
                break;
            default:
                break;
        }
        return align;
    }

    /**
     * 单元格中内容的垂直排列方式
     *
     * @param verticalAlignment
     * @return
     */
    private static String convertVerticalAlignToHtml(VerticalAlignment verticalAlignment) {
        String valign = "middle";
        switch (verticalAlignment) {
            case BOTTOM:
                valign = "bottom";
                break;
            case CENTER:
                valign = "center";
                break;
            case TOP:
                valign = "top";
                break;
            default:
                break;
        }
        return valign;
    }

    private static String convertToStardColor(HSSFColor hc) {
        StringBuffer sb = new StringBuffer("");
        if (hc != null) {
            sb.append("#");
            for (int i = 0; i < hc.getTriplet().length; i++) {
                sb.append(fillWithZero(Integer.toHexString(hc.getTriplet()[i])));
            }
        }
        return sb.toString();
    }

    private static String fillWithZero(String str) {
        if (str != null && str.length() < 2) {
            return "0" + str;
        }
        return str;
    }

    static String[] bordesr = {"border-top:", "border-right:", "border-bottom:", "border-left:"};
    static String[] borderStyles = {"none", "solid", "solid", "solid", "dashed", "solid ", "solid ", "double", "solid ", "solid ", "solid ", "solid ", "solid", "solid", "solid", "solid", "solid"};

    private static String getBorderStyle(HSSFPalette palette, int b, short s, short t) {
        if (s == 0) return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
        ;
        String borderColorStr = convertToStardColor(palette.getColor(t));
        borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr;
        return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
    }

    private static String getBorderStyle(int b, short s, XSSFColor xc) {
        if (s == 0) return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
        ;
        if (xc != null && !"".equals(xc)) {
            String borderColorStr = xc.getARGBHex();//t.getARGBHex();
            borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr.substring(2);
            return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
        }
        return "";
    }

}
