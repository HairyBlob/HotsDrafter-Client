package com.neogens.hotsdrafter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.FloatBuffer;
import org.apache.commons.io.IOUtils;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

/**
 *
 * @author François Gourdeau
 */


public class TeamEvaluator {
    Graph g,g2;
    Session s,s2;
    float[] teams = new float[200];
    float[] team = new float[100];
    float[] maps = new float[40];
    Tensor<Float> m = Tensor.create(new long[]{1,2, 20}, FloatBuffer.wrap(maps));        
    Tensor<Float> x = Tensor.create(new long[]{1,2, 100}, FloatBuffer.wrap(teams));
    Tensor<Float> x2 = Tensor.create(new long[]{1,1, 100}, FloatBuffer.wrap(team));
    Tensor<Float>  PlaceHolder_1=Tensor.create(new long[]{1},FloatBuffer.wrap(new float[]{1.0f}));
    
    public TeamEvaluator() throws IOException, Exception {
  
 //       String modelDir = "C:\\Users\\François\\Documents\\NetBeansProjects\\HotsDrafter2\\Models";
        g = new Graph();
  
        byte[] graphDef = fetchURL(getClass().getResource("opt_siamese.pb"));
       // byte[] graphDef = readAllBytesOrExit(Paths.get(modelDir, "opt_siamese.pb"));
        g.importGraphDef(graphDef);
        s = new Session(g);
        
        g2 = new Graph();
        graphDef = fetchURL(getClass().getResource("opt_discriminator.pb"));
        g2.importGraphDef(graphDef);
        s2 = new Session(g2);
     
    }
    
      public float getEstimatedEval(int[] a,int[] b,  int[] ba, int[] bb,int map){
        teams=new float[200];
        maps = new float[40];
  
        // Placer la méta
        double ajustA=0;
        double ajustB=0;  
           
        for (int i=0;i<a.length;i++){ajustA+=Hero.pickRate[a[i]];}
        for (int i=0;i<b.length;i++){ajustB+=Hero.pickRate[b[i]];}
        for (int i=0;i<ba.length;i++){ajustA+=Hero.pickRate[ba[i]];ajustB+=Hero.pickRate[ba[i]];
        }
        for (int i=0;i<bb.length;i++){ajustB+=Hero.pickRate[bb[i]];ajustA+=Hero.pickRate[bb[i]];
        }
        
              
        for (int i=0;i<Hero.heroes.length;i++){ 
           teams[i]= (float) ((float) Hero.pickRate[i]*((5-a.length)/(Hero.totalPickRate-ajustA)));          
           teams[i+100]=(float) ((float) Hero.pickRate[i]*((5-b.length)/(Hero.totalPickRate-ajustB)));
        }
        //****
 
        for (int i=0;i<a.length;i++){teams[a[i]]=1;}
        for (int i=0;i<b.length;i++){teams[b[i]+100]=1;}
        for (int i=0;i<ba.length;i++){teams[ba[i]]=0;teams[ba[i]+100]=0;
        }
        for (int i=0;i<bb.length;i++){teams[bb[i]+100]=0;teams[bb[i]]=0;
        }
        
            
        float totalA=0;
        float totalB=0;
             
        for (int i=0;i<Hero.heroes.length;i++){ 
           totalA+=teams[i];          
           totalB+=teams[i+100];
        }
       if ( totalA>5.1 || totalA<4.9 ||totalB>5.1 || totalB<4.9){
           System.err.println("Total <>5");
       }
     
        maps[map]=1;  maps[map+20]=1;

        m = Tensor.create(new long[]{1,2,20}, FloatBuffer.wrap(maps));        
        x = Tensor.create(new long[]{1,2,100}, FloatBuffer.wrap(teams));
        
        //PlaceHolder_1=1
        Tensor<Float> result =s.runner().feed("x",x).feed("m",m).feed("Placeholder_1",PlaceHolder_1).fetch("y").run().get(0).expect(Float.class);
        final long[] rshape = result.shape();                 
        float[] r= result.copyTo(new float[1][(int) rshape[1]])[0];
        return r[0];
    }
    
     public float getEstimatedTeamPlausability(int[] a){       
        team=new float[100];
       // Placer la méta
        double ajustA=0;     
        for (int i=0;i<a.length;i++){ajustA+=Hero.pickRate[a[i]];}     
              
        for (int i=0;i<Hero.heroes.length;i++){ 
           team[i]= (float) ((float) Hero.pickRate[i]*((5-a.length)/(Hero.totalPickRate-ajustA)));          
        }
        //**** 
        for (int i=0;i<a.length;i++){team[a[i]]=1;}
         
        float totalA=0;      
        for (int i=0;i<Hero.heroes.length;i++){ 
           totalA+=team[i];          
        }
       if ( totalA>5.1 || totalA<4.9){
           System.err.println("TotAL <>5");
       }
    
        x2 = Tensor.create(new long[]{1,1,100}, FloatBuffer.wrap(team));
        Tensor<Float> result =s2.runner().feed("x",x2).feed("keep_prob",PlaceHolder_1).fetch("y").run().get(0).expect(Float.class);
        final long[] rshape = result.shape();                 
        float[] r= result.copyTo(new float[1][(int) rshape[1]])[0];
        return r[0];
    }
    
    public float getEval(int[] a,int[] b, int map){
        teams=new float[200];
        maps = new float[40];
        for (int i=0;i<5;i++){teams[a[i]]=1;}
        for (int i=0;i<5;i++){teams[b[i]+100]=1;}
        maps[map]=1;  maps[map+20]=1;

        m = Tensor.create(new long[]{1,2,20}, FloatBuffer.wrap(maps));        
        x = Tensor.create(new long[]{1,2,100}, FloatBuffer.wrap(teams));
        
        Tensor<Float> result =s.runner().feed("x",x).feed("m",m).fetch("y").run().get(0).expect(Float.class);
        final long[] rshape = result.shape();                 
        float[] r= result.copyTo(new float[1][(int) rshape[1]])[0];
        return r[0];
    }
/*
    private static byte[] readAllBytesOrExit(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }*/
    private byte[] fetchURL(URL url) throws Exception {

        InputStream is = null;
        byte[] bytes = null;
        try {
            is = url.openStream();
            bytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            //handle errors
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return bytes;
    }
}

