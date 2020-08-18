package calculatorpackage;

// in this version matrix will be external, so this class will only include calculation funtions

public class MatrixCalculator {
    public boolean isEchelon(double [][] matrix){
        // WIP
        int pivotIdx = -1;
        for(int i = 0; i < matrix.length; i++ ){
            double tempRow[] = getRow(matrix, i);
            for(int tempPivotIdx = 0; tempPivotIdx < tempRow.length; tempPivotIdx++){
                if(tempRow[tempPivotIdx] != 0 ){
                    if(tempPivotIdx >= pivotIdx){
                        pivotIdx = tempPivotIdx;
                        break;
                    }
                    else if (tempPivotIdx < pivotIdx){
                       return false; 
                    }
                }
            }
        }
        return true;
    }

    public boolean isCannonic(double [][] matrix){
        int pivotIdx = -1;
        for(int i = 0; i < matrix[0].length; i++ ){
            double tempRow[] = getRow(matrix, i);
            for(int tempPivotIdx = 0; tempPivotIdx < tempRow.length; tempPivotIdx++){
                if(tempRow[tempPivotIdx] != 0 ){
                    if(tempPivotIdx >= pivotIdx){
                        pivotIdx = tempPivotIdx;
                        break;
                    }
                    else{
                       return false; 
                    }
                }
            }
        }
        return true;
    }

    public double[][] toEchelon(double [][] matrix){
        for(int i = 0; i < matrix.length; i++){
            int pivot = -1; 
            for(int row = 0; row < matrix.length; row++){ // rows seperated
                for(int tempPivot = 0; tempPivot < matrix[row].length; tempPivot++){ // cols seperated
                    if(matrix[row][tempPivot] != 0){ // temp pivot found
                        if(tempPivot >= pivot){ // wanted condition for making a matrix echelon. Take pivot and move on.
                            pivot = tempPivot;
                        }
                        else if(tempPivot < pivot && row != 0){ // unwanted condition, swap rows to make it echelon.
                            swap(matrix[row], matrix[row-1]);
                            System.out.println("R"+(row+1)+" <-> "+"R"+(row));
                        }
                        break; // our issue with the current row has ended, move on to the next one
                    }
                    if(tempPivot == matrix[0].length-1){ // for putting zero row/s to the end. Since zero row has no pivots, the if statement above couldn't swap it. And if tempPivot came all the way down and still couldn't find any value other than zero, it is a zero matrix row.
                        if(row != matrix.length-1){
                            swap(matrix[row],matrix[row+1]);    
                            System.out.println("R"+(row)+" <-> "+"R"+(row-1));
                        }
                    }
                }
            }
        }
        return matrix;        
    }
    
    public int findPivotIndex(double[] row){
        for(int i = 0; i < row.length; i++){
            if(row[i] != 0){
                return i;
            }
        }
        return -1; // is zero row
    }
   
    public double[][] toReducedEchelonForm(double[][] matrix){
        System.out.println("matrix:");
        show(matrix);
        
        matrix = toEchelon(matrix);

        // up to down check
        for(int row = 0; row < matrix.length; row++){
            
            // main(current) pivots index
            int pivotIdx = findPivotIndex(matrix[row]);
            
            // set the initial row's pivot to one
            if(pivotIdx==-1)
                continue;
            
            // adjust the initial row
            double constInit = (1/matrix[row][pivotIdx]);
            if(constInit != 1.0){
                matrix[row] = multRow( matrix[row] , constInit );
                System.out.println("R"+(row+1)+" -> "+constInit+"xR"+(row+1));
                show(matrix);        
            }

            // go for next rows to adjust them
            for(int next = 1; next < matrix[0].length; next++){
               if(row+next >= matrix.length){
                   break;
               }
               int nextPivotIdx = findPivotIndex(matrix[row+next]);
                if(pivotIdx <= nextPivotIdx){
                    double c = matrix[row+next][pivotIdx]/matrix[row][pivotIdx];
                    matrix[row+next] = subsRow(matrix[row+next], multRow(matrix[row], c));
                    if(c != 0.0){
                        System.out.println("R"+(row+next+1)+" -> "+"R"+(row+next+1)+" - "+c+"xR"+(row+1));
                        show(matrix);
                    }
                }   
            }
        }
        // down to up check
        for(int col = matrix[0].length-1; col >= 0; col--){
            int pivotIdx=-1;
            for(int row = matrix.length-1; row >= 0 ; row--){
                if(matrix[row][col] != 0){
                    if(pivotIdx==-1){ // means that its the first pivot it found
                        pivotIdx = row;
                    }
                    else{ // means that there is another, substract rows to get rid of it
                        double c = matrix[row][col]/matrix[pivotIdx][col];
                        matrix[row] = subsRow( matrix[row], multRow(matrix[pivotIdx], c));
                        if(c != 0.0){
                            System.out.println("R"+(row+1)+" -> "+"R"+(row+1)+" - "+c+"xR"+(pivotIdx+1));
                            show(matrix);
                        }
                    }
                }
            }
        }
        System.out.println("Result:");
        return matrix;
    }
    public double[][] reverse(double[][] matrix){
        return multiply(kofEach(matrix), (1/det(matrix)));
    }
    
    // helper function to swap two matrix rows
    private void swap(double[] a, double[] b){
        for(int i = 0; i < a.length ; i++){
            double temp = b[i];
            b[i] = a[i];
            a[i] = temp;
        }
    }
    
    public double[][] adj(double[][] matrix){
        double [][] temp = new double[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                temp[i][j] = kofactor(matrix, i,j);
            }
        }
        return trans(temp);
    }
    
    // helper function for 'reverese()', adj() would return transpos of temp whlist we need the original
    private double[][] kofEach(double[][] matrix){
        double [][] temp = new double[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                temp[i][j] = kofactor(matrix, i,j);
            }
        }
        return temp;
    }
    public double kofactor(double[][]matrix,  int row, int col){
        return ( (double)Math.pow(-1, row+col)*minor(matrix,row,col));
    }

    // only for 3x3
    public double det(double [][] matrix){
        if(matrix.length == 2 && matrix[0].length == 2){
            return detT(matrix);
        }
        double sum = 0;
        for(int i = 0; i < matrix.length; i++){
            sum += matrix[0][i]*kofactor(matrix, 0, i);
        }
        return sum;
    }
    // helper function to solve determinant of 2x2 or 1x1 matrix
    private double detT(double [][] matrix){
        if(matrix.length == 1 && matrix[0].length == 1){
            return matrix[0][0];
        }
        return ((matrix[0][0]*matrix[1][1])-(matrix[0][1]*matrix[1][0]));
    }
    public double minor(double[][] matrix, int row, int col){
        double[][] temp = new double[2][2];
        int a = 0, b = 0; 
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                if(i == row || j == col)
                    continue;
                temp[a][b] = matrix[i][j];
                if(b == 1){
                    a++;
                    b = 0;
                }
                else
                    b++;
            }
        }
        return detT(temp);
    }
    public double[] diagonal(double [][] matrix){
        if(matrix.length != matrix[0].length){
            System.err.println("The matrix must be a square matrix");
            return null;
        }
        double diag[] = new double[matrix.length];
        for(int i = 0; i < matrix.length; i++){
            diag[i] = matrix[i][i];
        }
        return diag;
    }
    public double[][] trans(double [][] matrix){
        double[][] temp = new double[matrix[0].length][matrix.length]; 
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                temp[j][i] = matrix[i][j]; 
            }
        }
        return temp;
    }
    
    public double[] getRow(double[][] matrix, int rowIdx){
        double temp[] = new double[matrix[0].length];
        int counter = 0;
        for(int i = 0; i < temp.length; i++){
            temp[counter++] = matrix[rowIdx][i];
        }
        
        return temp;
    }
    public double[] getCol(double[][] matrix, int colIdx){
        double temp[] = new double[matrix.length];
        int counter = 0;
        for (double[] col : matrix) {
            temp[counter++] = col[colIdx];
        }
        return temp;
    }
    
    public double[][] multiply(double[][] matrix, double[][] matrix2){
        if(matrix[0].length != matrix2.length){
            System.err.println("dimensions are not correct");
            return null;
        }
        double tempArr[][] = new double[matrix.length][matrix2.length];
        for(int i = 0; i < tempArr.length; i++){
            for(int j = 0; j < tempArr[0].length; j++){
                double sum = 0;
                for(int b = 0; b < matrix2.length; b++){
                    sum += (matrix[i][b] * matrix2[b][j]); 
                }
                tempArr[i][j] = sum;
            }
        }
        return tempArr;
    }
    public double[][] multiply(double[][] matrix, double constant){
        double tempArr[][] = new double[matrix.length][matrix[0].length];
        for(int i = 0; i < tempArr.length; i++){
            for(int j = 0; j < tempArr[0].length; j++){
                tempArr[i][j] = matrix[i][j]*constant;
            }
        }
        return tempArr;
    }
    public double[][] add(double[][] matrix, double [][] matrix2){
        if(matrix.length != matrix2.length && matrix[0].length != matrix2[0].length){
            System.err.println("dimensions are different");
            return null;
        }
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix2[i].length; j++){
                matrix[i][j] += matrix2[i][j];
            }
        }
        return matrix;
    } 
    public double[][] subtract(double[][] matrix, double [][] matrix2){
        if(matrix.length != matrix2.length && matrix[0].length != matrix2[0].length){
            System.err.println("dimensions are different");
            return null;
        }
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix2[i].length; j++){
                matrix[i][j] -= matrix2[i][j];
            }
        }
        return matrix;
    }
    public void show(double [][] matrix){
        for (double[] a : matrix) {
            for (int j = 0; j < a.length; j++) {
                System.out.print((double)Math.round(a[j] * 10) / 10 + ",       ");
            }
            System.out.println("\b\b");
        }
        System.out.println("");
    }
    public double[] sumRow(double[] matrixRow1, double[] matrixRow2 ){
        double[] tempRow = new double[matrixRow1.length];
        for(int i = 0; i < matrixRow1.length; i++){
            tempRow[i] = matrixRow1[i] + matrixRow2[i]; 
        }
        return tempRow;
    }
    public double[] subsRow(double[] matrixRow1, double[] matrixRow2 ){
        double[] tempRow = new double[matrixRow1.length];
        for(int i = 0; i < matrixRow1.length; i++){
            tempRow[i] = matrixRow1[i] - matrixRow2[i];
        }
        return tempRow;
    }
    public double[] multRow(double[] matrixRow1, double constant ){
        double[] tempRow = new double[matrixRow1.length];
        for(int i = 0; i < matrixRow1.length; i++){
            tempRow[i] = matrixRow1[i]*constant;
        }
        return tempRow;
    }
    public void showRow(double[] matrixRow){
        for(int i = 0; i < matrixRow.length; i++){
            System.out.print((double)Math.round(matrixRow[i] * 10) / 10 + " ");
        }
        System.out.println("");
    }
}
