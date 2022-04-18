package com.example.grpc.client.grpcclient;

import java.util.ArrayList;
import java.util.List;

import com.example.grpc.server.grpcserver.Mrow;
import com.example.grpc.server.grpcserver.PingRequest;
import com.example.grpc.server.grpcserver.PongResponse;
import com.example.grpc.server.grpcserver.PingPongServiceGrpc;
import com.example.grpc.server.grpcserver.MatrixRequest;
import com.example.grpc.server.grpcserver.MatrixReply;
import com.example.grpc.server.grpcserver.MatrixServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
@Service
public class GRPCClientService {
    public String ping() {
        	ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();        
		PingPongServiceGrpc.PingPongServiceBlockingStub stub
                = PingPongServiceGrpc.newBlockingStub(channel);        
		PongResponse helloResponse = stub.ping(PingRequest.newBuilder()
                .setPing("")
                .build());       
		channel.shutdown();        
		return helloResponse.getPong();
    }
    public String add(String m1, String m2){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
		.usePlaintext()
		.build();

		MatrixServiceGrpc.MatrixServiceBlockingStub stub = MatrixServiceGrpc.newBlockingStub(channel);

		MatrixRequest.Builder request_M = MatrixRequest.newBuilder();
		String[] m1_split = m1.split("\n");
		for(int i=0; i < m1_split.length; i++){
			String[] current_Row_Split = m1_split[i].split(" ");
			Mrow.Builder matrix_Row = Mrow.newBuilder();
			for(int j=0; j < current_Row_Split.length; j++){
				int temp =Integer.parseInt(current_Row_Split[j].substring(0,1));
				matrix_Row.addRow(temp);
			}
			request_M.addMat1(matrix_Row);
		}
		String[] m2_split = m2.split("\n");
		for(int i=0; i < m2_split.length; i++){
			String[] current_Row_Split = m2_split[i].split(" ");
			Mrow.Builder matrix_Row = Mrow.newBuilder();
			for(int j=0; j < current_Row_Split.length; j++){
				int temp =Integer.parseInt(current_Row_Split[j].substring(0,1));
				matrix_Row.addRow(temp);
			}
			request_M.addMat2(matrix_Row);
		}
		
		MatrixReply response = stub.addBlock(request_M.build());
		channel.shutdown();
		
		String result = "";
		for(int i=0; i < response.getMatList().size(); i++){
			List<Integer> currentRow = response.getMatList().get(i).getRowList();
			for(int j=0; j < currentRow.size(); j++){
				result += currentRow.get(j) + " ";
			}
			result += "<br/>";
		}
		return result;
    }
	public String multiply(String m1, String m2, String deadline){
		ManagedChannel[] channels = {
			ManagedChannelBuilder.forAddress("10.128.0.4", 9090).usePlaintext().build(), 
			ManagedChannelBuilder.forAddress("10.128.0.5", 9090).usePlaintext().build(),
			ManagedChannelBuilder.forAddress("10.128.0.6", 9090).usePlaintext().build(),
			ManagedChannelBuilder.forAddress("10.128.0.7", 9090).usePlaintext().build(),
			ManagedChannelBuilder.forAddress("10.128.0.8", 9090).usePlaintext().build(),
			ManagedChannelBuilder.forAddress("10.128.0.9", 9090).usePlaintext().build(),
			ManagedChannelBuilder.forAddress("10.128.0.17", 9090).usePlaintext().build(),
			ManagedChannelBuilder.forAddress("10.128.0.18", 9090).usePlaintext().build()
		};
		MatrixServiceGrpc.MatrixServiceBlockingStub stub1 = MatrixServiceGrpc.newBlockingStub(channels[0]);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub2 = MatrixServiceGrpc.newBlockingStub(channels[1]);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub3 = MatrixServiceGrpc.newBlockingStub(channels[2]);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub4 = MatrixServiceGrpc.newBlockingStub(channels[3]);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub5 = MatrixServiceGrpc.newBlockingStub(channels[4]);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub6 = MatrixServiceGrpc.newBlockingStub(channels[5]);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub7 = MatrixServiceGrpc.newBlockingStub(channels[6]);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub8 = MatrixServiceGrpc.newBlockingStub(channels[7]);

		
		String[] m1_split1 = m1.split("\n");
		String[] m2_split1 = m2.split("\n");
		//int servers_required = serverChecker(m1_split1.length);
		//convert String m1_split111 into 2d matrix
		int[][] m1_split_111 = new int[m1_split1.length][];
		int[][] m2_split_222 = new int[m2_split1.length][];
		
		for(int i=0; i < m1_split1.length; i++){

			String[] current_Row_Split = m1_split1[i].split(" ");
			m1_split_111[i] = new int[current_Row_Split.length];

			String[] current_Row_Split1 = m2_split1[i].split(" ");
			m2_split_222[i] = new int[current_Row_Split1.length];

			for(int j=0; j < current_Row_Split.length; j++){
				String row1_temp = current_Row_Split[j].replaceAll("\\s", "");
				String row2_temp = current_Row_Split1[j].replaceAll("\\s", "");
				m1_split_111[i][j] = Integer.parseInt(row1_temp);
				m2_split_222[i][j] = Integer.parseInt(row2_temp);
			}
		}
		//print m1_split_111
		/*for(int i=0; i < m1_split_111.length; i++){
			for(int j=0; j < m1_split_111[i].length; j++){
				System.out.print(m1_split_111[i][j] + " ");
			}
			System.out.println();
		}*/

		Integer[][][] matrix_1_3d = new Integer[4][m1_split_111.length/2][m1_split_111.length/2];
		Integer[][][] matrix_2_3d = new Integer[4][m2_split_222.length/2][m2_split_222.length/2];
		//divide array into chunks
		for(int i=0; i < m1_split_111.length/2; i++){
			for(int j=0; j < m1_split_111.length/2; j++){
				matrix_1_3d[0][i][j] = m1_split_111[i][j];
				matrix_1_3d[1][i][j] = m1_split_111[i][j+m1_split_111.length/2];
				matrix_1_3d[2][i][j] = m1_split_111[i+m1_split_111.length/2][j];
				matrix_1_3d[3][i][j] = m1_split_111[i+m1_split_111.length/2][j+m1_split_111.length/2];
				matrix_2_3d[0][i][j] = m2_split_222[i][j];
				matrix_2_3d[1][i][j] = m2_split_222[i][j+m2_split_222.length/2];
				matrix_2_3d[2][i][j] = m2_split_222[i+m2_split_222.length/2][j];
				matrix_2_3d[3][i][j] = m2_split_222[i+m2_split_222.length/2][j+m2_split_222.length/2];
			}
		}

		//print matrix_1_3d and matrix_2_3d
		/*for(int i=0; i < matrix_1_3d.length; i++){
			for(int j=0; j < matrix_1_3d[i].length; j++){
				for(int k=0; k < matrix_1_3d[i][j].length; k++){
					System.out.print(matrix_1_3d[i][j][k] + " ");
				}
				System.out.println();
			}
			System.out.println();
		} 
		for(int i=0; i < matrix_2_3d.length; i++){
			for(int j=0; j < matrix_2_3d[i].length; j++){
				for(int k=0; k < matrix_2_3d[i][j].length; k++){
					System.out.print(matrix_2_3d[i][j][k] + " ");
				}
				System.out.println();
			}
			System.out.println();
		}*/

		Integer[][][] result = new Integer[4][m1_split_111.length/2][m1_split_111.length/2];
        for (int i = 0;i<4;i++){
            for (int j = 0;j<m1_split_111.length/2;j++){
                for (int k = 0;k<m1_split_111.length/2;k++){
                    result[i][j][k] = 0;
                }
            }
        }

		//////////////////////////////////////////////////////////////00000000000000000000000000000000000
		MatrixRequest.Builder request_M = MatrixRequest.newBuilder();
		for(int i=0; i < matrix_1_3d[0].length; i++){
			Mrow.Builder matrix_Row = Mrow.newBuilder();
			Mrow.Builder matrix_Row1 = Mrow.newBuilder();
			for(int j=0; j < matrix_1_3d[0][i].length; j++){
				int temp = matrix_1_3d[0][i][j]; //a
				int temp1 = matrix_2_3d[0][i][j]; //e
				matrix_Row.addRow(temp);
				matrix_Row1.addRow(temp1);

			}
			request_M.addMat1(matrix_Row);
			request_M.addMat2(matrix_Row1);
		}
		MatrixReply response = stub1.multiplyBlock(request_M.build());
		
		for (int i = 0; i < response.getMatCount(); i++) {
            Mrow row = response.getMat(i);
            for (int j = 0; j < row.getRowCount(); j++) {
                result[0][i][j] += row.getRow(j);
            }
        }
		//////////////////////////////////////////////////////////////00000000000000000000000000000000000
		
		//////////////////////////////////////////////////////////////11111111111111111111111111111111111
		MatrixRequest.Builder request_M1 = MatrixRequest.newBuilder();
		for(int i=0; i < matrix_1_3d[0].length; i++){
			Mrow.Builder matrix_Row = Mrow.newBuilder();
			Mrow.Builder matrix_Row1 = Mrow.newBuilder();
			for(int j=0; j < matrix_1_3d[0][i].length; j++){
				int temp = matrix_1_3d[1][i][j]; //b
				int temp1 = matrix_2_3d[2][i][j]; //g
				matrix_Row.addRow(temp);
				matrix_Row1.addRow(temp1);

			}
			request_M1.addMat1(matrix_Row);
			request_M1.addMat2(matrix_Row1);
		}
		MatrixReply response1 = stub2.multiplyBlock(request_M1.build());
		//channel.shutdown();
		
		for (int i = 0; i < response1.getMatCount(); i++) {
            Mrow row = response1.getMat(i);
            for (int j = 0; j < row.getRowCount(); j++) {
                result[0][i][j] += row.getRow(j);
            }
        }
		//////////////////////////////////////////////////////////////11111111111111111111111111111111111

		//////////////////////////////////////////////////////////////22222222222222222222222222222222222
		MatrixRequest.Builder request_M2 = MatrixRequest.newBuilder();
		for(int i=0; i < matrix_1_3d[0].length; i++){
			Mrow.Builder matrix_Row = Mrow.newBuilder();
			Mrow.Builder matrix_Row1 = Mrow.newBuilder();
			for(int j=0; j < matrix_1_3d[0][i].length; j++){
				int temp = matrix_1_3d[0][i][j]; //a
				int temp1 = matrix_2_3d[1][i][j]; //f
				matrix_Row.addRow(temp);
				matrix_Row1.addRow(temp1);

			}
			request_M2.addMat1(matrix_Row);
			request_M2.addMat2(matrix_Row1);
		}
		MatrixReply response2 = stub3.multiplyBlock(request_M2.build());
		//channel.shutdown();
		
		for (int i = 0; i < response2.getMatCount(); i++) {
            Mrow row = response2.getMat(i);
            for (int j = 0; j < row.getRowCount(); j++) {
                result[1][i][j] += row.getRow(j);
            }
        }
		//////////////////////////////////////////////////////////////22222222222222222222222222222222222

		//////////////////////////////////////////////////////////////33333333333333333333333333333333333
		MatrixRequest.Builder request_M3 = MatrixRequest.newBuilder();
		for(int i=0; i < matrix_1_3d[0].length; i++){
			Mrow.Builder matrix_Row = Mrow.newBuilder();
			Mrow.Builder matrix_Row1 = Mrow.newBuilder();
			for(int j=0; j < matrix_1_3d[0][i].length; j++){
				int temp = matrix_1_3d[1][i][j]; //b
				int temp1 = matrix_2_3d[3][i][j]; //h
				matrix_Row.addRow(temp);
				matrix_Row1.addRow(temp1);

			}
			request_M3.addMat1(matrix_Row);
			request_M3.addMat2(matrix_Row1);
		}
		MatrixReply response3 = stub4.multiplyBlock(request_M3.build());
		//channel.shutdown();
		
		for (int i = 0; i < response3.getMatCount(); i++) {
            Mrow row = response3.getMat(i);
            for (int j = 0; j < row.getRowCount(); j++) {
                result[1][i][j] += row.getRow(j);
            }
        }
		//////////////////////////////////////////////////////////////33333333333333333333333333333333333

		//////////////////////////////////////////////////////////////44444444444444444444444444444444444
		MatrixRequest.Builder request_M4 = MatrixRequest.newBuilder();
		for(int i=0; i < matrix_1_3d[0].length; i++){
			Mrow.Builder matrix_Row = Mrow.newBuilder();
			Mrow.Builder matrix_Row1 = Mrow.newBuilder();
			for(int j=0; j < matrix_1_3d[0][i].length; j++){
				int temp = matrix_1_3d[2][i][j]; //c
				int temp1 = matrix_2_3d[0][i][j]; //e
				matrix_Row.addRow(temp);
				matrix_Row1.addRow(temp1);
			}
			request_M4.addMat1(matrix_Row);
			request_M4.addMat2(matrix_Row1);
		}
		MatrixReply response4 = stub5.multiplyBlock(request_M4.build());
		
		for (int i = 0; i < response4.getMatCount(); i++) {
            Mrow row = response4.getMat(i);
            for (int j = 0; j < row.getRowCount(); j++) {
                result[2][i][j] += row.getRow(j);
            }
        }
		//////////////////////////////////////////////////////////////44444444444444444444444444444444444

		//////////////////////////////////////////////////////////////55555555555555555555555555555555555
		MatrixRequest.Builder request_M5 = MatrixRequest.newBuilder();
		for(int i=0; i < matrix_1_3d[0].length; i++){
			Mrow.Builder matrix_Row = Mrow.newBuilder();
			Mrow.Builder matrix_Row1 = Mrow.newBuilder();
			for(int j=0; j < matrix_1_3d[0][i].length; j++){
				int temp = matrix_1_3d[3][i][j]; //d
				int temp1 = matrix_2_3d[2][i][j]; //g
				matrix_Row.addRow(temp);
				matrix_Row1.addRow(temp1);
			}
			request_M5.addMat1(matrix_Row);
			request_M5.addMat2(matrix_Row1);
		}
		MatrixReply response5 = stub6.multiplyBlock(request_M5.build());
		
		for (int i = 0; i < response5.getMatCount(); i++) {
            Mrow row = response5.getMat(i);
            for (int j = 0; j < row.getRowCount(); j++) {
                result[2][i][j] += row.getRow(j);
            }
        }
		//////////////////////////////////////////////////////////////55555555555555555555555555555555555

		//////////////////////////////////////////////////////////////66666666666666666666666666666666666
		MatrixRequest.Builder request_M6 = MatrixRequest.newBuilder();
		for(int i=0; i < matrix_1_3d[0].length; i++){
			Mrow.Builder matrix_Row = Mrow.newBuilder();
			Mrow.Builder matrix_Row1 = Mrow.newBuilder();
			for(int j=0; j < matrix_1_3d[0][i].length; j++){
				int temp = matrix_1_3d[2][i][j]; //c
				int temp1 = matrix_2_3d[1][i][j]; //f
				matrix_Row.addRow(temp);
				matrix_Row1.addRow(temp1);
			}
			request_M6.addMat1(matrix_Row);
			request_M6.addMat2(matrix_Row1);
		}
		MatrixReply response6 = stub7.multiplyBlock(request_M6.build());
		
		for (int i = 0; i < response6.getMatCount(); i++) {
            Mrow row = response6.getMat(i);
            for (int j = 0; j < row.getRowCount(); j++) {
                result[3][i][j] += row.getRow(j);
            }
        }
		//////////////////////////////////////////////////////////////66666666666666666666666666666666666

		//////////////////////////////////////////////////////////////77777777777777777777777777777777777
		MatrixRequest.Builder request_M7 = MatrixRequest.newBuilder();
		for(int i=0; i < matrix_1_3d[0].length; i++){
			Mrow.Builder matrix_Row = Mrow.newBuilder();
			Mrow.Builder matrix_Row1 = Mrow.newBuilder();
			for(int j=0; j < matrix_1_3d[0][i].length; j++){
				int temp = matrix_1_3d[3][i][j]; //d
				int temp1 = matrix_2_3d[3][i][j]; //h
				matrix_Row.addRow(temp);
				matrix_Row1.addRow(temp1);
			}
			request_M7.addMat1(matrix_Row);
			request_M7.addMat2(matrix_Row1);
		}
		MatrixReply response7 = stub8.multiplyBlock(request_M7.build());
		
		for (int i = 0; i < response7.getMatCount(); i++) {
            Mrow row = response7.getMat(i);
            for (int j = 0; j < row.getRowCount(); j++) {
                result[3][i][j] += row.getRow(j);
            }
        }	
		//////////////////////////////////////////////////////////////77777777777777777777777777777777777
		channels[0].shutdown();
		channels[1].shutdown();
		channels[2].shutdown();
		channels[3].shutdown();
		channels[4].shutdown();
		channels[5].shutdown();
		channels[6].shutdown();
		channels[7].shutdown();
		//print result
		String part1 = "";
		String part2 = "";
		String part3 = "";
		String part4 = "";
		for(int j=0; j < result[0].length; j++){
			for(int k=0; k < result[0][j].length; k++){
					part1 = part1 + result[0][j][k] + " ";
					part2 = part2 + result[1][j][k] + " ";
					part3 = part3 + result[2][j][k] + " ";
					part4 = part4 + result[3][j][k] + " ";
			}
			part1 = part1 + "\n";
			part2 = part2 + "\n";
			part3 = part3 + "\n";
			part4 = part4 + "\n";
		}
		String finalpart = part1+part3;
		String finalpart1 = part2+part4;
		System.out.print(finalpart);
		System.out.print(finalpart1);
		return "<table style='width:10%'><tr><td><pre>"+finalpart+"</pre></td><td><pre>"+finalpart1+"</pre></td></tr></table>";
	}
}
