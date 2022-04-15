package com.example.grpc.server.grpcserver;

import java.util.List;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MatrixServiceImpl extends MatrixServiceGrpc.MatrixServiceImplBase {
	@Override
	public void addBlock(MatrixRequest request, StreamObserver<MatrixReply> reply) {
		MatrixReply.Builder response = MatrixReply.newBuilder();
		List<Mrow> m1 = request.getMat1List();
		List<Mrow> m2 = request.getMat2List();

		for (int i = 0; i < m1.size(); i++) {
			Mrow.Builder Mrow_object = Mrow.newBuilder();
			List<Integer> current_row_matrix1 = m1.get(i).getRowList();
			List<Integer> current_row_matrix2 = m2.get(i).getRowList();

			for (int j = 0; j < current_row_matrix1.size(); j++) {
				Integer m1_item = current_row_matrix1.get(j);
				Integer m2_item = current_row_matrix2.get(j);
				Mrow_object.addRow(m1_item + m2_item);
			}
			response.addMat(Mrow_object);
		}

		reply.onNext(response.build());
		reply.onCompleted();
	}

	@Override
	public void multiplyBlock(MatrixRequest request, StreamObserver<MatrixReply> reply) {
		MatrixReply.Builder response = MatrixReply.newBuilder();
		List<Mrow> m1 = request.getMat1List();
		List<Mrow> m2 = request.getMat2List();

		for (int i = 0; i < m1.size(); i++) {
			Mrow.Builder Mrow_object = Mrow.newBuilder();
			List<Integer> current_row_matrix1 = m1.get(i).getRowList();
			List<Integer> current_row_matrix2 = m2.get(i).getRowList();

			for (int j = 0; j < current_row_matrix1.size(); j++) {
				Integer m1_item = current_row_matrix1.get(j);
				Integer m2_item = current_row_matrix2.get(j);
				Mrow_object.addRow(m1_item + m2_item);
			}
			response.addMat(Mrow_object);
		}

		reply.onNext(response.build());
		reply.onCompleted();
	}
}
