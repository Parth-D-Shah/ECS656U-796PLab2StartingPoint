package com.example.grpc.client.grpcclient;

import java.io.*;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUpload {
	public static String uploadDirectory = System.getProperty("user.dir") + "/uploadedmatrixfiles";

	/*
	 * @RequestMapping("/")
	 * public String UploadPage(Model model) {
	 * return "uploadview";
	 * }
	 */
	@RequestMapping("/")
		 public String upload(Model model)
		 {
			 return "upload";
		 }
	@RequestMapping("/upload")
	public String upload(Model model, @RequestParam("matrices") MultipartFile[] files) throws IOException {
		try {
			if (!files[0].isEmpty()||!files[1].isEmpty()){
				if (files.length > 2 || files.length < 2){
					model.addAttribute("message", "Please upload only two files");
					return "uploadchecker";
				}
				byte[] matrix_A = files[0].getBytes();
				byte[] matrix_B = files[1].getBytes();
				String matrix_A_String = new String(matrix_A);
				String matrix_B_String = new String(matrix_B);
				model.addAttribute("matrixA", matrix_A_String);
				model.addAttribute("matrixB", matrix_B_String);
				String[] matrix_A_Rows = matrix_A_String.split("\n"); // split the string into rows
				String[] matrix_B_Rows = matrix_B_String.split("\n");
				
				ArrayList<Integer> matrix_A_Columns = new ArrayList<Integer>();
				ArrayList<Integer> matrix_B_Columns = new ArrayList<Integer>();
				for(int i = 0; i < matrix_A_Rows.length; i++){
					matrix_A_Columns.add(matrix_A_Rows[i].length() - matrix_A_Rows[i].replace(" ","").length());
				}
				for(int i = 0; i < matrix_B_Rows.length; i++){
					matrix_B_Columns.add(matrix_B_Rows[i].length() - matrix_B_Rows[i].replace(" ","").length());
				}
				for(int i = 0; i < matrix_A_Columns.size(); i++ )
				{
					if(matrix_A_Columns.get(i) != matrix_A_Columns.get(0)){
						model.addAttribute("file_data", "Please upload a valid matrix A");
						return "uploadchecker";
					}
				}
				if (matrix_A_Rows.length != matrix_A_Rows[0].split(" ").length){
					model.addAttribute("file_data", "matrix A is not square matrix");
					return "uploadchecker";
				}
				for(int i = 0; i < matrix_B_Columns.size(); i++ )
				{
					if(matrix_B_Columns.get(i) != matrix_B_Columns.get(0)){
						model.addAttribute("file_data", "Please upload a valid matrix B");
						return "uploadchecker";
					}
				}
				if (matrix_B_Rows.length != matrix_B_Rows[0].split(" ").length){
					model.addAttribute("file_data", "matrix B is not square matrix");
					return "uploadchecker";
				}
				if (matrix_A_Rows.length != matrix_B_Rows.length){
					model.addAttribute("file_data", "matrix A and matrix B are not compatible");
					return "uploadchecker";
				}
			}
			else{
				model.addAttribute("message", "The file is empty");
				return "uploadchecker";
			}	
		} catch (Exception e) {
			model.addAttribute("message", "Please upload a file with valid characters");
			return "uploadchecker";
		}
		StringBuilder fileNames = new StringBuilder();
		for (MultipartFile file : files) {
			Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
			fileNames.append(file.getOriginalFilename() + " ");
			try {
				Files.write(fileNameAndPath, file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("message", "Successfully uploaded files " + fileNames.toString());
		return "uploadsuccess";
	}
}
