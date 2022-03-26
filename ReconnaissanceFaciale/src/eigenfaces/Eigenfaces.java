package eigenfaces;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class Eigenfaces {
	// récupérer le visage moyen
	// phiI = vecteur image i - visage moyen
	double[] phi1 = new double[960*960];
	int taille; // donne le nombre d'images et donc le nombre de phiI
	double[][] A = new double[960*960][taille];
	
	public static double[][] transposition(double[][] M) {
		int n = M.length;
		int m = M[0].length;
		double[][] TM = new double[m][n];
		for(int i=0; i < n; i++) {
			for(int j=0; j < m; j++) {
				TM[j][i]=M[i][j];
			}
		}
		return TM;
	}
	double[][] TA = transposition(A);
	
	public static double[][] produit(double[][] M, double[][] N) {
		double[][] P = new double[M.length][N[0].length];
		if (M[0].length == N.length) {
			for (int i = 0; i < M.length; i++) {
				for (int j = 0; j < N[0].length; j++) {
					P[i][j] = 0;
					for (int k=0; k < N.length; k++) {
						P[i][j]+=M[i][k]*N[k][j];
					}
				}
			}
			return P;
		}
		else {
			System.out.println("Problème de dimensions");
			return P;
		}
	}
	
	double [][] C = produit(A, TA); 
    
	// Renvoie les valeurs singulières de M qui correspondent aux valeurs propres pour C qui hermitienne semi-positive (car symétrique réelle à coefficients positifs
	public static double[] valeursP(double[][] M) {
		RealMatrix m = MatrixUtils.createRealMatrix(M);
		SingularValueDecomposition svd = new SingularValueDecomposition(m);
		double[] v = svd.getSingularValues();
		return v;
	}
   
    public static RealMatrix u(double[][] M) {
    	RealMatrix m = MatrixUtils.createRealMatrix(M);
    	SingularValueDecomposition svd = new SingularValueDecomposition(m);
    	RealMatrix U = svd.getU();
    	return U;
    }
	
    public static RealMatrix v(double[][] M) {
    	RealMatrix m = MatrixUtils.createRealMatrix(M);
    	SingularValueDecomposition svd = new SingularValueDecomposition(m);
    	RealMatrix V = svd.getUT();
    	return V;
    }
    
    public static RealMatrix d(double[][] M) {
    	RealMatrix m = MatrixUtils.createRealMatrix(M);
    	SingularValueDecomposition svd = new SingularValueDecomposition(m);
    	RealMatrix S = svd.getS();
    	return S;
    }
    
    public static void main(String[] args) {
        double[][] M = { {1,0,1}, {0,1,0}, {1,0,1} };
    	double[] vp = valeursP(M);
    	RealMatrix U = u(M);
    	RealMatrix S = d(M);
    	int n = U.getRowDimension();
    	for (int i=0; i < vp.length; i++) {
    		System.out.println("Valeur propre = "+vp[i]);
    	}
    	RealMatrix TU = v(M);
    	RealMatrix res = U.multiply(S).multiply(TU);
    	for (int i=0; i < n; i++) {
    		for (int j=0; j < n;j++) {
    			//System.out.println("Coefficient de U en "+i+","+j+" = "+U.getEntry(i, j));
    			//System.out.println("Coefficient de TU en "+i+","+j+" = "+TU.getEntry(i, j));
    			System.out.println("Coefficient de M en "+i+","+j+" = "+res.getEntry(i, j));
    		}
    	}
    }
    
}
