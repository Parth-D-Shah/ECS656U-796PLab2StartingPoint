package com.example.grpc.client.grpcclient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUpload{
  public static String uploadDirectory = System.getProperty("user.dir")+"/uploadedmatrixfiles";
/*  @RequestMapping("/")
  public String UploadPage(Model model) {
	  return "uploadview";
  }*/
  @RequestMapping("/upload")
  public String upload(Model model,@RequestParam("matrices") MultipartFile[] files) throws IOException{
	  if(files.length > 2 || files.length < 2){
		  model.addAttribute("message", "Please upload only two files");
		  return "uploadchecker";
	  }
	  byte[] file1 = files[0].getBytes();
	  byte[] file2 = files[1].getBytes();
	  
	  String file11 = new String(file1); 
      String file22 = new String(file2);
	  String[] file_1_matrix_rows = file11.split("/n");
	  String[] file_2_matrix_rows = file22.split("/n");
	  String[] matrix_column =  file_1_matrix_rows[0].split(" ");
	  String[] matrix_column =  file_1_matrix_rows[0].split(" ");
	  StringBuilder fileNames = new StringBuilder();
	  for (MultipartFile file : files){
		  Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
		  fileNames.append(file.getOriginalFilename()+" ");
		  try {
			Files.write(fileNameAndPath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	  model.addAttribute("message", "Successfully uploaded files "+fileNames.toString());
	  return "uploadchecker";
  }
}
