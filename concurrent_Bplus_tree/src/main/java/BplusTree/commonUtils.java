package BplusTree;

import java.util.ArrayList;
import java.util.List;

public class commonUtils {

    public static<T> List<T> ArrayCopy(List<T> source,int begin,int end){
        List<T> res = new ArrayList<>();
        for(int i = begin;i <= end;i++){
            res.add(source.get(i));
        }
        return res;
    }
}
