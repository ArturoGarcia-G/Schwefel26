/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schwefel26;

import java.util.Arrays;
import java.util.Random;
import java.util.*;

/**
 *
 * @author Arturo
 */
public class Schwefel26 {

    /**
     * @param args the command line arguments
     */
    public static double calificar(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            double xi = x[i];
            sum += xi * Math.sin(Math.sqrt(Math.abs(xi)));
        }
        return 418.9829 * x.length - sum;
    }
    public static double[] generarSolucion()
    {
        Random rand = new Random();
        double[] numeros = new double[10];
        
        for (int i = 0; i < numeros.length; i++) {
            numeros[i] = rand.nextDouble() * 1000 - 500;
        }
     
        return numeros;

    }
    public static int[] seleccionarPadres()
    {
        int[] numeros = new int[800];

        // Llenar el arreglo con los números del 1 al 
        for (int i = 0; i < numeros.length; i++) {
            numeros[i] = i;
        }

        // Ordenar aleatoriamente el arreglo
        Random random = new Random();
        for (int i = numeros.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = numeros[i];
            numeros[i] = numeros[j];
            numeros[j] = temp;
        }

        
        
        return numeros;
        
    
    }
    public static double[] crossover(double[] padre1, double[] padre2)
    {
        int n1, n2, bandera=0, m=0, temp=0;
        double [] hijo;
        hijo = new double[10];
        
        
        n1=numeroAleatorio(1,8);
        do {
            n2=numeroAleatorio(1,8); 
        } while (n2==(n1+1)|| n2==(n1-1)|| n2==n1);
        
      
        // Copiar una parte aleatoria del padre al hijo 
        for(int x=n1;x!=(n2+1);x++)
        {
            if(x==10)
            {
                x=0;
            }
            hijo[x]=padre1[x];
            
            m=x;
        }
        m++;
        
        // Llenar la parte restante con los numeros del otro padre 
        for(int x=(n2+1);x!=n2;x++)
        {
            bandera=0;
            if(x==10)
            {
                x=0;
            }
            
            for(int y=n1;y!=n2+1;y++)
            {
                if(y==10)
                {
                    y=0;
                }
                if(padre2[x]==hijo[y])
                {
                    bandera=1;
                }
                
            }
            if(bandera==0){
                
                hijo[m]=padre2[x];
                
                m++;
            }
            temp=x;
            if(m==10)
            {
                m=0;
            }
            
        }
        hijo[n1-1]=padre2[temp+1];
        
        return hijo;
    }
    public static int numeroAleatorio(int min, int max) {
        Random rand = new Random();        
        int numero = rand.nextInt((max - min) + 1) + min; // Generar primer número aleatorio
        return numero;
    }
    static void swap(double[] arreglo, double[][] padres, int i, int j) {
        
        double [] padretemp;
        padretemp = new double[63009];
        
        double temp = arreglo[i];
        padretemp = padres[i];
        arreglo[i] = arreglo[j];
        padres[i] = padres[j];
        arreglo[j] = temp;
        padres[j] = padretemp;
    }
    static int partition(double[] arreglo,double[][] padres, int low, int high) {
        double pivot = arreglo[high]; // pivote
        int i = (low - 1); // índice del elemento más pequeño

        for (int j = low; j <= high - 1; j++) {
            // Si el elemento actual es menor o igual al pivote
            if (arreglo[j] <= pivot) {
                i++; // Incrementa el índice del elemento más pequeño
                swap(arreglo,padres, i, j);
            }
        }
        swap(arreglo,padres, i + 1, high);
        return (i + 1);
    }
    static void quickSort(double[] arreglo, double[][] padres,int low, int high) {
        if (low < high) {
            /* pi es el índice de partición, arreglo[pi] está en su posición correcta */
            int pi = partition(arreglo,padres, low, high);

            /* Ordena los elementos antes de la partición y después de la partición */
            quickSort(arreglo, padres, low, pi - 1);
            quickSort(arreglo, padres, pi + 1, high);
        }
    }
    public static void main(String[] args) {
        // TODO code application logic here
        
        double[][] poblacionInicial;
        poblacionInicial = new double[1000][10];
        
        double[][] padres;
        padres = new double[800][10];
        double[][] hijos;
        hijos = new double[800][10];
        int [] padresSelec;        
        padresSelec = new int[800];
        double [] costos;
        costos=new double[1000];      
        double [] hijo1;
        double [] hijo2;
        hijo1 = new double[63009];
        hijo2 = new double[63009];
        int p1, p2, n1, n2, n3, probabilidad, r;
        
        // Crear una poblacion inicial de 1000 elementos
        double[] numeros= new double [10];
        double costo;
        for(int i=0; i<1000; i++){
            numeros = generarSolucion();
            poblacionInicial[i]= numeros;
            costo=calificar(numeros);
            costos[i]=costo;
        }
        
        //CICLO DE 100 GENERACIONES
        for(int m=0; m<100;m++){
            //Para imprimir el mejor resultado de cada generacion
            //System.out.println("generacion "+m+" Mejor: " +costos[0]);
            for(int i=0; i<1000; i++){
                
                costo=calificar(poblacionInicial[i]);
                costos[i]=costo;
            }
            
        
        
        //Seleccion de padres 
        for(int x=0;x<800; x++ )
        {
            
            p1=numeroAleatorio(0,999);
            p2=numeroAleatorio(0,999);
            if(costos[p1]>=costos[p2])
            {
                padres[x]=poblacionInicial[p2];
            }
            else
            {
                padres[x]=poblacionInicial[p1];
            }
                
        }
        
        
        // Crossover (Creacion de hijos)
        padresSelec=seleccionarPadres(); //Se seleccionan aleatoriamente el orden de padres 
        for(int z=0;z<800;z=z+2)
        {
            probabilidad=numeroAleatorio(1,10);
            if(probabilidad>7)
            {
                n1=numeroAleatorio(0,9);
                n2=numeroAleatorio(0,9);
                hijo1=crossover(padres[padresSelec[z]], padres[padresSelec[z+1]]);
                hijo2=crossover(padres[padresSelec[z+1]], padres[padresSelec[z]]);
                double aux;
                aux=hijo1[n1];
                hijo1[n1]=hijo1[n2];
                hijo1[n2]=aux;
                aux=hijo2[n1];
                hijo2[n1]=hijo2[n2];
                hijo2[n2]=aux;
                hijos[z]=hijo1;
                hijos[z+1]=hijo2;
                
            }
            else
            {
               hijos[z]=crossover(padres[padresSelec[z]], padres[padresSelec[z+1]]);
                hijos[z+1]=crossover(padres[padresSelec[z+1]], padres[padresSelec[z]]); 
            }
            
        }
       
        quickSort(costos, poblacionInicial, 0, 999);
        
        // Sustituyen
        for(int p=0; p<800;p++)
        {
            poblacionInicial[199+p]=hijos[p];
            
        }
        
        
        
        }
        System.out.println("Resultado:");
        System.out.println(costos[0]);
        System.out.println("Mejor arreglo");
        System.out.println(Arrays.toString(poblacionInicial[0])); 
        
    }
    
}
