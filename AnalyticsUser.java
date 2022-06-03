import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalyticsUser
{
    public static void main(String args[]) throws Exception
    {
        AnalyticsService a = new AnalyticsService();
        Double[][] initialArray, transposedArr;
        int oRowCount, oColCount, tRowCount, tColCount;
        a.readStoreFile("dataset3.csv");
        
        initialArray = a.getInitialArr();
        oRowCount = a.getORow();
        oColCount = a.getOCol();
        a.rowAnalytics(initialArray, oRowCount, oColCount);
        a.colAnalytics(initialArray, oRowCount, oColCount);
        a.overallAnalytics(initialArray, oRowCount, oColCount);
        a.col1Analytics(oRowCount);
        
        a.transposeArr(initialArray);
        transposedArr = a.getTranArr();
        tRowCount = a.getTRow();
        tColCount = a.getTCol();
        a.rowAnalytics(transposedArr, tRowCount, tColCount);
        a.colAnalytics(transposedArr, tRowCount, tColCount);
        a.overallAnalytics(transposedArr, tRowCount, tColCount);
        a.col1Analytics(tRowCount);
   }
}
