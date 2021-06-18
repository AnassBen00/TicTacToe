package task;


import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.concurrent.Task;

public class ProgressBarTask extends Task<Integer> {
    private int size;



    public ProgressBarTask(int size){

        this.size= size;

    }



    @Override
    protected Integer call() throws Exception {

            Integer iter = 0;
            String msg = null;
            System.out.println("START TRAINING ...");
            int[] layers = new int[]{ 2, 5, 1 };

            double error = 0.0 ;
            MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.1, new SigmoidalTransferFunction());
            double samples = 1000000000 ;

            //TRAINING ...
            for(int i = 0; i < samples; i++){
                double[] inputs = new double[]{Math.round(Math.random()), Math.round(Math.random())};
                double[] output = new double[1];

                if((inputs[0] == 1.0) || (inputs[1] == 1.0))
                    output[0] = 1.0;
                else
                    output[0] = 0.0;



                error += net.backPropagate(inputs, output);
                updateProgress(i,samples);
                if ( i % 100000 == 0 ) {
                    System.out.println("Error at step "+i+" is "+ (error/(double)i));
                    msg = "Error at step "+i+" is "+ (error/(double)i);
                    updateMessage(msg);
                }
            }
            error /= samples ;
            System.out.println("Error is "+error);
            msg ="Error is "+error;
            //
            System.out.println("Learning completed!");
            msg ="Error is "+error ;
            updateMessage(msg);

            //TEST ...
            double[] inputs = new double[]{0.0, 1.0};
            double[] output = net.forwardPropagation(inputs);

            System.out.println(inputs[0]+" or "+inputs[1]+" = "+Math.round(output[0])+" ("+output[0]+")");

            return iter;
        }





}
