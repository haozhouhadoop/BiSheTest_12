
import jxl.*;

import java.io.*;
import java.io.*;
import java.util.Arrays;

import org.omg.CORBA.MARSHAL;
public class ExcelIO {
	
	  
	  Workbook workbook=null;
	  Sheet shell=null;
	  File file=new File("C://Users//ASUS//Desktop//毕业设计//相对定向//相对定向平差初始数据.xls");
	  { try {
    		 workbook=Workbook.getWorkbook(file);
		} catch (Exception e) {
			e.printStackTrace();

		}
	  
	  shell=workbook.getSheet(0);
	  }
	  //用于算像平面坐标的参数
	  static final double k1=1.272754e-003;
	  static final double k2=-1.3776563e-004;
	  static final double k3=-1.236405e-055;
	  static final double p1=-1.983858e-004;
	  static final double p2=-8.264034e-006;
	  static final double mm_width=4.17;
	  static final double mm_height=5.56;
	  static final double p_width=1058.3;//单位mm                             1411.1
	  static final double p_height=1411.1;//单位mm
	  static final double pixel_width= 3000.000000;
	  static final double pixel_height= 4000.000000;
	  static  double x0 = 0.024002;                                 
	  static  double y0 = 0.026723;
	  double bs;//放大倍数
	  //用两数组存储像平面坐标
       double[][] rd=new double[6][2];
       double[][] ld=new double[6][2];
    //相片焦距
	   double f;
       double Bx;
    //限差
       double fx;
    //相片比例尺
       double m;
    //畸变差改正以及像平面坐标解算
       public void set_JBC_xy( ){
    	   bs=(p_width/mm_width+p_height/mm_height)/2;
    	   System.out.println("放大倍数bs:"+bs);
    	   
    	   f=f*bs;
    	   System.out.println("像片焦距f:"+f);
    	   for(int i=0;i<6;i++){
    		   rd[i][0]=rd[i][0]/bs;
    		   rd[i][1]=rd[i][1]/bs;
    		   ld[i][0]=ld[i][0]/bs;
    		   ld[i][1]=ld[i][1]/bs;
    		  double r=Math.sqrt(Math.pow(rd[i][0]-x0,2)+Math.pow(rd[i][1]-y0, 2));
    		  System.out.println("r="+r);
    		  double x_x=(rd[i][0]-x0)*(k1*Math.pow(r, 2)+k2*Math.pow(r, 4))+p1*(Math.pow(r, 2)+2*Math.pow(rd[i][0]-x0, 2))+p2*(2*(rd[i][0]-x0)*(rd[i][1]-y0));
    		  System.out.println("x_x"+x_x);
    		  double y_y=(rd[i][1]-y0)*(k1*Math.pow(r, 2)+k2*Math.pow(r, 4))+p2*(Math.pow(r, 2)+2*Math.pow(rd[i][1]-y0, 2))+p1*(2*(rd[i][0]-x0)*(rd[i][1]-y0));
    		  rd[i][0]=rd[i][0]-x_x-x0;
    		  rd[i][1]=rd[i][1]-y_y-y0;
    		  rd[i][0]=rd[i][0]*bs;
    		  rd[i][1]=rd[i][1]*bs;
    		  double r1=Math.sqrt(Math.pow(ld[i][0]-x0,2)+Math.pow(ld[i][1], 2));
    		  double x=(ld[i][0]-x0)*(k1*Math.pow(r1, 2)+k2*Math.pow(r1, 4))+p1*(Math.pow(r1, 2)+2*Math.pow(ld[i][0]-x0, 2))+p2*(2*(ld[i][0]-x0)*(ld[i][1]-y0));
    		  double y=(ld[i][1]-y0)*(k1*Math.pow(r1, 2)+k2*Math.pow(r1, 4))+p2*(Math.pow(r1, 2)+2*Math.pow(ld[i][1]-y0, 2))+p1*(2*(ld[i][0]-x0)*(ld[i][1]-y0));
    		  ld[i][0]=ld[i][0]-x-x0;
    		  ld[i][1]=ld[i][1]-y-y0;
    		  ld[i][0]=ld[i][0]*bs;
    		  ld[i][1]=ld[i][1]*bs;
    	   }
    	   System.out.println("畸变差后的像平面坐标:");
    	   System.out.println("右像平面坐标:");
    	   for (double[] d : rd) {
			System.out.println(Arrays.toString(d));
		}
    	   System.out.println("左像平面坐标:");
    	   for (double[] d : ld) {
			System.out.println(Arrays.toString(d));
    	   
        }
    	   
    	   
       }
       //用于平差后的坐标计算
       public double[][]  set_JBC_xy(double[][] d ){
    	   System.out.println("刚传进去的d");
    	   for (double[] es : d) {
			System.out.println(Arrays.toString(es));
		}
    	   bs=(p_width/mm_width+p_height/mm_height)/2;
    	   System.out.println("放大倍数:"+bs);
    	   d[0][0]=d[0][0]-p_width/2;//x
    	   d[1][0]=d[1][0]-p_height/2;//y
    	   d[1][0]=-d[1][0];
    	   d[0][0]=d[0][0]/bs;
		   d[1][0]=d[1][0]/bs;
		   System.out.println("移到中心后的xy:");
		   for (double[] es : d) {
			System.out.println(Arrays.toString(es));
		}
    	   
     	   double r=Math.sqrt(Math.pow(d[0][0]-x0,2)+Math.pow(d[1][0]-y0, 2));
     	   System.out.println("r="+r);
     	   double x_x=(d[0][0]-x0)*(k1*Math.pow(r, 2)+k2*Math.pow(r, 4))+p1*(Math.pow(r, 2)+2*Math.pow(d[0][0]-x0, 2))+p2*(2*(d[0][0]-x0)*(d[1][0]-y0));
     	   double y_y=(d[1][0]-y0)*(k1*Math.pow(r, 2)+k2*Math.pow(r, 4))+p2*(Math.pow(r, 2)+2*Math.pow(d[1][0]-y0, 2))+p1*(2*(d[0][0]-x0)*(d[1][0]-y0));
     	   System.out.println("x_x="+x_x+" y_y="+y_y);
     	   d[0][0]=d[0][0]-x_x-x0;
     	   d[1][0]=d[1][0]-y_y-y0;
     	   System.out.println("畸变差后xy:");
     	   for (double[] es : d) {
			System.out.println(Arrays.toString(es));
		}
     	   d[0][0]=d[0][0]*bs;
     	   d[1][0]=d[1][0]*bs;
     	   System.out.println("d矩阵：");
     	   for (double[] es : d) {
			System.out.println(Arrays.toString(es));
		}
     		  
     	   
    	   return d;
     	  
     	   
       }
       
      //读取excel中像平面坐标赋值给数组
     public double getXYfdata(){
    	 System.out.println("开始读取excel...");
    
    	 
    	 for(int i=7;i<13;i++){
    		 for(int j=0;j<2;j++){
    			 ld[i-7][j]=Double.valueOf(shell.getCell(j, i).getContents());
    			 
    		 }
    		//将左像点坐标平移到相片重心
			 ld[i-7][0]=ld[i-7][0]-p_width/2;
			 ld[i-7][1]=ld[i-7][1]-p_height/2;
			 ld[i-7][1]=-ld[i-7][1];
    		 
    	 }
    	 //打印左像片面坐标
    	 System.out.println("左像平面坐标：");
    	 for (double[] d : ld) {
			System.out.println(Arrays.toString(d));
		}
    	 System.out.println("左相平面坐标读取完毕。");
    	 //右像平面坐标读取
    	 for(int i=7;i<13;i++){
    		 for(int j=2;j<4;j++){
    			 rd[i-7][j-2]=Double.valueOf(shell.getCell(j,i).getContents());
    			
    		 }
    		 //将右像点坐标平移到相片重心
			 rd[i-7][0]=rd[i-7][0]-p_width/2;
			 rd[i-7][1]=rd[i-7][1]-p_height/2;
			 rd[i-7][1]=-rd[i-7][1];
    	 }
    	 //打印右像平面坐标
    	 System.out.println("右像平面坐标：");
    	 for (double[] d : rd) {
			System.out.println(Arrays.toString(d));
		}
    	 System.out.println("右像平面坐标读取完毕");
    	 f=Double.valueOf(shell.getCell(0,4).getContents());
    	 System.out.println("像片焦距f:"+f);
    	 System.out.println();
    	 fx=Double.valueOf(shell.getCell(6,12).getContents());
    	 Bx=Double.valueOf(shell.getCell(0, 7).getContents())-Double.valueOf(shell.getCell(2, 7).getContents());
    	 m=Double.valueOf(shell.getCell(2,4).getContents());
    	 System.out.println("相片比例尺："+m);
    	 System.out.println("一号点左右视差Bx："+Bx);
    	 System.out.println("平差限差："+fx);
    	 System.out.println("excel原始数据读取完毕。");
    		return Bx;	 
      }
      
      //单例模式
      private static ExcelIO instance;
      private ExcelIO(){
    	  
    	  getXYfdata();
    	  set_JBC_xy();
    	  }
      public static synchronized ExcelIO getInstance(){
    	  if(instance==null){
    		  instance=new ExcelIO();
    	  }
    	  return instance;
      }
      
}
