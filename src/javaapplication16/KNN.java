/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication16;

public class KNN {
    
    
  public static int extractInt( byte b31ToB24 , byte b23ToB16, byte b15ToB8, byte b7ToB0){
    
 int value = ((b31ToB24 & 0xFF) << 24) | ((b23ToB16 & 0xFF) << 16)
        | ((b15ToB8& 0xFF) << 8) | (b7ToB0 & 0xFF); 
return value;
}

public static byte[][][]parseIDXimages(byte[]data)
{
    for(int i=16;i<data.length;i++){
    if ((Math.abs((int)data[i]-127))>(Math.abs((int)data[i]+128)))
        data[i]=(byte)(127);
    else
        data[i]=(byte)(-128);
    }
    
        
int nb_images=extractInt(data[4],data[5],data[6],data[7]);
int hauteur=extractInt(data[8],data[9],data[10],data[11]); 
int largeur=extractInt(data[12],data[13],data[14],data[15]);
byte[][][]tensor=new byte[nb_images][hauteur][largeur];
 int position=16; 
for (int k=0;k<nb_images;k++){
    for (int i=0;i<hauteur;i++){
        for (int j=0;j<largeur;j++){
            tensor[k][i][j]=data[position];
            position +=1;
        }
    }
        
    //images[k]=extraction_image(data,hauteur,largeur, indice);
    //indice+=(hauteur*largeur);
}
return(tensor);
} 
     public static byte[] parseIDXlabels( byte[] data){
      int nb_etiquettes=extractInt(data[4],data[5],data[6],data[7]);
      byte[]labels=new byte[nb_etiquettes];
      for(int i=0;i<nb_etiquettes;i++){
          labels[i]=data[8+i];
      }
      return(labels);
     }
     
     public static float squaredEuclideanDistance( byte[][] a, byte[][] b){
       int W=a[0].length;
       int H=a.length;
       double k =0 ;
       for (int i=0;i<=H-1;i++){
           for(int j=0;j<=W-1;j++){
               k=k+((a[i][j]-b[i][j])*(a[i][j]-b[i][j]));
           }
           
       } 
     
        //f=Math.sqrt(k);
       float f=(float)k;
       return f;
     }
     public static float[] moyenne (byte[][]a,byte[][]b){
         float tab[]=new float[2];
        int W=a[0].length;
       int H=a.length;
       tab[0]=0;
       tab[1]=0;
       float k=1/(H*W);
       for (int i=0;i<H-1;i++){
           for(int j=0;j<W-1;j++){
               tab[0]+=a[i][j];
               tab[1]+=b[i][j];
               
           }
       }
          tab[0]=k*tab[0];
          tab[1]=k*tab[1];
           return (tab);
     }
     public static float invertedSimilarity( byte[][] a, byte[][] b){
       int W=a[0].length;
       int H=a.length; 
        float ma=moyenne(a,b)[0];
        float mb=moyenne(a,b)[1];
        float k=0;
        double d=0;
        float s;
         for(int i=0;i<H-1;i++){
             for(int j=0;j<W-1;j++){
                 
                k+=((a[i][j]-ma)*(b[i][j]-mb));
                d+=(Math.pow((a[i][j]-ma),2)*Math.pow((b[i][j]-mb),2));
                
             }
         }
             if (d==0)
                 return 2;
             else {
                  double racine =Math.sqrt(d);
                   float c=(float)racine ;
             
                   s=1-(k/c);
         
                    return s ;
                     }
     }
     public static void swap(int i, int j, float[] values , int[] indices){
           int aux1;
           float aux2;
           aux2=values[i];
           values[i]=values[j];
           values[j]=aux2;
           aux1=indices[i];
           indices[i]=indices[j];
           indices[j]=aux1;
           
     }
     /**
	 * @brief Quicksorts and returns the new indices of each value.
	 * 
	 * @param values the values whose indices have to be sorted in non decreasing
	 *               order
	 * 
	 * @return the array of sorted indices
	 * 
	 *         Example: values = quicksortIndices([3, 7, 0, 9]) gives [2, 0, 1, 3]
	 */                                           //0 ,1, 2, 3
	public static int[] quicksortIndices(float[] values) {
		int[]indices=new int [values.length];
                  for(int i=0;i<indices.length;i++){
                      indices[i]=i;
                  }
                quicksortIndices( values, indices,0,values.length-1);
                
		return indices;
	}

	/**
	 * @brief Sorts the provided values between two indices while applying the same
	 *        transformations to the array of indices
	 * 
	 * @param values  the values to sort
	 * @param indices the indices to sort according to the corresponding values
	 * @param         low, high are the **inclusive** bounds of the portion of array
	 *                to sort
	 */
	public static void quicksortIndices(float[] values, int[] indices, int low, int high) {
		int l=low;
                int h=high;
                float pivot =values[low];
                while(l<=h) {
                    if (values[l]<pivot)
                        l++;
                    else if (values[h]>pivot) 
                       h--;
                    else{
                        swap(l,h, values ,indices);
                        l++;
                        h--;
                    }
                }
                if (low<h) 
                    quicksortIndices(values, indices, low, h);
                if (high>l) 
                    quicksortIndices(values, indices, l, high);
                        
              
	}

	
/**
	 
	
	/**
	 * @brief Returns the index of the largest element in the array
	 * 
	 * @param array an array of integers
	 * 
	 * @return the index of the largest integer
	 */
	public static int indexOfMax(int[] array) {
		int max=array[0];
                int indice=0;
                for(int h = 1; h< array.length-1; h++){
                 if(array[h] > max){
                max = array[h];
                indice=h;}
                }
		return indice;
	}
        
	/**
	 * The k first elements of the provided array vote for a label
	 *
	 * @param sortedIndices the indices sorted by non-decreasing distance
	 * @param labels        the labels corresponding to the indices
	 * @param k             the number of labels asked to vote
	 *
	 * @return the winner of the election
	 */
	public static byte electLabel(int[] sortedIndices, byte[] labels, int k) {
		int []tab=new int[10];
                int i=0;
                byte max;
                for(int j=0;j<10;j++){
                      tab[j]=0;
                  }
                while(i<k){
                    int x=sortedIndices[i];//cherche l'etiquette d'indice i
                    tab[labels[x]]=tab[labels[x]]+1;
                
                i++;
                }
                max= (byte)indexOfMax(tab);
		return max;
	}

	/**
	 * Classifies the symbol drawn on the provided image
	 *
	 * @param image       the image to classify
	 * @param trainImages the tensor of training images
	 * @param trainLabels the list of labels corresponding to the training images
	 * @param k           the number of voters in the election process
	 *
	 * @return the label of the image
	 */
	public static byte knnClassify(byte[][] image, byte[][][] trainImages, byte[] trainLabels, int k) {
	    float tab[]=new float [trainImages.length];
            int tab2[]=new int [tab.length];
            for(int i=0;i<trainImages.length;i++){
                tab[i]= squaredEuclideanDistance(trainImages[i],image);
               
            }
                 tab2=quicksortIndices(tab);
            
            
                byte x= electLabel(tab2,trainLabels,k);
            return x;   
            
            

        }
        	/**
	 * Computes accuracy between two arrays of predictions
	 * 
	 * @param predictedLabels the array of labels predicted by the algorithm
	 * @param trueLabels      the array of true labels
	 * 
	 * @return the accuracy of the predictions. Its value is in [0, 1]
	 */

        public static double accuracy(byte[] predictedLabels, byte[] trueLabels) {
		int n=predictedLabels.length;
                double k=0;
                int i=0;
                while ((i<n) && (predictedLabels[i]==trueLabels[i])){
                         k=k+1;
                    i++;
                    
                }
                
                double s=Math.pow(n,-1);
                 double a=s*k;
		return a;
	}

     }



