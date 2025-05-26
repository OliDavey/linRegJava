import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class LinRegMaker{
    // attributes
    private double slope;
    private double intercept;
    private double r;
    private double rsqrd;

    private double xsum = 0;
    private double ysum = 0;
    private double xsqsum = 0;
    private double ysqsum = 0;
    private double xysum = 0;

    private final String [][] data;
    private final double[] x;
    private final double[] y;
    private String[] chosen = new String[2];

    // constructor
    public LinRegMaker(String[][] data){
        this.data = data;
        this.x = new double[data.length - 1];
        this.y = new double[data.length - 1];
    }

    public LinRegMaker(String[][] data, int var1, int var2){
        this.data = data;
        this.x = new double[data.length - 1];
        this.y = new double[data.length - 1];
        this.chosen[0] = data[0][var1];
        this.chosen[1] = data[0][var2];

        for (int i = 1; i < data.length; i++){
                this.x[i - 1] = Double.parseDouble(data[i][var1]);
                this.y[i - 1] = Double.parseDouble(data[i][var2]);
        }

        correl();
        interceptCalc();
        slopeCalc();
    }

    // variable chooser, fills in x and y arays
    private void chooser(){
        int length = data[0].length;
        Scanner scan = new Scanner(System.in);
        int explanatory;
        int response;

        // print out all variables 
        for (int i = 0; i < length; i++){
            System.out.println("* " + data[0][i] + " " + i);
        }
        
        // user input for exp and resp
        System.out.println("Choose your explanatory variable (x) by pressing number:");
        explanatory = scan.nextInt();
        System.out.println("Choose your response variable (y) by pressing number:");
        response = scan.nextInt();

        chosen[0] = data[0][explanatory];
        chosen[1] = data[0][response];

        scan.close();

        // filling variable arrays
        for (int i = 1; i < data.length; i++){
                x[i - 1] = Double.parseDouble(data[i][explanatory]);
                y[i - 1] = Double.parseDouble(data[i][response]);
        }
    }

    private void sums(){
        for (int i = 0; i < x.length; i++){
            xsum += x[i];
            ysum += y[i];
            xysum += x[i] * y[i];
            xsqsum += Math.pow(x[i], 2);
            ysqsum += Math.pow(y[i], 2);
        }
    }

    private void correl(){
        sums();
        int length = x.length;

        double cov = length * xysum - (xsum * ysum);
        double sampleStdev = 
            Math.sqrt((length*xsqsum - Math.pow(xsum, 2))*
            (length*ysqsum - Math.pow(ysum, 2)));
        r = cov/sampleStdev;
        rsqrd = Math.pow(r, 2);
    }


    private void interceptCalc(){
        int length = x.length;

        // intercept calculations for numerator and denominator
        double num = (ysum * xsqsum) - (xsum * xysum);
        double denum = length * xsqsum - Math.pow(xsum, 2);
        intercept = num / denum;
    }

    private void slopeCalc(){
        int length = x.length;

        double num = length * xysum - (xsum * ysum);
        double denum = length * xsqsum - Math.pow(xsum, 2);
        slope = num / denum;
    }

    public void summary(){
        System.out.println("_______________________________");
        System.out.println("-Linear Regression Moddeller-");
        System.out.println("-Variables:");
        chooser();
        System.out.println("_______________________________");

        correl();
        interceptCalc();
        slopeCalc();

        System.out.println("- Predicting: " + chosen[0]);
        System.out.println("- Using: " + chosen[1]);
        System.out.println("_______________________________");

        System.out.println("- Linear Regression Equation:");
        System.out.printf("Predicted %S = %f + %f %S %n", 
            chosen[0],intercept, slope, chosen[1]);

        System.out.println("_______________________________");
        System.out.println("- Pearsons Corr Coefficiant Value: " + r);
        System.out.println("- R Square Score: " + rsqrd);
        System.out.println("_______________________________");
    }

    public void summaryVal(){
        System.out.println("_______________________________");
        System.out.println("- Predicting: " + chosen[0]);
        System.out.println("- Using: " + chosen[1]);
        System.out.println("_______________________________");
        System.out.println("- Linear Regression Equation:");
        System.out.printf("Predicted %S = %f + %f %S %n", 
            chosen[0],intercept, slope, chosen[1]);
        System.out.println("_______________________________");
        System.out.println("- Pearsons Corr Coefficiant Value: " + r);
        System.out.println("- R Square Score: " + rsqrd);
        System.out.println("_______________________________");
    }

    public double predictVal(double userin){
        double pred = intercept + slope * userin;
        return pred;
    }
    
     public double getSlope() {
        return slope;
    }

    public double getIntercept() {
        return intercept;
    }

    public double getR() {
        return r;
    }

    public double getRsqrd() {
        return rsqrd;
    }

    public static void main(String[] args) {
        String filep = "HeptathlonData.csv"; // <-- add ability to input own csv through ui later
        String line;
        // String seperator = ",";
        BufferedReader br = null;
        List<String[]> data = new ArrayList<>();
        String[][] dataTwo = {};
        
        try {
            br = new BufferedReader(new FileReader(filep));
            while ((line = br.readLine()) != null) { 
                // singular line e.g 1,2,3 in csv file
                data.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1));
            }
            dataTwo = data.toArray(new String[0][]);
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // example output and use case
        LinRegMaker reg = new LinRegMaker(dataTwo);
        reg.summary();
        System.out.println(reg.predictVal(141.5));
    }
}