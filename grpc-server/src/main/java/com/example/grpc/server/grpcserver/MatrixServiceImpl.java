package com.example.grpc.server.grpcserver;

import java.util.*;
import java.util.List;

import io.grpc.internal.Stream;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MatrixServiceImpl extends MatrixServiceGrpc.MatrixServiceImplBase {
	@Override
	public void addBlock(MatrixRequest request, StreamObserver<MatrixReply> reply) {
		MatrixReply.Builder response = MatrixReply.newBuilder();
		List<Mrow> m1 = request.getMat1List();
		List<Mrow> m2 = request.getMat2List();

		for (int i = 0; i < m1.size(); i++)
		{
			Mrow.Builder Mrow_object = Mrow.newBuilder();
			List<Integer> current_row_matrix1 = m1.get(i).getRowList();
			List<Integer> current_row_matrix2 = m2.get(i).getRowList();

			for (int j = 0; j < current_row_matrix1.size(); j++)
			{
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

		Integer m3[][] = new Integer[m1.size()][m2.size()];
		int Rsize = m1.get(1).getRowList().size();

		for (int i = 0; i < m1.size(); i++)
		{
			for (int j = 0; j < Rsize; j++)
			{
				m3[i][j] = 0;
				List<Integer> matrix_1_row = m1.get(i).getRowList();
				for (int z = 0; z < Rsize; z++)
				{
					List<Integer> matrix_2_row = m2.get(z).getRowList();
					m3[i][j] = m3[i][j] + matrix_1_row.get(z) * matrix_2_row.get(j);
				}
			}
		}
		// convert 2d array to list of mrow
		for (int i = 0; i < m3.length; i++)
		{
			Mrow.Builder Mrow_object = Mrow.newBuilder();
			for (int j = 0; j < m3[i].length; j++)
			{
				Mrow_object.addRow(m3[i][j]);
			}
			response.addMat(Mrow_object);
		}

		reply.onNext(response.build());
		reply.onCompleted();
	}
}