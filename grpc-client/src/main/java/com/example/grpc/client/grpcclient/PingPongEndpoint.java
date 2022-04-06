package com.example.grpc.client.grpcclient;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Controller
public class PingPongEndpoint{

	GRPCClientService grpcClientService;
	@Autowired
    	public PingPongEndpoint(GRPCClientService grpcClientService) {
        	this.grpcClientService = grpcClientService;
    	}
	@GetMapping("/ping")
			public String ping(Model model) {
			    model.addAttribute("response", grpcClientService.ping());
			       	return "upload";
			}
	@GetMapping("/add")
			public String add() {
					return grpcClientService.add();
		  }

	@GetMapping("/")
		 public String upload(Model model)
		 {
			 return "upload";
		 }
	@PostMapping("/")
	public String handleFileUpload(@RequestParam("matrixA") MultipartFile matrixA, @RequestParam("matrixB") MultipartFile matrixB, RedirectAttributes redirectAttributes) {

		String contentA = new String();
		String contentB = new String(); 
		try {
				contentA = new String(matrixA.getBytes());
				contentB = new String(matrixB.getBytes());
		} catch (IOException e) {
				System.out.println("Can't read file input stream");
		}

		contentA = contentA.substring(0, contentA.length()-1);
		contentB = contentA.substring(0, contentA.length()-1);
		String[] split_contentA = contentA.split(";");
		String[] split_contentB = contentB.split(";");
		int length = split_contentA.length;

		int[][] inputA = new int[length][length];
		int[][] inputB = new int[length][length];
		for (int i=0; i < length; i++) {
				split_contentA[i] = split_contentA[i].trim();
				split_contentB[i] = split_contentB[i].trim();
				String[] single_intA = split_contentA[i].split(", ");
				String[] single_intB = split_contentB[i].split(", ");

				for (int j=0; j < length; j++) {
						inputA[i][j] = Integer.valueOf(single_intA[j]);
						inputB[i][j] = Integer.valueOf(single_intB[j]);
				}
		}

		if (length < 4) {
				throw new MatrixTooSmallException();
		}
		else if (inputA.length != inputB.length) {
				//throw new InputMatricesNotSameSizeException();
				throw new RuntimeException("This was thrown intentionally");
		}

		int[][] intResult = new int[length][length];
		String returnResult = new String();

		if ((length != 0) && ((length & (length - 1)) == 0)) {
				intResult = grpcClientService.matrixMult(inputA, inputB);
				String result = Arrays.deepToString(intResult);
				redirectAttributes.addFlashAttribute("message", result);
				returnResult = "redirect:/";
		}
		else {
				throw new MatrixNotAPowerOfTwoException();
		}
		return returnResult;
	}
}
