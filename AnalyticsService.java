import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.DecimalFormat;
import java.math.RoundingMode;


public class AnalyticsService 
{
    Double [][] initialArray;
    DecimalFormat df = new DecimalFormat("##0.00");
    
    int oRowCount = 0;
    int oColCount = 0;
    int tRowCount, tColCount;
    int total_count;
    Double [] overallArr;
    String[][] colInfo;
    Double [] col1;
    Double [][] transposedArr;
    int count;
    Double sum, avg, median, min, max, range, std_dev;
    Double[] mode;
    
    public void readStoreFile(String fileName) throws Exception
    {
        System.out.println("SOURCE INPUT...");
        File file = new File(fileName);
        Scanner sc1 = new Scanner(file);
        int row_count = 0;
        int col_count = 0;
	df.setRoundingMode(RoundingMode.DOWN);
        
        // Count how many inputs in a row
        String row1 = sc1.nextLine();
        Scanner sc2 = new Scanner(row1);
	sc2.useDelimiter(",");
        row_count++;
	String rowInput;

        while(sc2.hasNext())
        {
	    rowInput = sc2.next();
	    col_count++;              
	    if(rowInput.isEmpty()){
		col_count++;
		continue;
	    }
        }

        // Count how many rows there are
        while(sc1.hasNextLine())
        {
            row_count++;
            sc1.nextLine();
        }
        Scanner sc3 = new Scanner(new BufferedReader(new FileReader(fileName)));

        Double [][] myArray = new Double[row_count][col_count];
	int numLines = 0;
        while(sc3.hasNextLine())
        {
            String line = sc3.nextLine();
            String [] split = new String[col_count];
	    split = line.split(",");

            for (int j=0; j<col_count; j++)
            {
                if(split[j].trim().isEmpty()){
                    System.out.print(",");
                }else{
   		    myArray[numLines][j] = Double.parseDouble(split[j].trim());
                    if(j == col_count-1 && numLines == row_count-1){
                        System.out.print(df.format(myArray[numLines][j]));
                    }else{
                        System.out.print(df.format(myArray[numLines][j]) + ",");
                    }
                }
            }
	    numLines++;
            System.out.println();
        }
        sc1.close();
        this.initialArray = myArray.clone();
        oRowCount = row_count;
        oColCount = col_count;
        total_count = row_count * col_count;
    }
    
    public void computeAnalytics(Double[] array, int length)
    {
        // Compute count, sum, avg, median, mode, min, max, range, std_dev
        // Find count, sum, and average of array passed
        count = 0;
        sum = 0.0;
        for(int i = 0; i < length; i++)
        {
            if(array[i] != null)
            {
                count++;
                sum += array[i];
            }
        }
        avg = sum / count;
        
        // Find mode and frequency of mode
        int highestFreq = 0;
        int numOfModes = 0;
        Double tempMode[] = new Double[length];
        for(int i = 0; i < length-1; i++){
            if(array[i] != null){
                int freq = 0;
                for(int j = i + 1; j < length; j++){
                    if(array[j] != null){
                        if(Double.compare(array[i], array[j]) == 0){
                            freq++;
                        }
                    }
                }
                if(highestFreq < freq){
                    highestFreq = freq;
                    numOfModes = 1;
                    tempMode[0] = array[i];
                }else if(highestFreq == freq && freq != 0){
                    numOfModes++;
                    tempMode[numOfModes-1] = array[i];
                }
            }
        }
        mode = new Double[numOfModes];
        for(int i = 0; i < numOfModes; i++){
            mode[i] = tempMode[i];
        }
        
        // Sort the array to find median, max, and min
        Double[] sortedArr = new Double[length];
        int noNullIndex = 0;
        for(int i = 0; i < length; i++){
            if(array[i] == null){
            }else{
                sortedArr[noNullIndex] = array[i];
                noNullIndex++;
            }
        }
        for(int i = 0; i < noNullIndex-1 ; i++){
            for(int j = 0; j < noNullIndex-i-1; j++){
                if(sortedArr[j+1]<sortedArr[j]){
                    Double temp = sortedArr[j];
                    sortedArr[j] = sortedArr[j+1];
                    sortedArr[j+1] = temp;
                }
            }
        }
        
        // Find the median
        if(noNullIndex % 2 == 0){
            median = (sortedArr[noNullIndex/2] + sortedArr[(noNullIndex/2)-1]) / 2;
        }else if(noNullIndex % 2 == 1){
            if(noNullIndex/2 == 0){
                median = sortedArr[0];
            }else{
                median = sortedArr[(noNullIndex/2)-1];
            }
        }
        min = sortedArr[0];
        max = sortedArr[noNullIndex-1];
        range = max - min;
        
        // Find the standard deviation
        Double sqDiffSum = 0.0;
        for(int i = 0; i < noNullIndex; i++){
            sqDiffSum += Math.pow((sortedArr[i]-avg), 2);
        }
        std_dev = Math.sqrt(sqDiffSum/noNullIndex);
    }
    
    public void rowAnalytics(Double[][] array, int row_count, int col_count){
        Double[] currentRow = new Double[col_count];
        System.out.println("\nCOMPUTATION...");
        for(int i = 0; i < row_count; i++){
            for(int j = 0; j < col_count; j++){
                currentRow[j] = array[i][j];
                if(array[i][j] == null){
                    System.out.print("\t");
                }else{
                    System.out.print("\t" + df.format(array[i][j]));
                }
            }
            computeAnalytics(currentRow, col_count);
            System.out.println("\t||Count="+count+"||Sum="+df.format(sum)+"||Avg="+df.format(avg)
                +"||Median="+df.format(median)+"||Mode="+Arrays.toString(mode)+"||Min="
                +min+"||Max="+max+"||Range="+df.format(range)+"||Std_Dev="+df.format(std_dev));
        }
    }

    // For each analytic, an array of results (one for each column) is made so that the format is correct
    // All the analytics of the first column is saved in a different array, to be printed later
    public void colAnalytics(Double[][] array, int row_count, int col_count){
        colInfo = new String[9][col_count+1];
        Double[] currentCol = new Double[row_count];
        col1 = new Double[row_count];
        System.out.print("\t");
        for(int i = 0; i < col_count; i++){
            System.out.print("--------");
        }
        System.out.println();
        colInfo[0][0] = "COUNT.:";
        colInfo[1][0] = "SUM...:";
        colInfo[2][0] = "AVG...:";
        colInfo[3][0] = "MEDIAN:";
        colInfo[4][0] = "MODE..:";
        colInfo[5][0] = "MIN...:";
        colInfo[6][0] = "MAX...:";
        colInfo[7][0] = "RANGE.:";
        colInfo[8][0] = "STDDEV:";
        
        for(int i = 0; i < col_count; i++){
            for(int j = 0; j < row_count; j++){
                currentCol[j] = array[j][i];
                if(i == 0){
                    col1[j] = array[j][i];
                }
            }
            computeAnalytics(currentCol, row_count);
            colInfo[0][i+1] = String.valueOf(count);
            colInfo[1][i+1] = df.format(sum);
            colInfo[2][i+1] = df.format(avg);
            colInfo[3][i+1] = df.format(median);
            colInfo[4][i+1] = Arrays.toString(mode);
            colInfo[5][i+1] = String.valueOf(min);
            colInfo[6][i+1] = String.valueOf(max);
            colInfo[7][i+1] = df.format(range);
            colInfo[8][i+1] = df.format(std_dev);
        }
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < col_count+1; j++){
                if(j == 0){
                    System.out.print(colInfo[i][j]);
                }else{
                    System.out.print("\t" + colInfo[i][j]);
                }
            }
            System.out.println();
        }
    }
    
    //overallAnalytics()
    public void overallAnalytics(Double[][] array, int row_count, int col_count){
        overallArr = new Double[total_count];
        int index = 0;
        System.out.print("\n\n--------OVERALL-------------\nData points: ");
        for(int i = 0; i < row_count; i++){
            for(int j = 0; j < col_count; j++){
                overallArr[index] = array[i][j];
                index++;
            }
        }
        System.out.println(Arrays.toString(overallArr));
        computeAnalytics(overallArr, index);
        System.out.println("Count......: "+count+"\nSum........: "+df.format(sum)
            +"\nAverage....: "+df.format(avg)+"\nMedian.....: "+df.format(median)+"\nMode.......: "
            +Arrays.toString(mode)+"\nMinimum....: "+min+"\nMaximum....: "
            +max+"\nRange......: "+df.format(range)+"\nStd.Dev....: "+df.format(std_dev));
    }
    
    public void col1Analytics(int row_count){
        System.out.print("\n------COLUMN 1 AGAIN----------\nData points: " 
            + Arrays.toString(col1));
        computeAnalytics(col1, row_count);
        System.out.println("\nCount......: "+count+"\nSum........: "+df.format(sum)
            +"\nAverage....: "+df.format(avg)+"\nMedian.....: "+df.format(median)+"\nMode.......: "
            +Arrays.toString(mode)+"\nMinimum....: "+min+"\nMaximum....: "
            +max+"\nRange......: "+df.format(range)+"\nStd.Dev....: "+df.format(std_dev));
    }
    
    public void transposeArr(Double[][] array){
        // Change local row_count, col_count to each other
        // Make initialArray == to transposeArr[][]
        System.out.println("\n\n\nTRANSPOSED...");
        tRowCount = oColCount;
        tColCount = oRowCount;
        transposedArr = new Double[tRowCount][tColCount];
         for(int i = 0; i < tRowCount; i++){
            for(int j = 0; j < tColCount; j++){
                transposedArr[i][j] = array[j][i];
                System.out.print(transposedArr[i][j] + "\t");
            }
            System.out.println();
         }
    }
    
    public Double[][] getInitialArr(){
        return initialArray;
    }
    
    public Double[][] getTranArr(){
        return transposedArr;
    }
    
    public int getORow(){
        return oRowCount;
    }
    public int getOCol(){
        return oColCount;
    }
    public int getTRow(){
        return tRowCount;
    }
    public int getTCol(){
        return tColCount;
    }
}