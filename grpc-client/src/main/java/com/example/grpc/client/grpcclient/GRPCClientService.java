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
		MatrixServiceGrpc.MatrixServiceBlockingStub stub
		 = MatrixServiceGrpc.newBlockingStub(channel);

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
	public String multiply(String m1, String m2){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
		.usePlaintext()
		.build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub
		 = MatrixServiceGrpc.newBlockingStub(channel);

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
		
		MatrixReply response = stub.multiplyBlock(request_M.build());
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

}
