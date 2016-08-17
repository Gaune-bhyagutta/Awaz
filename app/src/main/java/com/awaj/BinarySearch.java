package com.awaj;

/**
 * Created by imas on 8/17/16.
 */
public class BinarySearch {

    /** Search of the Nearest value in an array *
     *
     * @param: float[] , float
     * @return: int
     */
    public static int searchNearestValue(float[] data, float value){
        float distance =10000;
        int index=0;
        for(int i=0;i<data.length;i++){
            float cDistance = Math.abs(data[i]-value);
            if(cDistance<=distance){
                index =i;
                distance = cDistance;
            }
        }
        System.out.print("Close to"+ index);
        return index;
    }/** End of searchNearestValue */
}
