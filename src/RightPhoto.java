
import java.lang.Math;

/* 从excel读取原始想平面坐标进行平差，
 * 求出5个待测量
 * 
 * 记事本读取需要转换的左右像平面坐标，
 * 并将摄影坐标输出到记事本
 * 记事本不能有空格，否者可能转换出错
 * 
 */











import Jama.*;

import java.io.*;
import java.util.*;

public class RightPhoto {
	LeftPhoto leftPhoto=null;
    static ExcelIO excelio=ExcelIO.getInstance();
	double x1,y1,x2,y2,x3,y3,x4,y4,x5,y5,x6,y6;
	static final double hudu_dufenmiao=180*60*60/Math.PI;
	 
	//右相片旋转矩阵r2的转置
	double[][]  R2=new double[3][3];
	//像空间坐标
	double[][]  S_xyz=new double[6][3];
	//像空间辅助坐标
	double[][]  XYZ=new double[6][3];
	
	double bx,by,bz;
	//左右投影系数N1,N2
	double[] N1=new double[6],N2=new double[6];
	
//	//5个待求参数初始值
//	 static double ψ=0;
//	 static double w=0;
//	 static double k=0;
//	 static double u=0;
//	 static double r=0;
	 //待求参数矩阵ψ,w,k,u,r
	 double[][] X0={{0},{0},{0},{0},{0}};
	  
		
//		X0[0][0]=ψ;
//		X0[1][0]=w;
//		X0[2][0]=k;
//		X0[3][0]=u;
//		X0[4][0]=r;
		
	
	
	
	
	 
	 //B阵
	 double[][]B=new double[6][5];
	 //B阵转置
	 double[][] Bt=new double[5][6];
	//系数阵
	 double[][] NBB=new double[6][6];
	 //系数阵的逆
	 double[][] NBB_1=new double[6][6];
	 //l阵(Q)
	 double[][] l=new double[6][1];
	 //观测值
	 double[][] L=new double[6][1];
	 
	
	 //待求5个参数改正数
	 double[][] xv=new double[5][1];
	 //平差矩阵W
	 double[][] W=new double[5][1];
	 //观测值改正数
	 double[][] V=new double[6][1];
	 //观测值最或然值
	 double[][] Q=new double[6][1];
	public RightPhoto(LeftPhoto lp) {
		this.leftPhoto=lp;
		setS_xyz();
	}

	//给x1,x2,...赋值
	 public void setS_xyz(){
//		 excelio.getXYfdata();
		 x1=excelio.rd[0][0];
		 y1=excelio.rd[0][1];
		 x2=excelio.rd[1][0];
		 y2=excelio.rd[1][1];
		 x3=excelio.rd[2][0];
		 y3=excelio.rd[2][1];
		 x4=excelio.rd[3][0];
		 y4=excelio.rd[3][1];
		 x5=excelio.rd[4][0];
		 y5=excelio.rd[4][1];
		 x6=excelio.rd[5][0];
		 y6=excelio.rd[5][1];
		 for(int i=0;i<6;i++)
			 for(int j=0;j<3;j++){
				if(j<2){
					S_xyz[i][j]=excelio.rd[i][j];
				}
				else
					S_xyz[i][j]=-excelio.f;
			 }
		 System.out.println("右像空间坐标：");
		 for (double[] d : S_xyz) {
			System.out.println(Arrays.toString(d));
		}
	 }
	
	//旋转矩阵
     public double[][] R2(){
    	R2[0][0]=Math.cos(X0[0][0])*Math.cos(X0[2][0])-Math.sin(X0[0][0])*Math.sin(X0[1][0])*Math.sin(X0[2][0]);//a1
    	R2[0][1]=-Math.cos(X0[0][0])*Math.sin(X0[2][0])-Math.sin(X0[0][0])*Math.sin(X0[1][0])*Math.cos(X0[2][0]);//a2
    	R2[0][2]=-Math.sin(X0[0][0])*Math.cos(X0[1][0]);                                    //a3
    	R2[1][0]=Math.cos(X0[1][0])*Math.sin(X0[2][0]);                                     //b1
    	R2[1][1]=Math.cos(X0[1][0])*Math.cos(X0[2][0]);                                     //b2
    	R2[1][2]=-Math.sin(X0[1][0]);                                                //b3
    	R2[2][0]=Math.sin(X0[0][0])*Math.cos(X0[2][0])+Math.cos(X0[0][0])*Math.sin(X0[1][0])*Math.sin(X0[2][0]); //c1
    	R2[2][1]=-Math.sin(X0[0][0])*Math.sin(X0[2][0])+Math.cos(X0[0][0])*Math.sin(X0[1][0])*Math.cos(X0[2][0]);//c2
    	R2[2][2]=Math.cos(X0[0][0])*Math.cos(X0[1][0]);                                     //c3
    	//旋转矩阵R2
    	System.out.println("旋转矩阵R2:");
    	for (double[] d : R2) {
			System.out.println(Arrays.toString(d));
		}
    	//R2转置
    	R2=new Matrix(R2).transpose().getArray();
    	//打印旋转矩阵
    	System.out.println("旋转矩阵R2的转置:");
    	for (double[] d :R2) {
			System.out.println(Arrays.toString(d));
		}
    	return R2;
    			
	}
    //右像空间辅助坐标的计算
    public double[][] rightXiangFuZB(){
    	
//    	for(int i=0;i<6;i++)
//    		for(int j=0;j<3;j++)
//    		    for(int k=0;k<3;k++){
//    			  XYZ[i][j]+=S_xyz[i][k]*R2[j][k];
//    			
//    		}
    	XYZ=new Matrix(S_xyz).times(new Matrix(R2)).getArray();
    	System.out.println("右像空间辅助坐标");
    	for (double[] d : XYZ) {
			System.out.println(Arrays.toString(d));
		}
    	return XYZ;
    			
    		
    	
    }
    //计算bx by bz 以及N1,N2
    public void bybzN1N2(){
    	
    	bx=excelio.Bx;
    	by=bx*X0[3][0];
    	bz=bx*X0[4][0];
    	System.out.println("摄影基线by="+by);
    	System.out.println("摄影基线bz="+bz);
    	N1[0]=(bx*XYZ[0][2]-bz*XYZ[0][0])/(leftPhoto.XYZ[0][0]*XYZ[0][2]-XYZ[0][0]*leftPhoto.XYZ[0][2]);
    	N2[0]=(bx*leftPhoto.XYZ[0][2]-bz*leftPhoto.XYZ[0][0])/(leftPhoto.XYZ[0][0]*XYZ[0][2]-XYZ[0][0]*leftPhoto.XYZ[0][2]);
    	N1[1]=(bx*XYZ[1][2]-bz*XYZ[1][0])/(leftPhoto.XYZ[1][0]*XYZ[1][2]-XYZ[1][0]*leftPhoto.XYZ[1][2]);
    	N2[1]=(bx*leftPhoto.XYZ[1][2]-bz*leftPhoto.XYZ[1][0])/(leftPhoto.XYZ[1][0]*XYZ[1][2]-XYZ[1][0]*leftPhoto.XYZ[1][2]);
    	N1[2]=(bx*XYZ[2][2]-bz*XYZ[2][0])/(leftPhoto.XYZ[2][0]*XYZ[2][2]-XYZ[2][0]*leftPhoto.XYZ[2][2]);
    	N2[2]=(bx*leftPhoto.XYZ[2][2]-bz*leftPhoto.XYZ[2][0])/(leftPhoto.XYZ[2][0]*XYZ[2][2]-XYZ[2][0]*leftPhoto.XYZ[2][2]);
    	N1[3]=(bx*XYZ[3][2]-bz*XYZ[3][0])/(leftPhoto.XYZ[3][0]*XYZ[3][2]-XYZ[3][0]*leftPhoto.XYZ[3][2]);
    	N2[3]=(bx*leftPhoto.XYZ[3][2]-bz*leftPhoto.XYZ[3][0])/(leftPhoto.XYZ[3][0]*XYZ[3][2]-XYZ[3][0]*leftPhoto.XYZ[3][2]);
    	N1[4]=(bx*XYZ[4][2]-bz*XYZ[4][0])/(leftPhoto.XYZ[4][0]*XYZ[4][2]-XYZ[4][0]*leftPhoto.XYZ[4][2]);
    	N2[4]=(bx*leftPhoto.XYZ[4][2]-bz*leftPhoto.XYZ[4][0])/(leftPhoto.XYZ[4][0]*XYZ[4][2]-XYZ[4][0]*leftPhoto.XYZ[4][2]);
    	N1[5]=(bx*XYZ[5][2]-bz*XYZ[5][0])/(leftPhoto.XYZ[5][0]*XYZ[5][2]-XYZ[5][0]*leftPhoto.XYZ[5][2]);
    	N2[5]=(bx*leftPhoto.XYZ[5][2]-bz*leftPhoto.XYZ[5][0])/(leftPhoto.XYZ[5][0]*XYZ[5][2]-XYZ[5][0]*leftPhoto.XYZ[5][2]);
    	//投影系数
    	System.out.println("投影系数:");
    	System.out.println("投影系数N1"+Arrays.toString(N1));
    	System.out.println("投影系数N2"+Arrays.toString(N2));
    }
   
    //平差
    public void pingCha(){
    	//给B赋值
    	for(int i=0;i<6;i++){
    		B[i][0]=-XYZ[i][0]*XYZ[i][1]*N2[i]/XYZ[i][2];
        	B[i][1]=-(XYZ[i][2]+Math.pow(XYZ[i][1], 2)/XYZ[i][2])*N2[i];
        	B[i][2]=XYZ[i][0]*N2[i];
        	B[i][3]=bx;
        	B[i][4]=-XYZ[i][1]*bx/XYZ[i][2];
    	}
    	
    	//L阵
    	for(int i=0;i<6;i++)
    	    for(int j=0;j<1;j++){
    		l[i][j]=leftPhoto.XYZ[i][1]*N1[i]-N2[i]*XYZ[i][1]-by;
    	}
    	
    	
    	//B阵转置
    	Bt=new Matrix(B).transpose().getArray();
    	System.out.println("Bt:");
    	for (double[] d : Bt) {
			System.out.println(Arrays.toString(d));
		}
    	//NBB
    	NBB=new Matrix(Bt).times(new Matrix(B)).getArray();
    	//矩阵求逆NBB_1
    	
    	System.out.println("NBB:");
    	for (double[] d : NBB) {
			System.out.println(Arrays.toString(d));
		}
    	NBB_1=new Matrix(NBB).inverse().getArray();
    	System.out.println("NBB_1:");
    	for (double[] d : NBB_1) {
			System.out.println(Arrays.toString(d));
		}
    	//W矩阵赋值
    	W=new Matrix(Bt).times(new Matrix(l)).getArray();
    	//待求参数改正数的矩阵
    	xv=new Matrix(NBB_1).times(new Matrix(W)).getArray();
    	
    	//改正数V
    	V=(new Matrix(B).times(new Matrix(xv)).minus(new Matrix(l))).getArray();
    	//观测值Q的最或然值L
    	Q=new Matrix(l).plus(new Matrix(V)).getArray();
    	//平差后的5个参数值
    	X0=new Matrix(X0).plus(new Matrix(xv)).getArray();
    	System.out.println("平差后待求的5个参数值：");
    	for (double[] d : X0) {
			System.out.println(Arrays.toString(d));
		}
    	System.out.println("系数改正数xv:");
    	for (double[] d : xv) {
			System.out.println(Arrays.toString(d));
		}
    	System.out.println("观测值改正数V:");
    	for (double[] d : V) {
			System.out.println(Arrays.toString(d));
		}
    	
    	System.out.println("l阵：");
    	for (double[] d : l) {
			System.out.println(Arrays.toString(d));
		}
    	
    }
    //精度评定,迭代
    int  jingDuPingding(){
    /*	//平差方程自由度为1
    	double δ0;
    	//精度，中误差
    	δ0=Math.sqrt(new Matrix(V).transpose().times(new Matrix(V)).get(0, 0));
    	System.out.println("中误差值是："+δ0);
    	return δ0;
    */
    	System.out.println("弧度转角度"+hudu_dufenmiao);
    	System.out.println("弧度改正数转角度改正数：");
    	double[][] xv_xv;
    	
    	xv_xv=new Matrix(xv).times(hudu_dufenmiao).getArray();
    	for (double[] d : xv_xv) {
			System.out.println(Arrays.toString(d));
		}
    	if(xv_xv[0][0]<1.0e-10&&xv_xv[1][0]<1.0e-10&&xv_xv[2][0]<1.0e-10&&xv_xv[3][0]<1.0e-10&&xv_xv[4][0]<1.0e-10)
    		return 0;
    	
    	else 
    		return 1;
			
		
    	
    		
    }
  
    
    
    

    public static void main(String[] args)throws Exception{
    	//用于存储txt数据
    	ArrayList<String[]> array=new ArrayList< String[]>();
    	//用于存储摄影测量坐标
    	ArrayList<double[][]> arrayd=new ArrayList<>();
    	//读取坐标文件
    	BufferedReader br=new BufferedReader(new FileReader("C://Users//ASUS//Desktop//毕业设计//相对定向//绝对定向控制点像平面坐标//95-96.txt"));
    	//写入文件
		BufferedWriter bw=new BufferedWriter(new FileWriter("C://Users//ASUS//Desktop//毕业设计//相对定向//摄影测量坐标//95-96.txt"));
		BufferedWriter bw2=new BufferedWriter(new FileWriter("C://Users//ASUS//Desktop//毕业设计//相对定向//相对定向待求参数//95-96.txt"));
    	
    	int i = 0;
    	RightPhoto rp=new RightPhoto(new LeftPhoto());
    	do {
//    		迭代10次
//    		if(i>10)
//    			break;
    		
    		rp.R2();
    		rp.rightXiangFuZB();
    		rp.bybzN1N2();
    		rp.pingCha();
    		i++;
    		System.out.println("第"+i+"次迭代");
    		
		} while (rp.jingDuPingding()==1);
    	System.out.println("迭代次数："+i);	
    	//旋转矩阵的转置
    	System.out.println("旋转矩阵R2的转置：");
    	for (double[] d : rp.R2) {
			System.out.println(Arrays.toString(d));
		}
    	System.out.println("投影系数N1.N2：");
    	System.out.println("N1="+Arrays.toString(rp.N1)+"   "+"N2="+Arrays.toString(rp.N2));
    	System.out.println("5个待求参数的最终值：");
//    	rp.X0=new Matrix(rp.X0).times(hudu_dufenmiao).getArray();
    	System.out.println("ψ:"+rp.X0[0][0]+"  w:"+rp.X0[1][0]+"  k:"+rp.X0[2][0]+"  u:"+rp.X0[3][0]+"  r:"+rp.X0[4][0]);
    	
    	//此处相对定向完
    
    	//下面批量处理坐标
    	String str=null;
    	br.readLine();//第一行不处理
    	
    	//读取txt文件中待测像平面坐标，并存储到list
    	while((str=br.readLine())!=null){
    		System.out.println(str);
    	    String[] arrystr=str.split("	");
    	    array.add(arrystr);
    	}
    
    	Iterator<String[]> it= array.iterator();
    	while(it.hasNext()){
    		double[][] ds=new double[3][1];
    		double[][] dl=new double[3][1];
    		double[][] dr=new double[3][1];
    		
    		
    		//像空间辅助坐标赋值
    		String[] string=it.next();//将list容器中的字符串数组地址复制给变量String
    		dl[0][0]=Double.valueOf(string[0]);
    		dl[1][0]=Double.valueOf(string[1]);
    		System.out.println("左像空间y:"+dl[1][0]);
    		dr[0][0]=Double.valueOf(string[2]);
    		dr[1][0]=Double.valueOf(string[3]);
    		System.out.println("右像空间y:"+dr[1][0]);
    		dr=excelio.set_JBC_xy(dr);
    		dl=excelio.set_JBC_xy(dl);
    		dl[2][0]=-excelio.f;
    		dr[2][0]=-excelio.f;
    		System.out.println("右像空间坐标：");
    		for (double[] d : dr) {
				System.out.println(Arrays.toString(d));
			}
    		System.out.println("左像空间坐标：");
    		for (double[] d : dl) {
				System.out.println(Arrays.toString(d));
			}

    		dr=new Matrix(rp.R2).transpose().times(new Matrix(dr)).getArray();
    		System.out.println("R2:");
    		for (double[] d : rp.R2) {
				System.out.println(Arrays.toString(d));
			}
    		double N1=(rp.bx*dr[2][0]-rp.bz*dr[0][0])/(dl[0][0]*dr[2][0]-dr[0][0]*dl[2][0]);
    		double N2=(rp.bx*dl[2][0]-rp.bz*dl[0][0])/(dl[0][0]*dr[2][0]-dr[0][0]*dl[2][0]);
    		System.out.println("N1="+N1+" N2="+N2);
    		ds[0][0]=excelio.m*N1*dl[0][0];
    		ds[1][0]=excelio.m*(N1*dl[1][0]+N2*dr[1][0]+rp.by)/2;
    		ds[2][0]=excelio.m*excelio.f+excelio.m*N1*dl[2][0];	
    		ds=new Matrix(ds).transpose().getArray();
    		arrayd.add(ds);
    		System.out.println("左像空间辅助坐标");
    		for (double[] ds2 : dl) {
				System.out.println(Arrays.toString(ds2));
			}
    		System.out.println("右像空间辅助坐标");
    		for (double[] ds2 : dr) {
				System.out.println(Arrays.toString(ds2));
			}
    		System.out.println("摄影测量坐标：");
    		for (double[] ds2 : ds) {
				System.out.println(Arrays.toString(ds2));
			}
    		
    		}
    	Iterator<double[][]> iterator=arrayd.iterator();
    	System.out.println("摄影测量坐标输入到记事本");
//		while(iterator.hasNext()){
//			
//			for (double[] ds2 : iterator.next()) {
//				System.out.println(Arrays.toString(ds2));
//				
//			}
//			
//    	}
    	//将摄影测量坐标输出到记事本
		while(iterator.hasNext()){
		    double[][] d=iterator.next();
			bw.write(d[0][0]+"\t");
			bw.write(d[0][1]+"\t");
			bw.write(d[0][2]+"\t");
			bw.write("\r\n");
		}
		
//    	//打印摄影坐标系下坐标
//    	System.out.println("打印摄影坐标系下坐标：");
//    	for (double[] ds2 : ds) {
//			System.out.println(Arrays.toString(ds2));
//		}
//    	//将数据写入到记事本
//    	//5个待求参数
    	bw2.write("ψ,w,k,u,r依次为:");
    	bw2.newLine();
    	for(int a=0;a<5;a++){
    		
    		bw2.write(Arrays.toString(rp.X0[a])+"\t");
    	
    	}
    	bw2.flush();
    	bw.flush();
    	bw.close();
    	br.close();
    	bw2.close();

   	
  	
    	
    	
  }

}

