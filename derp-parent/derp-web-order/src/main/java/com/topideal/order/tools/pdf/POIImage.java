package com.topideal.order.tools.pdf;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.awt.*;
import java.util.List;

public class POIImage {
    protected Dimension dimension;
    protected byte[] bytes;
    protected ClientAnchor anchor;

    public POIImage getCellImage(Cell cell) {
        Sheet sheet = cell.getSheet();
        if (sheet instanceof HSSFSheet) {
            HSSFSheet hssfSheet = (HSSFSheet) sheet;
            if(hssfSheet.getDrawingPatriarch() == null) {
            	return this;
            }
            List<HSSFShape> shapes = hssfSheet.getDrawingPatriarch().getChildren();
            for (HSSFShape shape : shapes) {
                HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
                if (shape instanceof HSSFPicture) {
                    HSSFPicture pic = (HSSFPicture) shape;
                    PictureData data = pic.getPictureData();
                    String extension = data.suggestFileExtension();
                    int row1 = anchor.getRow1();
                    int col1 = anchor.getCol1();
                    if(row1 == cell.getRowIndex() && col1 == cell.getColumnIndex()){
                        dimension = pic.getImageDimension();
                        this.anchor = anchor;
                        this.bytes = data.getData();
                    }
                }
            }
        }

        if (sheet instanceof XSSFSheet) {
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            if(xssfSheet.getRelations() == null) {
                return this;
            }

            for (POIXMLDocumentPart dr : xssfSheet.getRelations()) {
                if (dr instanceof XSSFDrawing) {
                    XSSFDrawing drawing = (XSSFDrawing) dr;
                    List<XSSFShape> shapes = drawing.getShapes();
                    for (XSSFShape shape : shapes) {
                        XSSFPicture pic = (XSSFPicture) shape;
                        XSSFClientAnchor anchor = pic.getPreferredSize();
                        CTMarker ctMarker = anchor.getFrom();
                        String picIndex = ctMarker.getRow() + "_" + ctMarker.getCol();
                        int row1 = ctMarker.getRow();
                        int col1 = ctMarker.getCol();
                        if(row1 == cell.getRowIndex() && col1 == cell.getColumnIndex()){
                            dimension = pic.getImageDimension();
                            this.anchor = anchor;
                            this.bytes = pic.getPictureData().getData();
                        }
                    }
                }
            }
        }
        return this;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public ClientAnchor getAnchor() {
        return anchor;
    }

    public void setAnchor(ClientAnchor anchor) {
        this.anchor = anchor;
    }
}