package com.wsiz.ollamawrapper;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ollama")
public class OllamaController {

	private final OllamaGrpcClient ollamaGrpcClient;

	public OllamaController(OllamaGrpcClient ollamaGrpcClient) {
		this.ollamaGrpcClient = ollamaGrpcClient;
	}

	@GetMapping("/data")
	public String getData(@RequestParam String query) {
//		return ollamaGrpcClient.getData(query);
		return null;
	}

	@PostMapping("/data")
	public String postData(@RequestBody String query) {
//		return ollamaGrpcClient.postData(query);
		return null;
	}
}
