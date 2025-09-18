import java.util.Arrays;


public class Lab1 {

    public static void main(String[] args) {
        try {
            final int STUDENT_ID = 12;
            final int C5 = STUDENT_ID % 5; 
            final int C7 = STUDENT_ID % 7; 
            final int C11 = STUDENT_ID % 11; 
            
           
            char[][] matrixA = {
                {10, 20, 30},  
                {40, 50, 60},
                {70, 80, 90}
            };
            
            char[][] matrixB = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
            };
            
            System.out.println("Початкова матриця A:");
            printMatrixAsNumbers(matrixA);
            System.out.println("\nПочаткова матриця B:");
            printMatrixAsNumbers(matrixB);
            
           
            char[][] matrixC = addMatrices(matrixA, matrixB);
            System.out.println("\nМатриця C = A + B:");
            printMatrixAsNumbers(matrixC);
            
           
            int sumRowMin = sumOfMinElementsInRows(matrixC);
            System.out.println("\nСума найменших елементів кожного рядка: " + sumRowMin);
            
        } catch (Exception e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }
    
    
    private static char[][] addMatrices(char[][] matrixA, char[][] matrixB) {
        if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) {
            throw new IllegalArgumentException("Матриці мають різні розміри");
        }
        
        int rows = matrixA.length;
        int cols = matrixA[0].length;
        char[][] result = new char[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                
                result[i][j] = (char) (matrixA[i][j] + matrixB[i][j]);
            }
        }
        
        return result;
    }
    
    
    private static int sumOfMinElementsInRows(char[][] matrix) {
        int sum = 0;
        
        for (int i = 0; i < matrix.length; i++) {
            int min = matrix[i][0]; 
            for (int j = 1; j < matrix[i].length; j++) {
                if (matrix[i][j] < min) {
                    min = matrix[i][j];
                }
            }
            sum += min;
        }
        
        return sum;
    }
    
    
    private static void printMatrixAsNumbers(char[][] matrix) {
        for (char[] row : matrix) {
            for (char element : row) {
                System.out.print((int)element + "\t");
            }
            System.out.println();
        }
    }
    
   
    private static void printMatrixAsChars(char[][] matrix) {
        for (char[] row : matrix) {
            for (char element : row) {
                System.out.print(element + "\t");
            }
            System.out.println();
        }
    }
}
