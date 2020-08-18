import calculatorpackage.MatrixCalculator;


public class Main {
    public static void main(String[] args) {

        double[][] sampleMatrix = {
            {1,2,-1,3,1},
            {2,-1,1,-2,3},
            {3,1,0,2,-1},
            {5,1,2,-3,4},
            {-2,3,-1,1,-2},
        };

        MatrixCalculator mC = new MatrixCalculator();
        
        mC.show(mC.toReducedEchelonForm(sampleMatrix));

    }
}




