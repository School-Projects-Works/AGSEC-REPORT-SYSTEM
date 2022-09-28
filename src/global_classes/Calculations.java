
package global_classes;
import java.text.DecimalFormat;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pojo_classes.Scores;

/**
 *
 * @author emman
 */
public class Calculations {
     DecimalFormat df = new DecimalFormat("#.##");
    
   public double getThirtyPercent(double classTotal){
      
        return Double.parseDouble(df.format(classTotal*0.3)) ;
   }

    public double getSeventyPercent(double finalExam){        
         return Double.parseDouble(df.format(finalExam*0.7));
         
    }

    public double getTotalGrade(double thirtyPercent,double seventyPercent){
        return Double.parseDouble(df.format(thirtyPercent+seventyPercent)) ;
    }

    public String getLetterGrade(double totalGrade){
        if(totalGrade>=80){
            return "A";
        }else if(totalGrade>=70){
            return "B";
        }else if(totalGrade>=60){
            return "C";
        }else if(totalGrade>=50){
            return "D";
        }else if(totalGrade>=40){
            return "E";
        }else{
            return "F";
        }
    }

public ObservableList<Scores> getPosition(ObservableList<Scores> scores){
  Collections.sort(scores, (Scores o1, Scores o2) -> Integer.valueOf(round(o1.getOverAllTotal())).compareTo(round(o2.getOverAllTotal())));
  FXCollections.reverse(scores);
  for(int i=0;i<scores.size();i++){
      String position=String.valueOf(i+1)+" th";
      String n=String.valueOf(i+1);
      switch (n.charAt(n.length()-1)) {
          case '1':
              position=n+" st";
              break;
          case '2':
              position=n+" nd";
              break;
          case '3':
              position=n+" rd";
              break;
          default:
              break;
      }
      if(n.length()>1&&n.charAt(0)=='1'){
           position=n+" th";
      }
      scores.get(i).setPosition(position);     
  } 
  return scores;
  
}

private int round(double d){
    double dAbs = Math.abs(d);
    int i = (int) dAbs;
    double result = dAbs - (double) i;
    if(result<0.5){
        return d<0 ? -i : i;            
    }else{
        return d<0 ? -(i+1) : i+1;          
    }
}

    double getClassTotal(double exercise, double midTerms, double homeWork) {
        return Double.parseDouble(df.format(exercise+midTerms+homeWork)) ;
       
    }

    
}
