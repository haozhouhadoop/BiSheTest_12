

import java.util.Arrays;

public class LeftPhoto  {
	ExcelIO excelio=ExcelIO.getInstance();
	double x1,y1,x2,y2,x3,y3,x4,y4,x5,y5,x6,y6;
	
	//像空间辅助坐标
	double[][]  XYZ=new double[6][3];
	//像空间坐标
	double[][]  S_xyz=new double[6][3];
	double bx,by,bz;
	
	//5个待求参数初始值
	 double ψ=0;
	 double w=0;
	 double k=0;
	 double u=0;
	 double r=0;
	 public LeftPhoto(){
		 setXyz();
		 leftXiangFuZB();
	 }
	//给x1,x2,...赋值
	 public void setXyz(){
//		 excelio.getXYfdata();
		 x1=excelio.ld[0][0];
		 y1=excelio.ld[0][1];
		 x2=excelio.ld[1][0];
		 y2=excelio.ld[1][1];
		 x3=excelio.ld[2][0];
		 y3=excelio.ld[2][1];
		 x4=excelio.ld[3][0];
		 y4=excelio.ld[3][1];
		 x5=excelio.ld[4][0];
		 y5=excelio.ld[4][1];
		 x6=excelio.ld[5][0];
		 y6=excelio.ld[5][1];
		 for(int i=0;i<6;i++)
			 for(int j=0;j<3;j++){
				if(j<2){
					S_xyz[i][j]=excelio.ld[i][j];
				}
				else
					S_xyz[i][j]=-excelio.f;
			 }
		 System.out.println("左像空间坐标S_xyz:");
		 for (double[] d : S_xyz) {
			System.out.println(Arrays.toString(d));
		}
	 }
	
	//旋转矩阵
    //左像空间辅助坐标的计算
    public double[][] leftXiangFuZB(){
    	XYZ=S_xyz;
    	
    	System.out.println("左像空间辅助坐标");
    	for (double[] d : XYZ) {
			System.out.println(Arrays.toString(d));
		}
    	return XYZ;

	
    }
	


	
	
    



}
