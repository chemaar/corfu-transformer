package corfu;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class POIUtils {

        public static String extractValue(HSSFCell cell, FormulaEvaluator evaluator) {
                String value = "";
                try{
        
                CellValue cellValue = evaluator.evaluate(cell); 
                if (cellValue != null){
                        switch (cellValue.getCellType()) {
                            case Cell.CELL_TYPE_BOOLEAN:
                                value = String.valueOf(cellValue.getBooleanValue());
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                 value = String.valueOf(cellValue.getNumberValue());
                                break;
                            case Cell.CELL_TYPE_STRING:
                                 value = cellValue.getStringValue();
                                break;
                            case Cell.CELL_TYPE_BLANK:
                                value = "";
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                value = "";
                                break;
                            // CELL_TYPE_FORMULA will never happen
                            case Cell.CELL_TYPE_FORMULA: 
                                break;
                        }
                }
                
                }catch(java.lang.ArrayIndexOutOfBoundsException e){
                        
                }
                return value;
        }
        public static String extractValue(HSSFCell cell) {
        String value = "";
        if (cell != null){
                switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_FORMULA:
                        //FIXME: evaluate formula && skip formualae
                        value = String.valueOf(cell.getCellFormula()); 
                        break;

                case HSSFCell.CELL_TYPE_NUMERIC:
                        value = String.valueOf(cell.getNumericCellValue());
                        break;

                case HSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;

                default:
                }
        }
        return value;
        }
}
