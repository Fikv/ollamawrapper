package com.wsiz.ollamawrapper;

//import com.example.ollama.OllamaServiceGrpc;
//import com.example.ollama.OllamaRequest;
//import com.example.ollama.OllamaResponse;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class OllamaGrpcClient {

//	private final OllamaServiceGrpc.OllamaServiceBlockingStub ollamaServiceStub;
//
//	public OllamaGrpcClient() {
//		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
//				.usePlaintext()
//				.build();
//		ollamaServiceStub = OllamaServiceGrpc.newBlockingStub(channel);
//	}
//
//	public String getData(String query) {
//		OllamaRequest request = OllamaRequest.newBuilder().setQuery(query).build();
//		OllamaResponse response = ollamaServiceStub.getData(request);
//		return response.getResult();
//	}
//
//	public String postData(String query) {
//		OllamaRequest request = OllamaRequest.newBuilder().setQuery(query).build();
//		OllamaResponse response = ollamaServiceStub.postData(request);
//		return response.getResult();
//	}
}

